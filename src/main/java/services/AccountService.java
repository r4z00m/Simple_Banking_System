package services;

import java.util.Random;

public class AccountService {
    private static final Random random = new Random();

    public static String generateCardNumber() {
        StringBuilder sb = new StringBuilder("400000");
        for (int i = 0; i < 9; i++) {
            sb.append(random.nextInt(9));
        }
        int checkSum = calculateCheckSum(sb.toString());
        if (checkSum % 10 == 0) {
            sb.append(0);
        } else {
            sb.append(Math.abs(10 - (checkSum % 10)));
        }
        return sb.toString();
    }

    public static String generatePinCode() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int number = random.nextInt(9);
            if (i == 0 && number == 0) {
                number++;
            }
            sb.append(number);
        }
        return sb.toString();
    }

    public static int calculateCheckSum(String cardNumber) {
        int checkSum = 0;
        for (int i = 0; i < cardNumber.length(); i++) {
            int digit = Character.digit(cardNumber.charAt(i), 10);
            if (i % 2 == 0) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }
            checkSum += digit;
        }
        return checkSum;
    }
}
