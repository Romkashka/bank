package ru.Khalilov.banks.console.factories;

import lombok.NonNull;
import ru.Khalilov.banks.console.handlers.ChangingHandlerBuilder;
import ru.Khalilov.banks.console.handlers.HandlerBase;
import ru.Khalilov.banks.console.readers.ConsoleIOHandler;
import ru.Khalilov.banks.domain.exceptions.GeneralBankException;
import ru.Khalilov.banks.domain.models.AccountId;
import ru.Khalilov.banks.domain.models.BankId;

import java.util.List;
import java.util.UUID;

public class ChooseHandlerFactoryImpl implements HandlerFactory {
    private ChangingHandlerBuilder builder;
    private ConsoleIOHandler ioHandler;

    public ChooseHandlerFactoryImpl(ConsoleIOHandler ioHandler) {
        this.ioHandler = ioHandler;
        this.builder = new ChangingHandlerBuilder().withTriggerWord("choose");
    }

    public @NonNull ChooseHandlerFactoryImpl withBank() {
        builder.addConcreteHandler(new ChangingHandlerBuilder().withTriggerWord("bank").withTask((service) -> {
            ioHandler.println("Do you want to choose or enter id?");
            String answer = ioHandler.readOptions(List.of("choose", "enter"));

            BankId id;
            if (answer.equals("enter")) {
                id = new BankId(UUID.fromString(ioHandler.readInput()));
            }
            else {
                id = ioHandler.chooseFromList(service.getBanks()).getBankID();
            }

            ioHandler.println(id.toString());

            try {
                service.enterBank(id);
            }
            catch (GeneralBankException e) {
                ioHandler.println(e.getMessage());
            }

            return service;
        }).build());
        return this;
    }

    public @NonNull ChooseHandlerFactoryImpl withTariff() {
        builder.addConcreteHandler(new ChangingHandlerBuilder().withTriggerWord("tariff").withTask((service) -> {
            ioHandler.println("Do you want to choose or enter id?");
            String answer = ioHandler.readOptions(List.of("choose", "enter"));

            UUID id;
            if (answer.equals("choose")) {
                id = ioHandler.chooseFromList(service.getTariffStatsList()).id();
            }
            else {
                id = UUID.fromString(ioHandler.readInput());
            }

            try {
                service.chooseTariff(id);
            }
            catch (Exception e) {
                ioHandler.println(e.getMessage());
            }

            return service;
        }).build());
        return this;
    }

    public @NonNull ChooseHandlerFactoryImpl withClient() {
        builder.addConcreteHandler(new ChangingHandlerBuilder().withTriggerWord("client").withTask((service) -> {
            ioHandler.println("Do you want to choose or enter id?");
            String answer = ioHandler.readOptions(List.of("choose", "enter"));

            UUID id;
            if (answer.equals("choose")) {
                id = ioHandler.chooseFromList(service.getClients()).getId();
            }
            else {
                id = UUID.fromString(ioHandler.readInput());
            }

            try {
                service.enterClient(id);
            }
            catch (Exception e) {
                ioHandler.println(e.getMessage());
            }

            return service;
        }).build());
        return this;
    }

    public @NonNull ChooseHandlerFactoryImpl withAccount() {
        builder.addConcreteHandler(new ChangingHandlerBuilder().withTriggerWord("account").withTask((service) -> {
            ioHandler.println("Do you want to choose main account or secondary?");
            String answer = ioHandler.readOptions(List.of("main", "secondary"));

            AccountId id = ioHandler.chooseFromList(service.getAccounts()).getAccountID();

            try {
                if (answer.equals("main")) {
                    service.chooseAccount(id);
                }
                else {
                    service.chooseSecondaryAccount(id);
                }
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
        withBank();
        withAccount();
        withTariff();
        withClient();
        return build();
    }

    @Override
    public @NonNull HandlerBase build() {
        return builder.build();
    }
}
