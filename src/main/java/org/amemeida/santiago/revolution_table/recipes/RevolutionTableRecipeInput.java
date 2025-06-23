package org.amemeida.santiago.revolution_table.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.recipe.input.RecipeInput;

import java.util.List;

public class RevolutionTableRecipeInput implements RecipeInput {
    private static final int INPUT_SLOTS = 12;
    private final List<ItemStack> stacks;
    private final RecipeFinder matcher = new RecipeFinder();
    private final int stackCount;

    private RevolutionTableRecipeInput(List<ItemStack> stacks) {
        this.stacks = stacks;
        int count = 0;

        for (int i = 0; i < INPUT_SLOTS; i++) {
            ItemStack itemStack = this.stacks.get(i);
            if (!itemStack.isEmpty()) {
                count++;
                this.matcher.addInput(itemStack, 1);
            }
        }

        this.stackCount = count;
    }

    public static RevolutionTableRecipeInput create(List<ItemStack> stacks) {
        return new RevolutionTableRecipeInput(stacks);
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return (ItemStack)this.stacks.get(slot);
    }

    @Override
    public int size() {
        return this.stacks.size();
    }

    @Override
    public boolean isEmpty() {
        return this.stackCount == 0;
    }

    public RecipeFinder getRecipeMatcher() {
        return this.matcher;
    }

    public List<ItemStack> getStacks() {
        return this.stacks;
    }

    public int getStackCount() {
        return this.stackCount;
    }

    public static boolean stacksEqual(List<ItemStack> left, List<ItemStack> right) {
        if (left.size() != right.size()) {
            return false;
        } else {
            for (int i = 0; i < left.size(); i++) {
                if (!ItemStack.areEqual((ItemStack)left.get(i), (ItemStack)right.get(i))) {
                    return false;
                }
            }

            return true;
        }
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else {
            return !(o instanceof RevolutionTableRecipeInput revolutionTableRecipeInput)
                    ? false
                    : this.stackCount == revolutionTableRecipeInput.stackCount
                    && stacksEqual(this.stacks, revolutionTableRecipeInput.stacks);
        }
    }

    public int hashCode() {
        int i = 0;

        for (ItemStack itemStack : this.stacks) {
            i = i * 31 + ItemStack.hashCode(itemStack);
        }

        return i;
    }

}
