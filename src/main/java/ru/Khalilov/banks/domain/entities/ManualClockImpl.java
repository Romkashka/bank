package ru.Khalilov.banks.domain.entities;

import ru.Khalilov.banks.domain.exceptions.ClockException;
import ru.Khalilov.banks.domain.exceptions.ManualClockException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Clocks that can be forwarded manually for positive duration of time. Implements {@link Clock}
 */
public class ManualClockImpl implements Clock{
    private final List<ClockSubscriber> subscribers;
    private LocalDateTime dateTime;

    /**
     * Constructs ManualClockImpl with given time
     * @param dateTime - start time
     */
    public ManualClockImpl(LocalDateTime dateTime) {
        this.dateTime = dateTime;
        subscribers = new ArrayList<>();
    }

    /**
     * Moves time forward by given value
     * @param duration - value by which time must be forwarded
     */
    @Override
    public void forward(Duration duration) throws ManualClockException {
        if (duration.isNegative() || duration.isZero()) {
            throw ManualClockException.NotPositiveDurationToForward();
        }
        dateTime = dateTime.plus(duration);
        _notify();
    }

    @Override
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    @Override
    public int getDaysInYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, dateTime.getYear());

        return calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
    }

    @Override
    public void subscribe(ClockSubscriber subscriber) throws ClockException {
        if (subscribers.contains(subscriber)) {
            throw ClockException.AlreadySubscribed();
        }

        subscribers.add(subscriber);
    }

    @Override
    public boolean unsubscribe(ClockSubscriber subscriber) {
        return subscribers.remove(subscriber);
    }

    private void _notify() {
        subscribers.forEach(ClockSubscriber::updateThroughTime);
    }
}
