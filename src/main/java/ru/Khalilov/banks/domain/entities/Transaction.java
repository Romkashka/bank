package ru.Khalilov.banks.domain.entities;

import lombok.Getter;
import lombok.NonNull;
import ru.Khalilov.banks.domain.exceptions.TransactionException;
import ru.Khalilov.banks.domain.models.TransactionEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Container for transaction entities. Able to cancel all its entities if all of them are valid.
 */
public class Transaction {
    @NonNull
    private final List<TransactionEntity> entities;
    @NonNull @Getter
    private final UUID id;

    /**
     * Creates transaction with given list of transaction entities and id
     * @param entities list of {@link TransactionEntity}
     * @param id {@link UUID} of this transaction
     */
    public Transaction(@NonNull List<TransactionEntity> entities, @NonNull UUID id) {
        this.entities = entities;
        this.id = id;
    }

    /**
     * Successively cancel all entities of this transaction. Validate cancellation before start
     */
    public void cancel() {
        validateCancellation();

        entities.forEach(entity -> entity.account().cancelTransaction(entity.transactionEntityId(), entity.sum()));
    }

    @Override
    public String toString() {
        List<String> entityStrings = new ArrayList<>();
        int maxLength = 0;
        for (TransactionEntity entity: entities) {
            String current = entity.toString();
            maxLength = Math.max(maxLength, current.length());
            entityStrings.add(current);
        }

        StringBuilder builder = new StringBuilder();
        builder.append("+").append(String.join("", Collections.nCopies(maxLength + 2, " "))).append("+\n");
        entityStrings.forEach(str -> builder.append("| ").append(str).append(" |\n"));
        builder.append("+").append(String.join("", Collections.nCopies(maxLength + 2, " "))).append("+\n");

        return builder.toString();
    }

    private void validateCancellation() {
        if (entities.stream().anyMatch(entity -> !entity.account().isContainsTransactionEntity(entity.transactionEntityId()))) {
            throw TransactionException.invalidTransactionEntityInformation();
        }
    }
}
