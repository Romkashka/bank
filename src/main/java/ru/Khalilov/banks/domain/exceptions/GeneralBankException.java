package ru.Khalilov.banks.domain.exceptions;

/**
 * Thrown to indicate invalid arguments and incorrect operations of all domain classes. Root exception for all exceptions in Banks.Domain package.
 */
public class GeneralBankException extends RuntimeException{
    /**
     * Constructs GeneralBankException with the specified detail message
     * @param message - the detail message
     */
    protected GeneralBankException(String message) {
        super(message);
    }
}
