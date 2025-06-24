package org.amemeida.santiago.revolution_table;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;
import org.amemeida.santiago.registry.blocks.ModScreenHandlers;

/**
 * Gerencia a interface de usuário (screen handler) do Revolution Table,
 * controlando os slots de input, output e o inventário do jogador.
 */
public class RevolutionTableScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    public final RevolutionTableBlockEntity blockEntity;
    private final Slot[] INPUT_SLOTS;
    private final Slot OUTPUT_SLOT;

    /**
     * Cria um ScreenHandler baseado na posição do bloco no mundo.
     *
     * @param syncId Identificador de sincronização da interface.
     * @param inventory Inventário do jogador.
     * @param pos Posição do bloco no mundo.
     */
    public RevolutionTableScreenHandler(int syncId, PlayerInventory inventory, BlockPos pos) {
        this(syncId, inventory, inventory.player.getWorld().getBlockEntity(pos));
    }

    /**
     * Cria um ScreenHandler a partir de um BlockEntity específico.
     *
     * @param syncId Identificador de sincronização da interface.
     * @param playerInventory Inventário do jogador.
     * @param blockEntity Entidade do bloco associada ao inventário.
     */
    public RevolutionTableScreenHandler(int syncId, PlayerInventory playerInventory,
                                        BlockEntity blockEntity) {
        super(ModScreenHandlers.REVOLUTION_TABLE_SCREEN_HANDLER, syncId);
        this.inventory = ((Inventory) blockEntity);
        this.blockEntity = ((RevolutionTableBlockEntity) blockEntity);
        INPUT_SLOTS = new Slot[]{
            this.addSlot(new Slot(inventory, 0, 52, 17)),
            this.addSlot(new Slot(inventory, 1, 70, 17)),
            this.addSlot(new Slot(inventory, 2, 34, 35)),
            this.addSlot(new Slot(inventory, 3, 52, 35)),
            this.addSlot(new Slot(inventory, 4, 70, 35)),
            this.addSlot(new Slot(inventory, 5, 88, 35)),
            this.addSlot(new Slot(inventory, 6, 34, 53)),
            this.addSlot(new Slot(inventory, 7, 52, 53)),
            this.addSlot(new Slot(inventory, 8, 70, 53)),
            this.addSlot(new Slot(inventory, 9, 88, 53)),
            this.addSlot(new Slot(inventory, 10, 52, 71)),
            this.addSlot(new Slot(inventory, 11, 70, 71))
        };

        OUTPUT_SLOT = this.addSlot(new OutputSlot(inventory, 12, 147, 44));

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    /**
     * Trata a movimentação rápida de itens (shift-click) entre inventário do bloco e do jogador.
     *
     * @param player Jogador que está movendo o item.
     * @param invSlot Índice do slot selecionado.
     * @return ItemStack resultante após a tentativa de movimentação.
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
     * Verifica se o jogador pode usar esta interface.
     *
     * @param player Jogador que está tentando usar a interface.
     * @return true se o jogador pode usar, false caso contrário.
     */
    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    /**
     * Adiciona os slots do inventário principal do jogador à interface.
     *
     * @param playerInventory Inventário do jogador.
     */
    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 23 + l * 18, 103 + i * 18));
            }
        }
    }

    /**
     * Adiciona os slots da hotbar do jogador à interface.
     *
     * @param playerInventory Inventário do jogador.
     */
    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 23 + i * 18, 161));
        }
    }

    /**
     * Slot personalizado para saída, que não permite inserção de itens.
     */
    private class OutputSlot extends Slot {
        public OutputSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public boolean canInsert(ItemStack stack) {
            return false;
        }
    }
}
