package org.amemeida.santiago.registry.recipes;

import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.amemeida.santiago.Santiago;
import org.amemeida.santiago.incubator.recipes.IncubatorRecipe;
import org.amemeida.santiago.revolution_table.recipes.RevolutionTableRecipe;

/**
 * Classe responsável pelo registro dos serializers de receitas customizadas do mod.
 */
public class ModRecipeSerializers {

    /**
     * Serializer para a receita da Incubadora.
     * Registrado com um identificador único no registro de serializers de receita.
     */
    public static final RecipeSerializer<IncubatorRecipe> INCUBATOR_RECIPE_SERIALIZER = Registry.register(
            Registries.RECIPE_SERIALIZER,
            Identifier.of(Santiago.MOD_ID, "incubator"),
            new IncubatorRecipe.Serializer());

    /**
     * Serializer para a receita da Mesa de Revolução.
     * Registrado com um identificador único no registro de serializers de receita.
     */
    public static final RecipeSerializer<RevolutionTableRecipe> REVOLUTION_TABLE_RECIPE_SERIALIZER = Registry.register(
            Registries.RECIPE_SERIALIZER,
            Identifier.of(Santiago.MOD_ID, "revolution_table"),
            new RevolutionTableRecipe.Serializer());

    /**
     * Método de inicialização vazio para garantir o carregamento da classe.
     */
    public static void initialize() {}
}

