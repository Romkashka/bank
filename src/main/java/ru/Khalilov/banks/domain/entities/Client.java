package ru.Khalilov.banks.domain.entities;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import ru.Khalilov.banks.domain.models.ClientInformation;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Entity representing clients. Consist of ID, basic information given through {@link ClientInformation} and list of accounts client has
 *
 * @see Account
 */
public class Client {
    @NonNull
    private List<Account> accounts;
    @Getter @Setter @NonNull
    private ClientInformation clientInformation;
    @Getter @NonNull
    private UUID id;

    /**
     * Constructs Client with full information about him
     * @param id - client's ID
     * @param clientInformation - basic information about client
     * @param accounts - list of accounts client has
     * @see ClientInformation
     * @see Account
     */
    public Client(UUID id, ClientInformation clientInformation, List<Account> accounts) {
        this.accounts = accounts;
        this.clientInformation = clientInformation;
        this.id = id;
    }

    /**
     * Adds new account to client
     * @param account - account to add
     */
    public void addAccount(Account account) {
        accounts.add(account);
    }

    /**
     * Returns unmodifiable list of accounts
     * @return unmodifiable list of accounts
     */
    public List<Account> getAccounts() {
        return Collections.unmodifiableList(accounts);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return id.equals(client.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
