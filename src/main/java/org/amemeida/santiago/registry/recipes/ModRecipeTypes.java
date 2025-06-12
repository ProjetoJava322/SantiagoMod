package org.amemeida.santiago.registry.recipes;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModRecipeTypes {
    private static <T extends Recipe<?>> RecipeType<T> register(String id) {
        return Registry.register(Registries.RECIPE_TYPE, Identifier.ofVanilla(id), new RecipeType<T>() {
            public String toString() {
                return id;
            }
        });
    }

    public static void initialize() {}
}
