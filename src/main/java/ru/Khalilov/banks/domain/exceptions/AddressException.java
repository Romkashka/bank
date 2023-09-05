package ru.Khalilov.banks.domain.exceptions;

import ru.Khalilov.banks.domain.models.Address;

/**
 * Thrown to indicate invalid arguments and incorrect operations address-related classes and it's methods.
 *
 * @see Address
 */
public class AddressException extends ClientException{
    /**
     * Constructs AddressException with the specified detail message
     * @param message - the detail message
     */
    protected AddressException(String message) {
        super(message);
    }

    /**
     * Invalid street name format exception builder
     * @param found - given name of street not satisfying preassigned format
     * @return AddressException indicating invalid street name format
     */
    public static AddressException invalidStreetFormat(String found) {
        return new AddressException("String " + found + "sin't a valid street name");
    }

    /**
     * Invalid building number format exception builder
     * @param found - given building number not satisfying preassigned format
     * @return AddressException indicating invalid building number format
     */
    public static AddressException invalidBuildingNumber(int found) {
        return new AddressException(found + "sin't a valid building number");
    }
}
