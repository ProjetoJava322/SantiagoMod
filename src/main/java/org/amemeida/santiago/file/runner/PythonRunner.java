package org.amemeida.santiago.file.runner;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class PythonRunner {
    private static PythonRunner runner = null;

    private PythonRunner() {}

    public String run(File file, String in) throws RunningException {
        var builder = new ProcessBuilder("python", file.getAbsolutePath());

        try {
            var proc = builder.start();

            try (var out = proc.outputWriter()) {
                out.write(in);
            }

            int exit = proc.waitFor();

            if (exit != 0) {
                throw new RunningException(new BufferedReader(new InputStreamReader(proc.getErrorStream()))
                        .lines().collect(Collectors.joining()));
            }

            return new BufferedReader(new InputStreamReader(proc.getInputStream()))
                    .lines().collect(Collectors.joining());
        } catch (Exception e) {
            throw new RunningException(e.getMessage());
        }
    }

    public static PythonRunner getInstance() {
        if (runner == null) {
            runner = new PythonRunner();
        }

        return runner;
    }

    public static class RunningException extends InterruptedException {
        public RunningException(String msg) {
            super(msg);
        }
    }
}
