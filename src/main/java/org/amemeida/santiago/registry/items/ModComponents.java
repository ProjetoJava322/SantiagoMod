package org.amemeida.santiago.registry.items;

import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.amemeida.santiago.Santiago;
import org.amemeida.santiago.components.LocalText;
import org.amemeida.santiago.components.EnderText;

import java.util.function.UnaryOperator;

/**
 *  @see net.minecraft.component.DataComponentTypes
 */

public class ModComponents {
    public static final ComponentType<EnderText> ENDER_TEXT = register(
            "ender_text", builder ->
                    builder.codec(EnderText.CODEC).cache());

    public static final ComponentType<LocalText> LOCAL_TEXT = register(
            "local_text", builder ->
                    builder.codec(LocalText.CODEC).cache());

    private static <T> ComponentType<T> register(String name, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(Santiago.MOD_ID, name),
                builderOperator.apply(ComponentType.builder()).build());
    }

    public static void initialize() {}
}
