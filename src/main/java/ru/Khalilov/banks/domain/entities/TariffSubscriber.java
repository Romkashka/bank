package ru.Khalilov.banks.domain.entities;

import ru.Khalilov.banks.domain.models.Message;

import java.util.List;

public interface TariffSubscriber {
    public List<Message> getMessages();
    public void receiveMessage(Message message);
    public String toStringAll();
    public String toStringLast();
}
