package org.amemeida.santiago.santiaguita.encubadora.recipes;

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

public record IncubatorRecipe(Ingredient inputItem, ItemStack output) implements Recipe<IncubatorRecipeInput> {
    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> list = DefaultedList.of();
        list.add(this.inputItem);
        return list;
    }

    @Override
    public boolean matches(IncubatorRecipeInput input, World world) {
        if(world.isClient()) {
            return false;
        }

        return inputItem.test(input.getStackInSlot(0));
    }

    @Override
    public ItemStack craft(IncubatorRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        return output.copy();
    }

    @Override
    public RecipeSerializer<? extends Recipe<IncubatorRecipeInput>> getSerializer() {
        return ModRecipeSerializers.INCUBATOR_RECIPE_SERIALIZER;
    }

    @Override
    public RecipeType<? extends Recipe<IncubatorRecipeInput>> getType() {
        return ModRecipeTypes.INCUBATOR_RECIPE_TYPE;
    }

    @Override
    public IngredientPlacement getIngredientPlacement() {
        return IngredientPlacement.forSingleSlot(inputItem);
    }

    @Override
    public RecipeBookCategory getRecipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    public static class Serializer implements RecipeSerializer<IncubatorRecipe> {
        public static final MapCodec<IncubatorRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC.fieldOf("ingredient").forGetter(IncubatorRecipe::inputItem),
                ItemStack.CODEC.fieldOf("result").forGetter(IncubatorRecipe::output)
        ).apply(inst, IncubatorRecipe::new));

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