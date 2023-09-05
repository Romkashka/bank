package ru.Khalilov.banks.domain.exceptions;

import ru.Khalilov.banks.domain.models.Passport;

/**
 * Thrown to indicate invalid arguments and incorrect operations address-related classes and it's methods.
 *
 * @see Passport
 */
public class PassportException extends ClientException {
    /**
     * Constructs PassportException with the specified detail message
     *
     * @param message - the detail message
     */
    protected PassportException(String message) {
        super(message);
    }

    /**
     * Creates PassportException indicating attempt to construct Passport object with invalid passport series
     * @param found - invalid passport series
     * @return PassportException indicating attempt to construct Passport object with invalid passport series
     */
    public static PassportException InvalidPassportSeries(String found)
    {
        return new PassportException("'" + found + "' isn't a valid passport series");
    }

    /**
     * Creates PassportException indicating attempt to construct Passport object with invalid passport number
     * @param found - invalid passport number
     * @return PassportException indicating attempt to construct Passport object with invalid passport number
     */
    public static PassportException InvalidPassportNumber(String found)
    {
        return new PassportException("'" + found + "' isn't a valid passport number");
    }
}
