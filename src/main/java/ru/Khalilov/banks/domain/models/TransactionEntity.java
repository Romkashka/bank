package ru.Khalilov.banks.domain.models;

import ru.Khalilov.banks.domain.entities.Account;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Represents simple unique change of sum in 1 account.
 * @param account - {@link Account} which balance was changed
 * @param sum - sum by which balance was changed. Negative value indicates a decrease in balance, positive - an increase.
 * @param transactionEntityId - {@link UUID} of this entity
 */
public record TransactionEntity(Account account, BigDecimal sum, UUID transactionEntityId) {
    @Override
    public String toString() {
        return "TransactionEntity{" +
                "account: " + account.getAccountID() +
                ", sum: " + sum +
                '}';
    }
}
