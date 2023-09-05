package ru.Khalilov.banks.domain.exceptions;

/**
 * Thrown to indicate invalid arguments and incorrect operations clock-related classes and it's methods
 *
 * @see ru.Khalilov.banks.domain.entities.Clock
 */
public class ClockException extends GeneralBankException{
    /**
     * Constructs ClockException with the specified detail message
     *
     * @param message - the detail message
     */
    protected ClockException(String message) {
        super(message);
    }

    /**
     * Creates ClockException indicating attempt to subscribe already subscribed object
     * @return ClockException indicating attempt to subscribe already subscribed object
     */
    public static ClockException AlreadySubscribed()
    {
        return new ClockException("Twice subscription isn't allowed");
    }
}
