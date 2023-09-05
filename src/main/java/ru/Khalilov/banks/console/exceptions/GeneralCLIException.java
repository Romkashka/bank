package ru.Khalilov.banks.console.exceptions;

import java.util.List;

public class GeneralCLIException extends RuntimeException {
    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    protected GeneralCLIException(String message) {
        super(message);
    }

    public static GeneralCLIException noHandler(List<String> args) {
        StringBuilder builder = new StringBuilder("No handler was found to arguments:");
        args.forEach(s -> builder.append(" ").append(s));
        return new GeneralCLIException(builder.toString());
    }
}
