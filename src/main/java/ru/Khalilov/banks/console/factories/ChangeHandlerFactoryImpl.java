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

public class ChangeHandlerFactoryImpl implements HandlerFactory {
    private ChangingHandlerBuilder builder;
    private ConsoleIOHandler ioHandler;

    public ChangeHandlerFactoryImpl(ConsoleIOHandler ioHandler) {
        this.ioHandler = ioHandler;
        builder = new ChangingHandlerBuilder().withTriggerWord("change");
    }

    public @NonNull ChangeHandlerFactoryImpl withTariff() {
        builder.addConcreteHandler(new ChangingHandlerBuilder().withTriggerWord("tariff").withTask((service) -> {
            TariffBuilder tariffBuilder = new TariffBuilder(service.getTariffStats());

            if (ioHandler.readBooleanAnswer("Do you want to change tariff name?")) {
                ioHandler.println("Enter tariff name");
                String input = ioHandler.readInput();

                while (!tariffBuilder.isNameValid(input))
                {
                    ioHandler.println("Tariff name isn't valid! Try again!");
                    input = ioHandler.readInput();
                }

                tariffBuilder.withName(input);
            }

            if (ioHandler.readBooleanAnswer("Do you want to change tariff type?")) {
                ioHandler.println("Enter tariff type (e.g. 'debit', 'credit' etc.):");
                String input = ioHandler.readInput();
                while (!tariffBuilder.isAccountTypeValid(input))
                {
                    ioHandler.println("Tariff type isn't valid! Try again!");
                    input = ioHandler.readInput();
                }

                tariffBuilder.withAccountType(input);
            }

            if (ioHandler.readBooleanAnswer("Do you want to change annual interest rate?")) {
                ioHandler.println("Enter annual interest rate:");
                BigDecimal decimalInput = ioHandler.readBigDecimal();
                tariffBuilder.withBalanceInterest(decimalInput);
            }

            if (ioHandler.readBooleanAnswer("Do you want to change negative balance opertarion tax?")) {
                ioHandler.println("Enter negative balance operation tax:");
                BigDecimal decimalInput = ioHandler.readBigDecimal();
                tariffBuilder.withNegativeOperationTax(decimalInput);
            }

            if (ioHandler.readBooleanAnswer("Do you want to change minimal balance?")) {
                ioHandler.println("Enter minimal balance:");
                BigDecimal decimalInput = ioHandler.readBigDecimal();
                tariffBuilder.withMinimalBalance(decimalInput);
            }

            if (ioHandler.readBooleanAnswer("Do you want to change add-only period?")) {
                ioHandler.println("Enter add-only period:");
                int days = ioHandler.readInt();
                tariffBuilder.withAddOnlyPeriod(Duration.ofDays(days));
            }

            try {
                service.changeTariff(tariffBuilder);
            }
            catch (GeneralBankException e) {
                ioHandler.println(e.getMessage());
            }

            return service;
        }).build());
        return this;
    }

    public @NonNull ChangeHandlerFactoryImpl withClient() {
        builder.addConcreteHandler(new ChangingHandlerBuilder().withTriggerWord("client").withTask((service) -> {
            ClientInformationBuilder clientBuilder = new ClientInformationBuilder(service.getClient().getClientInformation());

            if (ioHandler.readBooleanAnswer("Do you want to change name?")) {
                ioHandler.println("Enter client name:");
                String input = ioHandler.readInput();
                while (!clientBuilder.isNamePartValid(input))
                {
                    ioHandler.println("Client name isn't valid! Try again!");
                    input = ioHandler.readInput();
                }

                clientBuilder.withName(input);
            }


            if (ioHandler.readBooleanAnswer("Do you want to change surname?")) {
                ioHandler.println("Enter client surname:");
                String input = ioHandler.readInput();
                while (!clientBuilder.isNamePartValid(input))
                {
                    ioHandler.println("Client surname isn't valid! Try again!");
                    input = ioHandler.readInput();
                }

                clientBuilder.withSurname(input);
            }

            ioHandler.println("Do you want to change address?");
            while (ioHandler.readBooleanAnswer())
            {
                ioHandler.println("Enter street name:");
                String input = ioHandler.readInput();
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

            ioHandler.println("Do you want to change passport?");
            while (ioHandler.readBooleanAnswer())
            {
                ioHandler.println("Enter passport series:");
                String input = ioHandler.readInput();
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
                    ioHandler.println("Do you want to retry?");
                }
            }

            ioHandler.println("Do you want to change email?");
            while (ioHandler.readBooleanAnswer())
            {
                ioHandler.println("Enter email address:");
                String input = ioHandler.readInput();
                try
                {
                    clientBuilder.withEmail(new EmailImpl(input));
                    break;
                }
                catch (EmailException e)
                {
                    ioHandler.println(e.getMessage());
                    ioHandler.println("Do you want to retry?");
                }
            }

            try {
                service.changeClient(clientBuilder);
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
        withTariff();
        withClient();
        return build();
    }

    @Override
    public @NonNull HandlerBase build() {
        return builder.build();
    }
}
