package org.amemeida.santiago.exceptions;

/**
 * Exceção lançada para indicar que a execução de um processo foi interrompida ou encontrou um erro.
 * Esta exceção estende {@link InterruptedException}.
 */
public class RunningException extends InterruptedException {

    /**
     * Construtor da exceção que recebe uma mensagem detalhando o motivo da interrupção.
     *
     * @param msg mensagem de erro descritiva
     */
    public RunningException(String msg) {
        super(msg);
    }
}
