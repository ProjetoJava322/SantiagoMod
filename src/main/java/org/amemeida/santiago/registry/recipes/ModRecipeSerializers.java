package org.amemeida.santiago.registry.recipes;

import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.amemeida.santiago.Santiago;
import org.amemeida.santiago.santiaguita.encubadora.recipes.IncubatorRecipe;

public class ModRecipeSerializers {

    public static final RecipeSerializer<IncubatorRecipe> INCUBATOR_RECIPE_SERIALIZER = Registry.register(Registries.RECIPE_SERIALIZER, Identifier.of(Santiago.MOD_ID, "incubator"), new IncubatorRecipe.Serializer());
    public static void initialize() {}
}
