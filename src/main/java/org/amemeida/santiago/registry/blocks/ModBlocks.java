package org.amemeida.santiago.registry.blocks;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
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

/**
 * Classe responsável pelo registro dos blocos customizados do mod.
 * Cada bloco é registrado junto com seu Item correspondente para uso no inventário.
 */
public class ModBlocks {

    /**
     * Bloco de minério Santiaguita.
     */
    public static final Block SANTIAGUITA_ORE = register("santiaguita_ore",
            AbstractBlock.Settings.create().requiresTool().strength(5f));

    /**
     * Bloco de minério Santiaguita na variante de deepslate.
     */
    public static final Block DEEPSLATE_SANTIAGUITA_ORE = register("deepslate_santiaguita_ore",
            AbstractBlock.Settings.create().requiresTool().strength(5.5f));

    /**
     * Bloco incubadora, associado à classe IncubatorBlock.
     */
    public static final Block INCUBATOR = register("incubator",
            IncubatorBlock::new, AbstractBlock.Settings.create().requiresTool().strength(3f));

    /**
     * Bloco computador, associado à classe Computer.
     */
    public static final Block COMPUTER = register("computer",
            Computer::new, AbstractBlock.Settings.create().strength(1f));

    /**
     * Bloco mesa de revolução, associado à classe RevolutionTableBlock.
     */
    public static final Block REVOLUTION_TABLE = register("revolution_table",
            RevolutionTableBlock::new, AbstractBlock.Settings.create().requiresTool().strength(3f));

    /**
     * Bloco sólido de Santiaguita.
     */
    public static final Block SANTIAGUITA_BLOCK = register("santiaguita_block",
            AbstractBlock.Settings.create().requiresTool().strength(7f));

    /**
     * Registra um bloco simples sem fábrica customizada e com item associado.
     *
     * @param name     nome do bloco
     * @param settings configurações do bloco
     * @return bloco registrado
     */
    private static Block register(String name, AbstractBlock.Settings settings) {
        return register(name, Block::new, settings);
    }

    /**
     * Registra um bloco com fábrica customizada e com item associado.
     *
     * @param name         nome do bloco
     * @param blockFactory função fábrica para criar o bloco
     * @param settings     configurações do bloco
     * @return bloco registrado
     */
    private static Block register(String name, Function<AbstractBlock.Settings, Block> blockFactory,
                                  AbstractBlock.Settings settings) {
        return register(name, blockFactory, settings, true);
    }

    /**
     * Registra um bloco com controle sobre se o item será registrado.
     *
     * @param name               nome do bloco
     * @param blockFactory       função fábrica para criar o bloco
     * @param settings           configurações do bloco
     * @param shouldRegisterItem indica se o item do bloco será registrado
     * @return bloco registrado
     */
    private static Block register(String name, Function<AbstractBlock.Settings, Block> blockFactory,
                                  AbstractBlock.Settings settings, boolean shouldRegisterItem) {
        RegistryKey<Block> blockKey = keyOfBlock(name);
        Block block = blockFactory.apply(settings.registryKey(blockKey));

        if (shouldRegisterItem) {
            RegistryKey<Item> itemKey = keyOfItem(name);
            BlockItem blockItem = new BlockItem(block, new Item.Settings().registryKey(itemKey));

            // Adiciona o item do bloco ao grupo padrão de itens do mod
            ItemGroupEvents.modifyEntriesEvent(ModGroups.DEFAULT_GROUP_KEY).register(group -> group.add(blockItem));

            Registry.register(Registries.ITEM, itemKey, blockItem);
        }

        return Registry.register(Registries.BLOCK, blockKey, block);
    }

    /**
     * Retorna a chave de registro para o bloco com o nome fornecido.
     *
     * @param name nome do bloco
     * @return chave de registro do bloco
     */
    private static RegistryKey<Block> keyOfBlock(String name) {
        return RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Santiago.MOD_ID, name));
    }

    /**
     * Retorna a chave de registro para o item do bloco com o nome fornecido.
     *
     * @param name nome do item/bloco
     * @return chave de registro do item
     */
    private static RegistryKey<Item> keyOfItem(String name) {
        return RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Santiago.MOD_ID, name));
    }

    /**
     * Método de inicialização vazio para garantir carregamento da classe.
     */
    public static void initialize() {
    }
}
