package ru.Khalilov.banks.domain.exceptions;

import java.math.BigDecimal;

/**
 * Thrown to indicate invalid arguments and incorrect operations bank-related classes and it's methods
 *
 * @see ru.Khalilov.banks.domain.entities.Bank
 */
public class BankException extends GeneralBankException{
    /**
     * Constructs BankException with the specified detail message
     *
     * @param message - the detail message
     */
    protected BankException(String message) {
        super(message);
    }

    /**
     * Creates BankException indicating attempt to access object of type 'objectType' by its ID
     * @param objectType - type of desired object
     * @return BankException indicating attempt to access object of type 'objectType' by its ID
     */
    public static BankException accessToNonExistingObjectById(String objectType)
    {
        return new BankException("Bank doesn't have " + objectType + " with given Id");
    }

    /**
     * Creates BankException indicating attempt to access non-existing Client
     * @return BankException indicating attempt to access non-existing Client
     *
     * @see ru.Khalilov.banks.domain.entities.Client
     */
    public static BankException accessToNonExistingClient()
    {
        return new BankException("Bank doesn't contain this client");
    }

    /**
     * Creates BankException indicating attempt to proceed transaction exceeding limits for clients with incomplete information
     * @param sum - desired sum of transaction
     * @param limit - limit for transactions for clients with incomplete information
     * @return BankException indicating attempt to proceed transaction exceeding limits for clients with incomplete information
     */
    public static BankException doubtfulClientExceedsTransactionLimit(BigDecimal sum, BigDecimal limit)
    {
        return new BankException("Transaction sum is " + sum + ", but only " + limit + " is allowed for doubtful clients");
    }
}
