package org.amemeida.santiago.file.runner;

import java.io.*;
import java.util.stream.Collectors;

public class PythonRunner {
    private static PythonRunner runner = null;

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
                throw new RunningException(streamToString(proc.getErrorStream()));
            }

            return streamToString(proc.getInputStream());
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
