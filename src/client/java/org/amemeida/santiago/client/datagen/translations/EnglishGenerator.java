package org.amemeida.santiago.client.datagen.translations;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.registry.RegistryWrapper;
import org.amemeida.santiago.registry.blocks.ModBlocks;
import org.amemeida.santiago.registry.items.ModItems;
import org.amemeida.santiago.registry.ModSounds;

import java.util.concurrent.CompletableFuture;

public class EnglishGenerator extends Translator {
    public EnglishGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        // Specifying en_us is optional, as it's the default language code
        super(dataOutput, "en_us", registryLookup);
    }

    @Override
    public void translate() {
        add("itemGroup.santiago", "Santiago Mod");

        add(ModBlocks.INCUBATOR, "Incubator");
        add(ModBlocks.REVOLUTION_TABLE, "Revolution Table");

        add(ModBlocks.SANTIAGUITA_ORE, "Santiaguita Ore");
        add(ModBlocks.DEEPSLATE_SANTIAGUITA_ORE, "Deepslate Santiaguita Ore");


        add(ModItems.SANTIAGUITA_AXE, "Santiaguita Axe");
        add(ModItems.SANTIAGUITA_HOE, "Santiaguita Hoe");
        add(ModItems.SANTIAGUITA_PICKAXE, "Santiaguita Pickaxe");
        add(ModItems.SANTIAGUITA_SWORD,  "Santiaguita Sword");
        add(ModItems.SANTIAGUITA_SHOVEL, "Santiaguita Shovel");

        add(ModItems.SANTIAGUITA_HELMET, "Santiaguita Helmet");
        add(ModItems.SANTIAGUITA_CHESTPLATE, "Santiaguita Chestplate");
        add(ModItems.SANTIAGUITA_LEGGINGS, "Santiaguita Leggings");
        add(ModItems.SANTIAGUITA_BOOTS, "Santiaguita Boots");

        add(ModItems.SANTIAGUITA_INGOT, "Santiaguita Ingot");
        add(ModItems.SANTIAGUITA_NUGGET, "Santiaguita Nugget");
        add(ModItems.RAW_SANTIAGUITA, "Raw Santiaguita");
        add(ModItems.UNDERLINE, "Downwards Guide");
        add(ModItems.SANTIAGOS_ANTHEM_MUSIC_DISC, "Music Disc");
        addDesc(ModItems.SANTIAGOS_ANTHEM_MUSIC_DISC, "Santiago's Anthem");

        add(ModItems.SUPER_SNOWBALL, "Super Snowball");
        add(ModItems.PUNCH_CARD, "Punch Card");
        add(ModItems.ENDER_CARD, "Ender Card");
        add(ModItems.FLOPPY_DISK, "Floppy Disk");
        add(ModBlocks.COMPUTER, "Computer");
        add(ModItems.STRIKE_TOTEM, "Strike Totem");

        add(ModSounds.TEMMIE, "Temmie Talking");
    }
}
