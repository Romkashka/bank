package ru.Khalilov.banks.domain.models;

import java.util.UUID;

/**
 * Container for account id. Since account depends on bank, it made of bankId and UUID of this account.
 *
 * @param accountId UUID of account
 * @param bankId    ID of bank that contains this account
 * @see UUID
 */
public record AccountId(UUID accountId, ru.Khalilov.banks.domain.models.BankId bankId) {
}
