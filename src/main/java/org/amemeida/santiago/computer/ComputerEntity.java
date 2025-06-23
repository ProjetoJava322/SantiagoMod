package org.amemeida.santiago.computer;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.CrafterBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.amemeida.santiago.components.TextContent;
import org.amemeida.santiago.file.runner.PythonRunner;
import org.amemeida.santiago.registry.blocks.ModBlockEntities;
import org.amemeida.santiago.registry.blocks.ModBlocks;
import org.amemeida.santiago.registry.items.ModComponents;
import org.amemeida.santiago.util.ImplementedInventory;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @see CrafterBlockEntity
 */

public class ComputerEntity extends BlockEntity implements ImplementedInventory, ExtendedScreenHandlerFactory<ComputerEntity.ComputerData> {
    public static final int IO_SLOTS = 4;

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(IO_SLOTS * 2 + 1, ItemStack.EMPTY);
    private OutputMode output;
    private ResultMode result;
    private final PropertyDelegate propertyDelegate;

    public ComputerEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.COMPUTER, pos, state);

        this.output = OutputMode.COMPARE;
        this.result = ResultMode.AND;

        propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> output.ordinal();
                    case 1 -> result.ordinal();
                    default -> throw new IndexOutOfBoundsException(index);
                };
            }

            @Override
            public void set(int index, int value) {
                markDirty();

                switch (index) {
                    case 0 -> output = OutputMode.values()[value];
                    case 1 -> result = ResultMode.values()[value];
                    default -> throw new IndexOutOfBoundsException(index);
                }
            }

            @Override
            public int size() {
                return 2;
            }
        };
    }

    public enum OutputMode {
        WRITE,
        COMPARE;

        public OutputMode cycleNext() {
            return OutputMode.values()[(this.ordinal() + 1) % OutputMode.values().length];
        }

        @Override
        public String toString() {
            var name = super.toString();
            return name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
        }

        public static final PacketCodec<PacketByteBuf, OutputMode> PACKET_CODEC = PacketCodec.tuple(
                PacketCodecs.INTEGER, Enum::ordinal, (a) -> OutputMode.values()[a]
        );
    }

    public enum ResultMode {
        AND,
        OR;

        public ResultMode cycleNext() {
            return ResultMode.values()[(this.ordinal() + 1) % ResultMode.values().length];
        }

        @Override
        public String toString() {
            var name = super.toString();
            return name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
        }

        public static final PacketCodec<PacketByteBuf, ResultMode> PACKET_CODEC = PacketCodec.tuple(
                PacketCodecs.INTEGER, Enum::ordinal, (a) -> ResultMode.values()[a]
        );
    }

    public record ComputerData(BlockPos pos, ComputerEntity.OutputMode output, ComputerEntity.ResultMode result) {
        public static final PacketCodec<? super PacketByteBuf, ComputerData> PACKET_CODEC = PacketCodec.tuple(
                BlockPos.PACKET_CODEC, ComputerData::pos,
                OutputMode.PACKET_CODEC, ComputerData::output,
                ResultMode.PACKET_CODEC, ComputerData::result,
                ComputerData::new
        );
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public ComputerData getScreenOpeningData(ServerPlayerEntity serverPlayerEntity) {
        return new ComputerData(this.getPos(), output, result);
    }

    public interface TestCase {
        boolean run() throws PythonRunner.RunningException;
    }

    public List<TestCase> testCases(ServerWorld world) {
        var floppy = this.inventory.getFirst();

        if (floppy.isEmpty() || !floppy.contains(ModComponents.SCRIPT)) {
            return List.of();
        }

        var comp = floppy.get(ModComponents.SCRIPT);
        assert comp != null;

        List<TestCase> testCases = new ArrayList<>();

        var hasIO = hasIO();

        for (int i = 1; i < IO_SLOTS * 2; i += 2) {
            var slot = i;

            if (hasIO && (this.inventory.get(slot).isEmpty() &&
                this.inventory.get(slot + 1).isEmpty())) {
                continue;
            }

            testCases.add(() -> {
                var in = getIO(this.inventory.get(slot));
                var out = this.inventory.get(slot + 1);

                var text = comp.getScript().runScript(in);
                System.out.println(text);

                if (output == OutputMode.COMPARE && !out.isEmpty()) {
                    return getIO(out).equals(text);
                }

                if (output == OutputMode.WRITE && !out.isEmpty()) {
                    var outIO = TextContent.get(out);
                    assert outIO != null;
                    outIO.setComponent(text, out);
                }

                return true;
            });

            if (slot == 1 && !hasIO) {
                break;
            }
        }

        assert hasIO || testCases.size() == 1;
        return Collections.unmodifiableList(testCases);
    }

    private static String getIO(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return "";
        }

        var io = TextContent.get(stack);
        assert io != null;
        return io.text();
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable(ModBlocks.COMPUTER.getTranslationKey());
    }

    @Override
    public void onBlockReplaced(BlockPos pos, BlockState oldState){
        ItemScatterer.spawn(world, pos, (this));
        super.onBlockReplaced(pos, oldState);
    }

    public boolean hasDisk() {
        return inventory.getFirst().contains(ModComponents.SCRIPT);
    }

    public boolean hasIO() {
        return !inventory.stream().skip(1)
                .allMatch(ItemStack::isEmpty);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        nbt.putInt("output", this.output.ordinal());
        nbt.putInt("result", this.output.ordinal());

        Inventories.writeNbt(nbt, inventory, registryLookup);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);

        nbt.getInt("output").ifPresent((nbtInt) -> {
            this.output = OutputMode.values()[nbtInt];
        });

        nbt.getInt("result").ifPresent((nbtInt) -> {
            this.result = ResultMode.values()[nbtInt];
        });

        Inventories.readNbt(nbt, inventory, registryLookup);
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new ComputerScreenHandler(syncId, playerInventory, this, propertyDelegate,
                this.getScreenOpeningData((ServerPlayerEntity) player));
    }
}
