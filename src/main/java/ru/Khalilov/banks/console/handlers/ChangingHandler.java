package ru.Khalilov.banks.console.handlers;

import lombok.Getter;
import lombok.NonNull;
import ru.Khalilov.banks.domain.services.SingleWorkstationService;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class ChangingHandler extends HandlerBase {
    private final Function<SingleWorkstationService, SingleWorkstationService> task;
    public ChangingHandler(HandlerBase successor, @NonNull String triggerWord, HandlerBase concreteHandler, Function<SingleWorkstationService, SingleWorkstationService> task) {
        super(successor, triggerWord, concreteHandler);
        this.task = task;
    }

    @Override
    public Optional<SingleWorkstationService> handle(@NonNull SingleWorkstationService service, @NonNull List<String> arguments) {
        if (arguments.get(0).equals(getTriggerWord())) {
            if (arguments.size() == 1) {
                return Optional.of(task.apply(service));
            }
            else {
                return getConcreteHandler().handle(service, arguments.subList(1, arguments.size()));
            }
        }

        return sendToNext(service, arguments);
    }
}
