package org.amemeida.santiago.santiaguita.revolution_table;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;
import org.amemeida.santiago.registry.blocks.ModScreenHandlers;
import org.jetbrains.annotations.Nullable;

public class RevolutionTableScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    public final RevolutionTableBlockEntity blockEntity;

    public RevolutionTableScreenHandler(int syncId, PlayerInventory inventory, BlockPos pos) {
        this(syncId, inventory, inventory.player.getWorld().getBlockEntity(pos));
    }

    public RevolutionTableScreenHandler(int syncId, PlayerInventory playerInventory,
                                      BlockEntity blockEntity) {
        super(ModScreenHandlers.REVOLUTION_TABLE_SCREEN_HANDLER, syncId);
        this.inventory = ((Inventory) blockEntity);
        this.blockEntity = ((RevolutionTableBlockEntity) blockEntity);

        this.addSlot(new Slot(inventory, 0, 40, 7));
        this.addSlot(new Slot(inventory, 1, 58, 7));
        this.addSlot(new Slot(inventory, 2, 22, 25));
        this.addSlot(new Slot(inventory, 3, 40, 25));
        this.addSlot(new Slot(inventory, 4, 58, 25));
        this.addSlot(new Slot(inventory, 5, 76, 25));
        this.addSlot(new Slot(inventory, 6, 22, 43));
        this.addSlot(new Slot(inventory, 7, 40, 43));
        this.addSlot(new Slot(inventory, 8, 58, 43));
        this.addSlot(new Slot(inventory, 9, 76, 43));
        this.addSlot(new Slot(inventory, 10, 40, 61));
        this.addSlot(new Slot(inventory, 11, 58, 61));
        this.addSlot(new Slot(inventory, 12, 124, 36));

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

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

    //TEM QUE AJEITAR ISSO +
    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}
