package org.amemeida.santiago.file;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WorldSavePath;
import org.amemeida.santiago.Santiago;
import org.amemeida.santiago.file.runner.PythonRunner;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class Script {
    public static final Codec<Script> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.fieldOf("file").forGetter(Script::scriptName)
    ).apply(instance, Script::new));

    public static final PacketCodec<ByteBuf, Script> PACKET_CODEC = PacketCodecs.codec(CODEC);

    private final String file;
    private static MinecraftServer server;

    public Script(String path) {
        this.file = scriptName(path);
    }

    public String scriptName() {
        return this.file;
    }

    public static void setServer(MinecraftServer server) {
        Script.server = server;
    }

    public static MinecraftServer getServer() { return server; }

    public static @NotNull String scriptName(@NotNull String title) {
        var trimmed = title.trim();
        return trimmed.endsWith(".py") ? trimmed : trimmed + ".py";
    }

    public String runScript(String in) throws PythonRunner.RunningException {
        var file = file();

        if (file.length() <= 0) {
            throw new PythonRunner.RunningException("Empty script");
        }

        return PythonRunner.getInstance().run(file, in);
    }

    public String runScript() throws PythonRunner.RunningException {
        return runScript("");
    }

    public void writeScript(String script) {
        var file = this.file();

        if (script.length() <= 0 && !file.exists()) {
            return;
        }

        this.create();
        file.setWritable(true);

        try (var fileWriter = new FileWriter(file))  {
            fileWriter.write(script.trim());
        } catch (IOException ex) {
            throw new RuntimeException("Cannot write to script file.");
        }
    }

    public String getScript() {
        var file = this.file();

        if (!file.exists()) {
            return "";
        }

        try (var scanner = new Scanner(file)) {
            return Files.readString(file.toPath());
        } catch (IOException ex) {
            throw new RuntimeException("Cannot read script file.");
        }
    }

    public static Path folder() {
        return server.getSavePath(WorldSavePath.ROOT)
                .resolve(Santiago.MOD_ID)
                .resolve("scripts");
    }

    protected File file() {
        return folder().resolve(this.file).toFile();
    }

    protected void create() {
        try {
            Files.createDirectories(folder());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        var file = file();

        try {
            boolean a = file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
