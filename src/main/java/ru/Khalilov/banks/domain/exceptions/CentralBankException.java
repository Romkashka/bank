package ru.Khalilov.banks.domain.exceptions;

/**
 * Thrown to indicate invalid arguments and incorrect operations central bank related classes and it's methods
 *
 * @see ru.Khalilov.banks.domain.entities.CentralBank
 */
public class CentralBankException extends GeneralBankException{
    /**
     * Constructs CentralBankException with the specified detail message
     *
     * @param message - the detail message
     */
    protected CentralBankException(String message) {
        super(message);
    }

    /**
     * Creates CentralBankException indicating attempt to get non-existing or already canceled transaction
     * @return CentralBankException indicating attempt to get non-existing or already canceled transaction
     */
    public static CentralBankException noSuchTransaction()
    {
        return new CentralBankException("This transaction already was canceled or doesn't belong to this central bank");
    }

    /**
     * Creates CentralBankException indicating attempt to get non-existing account
     * @return CentralBankException indicating attempt to get non-existing account
     */
    public static CentralBankException noSuchAccount()
    {
        return new CentralBankException("This account doesn't belong to controlled bank");
    }

    /**
     * Creates CentralBankException indicating not positive transaction sum
     * @return CentralBankException indicating not positive transaction sum
     */
    public static CentralBankException notPositiveTransactionSum()
    {
        return new CentralBankException("Sum of transaction must be more thn 0");
    }

    /**
     * Creates CentralBankException indicating not positive transaction limit
     * @return CentralBankException indicating not positive transaction limit
     */
    public static CentralBankException NotPositiveTransactionLimit()
    {
        return new CentralBankException("Doubtful client transaction limit must be positive");
    }
}
