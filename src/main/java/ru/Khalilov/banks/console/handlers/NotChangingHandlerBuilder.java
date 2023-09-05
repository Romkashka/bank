package ru.Khalilov.banks.console.handlers;

import lombok.Getter;
import lombok.NonNull;
import ru.Khalilov.banks.domain.services.SingleWorkstationService;

import java.util.function.Function;

public class NotChangingHandlerBuilder {
    @Getter
    private String triggerWord;
    @Getter
    private HandlerBase successor;
    @Getter
    private HandlerBase concreteHandler;
    @Getter
    private Function<SingleWorkstationService, Void> task;

    public NotChangingHandlerBuilder() {
        clean();
    }

    public NotChangingHandlerBuilder withTriggerWord(String triggerWord) {
        this.triggerWord = triggerWord;
        return this;
    }

    public NotChangingHandlerBuilder withSuccessor(HandlerBase successor) {
        this.successor = successor;
        return this;
    }

    public NotChangingHandlerBuilder withTask(Function<SingleWorkstationService, Void> task) {
        this.task = task;
        return this;
    }

    public NotChangingHandlerBuilder withConcreteHandler(HandlerBase concreteHandler) {
        this.concreteHandler = concreteHandler;
        return this;
    }

    public NotChangingHandlerBuilder addConcreteHandler(HandlerBase newConcreteHandler) {
        newConcreteHandler.setSuccessor(this.concreteHandler);
        concreteHandler = newConcreteHandler;
        return this;
    }

    public NotChangingHandler build() {
        return new NotChangingHandler(successor, triggerWord, concreteHandler, task);
    }

    public void clean() {
        triggerWord = null;
        successor = null;
        task = null;
    }
}
