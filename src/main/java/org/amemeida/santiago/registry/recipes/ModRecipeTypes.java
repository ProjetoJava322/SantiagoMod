package org.amemeida.santiago.registry.recipes;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.amemeida.santiago.Santiago;
import org.amemeida.santiago.santiaguita.encubadora.recipes.IncubatorRecipe;

public class ModRecipeTypes {

    public static final RecipeType<IncubatorRecipe>  INCUBATOR_RECIPE_TYPE = Registry.register(Registries.RECIPE_TYPE, Identifier.of(Santiago.MOD_ID, "incubator"), new RecipeType<IncubatorRecipe>() {
        public String toString() {
            return "incubator";
        }
    });

    private static <T extends Recipe<?>> RecipeType<T> register(String id) {
        return Registry.register(Registries.RECIPE_TYPE, Identifier.ofVanilla(id), new RecipeType<T>() {
            public String toString() {
                return id;
            }
        });
    }

    private static RecipeType<?> register(String id, RecipeType<?> type){
        return Registry.register(Registries.RECIPE_TYPE, Identifier.of(Santiago.MOD_ID, id), type);
    }



    public static void initialize() {}
}
