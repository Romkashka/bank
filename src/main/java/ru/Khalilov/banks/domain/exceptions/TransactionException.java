package ru.Khalilov.banks.domain.exceptions;

/**
 * Thrown to indicate invalid arguments and incorrect operations transaction-related classes and it's methods.
 */
public class TransactionException extends GeneralBankException {
    /**
     * Constructs TransactionException with the specified detail message
     *
     * @param message - the detail message
     */
    protected TransactionException(String message) {
        super(message);
    }

    /**
     * Creates TransactionException indicating attempt to cancel already canceled or corrupted transaction
     * @return TransactionException indicating attempt to cancel already canceled or corrupted transaction
     */
    public static TransactionException invalidTransactionEntityInformation()
    {
        return new TransactionException("Transaction can't be canceled: some account hasn't committed it");
    }
}
