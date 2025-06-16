package org.amemeida.santiago.santiaguita.revolution_table;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.chars.CharArraySet;
import it.unimi.dsi.fastutil.chars.CharSet;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.recipe.display.SlotDisplay;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.World;
import org.amemeida.santiago.registry.blocks.ModBlockEntities;
import org.amemeida.santiago.registry.blocks.ModBlocks;
import org.amemeida.santiago.registry.recipes.ModRecipeBooks;
import org.amemeida.santiago.registry.recipes.ModRecipeSerializers;
import org.amemeida.santiago.registry.recipes.ModRecipeTypes;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.VisibleForTesting;

import java.util.*;
import java.util.function.Function;

public class RevolutionTableRecipe implements Recipe<RevolutionTableRecipeInput> {
    final RawRecipe raw;
    public final ItemStack result;
    final String group;
    @Nullable
    private IngredientPlacement ingredientPlacement;

    public RevolutionTableRecipe(String group, RawRecipe raw, ItemStack result){
        this.group = group;
        this.raw = raw;
        this.result = result;
    }

    @Override
    public RecipeType<RevolutionTableRecipe> getType() {
        return ModRecipeTypes.REVOLUTION_TABLE_RECIPE_TYPE;
    }

    @Override
    public RecipeSerializer<? extends RevolutionTableRecipe> getSerializer() {
        return ModRecipeSerializers.REVOLUTION_TABLE_RECIPE_SERIALIZER;
    }
    @Override
    public RecipeBookCategory getRecipeBookCategory() {
        return ModRecipeBooks.REVOLUTION_TABLE_RECIPE_BOOK_CATEGORY;
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    @Override
    public boolean isIgnoredInRecipeBook() {
        return false;
    }

    @Override
    public boolean showNotification() {
        return true;
    }

    public DefaultedList<ItemStack> getRecipeRemainders(RevolutionTableRecipeInput input) {
        return collectRecipeRemainders(input);
    }

    public static DefaultedList<ItemStack> collectRecipeRemainders(RevolutionTableRecipeInput input) {
        DefaultedList<ItemStack> defaultedList = DefaultedList.ofSize(input.size(), ItemStack.EMPTY);

        for (int i = 0; i < defaultedList.size(); i++) {
            Item item = input.getStackInSlot(i).getItem();
            defaultedList.set(i, item.getRecipeRemainder());
        }

        return defaultedList;
    }

    @VisibleForTesting
    public List<Optional<Ingredient>> getIngredients() {
        return this.raw.getIngredients();
    }

    @Override
    public IngredientPlacement getIngredientPlacement() {
        if (this.ingredientPlacement == null) {
            this.ingredientPlacement = IngredientPlacement.forMultipleSlots(this.getIngredients());
        }

        return this.ingredientPlacement;
    }

    public boolean matches(RevolutionTableRecipeInput input, World world) {
        return this.raw.matches(input);
    }

    public ItemStack craft(RevolutionTableRecipeInput input, RegistryWrapper.WrapperLookup wrapperLookup) {
        return this.result.copy();
    }

    public ItemStack result() {
        return this.result;
    }

    @Override
    public List<RecipeDisplay> getDisplays() {
        return List.of(
                new RevolutionTableRecipeDisplay(
                        this.raw
                                .getIngredients()
                                .stream()
                                .map(ingredient -> (SlotDisplay)ingredient.map(Ingredient::toDisplay).orElse(SlotDisplay.EmptySlotDisplay.INSTANCE))
                                .toList(),
                        new SlotDisplay.StackSlotDisplay(this.result),
                        new SlotDisplay.ItemSlotDisplay(ModBlocks.REVOLUTION_TABLE.asItem())
                )
        );
    }

    public static class Serializer implements RecipeSerializer<RevolutionTableRecipe> {
        public static final MapCodec<RevolutionTableRecipe> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                                Codec.STRING.optionalFieldOf("group", "").forGetter(recipe -> recipe.group),
                                RawRecipe.CODEC.forGetter(recipe -> recipe.raw),
                                ItemStack.VALIDATED_CODEC.fieldOf("result").forGetter(recipe -> recipe.result)
                        )
                        .apply(instance, RevolutionTableRecipe::new)
        );
        public static final PacketCodec<RegistryByteBuf, RevolutionTableRecipe> PACKET_CODEC = PacketCodec.ofStatic(
                RevolutionTableRecipe.Serializer::write, RevolutionTableRecipe.Serializer::read
        );

        @Override
        public MapCodec<RevolutionTableRecipe> codec() {
            return CODEC;
        }

        @Deprecated
        @Override
        public PacketCodec<RegistryByteBuf, RevolutionTableRecipe> packetCodec() {
            return PACKET_CODEC;
        }

        private static RevolutionTableRecipe read(RegistryByteBuf buf) {
            String string = buf.readString();
            RawRecipe rawRecipe = RawRecipe.PACKET_CODEC.decode(buf);
            ItemStack result = ItemStack.PACKET_CODEC.decode(buf);
            return new RevolutionTableRecipe(string, rawRecipe, result);
        }

        private static void write(RegistryByteBuf buf, RevolutionTableRecipe recipe) {
            buf.writeString(recipe.group);
            RawRecipe.PACKET_CODEC.encode(buf, recipe.raw);
            ItemStack.PACKET_CODEC.encode(buf, recipe.result);
        }
    }

    public static final class RawRecipe {
        private static final int MAX_WIDTH_AND_HEIGHT = 4;
        public static final char SPACE = ' ';
        public static final MapCodec<RawRecipe> CODEC = RawRecipe.Data.CODEC
                .flatXmap(
                        RawRecipe::fromData,
                        recipe -> (DataResult<Data>)recipe.data.map(DataResult::success).orElseGet(() -> DataResult.error(() -> "Cannot encode unpacked recipe"))
                );
        public static final PacketCodec<RegistryByteBuf, RawRecipe> PACKET_CODEC = PacketCodec.tuple(
                Ingredient.OPTIONAL_PACKET_CODEC.collect(PacketCodecs.toList()),
                recipe -> recipe.ingredients,
                RawRecipe::create
        );
        private final List<Optional<Ingredient>> ingredients;
        private final Optional<RawRecipe.Data> data;
        private final int ingredientCount;

        public RawRecipe(List<Optional<Ingredient>> ingredients, Optional<RawRecipe.Data> data) {
            this.ingredients = ingredients;
            this.data = data;
            this.ingredientCount = (int)ingredients.stream().flatMap(Optional::stream).count();
        }

        private static RawRecipe create(List<Optional<Ingredient>> ingredients) {
            return new RawRecipe(ingredients, Optional.empty());
        }

        public static RawRecipe create(Map<Character, Ingredient> key, String... pattern) {
            return create(key, List.of(pattern));
        }

        public static RawRecipe create(Map<Character, Ingredient> key, List<String> pattern) {
            RawRecipe.Data data = new RawRecipe.Data(key, pattern);
            return fromData(data).getOrThrow();
        }

        private static DataResult<RawRecipe> fromData(RawRecipe.Data data) {
            String[] strings = removePadding(data.pattern);
            int i = strings[0].length();
            int j = strings.length;
            List<Optional<Ingredient>> list = new ArrayList<Optional<Ingredient>>(i * j);
            CharSet charSet = new CharArraySet(data.key.keySet());

            for (String string : strings) {
                for (int k = 0; k < string.length(); k++) {
                    char c = string.charAt(k);
                    Optional<Ingredient> optional;
                    if (c == ' ') {
                        optional = Optional.empty();
                    } else {
                        Ingredient ingredient = (Ingredient)data.key.get(c);
                        if (ingredient == null) {
                            return DataResult.error(() -> "Pattern references symbol '" + c + "' but it's not defined in the key");
                        }

                        optional = Optional.of(ingredient);
                    }

                    charSet.remove(c);
                    list.add(optional);
                }
            }

            return !charSet.isEmpty()
                    ? DataResult.error(() -> "Key defines symbols that aren't used in pattern: " + charSet)
                    : DataResult.success(new RawRecipe(list, Optional.of(data)));
        }

        /**
         * Removes empty space from around the recipe pattern.
         *
         * <p>Turns patterns such as:
         * <pre>
         * {@code
         * "   o"
         * "   a"
         * "	"
         * }
         * </pre>
         * Into:
         * <pre>
         * {@code
         * "o"
         * "a"
         * }
         * </pre>
         *
         * @return a new recipe pattern with all leading and trailing empty rows/columns removed
         */
        @VisibleForTesting
        static String[] removePadding(List<String> pattern) {
            // Trim each line
            List<String> trimmedLines = pattern.stream()
                    .map(String::trim)
                    .toList();

            // Remove leading empty lines
            int start = 0;
            while (start < trimmedLines.size() && trimmedLines.get(start).isEmpty()) {
                start++;
            }

            // Remove trailing empty lines
            int end = trimmedLines.size();
            while (end > start && trimmedLines.get(end - 1).isEmpty()) {
                end--;
            }

            // Return the cleaned pattern
            return trimmedLines.subList(start, end).toArray(new String[0]);
        }

        public boolean matches(RevolutionTableRecipeInput input) {
            if (input.getStackCount() != this.ingredientCount) {
                return false;
            } else {
                for (int i = 0; i < MAX_WIDTH_AND_HEIGHT; i++) {
                    final int fi = i + 1;
                    if (fi == MAX_WIDTH_AND_HEIGHT) {
                        Optional<Ingredient> optional = (Optional<Ingredient>)this.ingredients.get(i * MAX_WIDTH_AND_HEIGHT);

                        ItemStack itemStack = input.getStackInSlot(0, i);
                        if (!Ingredient.matches(optional, itemStack)) {
                            return false;
                        }
                    }
                    else {
                        for (int j = 0; j < MAX_WIDTH_AND_HEIGHT; j++) {
                            Optional<Ingredient> optional = (Optional<Ingredient>)this.ingredients.get(j + i * MAX_WIDTH_AND_HEIGHT);

                            ItemStack itemStack = input.getStackInSlot(j, i);
                            if (!Ingredient.matches(optional, itemStack)) {
                                return false;
                            }
                        }
                    }
                }

                return true;
            }
        }

        public List<Optional<Ingredient>> getIngredients() {
            return this.ingredients;
        }

        public record Data(Map<Character, Ingredient> key, List<String> pattern) {
            private static final Codec<List<String>> PATTERN_CODEC = Codec.STRING.listOf().comapFlatMap(pattern -> {
                if (pattern.size() > MAX_WIDTH_AND_HEIGHT) {
                    return DataResult.error(() -> "Invalid pattern: too many rows, 4 is maximum");
                } else if (pattern.isEmpty()) {
                    return DataResult.error(() -> "Invalid pattern: empty pattern not allowed");
                } else {
                    for (int i = 0; i < pattern.size(); i++) {
                        final int fi = i + 1;
                        String string = pattern.get(i);
                    }
                    return DataResult.success(pattern);
                }
            }, Function.identity());
            private static final Codec<Character> KEY_ENTRY_CODEC = Codec.STRING.comapFlatMap(keyEntry -> {
                if (keyEntry.length() != 1) {
                    return DataResult.error(() -> "Invalid key entry: '" + keyEntry + "' is an invalid symbol (must be 1 character only).");
                } else {
                    return " ".equals(keyEntry) ? DataResult.error(() -> "Invalid key entry: ' ' is a reserved symbol.") : DataResult.success(keyEntry.charAt(0));
                }
            }, String::valueOf);
            public static final MapCodec<RawRecipe.Data> CODEC = RecordCodecBuilder.mapCodec(
                    instance -> instance.group(
                                    Codecs.strictUnboundedMap(KEY_ENTRY_CODEC, Ingredient.CODEC).fieldOf("key").forGetter(data -> data.key),
                                    PATTERN_CODEC.fieldOf("pattern").forGetter(data -> data.pattern)
                            )
                            .apply(instance, RawRecipe.Data::new)
            );
        }
    }
}
