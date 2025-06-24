package org.amemeida.santiago.registry.recipes;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.amemeida.santiago.Santiago;
import org.amemeida.santiago.incubator.recipes.IncubatorRecipe;
import org.amemeida.santiago.revolution_table.recipes.RevolutionTableRecipe;

/**
 * Classe responsável pelo registro dos tipos de receita customizados do mod.
 */
public class ModRecipeTypes {

    /**
     * Tipo de receita para a Incubadora.
     * Registrado com um identificador único no registro de tipos de receita.
     */
    public static final RecipeType<IncubatorRecipe> INCUBATOR_RECIPE_TYPE = Registry.register(
            Registries.RECIPE_TYPE,
            Identifier.of(Santiago.MOD_ID, "incubator"),
            new RecipeType<IncubatorRecipe>() {
                @Override
                public String toString() {
                    return "incubator";
                }
            });

    /**
     * Tipo de receita para a Mesa de Revolução.
     * Registrado com um identificador único no registro de tipos de receita.
     */
    public static final RecipeType<RevolutionTableRecipe> REVOLUTION_TABLE_RECIPE_TYPE = Registry.register(
            Registries.RECIPE_TYPE,
            Identifier.of(Santiago.MOD_ID, "revolution_table"),
            new RecipeType<RevolutionTableRecipe>() {
                @Override
                public String toString() {
                    return "revolution_table";
                }
            });

    /**
     * Método auxiliar para registrar um tipo de receita vanilla usando um ID.
     * @param id o identificador do tipo de receita
     * @return o tipo de receita registrado
     */
    private static <T extends Recipe<?>> RecipeType<T> register(String id) {
        return Registry.register(
                Registries.RECIPE_TYPE,
                Identifier.ofVanilla(id),
                new RecipeType<T>() {
                    @Override
                    public String toString() {
                        return id;
                    }
                });
    }

    /**
     * Método auxiliar para registrar um tipo de receita customizado.
     * @param id o identificador do tipo de receita
     * @param type o tipo de receita a ser registrado
     * @return o tipo de receita registrado
     */
    private static RecipeType<?> register(String id, RecipeType<?> type) {
        return Registry.register(Registries.RECIPE_TYPE, Identifier.of(Santiago.MOD_ID, id), type);
    }

    /**
     * Método de inicialização vazio para garantir o carregamento da classe.
     */
    public static void initialize() {}
}

