package ru.Khalilov.banks.console.factories;

import lombok.NonNull;
import ru.Khalilov.banks.console.handlers.HandlerBase;
import ru.Khalilov.banks.console.handlers.NotChangingHandlerBuilder;
import ru.Khalilov.banks.console.readers.ConsoleIOHandler;
import ru.Khalilov.banks.domain.exceptions.GeneralBankException;

import java.time.Duration;

public class PredictHandlerFactoryImpl implements HandlerFactory {
    private ConsoleIOHandler ioHandler;

    public PredictHandlerFactoryImpl(ConsoleIOHandler ioHandler) {
        this.ioHandler = ioHandler;
    }

    @Override
    public @NonNull HandlerBase getHandler() {
        return build();
    }

    @Override
    public @NonNull HandlerBase build() {
        NotChangingHandlerBuilder builder = new NotChangingHandlerBuilder().withTriggerWord("predict")
                .withTask((service -> {
                    ioHandler.println("How many days ahead you want to look?");
                    int days = ioHandler.readInt();

                    try {
                        service.predictBalance(Duration.ofDays(days));
                    }
                    catch (GeneralBankException e) {
                        ioHandler.println(e.getMessage());
                    }
                    return null;
                }));

        return builder.build();
    }
}
