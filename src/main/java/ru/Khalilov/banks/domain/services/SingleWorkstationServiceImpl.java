package ru.Khalilov.banks.domain.services;

import lombok.NonNull;
import ru.Khalilov.banks.domain.entities.*;
import ru.Khalilov.banks.domain.exceptions.ServiceException;
import ru.Khalilov.banks.domain.models.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SingleWorkstationServiceImpl implements SingleWorkstationService {
    private final CentralBank centralBank;
    private Bank currentBank;
    private TariffStats currentTariffStats;
    private Client currentClient;
    private Account currentAccount;
    private Account secondaryAccount;

    public SingleWorkstationServiceImpl(Clock clock) {
        centralBank = new CentralBank(clock, UUID.randomUUID());
    }

    /**
     * Return empty {@link TariffBuilder}
     *
     * @return empty TariffBuilder
     */
    @Override
    public @NonNull TariffBuilder getTariffBuilder() {
        return new TariffBuilder();
    }

    /**
     * Return empty {@link ClientInformationBuilder}
     *
     * @return empty ClientInformationBuilder
     */
    @Override
    public @NonNull ClientInformationBuilder getClientInformationBuilder() {
        return new ClientInformationBuilder();
    }

    /**
     * Return current bank if it was chosen
     *
     * @return current bank if it was chosen
     */
    @Override
    public @NonNull Bank getBank() {
        validateBank();
        return currentBank;
    }

    /**
     * Return current {@link TariffStats} if it was chosen
     *
     * @return current {@link TariffStats} if it was chosen
     */
    @Override
    public @NonNull TariffStats getTariffStats() {
        validateTariffStats();
        return currentTariffStats;
    }

    /**
     * Return current {@link Client} if it was chosen
     *
     * @return current {@link Client} if it was chosen
     */
    @Override
    public @NonNull Client getClient() {
        validateClient();
        return currentClient;
    }

    /**
     * Return current {@link EmailImpl} if it was chosen
     *
     * @return current {@link EmailImpl} if it was chosen
     */
    @Override
    public @NonNull EmailImpl getEmail() {
        validateClient();
        return currentClient.getClientInformation().emailImpl();
    }

    /**
     * Return current (from) {@link Account} if it was chosen
     *
     * @return current (from) {@link Account} if it was chosen
     */
    @Override
    public @NonNull Account getAccount() {
        validateAccount(currentAccount);
        return currentAccount;
    }

    /**
     * Return second (to) {@link Account} if it was chosen
     *
     * @return current (to) {@link Account} if it was chosen
     */
    @Override
    public @NonNull Account getSecondaryAccount() {
        validateAccount(secondaryAccount);
        return secondaryAccount;
    }

    /**
     * Return balance of current account if it was chosen
     *
     * @return balance of current account if it was chosen
     */
    @Override
    public @NonNull BigDecimal getBalance() {
        validateAccount(currentAccount);
        return currentAccount.getBalance();
    }

    @Override
    public @NonNull Clock getClock() {
        return centralBank.getClock();
    }

    /**
     * Return list of all {@link Transaction}
     *
     * @return list of all {@link Transaction}
     */
    @Override
    public @NonNull List<Transaction> getTransactions() {
        return centralBank.getTransactions();
    }

    /**
     * Return list of all {@link Bank}
     *
     * @return list of all {@link Bank}
     */
    @Override
    public @NonNull List<Bank> getBanks() {
        return centralBank.getBanks();
    }

    /**
     * Return list of all {@link TariffStats}
     *
     * @return list of all {@link TariffStats}
     */
    @Override
    public @NonNull List<TariffStats> getTariffStatsList() {
        validateBank();
        return currentBank.getTariffStats();
    }

    /**
     * Return list of all {@link Client}
     *
     * @return list of all {@link Client}
     */
    @Override
    public @NonNull List<Client> getClients() {
        validateBank();
        return currentBank.getClients();
    }

    /**
     * Return list of all {@link Account}
     *
     * @return list of all {@link Account}
     */
    @Override
    public @NonNull List<Account> getAccounts() {
        validateClient();
        return currentClient.getAccounts();
    }

    /**
     * Creates empty bank
     *
     * @param transactionLimitForDoubtfulClients limit for transactions for clients without passport or/and address
     * @param enter                              should it be chosen
     */
    @Override
    public void createBank(@NonNull BigDecimal transactionLimitForDoubtfulClients, boolean enter) {
        Bank bank = centralBank.createBank(transactionLimitForDoubtfulClients);

        if (enter) {
            enterBank(bank);
        }
    }

    /**
     * Choose bank by its id. If bank differs from current properties of lower tier set null
     *
     * @param id {@link BankId} of target bank
     */
    @Override
    public void enterBank(@NonNull BankId id) {
        Optional<Bank> found = centralBank.getBanks().stream().filter(b -> b.getBankID().equals(id)).findFirst();
        if (found.isEmpty())
        {
            throw ServiceException.noBankFound(id);
        }

        if (found.get().equals(currentBank)) return;

        enterBank(found.get());
    }

    /**
     * Cancel transaction with given id
     *
     * @param id - id of transaction to cancel
     */
    @Override
    public void cancelTransaction(@NonNull UUID id) {
        centralBank.cancelTransaction(id);
    }

    /**
     * Returns {@link TariffBuilder} based on current tariff
     *
     * @return {@link TariffBuilder} based on current tariff
     */
    @Override
    public @NonNull TariffBuilder getTariffChanger() {
        validateTariffStats();
        return currentBank.getTariffChanger(currentTariffStats);
    }

    /**
     * Add new client preset in given client builder
     *
     * @param builder {@link ClientInformationBuilder} with data
     * @param enter   should new client be chosen as current
     */
    @Override
    public void addClient(@NonNull ClientInformationBuilder builder, boolean enter) {
        validateBank();

        ClientInformation client = builder.build();
        UUID id = currentBank.addClient(client);

        if (enter)
        {
            enterClient(id);
        }
    }

    /**
     * Change current client information with new data set in {@link ClientInformationBuilder}
     *
     * @param builder {@link ClientInformationBuilder} with new data
     */
    @Override
    public void changeClient(@NonNull ClientInformationBuilder builder) {
        validateClient();

        currentBank.changeClientInformation(currentClient.getId(), builder.build());
    }

    /**
     * Creates new tariff with data in builder
     *
     * @param builder {@link TariffBuilder} with data
     * @param enter   should enw tariff bw chosen as current
     */
    @Override
    public void addTariff(@NonNull TariffBuilder builder, boolean enter) {
        validateBank();
        TariffStats built = builder.build();
        currentBank.addTariff(built);
        if (enter)
        {
            currentTariffStats = built;
        }
    }

    /**
     * Change current tariff's {@link TariffStats} on data set in builder
     *
     * @param builder {@link TariffBuilder} with new data
     */
    @Override
    public void changeTariff(@NonNull TariffBuilder builder) {
        validateTariffStats();
        currentBank.changeTariff(currentTariffStats.id(), builder.build());
    }

    /**
     * Set {@link Tariff} as current
     *
     * @param id target tariff id
     */
    @Override
    public void chooseTariff(@NonNull UUID id) {
        validateBank();

        Optional<TariffStats> found = currentBank.getTariffStats().stream().filter(t -> t.id().equals(id)).findFirst();

        if (found.isEmpty()) {
            throw ServiceException.noTariffFound(id);
        }

        currentTariffStats = found.get();
    }

    /**
     * Set {@link Client} as current
     *
     * @param id target client id
     */
    @Override
    public void enterClient(@NonNull UUID id) {
        validateBank();

        Optional<Client> found = currentBank.getClients().stream().filter(c -> c.getId().equals(id)).findFirst();
        if (found.isEmpty())
        {
            throw ServiceException.noClientFound(id);
        }

        if (found.get().equals(currentClient)) return;

        enterClient(found.get());
    }

    /**
     * Return string with last email messages
     *
     * @return string with last email messages
     */
    @Override
    public @NonNull String showLastEmail() {
        validateEmail();
        return currentClient.getClientInformation().emailImpl().toStringLast();
    }

    /**
     * Return string with all email messages
     *
     * @return string with all email messages
     */
    @Override
    public @NonNull String showAllEmails() {
        validateEmail();
        return currentClient.getClientInformation().emailImpl().toStringAll();
    }

    /**
     * Creates new account for current client based on current tariff in current bank
     */
    @Override
    public void createAccount() {
        validateClient();
        validateTariffStats();
        currentBank.createAccount(currentClient, currentTariffStats);
    }

    /**
     * Set account as current (from-account)
     *
     * @param id target account id
     */
    @Override
    public void chooseAccount(@NonNull AccountId id) {
        currentAccount = getAccount(id);
    }

    /**
     * Set account as secondary (to-account)
     *
     * @param id target account id
     */
    @Override
    public void chooseSecondaryAccount(@NonNull AccountId id) {
        secondaryAccount = getAccount(id);
    }

    /**
     * Add money to current account
     *
     * @param sum sum of transaction
     * @return id of this transaction
     */
    @Override
    public @NonNull UUID putMoney(@NonNull BigDecimal sum) {
        validateAccount(currentAccount);
        return centralBank.addMoney(currentAccount.getAccountID(), sum);
    }

    /**
     * Withdraw money from current account
     *
     * @param sum sum of transaction
     * @return id of this transaction
     */
    @Override
    public @NonNull UUID withdrawMoney(@NonNull BigDecimal sum) {
        validateAccount(currentAccount);
        return centralBank.withdrawMoney(currentAccount.getAccountID(), sum);
    }

    /**
     * Transfers money from current account to secondary
     *
     * @param sum sum of transaction
     * @return if of this transaction
     */
    @Override
    public @NonNull UUID transferMoney(@NonNull BigDecimal sum) {
        validateAccount(currentAccount);
        validateAccount(secondaryAccount);
        return centralBank.transferMoney(currentAccount.getAccountID(), secondaryAccount.getAccountID(), sum);
    }

    /**
     * Predicts balance of current account after given time period
     *
     * @param duration period of time to simulate
     * @return balance of account after this time
     */
    @Override
    public @NonNull BigDecimal predictBalance(@NonNull Duration duration) {
        validateAccount(currentAccount);

        return currentAccount.predict(duration);
    }

    private void validateBank()
    {
        if (currentBank == null)
        {
            throw ServiceException.noBankChosen();
        }
    }

    private void validateTariffStats()
    {
        validateBank();
        if (currentTariffStats == null)
        {
            throw ServiceException.noTariffChosen();
        }
    }

    private void validateClient()
    {
        validateBank();

        if (currentClient == null)
        {
            throw ServiceException.noClientChosen();
        }
    }

    private void validateEmail()
    {
        validateClient();

        if (currentClient.getClientInformation().emailImpl() == null)
        {
            throw ServiceException.clientWithNoEmail();
        }
    }

    private void validateAccount(Account account)
    {
        if (account == null)
        {
            throw ServiceException.noAccountChosen();
        }
    }

    private Account getAccount(@NonNull AccountId id)
    {
        validateClient();

        Optional<Account> found = currentClient.getAccounts().stream().filter(account -> account.getAccountID().equals(id)).findFirst();

        if (found.isEmpty()) {
            throw ServiceException.noAccountFound(id);
        }

        return found.get();
    }

    private void enterBank(@NonNull Bank bank)
    {
        currentBank = bank;
        currentTariffStats = null;
        currentClient = null;
        currentAccount = null;
    }

    private void enterClient(@NonNull Client found)
    {
        currentClient = found;
        currentAccount = null;
    }
}
