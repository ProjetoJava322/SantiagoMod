package org.amemeida.santiago.computer;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.Slot;
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
    private final PropertyDelegate propertyDelegate;
    private final ComputerData data;

    public ComputerScreenHandler(int syncId, PlayerInventory playerInventory, ComputerData data) {
        this(syncId, playerInventory, playerInventory.player.getWorld().getBlockEntity(data.pos()),
                new ArrayPropertyDelegate(2), data);
    }

    public ComputerScreenHandler(int syncId, PlayerInventory playerInventory, BlockEntity blockEntity,
                                 PropertyDelegate propertyDelegate, ComputerData data) {
        super(ModScreenHandlers.COMPUTER_SCREEN_HANDLER, syncId);

        this.inventory = (Inventory) blockEntity;
        this.blockEntity = (ComputerEntity) blockEntity;
        this.data = data;
        this.propertyDelegate = propertyDelegate;

        this.addProperties(propertyDelegate);

        addSlots();
        addPlayerSlots(playerInventory, 8, 170);
    }

    public ComputerData getData() {
        return data;
    }

    public static record ComputerData(BlockPos pos, boolean write, boolean and) {
            public static final PacketCodec<RegistryByteBuf, ComputerData> PACKET_CODEC = PacketCodec.tuple(
                    BlockPos.PACKET_CODEC, ComputerData::pos,
                    PacketCodecs.BOOLEAN, ComputerData::write,
                    PacketCodecs.BOOLEAN, ComputerData::and,
                    ComputerData::new
            );
    }

    public boolean getWrite() {
        return propertyDelegate.get(0) == 1;
    }

    public void setWrite(boolean write) {
        propertyDelegate.set(0, write ? 1 : 0);
    }

    public boolean getAnd() {
        return propertyDelegate.get(1) == 1;
    }

    public void setAnd(boolean and) {
        propertyDelegate.set(1, and ? 1 : 0);
    }

    private void addSlots() {
        this.addSlot(new FloppySlot(inventory, 0, 80, 11));

        for (int i = 1; i <= ComputerEntity.IO_SLOTS * 2; i += 2) {
            this.addSlot(new IOSlot(inventory, i, 26, 17 * i + 13));
            this.addSlot(new IOSlot(inventory, i + 1, 134, 17 * i + 13));
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
