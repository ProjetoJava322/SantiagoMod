package org.amemeida.santiago.incubator;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.amemeida.santiago.registry.blocks.ModBlockEntities;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.amemeida.santiago.incubator.recipes.IncubatorRecipe;
import org.amemeida.santiago.incubator.recipes.IncubatorRecipeInput;
import org.amemeida.santiago.util.ImplementedInventory;
import org.jetbrains.annotations.Nullable;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import org.amemeida.santiago.registry.recipes.ModRecipeTypes;

import java.util.Optional;
/**
 * Representa a entidade do bloco Incubadora, que possui um inventário com dois slots:
 * um para entrada e outro para saída.
 * Implementa funcionalidades para manipular receitas, progresso de fabricação e
 * interação com a interface gráfica.
 */
public class IncubatorBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory<BlockPos>, ImplementedInventory {

    // Inventário da incubadora com 2 slots (input e output)
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);

    // Constantes para identificar os slots do inventário
    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;

    // Delegate para sincronização de propriedades entre servidor e cliente
    protected final PropertyDelegate propertyDelegate;

    // Progresso atual da incubação/fabricação e progresso máximo necessário
    private int progress = 0;
    private int maxProgress = 72;

    /**
     * Construtor da entidade do bloco incubadora.
     * Inicializa o inventário e o PropertyDelegate para sincronizar o progresso.
     */
    public IncubatorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.INCUBATOR, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                // Retorna o valor do progresso ou do progresso máximo conforme o índice
                return switch (index) {
                    case 0 -> IncubatorBlockEntity.this.progress;
                    case 1 -> IncubatorBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                // Define o valor do progresso ou do progresso máximo conforme o índice
                switch (index) {
                    case 0: IncubatorBlockEntity.this.progress = value;
                    case 1: IncubatorBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int size() {
                // Define que existem 2 propriedades para sincronizar
                return 2;
            }
        };
    }

    /**
     * Retorna a posição do bloco para abertura da interface.
     */
    @Override
    public BlockPos getScreenOpeningData(ServerPlayerEntity player) {
        return this.pos;
    }

    /**
     * Retorna o inventário da incubadora.
     */
    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    /**
     * Retorna o nome exibido na interface do bloco.
     */
    @Override
    public Text getDisplayName() {
        return Text.translatable("incubator"); // texto traduzível para o nome "incubator"
    }

    /**
     * Cria o container da interface gráfica para interação do jogador.
     */
    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new IncubatorScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    /**
     * Ao substituir o bloco, dispersa os itens do inventário no mundo.
     */
    @Override
    public void onBlockReplaced(BlockPos pos, BlockState oldState) {
        ItemScatterer.spawn(world, pos, (this));
        super.onBlockReplaced(pos, oldState);
    }

    /**
     * Salva os dados do inventário e progresso no NBT (armazenamento persistente).
     */
    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        Inventories.writeNbt(nbt, inventory, registryLookup);
        nbt.putInt("incubator.progress", progress);
        nbt.putInt("incubator.max_progress", maxProgress);
    }

    /**
     * Lê os dados do inventário e progresso do NBT ao carregar o mundo.
     */
    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        Inventories.readNbt(nbt, inventory, registryLookup);
        progress = nbt.getInt("incubator.progress").get();
        maxProgress = nbt.getInt("incubator.max_progress").get();
        super.readNbt(nbt, registryLookup);
    }

    /**
     * Método chamado a cada tick do jogo para processar o funcionamento da incubadora.
     */
    public void tick(World world, BlockPos pos, BlockState state) {
        if(hasRecipe()) {
            increaseCraftingProgress();
            markDirty(world, pos, state);

            if(hasCraftingFinished()) {
                craftItem();
                resetProgress();
            }
        } else {
            resetProgress();
        }
    }

    /**
     * Reseta o progresso da incubadora.
     */
    private void resetProgress() {
        this.progress = 0;
        this.maxProgress = 72;
    }

    /**
     * Executa a fabricação do item, consumindo o item de entrada e colocando o resultado na saída.
     */
    private void craftItem() {
        Optional<RecipeEntry<IncubatorRecipe>> recipe = getCurrentRecipe();

        ItemStack output = recipe.get().value().output();
        this.removeStack(INPUT_SLOT, 1);
        this.setStack(OUTPUT_SLOT, new ItemStack(output.getItem(),
                this.getStack(OUTPUT_SLOT).getCount() + output.getCount()));
    }

    /**
     * Verifica se o progresso da fabricação já alcançou o máximo.
     */
    private boolean hasCraftingFinished() {
        return this.progress >= this.maxProgress;
    }

    /**
     * Incrementa o progresso da fabricação em 1.
     */
    private void increaseCraftingProgress() {
        this.progress++;
    }

    /**
     * Verifica se existe uma receita válida para o item no slot de entrada,
     * e se o item pode ser inserido no slot de saída.
     */
    private boolean hasRecipe() {
        Optional<RecipeEntry<IncubatorRecipe>> recipe = getCurrentRecipe();
        if(recipe.isEmpty()) {
            return false;
        }

        ItemStack output = recipe.get().value().output();

        if (!canInsertAmountIntoOutputSlot(output.getCount())){
            return false;
        }

        if (!canInsertItemIntoOutputSlot(output)){
            return false;
        }

        return true;
    }

    /**
     * Obtém a receita atual válida para o item no slot de entrada.
     */
    private Optional<RecipeEntry<IncubatorRecipe>> getCurrentRecipe() {
        return ((ServerWorld) this.getWorld()).getRecipeManager()
                .getFirstMatch(ModRecipeTypes.INCUBATOR_RECIPE_TYPE, new IncubatorRecipeInput(inventory.get(INPUT_SLOT)), this.getWorld());
    }

    /**
     * Verifica se é possível inserir o item de saída no slot de saída (se está vazio ou contém o mesmo tipo de item).
     */
    private boolean canInsertItemIntoOutputSlot(ItemStack output) {
        return this.getStack(OUTPUT_SLOT).isEmpty() || this.getStack(OUTPUT_SLOT).getItem() == output.getItem();
    }

    /**
     * Verifica se é possível inserir a quantidade do item no slot de saída (sem ultrapassar o limite máximo).
     */
    private boolean canInsertAmountIntoOutputSlot(int count) {
        int maxCount = this.getStack(OUTPUT_SLOT).isEmpty() ? 64 : this.getStack(OUTPUT_SLOT).getMaxCount();
        int currentCount = this.getStack(OUTPUT_SLOT).getCount();

        return maxCount >= currentCount + count;
    }

    /**
     * Cria o pacote para sincronizar a entidade do bloco com o cliente.
     */
    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    /**
     * Retorna os dados NBT iniciais para o chunk ao carregar o mundo.
     */
    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }
}
