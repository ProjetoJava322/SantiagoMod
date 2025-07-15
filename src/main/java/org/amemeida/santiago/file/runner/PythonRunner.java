package org.amemeida.santiago.file.runner;

import net.minecraft.registry.RegistryKey;
import net.minecraft.world.GameRules;
import org.amemeida.santiago.Santiago;
import org.amemeida.santiago.exceptions.RunningException;
import org.amemeida.santiago.registry.ModGamerules;

import java.io.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.amemeida.santiago.registry.ModGamerules.COMPUTER_TIMEOUT;

/**
 * Classe utilitária para executar scripts Python a partir de arquivos.
 * Implementa um singleton para gerenciar a execução dos scripts.
 */
public class PythonRunner {
    private static PythonRunner runner = null;

    /**
     * Converte um InputStream em uma String, lendo todas as linhas.
     *
     * @param stream o InputStream a ser convertido
     * @return o conteúdo completo do stream como String
     */
    private static String streamToString(InputStream stream) {
        var buff = new BufferedReader(new InputStreamReader(stream));
        var text = buff.lines().collect(Collectors.joining("\n"));

        try {
            buff.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return text;
    }

    /**
     * Construtor privado para implementar padrão Singleton.
     */
    private PythonRunner() {}

    /**
     * Executa um script Python a partir de um arquivo, passando uma string de entrada para o script.
     *
     * @param file o arquivo Python a ser executado
     * @param in a entrada que será fornecida ao script Python via stdin
     * @return a saída produzida pelo script Python
     * @throws RunningException se o script retornar um código de erro ou se houver problema na execução
     */
    public String run(File file, String in) throws RunningException {
        var builder = new ProcessBuilder("python", file.getAbsolutePath());

        try {
            var proc = builder.start();

            try (var out = proc.outputWriter()) {
                out.write(in);
            }

            long timeout = getTimeout();
            boolean has_finished = proc.waitFor(timeout, TimeUnit.MILLISECONDS);
            if (!has_finished) {
                System.out.println("timeout");
                throw new RunningException("TIMEOUT ERROR");
            }
            int exit = proc.exitValue();

            if (exit != 0) {
                throw new RunningException(streamToString(proc.getErrorStream()));
            }

            return streamToString(proc.getInputStream());
        } catch (Exception e) {
            throw new RunningException(e.getMessage());
        }
    }

    /**
     * Retorna a instância única (singleton) do PythonRunner.
     *
     * @return a instância do PythonRunner
     */
    public static PythonRunner getInstance() {
        if (runner == null) {
            runner = new PythonRunner();
        }

        return runner;
    }

    private int getTimeout(){
        int timeout = Santiago.getServer()
                .getOverworld() //não tem problema pq a gamerule é a mesma em todas as dimensões.
                .getGameRules()
                .get(COMPUTER_TIMEOUT)
                .get();
        return timeout;
    }
}
