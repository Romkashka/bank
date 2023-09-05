package ru.Khalilov.banks.domain.models;

import lombok.Getter;
import ru.Khalilov.banks.domain.entities.TariffSubscriber;
import ru.Khalilov.banks.domain.exceptions.EmailException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Model of email with incoming messages. Implements interface {@link TariffSubscriber}
 */
public class EmailImpl implements TariffSubscriber {
    private final List<Message> messages;
    @Getter
    private final String address;

    /**
     * Constructs new email with given address and no received messages. Checks address for validity.
     * @param address - given address
     * @throws EmailException in case of invalid address
     * @see Address
     */
    public EmailImpl(String address) throws EmailException {
        if (!EmailImpl.isValid(address)) {
            throw EmailException.InvalidAddress(address);
        }
        this.address = address;
        messages = new ArrayList<>();
    }

    public List<Message> getMessages() {
        return Collections.unmodifiableList(messages);
    }

    /**
     * Saves received message
     * @param message - received message
     */
    public void receiveMessage(Message message)
    {
        messages.add(message);
    }

    /**
     * Creates string containing all received messages
     * @return string containing all received messages
     */
    public String toStringAll() {
        return Stream.of(messages).collect( String::new, (result, item) -> result += item.toString() + "\n", (res1, res2) -> res1 += res2);
    }

    /**
     * Creates string with the latest received message
     * @return string with the latest received message
     */
    public String toStringLast() {
        if (messages.isEmpty()) {
            return new String();
        }
        return messages.get(messages.size() - 1).toString();
    }

    /**
     * Creates string with email address
     * @return string with email address
     */
    @Override
    public String toString() {
        return address;
    }

    /**
     * Checks if given email address is valid
     * @param address - given email address
     * @return true if valid, false otherwise
     */
    public static boolean isValid(String address) {
        return Pattern.compile("^(.+)@(\\S+)$").matcher(address).matches();
    }
}
