import java.io.*;
import java.util.*;

// ===== TODO 1: Custom Exceptions =====
class InvalidRequestException extends Exception {
    public InvalidRequestException(String message) {
        super(message);
    }
}

class DatabaseUnavailableException extends Exception {
    public DatabaseUnavailableException(String message) {
        super(message);
    }
}

class UnauthorizedAccessException extends Exception {
    public UnauthorizedAccessException(String message) {
        super(message);
    }
}

// ===== TODO 2: ApiResponse Class =====
class ApiResponse {
    private String status;
    private String message;

    public ApiResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    // Getters and Setters
    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Status: " + status + " | Message: " + message;
    }
}

public class RestApiGlobalExceptionHandling {

    // ===== Database Simulation =====
    private static String fetchFromDatabase(boolean shouldFail) throws DatabaseUnavailableException {
        if (shouldFail) {
            throw new DatabaseUnavailableException("Database connection timed out.");
        }
        return "Data from DB";
    }

    // ===== TODO 3: Request Validation =====
    private static void validateRequest(boolean isInvalid) throws InvalidRequestException {
        if (isInvalid) {
            throw new InvalidRequestException("Invalid request payload.");
        }
    }

    // ===== TODO 4: Authorization Check =====
    private static void checkAuthorization(boolean isUnauthorized) throws UnauthorizedAccessException {
        if (isUnauthorized) {
            throw new UnauthorizedAccessException("User is not authorized.");
        }
    }

    // ===== TODO 5-13: Global Exception Handling =====
    public static ApiResponse processApiRequest(boolean isInvalid, boolean isDbDown, boolean isUnauthorized) {
        ApiResponse response = null;

        try (BufferedWriter logWriter = new BufferedWriter(new FileWriter("api_log.txt", true))) {

            // ===== TODO 6-8: Call Methods =====
            validateRequest(isInvalid);
            checkAuthorization(isUnauthorized);
            String data = fetchFromDatabase(isDbDown);

            response = new ApiResponse("SUCCESS", "Request processed successfully.");

        } catch (InvalidRequestException e) {
            // ===== TODO 9: BAD_REQUEST =====
            response = new ApiResponse("BAD_REQUEST", e.getMessage());
        } catch (DatabaseUnavailableException e) {
            // ===== TODO 10: SERVICE_UNAVAILABLE =====
            response = new ApiResponse("SERVICE_UNAVAILABLE", e.getMessage());
        } catch (UnauthorizedAccessException e) {
            // ===== TODO 11: UNAUTHORIZED =====
            response = new ApiResponse("UNAUTHORIZED", e.getMessage());
        } catch (IOException e) {
            // ===== TODO 12: Log IO Errors =====
            System.err.println("Failed to write logs: " + e.getMessage());
            response = new ApiResponse("INTERNAL_SERVER_ERROR", "Logging failed.");
        } finally {
            // ===== TODO 13: Ensure Non-Null Response =====
            if (response == null) {
                response = new ApiResponse("INTERNAL_SERVER_ERROR", "Unknown error occurred.");
            }
        }

        return response;
    }

    // ===== TODO 14: Test Cases =====
    public static void main(String[] args) {
        ApiResponse response1 = processApiRequest(true, false, false);   // BAD_REQUEST
        ApiResponse response2 = processApiRequest(false, true, false);  // SERVICE_UNAVAILABLE
        ApiResponse response3 = processApiRequest(false, false, true);  // UNAUTHORIZED
        ApiResponse response4 = processApiRequest(false, false, false); // SUCCESS

        System.out.println(response1);
        System.out.println(response2);
        System.out.println(response3);
        System.out.println(response4);
    }
}