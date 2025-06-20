package org.amemeida.santiago.santiaguita.revolution_table;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.amemeida.santiago.registry.blocks.ModBlockEntities;
import org.amemeida.santiago.registry.blocks.ModBlocks;
import org.amemeida.santiago.registry.items.ModItems;
import org.amemeida.santiago.util.ImplementedInventory;
import org.jetbrains.annotations.Nullable;

public class RevolutionTableBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory<BlockPos>, ImplementedInventory {

    private final DefaultedList<ItemStack> INVENTORY = DefaultedList.ofSize(17, ItemStack.EMPTY);
    private static final int INPUT_GRID_START = 0;
    private static final int INPUT_GRID_END = 15;
    private static final int OUTPUT_SLOT = 16;

    public RevolutionTableBlockEntity( BlockPos pos, BlockState state) {
        super(ModBlockEntities.REVOLUTION_TABLE, pos, state);
    }

    @Override
    public BlockPos getScreenOpeningData(ServerPlayerEntity serverPlayerEntity) {
        return this.pos;
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable(ModBlocks.REVOLUTION_TABLE.getTranslationKey());
    }

    @Override
    public void onBlockReplaced(BlockPos pos, BlockState oldState){
        ItemScatterer.spawn(world, pos, (this));
        super.onBlockReplaced(pos, oldState);
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new RevolutionTableScreenHandler(syncId, playerInventory, this);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return INVENTORY;
    }

     public void tick(World world, BlockPos pos, BlockState state) {
        if (hasRecipe()){
            markDirty(world, pos, state);
            craftItem();
        }
     }

     //receita hardcoded teste pra ver se o babado funciona
    private boolean hasRecipe() {
        ItemStack input = new ItemStack(ModItems.SANTIAGUITA_INGOT, 16);
        ItemStack output = new ItemStack(ModBlocks.REVOLUTION_TABLE, 1);

        for (int i = 0; i < INPUT_GRID_END; i++) {
            if (!this.getStack(i).isOf(ModItems.SANTIAGUITA_INGOT)) {
                return false;
            }
        }
        if (!canInsertItemIntoOutputSlot(output) || !canInsertAmountIntoOutputSlot(output.getCount())){
            return false;
        }
        return true;
    }

    private boolean canInsertAmountIntoOutputSlot(int count) {
        int maxCount = this.getStack(OUTPUT_SLOT).isEmpty() ? 64 : this.getStack(OUTPUT_SLOT).getMaxCount();
        int currentCount = this.getStack(OUTPUT_SLOT).getCount();

        return maxCount >= currentCount + count;
    }

    private boolean canInsertItemIntoOutputSlot(ItemStack output) {
        return this.getStack(OUTPUT_SLOT).isEmpty() || this.getStack(OUTPUT_SLOT).getItem() == output.getItem();
    }

    private void craftItem() {
        ItemStack output = new ItemStack(ModBlocks.CREATURE_BLOCK, 1);

        for (int i = 0; i < INPUT_GRID_END; i++) {
            this.removeStack(i, 1);
        }
        this.setStack(OUTPUT_SLOT, new ItemStack(output.getItem(),
                this.getStack(OUTPUT_SLOT).getCount() + output.getCount()));
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        Inventories.writeNbt(nbt, INVENTORY, registryLookup);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        Inventories.readNbt(nbt, INVENTORY, registryLookup);
        super.readNbt(nbt, registryLookup);
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }
}
