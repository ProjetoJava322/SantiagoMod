package org.amemeida.santiago.registry.items;

import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.amemeida.santiago.Santiago;
import org.amemeida.santiago.components.IOComponent;
import org.amemeida.santiago.components.ScriptComponent;
import org.amemeida.santiago.components.TextContent;

import java.util.function.UnaryOperator;

/**
 *  @see net.minecraft.component.DataComponentTypes
 */

public class ModComponents {
    public static final ComponentType<ScriptComponent> SCRIPT = register(
            "ender_text", builder ->
                    builder.codec(ScriptComponent.CODEC).cache());

    public static final ComponentType<TextContent> IO = register(
            "local_text", builder ->
                    builder.codec(IOComponent.CODEC).cache());

    private static <T> ComponentType<T> register(String name, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(Santiago.MOD_ID, name),
                builderOperator.apply(ComponentType.builder()).build());
    }

    public static void initialize() {}
}
