package org.amemeida.santiago.exceptions;

public class RunningException extends InterruptedException {
    public RunningException(String msg) {
        super(msg);
    }
}
