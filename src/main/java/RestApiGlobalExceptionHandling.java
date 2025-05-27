import java.io.*;

// Zadanie 5
// TODO 1: Napisz 3 własne wyjątki:
// - InvalidRequestException (extends Exception)
// - DatabaseUnavailableException (extends Exception)
// - UnauthorizedAccessException (extends Exception)
// Każdy wyjątek powinien mieć konstruktor przyjmujący komunikat błędu (String message).


class ApiResponse {
    private String status;
    private String message;

    // TODO 2: Zaimplementuj konstruktor, gettery i settery
}

public class RestApiGlobalExceptionHandling {

    // Symuluje pobieranie danych z bazy danych
    private static String fetchFromDatabase(boolean shouldFail) throws DatabaseUnavailableException {
        if (shouldFail) {
            throw new DatabaseUnavailableException("Database connection timed out.");
        }
        return "Data from DB";
    }

    // Symuluje walidację żądania
    private static void validateRequest(boolean isInvalid) throws InvalidRequestException {
        // TODO 3: Jeśli parametr 'isInvalid' jest true, wyrzuć InvalidRequestException z odpowiednim komunikatem
    }

    // Symuluje sprawdzenie autoryzacji
    private static void checkAuthorization(boolean isUnauthorized) throws UnauthorizedAccessException {
        // TODO 4: Jeśli 'isUnauthorized' jest true, wyrzuć UnauthorizedAccessException z odpowiednim komunikatem
    }

    // Przetwarza żądanie API i obsługuje wyjątki globalnie
    public static ApiResponse processApiRequest(boolean isInvalid, boolean isDbDown, boolean isUnauthorized) {
        // TODO 5: Zainicjuj obiekt ApiResponse
        ApiResponse response = null;

        try (BufferedWriter logWriter = new BufferedWriter(new FileWriter("api_log.txt", true))) {

            // TODO 6: Wywołaj validateRequest(isInvalid)
            // TODO 7: Wywołaj checkAuthorization(isUnauthorized)
            // TODO 8: Wywołaj fetchFromDatabase(isDbDown)

            response = new ApiResponse("SUCCESS", "Request processed successfully.");

        } catch (InvalidRequestException e) {
            // TODO 9: Ustaw response na "BAD_REQUEST" z komunikatem wyjątku
        } catch (DatabaseUnavailableException e) {
            // TODO 10: Ustaw response to "SERVICE_UNAVAILABLE" z komunikatem wyjątku
        } catch (UnauthorizedAccessException e) {
            // TODO 11: Ustaw response to "UNAUTHORIZED" z komunikatem wyjątku
        } catch (IOException e) {
            // TODO 12: Zaloguj błędy IO (np. "Nie udało się zapisać logów: " + e.getMessage())
        } finally {
            // TODO 13: Upewnij się, że response nigdy nie jest null (domyślnie "INTERNAL_SERVER_ERROR")
        }

        return response;
    }

    public static void main(String[] args) {
        // Test cases (symulacja różnych scenariuszy błędów)
        ApiResponse response1 = processApiRequest(true, false, false);  // Powinno zwrócić BAD_REQUEST
        ApiResponse response2 = processApiRequest(false, true, false);  // Powinno zwrócić SERVICE_UNAVAILABLE
        ApiResponse response3 = processApiRequest(false, false, true);  // Powinno zwrócić UNAUTHORIZED
        ApiResponse response4 = processApiRequest(false, false, false); // Powinno zwrócić SUCCESS

        // TODO 14: Wyprintuj wszystkie odpowiedzi, aby zweryfikować działanie
    }
}