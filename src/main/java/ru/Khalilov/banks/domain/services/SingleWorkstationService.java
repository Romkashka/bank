package ru.Khalilov.banks.domain.services;

import lombok.NonNull;
import ru.Khalilov.banks.domain.entities.*;
import ru.Khalilov.banks.domain.models.AccountId;
import ru.Khalilov.banks.domain.models.BankId;
import ru.Khalilov.banks.domain.models.EmailImpl;
import ru.Khalilov.banks.domain.models.TariffStats;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

public interface SingleWorkstationService {
    /**
     * Return empty {@link TariffBuilder}
     * @return empty TariffBuilder
     */
    @NonNull TariffBuilder getTariffBuilder();

    /**
     * Return empty {@link ClientInformationBuilder}
     * @return empty ClientInformationBuilder
     */
    @NonNull ClientInformationBuilder getClientInformationBuilder();

    /**
     * Return current bank if it was chosen
     * @return current bank if it was chosen
     */
    @NonNull Bank getBank();
    /**
     * Return current {@link TariffStats} if it was chosen
     * @return current {@link TariffStats} if it was chosen
     */
    @NonNull TariffStats getTariffStats();
    /**
     * Return current {@link Client} if it was chosen
     * @return current {@link Client} if it was chosen
     */
    @NonNull Client getClient();
    /**
     * Return current {@link EmailImpl} if it was chosen
     * @return current {@link EmailImpl} if it was chosen
     */
    @NonNull EmailImpl getEmail();
    /**
     * Return current (from) {@link Account} if it was chosen
     * @return current (from) {@link Account} if it was chosen
     */
    @NonNull Account getAccount();
    /**
     * Return second (to) {@link Account} if it was chosen
     * @return current (to) {@link Account} if it was chosen
     */
    @NonNull Account getSecondaryAccount();
    /**
     * Return balance of current account if it was chosen
     * @return balance of current account if it was chosen
     */
    @NonNull BigDecimal getBalance();

    @NonNull Clock getClock();
    /**
     * Return list of all {@link Transaction}
     * @return list of all {@link Transaction}
     */
    @NonNull List<Transaction> getTransactions();
    /**
     * Return list of all {@link Bank}
     * @return list of all {@link Bank}
     */
    @NonNull List<Bank> getBanks();
    /**
     * Return list of all {@link TariffStats}
     * @return list of all {@link TariffStats}
     */
    @NonNull List<TariffStats> getTariffStatsList();
    /**
     * Return list of all {@link Client}
     * @return list of all {@link Client}
     */
    @NonNull List<Client> getClients();
    /**
     * Return list of all {@link Account}
     * @return list of all {@link Account}
     */
    @NonNull List<Account> getAccounts();

    /**
     * Creates empty bank
     * @param transactionLimitForDoubtfulClients limit for transactions for clients without passport or/and address
     * @param enter should it be chosen
     */
    void createBank(@NonNull BigDecimal transactionLimitForDoubtfulClients, boolean enter);

    /**
     * Choose bank by its id
     * @param id {@link BankId} of target bank
     */
    void enterBank(@NonNull BankId id);

    /**
     * Cancel transaction with given id
     * @param id - id of transaction to cancel
     */
    void cancelTransaction(@NonNull UUID id);

    /**
     * Returns {@link TariffBuilder} based on current tariff
     * @return {@link TariffBuilder} based on current tariff
     */
    @NonNull TariffBuilder getTariffChanger();

    /**
     * Add new client preset in given client builder
     * @param builder {@link ClientInformationBuilder} with data
     * @param enter should new client be chosen as current
     */
    void addClient(@NonNull ClientInformationBuilder builder, boolean enter);

    /**
     * Change current client information with new data set in {@link ClientInformationBuilder}
     * @param builder {@link ClientInformationBuilder} with new data
     */
    void changeClient(@NonNull ClientInformationBuilder builder);

    /**
     * Creates new tariff with data in builder
     * @param builder {@link TariffBuilder} with data
     * @param enter should enw tariff bw chosen as current
     */
    void addTariff(@NonNull TariffBuilder builder, boolean enter);

    /**
     * Change current tariff's {@link TariffStats} on data set in builder
     * @param builder {@link TariffBuilder} with new data
     */
    void changeTariff(@NonNull TariffBuilder builder);

    /**
     * Set {@link Tariff} as current
     * @param id target tariff id
     */
    void chooseTariff(@NonNull UUID id);

    /**
     * Set {@link Client} as current
     * @param id target client id
     */
    void enterClient(@NonNull UUID id);

    /**
     * Return string with last email messages
     * @return string with last email messages
     */
    @NonNull String showLastEmail();

    /**
     * Return string with all email messages
     * @return string with all email messages
     */
    @NonNull String showAllEmails();

    /**
     * Creates new account for current client based on current tariff in current bank
     */
    void createAccount();

    /**
     * Set account as current (from-account)
     * @param id target account id
     */
    void chooseAccount(@NonNull AccountId id);

    /**
     * Set account as secondary (to-account)
     * @param id target account id
     */
    void chooseSecondaryAccount(@NonNull AccountId id);

    /**
     * Add money to current account
     * @param sum sum of transaction
     * @return id of this transaction
     */
    @NonNull UUID putMoney(@NonNull BigDecimal sum);

    /**
     * Withdraw money from current account
     * @param sum sum of transaction
     * @return id of this transaction
     */
    @NonNull UUID withdrawMoney(@NonNull BigDecimal sum);

    /**
     * Transfers money from current account to secondary
     * @param sum sum of transaction
     * @return if of this transaction
     */
    @NonNull UUID transferMoney(@NonNull BigDecimal sum);

    /**
     * Predicts balance of current account after given time period
     * @param duration period of time to simulate
     * @return balance of account after this time
     */
    @NonNull BigDecimal predictBalance(@NonNull Duration duration);
}
