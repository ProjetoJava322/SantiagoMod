package org.amemeida.santiago.computer;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.Slot;
import org.amemeida.santiago.registry.blocks.ModScreenHandlers;
import org.amemeida.santiago.registry.items.ModComponents;

/**
 * Gerenciador de tela para a entidade Computer, responsável por controlar a interface
 * entre o inventário do bloco e o jogador, assim como os modos de operação.
 *
 * @see net.minecraft.screen.AbstractFurnaceScreenHandler
 * @see net.minecraft.screen.CrafterScreenHandler
 * @see net.minecraft.screen.GenericContainerScreenHandler
 * @see net.minecraft.screen.BeaconScreenHandler
 */
public class ComputerScreenHandler extends ScreenHandler {
    /**
     * Inventário associado à entidade Computer.
     */
    private final Inventory inventory;

    /**
     * Propriedades expostas para sincronização com a interface gráfica (modos output e result).
     */
    private final PropertyDelegate propertyDelegate;

    /**
     * Dados sincronizados da entidade Computer.
     */
    private final ComputerEntity.ComputerData data;

    /**
     * Construtor usado para criar a tela a partir do inventário do jogador e dados da entidade.
     *
     * @param syncId ID de sincronização da tela
     * @param playerInventory inventário do jogador
     * @param data dados da entidade Computer a ser exibida
     */
    public ComputerScreenHandler(int syncId, PlayerInventory playerInventory, ComputerEntity.ComputerData data) {
        this(syncId, playerInventory, playerInventory.player.getWorld().getBlockEntity(data.pos()),
                new ArrayPropertyDelegate(2), data);
    }

    /**
     * Construtor principal, recebendo a entidade, propriedades e dados para configurar a tela.
     *
     * @param syncId ID de sincronização da tela
     * @param playerInventory inventário do jogador
     * @param blockEntity entidade do bloco associada
     * @param propertyDelegate propriedades sincronizadas da tela
     * @param data dados da entidade Computer
     */
    public ComputerScreenHandler(int syncId, PlayerInventory playerInventory, BlockEntity blockEntity,
                                 PropertyDelegate propertyDelegate, ComputerEntity.ComputerData data) {
        super(ModScreenHandlers.COMPUTER_SCREEN_HANDLER, syncId);

        this.inventory = (Inventory) blockEntity;
        this.data = data;
        this.propertyDelegate = propertyDelegate;

        this.addProperties(propertyDelegate);

        addSlots();
        addPlayerSlots(playerInventory, 8, 169);
    }

    /**
     * Obtém os dados sincronizados da entidade Computer.
     *
     * @return dados da entidade
     */
    public ComputerEntity.ComputerData getData() {
        return data;
    }

    /**
     * Obtém o modo de saída atual.
     *
     * @return modo de saída configurado
     */
    public ComputerEntity.OutputMode getOutputMode() {
        return ComputerEntity.OutputMode.values()[propertyDelegate.get(0)];
    }

    /**
     * Define o modo de saída.
     *
     * @param mode novo modo de saída
     */
    public void setOutputMode(ComputerEntity.OutputMode mode) {
        propertyDelegate.set(0, mode.ordinal());
    }

    /**
     * Obtém o modo de resultado atual.
     *
     * @return modo de resultado configurado
     */
    public ComputerEntity.ResultMode getResultMode() {
        return ComputerEntity.ResultMode.values()[propertyDelegate.get(1)];
    }

    /**
     * Define o modo de resultado.
     *
     * @param mode novo modo de resultado
     */
    public void setResultMode(ComputerEntity.ResultMode mode) {
        propertyDelegate.set(1, mode.ordinal());
    }

    /**
     * Adiciona os slots do inventário da entidade Computer na tela.
     */
    private void addSlots() {
        this.addSlot(new FloppySlot(inventory, 0, 80, 11));

        for (int i = 1; i <= ComputerEntity.IO_SLOTS * 2; i += 2) {
            this.addSlot(new IOSlot(inventory, i, 26, 17 * i + 13));
            this.addSlot(new IOSlot(inventory, i + 1, 134, 17 * i + 13));
        }
    }

    /**
     * Implementa o comportamento de shift-click para mover itens rapidamente entre inventários.
     *
     * @param player jogador que interage
     * @param invSlot slot que está sendo movido
     * @return o item movido, ou vazio se a movimentação falhar
     */
    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }
        return newStack;
    }

    /**
     * Permite que o jogador interaja com a tela.
     *
     * @param player jogador que tenta usar a tela
     * @return sempre true (permite uso)
     */
    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    /**
     * Slot especializado para o disco (floppy) que aceita apenas itens contendo scripts.
     */
    static class FloppySlot extends Slot {
        public FloppySlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        /**
         * Restringe a inserção para apenas itens que contenham o componente SCRIPT.
         *
         * @param stack item a ser inserido
         * @return true se puder inserir
         */
        @Override
        public boolean canInsert(ItemStack stack) {
            if (!stack.contains(ModComponents.SCRIPT)) {
                return false;
            }
            return super.canInsert(stack);
        }
    }

    /**
     * Slot especializado para os slots de entrada/saída que aceitam apenas itens com componentes IO ou ENDER.
     */
    static class IOSlot extends Slot {
        public IOSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        /**
         * Restringe a inserção para apenas itens que contenham os componentes IO ou ENDER.
         *
         * @param stack item a ser inserido
         * @return true se puder inserir
         */
        @Override
        public boolean canInsert(ItemStack stack) {
            if (!stack.contains(ModComponents.IO) && !stack.contains(ModComponents.ENDER)) {
                return false;
            }
            return super.canInsert(stack);
        }
    }
}
