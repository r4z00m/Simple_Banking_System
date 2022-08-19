package app;

import models.Account;
import repositories.AccountRepository;
import services.AccountService;

import java.util.Optional;
import java.util.Scanner;

public class BankingSystem {
    private final AccountRepository accountRepository;

    public BankingSystem(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void work() {
        accountRepository.createTable();

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                printMenu();
                String userInput = scanner.nextLine();
                if (userInput.equals("0")) {
                    System.out.println("Bye!");
                    break;
                } else if (userInput.equals("1")) {
                    createAccount();
                } else if (userInput.equals("2")) {
                    loginIntoAccount(scanner);
                } else {
                    System.out.println("Wrong command! Try again!");
                }
            }
        } catch (Exception e) {
            System.err.println("Error!");
        }
    }

    private void printMenu() {
        System.out.println("1. Create an account");
        System.out.println("2. Log into account");
        System.out.println("0. Exit");
    }

    private void createAccount() {
        Account account = new Account();
        account.setCardNumber(AccountService.generateCardNumber());
        account.setPinCode(AccountService.generatePinCode());
        account.setBalance(0);
        accountRepository.addAccount(account);
        System.out.println("Your card has been created");
        System.out.println("Your card number:");
        System.out.println(account.getCardNumber());
        System.out.println("Your card PIN:");
        System.out.println(account.getPinCode());
    }

    private void loginIntoAccount(Scanner scanner) {
        try {
            System.out.println("Enter your card number:");
            String cardNumber = scanner.nextLine();
            System.out.println("Enter your PIN:");
            String pinCode = scanner.nextLine();
            Account toFindAccount = new Account();
            toFindAccount.setCardNumber(cardNumber);
            toFindAccount.setPinCode(pinCode);
            Optional<Account> account = accountRepository
                    .findAccountByCardNumberAndPinCode(toFindAccount);
            if (account.isEmpty()) {
                throw new NumberFormatException();
            } else {
                accountLoop(scanner, account.get());
            }
        } catch (NumberFormatException e) {
            System.out.println("Wrong card number or PIN!");
        }
    }

    private void accountLoop(Scanner scanner, Account account) {
        System.out.println("You have successfully logged in!");
        while (true) {
            printAccountMenu();
            String userInput = scanner.nextLine();
            if (userInput.equals("0")) {
                System.out.println("Bye!");
                System.exit(0);
            } else if (userInput.equals("1")) {
                System.out.println("Balance: " + getBalance(account));
            } else if (userInput.equals("2")) {
                addIncome(scanner, account);
            } else if (userInput.equals("3")) {
                doTransfer(scanner, account);
            } else if (userInput.equals("4")) {
                deleteAccount(account);
                break;
            } else if (userInput.equals("5")) {
                System.out.println("You have successfully logged out!");
                break;
            } else {
                System.out.println("Command not found! Try again!");
            }
        }
    }

    private void printAccountMenu() {
        System.out.println("1. Balance");
        System.out.println("2. Add income");
        System.out.println("3. Do transfer");
        System.out.println("4. Close account");
        System.out.println("5. Log out");
        System.out.println("0. Exit");
    }

    private void doTransfer(Scanner scanner, Account account) {
        System.out.println("Transfer");
        System.out.println("Enter card number:");
        String cardNumberToTransfer = scanner.nextLine();
        if (cardNumberToTransfer.equals(account.getCardNumber())) {
            System.out.println("You can't transfer money to the same account!");
            return;
        }
        if (AccountService.calculateCheckSum(cardNumberToTransfer) % 10 != 0) {
            System.out.println("Probably you made a mistake in the card number. Please try again!");
            return;
        }
        Optional<Account> toTransfer = accountRepository
                .findAccountByCardNumber(cardNumberToTransfer);
        if (toTransfer.isEmpty()) {
            System.out.println("Such a card does not exist.");
            return;
        }
        System.out.println("Enter how much money you want to transfer:");
        int amountToTransfer = Integer.parseInt(scanner.nextLine());
        if (account.getBalance() < amountToTransfer) {
            System.out.println("Not enough money!");
            return;
        }
        accountRepository.doTransfer(account, toTransfer.get(), amountToTransfer);
        System.out.println("Success!");
    }

    private void deleteAccount(Account account) {
        accountRepository.deleteAccount(account);
        System.out.println("The account has been closed!");
    }

    private void addIncome(Scanner scanner, Account account) {
        System.out.println("Enter income:");
        int income = Integer.parseInt(scanner.nextLine());
        account.setBalance(account.getBalance() + income);
        accountRepository.updateAccountBalance(account);
        System.out.println("Income was added!");
    }

    private int getBalance(Account account) {
        return accountRepository
                .findAccountByCardNumberAndPinCode(account)
                .map(Account::getBalance)
                .orElse(0);
    }
}
