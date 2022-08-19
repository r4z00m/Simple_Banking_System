package dao;

import models.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class AccountDAOImpl implements AccountDAO {

    public static final String CREATE = "INSERT INTO card(number, pin, balance) VALUES(?, ?, ?);";
    public static final String READ = "SELECT * FROM card WHERE number=? and pin=?;";
    public static final String UPDATE = "UPDATE card SET balance=? WHERE number=?;";
    public static final String DELETE = "DELETE FROM card WHERE number=? and pin=?;";
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS card (id INTEGER PRIMARY KEY, number TEXT, pin TEXT, balance INTEGER);";
    public static final String FIND_A_CARD = "SELECT * FROM card WHERE number=?;";
    public static final String DECREASE_BALANCE = "UPDATE card SET balance=balance-? WHERE number=?;";
    public static final String INCREASE_BALANCE = "UPDATE card SET balance=balance+? WHERE number=?;";

    private final Connection connection;

    public AccountDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(Account account) {
        try (PreparedStatement preparedStatement = connection
                .prepareStatement(CREATE)) {
            preparedStatement.setString(1, account.getCardNumber());
            preparedStatement.setString(2, account.getPinCode());
            preparedStatement.setInt(3, 0);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Account> read(String cardNumber, String pinCode) {
        try (PreparedStatement preparedStatement = connection
                .prepareStatement(READ)) {
            preparedStatement.setString(1, cardNumber);
            preparedStatement.setString(2, pinCode);
            Optional<Account> account = getAccount(preparedStatement);
            if (account.isPresent()) {
                return account;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Account> read(String cardNumber) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_A_CARD)) {
            preparedStatement.setString(1, cardNumber);
            Optional<Account> account = getAccount(preparedStatement);
            if (account.isPresent()) {
                return account;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void doTransfer(Account account, Account toTransfer, int amountToTransfer) {
        try (PreparedStatement preparedStatementFrom = connection
                .prepareStatement(DECREASE_BALANCE);
             PreparedStatement preparedStatementTo = connection
                     .prepareStatement(INCREASE_BALANCE)) {
            connection.setAutoCommit(false);
            preparedStatementFrom.setInt(1, amountToTransfer);
            preparedStatementFrom.setString(2, account.getCardNumber());
            preparedStatementFrom.executeUpdate();
            preparedStatementTo.setInt(1, amountToTransfer);
            preparedStatementTo.setString(2, toTransfer.getCardNumber());
            preparedStatementTo.executeUpdate();
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Account account) {
        try (PreparedStatement preparedStatement = connection
                .prepareStatement(UPDATE)) {
            preparedStatement.setInt(1, account.getBalance());
            preparedStatement.setString(2, account.getCardNumber());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Account account) {
        try (PreparedStatement preparedStatement = connection
                .prepareStatement(DELETE)) {
            preparedStatement.setString(1, account.getCardNumber());
            preparedStatement.setString(2, account.getPinCode());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createTable() {
        try (PreparedStatement preparedStatement = connection
                .prepareStatement(CREATE_TABLE)) {
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Optional<Account> getAccount(PreparedStatement preparedStatement) throws SQLException {
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                Account account = new Account();
                account.setId(resultSet.getLong("id"));
                account.setCardNumber(resultSet.getString("number"));
                account.setPinCode(resultSet.getString("pin"));
                account.setBalance(resultSet.getInt("balance"));
                return Optional.of(account);
            }
        }
        return Optional.empty();
    }
}
