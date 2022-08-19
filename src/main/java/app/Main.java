package app;

import dao.AccountDAO;
import dao.AccountDAOImpl;
import repositories.AccountRepository;
import repositories.AccountRepositoryImpl;
import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Error! You should to put path to database!");
            System.out.println("java 'program_name' -fileName 'path_to_database'");
            return;
        }

        if (!args[0].equals("-fileName")) {
            System.out.println("Error! Bad arguments!");
            return;
        }

        String url = "jdbc:sqlite:" + args[1];

        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);

        try (Connection connection = dataSource.getConnection()) {
            AccountDAO accountDAO = new AccountDAOImpl(connection);
            AccountRepository accountRepository = new AccountRepositoryImpl(accountDAO);
            BankingSystem bankingSystem = new BankingSystem(accountRepository);
            bankingSystem.work();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}