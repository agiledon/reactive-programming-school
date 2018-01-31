package zhangyi.training.school.rp.akkastream.bank.service;

import zhangyi.training.school.rp.akkastream.bank.domain.Account;
import zhangyi.training.school.rp.akkastream.bank.domain.Transaction;
import zhangyi.training.school.rp.akkastream.bank.repository.AccountRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static zhangyi.training.school.rp.akkastream.bank.domain.TransactionType.Credit;
import static zhangyi.training.school.rp.akkastream.bank.domain.TransactionType.Debit;

public final class OnlineService {
    private static List<Transaction> trans = Arrays.asList(
            new Transaction("t-1", "a-1", Debit, 1000),
            new Transaction("t-2", "a-2", Debit, 1000),
            new Transaction("t-3", "a-3", Credit, 1000),
            new Transaction("t-4", "a-1", Credit, 1000),
            new Transaction("t-5", "a-1", Debit, 1000),
            new Transaction("t-6", "a-2", Debit, 1000),
            new Transaction("t-7", "a-3", Credit, 1000),
            new Transaction("t-8", "a-3", Debit, 1000),
            new Transaction("t-9", "a-2", Credit, 1000),
            new Transaction("t-10", "a-2", Debit, 1000),
            new Transaction("t-11", "a-1", Credit, 1000),
            new Transaction("t-12", "a-3", Debit, 1000)
    );


    public static CompletableFuture<List<String>> allAccounts() {
        return CompletableFuture.supplyAsync(() -> Arrays.asList("a-1", "a-2", "a-3"));
    }

    public static Account queryAccount(String no) {
        Optional<Account> account = AccountRepository.query(no);
        return account.orElseThrow(() -> new RuntimeException("Invalid account number"));
    }

    public static List<Transaction> getBankingTransactions(Account account) {
        return trans.stream().filter(t -> t.getAccountNo() == account.getNo()).collect(Collectors.toList());
    }

    public static List<Transaction> getSettlementTransaction(Account account) {
        return trans.stream().filter(t -> t.getAccountNo() == account.getNo()).collect(Collectors.toList());
    }

    public static Transaction validate(Transaction transaction) {
        return transaction;
    }

    public static CompletableFuture<List<Transaction>> allTransactions() {
        return CompletableFuture.supplyAsync(() -> trans);
    }
}
