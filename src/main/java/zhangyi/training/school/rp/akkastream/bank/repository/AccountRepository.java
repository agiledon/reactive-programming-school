package zhangyi.training.school.rp.akkastream.bank.repository;

import zhangyi.training.school.rp.akkastream.bank.domain.Account;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class AccountRepository {
    private static Map<String, Account> accounts;
    static {
        accounts = new HashMap<>();
        accounts.put("a-1", new Account("a-1", "dg", Optional.of(LocalDate.now())));
        accounts.put("a-2", new Account("a-2", "gh", Optional.of(LocalDate.now())));
        accounts.put("a-3", new Account("a-3", "tr", Optional.of(LocalDate.now())));

    }

    public static Optional<Account> query(String no) {
        return Optional.ofNullable(accounts.get(no));
    }
}
