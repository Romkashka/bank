package ru.Khalilov.banks.console.handlers;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import ru.Khalilov.banks.console.exceptions.GeneralCLIException;
import ru.Khalilov.banks.domain.services.SingleWorkstationService;

import java.util.List;
import java.util.Optional;

public abstract class HandlerBase {
    @Getter @Setter
    private HandlerBase successor;

    @Getter @Setter
    private HandlerBase concreteHandler;

    @NonNull @Getter
    private final String triggerWord;

    public HandlerBase(HandlerBase successor, @NonNull String triggerWord, HandlerBase concreteHandler) {
        this.successor = successor;
        this.triggerWord = triggerWord;
        this.concreteHandler = concreteHandler;
    }

    public abstract Optional<SingleWorkstationService> handle(@NonNull SingleWorkstationService service, @NonNull List<String> arguments);

    public HandlerBase addConcreteHandler(HandlerBase concreteHandler) {
        concreteHandler.setSuccessor(this.concreteHandler);
        this.concreteHandler = concreteHandler;
        return this;
    }

    protected Optional<SingleWorkstationService> sendToNext(@NonNull SingleWorkstationService service, @NonNull List<String> arguments) {
        if (getSuccessor() != null) {
            return getSuccessor().handle(service, arguments);
        }

        throw GeneralCLIException.noHandler(arguments);
    }

    @Override
    public String toString() {
        return "HandlerBase{" +
                ", triggerWord='" + triggerWord + '\'' +
                '}';
    }
}
