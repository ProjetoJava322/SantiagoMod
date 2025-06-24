package org.amemeida.santiago.incubator.recipes;

import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.amemeida.santiago.Santiago;

/**
 * Classe responsável pelo registro dos tipos e serializadores de receita personalizados do mod.
 * Neste caso, registra a receita e o tipo de receita para o incubador.
 */
public class ModRecipes {

    /**
     * Serializador da receita do incubador.
     * Registra no sistema de receitas do Minecraft um serializador para a receita do incubador,
     * permitindo salvar e carregar essa receita do arquivo de dados do jogo.
     */
    public static final RecipeSerializer<IncubatorRecipe> INCUBATOR_SERIALIZER = Registry.register(
            Registries.RECIPE_SERIALIZER, 
            Identifier.of(Santiago.MOD_ID, "incubator"), 
            new IncubatorRecipe.Serializer());

    /**
     * Tipo da receita do incubador.
     * Registra o tipo de receita "incubator", que será usado para identificar receitas
     * do incubador em todo o jogo.
     */
    public static final RecipeType<IncubatorRecipe> INCUBATOR_TYPE = Registry.register(
            Registries.RECIPE_TYPE, 
            Identifier.of(Santiago.MOD_ID, "incubator"), 
            new RecipeType<IncubatorRecipe>() {
                @Override
                public String toString() {
                    return "incubator";
                }
            });
}
