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
    private static final int INPUT_GRID_END = 11;
    private static final int OUTPUT_SLOT = 12;
    private boolean recipeStandby;

    public RevolutionTableBlockEntity( BlockPos pos, BlockState state) {
        super(ModBlockEntities.REVOLUTION_TABLE, pos, state);
        this.recipeStandby = false;
    }

    @Override
    public BlockPos getScreenOpeningData(ServerPlayerEntity serverPlayerEntity) {
        return this.pos;
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("block.santiago.revolution_table");
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
        int recipe_amnt = checkRecipeAmount();
        if (hasRecipe(recipe_amnt)) {
            if (this.getStack(OUTPUT_SLOT).getCount() >= 64) {return;}
            if (!getRecipeStandby()){
                toggleRecipeStandby(); //seta como true
                craftItem(recipe_amnt);
            }

            if (getRecipeStandby() && !this.getStack(OUTPUT_SLOT).isEmpty() && this.getStack(OUTPUT_SLOT).getCount() != recipe_amnt) {craftItem(recipe_amnt);}

            if (getRecipeStandby() && this.getStack(OUTPUT_SLOT).isEmpty()){
                toggleRecipeStandby(); //seta como false
                clearGrid(recipe_amnt);
            }
        } else {
            this.removeStack(OUTPUT_SLOT);
            if (getRecipeStandby()){toggleRecipeStandby();} //seta como false
        }
         markDirty(world, pos, state);
     }

     private boolean getRecipeStandby(){
        return this.recipeStandby;
     }

    private void toggleRecipeStandby() {
        this.recipeStandby = !this.getRecipeStandby();
    }

    //checa quantas receitas os itens da grid satisfazem
    private int checkRecipeAmount(){
        //64 é o stack máximo num geral, mudar essa função para receber um stack máximo depois;
        int min_recipes = 64, item_amount;
        for (int i = INPUT_GRID_START; i <= INPUT_GRID_END; i++){
            item_amount = this.getStack(i).getCount();
            if (item_amount < min_recipes){
                min_recipes = item_amount;
            }
        }
        return min_recipes;
    }

    //receita hardcoded teste pra ver se o babado funciona
    private boolean hasRecipe(int amnt) {
        ItemStack output = new ItemStack(ModBlocks.CREATURE_BLOCK, amnt);

        for (int i = INPUT_GRID_START; i <= INPUT_GRID_END; i++) {
            if (!this.getStack(i).isOf(ModItems.SANTIAGUITA_INGOT)) {
                return false;
            }
        }
        if (!canInsertItemIntoOutputSlot(output)) {
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

    private void clearGrid(int amnt){
        for (int i = INPUT_GRID_START; i <= INPUT_GRID_END; i++) {
            this.removeStack(i, amnt);
        }
    }

    private void craftItem(int amnt) {
        ItemStack output = new ItemStack(ModBlocks.CREATURE_BLOCK, amnt);
        this.setStack(OUTPUT_SLOT, output);
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
