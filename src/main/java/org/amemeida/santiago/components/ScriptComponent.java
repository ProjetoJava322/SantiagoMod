package org.amemeida.santiago.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;

import org.amemeida.santiago.Santiago;
import org.amemeida.santiago.file.Script;
import org.amemeida.santiago.registry.items.ModComponents;
import org.amemeida.santiago.util.RandomId;

import java.util.Optional;
import java.util.Random;

public class ScriptComponent implements TextContent {
    public static final Codec<ScriptComponent> CODEC = RecordCodecBuilder.create(builder -> {
        return builder.group(
                Script.CODEC.optionalFieldOf("script", null).forGetter(ScriptComponent::getScript)
        ).apply(builder, ScriptComponent::new);
    });

    private final Optional<Script> script;

    public ScriptComponent(Script script) {
        this.script = script != null ? Optional.of(script) : Optional.empty();
    }

    public ScriptComponent() {
        this.script = Optional.empty();
    }

    public Script getScript() {
        return this.script.orElse(null);
    }

    @Override
    public String text() {
        if (script.isEmpty()) {
            return "";
        }

        return script.get().getScript();
    }

    @Override
    public void setComponent(String script, ItemStack stack) {
        var newScript = this.script.orElseGet(() -> {
            return new Script(RandomId.genRandom());
        });

        newScript.writeScript(script);
        stack.set(ModComponents.SCRIPT, new ScriptComponent(newScript));
    }
}
