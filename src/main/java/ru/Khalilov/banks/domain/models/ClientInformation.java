package ru.Khalilov.banks.domain.models;

import lombok.NonNull;

/**
 * Simple model of client with basic information. Necessarily contains name and surname, other properties can be null.
 * @param name - name of client. Can't be null.
 * @param surname - surname of client. Can't be null.
 * @param address - Address of client.
 * @param passport - Passport of client.
 * @param emailImpl - Email of client.
 *
 * @see Address
 * @see Passport
 * @see EmailImpl
 * @see ru.Khalilov.banks.domain.entities.Client
 */
public record ClientInformation(@NonNull String name, @NonNull String surname, Address address, Passport passport, EmailImpl emailImpl) {
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(name).append(" ").append(surname);

        if (address != null)
        {
            builder.append("\nAddress: ").append(address);
        }

        if (passport != null)
        {
            builder.append("\nPassport: ").append(passport);
        }

        if (emailImpl != null)
        {
            builder.append("\nEmail: ").append(emailImpl);
        }

        return builder.toString();
    }
}
