package ru.Khalilov.banks.domain.entities;

/**
 * Defines methods obligatory for clock subscribers. It is invoked by {@link Clock}
 */
public interface ClockSubscriber {
    /**
     * Updates state after change in {@link Clock}
     */
    void updateThroughTime();
}
