package ru.Khalilov.banks.domain.exceptions;

/**
 * Thrown to indicate invalid arguments and incorrect operations in all client-related classes and it's methods.
 */
public class ClientException extends GeneralBankException{
    /**
     * Constructs ClientException with the specified detail message
     * @param message - the detail message
     */
    protected ClientException(String message) {
        super(message);
    }

    /**
     * Invalid format of client's name exception builder
     * @param part - part of name with invalid format
     * @return ClientException indicating invalid format of client's name
     */
    public static ClientException invalidNamePart(String part) {
        return new ClientException("'" + part + "' isn't a valid part of client name");
    }

    /**
     * Incomplete client's name exception builder. Used in Client constructor.
     * @return ClientException indicating attempt of creating client without name of surname
     */
    public static ClientException clientWithoutNameOrSurname() {
        return new ClientException("Client can't be created without name and surname");
    }
}
