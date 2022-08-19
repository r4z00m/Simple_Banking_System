package repositories;

import dao.AccountDAO;
import models.Account;

import java.util.Optional;

public class AccountRepositoryImpl implements AccountRepository {
    private final AccountDAO accountDAO;

    public AccountRepositoryImpl(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    @Override
    public void addAccount(Account account) {
        accountDAO.create(account);
    }

    @Override
    public Optional<Account> findAccountByCardNumberAndPinCode(Account account) {
        return accountDAO.read(account.getCardNumber(), account.getPinCode());
    }

    @Override
    public Optional<Account> findAccountByCardNumber(String cardNumber) {
        return accountDAO.read(cardNumber);
    }

    @Override
    public void updateAccountBalance(Account account) {
        accountDAO.update(account);
    }

    @Override
    public void deleteAccount(Account account) {
        accountDAO.delete(account);
    }

    @Override
    public void createTable() {
        accountDAO.createTable();
    }

    @Override
    public void doTransfer(Account account, Account toTransfer, int amountToTransfer) {
        accountDAO.doTransfer(account, toTransfer, amountToTransfer);
    }
}
