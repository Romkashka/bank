package ru.Khalilov.banks.domain.exceptions;

/**
 * Thrown to indicate invalid arguments and incorrect operations in all account-related classes and it's methods.
 */
public class AccountException extends GeneralBankException{
    /**
     * Constructs AccountException with the specified detail message
     *
     * @param message - the detail message
     */
    protected AccountException(String message) {
        super(message);
    }

    /**
     * @return AccountException object indicating that balance is too low to execute some operation with this account.
     */
    public static AccountException tooLowFinalBalance()
    {
        return new AccountException("Operation can't be executed: final balance less than minimal balance");
    }

    /**
     * Creates AccountException indicating invalid value of day in month
     * @param found - invalid day number causing exception
     * @return AccountException indicating invalid value of day in month
     */
    public static AccountException unrealMonthlyUpdateDay(int found)
    {
        return new AccountException("Monthly update day must be from 1 to 31, but " + found + " found");
    }

    /**
     * Creates AccountException indicating that not enough money to execute transaction
     * @return AccountException indicating that not enough money to execute transaction
     */
    public static AccountException notEnoughMoney()
    {
        return new AccountException("Transaction can't be proceed: there isn't enough money on account");
    }

    /**
     * Creates AccountException caused by attempt to cancel unregistered transaction. Used if transaction wasn't
     * committed or already canceled.
     * @return AccountException caused by attempt to cancel unregistered transaction
     */
    public static AccountException noSuchTransaction()
    {
        return new AccountException("Account hasn't committed given transaction or it already canceled");
    }

    /**
     * Creates AccountException indicating attempt to update balance after negative time span
     * @return AccountException indicating attempt to update balance after negative time span
     */
    public static AccountException negativeUpdateTimeSpan()
    {
        return new AccountException("Time span must be positive");
    }
}
