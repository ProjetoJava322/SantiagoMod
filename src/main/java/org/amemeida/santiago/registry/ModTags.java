package org.amemeida.santiago.registry;

import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import org.amemeida.santiago.santiaguita.SantiaguitaMaterial;

/**
 * Classe que contém as tags personalizadas usadas no mod.
 */
public class ModTags {

    /**
     * Tag de itens que podem reparar itens feitos de Santiaguita.
     */
    public static final TagKey<Item> REPAIRS_SANTIAGUITA = SantiaguitaMaterial.REPAIRS_SANTIAGUITA;

    /**
     * Método vazio para forçar a inicialização da classe.
     */
    public static void initialize() {}
}

