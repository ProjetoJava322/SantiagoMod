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

/**
 * Um componente que representa dados customizados (texto) vinculados a um item,
 * identificados por um ID de sincronização (syncID).
 *
 * COMENTARIOS FEITOS POR IA
 */
public record EnderIOComponent(String syncID) implements TextContent {

    /**
     * Codec responsável por serializar e desserializar o EnderIOComponent.
     */
    public static final Codec<EnderIOComponent> CODEC = RecordCodecBuilder.create(
            builder -> builder.group(
                    Codec.STRING.fieldOf("sync").forGetter(EnderIOComponent::syncID)
            ).apply(builder, EnderIOComponent::new));

    /**
     * Retorna o texto armazenado associado ao syncID, usando o Saver.
     *
     * @return texto salvo correspondente ao syncID
     */
    @Override
    public String text() {
        var saver = Saver.getInstance();
        return saver.getText(syncID);
    }

    /**
     * Define o texto do componente, atualizando tanto o Saver quanto o ItemStack.
     *
     * @param script texto a ser salvo
     * @param stack  o ItemStack onde o componente será atualizado
     */
    @Override
    public void setComponent(String script, ItemStack stack) {
        var saver = Saver.getInstance();
        saver.setText(this.syncID(), script);
        stack.set(ModComponents.ENDER, new EnderIOComponent(this.syncID));
    }

    /**
     * Gerenciador de estado persistente usado para armazenar textos associados a IDs.
     * Os dados são salvos e carregados automaticamente pelo sistema do Minecraft.
     *
     * @see PersistentState
     */
    public static class Saver extends PersistentState {

        /**
         * Mapa que armazena os textos associados a um ID (chave).
         */
        private final Map<String, String> ender;

        /**
         * Instância singleton do Saver.
         */
        private static Saver instance;

        /**
         * Codec que permite serializar o Saver (especialmente o mapa de textos).
         */
        public static final Codec<Saver> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        Codec.dispatchedMap(Codec.STRING, (key) -> Codec.STRING).fieldOf("map")
                                .forGetter(Saver::getEnder)
                ).apply(instance, Saver::new)
        );

        /**
         * Construtor que recebe um mapa inicial.
         *
         * @param map mapa de textos a ser armazenado
         */
        private Saver(Map<String, String> map) {
            if (map instanceof HashMap<String, String>) {
                this.ender = map;
            } else {
                this.ender = new HashMap<>();
                this.ender.putAll(map);
            }
        }

        /**
         * Construtor padrão que cria um mapa vazio.
         */
        private Saver() {
            this(new HashMap<>());
        }

        /**
         * Recupera o texto associado a uma chave, ou retorna vazio se não existir.
         *
         * @param key chave de identificação
         * @return texto salvo ou string vazia
         */
        public String getText(String key) {
            return ender.getOrDefault(key, "");
        }

        /**
         * Atualiza o texto associado à chave.
         * Se for igual ao atual, não faz nada. Se estiver vazio, remove.
         *
         * @param key   chave de identificação
         * @param value novo texto
         */
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

        /**
         * Retorna o mapa de textos interno.
         *
         * @return mapa ender
         */
        private Map<String, String> getEnder() {
            return ender;
        }

        /**
         * Tipo do estado persistente usado pelo Minecraft para salvar e carregar o Saver.
         */
        public static final PersistentStateType<Saver> TYPE =
                new PersistentStateType<>("ender_card_map", Saver::new, CODEC, null);

        /**
         * Obtém a instância única do Saver, associada ao mundo Overworld.
         * Se ainda não existir, cria e registra no gerenciador de estado persistente.
         *
         * @return instância única do Saver
         */
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
