package org.amemeida.santiago.file;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.WorldSavePath;
import org.amemeida.santiago.Santiago;
import org.amemeida.santiago.exceptions.RunningException;
import org.amemeida.santiago.file.runner.PythonRunner;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

/**
 * Representa um script Python armazenado em arquivo dentro da pasta de scripts do mod.
 * Fornece funcionalidades para executar, ler, escrever e criar o arquivo do script.
 */
public class Script {
    /**
     * Codec para serialização/deserialização do script usando o campo "file" (nome do arquivo).
     */
    public static final Codec<Script> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("file").forGetter(Script::scriptName)
    ).apply(instance, Script::new));

    /**
     * Codec para serialização via rede, baseado no CODEC.
     */
    public static final PacketCodec<ByteBuf, Script> PACKET_CODEC = PacketCodecs.codec(CODEC);

    private final String file;

    /**
     * Construtor do Script a partir do caminho/nome do arquivo.
     * @param path caminho ou nome do arquivo do script
     */
    public Script(String path) {
        this.file = scriptName(path);
    }

    /**
     * Retorna o nome do arquivo do script.
     * @return nome do arquivo do script
     */
    public String scriptName() {
        return this.file;
    }

    /**
     * Garante que o nome do script termine com ".py".
     * @param title nome ou caminho do script
     * @return nome com extensão ".py"
     */
    public static @NotNull String scriptName(@NotNull String title) {
        var trimmed = title.trim();
        return trimmed.endsWith(".py") ? trimmed : trimmed + ".py";
    }

    /**
     * Executa o script Python passando a string de entrada para o script.
     * @param in entrada fornecida ao script
     * @return saída produzida pelo script Python
     * @throws RunningException se o script estiver vazio ou ocorrer erro na execução
     */
    public String runScript(String in) throws RunningException {
        var file = file();

        if (file.length() <= 0) {
            throw new RunningException("Empty script");
        }

        return PythonRunner.getInstance().run(file, in);
    }

    /**
     * Executa o script Python sem entrada.
     * @return saída produzida pelo script Python
     * @throws RunningException se ocorrer erro na execução
     */
    public String runScript() throws RunningException {
        return runScript("");
    }

    /**
     * Escreve o conteúdo do script no arquivo, criando a pasta e o arquivo se necessário.
     * @param script conteúdo do script a ser escrito
     */
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

    /**
     * Lê o conteúdo do script do arquivo.
     * @return conteúdo do script como String, ou string vazia se o arquivo não existir
     */
    public String getScript() {
        var file = this.file();

        if (!file.exists()) {
            return "";
        }

        try {
            return Files.readString(file.toPath());
        } catch (IOException ex) {
            throw new RuntimeException("Cannot read script file.");
        }
    }

    /**
     * Retorna o caminho da pasta onde os scripts são armazenados dentro do diretório de salvamento do servidor.
     * @return Path da pasta dos scripts
     */
    public static Path folder() {
        return Santiago.getServer().getSavePath(WorldSavePath.ROOT)
                .resolve(Santiago.MOD_ID)
                .resolve("scripts");
    }

    /**
     * Retorna o arquivo do script (na pasta dos scripts).
     * @return arquivo do script
     */
    protected File file() {
        return folder().resolve(this.file).toFile();
    }

    /**
     * Cria a pasta dos scripts e o arquivo do script se ainda não existirem.
     */
    protected void create() {
        try {
            Files.createDirectories(folder());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        var file = file();

        try {
            // SIDE EFFECT: cria o arquivo se não existir
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
