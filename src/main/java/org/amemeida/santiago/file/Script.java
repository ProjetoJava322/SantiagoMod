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

    public static @NotNull String scriptName(@NotNull String title) {
        var trimmed = title.trim();
        return trimmed.endsWith(".py") ? trimmed : trimmed + ".py";
    }

    public String runScript() throws PythonRunner.RunningException {
        var file = file();
        return PythonRunner.getInstance().run(file);
    }

    public void writeScript(String script) {
        var file = this.file();

        try {
            Files.createDirectories(folder());
            boolean created = file.createNewFile();
            System.out.println(created);
            file.setWritable(true);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        try (var fileWriter = new FileWriter(file))  {
            fileWriter.flush();
            fileWriter.write(script);
        } catch (IOException ex) {
            throw new RuntimeException("Cannot write to script file.");
        }
    }

    public String getScript() {
        var file = this.file();

        try (var scanner = new Scanner(file)) {
            return Files.readString(file.toPath());
        } catch (IOException ex) {
            throw  new RuntimeException("Cannot read script file.");
        }
    }

    public static Path folder() {
        return server.getSavePath(WorldSavePath.ROOT)
                .resolve(Santiago.MOD_ID)
                .resolve("scripts");
    }

    public File file() {
        return folder().resolve(file).toFile();
    }
}
