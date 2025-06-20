package org.amemeida.santiago.file.runner;

import java.io.File;

public class LinuxPythonRunner  {
    public String run(File file) {
        var proc = new ProcessBuilder("python", file.getAbsolutePath());

        return "";
    }
}
