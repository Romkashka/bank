package ru.Khalilov.banks.domain.entities;

import lombok.Getter;
import lombok.NonNull;
import ru.Khalilov.banks.domain.exceptions.TariffException;
import ru.Khalilov.banks.domain.models.BalanceInterval;
import ru.Khalilov.banks.domain.models.TariffStats;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class TariffBuilder {
    @Getter @NonNull
    private String name;
    @Getter @NonNull
    private String accountType;
    @Getter @NonNull
    private BigDecimal balanceInterest;
    @Getter @NonNull
    private BigDecimal negativeBalanceOperationTax;
    @Getter @NonNull
    private BigDecimal minimalBalance;
    @Getter @NonNull
    private Duration addOnlyPeriod;
    @NonNull
    private List<BalanceInterval> depositPercentages;

    /**
     * Initialise all properties with valid empty values (empty string for name and account type, empty list for deposit intervals, 0 for other fields)
     */
    public TariffBuilder() {
        name = "";
        accountType = "";
        balanceInterest = BigDecimal.ZERO;
        negativeBalanceOperationTax = BigDecimal.ZERO;
        minimalBalance = BigDecimal.ZERO;
        addOnlyPeriod = Duration.ZERO;
        depositPercentages = new ArrayList<>();
    }

    /**
     * Initialise all fields as in given tariff stats
     * @param baseTariffStats - tariff stats with data
     */
    public TariffBuilder(TariffStats baseTariffStats) {
        name = baseTariffStats.name();
        accountType = baseTariffStats.accountType();
        balanceInterest = baseTariffStats.balanceInterest();
        negativeBalanceOperationTax = baseTariffStats.negativeBalanceOperationTax();
        minimalBalance = baseTariffStats.minimalBalance();
        addOnlyPeriod = baseTariffStats.addOnlyPeriod();
        depositPercentages = baseTariffStats.depositPercentages();
    }

    /**
     * Set given name to tariff if it is valid
     * @param name - new name
     * @return used TariffBuilder
     * @throws TariffException - in case of invalid name
     */
    public TariffBuilder withName(String name) throws TariffException {
        this.name = validateName(name);
        return this;
    }

    /**
     * Set given account type to tariff if it is valid
     * @param type - new account type
     * @return used TariffBuilder
     * @throws TariffException - in case of invalid account type
     */
    public TariffBuilder withAccountType(String type) throws TariffException {
        this.accountType = validateAccountType(type);
        return this;
    }

    /**
     * Set given balance interest to tariff it is valid
     * @param interest - new balance interest
     * @return used TariffBuilder
     * @throws TariffException - in case of new interest is lest than 0
     */
    public TariffBuilder withBalanceInterest(BigDecimal interest) throws TariffException {
        if (interest.compareTo(BigDecimal.ZERO) < 0) {
            throw TariffException.negativeBalanceInterest();
        }

        this.balanceInterest = interest;
        return this;
    }

    /**
     * Set given negative operation tax to tariff if it is valid
     * @param tax - new negative operation tax
     * @return used TariffBuilder
     * @throws TariffException - in case of new tax is less than 0
     */
    public TariffBuilder withNegativeOperationTax(BigDecimal tax) throws TariffException {
        if (tax.compareTo(BigDecimal.ZERO) < 0) {
            throw TariffException.negativeTax();
        }

        this.negativeBalanceOperationTax = tax;
        return this;
    }

    /**
     * Set given minimal balance to tariff
     * @param minimalBalance - new minimal balance
     * @return used TariffBuilder
     */
    public TariffBuilder withMinimalBalance(BigDecimal minimalBalance) {
        this.minimalBalance = minimalBalance;
        return this;
    }

    /**
     * Set given add only period to tariff if it is valid
     * @param period - new add only period
     * @return used TariffBuilder
     * @throws TariffException - in case of period is less than 0
     */
    public TariffBuilder withAddOnlyPeriod(Duration period) throws TariffException {
        if (period.isNegative()) {
            throw TariffException.negativeAddOnlyPeriod();
        }

        this.addOnlyPeriod = period;
        return this;
    }

    /**
     * Add given deposit percentage interval to tariff if it is valid
     * @param interval - new deposit percentage interval
     * @return used TariffBuilder
     * @throws TariffException - if given interval intersects with the existing
     */
    public TariffBuilder withDepositPercentage(BalanceInterval interval) throws TariffException {
        if (depositPercentages.stream().anyMatch(current -> current.isIntersects(interval))) {
            throw TariffException.depositIntervalsIntersects();
        }

        depositPercentages.add(interval);
        return this;
    }

    /**
     * Builds new TariffStats with saved properties
     * @return new TariffStats with saved properties
     * @throws TariffException - if name or account type is empty or invalid
     */
    public TariffStats build() throws TariffException {
        validateName(name);
        validateAccountType(accountType);

        return new TariffStats(depositPercentages,
                name,
                accountType,
                balanceInterest,
                negativeBalanceOperationTax,
                minimalBalance,
                addOnlyPeriod,
                UUID.randomUUID());
    }

    /**
     * Returns unmodifiable list of deposit percentage intervals
     * @return unmodifiable list of deposit percentage intervals
     */
    public List<BalanceInterval> getDepositPercentages() {
        return Collections.unmodifiableList(depositPercentages);
    }

    /**
     * Checks if given string is valid part of tariff's name
     * @param part - string to verify
     * @return true if it is a valid part of name, false otherwise
     */
    public boolean isNameValid(String part)
    {
        return !part.equals("") && part.chars().allMatch(letter -> Character.getType(letter) == Character.LOWERCASE_LETTER ||
                Character.getType(letter) == Character.UPPERCASE_LETTER ||
                Character.getType(letter) == Character.SPACE_SEPARATOR);
    }

    /**
     * Checks if given string is valid tariff's account type
     * @param type - string to verify
     * @return true if it is a valid account type, false otherwise
     */
    public boolean isAccountTypeValid(String type) {
        return !type.equals("") && type.chars().allMatch(letter -> Character.getType(letter) == Character.LOWERCASE_LETTER);
    }

    private String validateName(String name) throws TariffException
    {
        if (!isNameValid(name))
        {
            throw TariffException.invalidName(name);
        }

        return name;
    }

    private String validateAccountType(String type) throws TariffException {
        if (!isAccountTypeValid(type)) {
            throw TariffException.invalidAccountType(type);
        }

        return type;
    }
}
