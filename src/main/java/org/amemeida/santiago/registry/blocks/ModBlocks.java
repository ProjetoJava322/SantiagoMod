package org.amemeida.santiago.registry.blocks;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import org.amemeida.santiago.Santiago;
import org.amemeida.santiago.computer.Computer;
import org.amemeida.santiago.registry.items.ModGroups;
import org.amemeida.santiago.incubator.IncubatorBlock;
import org.amemeida.santiago.revolution_table.RevolutionTableBlock;

import java.util.function.Function;

public class ModBlocks {

    public static final Block SANTIAGUITA_ORE = register("santiaguita_ore",
            AbstractBlock.Settings.create().requiresTool().strength(5f));
    public static final Block DEEPSLATE_SANTIAGUITA_ORE = register("deepslate_santiaguita_ore",
            AbstractBlock.Settings.create().requiresTool().strength(5.5f));

    public static final Block INCUBATOR = register("incubator",
            IncubatorBlock::new, AbstractBlock.Settings.create().requiresTool().strength(3f));

    public static final Block COMPUTER = register("computer",
            Computer::new, AbstractBlock.Settings.create().strength(1f));

    public static final Block REVOLUTION_TABLE = register("revolution_table",
            RevolutionTableBlock::new, AbstractBlock.Settings.create().requiresTool().strength(3f));

    public static final Block SANTIAGUITA_BLOCK = register("santiaguita_block",
            AbstractBlock.Settings.create().requiresTool().strength(7f));

    private static Block register(String name) {
        return register(name, Block::new, AbstractBlock.Settings.create());
    }

    private static Block register(String name, AbstractBlock.Settings settings) {
        return register(name, Block::new, settings);
    }

    // private static Block register(String name, AbstractBlock.Settings settings,
    // boolean shouldRegisterItem) {
    // return register(name, Block::new, settings, shouldRegisterItem);
    // }

    // private static Block register(String name, Function<AbstractBlock.Settings,
    // Block> blockFactory) {
    // return register(name, blockFactory, AbstractBlock.Settings.create());
    // }

    private static Block register(String name, Function<AbstractBlock.Settings, Block> blockFactory,
            AbstractBlock.Settings settings) {
        return register(name, blockFactory, settings, true);
    }

    private static Block register(String name, Function<AbstractBlock.Settings, Block> blockFactory,
            AbstractBlock.Settings settings, boolean shouldRegisterItem) {
        // Create a registry key for the block
        RegistryKey<Block> blockKey = keyOfBlock(name);
        // Create the block instance
        Block block = blockFactory.apply(settings.registryKey(blockKey));

        // Sometimes, you may not want to register an item for the block.
        // Eg: if it's a technical block like `minecraft:moving_piston` or
        // `minecraft:end_gateway`
        if (shouldRegisterItem) {
            // Items need to be registered with a different type of registry key, but the ID
            // can be the same.
            RegistryKey<Item> itemKey = keyOfItem(name);

            BlockItem blockItem = new BlockItem(block, new Item.Settings().registryKey(itemKey));

            ItemGroupEvents.modifyEntriesEvent(ModGroups.DEFAULT_GROUP_KEY).register(group -> group.add(blockItem));

            Registry.register(Registries.ITEM, itemKey, blockItem);
        }

        return Registry.register(Registries.BLOCK, blockKey, block);
    }

    private static RegistryKey<Block> keyOfBlock(String name) {
        return RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Santiago.MOD_ID, name));
    }

    private static RegistryKey<Item> keyOfItem(String name) {
        return RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Santiago.MOD_ID, name));
    }

    public static void initialize() {
    }
}