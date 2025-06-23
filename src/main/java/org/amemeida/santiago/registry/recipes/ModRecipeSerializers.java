package org.amemeida.santiago.registry.recipes;

import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.amemeida.santiago.Santiago;
import org.amemeida.santiago.santiaguita.encubadora.recipes.IncubatorRecipe;
import org.amemeida.santiago.santiaguita.revolution_table.recipes.RevolutionTableRecipe;

public class ModRecipeSerializers {
    public static final RecipeSerializer<IncubatorRecipe> INCUBATOR_RECIPE_SERIALIZER = Registry.register(Registries.RECIPE_SERIALIZER, Identifier.of(Santiago.MOD_ID, "incubator"), new IncubatorRecipe.Serializer());
    public static final RecipeSerializer<RevolutionTableRecipe> REVOLUTION_TABLE_RECIPE_SERIALIZER = Registry.register(Registries.RECIPE_SERIALIZER, Identifier.of(Santiago.MOD_ID, "revolution_table"), new RevolutionTableRecipe.Serializer());
    public static void initialize() {}
}
