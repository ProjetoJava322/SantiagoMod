package org.amemeida.santiago.santiaguita.revolution_table;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.recipe.display.SlotDisplay;
import net.minecraft.resource.featuretoggle.FeatureSet;

import java.util.List;

public record RevolutionTableRecipeDisplay(List<SlotDisplay> ingredients, SlotDisplay.StackSlotDisplay result, SlotDisplay.ItemSlotDisplay craftingStation) implements RecipeDisplay {
        public static final MapCodec<RevolutionTableRecipeDisplay> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                                SlotDisplay.CODEC.listOf().fieldOf("ingredients").forGetter(RevolutionTableRecipeDisplay::ingredients),
                                SlotDisplay.StackSlotDisplay.CODEC.fieldOf("result").forGetter(RevolutionTableRecipeDisplay::result),
                                SlotDisplay.ItemSlotDisplay.CODEC.fieldOf("crafting_station").forGetter(RevolutionTableRecipeDisplay::craftingStation)
                        )
                        .apply(instance, RevolutionTableRecipeDisplay::new)
        );
        public static final PacketCodec<RegistryByteBuf, RevolutionTableRecipeDisplay> PACKET_CODEC = PacketCodec.tuple(
                SlotDisplay.PACKET_CODEC.collect(PacketCodecs.toList()),
                RevolutionTableRecipeDisplay::ingredients,
                SlotDisplay.StackSlotDisplay.PACKET_CODEC,
                RevolutionTableRecipeDisplay::result,
                SlotDisplay.ItemSlotDisplay.PACKET_CODEC,
                RevolutionTableRecipeDisplay::craftingStation,
                RevolutionTableRecipeDisplay::new
        );
        public static final RecipeDisplay.Serializer<RevolutionTableRecipeDisplay> SERIALIZER = new RecipeDisplay.Serializer<>(CODEC, PACKET_CODEC);

        public static final int MAX_INGREDIENTS = 12;

	public RevolutionTableRecipeDisplay(List<SlotDisplay> ingredients, SlotDisplay.StackSlotDisplay result, SlotDisplay.ItemSlotDisplay craftingStation) {
            List<SlotDisplay> subbedIngredients = ingredients.subList(0, MAX_INGREDIENTS);
            if (subbedIngredients.size() != MAX_INGREDIENTS) {
                throw new IllegalArgumentException("Invalid shaped recipe display contents");
            } else {
                this.ingredients = subbedIngredients;
                this.result = result;
                this.craftingStation = craftingStation;
            }
        }

        @Override
        public RecipeDisplay.Serializer<RevolutionTableRecipeDisplay> serializer() {
            return SERIALIZER;
        }

        @Override
        public boolean isEnabled(FeatureSet features) {
            return this.ingredients.stream().allMatch(ingredient -> ingredient.isEnabled(features)) && RecipeDisplay.super.isEnabled(features);
        }
}
