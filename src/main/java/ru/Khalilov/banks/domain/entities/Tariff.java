package ru.Khalilov.banks.domain.entities;

import lombok.Getter;
import lombok.NonNull;
import ru.Khalilov.banks.domain.exceptions.TariffException;
import ru.Khalilov.banks.domain.models.Message;
import ru.Khalilov.banks.domain.models.TariffStats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Tariff contains basic information about itself via {@link TariffStats}, and sends notifications when its stats changes
 */
public class Tariff {
    @NonNull
    private List<TariffSubscriber> subscribers;
    @NonNull @Getter
    private Clock clock;
    @NonNull @Getter
    private TariffStats tariffStats;

    /**
     * Constructs tariff with given clock and tariff stats
     * @param clock - {@link Clock} clock
     * @param tariffStats - {@link TariffStats}
     */
    public Tariff(@NonNull Clock clock, @NonNull TariffStats tariffStats) {
        this.clock = clock;
        this.tariffStats = tariffStats;
        subscribers = new ArrayList<>();
    }

    /**
     * Adds given subscriber to subscriber list
     * @param subscriber - subscriber to add
     * @throws TariffException - if subscriber already subscribed
     */
    public void subscribe(TariffSubscriber subscriber) throws TariffException {
        if (subscribers.contains(subscriber)) {
            throw TariffException.alreadySubscribed();
        }

        subscribers.add(subscriber);
    }

    /**
     * Removes given subscriber from tariff's subscriber list
     * @param subscriber - subscriber to remove
     * @return true if subscriber was removed, false otherwise
     */
    public boolean unsubscribe(TariffSubscriber subscriber) {
        return subscribers.remove(subscriber);
    }

    /**
     * Return unmodifiable list of all subscribers
     * @return unmodifiable list of all subscribers
     */
    public List<TariffSubscriber> getSubscribers() {
        return Collections.unmodifiableList(subscribers);
    }

    /**
     * Set new tariff stats to this tariff. Also send messages to all subscribers about changes
     * @param tariffStats - new tariff stats
     * @see TariffStats
     * @see Message
     */
    public void setTariffStats(TariffStats tariffStats) {
        this.tariffStats = tariffStats;
        notifySubscribers();
    }

    private void notifySubscribers() {
        subscribers.forEach(subscriber -> subscriber.receiveMessage(generateMessage()));
    }

    private Message generateMessage() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Dear customer!\n\n")
                .append("You receive this message because You subscribed to Notifications.\n")
                .append("Tariff " + tariffStats.name() + " was changed. Actual terms are listed here:\n")
                .append(tariffStats.toStringStatsOnly());
        return new Message(stringBuilder.toString(), clock.getDateTime());
    }
}
