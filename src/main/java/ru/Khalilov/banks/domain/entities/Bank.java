package ru.Khalilov.banks.domain.entities;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import ru.Khalilov.banks.domain.exceptions.AccountException;
import ru.Khalilov.banks.domain.exceptions.BankException;
import ru.Khalilov.banks.domain.exceptions.TariffException;
import ru.Khalilov.banks.domain.models.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * Contains tariffs and clients and provides methods to handle them
 */
public class Bank {
    @NonNull
    private final List<Tariff> tariffs;
    @NonNull
    private final List<Client> clients;
    @NonNull
    private final Clock clock;
    @NonNull @Getter
    private final BankId bankID;
    @NonNull @Getter @Setter
    private BigDecimal transactionLimitForDoubtfulClients;

    /**
     * Constructs bank without any clients and tariffs
     * @param clock clock used by this bank
     * @param bankID bank's id
     * @param transactionLimitForDoubtfulClients limit for transactions made by clients without passport or/and address
     */
    public Bank(@NonNull Clock clock, @NonNull BankId bankID, @NonNull BigDecimal transactionLimitForDoubtfulClients) {
        this.clock = clock;
        this.bankID = bankID;
        this.transactionLimitForDoubtfulClients = transactionLimitForDoubtfulClients;
        tariffs = new ArrayList<>();
        clients = new ArrayList<>();
    }

    /**
     * Creates new account for given client with given tariff. Also subscribes clint to this tariff
     * @param client chosen client
     * @param tariffStats chosen tariff
     * @return new account for given client with given tariff
     * @throws BankException if tariff or client wasn't found
     * @throws TariffException if client already subscribed to tariff
     * @throws AccountException
     */
    public Account createAccount(@NonNull Client client, @NonNull TariffStats tariffStats) throws BankException, TariffException, AccountException {
        Tariff tariff = getTariffByID(tariffStats.id());

        if (!clients.contains(client)) {
            throw BankException.accessToNonExistingClient();
        }

        Account account = new Account(new AccountId(UUID.randomUUID(), bankID), clock.getDateTime(), new AccountBalanceHandler(tariff), clock.getDateTime().getDayOfMonth());

        client.addAccount(account);

        if (client.getClientInformation().emailImpl() != null) {
            tariff.subscribe(client.getClientInformation().emailImpl());
        }

        return account;
    }

    /**
     * Adds money to account
     * @param accountId id of target account
     * @param sum sum to add
     * @return {@link TransactionEntity} of this completed transaction
     * @throws BankException if some entity wasn't found
     * @throws AccountException if transaction can't be completed due to account state
     */
    public TransactionEntity addTransactionSum(@NonNull AccountId accountId, @NonNull BigDecimal sum) throws BankException, AccountException {
        if (!accountId.bankId().equals(bankID)) {
            throw BankException.accessToNonExistingObjectById(Bank.class.getTypeName());
        }

        Optional<Client> optionalClient = clients.stream().filter(current -> current.getAccounts().stream().anyMatch(account -> account.getAccountID().equals(accountId))).findFirst();

        if (optionalClient.isEmpty()) {
            throw BankException.accessToNonExistingObjectById(Account.class.getTypeName());
        }
        Client client = optionalClient.get();

        if (isClientDoubtful(client) && sum.abs().compareTo(transactionLimitForDoubtfulClients) >= 0) {
            throw BankException.doubtfulClientExceedsTransactionLimit(sum, transactionLimitForDoubtfulClients);
        }

        Account account = client.getAccounts().stream().filter(current -> current.getAccountID().equals(accountId)).findFirst().get();
        return account.addTransactionSum(sum);
    }

    /**
     * Creates new {@link Client} based on given {@link ClientInformation}
     * @param clientInformation given {@link ClientInformation}
     * @return UUID of this client
     */
    public UUID addClient(ClientInformation clientInformation) {
        UUID clientID = UUID.randomUUID();
        clients.add(new Client(clientID, clientInformation, new ArrayList<>()));
        return clientID;
    }

    /**
     * Changes client information of client with given id. Also changes subscription if email changes
     * @param clientID target client's id
     * @param newClientInformation new data to set
     * @throws BankException if there is no client with given id
     */
    public void changeClientInformation(@NonNull UUID clientID, @NonNull ClientInformation newClientInformation) throws BankException {
        Optional<Client> optionalClient = clients.stream().filter(current -> current.getId().equals(clientID)).findFirst();
        if (optionalClient.isEmpty()) {
            throw BankException.accessToNonExistingClient();
        }

        Client client = optionalClient.get();

        if (!client.getClientInformation().emailImpl().equals(newClientInformation.emailImpl())) {
            for (Account account: client.getAccounts()) {
                Tariff tariff = getTariffByID(account.getAccountBalanceHandler().getTariff().getTariffStats().id());

                if (client.getClientInformation().emailImpl() != null) {
                    tariff.unsubscribe(client.getClientInformation().emailImpl());
                }

                if (newClientInformation.emailImpl() != null) {
                    tariff.subscribe(newClientInformation.emailImpl());
                }
            }
        }

        client.setClientInformation(newClientInformation);
    }

    /**
     * Adds new tariff based in given tariff stats
     * @param tariffStats data to base tariff on
     */
    public void addTariff(@NonNull TariffStats tariffStats) {
        tariffs.add(new Tariff(clock, tariffStats));
    }

    /**
     * Changes {@link TariffStats} of tariff with given id
     * @param tariffID id of target tariff
     * @param newTariffStats new tariff stats
     */
    public void changeTariff(@NonNull UUID tariffID, @NonNull TariffStats newTariffStats) {
        Tariff tariff = getTariffByID(tariffID);

        tariff.setTariffStats(newTariffStats);
    }

    /**
     * Returns {@link TariffBuilder} based on given tariff stats
     * @param baseTariffStats - base tariff stats
     * @return TariffBuilder based on given tariff stats
     */
    public TariffBuilder getTariffChanger(@NonNull TariffStats baseTariffStats) {
        return new TariffBuilder(baseTariffStats);
    }

    /**
     * Returns account with given accountId or null if no such account
     * @param accountId id of target account
     * @return account with given accountId or null if no such account
     */
    public Account findAccount(@NonNull AccountId accountId) {
        return clients.stream().map(c -> c.getAccounts()).flatMap(accounts -> accounts.stream()).filter(account -> account.getAccountID().equals(accountId)).findFirst().orElse(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bank bank = (Bank) o;
        return bankID.equals(bank.bankID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bankID);
    }

    /**
     * Return unmodifiable list of all tariff stats
     * @return unmodifiable list of all tariff stats
     */
    public List<TariffStats> getTariffStats() {
        return Collections.unmodifiableList(tariffs.stream().collect(ArrayList::new, (list, item) -> list.add(item.getTariffStats()), ArrayList::addAll));
    }

    /**
     * Return unmodifiable list of all clients
     * @return unmodifiable list of all clients
     */
    public List<Client> getClients() {
        return Collections.unmodifiableList(clients);
    }

    private boolean isClientDoubtful(Client client) {
        return client.getClientInformation().address() == null || client.getClientInformation().passport() == null;
    }

    private Tariff getTariffByID(@NonNull UUID id) throws BankException {
        Optional<Tariff> tariff = tariffs.stream().filter(current -> current.getTariffStats().id().equals(id)).findFirst();
        if (tariff.isEmpty()) {
            throw BankException.accessToNonExistingObjectById(Tariff.class.getTypeName());
        }

        return tariff.get();
    }
}
