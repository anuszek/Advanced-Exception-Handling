import java.io.*;
import java.time.*;
import java.time.format.*;
import java.util.*;

public class InputValidatorWithLogging {
    private static final String LOG_FILE = "errors.log";

    // Custom exception for invalid numbers
    static class InvalidNumberException extends Exception {
        public InvalidNumberException(String message) {
            super(message);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int attempts = 0;
        boolean validInput = false;
        int number = 0;

        while (attempts < 3 && !validInput) {
            try {
                System.out.print("Enter a positive integer: ");
                number = scanner.nextInt();

                if (number <= 0) {
                    throw new InvalidNumberException("Number must be positive");
                }

                validInput = true;
                System.out.println("Valid input: " + number);

            } catch (InvalidNumberException e) {
                System.out.println("Invalid input! " + e.getMessage());
                logError(e);
                attempts++;
                scanner.nextLine(); // Clear invalid input
            } catch (InputMismatchException e) {
                System.out.println("Invalid number format!");
                logError(e);
                attempts++;
                scanner.nextLine(); // Clear invalid input
            } finally {
                if (attempts >= 3 || validInput) {
                    System.out.println("Closing scanner...");
                    scanner.close();
                }
            }
        }

        if (!validInput) {
            System.out.println("Maximum attempts reached.");
        }
    }

    // Error logging method
    private static void logError(Exception e) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            writer.println(timestamp + " - " + e.getClass().getSimpleName() + ": " + e.getMessage());
        } catch (IOException ioException) {
            System.err.println("Failed to log error: " + ioException.getMessage());
        }
    }
}