package org.amemeida.santiago.client.screens;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.recipebook.GhostRecipe;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.client.gui.screen.recipebook.RecipeResultCollection;
import net.minecraft.client.recipebook.RecipeBookType;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.recipe.RecipeGridAligner;
import net.minecraft.recipe.book.RecipeBookCategories;
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.recipe.display.ShapedCraftingRecipeDisplay;
import net.minecraft.recipe.display.ShapelessCraftingRecipeDisplay;
import net.minecraft.recipe.display.SlotDisplay;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.context.ContextParameterMap;
import org.amemeida.santiago.santiaguita.revolution_table.RevolutionTableScreenHandler;

import java.util.List;
import java.util.Objects;

import static com.sun.tools.javac.jvm.ByteCodes.invokedynamic;

@Environment(EnvType.CLIENT)
public class AbstractRevolutionRecipeBookWidget extends RecipeBookWidget<RevolutionTableScreenHandler> {
    private static final ButtonTextures TEXTURES = new ButtonTextures(Identifier.ofVanilla("recipe_book/filter_enabled"), Identifier.ofVanilla("recipe_book/filter_disabled"), Identifier.ofVanilla("recipe_book/filter_enabled_highlighted"), Identifier.ofVanilla("recipe_book/filter_disabled_highlighted"));
    private static final Text TOGGLE_CRAFTABLE_TEXT = Text.translatable("gui.recipebook.toggleRecipes.craftable");
    private static final List<RecipeBookWidget.Tab> TABS;

    public AbstractRevolutionRecipeBookWidget(RevolutionTableScreenHandler screenHandler) {
        super(screenHandler, TABS);
    }

    protected boolean isValid(Slot slot) {
        return ((RevolutionTableScreenHandler)this.craftingScreenHandler).getOutputSlot() == slot || ((RevolutionTableScreenHandler)this.craftingScreenHandler).getInputSlots().contains(slot);
    }

    private boolean canDisplay(RecipeDisplay display) {
        int i = ((RevolutionTableScreenHandler)this.craftingScreenHandler).getWidthAndHeight();
        int j = ((RevolutionTableScreenHandler)this.craftingScreenHandler).getWidthAndHeight();
        boolean var10000;
        switch (display) {
            case ShapedCraftingRecipeDisplay shapedCraftingRecipeDisplay -> var10000 = i >= shapedCraftingRecipeDisplay.width() && j >= shapedCraftingRecipeDisplay.height();
            case ShapelessCraftingRecipeDisplay shapelessCraftingRecipeDisplay -> var10000 = i * j >= shapelessCraftingRecipeDisplay.ingredients().size();
            default -> var10000 = false;
        }

        return var10000;
    }

    protected void showGhostRecipe(GhostRecipe ghostRecipe, RecipeDisplay display, ContextParameterMap context) {
        ghostRecipe.addResults(((RevolutionTableScreenHandler)this.craftingScreenHandler).getOutputSlot(), context, display.result());
        Objects.requireNonNull(display);
        byte var5 = 0;
        //$FF: var5->value
        //0->net/minecraft/recipe/display/ShapedCraftingRecipeDisplay
        //1->net/minecraft/recipe/display/ShapelessCraftingRecipeDisplay
        switch (display.typeSwitch<invokedynamic>(display, var5)) {
            case 0:
                ShapedCraftingRecipeDisplay shapedCraftingRecipeDisplay = (ShapedCraftingRecipeDisplay)display;
                List<Slot> list = ((RevolutionTableScreenHandler)this.craftingScreenHandler).getInputSlots();
                RecipeGridAligner.alignRecipeToGrid(((RevolutionTableScreenHandler)this.craftingScreenHandler).getWidth(), ((RevolutionTableScreenHandler)this.craftingScreenHandler).getHeight(), shapedCraftingRecipeDisplay.width(), shapedCraftingRecipeDisplay.height(), shapedCraftingRecipeDisplay.ingredients(), (slot, index, x, y) -> {
                    Slot slot2 = (Slot)list.get(index);
                    ghostRecipe.addInputs(slot2, context, slot);
                });
                break;
            case 1:
                ShapelessCraftingRecipeDisplay shapelessCraftingRecipeDisplay = (ShapelessCraftingRecipeDisplay)display;
                List<Slot> list2 = ((RevolutionTableScreenHandler)this.craftingScreenHandler).getInputSlots();
                int i = Math.min(shapelessCraftingRecipeDisplay.ingredients().size(), list2.size());

                for(int j = 0; j < i; ++j) {
                    ghostRecipe.addInputs((Slot)list2.get(j), context, (SlotDisplay)shapelessCraftingRecipeDisplay.ingredients().get(j));
                }
        }

    }

    protected void setBookButtonTexture() {
        this.toggleCraftableButton.setTextures(TEXTURES);
    }

    protected Text getToggleCraftableButtonText() {
        return TOGGLE_CRAFTABLE_TEXT;
    }

    protected void populateRecipes(RecipeResultCollection recipeResultCollection, RecipeFinder recipeFinder) {
        recipeResultCollection.populateRecipes(recipeFinder, this::canDisplay);
    }

    static {
        TABS = List.of(new RecipeBookWidget.Tab(RecipeBookType.CRAFTING), new RecipeBookWidget.Tab(Items.IRON_AXE, Items.GOLDEN_SWORD, RecipeBookCategories.CRAFTING_EQUIPMENT), new RecipeBookWidget.Tab(Items.BRICKS, RecipeBookCategories.CRAFTING_BUILDING_BLOCKS), new RecipeBookWidget.Tab(Items.LAVA_BUCKET, Items.APPLE, RecipeBookCategories.CRAFTING_MISC), new RecipeBookWidget.Tab(Items.REDSTONE, RecipeBookCategories.CRAFTING_REDSTONE));
    }
}
}
