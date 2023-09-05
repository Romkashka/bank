package ru.Khalilov.banks.domain.models;

import lombok.Getter;
import ru.Khalilov.banks.domain.exceptions.*;

/**
 * Simple model of address. Consists of street name and building number.
 */
public class Address {

    /**
     * Name of street
     */
    @Getter
    private final String _street;

    /**
     * Building number
     */
    @Getter
    private final int _building;

    /**
     *
     * @param street - name of street, comprises upper and lower cases letters
     * @param building - non-negative integer
     * @throws AddressException in case of invalid street or building formats
     */
    public Address(String street, int building) throws AddressException {
        _street = validateStreet(street);
        _building = validateBuildingNumber(building);
    }

    private String validateStreet(String street) throws AddressException {
        for (int i = 0; i < street.length(); i++) {
            int unicodeCategory = Character.getType(street.charAt(i));
            if (unicodeCategory != Character.LOWERCASE_LETTER && unicodeCategory != Character.UPPERCASE_LETTER) {
                throw AddressException.invalidStreetFormat(street);
            }
        }

        return street;
    }

    private int validateBuildingNumber(int number) throws AddressException{
        if (number < 1) {
            throw AddressException.invalidBuildingNumber(number);
        }

        return number;
    }
}
