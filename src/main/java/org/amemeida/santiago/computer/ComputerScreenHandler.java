package org.amemeida.santiago.computer;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.screen.slot.CrafterInputSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import org.amemeida.santiago.registry.blocks.ModScreenHandlers;
import org.amemeida.santiago.registry.items.ModComponents;

/**
 * @see net.minecraft.screen.AbstractFurnaceScreenHandler
 * @see net.minecraft.screen.CrafterScreenHandler
 * @see net.minecraft.screen.GenericContainerScreenHandler
 * @see net.minecraft.screen.BeaconScreenHandler
 */

public class ComputerScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final ComputerEntity blockEntity;

    public ComputerScreenHandler(int syncId, PlayerInventory playerInventory, BlockPos pos) {
        this(syncId, playerInventory, playerInventory.player.getWorld().getBlockEntity(pos));
    }

    public ComputerScreenHandler(int syncId, PlayerInventory playerInventory, BlockEntity blockEntity) {
        super(ModScreenHandlers.COMPUTER_SCREEN_SCREEN_HANDLER, syncId);

        this.inventory = (Inventory) blockEntity;
        this.blockEntity = (ComputerEntity) blockEntity;

        addSlots();
        addPlayerSlots(playerInventory, 8, 130);
    }

    public boolean toggleWrite() {
        var a = blockEntity.toggleWrite();
        this.sendContentUpdates();
        return a;
    }

    public boolean getWrite() {
        return blockEntity.getWrite();
    }

    private void addSlots() {
        this.addSlot(new FloppySlot(inventory, 0, 20, 20));

        for (int i = 1; i <= ComputerEntity.IO_SLOTS * 2; i += 2) {
            this.addSlot(new IOSlot(inventory, i, 60, 20 * i + 5));
            this.addSlot(new IOSlot(inventory, i + 1, 100, 20 * i + 5));
        }
    }

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

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    static class FloppySlot extends Slot {
        public FloppySlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public boolean canInsert(ItemStack stack) {
            if (!stack.contains(ModComponents.SCRIPT)) {
                return false;
            }

            return super.canInsert(stack);
        }
    }

    static class IOSlot extends Slot {
        public IOSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public boolean canInsert(ItemStack stack) {
            if (!stack.contains(ModComponents.IO)) {
                return false;
            }

            return super.canInsert(stack);
        }
    }
}
