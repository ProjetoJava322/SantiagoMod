package org.amemeida.santiago.exceptions;

/**
 * Exceção lançada quando uma receita específica não é encontrada.
 */
public class RecipeNotFoundException extends Exception {

    /**
     * Construtor da exceção que recebe uma mensagem detalhando o motivo da falha.
     *
     * @param msg mensagem de erro descritiva
     */
    public RecipeNotFoundException(String msg) {
        super(msg);
    }
}

