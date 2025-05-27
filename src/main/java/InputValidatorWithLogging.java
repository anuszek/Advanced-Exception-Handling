import java.util.*;

// Zadanie 4
// Uzupełnij kod, aby obsługiwał wyjątki i logował błędy do pliku errors.log.


public class InputValidatorWithLogging {
    private static final String LOG_FILE = "errors.log";

    // TODO 1: Zdefiniuj własną klasę wyjątku InvalidNumberException
    static class ______ extends ______ {
        public ______(String message) {
            ______
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int attempts = 0;
        boolean validInput = false;
        int number = 0;

        // TODO 2: Użyj pętli while do zebrania poprawnego wejścia od użytkownika
        while (attempts < 3 && !validInput) {
            ______ {
                System.out.print("Enter a positive integer: ");
                number = scanner.nextInt();

                if (number <= 0) {
                    throw new ______("Number must be positive");
                }

                validInput = true;
                System.out.println("Valid input: " + number);

            } ______ (______ e) {
                System.out.println("Invalid input! " + e.getMessage());
                logError(e);
                attempts++;
                scanner.nextLine();
            } ______ (______ e) {
                System.out.println("Invalid number format!");
                logError(e);
                attempts++;
                scanner.nextLine();
            } ______ {
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

    // TODO 3: Zaimplementuj metodę logError, która zapisuje wyjątek do pliku errors.log (HINT: skorzystaj z projektu)
    private static void logError(Exception e) {
        ______
    }
}