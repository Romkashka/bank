package ru.Khalilov.banks.domain.exceptions;

/**
 * Thrown to indicate invalid arguments and incorrect operations in manual clock-related classes and methods
 * @see ru.Khalilov.banks.domain.entities.ManualClockImpl
 */
public class ManualClockException extends ClockException {
    /**
     * Constructs ManualClockException with the specified detail message
     *
     * @param message - the detail message
     */
    protected ManualClockException(String message) {
        super(message);
    }

    /**
     * Creates ManualClockException indicating attempt to forward time by not positive value
     * @return ManualClockException indicating attempt to forward time by not positive value
     */
    public static ManualClockException NotPositiveDurationToForward() {
        return new ManualClockException("Duration must be positive");
    }
}
