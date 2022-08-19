package repositories;

import models.Account;

import java.util.Optional;

public interface CrudRepository {
    void addAccount(Account account);
    Optional<Account> findAccountByCardNumberAndPinCode(Account account);
    void updateAccountBalance(Account account);
    void deleteAccount(Account account);
}
