package ru.Khalilov.banks.domain.models;

import java.time.LocalDateTime;

/**
 * Simple model of message. Consists of message text and date it was sent
 * @param messageText - text contained in the message
 * @param dateTime - date and time when it was sent
 * @see EmailImpl
 */
public record Message(String messageText, LocalDateTime dateTime) {
    @Override
    public String toString() {
        return dateTime.toString() + "\nMessage text:\n\n" + messageText;
    }
}
