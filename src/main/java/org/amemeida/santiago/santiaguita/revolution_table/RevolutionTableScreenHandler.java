package org.amemeida.santiago.santiaguita.revolution_table;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeUnlocker;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.amemeida.santiago.Santiago;
import org.amemeida.santiago.registry.blocks.ModScreenHandlers;

public class RevolutionTableScreenHandler extends AbstractRecipeScreenHandler {
    public static final int OUTPUT_SLOT = 0;
    public static final int INPUT_SLOTS_START = 1;
    public static final int INPUT_SLOTS_END = 12;
    public static final int BRIMSTONE_SLOT = 8;
    public static final int MAX_WIDTH_AND_HEIGHT = 4;
    public static final int MAX_WIDTH_END = 1;
    public static final int INVENTORY_SLOTS_START = 9;
    public static final int INVENTORY_SLOTS_END = 35;
    public static final int HOTBAR_SLOTS_START = 36;
    public static final int HOTBAR_SLOTS_END = 45;

    public final RecipeInputInventory inventory;
    private final CraftingResultInventory resultInventory;

    public ScreenHandlerContext context;
    private final PlayerEntity player;

    private Slot[] _slots;
    private Slot _outputSlot;

    public boolean hasOutput() {
        return _outputSlot.hasStack();
    }

    public RevolutionTableScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    public RevolutionTableScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(ModScreenHandlers.REVOLUTION_TABLE_SCREEN_HANDLER, syncId);
        this.inventory = new EffigySimpleInventory(this, BRIMSTONE_SLOT);
        this.resultInventory = new EffigyCraftingResultInventory(this);
        this.addProperty(this.levelCost);
        this.context = context;
        this.player = playerInventory.player;

        _outputSlot = this.addSlot(new OutputSlot(this, this.player, this.inventory, this.resultInventory, 0, 132, 29 - 8));
    }

    private class RevolutionTableSimpleInventory extends SimpleInventory implements RecipeInputInventory {
        private ScreenHandler _screen;
        public RevolutionTableSimpleInventory(RevolutionTableScreenHandler screen, int size) {
            super(size);
            _screen = screen;
        }

        public void markDirty() {
            _screen.onContentChanged(this);
            super.markDirty();
        }

        @Override
        public int getWidth() {
            return MAX_WIDTH_AND_HEIGHT;
        }

        @Override
        public int getHeight() {
            return MAX_WIDTH_AND_HEIGHT;
        }
    }

    private class RevolutionTableCraftingResultInventory extends CraftingResultInventory {
        private ScreenHandler _screen;
        public RevolutionTableCraftingResultInventory(RevolutionTableScreenHandler screen) {
            super();
            _screen = screen;
        }
        public void markDirty() {
            _screen.onContentChanged(this);
            super.markDirty();
        }
    }

    private class OutputSlot extends Slot {
        private final RecipeInputInventory input;
        private final PlayerEntity player;
        private final RevolutionTableScreenHandler handler;
        private int amount;
        public OutputSlot(RevolutionTableScreenHandler handler, PlayerEntity player, RecipeInputInventory input, Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
            this.player = player;
            this.input = input;
            this.handler = handler;
        }

        @Override
        public boolean canInsert(ItemStack stack) {
            return false;
        }

        @Override
        public ItemStack takeStack(int amount) {
            if (this.hasStack()) {
                this.amount = this.amount + Math.min(amount, this.getStack().getCount());
            }

            return super.takeStack(amount);
        }

        @Override
        protected void onCrafted(ItemStack stack, int amount) {
            this.amount += amount;
            this.onCrafted(stack);
        }

        @Override
        protected void onTake(int amount) {
            this.amount += amount;
        }

        @Override
        protected void onCrafted(ItemStack stack) {
            if (this.amount > 0) {
                stack.onCraftByPlayer(this.player, this.amount);
            }

            if (this.inventory instanceof RecipeUnlocker recipeUnlocker) {
                recipeUnlocker.unlockLastRecipe(this.player, this.input.getHeldStacks());
            }

            this.amount = 0;
        }

        private static DefaultedList<ItemStack> copyInput(RevolutionTableRecipeInput input) {
            DefaultedList<ItemStack> defaultedList = DefaultedList.ofSize(input.size(), ItemStack.EMPTY);

            for (int i = 0; i < defaultedList.size(); i++) {
                defaultedList.set(i, input.getStackInSlot(i));
            }

            return defaultedList;
        }

        @SuppressWarnings("unchecked")
        private DefaultedList<ItemStack> getRecipeRemainders(RevolutionTableRecipeInput input, World world) {
            return world instanceof ServerWorld serverWorld
                    ? (DefaultedList<ItemStack>)serverWorld.getRecipeManager()
                    .getFirstMatch(PeacefulMod.EFFIGY_ALTAR_RECIPE_TYPE, input, serverWorld)
                    .map(recipe -> ((EffigyAltarRecipe)recipe.value()).getRecipeRemainders(input))
                    .orElseGet(() -> copyInput(input))
                    : EffigyAltarRecipe.collectRecipeRemainders(input);
        }

        @Override
        public void onTakeItem(PlayerEntity player, ItemStack stack) {
            this.onCrafted(stack);
            EffigyAltarRecipeInput recipeInput = EffigyAltarRecipeInput.create(this.input.getHeldStacks());
            DefaultedList<ItemStack> defaultedList = this.getRecipeRemainders(recipeInput, player.getWorld());

            this.handler.context.run((world, pos) -> {
                if (!player.isInCreativeMode()) {
                    player.addExperienceLevels(-getOutputXPCost());
                }
                world.playSound((Entity)null, pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0F, world.random.nextFloat() * 0.1F + 0.9F);
            });

            for (int y = 0; y < MAX_WIDTH_AND_HEIGHT; y++) {
                for (int x = 0; x < (y == MAX_WIDTH_AND_HEIGHT - 1 ? MAX_WIDTH_AND_HEIGHT - MAX_WIDTH_END : MAX_WIDTH_AND_HEIGHT); x++) {
                    int z = x + y * MAX_WIDTH_AND_HEIGHT;
                    ItemStack itemStack = this.input.getStack(z);
                    ItemStack defaultStack = defaultedList.get(z);
                    if (!itemStack.isEmpty()) {
                        this.input.removeStack(z, 1);
                        itemStack = this.input.getStack(z);
                    }

                    if (!defaultStack.isEmpty()) {
                        if (itemStack.isEmpty()) {
                            this.input.setStack(z, defaultStack);
                        } else if (ItemStack.areItemsAndComponentsEqual(itemStack, defaultStack)) {
                            defaultStack.increment(itemStack.getCount());
                            this.input.setStack(z, defaultStack);
                        } else if (!this.player.getInventory().insertStack(defaultStack)) {
                            this.player.dropItem(defaultStack, false);
                        }
                    }
                }
            }
        }

        @Override
        public boolean disablesDynamicDisplay() {
            return true;
        }
    }
}