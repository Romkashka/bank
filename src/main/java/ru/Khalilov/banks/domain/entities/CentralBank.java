package ru.Khalilov.banks.domain.entities;

import lombok.Getter;
import lombok.NonNull;
import ru.Khalilov.banks.domain.exceptions.CentralBankException;
import ru.Khalilov.banks.domain.models.AccountId;
import ru.Khalilov.banks.domain.models.BankId;
import ru.Khalilov.banks.domain.models.TransactionEntity;

import java.math.BigDecimal;
import java.util.*;

public class CentralBank {
    @NonNull
    private final List<Bank> banks;
    @NonNull
    private  final List<Transaction> transactions;
    @NonNull @Getter
    private final Clock clock;
    @NonNull @Getter
    private final UUID centralBankId;

    /**
     * Constructs central bank with given {@link Clock} and id. There are no banks and transactions.
     * @param clock - clock to set
     * @param centralBankId - id to set
     */
    public CentralBank(@NonNull Clock clock, @NonNull UUID centralBankId) {
        this.clock = clock;
        this.centralBankId = centralBankId;
        banks = new ArrayList<>();
        transactions = new ArrayList<>();
    }

    /**
     * Add money to the account. Sum must be positive
     * @param accountId target account id
     * @param sum sum of transaction. Must be positive
     * @return UUID of this transaction
     */
    public @NonNull UUID addMoney(@NonNull AccountId accountId, @NonNull BigDecimal sum) {
        return oneAccountTransaction(accountId, validateTransactionSum(sum));
    }

    /**
     * Withdraw money from the account. Sum must be positive
     * @param accountId target account id
     * @param sum sum of the transaction. Must be positive
     * @return UUID of this transaction
     */
    public @NonNull UUID withdrawMoney(@NonNull AccountId accountId, @NonNull BigDecimal sum) {
        return oneAccountTransaction(accountId, validateTransactionSum(sum).negate());
    }

    /**
     * Transfers money from one account to another. Sum must be positive. If money can't be withdrawn from source account, nothing changes
     * @param accountFromId source account id
     * @param accountToId target account id
     * @param sum sum of transfer
     * @return UUID of transaction
     */
    public @NonNull UUID transferMoney(@NonNull AccountId accountFromId, @NonNull AccountId accountToId, BigDecimal sum) {
        validateTransactionSum(sum);

        Account accountTo = findAccount(accountToId);

        if (accountTo == null) {
            throw CentralBankException.noSuchAccount();
        }

        UUID transactionId = UUID.randomUUID();

        List<TransactionEntity> entities = List.of(
                changeBalanceBy(accountFromId, sum.negate()),
                changeBalanceBy(accountToId, sum)
        );

        transactions.add(new Transaction(entities, transactionId));
        return transactionId;
    }

    /**
     * Cancel transaction if it is present
     * @param transactionId id of transaction to cancel
     */
    public void cancelTransaction(@NonNull UUID transactionId) {
        Optional<Transaction> optionalTransaction = transactions.stream().filter(t -> t.getId().equals(transactionId)).findFirst();

        if (transactions.isEmpty()) {
            throw CentralBankException.noSuchTransaction();
        }

        optionalTransaction.get().cancel();
    }

    public @NonNull Bank createBank(BigDecimal transactionLimitForDoubtfulClients) {
        if (transactionLimitForDoubtfulClients.compareTo(BigDecimal.ZERO) < 0)
        {
            throw CentralBankException.NotPositiveTransactionLimit();
        }

        var result = new Bank(clock, new BankId(UUID.randomUUID()), transactionLimitForDoubtfulClients);
        banks.add(result);
        return result;
    }

    /**
     * Return unmodifiable list of banks
     * @return unmodifiable list of banks
     */
    public List<Bank> getBanks() {
        return Collections.unmodifiableList(banks);
    }

    /**
     * Return unmodifiable list of transactions
     * @return unmodifiable list of transactions
     */
    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    private TransactionEntity changeBalanceBy(@NonNull AccountId accountId, @NonNull BigDecimal sum) {
        return getBankOfAccount(accountId).addTransactionSum(accountId, sum);
    }

    private Account findAccount(@NonNull AccountId accountId) {
        return getBankOfAccount(accountId).findAccount(accountId);
    }

    private Bank getBankOfAccount(@NonNull AccountId accountId) {
        return banks.stream().filter(bank -> bank.getBankID().equals(accountId.bankId())).findFirst().get();
    }

    private @NonNull BigDecimal validateTransactionSum(@NonNull BigDecimal sum) {
        if (sum.compareTo(BigDecimal.ZERO) < 0) {
            throw CentralBankException.notPositiveTransactionSum();
        }

        return sum;
    }

    private @NonNull UUID oneAccountTransaction(@NonNull AccountId accountId, @NonNull BigDecimal sum) {
        UUID transactionId = UUID.randomUUID();
        TransactionEntity entity = changeBalanceBy(accountId, sum);
        transactions.add(new Transaction(List.of(entity), transactionId));
        return transactionId;
    }
}
