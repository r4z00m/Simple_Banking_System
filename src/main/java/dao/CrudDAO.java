package dao;

import models.Account;

import java.util.Optional;

public interface CrudDAO {
    void create(Account account);
    Optional<Account> read(String cardNumber, String pinCode);
    void update(Account account);
    void delete(Account account);
}
