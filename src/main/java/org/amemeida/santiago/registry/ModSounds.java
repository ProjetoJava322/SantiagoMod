package org.amemeida.santiago.registry;

import net.minecraft.block.jukebox.JukeboxSong;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import org.amemeida.santiago.Santiago;

/**
 * Classe responsável pelo registro dos sons do mod.
 */
public class ModSounds {

    /**
     * Evento de som "popipo".
     */
    public static final SoundEvent POPIPO = register("popipo");

    /**
     * Evento de som "temmie".
     */
    public static final SoundEvent TEMMIE = register("temmie");

    /**
     * Evento de som do hino "santiagos_anthem".
     */
    public static final SoundEvent SANTIAGOS_ANTHEM = register("santiagos_anthem");

    /**
     * Chave de registro para a música "santiagos_anthem" no jukebox.
     */
    public static final RegistryKey<JukeboxSong> SANTIAGOS_ANTHEM_KEY = RegistryKey.of(RegistryKeys.JUKEBOX_SONG, Identifier.of(Santiago.MOD_ID, "santiagos_anthem"));

    /**
     * Registra um evento de som com o nome fornecido.
     *
     * @param name Nome do som a ser registrado
     * @return O SoundEvent registrado
     */
    public static SoundEvent register(String name) {
        var id = Identifier.of(Santiago.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    /**
     * Método vazio para forçar a inicialização estática da classe.
     */
    public static void initialize() {}
}

