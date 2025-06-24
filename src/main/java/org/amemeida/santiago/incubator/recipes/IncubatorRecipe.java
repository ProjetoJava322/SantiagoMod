package org.amemeida.santiago.incubator.recipes;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.RecipeBookCategories;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.amemeida.santiago.registry.recipes.ModRecipeSerializers;
import org.amemeida.santiago.registry.recipes.ModRecipeTypes;

/**
 * Representa uma receita do incubador, que possui um ingrediente de entrada (inputItem)
 * e um ItemStack de saída (output).
 * Implementa a interface Recipe para o tipo de entrada IncubatorRecipeInput.
 */
public record IncubatorRecipe(Ingredient inputItem, ItemStack output) implements Recipe<IncubatorRecipeInput> {

    /**
     * Retorna a lista de ingredientes desta receita.
     * Como esta receita possui apenas um ingrediente, retorna uma lista com ele.
     * 
     * @return lista contendo o ingrediente de entrada
     */
    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> list = DefaultedList.of();
        list.add(this.inputItem);
        return list;
    }

    /**
     * Verifica se a receita corresponde ao input fornecido.
     * Retorna falso se o mundo for cliente (client-side).
     * 
     * @param input o input da receita (geralmente inventário)
     * @param world o mundo onde a receita está sendo verificada
     * @return true se o ingrediente de entrada corresponder ao item no slot 0 do input
     */
    @Override
    public boolean matches(IncubatorRecipeInput input, World world) {
        if (world.isClient()) {
            return false;
        }

        return inputItem.test(input.getStackInSlot(0));
    }

    /**
     * Realiza o crafting da receita, retornando a saída definida.
     * Retorna uma cópia do ItemStack de saída para evitar modificações no original.
     * 
     * @param input o input da receita
     * @param lookup registro para busca de dados adicionais (geralmente ignorado aqui)
     * @return o ItemStack resultante do crafting
     */
    @Override
    public ItemStack craft(IncubatorRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        return output.copy();
    }

    /**
     * Retorna o serializador responsável por ler e escrever essa receita.
     * 
     * @return o serializador da receita do incubador
     */
    @Override
    public RecipeSerializer<? extends Recipe<IncubatorRecipeInput>> getSerializer() {
        return ModRecipeSerializers.INCUBATOR_RECIPE_SERIALIZER;
    }

    /**
     * Retorna o tipo da receita, usado para categorizar e registrar a receita.
     * 
     * @return o tipo da receita do incubador
     */
    @Override
    public RecipeType<? extends Recipe<IncubatorRecipeInput>> getType() {
        return ModRecipeTypes.INCUBATOR_RECIPE_TYPE;
    }

    /**
     * Retorna a posição dos ingredientes na interface de receita,
     * neste caso, um único slot com o ingrediente de entrada.
     * 
     * @return posicionamento do ingrediente
     */
    @Override
    public IngredientPlacement getIngredientPlacement() {
        return IngredientPlacement.forSingleSlot(inputItem);
    }

    /**
     * Retorna a categoria da receita para o livro de receitas.
     * 
     * @return categoria misc de crafting
     */
    @Override
    public RecipeBookCategory getRecipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    /**
     * Classe interna que implementa o serializador da receita do incubador,
     * responsável por codificar e decodificar a receita para armazenamento e rede.
     */
    public static class Serializer implements RecipeSerializer<IncubatorRecipe> {
        // Codec para serialização baseada em registros (usado para salvar/ler receitas)
        public static final MapCodec<IncubatorRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC.fieldOf("ingredient").forGetter(IncubatorRecipe::inputItem),
                ItemStack.CODEC.fieldOf("result").forGetter(IncubatorRecipe::output)
        ).apply(inst, IncubatorRecipe::new));

        // Codec para transmissão via pacote de rede (packet)
        public static final PacketCodec<RegistryByteBuf, IncubatorRecipe> STREAM_CODEC =
                PacketCodec.tuple(
                        Ingredient.PACKET_CODEC, IncubatorRecipe::inputItem,
                        ItemStack.PACKET_CODEC, IncubatorRecipe::output,
                        IncubatorRecipe::new);

        @Override
        public MapCodec<IncubatorRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, IncubatorRecipe> packetCodec() {
            return STREAM_CODEC;
        }
    }
}
