package repositories;

import models.Account;

import java.util.Optional;

public interface AccountRepository extends CrudRepository {
    Optional<Account> findAccountByCardNumber(String cardNumber);
    void createTable();
    void doTransfer(Account account, Account toTransfer, int amountToTransfer);
}
