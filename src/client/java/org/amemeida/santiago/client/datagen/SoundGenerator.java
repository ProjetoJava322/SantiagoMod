package org.amemeida.santiago.client.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.builder.SoundTypeBuilder;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricSoundsProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.sound.SoundEvent;
import org.amemeida.santiago.registry.ModSounds;

import java.util.concurrent.CompletableFuture;

public class SoundGenerator extends FabricSoundsProvider {
    public SoundGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    private SoundExporter exp;

    @Override
    protected final void configure(RegistryWrapper.WrapperLookup wrapperLookup, SoundExporter exp) {
        this.exp = exp;

        register(ModSounds.TEMMIE);
        register(ModSounds.POPIPO);
    }

    protected void register(SoundEvent sound, SoundTypeBuilder builder) {
        var key = sound.id().toTranslationKey("sound");
        exp.add(sound, builder.subtitle(key));
    }

    protected void register(SoundEvent sound) {
        register(sound, SoundTypeBuilder.of(sound)
                .sound(SoundTypeBuilder.EntryBuilder.ofFile(sound.id())));
    }

    @Override
    public String getName() {
        return "Santiago Sound Generator";
    }
}
