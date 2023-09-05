package ru.Khalilov.banks.console.factories;

import lombok.NonNull;
import ru.Khalilov.banks.console.handlers.ChangingHandlerBuilder;
import ru.Khalilov.banks.console.handlers.HandlerBase;
import ru.Khalilov.banks.console.readers.ConsoleIOHandler;

import java.util.List;
import java.util.UUID;

public class CancelHandlerFactoryImpl implements HandlerFactory {
    private ChangingHandlerBuilder builder;
    private ConsoleIOHandler ioHandler;

    public CancelHandlerFactoryImpl(ConsoleIOHandler ioHandler) {
        this.ioHandler = ioHandler;
        this.builder = new ChangingHandlerBuilder().withTriggerWord("cancel");
    }

    public @NonNull CancelHandlerFactoryImpl withTransaction() {
        builder.addConcreteHandler(new ChangingHandlerBuilder().withTriggerWord("transaction").withTask((service) -> {
            ioHandler.println("Do you want to choose or enter id?");
            String answer = ioHandler.readOptions(List.of("enter", "choose"));

            UUID id;
            if (answer.equals("enter")) {
                id = UUID.fromString(ioHandler.readInput());
            }
            else {
                id = ioHandler.chooseFromList(service.getTransactions()).getId();
            }

            try {
                service.cancelTransaction(id);
            }
            catch (Exception e) {
                ioHandler.println(e.getMessage());
            }

            return service;
        }).build());
        return this;
    }

    @Override
    public @NonNull HandlerBase getHandler() {
        withTransaction();
        return build();
    }

    @Override
    public @NonNull HandlerBase build() {
        return builder.build();
    }
}
