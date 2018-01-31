package zhangyi.training.school.rp.akkastream.bank.domain;

import java.time.LocalDate;
import java.util.Date;

public class Transaction {
    private String id;
    private String accountNo;
    private TransactionType debitCredit;
    private double amount;
    private LocalDate date;

    public Transaction(String id, String accountNo, TransactionType debitCredit, double amount) {
        this.id = id;
        this.accountNo = accountNo;
        this.debitCredit = debitCredit;
        this.amount = amount;
        this.date = LocalDate.now();
    }

    public String getId() {
        return id;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public TransactionType getDebitCredit() {
        return debitCredit;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }
}
