package org.amemeida.santiago.client.screens;

import net.minecraft.client.gui.screen.ingame.RecipeBookScreen;
import net.minecraft.client.gui.screen.recipebook.AbstractCraftingRecipeBookWidget;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.client.recipebook.RecipeBookType;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeBookCategories;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.amemeida.santiago.Santiago;
import org.amemeida.santiago.registry.items.ModItems;
import org.amemeida.santiago.registry.recipes.ModRecipeBooks;
import org.amemeida.santiago.santiaguita.encubadora.IncubatorScreenHandler;
import org.amemeida.santiago.santiaguita.revolution_table.RevolutionTableScreenHandler;

import java.util.List;

public class RevolutionTableScreen extends RecipeBookScreen<RevolutionTableScreenHandler> {
    private static final Identifier TEXTURE = Identifier.of(Santiago.MOD_ID, "textures/gui/revolution_table_gui.png");
    private static final List<RecipeBookWidget.Tab> TABS = List.of(
            new RecipeBookWidget.Tab(RecipeBookType.CRAFTING),
            new RecipeBookWidget.Tab(ModItems.HAMSTER, ModRecipeBooks.REVOLUTION_TABLE_RECIPE_BOOK_CATEGORY)
    );

    public RevolutionTableScreen(RevolutionTableScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, new AbstractCraftingRecipeBookWidget(handler), inventory, title);
    }
}
