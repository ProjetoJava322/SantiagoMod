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
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.amemeida.santiago.file.Script;
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

public class ComputerEntity extends BlockEntity implements ImplementedInventory, ExtendedScreenHandlerFactory<ComputerScreenHandler.ComputerData> {
    public static final int IO_SLOTS = 4;

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(IO_SLOTS * 2 + 1, ItemStack.EMPTY);
    private boolean write;
    private boolean and;
    private final PropertyDelegate propertyDelegate;

    public ComputerEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.COMPUTER, pos, state);

        propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> write ? 1 : 0;
                    case 1 -> and ? 1 : 0;
                    default -> throw new IndexOutOfBoundsException(index);
                };
            }

            @Override
            public void set(int index, int value) {
                markDirty();

                switch (index) {
                    case 0 -> write = value == 1;
                    case 1 -> and = value == 1;
                    default -> throw new IndexOutOfBoundsException(index);
                }
            }

            @Override
            public int size() {
                return 2;
            }
        };
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public ComputerScreenHandler.ComputerData getScreenOpeningData(ServerPlayerEntity serverPlayerEntity) {
        return new ComputerScreenHandler.ComputerData(this.getPos(), write, and);
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

        Script.setServer(world.getServer());
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

                if (!write && !out.isEmpty()) {
                    return getIO(out).equals(text);
                }

                if (write && !out.isEmpty()) {
                    var outIO = out.get(ModComponents.IO);
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

        var io = stack.get(ModComponents.IO);

        if (io == null) {
            return "";
        }

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

    public boolean hasIO() {
        return !inventory.stream().skip(1)
                .allMatch(ItemStack::isEmpty);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        nbt.putBoolean("write", this.write);
        nbt.putBoolean("and", this.and);

        System.out.println("GOODBYE. " + nbt.getBoolean("write").get());

        Inventories.writeNbt(nbt, inventory, registryLookup);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);

        nbt.getBoolean("write").ifPresent((nbtBoolean) -> {
            this.write = nbtBoolean;
            System.out.println("FOUND YOU " + nbtBoolean);
        });

        nbt.getBoolean("and").ifPresent((nbtBoolean) -> {
            this.and = nbtBoolean;
        });

        Inventories.readNbt(nbt, inventory, registryLookup);
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new ComputerScreenHandler(syncId, playerInventory, this, propertyDelegate,
                this.getScreenOpeningData((ServerPlayerEntity) player));
    }
}
