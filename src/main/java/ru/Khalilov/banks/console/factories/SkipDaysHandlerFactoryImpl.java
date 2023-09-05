package ru.Khalilov.banks.console.factories;

import lombok.NonNull;
import ru.Khalilov.banks.console.handlers.ChangingHandlerBuilder;
import ru.Khalilov.banks.console.handlers.HandlerBase;
import ru.Khalilov.banks.console.readers.ConsoleIOHandler;
import ru.Khalilov.banks.domain.exceptions.GeneralBankException;

import java.time.Duration;

public class SkipDaysHandlerFactoryImpl implements HandlerFactory {
    private ConsoleIOHandler ioHandler;

    public SkipDaysHandlerFactoryImpl(ConsoleIOHandler ioHandler) {
        this.ioHandler = ioHandler;
    }

    @Override
    public @NonNull HandlerBase getHandler() {
        return build();
    }

    @Override
    public @NonNull HandlerBase build() {
        ChangingHandlerBuilder builder = new ChangingHandlerBuilder().withTriggerWord("skip")
                .withTask((service -> {
                    ioHandler.println("How many days you want to skip?");
                    int days = ioHandler.readInt();

                    try {
                        service.getClock().forward(Duration.ofDays(days));
                    }
                    catch (GeneralBankException e) {
                        ioHandler.println(e.getMessage());
                    }
                    return service;
                }));
        return builder.build();
    }
}
