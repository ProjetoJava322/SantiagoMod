package org.amemeida.santiago.registry.recipes;

import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.amemeida.santiago.Santiago;

public class ModRecipeBooks {
    public static final RecipeBookCategory EFFIGY_ALTAR_RECIPE_BOOK_CATEGORY = Registry.register(Registries.RECIPE_BOOK_CATEGORY, Identifier.of(Santiago.MOD_ID, "revolution_table"), new RecipeBookCategory() {
        public String toString() {
            return "revolution_table";
        }
    });
    public static void initialize() {}
}
