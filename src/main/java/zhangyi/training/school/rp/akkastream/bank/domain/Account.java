package zhangyi.training.school.rp.akkastream.bank.domain;

import java.time.LocalDate;
import java.util.Optional;

public class Account {
    private String no;
    private String name;
    private Optional<LocalDate> dateOfOpen;
    private Optional<LocalDate> dateOfClose;
    private Balance balance;

    public Account(String no, String name, Optional<LocalDate> dateOfOpen) {
        this.no = no;
        this.name = name;
        this.dateOfOpen = dateOfOpen;
        this.dateOfClose = Optional.empty();
        balance = new Balance(0);
    }

    public Account(String no, String name, Optional<LocalDate> dateOfOpen, Optional<LocalDate> dateOfClose) {
        this(no, name, dateOfOpen);
        this.dateOfClose = dateOfClose;
        balance = new Balance(0);
    }

    public Account(String no, String name, Optional<LocalDate> dateOfOpen, Optional<LocalDate> dateOfClose, Balance balance) {
        this(no, name, dateOfOpen, dateOfClose);
        this.balance = balance;
    }

    public String getNo() {
        return no;
    }

    public String getName() {
        return name;
    }

    public Optional<LocalDate> getDateOfOpen() {
        return dateOfOpen;
    }

    public Optional<LocalDate> getDateOfClose() {
        return dateOfClose;
    }

    public Balance getBalance() {
        return balance;
    }
}
