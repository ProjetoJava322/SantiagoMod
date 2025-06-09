package org.amemeida.santiago.client.datagen.translations;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.sound.SoundEvent;
import org.amemeida.santiago.Santiago;

import java.util.concurrent.CompletableFuture;

public abstract class Translator extends FabricLanguageProvider {
    public Translator(FabricDataOutput dataOutput, String langCode, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        // Specifying en_us is optional, as it's the default language code
        super(dataOutput, langCode, registryLookup);
    }

    private TranslationBuilder builder = null;

    @Override
    public final void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder translationBuilder) {
        builder = translationBuilder;
        translate();
    }

    public abstract void translate();

    protected final Translator add(Block block, String translation) {
        String key = block.getTranslationKey();
        builder.add(key, translation);

        if (block.asItem() != null) {
            key = block.asItem().getTranslationKey();
            builder.add(key, translation);
        }

        return this;
    }

    protected final Translator add(Item item, String translation) {
        String key = item.getTranslationKey();
        builder.add(key, translation);

        return this;
    }

    protected final Translator add(String key, String translation) {
        builder.add(key, translation);
        return this;
    }

    protected final Translator add(SoundEvent soundEvent, String translation) {
        var key = soundEvent.id().toTranslationKey("sound");
        builder.add(key, translation);

        return this;
    }
}
