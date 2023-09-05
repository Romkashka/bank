package ru.Khalilov.banks.domain.entities;

import ru.Khalilov.banks.domain.exceptions.ClockException;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Clock used to invoke events in subscribers after changes in its own state
 * @see ClockSubscriber
 */
public interface Clock {
    /**
     * Returns current date and time
     * @return current date and time
     */
    LocalDateTime getDateTime();

    /**
     * Return amount of days in current year
     * @return amount of days in current year
     */
    int getDaysInYear();

    /**
     * Saves subscriber inside. It will be invoked when state will change.
     * @param subscriber - object to subscribe. Must implement {@link ClockSubscriber} interface
     * @throws ClockException - if subscriber is already tracking
     */
    void subscribe(ClockSubscriber subscriber) throws ClockException;

    /**
     * Stop tracking subscriber. It won't be invoked anymore when state of clock changes
     * @param subscriber - object to unsubscribe
     * @return true if given object was unsubscribed successfully, false otherwise
     */
    boolean unsubscribe(ClockSubscriber subscriber);
    public void forward(Duration duration);
}
