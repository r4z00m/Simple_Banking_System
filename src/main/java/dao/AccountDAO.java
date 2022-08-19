package dao;

import models.Account;

import java.util.Optional;

public interface AccountDAO extends CrudDAO {
    void createTable();
    Optional<Account> read(String cardNumber);
    void doTransfer(Account account, Account toTransfer, int amountToTransfer);
}
