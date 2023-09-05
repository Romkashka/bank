package ru.Khalilov.banks.console.handlers;

import lombok.Getter;
import lombok.NonNull;
import ru.Khalilov.banks.domain.services.SingleWorkstationService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class NotChangingHandler extends HandlerBase {
    private final Function<SingleWorkstationService, Void> task;
    public NotChangingHandler(HandlerBase successor, @NonNull String triggerWord, HandlerBase concreteHandler, Function<SingleWorkstationService, Void> task) {
        super(successor, triggerWord, concreteHandler);
        this.task = task;
    }

    @Override
    public Optional<SingleWorkstationService> handle(@NonNull SingleWorkstationService service, @NonNull List<String> arguments) {
        if (arguments.get(0).equals(getTriggerWord())) {
            if (arguments.size() == 1) {
                task.apply(service);
            }
            else {
                getConcreteHandler().handle(service, arguments.subList(1, arguments.size()));
            }

            return Optional.empty();
        }

        return sendToNext(service, arguments);
    }
}
