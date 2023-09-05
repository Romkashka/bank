package ru.Khalilov.banks.domain.exceptions;

import ru.Khalilov.banks.domain.models.EmailImpl;

/**
 * Thrown to indicate invalid arguments and incorrect operations email-related classes and it's methods.
 *
 * @see EmailImpl
 */
public class EmailException extends GeneralBankException {
    /**
     * Constructs EmailException with the specified detail message
     *
     * @param message - the detail message
     */
    protected EmailException(String message) {
        super(message);
    }

    /**
     * Creates EmailMessage indicating attempt to create Email object with invalid email address
     * @param address - found invalid email address
     * @return EmailMessage indicating attempt to create Email object with invalid email address
     */
    public static EmailException InvalidAddress(String address)
    {
        return new EmailException("String '" + address + "' isn't a valid email address");
    }
}
