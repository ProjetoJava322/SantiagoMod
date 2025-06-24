package org.amemeida.santiago.registry.recipes;


import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.amemeida.santiago.Santiago;

/**
 * Classe responsável pelo registro das categorias de livros de receitas customizadas do mod.
 */
public class ModRecipeBooks {

    /**
     * Categoria do livro de receitas para a Mesa de Revolução.
     * Registrada com um identificador único no registro de categorias de livros de receita.
     */
    public static final RecipeBookCategory REVOLUTION_TABLE_BOOK_CATEGORY = Registry.register(
            Registries.RECIPE_BOOK_CATEGORY,
            Identifier.of(Santiago.MOD_ID, "revolution_table"),
            new RecipeBookCategory() {
                @Override
                public String toString() {
                    return "REVOLUTION_TABLE";
                }
            });

    /**
     * Método de inicialização vazio para garantir o carregamento da classe.
     */
    public static void initialize() {}
}

