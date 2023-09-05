package ru.Khalilov.banks.domain.models;

import lombok.Getter;
import ru.Khalilov.banks.domain.exceptions.PassportException;

/**
 * Simple model of passport. Consists of its series and number which are validated at initialization.
 */
public class Passport {
    /**
     * Valid series length
     */
    public static final int SERIES_LENGTH = 4;
    /**
     * Valid number length
     */
    public static final int NUMBER_LENGTH = 6;

    @Getter
    private int series;
    @Getter
    private int number;

    /**
     * Constructs passport from two strings if they are valid series and number
     * @param series - passport series. Must consist of 4 decimal digits
     * @param number - passport number. Must consist of 6 decimal digits
     * @throws PassportException in case of invalid series or number
     */
    public Passport(String  series, String number) throws PassportException {
        if (series.length() != SERIES_LENGTH ||
            !series.chars().allMatch(c -> Character.getType(c) == Character.DECIMAL_DIGIT_NUMBER)) {
            throw PassportException.InvalidPassportSeries(series);
        }

        if (number.length() != NUMBER_LENGTH ||
                !number.chars().allMatch(c -> Character.getType(c) == Character.DECIMAL_DIGIT_NUMBER)) {
            throw PassportException.InvalidPassportNumber(number);
        }

        this.series = Integer.parseInt(series);
        this.number = Integer.parseInt(number);
    }

    @Override
    public String toString() {
        return "Passport{" +
                "series=" + series +
                ", number=" + number +
                '}';
    }
}
