package ru.Khalilov.banks.console;

import ru.Khalilov.banks.console.factories.*;
import ru.Khalilov.banks.console.handlers.HandlerBase;
import ru.Khalilov.banks.console.readers.ConsoleIOHandler;
import ru.Khalilov.banks.console.readers.SystemIOHandler;
import ru.Khalilov.banks.domain.entities.Clock;
import ru.Khalilov.banks.domain.entities.ManualClockImpl;
import ru.Khalilov.banks.domain.services.SingleWorkstationService;
import ru.Khalilov.banks.domain.services.SingleWorkstationServiceImpl;

import java.time.LocalDateTime;

public class RunMe {
    public static void main(String[] args) {
        ConsoleIOHandler ioHandler = new SystemIOHandler();
        HandlerBase showHandler = new ShowHandlerFactoryImpl(ioHandler).getHandler();

        HandlerBase addHandler = new AddHandlerFactoryImpl(ioHandler).getHandler();
        showHandler.setSuccessor(addHandler);

        HandlerBase changeHandler = new ChangeHandlerFactoryImpl(ioHandler).getHandler();
        addHandler.setSuccessor(changeHandler);

        HandlerBase chooseHandler = new ChooseHandlerFactoryImpl(ioHandler).getHandler();
        changeHandler.setSuccessor(chooseHandler);

        HandlerBase cancelHandler = new CancelHandlerFactoryImpl(ioHandler).getHandler();
        chooseHandler.setSuccessor(cancelHandler);

        HandlerBase predictHandler = new PredictHandlerFactoryImpl(ioHandler).getHandler();
        cancelHandler.setSuccessor(predictHandler);

        HandlerBase skipHandler = new SkipDaysHandlerFactoryImpl(ioHandler).getHandler();
        predictHandler.setSuccessor(skipHandler);

        HandlerBase transactionHandler = new TransactionHandlerFactoryImpl(ioHandler).getHandler();
        skipHandler.setSuccessor(transactionHandler);

        Clock clock = new ManualClockImpl(LocalDateTime.now());
        SingleWorkstationService service = new SingleWorkstationServiceImpl(clock);

        CLI cli = new CLI(service, showHandler, ioHandler);

        try {
            cli.run();
        }
        catch (Exception e) {
            ioHandler.println(e.getMessage());
        }
    }
}
