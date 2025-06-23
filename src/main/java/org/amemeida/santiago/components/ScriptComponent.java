package org.amemeida.santiago.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import org.amemeida.santiago.file.Script;
import org.amemeida.santiago.registry.items.ModComponents;

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
            int leftLimit = 97; // letter 'a'
            int rightLimit = 122; // letter 'z'
            int targetStringLength = 10;
            Random random = new Random();
            StringBuilder buffer = new StringBuilder(targetStringLength);
            for (int i = 0; i < targetStringLength; i++) {
                int randomLimitedInt = leftLimit + (int)
                        (random.nextFloat() * (rightLimit - leftLimit + 1));
                buffer.append((char) randomLimitedInt);
            }
            String generatedString = buffer.toString();

            System.out.println(generatedString);

            return new Script(generatedString);
        });

        newScript.writeScript(script);
        stack.set(ModComponents.SCRIPT, new ScriptComponent(newScript));
    }
}
