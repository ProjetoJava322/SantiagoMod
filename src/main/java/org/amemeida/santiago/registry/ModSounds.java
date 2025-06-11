package org.amemeida.santiago.registry;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import org.amemeida.santiago.Santiago;

public class ModSounds {
    public static final SoundEvent POPIPO = register("popipo");
    public static final SoundEvent TEMMIE = register("temmie");
    public static final SoundEvent SANTIAGOS_ANTHEM = register("santiagos_anthem");

    public static SoundEvent register(String name) {
        var id = Identifier.of(Santiago.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void initialize() {}
}
