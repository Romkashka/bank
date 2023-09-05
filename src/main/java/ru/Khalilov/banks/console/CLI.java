package ru.Khalilov.banks.console;

import lombok.Getter;
import lombok.NonNull;
import ru.Khalilov.banks.console.exceptions.GeneralCLIException;
import ru.Khalilov.banks.console.handlers.HandlerBase;
import ru.Khalilov.banks.console.readers.ConsoleIOHandler;
import ru.Khalilov.banks.domain.services.SingleWorkstationService;

import java.io.InputStreamReader;
import java.util.*;

public class CLI {
    @NonNull
    private SingleWorkstationService service;
    @NonNull
    private final HandlerBase startingHandler;
    @NonNull
    private final ConsoleIOHandler ioHandler;

    public CLI(@NonNull SingleWorkstationService service, @NonNull HandlerBase startingHandler, @NonNull ConsoleIOHandler ioHandler) {
        this.service = service;
        this.startingHandler = startingHandler;
        this.ioHandler = ioHandler;
    }

    public void run() {
        while (true) {
            List<String> args;
            while (true) {
                String input = ioHandler.readInput();
                if (input.equals("exit")) {
                    return;
                }
                args = List.of(input.split(" "));

                try {
                    Optional<SingleWorkstationService> tmp = startingHandler.handle(service, args);
                    if (tmp.isPresent() && !tmp.get().equals(service)) {
                        service = tmp.get();
                    }
                }
                catch (GeneralCLIException e) {
                    ioHandler.println(e.getMessage());
                }
            }
        }
    }
}
