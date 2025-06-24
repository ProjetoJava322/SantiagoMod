package org.amemeida.santiago.incubator;

import org.amemeida.santiago.registry.blocks.ModScreenHandlers;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;
/**
 * Classe responsável por gerenciar a interface gráfica (GUI) da incubadora,
 * incluindo a interação com o inventário do bloco e do jogador,
 * além da sincronização do progresso da incubação.
 */
public class IncubatorScreenHandler extends ScreenHandler {
    private final Inventory inventory; // Inventário da incubadora
    private final PropertyDelegate propertyDelegate; // Para sincronizar progresso e outras propriedades
    public final IncubatorBlockEntity blockEntity; // Referência à entidade do bloco incubadora

    /**
     * Construtor usado para criar a tela a partir da posição do bloco no mundo.
     * Obtém a entidade do bloco e cria um PropertyDelegate padrão.
     */
    public IncubatorScreenHandler(int syncId, PlayerInventory inventory, BlockPos pos) {
        this(syncId, inventory, inventory.player.getWorld().getBlockEntity(pos), new ArrayPropertyDelegate(2));
    }

    /**
     * Construtor principal que inicializa a tela, slots e propriedades.
     * 
     * @param syncId id de sincronização da tela
     * @param playerInventory inventário do jogador
     * @param blockEntity entidade do bloco incubadora
     * @param arrayPropertyDelegate delegate para propriedades sincronizadas (progresso)
     */
    public IncubatorScreenHandler(int syncId, PlayerInventory playerInventory,
                                  BlockEntity blockEntity, PropertyDelegate arrayPropertyDelegate) {
        super(ModScreenHandlers.INCUBATOR_SCREEN_HANDLER, syncId);
        this.inventory = ((Inventory) blockEntity);
        this.blockEntity = ((IncubatorBlockEntity) blockEntity);
        this.propertyDelegate = arrayPropertyDelegate;

        // Adiciona slots do inventário da incubadora na GUI
        this.addSlot(new Slot(inventory, 0, 54, 34));   // Slot de entrada
        this.addSlot(new Slot(inventory, 1, 104, 34));  // Slot de saída

        // Adiciona inventário do jogador (inventário principal e hotbar)
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

        // Adiciona propriedades para sincronização (ex: progresso)
        addProperties(arrayPropertyDelegate);
    }

    /**
     * Verifica se a incubadora está atualmente processando um item (progresso > 0).
     * 
     * @return true se estiver incubando, false caso contrário
     */
    public boolean isCrafting() {
        return propertyDelegate.get(0) > 0;
    }

    /**
     * Calcula o progresso para exibição da barra de progresso na GUI,
     * escalando o progresso atual para o tamanho da seta animada.
     * 
     * @return valor do progresso escalado para pixels (largura da seta)
     */
    public int getScaledArrowProgress() {
        int progress = this.propertyDelegate.get(0);
        int maxProgress = this.propertyDelegate.get(1);
        int arrowPixelSize = 24; // largura da seta na interface (pixels)

        return maxProgress != 0 && progress != 0 ? progress * arrowPixelSize / maxProgress : 0;
    }

    /**
     * Método para transferência rápida (shift-click) entre o inventário da incubadora e do jogador.
     * 
     * @param player jogador que está fazendo a ação
     * @param invSlot índice do slot clicado
     * @return o item movido ou ItemStack.EMPTY se não foi possível mover
     */
    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.size()) {
                // Se o slot está no inventário da incubadora, tenta mover para o inventário do jogador
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // Se o slot está no inventário do jogador, tenta mover para o inventário da incubadora
                if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                    return ItemStack.EMPTY;
                }
            }

            // Atualiza o slot após a movimentação
            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }
        return newStack;
    }

    /**
     * Verifica se o jogador pode usar a GUI da incubadora (distância e permissões).
     */
    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    /**
     * Adiciona os slots do inventário principal do jogador na GUI.
     */
    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    /**
     * Adiciona os slots da hotbar do jogador na GUI.
     */
    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}
