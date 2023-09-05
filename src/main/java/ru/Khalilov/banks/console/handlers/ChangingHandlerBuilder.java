package ru.Khalilov.banks.console.handlers;

import lombok.Getter;
import ru.Khalilov.banks.domain.services.SingleWorkstationService;

import java.util.function.Function;

public class ChangingHandlerBuilder {
    @Getter
    private String triggerWord;
    @Getter
    private HandlerBase successor;
    @Getter
    private HandlerBase concreteHandler;
    @Getter
    private Function<SingleWorkstationService, SingleWorkstationService> task;

    public ChangingHandlerBuilder() {
        clean();
    }

    public ChangingHandlerBuilder withTriggerWord(String triggerWord) {
        this.triggerWord = triggerWord;
        return this;
    }

    public ChangingHandlerBuilder withSuccessor(HandlerBase successor) {
        this.successor = successor;
        return this;
    }

    public ChangingHandlerBuilder withTask(Function<SingleWorkstationService, SingleWorkstationService> task) {
        this.task = task;
        return this;
    }

    public ChangingHandlerBuilder addConcreteHandler(HandlerBase newConcreteHandler) {
        newConcreteHandler.setSuccessor(this.concreteHandler);
        concreteHandler = newConcreteHandler;
        return this;
    }

    public ChangingHandler build() {
        return new ChangingHandler(successor, triggerWord, concreteHandler, task);
    }

    public void clean() {
        triggerWord = null;
        successor = null;
        concreteHandler = null;
        task = null;
    }
}
