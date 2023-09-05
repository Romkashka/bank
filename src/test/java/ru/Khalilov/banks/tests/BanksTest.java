package ru.Khalilov.banks.tests;

import org.junit.jupiter.api.Test;
import ru.Khalilov.banks.domain.entities.*;
import ru.Khalilov.banks.domain.exceptions.AccountException;
import ru.Khalilov.banks.domain.exceptions.BankException;
import ru.Khalilov.banks.domain.models.Address;
import ru.Khalilov.banks.domain.models.EmailImpl;
import ru.Khalilov.banks.domain.models.Passport;
import ru.Khalilov.banks.domain.models.TariffStats;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class BanksTest {
    private Clock clock;
    private CentralBank centralBank;
    private Bank bank1;
    private Bank bank2;
    private Client clientA;
    private Client clientB;
    private Account debitAccountA;
    private Account debitAccountB;

    public BanksTest()
    {
        clock = new ManualClockImpl(LocalDateTime.now());
        centralBank = new CentralBank(clock, UUID.randomUUID());
        bank1 = centralBank.createBank(BigDecimal.valueOf(10000));
        bank2 = centralBank.createBank(BigDecimal.valueOf(50000));

        TariffBuilder tariffBuilder1 = new TariffBuilder();
        tariffBuilder1.withName("debit").withAccountType("debit").withBalanceInterest(BigDecimal.valueOf(3.65));
        bank1.addTariff(tariffBuilder1.build());
        bank2.addTariff(tariffBuilder1.build());

        var clientBuilder = new ClientInformationBuilder();
        clientBuilder.withName("Aboba")
                .withSurname("Abobov")
                .withEmail(new EmailImpl("abobus@mail.ru"))
                .withAddress(new Address("aaaaaaa", 10))
                .withPassport(new Passport("1234", "567890"));
        bank1.addClient(clientBuilder.build());
        clientBuilder = new ClientInformationBuilder();
        clientBuilder.withName("Amogus")
                .withSurname("Amogusov");
        bank2.addClient(clientBuilder.build());

        clientA = bank1.getClients().get(0);
        clientB = bank2.getClients().get(0);

        debitAccountA = bank1.createAccount(clientA, bank1.getTariffStats().get(0));
        debitAccountB = bank2.createAccount(clientB, bank2.getTariffStats().get(0));
    }

    @Test
    public void commitValidTransactions_BalanceChanges()
    {
        centralBank.addMoney(debitAccountA.getAccountID(), BigDecimal.valueOf(1000));
        assertEquals(BigDecimal.valueOf(1000), debitAccountA.getBalance());
        centralBank.addMoney(debitAccountA.getAccountID(), BigDecimal.valueOf(100000));
        assertEquals(BigDecimal.valueOf(101000), debitAccountA.getBalance());

        centralBank.addMoney(debitAccountB.getAccountID(), BigDecimal.valueOf(1000));
        assertEquals(BigDecimal.valueOf(1000), debitAccountB.getBalance());

        centralBank.withdrawMoney(debitAccountA.getAccountID(), BigDecimal.valueOf(50000));
        assertEquals(BigDecimal.valueOf(51000), debitAccountA.getBalance());

        centralBank.transferMoney(debitAccountA.getAccountID(), debitAccountB.getAccountID(), BigDecimal.valueOf(10000));
        assertEquals(BigDecimal.valueOf(41000), debitAccountA.getBalance());
        assertEquals(BigDecimal.valueOf(11000), debitAccountB.getBalance());
    }

    @Test
    public void commitInvalidTransactions_ThrowsExceptions()
    {
        assertThrows(BankException.class, () -> centralBank.addMoney(debitAccountB.getAccountID(), BigDecimal.valueOf(100000)));

        assertThrows(AccountException.class, () -> centralBank.withdrawMoney(debitAccountA.getAccountID(), BigDecimal.valueOf(1000)));

        assertThrows(AccountException.class, () ->
            centralBank.transferMoney(debitAccountA.getAccountID(), debitAccountB.getAccountID(), BigDecimal.valueOf(10000)));
    }

    @Test
    public void cancelTransaction_BalanceChanges()
    {
        UUID firstTransaction = centralBank.addMoney(debitAccountA.getAccountID(), BigDecimal.valueOf(100000));
        centralBank.cancelTransaction(firstTransaction);

        assertEquals(BigDecimal.ZERO, debitAccountA.getBalance());

        centralBank.addMoney(debitAccountA.getAccountID(), BigDecimal.valueOf(10000));
        UUID secondTransaction = centralBank.transferMoney(debitAccountA.getAccountID(), debitAccountB.getAccountID(), BigDecimal.valueOf(10000));
        centralBank.cancelTransaction(secondTransaction);

        assertEquals(BigDecimal.valueOf(10000), debitAccountA.getBalance());
        assertEquals(BigDecimal.ZERO, debitAccountB.getBalance());
    }

    @Test
    public void ChangeTariff_SubscriberNotified()
    {
        assertTrue(clientA.getClientInformation().emailImpl().getMessages().isEmpty());

        TariffStats tariff = bank1.getTariffStats().get(0);
        TariffBuilder tariffBuilder = bank1.getTariffChanger(tariff);
        tariffBuilder.withMinimalBalance(BigDecimal.valueOf(-10000));
        bank1.changeTariff(tariff.id(), tariffBuilder.build());

        assertEquals(1, clientA.getClientInformation().emailImpl().getMessages().size());
    }
}
