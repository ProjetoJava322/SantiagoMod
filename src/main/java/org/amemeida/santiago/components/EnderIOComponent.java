package org.amemeida.santiago.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateType;
import net.minecraft.world.World;

import org.amemeida.santiago.Santiago;
import org.amemeida.santiago.registry.items.ModComponents;

import java.util.HashMap;
import java.util.Map;

public record EnderIOComponent(String syncID) implements TextContent {
    public static final Codec<EnderIOComponent> CODEC = RecordCodecBuilder.create(builder -> {
        return builder.group(
                Codec.STRING.fieldOf("sync").forGetter(EnderIOComponent::syncID)
        ).apply(builder, EnderIOComponent::new);
    });

    @Override
    public String text() {
        var saver = Saver.getInstance();
        return saver.getText(syncID);
    }

    @Override
    public void setComponent(String script, ItemStack stack) {
        var saver = Saver.getInstance();
        saver.setText(this.syncID(), script);
        stack.set(ModComponents.ENDER, new EnderIOComponent(this.syncID));
    }

    /**
     * @see PersistentState
     * @see ChunkUpdateState
     */
    public static class Saver extends PersistentState {
        private final Map<String, String> ender;
        private static Saver instance;

        public static final Codec<Saver> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        Codec.dispatchedMap(Codec.STRING, (key) -> Codec.STRING).fieldOf("map")
                                .forGetter(Saver::getEnder)
                ).apply(instance, Saver::new)
        );

        private Saver(Map<String, String> map) {
            if (map instanceof HashMap<String, String>) {
                this.ender = map;
            } else {
                this.ender = new HashMap<>();
                this.ender.putAll(map);
            }
        }

        private Saver() {
            this(new HashMap<>());
        }

        public String getText(String key) {
            return ender.getOrDefault(key, "");
        }

        public void setText(String key, String value) {
            if (value.equals(getText(key))) {
                return;
            }

            if (value.isEmpty()) {
                ender.remove(key);
            } else {
                ender.put(key, value);
            }

            this.markDirty();
        }

        private Map<String, String> getEnder() {
            return ender;
        }

        public static final PersistentStateType<Saver> TYPE =
                new PersistentStateType<Saver>("ender_card_map", Saver::new, CODEC, null);

        public static Saver getInstance() {
            if (instance != null) {
                return instance;
            }

            var world = Santiago.getServer().getWorld(World.OVERWORLD);
            assert world != null;

            instance = world.getPersistentStateManager().getOrCreate(TYPE);

            instance.markDirty();
            return instance;
        }
    }
}
