package ru.Khalilov.banks.console.factories;

import lombok.NonNull;
import ru.Khalilov.banks.console.handlers.ChangingHandlerBuilder;
import ru.Khalilov.banks.console.handlers.HandlerBase;
import ru.Khalilov.banks.console.readers.ConsoleIOHandler;
import ru.Khalilov.banks.domain.entities.ClientInformationBuilder;
import ru.Khalilov.banks.domain.entities.TariffBuilder;
import ru.Khalilov.banks.domain.exceptions.AddressException;
import ru.Khalilov.banks.domain.exceptions.EmailException;
import ru.Khalilov.banks.domain.exceptions.GeneralBankException;
import ru.Khalilov.banks.domain.exceptions.PassportException;
import ru.Khalilov.banks.domain.models.Address;
import ru.Khalilov.banks.domain.models.EmailImpl;
import ru.Khalilov.banks.domain.models.Passport;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Arrays;

public class AddHandlerFactoryImpl implements HandlerFactory {
    private ChangingHandlerBuilder builder;
    private ConsoleIOHandler ioHandler;

    public AddHandlerFactoryImpl(ConsoleIOHandler ioHandler) {
        this.ioHandler = ioHandler;
        builder = new ChangingHandlerBuilder().withTriggerWord("add");
    }

    public @NonNull AddHandlerFactoryImpl withBank() {
        builder.addConcreteHandler(new ChangingHandlerBuilder().withTriggerWord("bank").withTask((service) -> {
            try {
                ioHandler.println("Set transaction limit for doubtful clients:");
                BigDecimal limit = ioHandler.readBigDecimal();

                ioHandler.println("Do you want ot choose this bank?");
                service.createBank(limit, ioHandler.readBooleanAnswer());
            }
            catch (GeneralBankException e) {
                ioHandler.println(Arrays.toString(e.getStackTrace()));
            }

            return service;
        }).build());
        return this;
    }

    public @NonNull AddHandlerFactoryImpl withTariff() {
        builder.addConcreteHandler(new ChangingHandlerBuilder().withTriggerWord("tariff").withTask((service) -> {
            TariffBuilder tariffBuilder = service.getTariffBuilder();

            ioHandler.println("Enter tariff name");
            String input = ioHandler.readInput();

            while (!tariffBuilder.isNameValid(input))
            {
                ioHandler.println("Tariff name isn't valid! Try again!");
                input = ioHandler.readInput();
            }

            tariffBuilder.withName(input);

            ioHandler.println("Enter tariff type (e.g. 'debit', 'credit' etc.):");
            input = ioHandler.readInput();
            while (!tariffBuilder.isAccountTypeValid(input))
            {
                ioHandler.println("Tariff type isn't valid! Try again!");
                input = ioHandler.readInput();
            }

            tariffBuilder.withAccountType(input);

            ioHandler.println("Enter annual interest rate:");
            BigDecimal decimalInput = ioHandler.readBigDecimal();
            tariffBuilder.withBalanceInterest(decimalInput);

            ioHandler.println("Enter negative balance operation tax:");
            decimalInput = ioHandler.readBigDecimal();
            tariffBuilder.withNegativeOperationTax(decimalInput);

            ioHandler.println("Enter minimal balance:");
            decimalInput = ioHandler.readBigDecimal();
            tariffBuilder.withMinimalBalance(decimalInput);

            ioHandler.println("Enter add-only period:");
            int days = ioHandler.readInt();
            tariffBuilder.withAddOnlyPeriod(Duration.ofDays(days));

            ioHandler.println("Do you want ot choose this tariff?");
            try {
                service.addTariff(tariffBuilder, ioHandler.readBooleanAnswer());
            }
            catch (GeneralBankException e) {
                ioHandler.println(e.getMessage());
            }

            return service;
        }).build());
        return this;
    }

    public @NonNull AddHandlerFactoryImpl withClient() {
        builder.addConcreteHandler(new ChangingHandlerBuilder().withTriggerWord("client").withTask((service) -> {
            ClientInformationBuilder clientBuilder = service.getClientInformationBuilder();

            ioHandler.println("Enter client name:");
            String input = ioHandler.readInput();
            while (!clientBuilder.isNamePartValid(input))
            {
                ioHandler.println("Client name isn't valid! Try again!");
                input = ioHandler.readInput();
            }

            clientBuilder.withName(input);

            ioHandler.println("Enter client surname:");
            input = ioHandler.readInput();
            while (!clientBuilder.isNamePartValid(input))
            {
                ioHandler.println("Client surname isn't valid! Try again!");
                input = ioHandler.readInput();
            }

            clientBuilder.withSurname(input);

            ioHandler.println("Do you want to add address?");
            while (ioHandler.readBooleanAnswer())
            {
                ioHandler.println("Enter street name:");
                input = ioHandler.readInput();
                ioHandler.println("Enter building number:");
                int number = ioHandler.readInt();
                try
                {
                    clientBuilder.withAddress(new Address(input, number));
                    break;
                }
                catch (AddressException e)
                {
                    ioHandler.println(e.getMessage());
                    ioHandler.println("Do you want to retry?");
                }
            }

            ioHandler.println("Do you want to add passport?");
            while (ioHandler.readBooleanAnswer())
            {
                ioHandler.println("Enter passport series:");
                input = ioHandler.readInput();
                ioHandler.println("Enter passport number");
                String number = ioHandler.readInput();
                try
                {
                    clientBuilder.withPassport(new Passport(input, number));
                    break;
                }
                catch (PassportException e)
                {
                    ioHandler.println(e.getMessage());
                }
            }

            ioHandler.println("Do you want to add email?");
            while (ioHandler.readBooleanAnswer())
            {
                ioHandler.println("Enter email address:");
                input = ioHandler.readInput();
                try
                {
                    clientBuilder.withEmail(new EmailImpl(input));
                    break;
                }
                catch (EmailException e)
                {
                    ioHandler.println(e.getMessage());
                }
            }

            ioHandler.println("Do you want ot choose this client?");
            try {
                service.addClient(clientBuilder, ioHandler.readBooleanAnswer());
            }
            catch (GeneralBankException e) {
                ioHandler.println(e.getMessage());
            }

            return service;
        }).build());
        return this;
    }

    public @NonNull AddHandlerFactoryImpl withAccount() {
        builder.addConcreteHandler(new ChangingHandlerBuilder().withTriggerWord("account").withTask((service) -> {
            try {
                service.createAccount();
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
        withBank();
        withAccount();
        withClient();
        withTariff();
        return build();
    }

    @Override
    public @NonNull HandlerBase build() {
        return builder.build();
    }
}
