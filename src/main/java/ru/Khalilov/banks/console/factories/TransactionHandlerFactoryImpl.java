package ru.Khalilov.banks.console.factories;

import lombok.NonNull;
import ru.Khalilov.banks.console.handlers.ChangingHandlerBuilder;
import ru.Khalilov.banks.console.handlers.HandlerBase;
import ru.Khalilov.banks.console.readers.ConsoleIOHandler;
import ru.Khalilov.banks.domain.exceptions.GeneralBankException;

import java.math.BigDecimal;

public class TransactionHandlerFactoryImpl implements HandlerFactory {
    private ChangingHandlerBuilder builder;
    private ConsoleIOHandler ioHandler;

    public TransactionHandlerFactoryImpl(ConsoleIOHandler ioHandler) {
        this.ioHandler = ioHandler;
        this.builder = new ChangingHandlerBuilder().withTriggerWord("transaction");
    }

    public @NonNull TransactionHandlerFactoryImpl withPut() {
        builder.addConcreteHandler(new ChangingHandlerBuilder().withTriggerWord("put").withTask((service) -> {
            ioHandler.println("How much du you want to put?");
            BigDecimal sum = ioHandler.readBigDecimal();

            try {
                service.putMoney(sum);
            }
            catch (GeneralBankException e) {
                ioHandler.println(e.getMessage());
            }
            return service;
        }).build());
        return this;
    }

    public @NonNull TransactionHandlerFactoryImpl withWithdraw() {
        builder.addConcreteHandler(new ChangingHandlerBuilder().withTriggerWord("withdraw").withTask((service) -> {
            ioHandler.println("How much du you want to withdraw?");
            BigDecimal sum = ioHandler.readBigDecimal();

            try {
                service.withdrawMoney(sum);
            }
            catch (GeneralBankException e) {
                ioHandler.println(e.getMessage());
            }
            return service;
        }).build());
        return this;
    }

    public @NonNull TransactionHandlerFactoryImpl withTransfer() {
        builder.addConcreteHandler(new ChangingHandlerBuilder().withTriggerWord("transfer").withTask((service) -> {
            ioHandler.println("How much du you want to transfer?");
            BigDecimal sum = ioHandler.readBigDecimal();

            try {
                service.transferMoney(sum);
            }
            catch (GeneralBankException e) {
                ioHandler.println(e.getMessage());
            }
            return service;
        }).build());
        return this;
    }

    @Override
    public @NonNull HandlerBase getHandler() {
        withPut();
        withWithdraw();
        withTransfer();
        return build();
    }

    @Override
    public @NonNull HandlerBase build() {
        return builder.build();
    }
}
