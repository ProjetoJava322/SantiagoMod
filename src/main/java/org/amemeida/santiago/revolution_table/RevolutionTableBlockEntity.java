package org.amemeida.santiago.revolution_table;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.amemeida.santiago.exceptions.RecipeNotFoundException;
import org.amemeida.santiago.registry.blocks.ModBlockEntities;
import org.amemeida.santiago.registry.blocks.ModBlocks;
import org.amemeida.santiago.registry.recipes.ModRecipeTypes;
import org.amemeida.santiago.revolution_table.recipes.RevolutionTableRecipe;
import org.amemeida.santiago.revolution_table.recipes.RevolutionTableRecipeInput;
import org.amemeida.santiago.util.ImplementedInventory;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;


public class RevolutionTableBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory<BlockPos>, ImplementedInventory {

    private final DefaultedList<ItemStack> INVENTORY = DefaultedList.ofSize(13, ItemStack.EMPTY);
    private static final int INPUT_GRID_START = 0;
    private static final int INPUT_GRID_END = 11;
    private static final int OUTPUT_SLOT = 12;
    private boolean recipeStandby;

    public RevolutionTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.REVOLUTION_TABLE, pos, state);
        this.recipeStandby = false;
    }

    // Dados para abrir a tela no cliente — passa a posição do block entity
    @Override
    public BlockPos getScreenOpeningData(ServerPlayerEntity serverPlayerEntity) {
        return this.pos;
    }

    // Nome exibido no container (GUI)
    @Override
    public Text getDisplayName() {
        return Text.translatable(ModBlocks.REVOLUTION_TABLE.getTranslationKey());
    }

    // Quando o bloco é destruído ou substituído, solta os itens do inventário no mundo
    @Override
    public void onBlockReplaced(BlockPos pos, BlockState oldState){
        this.removeStack(OUTPUT_SLOT);
        ItemScatterer.spawn(world, pos, this);
        super.onBlockReplaced(pos, oldState);
    }

    // Cria o container para o inventário do jogador interagir
    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new RevolutionTableScreenHandler(syncId, playerInventory, this);
    }

    // Implementa o inventário (13 slots)
    @Override
    public DefaultedList<ItemStack> getItems() {
        return INVENTORY;
    }

    // Lógica rodada a cada tick do bloco
    public void tick(World world, BlockPos pos, BlockState state) {
        boolean recipe;
        try {
            recipe = hasRecipe();
        } catch (RecipeNotFoundException e) {
            recipe = false;
        }

        if (recipe) {
            int recipe_amnt = checkRecipeAmount();                   // Quantas receitas dá pra fazer com o inventário
            int max_craft_amnt = getMaxCount(getOutput());           // Quantidade máxima permitida para output
            final int amnt = Math.min(recipe_amnt, max_craft_amnt);  // Quantidade que vai ser craftada (mínimo)

            if (this.getStack(OUTPUT_SLOT).getCount() >= max_craft_amnt) {
                return; // Se output já cheio, não faz nada
            }

            if (!getRecipeStandby()) {
                toggleRecipeStandby(); // ativa standby
                craftItem(amnt);       // faz o craft
            }

            // Corrige o output caso o número no slot de output esteja diferente do esperado
            if (getRecipeStandby() && !this.getStack(OUTPUT_SLOT).isEmpty() && this.getStack(OUTPUT_SLOT).getCount() != amnt) {
                craftItem(amnt);
            }

            // Caso o output esteja vazio mas a receita estava em standby, significa que o player coletou o item
            if (getRecipeStandby() && this.getStack(OUTPUT_SLOT).isEmpty()) {
                recipe_amnt = checkRecipeAmount();
                toggleRecipeStandby(); // desativa standby
                clearGrid(recipe_amnt); // remove os ingredientes usados
            }

        } else {
            // Não tem receita, limpa output e desliga standby se ativo
            this.removeStack(OUTPUT_SLOT);
            if (getRecipeStandby()) {
                toggleRecipeStandby();
            }
        }

        // Marca para atualizar estado e sincronia com cliente
        markDirty(world, pos, state);
    }

    private boolean getRecipeStandby() {
        return this.recipeStandby;
    }

    private void toggleRecipeStandby() {
        this.recipeStandby = !this.recipeStandby;
    }

    // Verifica a quantidade mínima de ingredientes para determinar quantas receitas podem ser feitas
    private int checkRecipeAmount() {
        int min_recipes = 64;  // valor base (max stack count)
        for (int i = INPUT_GRID_START; i <= INPUT_GRID_END; i++) {
            int item_amount = this.getStack(i).getCount();
            if (item_amount < min_recipes) {
                min_recipes = item_amount;
            }
        }
        return min_recipes;
    }

    // Verifica se há uma receita válida para o inventário atual, lança exceção se não encontrar
    private boolean hasRecipe() throws RecipeNotFoundException {
        Optional<RecipeEntry<RevolutionTableRecipe>> recipe = getCurrentRecipe();
        if (recipe.isEmpty()) {
            throw new RecipeNotFoundException("Receita não encontrada");
        }
        return true;
    }

    // Obtém a receita atual baseada no inventário do bloco
    private Optional<RecipeEntry<RevolutionTableRecipe>> getCurrentRecipe() {
        RevolutionTableRecipeInput recipeInput = RevolutionTableRecipeInput.create(INVENTORY);
        return world.getServer().getRecipeManager().getFirstMatch(ModRecipeTypes.REVOLUTION_TABLE_RECIPE_TYPE, recipeInput, world);
    }

    // Remove uma quantidade (amnt) de cada slot de ingrediente
    private void clearGrid(int amnt) {
        for (int i = INPUT_GRID_START; i <= INPUT_GRID_END; i++) {
            this.removeStack(i, amnt);
        }
    }

    // Obtém o output da receita atual
    private ItemStack getOutput() {
        Optional<RecipeEntry<RevolutionTableRecipe>> recipe = getCurrentRecipe();
        return recipe.get().value().result();
    }

    // Define o item e quantidade no slot de saída conforme a receita
    private void craftItem(int recipe_amnt) {
        Optional<RecipeEntry<RevolutionTableRecipe>> recipe = getCurrentRecipe();
        ItemStack output = recipe.get().value().result().copy(); // <-- importante fazer cópia!
        output.setCount(recipe_amnt);
        this.setStack(OUTPUT_SLOT, output);
    }

    // Salvamento dos dados do inventário no NBT do bloco
    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        Inventories.writeNbt(nbt, INVENTORY, registryLookup);
    }

    // Leitura dos dados do inventário do NBT do bloco
    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        Inventories.readNbt(nbt, INVENTORY, registryLookup);  // <-- corrigido de writeNbt para readNbt!
        super.readNbt(nbt, registryLookup);
    }

    // Pacote enviado para o cliente quando o bloco precisa sincronizar estado
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
