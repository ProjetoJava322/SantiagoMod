package org.amemeida.santiago.santiaguita.revolution_table;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.recipe.RecipeUnlocker;
import net.minecraft.recipe.book.RecipeBookType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.amemeida.santiago.Santiago;
import org.amemeida.santiago.registry.blocks.ModBlocks;
import org.amemeida.santiago.registry.blocks.ModScreenHandlers;
import org.amemeida.santiago.registry.recipes.ModRecipeTypes;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RevolutionTableScreenHandler extends AbstractRecipeScreenHandler {
    public static final int OUTPUT_SLOT = 0;
    public static final int INPUT_SLOTS_START = 1;
    public static final int INPUT_SLOTS_END = 12;
    public static final int MAX_WIDTH_AND_HEIGHT = 4;
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

    private boolean filling;

    public boolean hasOutput() {
        return _outputSlot.hasStack();
    }

    public RevolutionTableScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    public RevolutionTableScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(ModScreenHandlers.REVOLUTION_TABLE_SCREEN_HANDLER, syncId);
        this.inventory = new RevolutionTableSimpleInventory(this, INPUT_SLOTS_END);
        this.resultInventory = new RevolutionTableCraftingResultInventory(this);
        this.context = context;
        this.player = playerInventory.player;

        _outputSlot = this.addSlot(new OutputSlot(this, this.player, this.inventory, this.resultInventory, 0, 132, 29 - 8));
        _slots = new Slot[] {
                this.addSlot(new CustomSlot(this, this.inventory, 0, 4, 17)),
                this.addSlot(new CustomSlot(this, this.inventory, 1, 22, 17)),
                this.addSlot(new CustomSlot(this, this.inventory, 2, 40, 17)),
                this.addSlot(new CustomSlot(this, this.inventory, 3, 58, 17)),
                this.addSlot(new CustomSlot(this, this.inventory, 4, 4, 35)),
                this.addSlot(new CustomSlot(this, this.inventory, 5, 22, 35)),
                this.addSlot(new CustomSlot(this, this.inventory, 6, 40, 35)),
                this.addSlot(new CustomSlot(this, this.inventory, 7, 58, 35)),
                this.addSlot(new CustomSlot(this, this.inventory, 8, 4, 53)),
                this.addSlot(new CustomSlot(this, this.inventory, 9, 22, 53)),
                this.addSlot(new CustomSlot(this, this.inventory, 10, 40, 53)),
                this.addSlot(new CustomSlot(this, this.inventory, 11, 58, 53))
        };
        this.addPlayerSlots(playerInventory, 8, 84);
    }

    public static boolean isAir(ItemStack stack) {
        return stack.isOf(Items.AIR);
    }

    public void updateResult(
            ServerWorld world,
            @Nullable RecipeEntry<RevolutionTableRecipe> recipe
    ) {
        RevolutionTableRecipeInput recipeInput = RevolutionTableRecipeInput.create(inventory.getHeldStacks());
        ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)player;
        ItemStack resultStack = ItemStack.EMPTY;
        int cost = 0;


        Optional<RecipeEntry<RevolutionTableRecipe>> optional = world.getServer().getRecipeManager().getFirstMatch(ModRecipeTypes.REVOLUTION_TABLE_RECIPE_TYPE, recipeInput, world, recipe);
        if (optional.isPresent()) {
            RecipeEntry<RevolutionTableRecipe> recipeEntry = (RecipeEntry<RevolutionTableRecipe>)optional.get();
            RevolutionTableRecipe altarRecipe = recipeEntry.value();
            boolean shouldCraftRecipe = resultInventory.shouldCraftRecipe(serverPlayerEntity, recipeEntry);
            if (shouldCraftRecipe) {
                ItemStack craftedStack = altarRecipe.craft(recipeInput, world.getRegistryManager());
                boolean isItemEnabled = craftedStack.isItemEnabled(world.getEnabledFeatures());
                if (isItemEnabled) {
                    resultStack = craftedStack;
                }
            }
        }


        resultInventory.setStack(0, resultStack);
        this.setReceivedStack(0, resultStack);
        serverPlayerEntity.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(this.syncId, this.nextRevision(), 0, resultStack));
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        if (!this.filling) {
            this.context.run((world, pos) -> {
                if (world instanceof ServerWorld serverWorld) {
                    updateResult(serverWorld, null);
                }
            });
        }
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slotAtIndex = this.slots.get(slot);
        if (slotAtIndex != null && slotAtIndex.hasStack() && slotAtIndex.canTakeItems(player)) {
            ItemStack itemStackAtIndex = slotAtIndex.getStack();
            itemStack = itemStackAtIndex.copy();
            if (slot == OUTPUT_SLOT) {
                itemStackAtIndex.getItem().onCraftByPlayer(itemStackAtIndex, player);
                if (!this.insertItem(itemStackAtIndex, INVENTORY_SLOTS_START, HOTBAR_SLOTS_END, true)) {
                    return ItemStack.EMPTY;
                }

                slotAtIndex.onQuickTransfer(itemStackAtIndex, itemStack);
            } else if (slot >= INVENTORY_SLOTS_START && slot < HOTBAR_SLOTS_END) {
                if (!this.insertItem(itemStackAtIndex, INPUT_SLOTS_START, INVENTORY_SLOTS_START, false)) {
                    if (slot < HOTBAR_SLOTS_START) {
                        if (!this.insertItem(itemStackAtIndex, HOTBAR_SLOTS_START, HOTBAR_SLOTS_END, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!this.insertItem(itemStackAtIndex, INVENTORY_SLOTS_START, HOTBAR_SLOTS_START, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.insertItem(itemStackAtIndex, INVENTORY_SLOTS_START, HOTBAR_SLOTS_END, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStackAtIndex.isEmpty()) {
                slotAtIndex.setStack(ItemStack.EMPTY);
            } else {
                slotAtIndex.markDirty();
            }

            if (itemStackAtIndex.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slotAtIndex.onTakeItem(player, itemStackAtIndex);
            if (slot == OUTPUT_SLOT) {
                player.dropItem(itemStackAtIndex, false);
            }
        }

        return itemStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return canUse(this.context, player, ModBlocks.REVOLUTION_TABLE);
    }

    @Override
    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        return slot.inventory != this.resultInventory && super.canInsertIntoSlot(stack, slot);
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.context.run((world, pos) -> this.dropInventory(player, this.inventory));
    }

    @SuppressWarnings("unchecked")
    @Override
    public PostFillAction fillInputSlots(
            boolean craftAll, boolean creative, RecipeEntry<?> recipe, ServerWorld world, PlayerInventory inventory
    ) {
        RecipeEntry<RevolutionTableRecipe> recipeEntry = (RecipeEntry<RevolutionTableRecipe>)recipe;

        this.onInputSlotFillStart();

        List<Slot> inputSlots = this.getInputSlots();
        PostFillAction postFillAction = new RevolutionTableInputSlotsFiller(inputSlots, inventory, recipeEntry, craftAll, creative).fill();

        this.onInputSlotFillFinish(world, recipeEntry);

        return postFillAction;
    }

    @Override
    public void populateRecipeFinder(RecipeFinder finder) {
        this.inventory.provideRecipeInputs(finder);
    }

    @Override
    public RecipeBookType getCategory() {
        return RecipeBookType.CRAFTING; // Return crafting because making a RecipeBookType is impossible.
    }

    public void onInputSlotFillStart() {
        this.filling = true;
    }

    public void onInputSlotFillFinish(ServerWorld world, RecipeEntry<RevolutionTableRecipe> recipe) {
        this.filling = false;
        updateResult(world, recipe);
    }

    public List<Slot> getInputSlots() {
        return this.slots.subList(INPUT_SLOTS_START, INPUT_SLOTS_END);
    }

    public PlayerEntity getPlayer() {
        return this.player;
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

    private class CustomSlot extends Slot {
        private RevolutionTableScreenHandler _table;
        public CustomSlot(RevolutionTableScreenHandler altar, Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
            _table = altar;
        }

        @Override
        public void markDirty() {
            super.markDirty();
            _table.onContentChanged(_table.inventory);
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
                    .getFirstMatch(ModRecipeTypes.REVOLUTION_TABLE_RECIPE_TYPE, input, serverWorld)
                    .map(recipe -> ((RevolutionTableRecipe)recipe.value()).getRecipeRemainders(input))
                    .orElseGet(() -> copyInput(input))
                    : RevolutionTableRecipe.collectRecipeRemainders(input);
        }

        @Override
        public void onTakeItem(PlayerEntity player, ItemStack stack) {
            this.onCrafted(stack);
            RevolutionTableRecipeInput recipeInput = RevolutionTableRecipeInput.create(this.input.getHeldStacks());
            DefaultedList<ItemStack> defaultedList = this.getRecipeRemainders(recipeInput, player.getWorld());

            for (int y = 0; y < MAX_WIDTH_AND_HEIGHT; y++) {
                for (int x = 0; x < MAX_WIDTH_AND_HEIGHT; x++) {
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

    private class RevolutionTableInputSlotsFiller {
        private final List<Slot> inputSlots;
        private final List<Slot> slotsToReturn;
        private final PlayerInventory inventory;
        private final RecipeEntry<RevolutionTableRecipe> recipe;
        private final boolean craftAll;
        private final boolean creative;

        public RevolutionTableInputSlotsFiller(
                List<Slot> inputSlots,
                PlayerInventory inventory,
                RecipeEntry<RevolutionTableRecipe> recipe,
                boolean craftAll,
                boolean creative
        ) {
            this(inputSlots, inputSlots, inventory, recipe, craftAll, creative);
        }

        public RevolutionTableInputSlotsFiller(
                List<Slot> inputSlots,
                List<Slot> slotsToReturn,
                PlayerInventory inventory,
                RecipeEntry<RevolutionTableRecipe> recipe,
                boolean craftAll,
                boolean creative
        ) {
            this.inputSlots = inputSlots;
            this.slotsToReturn = slotsToReturn;
            this.inventory = inventory;
            this.recipe = recipe;
            this.craftAll = craftAll;
            this.creative = creative;
        }

        public AbstractRecipeScreenHandler.PostFillAction fill() {
            if (!creative && !canReturnInputs()) {
                return AbstractRecipeScreenHandler.PostFillAction.NOTHING;
            } else {
                RecipeFinder recipeFinder = new RecipeFinder();
                inventory.populateRecipeFinder(recipeFinder);
                populateRecipeFinder(recipeFinder);
                return tryFill(recipe, recipeFinder);
            }
        }

        public void clear() {
            RevolutionTableScreenHandler.this.resultInventory.clear();
            RevolutionTableScreenHandler.this.inventory.clear();
        }

        public boolean matches(RecipeEntry<RevolutionTableRecipe> entry) {
            return entry.value()
                    .matches(RevolutionTableRecipeInput.create(RevolutionTableScreenHandler.this.inventory.getHeldStacks()), getPlayer().getWorld());
        }

        private AbstractRecipeScreenHandler.PostFillAction tryFill(RecipeEntry<RevolutionTableRecipe> recipe, RecipeFinder finder) {
            if (finder.isCraftable(recipe.value(), null)) {
                this.fill(recipe, finder);
                this.inventory.markDirty();
                return AbstractRecipeScreenHandler.PostFillAction.NOTHING;
            } else {
                this.returnInputs();
                this.inventory.markDirty();
                return AbstractRecipeScreenHandler.PostFillAction.PLACE_GHOST_RECIPE;
            }
        }

        private void returnInputs() {
            for (Slot slot : this.slotsToReturn) {
                ItemStack itemStack = slot.getStack().copy();
                this.inventory.offer(itemStack, false);
                slot.setStackNoCallbacks(itemStack);
            }
            clear();
        }

        private void fill(RecipeEntry<RevolutionTableRecipe> recipe, RecipeFinder finder) {
            boolean match = matches(recipe);
            int i = finder.countCrafts(recipe.value(), null);
            if (match) {
                for (Slot slot : this.inputSlots) {
                    ItemStack itemStack = slot.getStack();
                    if (!itemStack.isEmpty() && Math.min(i, itemStack.getMaxCount()) < itemStack.getCount() + 1) {
                        return;
                    }
                }
            }

            int j = this.calculateCraftAmount(i, match);
            List<RegistryEntry<Item>> entries = new ArrayList<RegistryEntry<Item>>();
            boolean isCraftable = finder.isCraftable(recipe.value(), j, entries::add);
            if (isCraftable) {
                int k = clampToMaxCount(j, entries);
                if (k != j) {
                    entries.clear();
                    if (!finder.isCraftable(recipe.value(), k, entries::add)) {
                        return;
                    }
                }

                this.returnInputs();
                RevolutionTableRecipe recipeValue = recipe.value();
                IntList placementSlots = recipeValue.getIngredientPlacement().getPlacementSlots();
                RegistryEntry<Item> brimstoneRegistryEntry = entries.get(placementSlots.getInt(inputSlots.size()));
                for (int index = 0; index < inputSlots.size(); index++) {
                    Slot slot = inputSlots.get(index);
                    int placementSlot = placementSlots.getInt(index);
                    RegistryEntry<Item> registryEntry = entries.get(placementSlot);
                    int jx = k;

                    while (jx > 0) {
                        jx = this.fillInputSlot(slot, registryEntry, jx);
                        if (jx == -1) {
                            return;
                        }
                    }
                }
            }
        }

        private static int clampToMaxCount(int count, List<RegistryEntry<Item>> entries) {
            for (RegistryEntry<Item> registryEntry : entries) {
                count = Math.min(count, registryEntry.value().getMaxCount());
            }

            return count;
        }

        private int calculateCraftAmount(int forCraftAll, boolean match) {
            if (this.craftAll) {
                return forCraftAll;
            } else if (match) {
                int i = Integer.MAX_VALUE;

                for (Slot slot : this.inputSlots) {
                    ItemStack itemStack = slot.getStack();
                    if (!itemStack.isEmpty() && i > itemStack.getCount()) {
                        i = itemStack.getCount();
                    }
                }

                if (i != Integer.MAX_VALUE) {
                    i++;
                }

                return i;
            } else {
                return 1;
            }
        }

        private int fillInputSlot(Slot slot, RegistryEntry<Item> item, int count) {
            ItemStack itemStack = slot.getStack();
            int i = this.inventory.getMatchingSlot(item, itemStack);
            if (i == -1) {
                return -1;
            } else {
                ItemStack itemStackAtIndex = this.inventory.getStack(i);
                ItemStack itemStack3;
                if (count < itemStackAtIndex.getCount()) {
                    itemStack3 = this.inventory.removeStack(i, count);
                } else {
                    itemStack3 = this.inventory.removeStack(i);
                }

                int j = itemStack3.getCount();
                if (itemStack.isEmpty()) {
                    slot.setStackNoCallbacks(itemStack3);
                } else {
                    itemStack.increment(j);
                }

                return count - j;
            }
        }

        private boolean canReturnInputs() {
            List<ItemStack> list = Lists.<ItemStack>newArrayList();
            int i = this.getFreeInventorySlots();

            for (Slot slot : this.inputSlots) {
                ItemStack itemStack = slot.getStack().copy();
                if (!itemStack.isEmpty()) {
                    int j = this.inventory.getOccupiedSlotWithRoomForStack(itemStack);
                    if (j == -1 && list.size() <= i) {
                        for (ItemStack itemStack2 : list) {
                            if (ItemStack.areItemsEqual(itemStack2, itemStack)
                                    && itemStack2.getCount() != itemStack2.getMaxCount()
                                    && itemStack2.getCount() + itemStack.getCount() <= itemStack2.getMaxCount()) {
                                itemStack2.increment(itemStack.getCount());
                                itemStack.setCount(0);
                                break;
                            }
                        }

                        if (!itemStack.isEmpty()) {
                            if (list.size() >= i) {
                                return false;
                            }

                            list.add(itemStack);
                        }
                    } else if (j == -1) {
                        return false;
                    }
                }
            }

            return true;
        }

        private int getFreeInventorySlots() {
            int i = 0;

            for (ItemStack itemStack : this.inventory.getMainStacks()) {
                if (itemStack.isEmpty()) {
                    i++;
                }
            }

            return i;
        }
    }
}