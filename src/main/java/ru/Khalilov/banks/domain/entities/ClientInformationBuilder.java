package ru.Khalilov.banks.domain.entities;

import lombok.Getter;
import lombok.NonNull;
import ru.Khalilov.banks.domain.exceptions.ClientException;
import ru.Khalilov.banks.domain.models.Address;
import ru.Khalilov.banks.domain.models.ClientInformation;
import ru.Khalilov.banks.domain.models.EmailImpl;
import ru.Khalilov.banks.domain.models.Passport;

/**
 * Simple client information builder. Accepts null address, passport and email.
 *
 * @see ClientInformation
 */
public class ClientInformationBuilder {
    @Getter @NonNull
    private String name;
    @Getter @NonNull
    private String surname;
    @Getter
    private Address address;
    @Getter
    private Passport passport;
    @Getter
    private EmailImpl email;

    /**
     * Initialise properties with nulls except name and surname: they are assigned with empty strings.
     */
    public ClientInformationBuilder() {
        clean();
    }

    /**
     * Initialise data from existing ClientInformation object. Used mostly to change existing client.
     * @param baseClientInformation - initial client information
     */
    public ClientInformationBuilder(ClientInformation baseClientInformation) {
        name = baseClientInformation.name();
        surname = baseClientInformation.surname();
        address = baseClientInformation.address();
        passport = baseClientInformation.passport();
        email = baseClientInformation.emailImpl();
    }

    /**
     * Sets new name if it is valid
     * @param newName - name to set
     * @return used ClientInformationBuilder
     * @throws ClientException - in case of invalid given name
     */
    public ClientInformationBuilder withName(String newName) throws ClientException {
        name = validateNamePart(newName);
        return this;
    }

    /**
     * Sets new surname if it is valid
     * @param newSurname - surname to set
     * @return used ClientInformationBuilder
     * @throws ClientException - in case of invalid given surname
     */
    public ClientInformationBuilder withSurname(String newSurname) throws ClientException {
        surname = validateNamePart(newSurname);
        return this;
    }

    /**
     * Sets new address
     * @param address - address to set
     * @return used ClientInformationBuilder
     * @see Address
     */
    public ClientInformationBuilder withAddress(Address address) {
        this.address = address;
        return this;
    }

    /**
     * Set new passport
     * @param passport - passport to set
     * @return used ClientInformationBuilder
     * @see Passport
     */
    public ClientInformationBuilder withPassport(Passport passport) {
        this.passport = passport;
        return this;
    }

    /**
     * Set new email
     * @param email - email to set
     * @return used ClientInformationBuilder
     * @see EmailImpl
     */
    public ClientInformationBuilder withEmail(EmailImpl email) {
        this.email = email;
        return this;
    }

    /**
     * Creates new ClientInformation object with saved information
     * @return new ClientInformation object with saved information
     * @throws ClientException - in case of name or surname is empty string
     * @see ClientInformation
     */
    public ClientInformation build() throws ClientException {
        if (name.equals("") || surname.equals("")) {
            throw ClientException.clientWithoutNameOrSurname();
        }

        return new ClientInformation(name, surname, address, passport, email);
    }

    /**
     * Set all properties to its valid empty values (empty string for name and surname, null for address, passport and email)
     */
    public void clean() {
        name = "";
        surname = "";
        address = null;
        passport = null;
        email = null;
    }

    /**
     * Checks if given string is valid part of client's name
     * @param part - string to verify
     * @return true if it is a valid part of name, false otherwise
     */
    public boolean isNamePartValid(String part)
    {
        return !part.equals("") && part.chars().allMatch(letter -> Character.getType(letter) == Character.LOWERCASE_LETTER ||
                                                                   Character.getType(letter) == Character.UPPERCASE_LETTER);
    }

    private String validateNamePart(String name) throws ClientException
    {
        if (!isNamePartValid(name))
        {
            throw ClientException.invalidNamePart(name);
        }

        return name;
    }
}
