package ru.Khalilov.banks.domain.exceptions;

/**
 * Thrown to indicate invalid arguments and incorrect operations tariff-related classes and it's methods.
 */
public class TariffException extends GeneralBankException {
    /**
     * Constructs TariffException with the specified detail message
     *
     * @param message - the detail message
     */
    protected TariffException(String message) {
        super(message);
    }

    /**
     * Creates TariffException indicating attempt to subscribe already subscribed object
     * @return TariffException indicating attempt to subscribe already subscribed object
     */
    public static TariffException alreadySubscribed()
    {
        return new TariffException("Twice subscription isn't allowed");
    }

    /**
     * Creates TariffException indicating invalid name of tariff
     * @param name - given name
     * @return TariffException indicating invalid name of tariff
     */
    public static TariffException invalidName(String name)
    {
        return new TariffException("'" + name + "' is not a valid Tariff name");
    }

    /**
     * Creates TariffException indicating invalid name of tariff type
     * @param type - given type name
     * @return TariffException indicating invalid name of tariff type
     */
    public static TariffException invalidAccountType(String type)
    {
        return new TariffException("'" + type + "' is not a valid Tariff type");
    }

    /**
     * Creates TariffException indicating invalid (negative) given balance interest
     * @return TariffException indicating invalid (negative) given balance interest
     */
    public static TariffException negativeBalanceInterest()
    {
        return new TariffException("Balance interest rate can't be less than 0");
    }

    /**
     * Creates TariffException indicating invalid (negative) given tax for operations with negative balance
     * @return TariffException indicating invalid (negative) given tax for operations with negative balance
     */
    public static TariffException negativeTax()
    {
        return new TariffException("Tax for operations with negative balance can't be less than 0");
    }

    /**
     * Creates TariffException indicating invalid (negative) given add-only period
     * @return TariffException indicating invalid (negative) given add-only period
     */
    public static TariffException negativeAddOnlyPeriod()
    {
        return new TariffException("Add-only period can't be less than 0");
    }

    /**
     * Creates TariffException indicating invalid (intersecting with existing one) given interest rate interval
     * @return TariffException indicating invalid (intersecting with existing one) given interest rate interval
     */
    public static TariffException depositIntervalsIntersects()
    {
        return new TariffException("New interval intersects with existing");
    }
}
