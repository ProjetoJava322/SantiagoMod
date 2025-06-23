package org.amemeida.santiago.incubator.recipes;

import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.amemeida.santiago.Santiago;

public class ModRecipes {
    public static final RecipeSerializer<IncubatorRecipe> INCUBATOR_SERIALIZER = Registry.register(
            Registries.RECIPE_SERIALIZER, Identifier.of(Santiago.MOD_ID, "incubator"),
            new IncubatorRecipe.Serializer());
    public static final RecipeType<IncubatorRecipe> INCUBATOR_TYPE = Registry.register(
            Registries.RECIPE_TYPE, Identifier.of(Santiago.MOD_ID, "incubator"), new RecipeType<IncubatorRecipe>() {
                @Override
                public String toString() {
                    return "incubator";
                }
            });
}