package ru.Khalilov.banks.domain.exceptions;

import ru.Khalilov.banks.domain.models.AccountId;
import ru.Khalilov.banks.domain.models.BankId;

import java.util.UUID;

/**
 * Thrown to indicate invalid arguments and incorrect operations service-related classes and it's methods.
 */
public class ServiceException extends GeneralBankException {
    /**
     * Constructs ServiceException with the specified detail message
     *
     * @param message - the detail message
     */
    protected ServiceException(String message) {
        super(message);
    }

    /**
     * Creates ServiceException indicating attempt to get non-existing bank by its ID
     * @param id - given bank's Id
     * @return ServiceException indicating attempt to get non-existing bank by its ID
     */
    public static ServiceException noBankFound(BankId id)
    {
        return new ServiceException("No bank with id '" + id + "' was found");
    }

    /**
     * Creates ServiceException indicating attempt to perform an operation with bank without chosen one
     * @return ServiceException indicating attempt to perform an operation with bank without chosen one
     */
    public static ServiceException noBankChosen()
    {
        return SomethingNotChosen("bank");
    }

    /**
     * Creates ServiceException indicating attempt to perform an operation with tariff without chosen one
     * @return ServiceException indicating attempt to perform an operation with tariff without chosen one
     */
    public static ServiceException noTariffChosen()
    {
        return SomethingNotChosen("tariff");
    }

    /**
     * Creates ServiceException indicating attempt to get non-existing tariff by ID
     * @param id - given ID
     * @return ServiceException indicating attempt to get non-existing tariff by ID
     */
    public static ServiceException noTariffFound(UUID id)
    {
        return new ServiceException("No tariff with id '" + id + "' was found");
    }

    /**
     * Creates ServiceException indicating attempt to get non-existing client by ID
     * @param id - given ID
     * @return ServiceException indicating attempt to get non-existing client by ID
     */
    public static ServiceException noClientFound(UUID id)
    {
        return new ServiceException("No client with id '" + id + "' was found");
    }

    /**
     * Creates ServiceException indicating attempt to perform an operation with client without chosen one
     * @return ServiceException indicating attempt to perform an operation with client without chosen one
     */
    public static ServiceException noClientChosen()
    {
        return SomethingNotChosen("client");
    }

    /**
     * Creates ServiceException indicating attempt to perform an operation with client email without set one
     * @return ServiceException indicating attempt to perform an operation with client email without set one
     */
    public static ServiceException clientWithNoEmail()
    {
        return new ServiceException("Operation can't be executed: this client does not have an email");
    }

    /**
     * Creates ServiceException indicating attempt to get non-existing account by ID
     * @param id - given ID
     * @return ServiceException indicating attempt to get non-existing account by ID
     */
    public static ServiceException noAccountFound(AccountId id)
    {
        return new ServiceException("No account with id '" + id + "' was found");
    }

    /**
     * Creates ServiceException indicating attempt to perform an operation with account without chosen one
     * @return ServiceException indicating attempt to perform an operation with account without chosen one
     */
    public static ServiceException noAccountChosen()
    {
        return SomethingNotChosen("account");
    }

    private static ServiceException SomethingNotChosen(String name)
    {
        return new ServiceException("To do this operation you should choose " + name + " first");
    }
}
