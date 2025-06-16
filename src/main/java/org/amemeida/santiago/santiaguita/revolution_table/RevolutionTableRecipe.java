package org.amemeida.santiago.santiaguita.revolution_table;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.IngredientPlacement;
import net.minecraft.recipe.Recipe;
import org.jetbrains.annotations.Nullable;

import java.util.OptionalInt;

public class RevolutionTableRecipe implements Recipe<RevolutionTableRecipeInput> {
    final RawRecipe raw;
    public final ItemStack result;
    final OptionalInt cost;
    final String group;
    @Nullable
    private IngredientPlacement ingredientPlacement;

    public RevolutionTableRecipe(String group, RawRecipe raw, ItemStack result){
        this(group, raw, result, OptionalInt.empty());
    }

    public RevolutionTableRecipe(String group, RawRecipe raw, ItemStack result){
        this.group = group;
        this.raw = raw;
        this.result = result;
        this.cost = cost;
    }


}
