//package org.amemeida.santiago.registry;

//import net.minecraft.block.jukebox.JukeboxSong;
//import net.minecraft.item.Items;
//import net.minecraft.registry.Registries;
//import net.minecraft.registry.Registry;
//import net.minecraft.registry.RegistryKey;
//import net.minecraft.registry.RegistryKeys;
//import net.minecraft.sound.SoundEvent;
//import net.minecraft.util.Identifier;
//import org.amemeida.santiago.Santiago;

//public class ModJukeboxSongs {
//    static {
//       JukeboxSong a = null;
//       var disc = Items.MUSIC_DISC_CAT;
//   }

//   private static JukeboxSong register(SoundEvent music) {
//       var keys = music.id().toTranslationKey().split("\\.");
//      var name = keys[keys.length - 1];

//      var musicKey = RegistryKey.of(RegistryKeys.SOUND_EVENT, music.id());

//      var jukeboxSong = new JukeboxSong(musicKey, "", 105f, 12);

//      var songKey = RegistryKeys.of(RegistryKeys.JUKEBOX_SONG, Identifier.of(Santiago.MOD_ID, name));

//      return Registry.register(Registries.SOUND_EVENT, songKey, jukeboxSong);
//  }
//}
