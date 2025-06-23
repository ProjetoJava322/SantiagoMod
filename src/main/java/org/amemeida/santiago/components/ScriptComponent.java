package org.amemeida.santiago.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;

import org.amemeida.santiago.file.Script;
import org.amemeida.santiago.registry.items.ModComponents;

public class ScriptComponent implements TextContent {
    public static final Codec<ScriptComponent> CODEC = RecordCodecBuilder
        .create(builder -> builder.group(
                Script.CODEC.fieldOf("script")
                    .forGetter(ScriptComponent::getScript)
        ).apply(builder, ScriptComponent::new));

    private final Script script;

    public ScriptComponent(Script script) {
        this.script = script;
    }

    public Script getScript() {
        return this.script;
    }
    
    @Override
    public String text() {
        if (script == null) {
            return "";
        }

        return script.getScript();
    }

    @Override
    public void setComponent(String script, ItemStack stack) {
        this.script.writeScript(script);
        stack.set(ModComponents.SCRIPT, new ScriptComponent(this.script));
    }
}
