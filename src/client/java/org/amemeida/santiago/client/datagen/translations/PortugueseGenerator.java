package org.amemeida.santiago.client.datagen.translations;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.registry.RegistryWrapper;
import org.amemeida.santiago.registry.blocks.ModBlocks;
import org.amemeida.santiago.registry.items.ModItems;

import java.util.concurrent.CompletableFuture;

public class PortugueseGenerator extends Translator {
    public PortugueseGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        // Specifying en_us is optional, as it's the default language code
        super(dataOutput, "pt_br", registryLookup);
    }

    @Override
    public void translate() {
        add("itemGroup.santiago", "Mod do Santiago");

        add(ModBlocks.INCUBATOR, "Encubadora");
        add(ModBlocks.REVOLUTION_TABLE, "Mesa da Revolução");

        add(ModBlocks.SANTIAGUITA_ORE, "Minério de Santiaguita");
        add(ModBlocks.DEEPSLATE_SANTIAGUITA_ORE, "Minério de Santiaguita de Ardosiabissal");

        add(ModItems.SANTIAGUITA_AXE, "Machado de Santiaguita");
        add(ModItems.SANTIAGUITA_HOE, "Enxada de Santiaguita");
        add(ModItems.SANTIAGUITA_PICKAXE, "Picareta de Santiaguita");
        add(ModItems.SANTIAGUITA_SWORD,  "Espada de Santiaguita");
        add(ModItems.SANTIAGUITA_SHOVEL, "Pá de Santiaguita");

        add(ModItems.SANTIAGUITA_HELMET, "Capacete de Santiaguita");
        add(ModItems.SANTIAGUITA_CHESTPLATE, "Peitoral de Santiaguita");
        add(ModItems.SANTIAGUITA_LEGGINGS, "Calça de Santiaguita");
        add(ModItems.SANTIAGUITA_BOOTS, "Botas de Santiaguita");

        add(ModItems.SANTIAGUITA_INGOT, "Barra de Santiaguita");
        add(ModItems.SANTIAGUITA_NUGGET, "Pepita de Santiaguita");
        add(ModBlocks.SANTIAGUITA_BLOCK, "Bloco de Santiaguita");
        add(ModItems.RAW_SANTIAGUITA, "Santiaguita Bruta");
        add(ModItems.UNDERLINE, "Guião Baixo");
        add(ModItems.SANTIAGOS_ANTHEM_MUSIC_DISC, "Disco Musical");
        addDesc(ModItems.SANTIAGOS_ANTHEM_MUSIC_DISC, "Hino do Santiago");
        add(ModItems.STRIKE_TOTEM, "Totem do Sindicalizado");

        add(ModItems.SUPER_SNOWBALL, "Bolinha");
        add(ModItems.PUNCH_CARD, "Cartão Perfurado");
        add(ModItems.ENDER_CARD, "Cartão do Fim");
        add(ModItems.FLOPPY_DISK, "Disquete");
        add(ModBlocks.COMPUTER, "Computador");
    }
}
