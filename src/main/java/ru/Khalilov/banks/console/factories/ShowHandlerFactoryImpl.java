package ru.Khalilov.banks.console.factories;

import lombok.NonNull;
import ru.Khalilov.banks.console.handlers.HandlerBase;
import ru.Khalilov.banks.console.readers.ConsoleIOHandler;
import ru.Khalilov.banks.console.handlers.NotChangingHandlerBuilder;

import java.util.Arrays;
import java.util.List;

public class ShowHandlerFactoryImpl implements HandlerFactory {
    private NotChangingHandlerBuilder builder;
    private ConsoleIOHandler ioHandler;

    public ShowHandlerFactoryImpl(ConsoleIOHandler ioHandler) {
        this.builder = new NotChangingHandlerBuilder().withTriggerWord("show");
        this.ioHandler = ioHandler;
    }

    public @NonNull ShowHandlerFactoryImpl withBank() {
        builder.addConcreteHandler(new NotChangingHandlerBuilder().withTriggerWord("bank").withTask((service) -> {
            try {
                ioHandler.println("Current bank id" + service.getBank().getBankID());
            }
            catch (Exception e) {
                ioHandler.println(e.getMessage());
            }

            return null;
        }).build());
        return this;
    }

    public @NonNull ShowHandlerFactoryImpl withAccount() {
        builder.addConcreteHandler(new NotChangingHandlerBuilder().withTriggerWord("account").withTask((service) -> {
            try {
                ioHandler.println("Current account id" + service.getAccount().getAccountID());
            }
            catch (Exception e) {
                ioHandler.println(e.getMessage());
            }

            return null;
        }).build());
        return this;
    }

    public @NonNull ShowHandlerFactoryImpl withTariff() {
        builder.addConcreteHandler(new NotChangingHandlerBuilder().withTriggerWord("tariff").withTask((service) -> {
            try {
                ioHandler.println("Current tariff " + service.getTariffStats());
            }
            catch (Exception e) {
                ioHandler.println(e.getMessage());
            }

            return null;
        }).build());
        return this;
    }

    public @NonNull ShowHandlerFactoryImpl withClient() {
        builder.addConcreteHandler(new NotChangingHandlerBuilder().withTriggerWord("client").withTask((service) -> {
            try {
                ioHandler.println("Current client " + service.getClient());
            }
            catch (Exception e) {
                ioHandler.println(e.getMessage());
            }

            return null;
        }).build());
        return this;
    }

    public @NonNull ShowHandlerFactoryImpl withBalance() {
        builder.addConcreteHandler(new NotChangingHandlerBuilder().withTriggerWord("balance").withTask((service) -> {
            try {
                ioHandler.println("Current balance: " + service.getAccount().getBalance());
            }
            catch (Exception e) {
                ioHandler.println(e.getMessage());
            }

            return null;
        }).build());
        return this;
    }

    public @NonNull ShowHandlerFactoryImpl withTransactions() {
        builder.addConcreteHandler(new NotChangingHandlerBuilder().withTriggerWord("transactions").withTask((service) -> {
            try {
                ioHandler.println("Current transactions: " + Arrays.toString(service.getTransactions().toArray()));
            }
            catch (Exception e) {
                ioHandler.println(e.getMessage());
            }

            return null;
        }).build());
        return this;
    }

    public @NonNull ShowHandlerFactoryImpl withBanks() {
        builder.addConcreteHandler(new NotChangingHandlerBuilder().withTriggerWord("banks").withTask((service) -> {
            try {
                ioHandler.println("All banks available:\n" + Arrays.toString(service.getBanks().toArray()));
            }
            catch (Exception e) {
                ioHandler.println(e.getMessage());
            }

            return null;
        }).build());
        return this;
    }

    public @NonNull ShowHandlerFactoryImpl withTariffs() {
        builder.addConcreteHandler(new NotChangingHandlerBuilder().withTriggerWord("tariffs").withTask((service) -> {
            try {
                ioHandler.println("All available tariffs:\n" + Arrays.toString(service.getTariffStatsList().toArray()));
            }
            catch (Exception e) {
                ioHandler.println(e.getMessage());
            }

            return null;
        }).build());
        return this;
    }

    public @NonNull ShowHandlerFactoryImpl withClients() {
        builder.addConcreteHandler(new NotChangingHandlerBuilder().withTriggerWord("clients").withTask((service) -> {
            try {
                ioHandler.println("All available clients:\n" + Arrays.toString(service.getClients().toArray()));
            }
            catch (Exception e) {
                ioHandler.println(e.getMessage());
            }

            return null;
        }).build());
        return this;
    }

    public @NonNull ShowHandlerFactoryImpl withAccounts() {
        builder.addConcreteHandler(new NotChangingHandlerBuilder().withTriggerWord("accounts").withTask((service) -> {
            try {
                ioHandler.println("All available accounts:\n" + Arrays.toString(service.getAccounts().toArray()));
            }
            catch (Exception e) {
                ioHandler.println(e.getMessage());
            }

            return null;
        }).build());
        return this;
    }

    public @NonNull ShowHandlerFactoryImpl withEmail() {
        builder.addConcreteHandler(new NotChangingHandlerBuilder().withTriggerWord("email").withTask((service) -> {
            try {
                ioHandler.println("You want to see all emails or only last one? (all/last)");
                String answer = ioHandler.readOptions(List.of("all", "last"));

                ioHandler.println(answer.equals("all") ? service.getEmail().toStringAll() : service.getEmail().toStringLast());
            }
            catch (Exception e) {
                ioHandler.println(e.getMessage());
            }

            return null;
        }).build());
        return this;
    }

    public @NonNull ShowHandlerFactoryImpl withHelloWorld() {
        builder.addConcreteHandler(new NotChangingHandlerBuilder().withTriggerWord("hello").withTask((service) -> {
            ioHandler.println("Hello, World!");
            return null;
        }).build());
        return this;
    }

    @Override
    public @NonNull HandlerBase getHandler() {
        withHelloWorld();
        withBank();
        withAccount();
        withTariff();
        withBalance();
        withClient();
        withTransactions();
        withBanks();
        withTariffs();
        withClients();
        withAccounts();
        withEmail();
        return build();
    }

    @Override
    public @NonNull HandlerBase build() {
        return builder.build();
    }
}
