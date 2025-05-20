import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.Stack;

// Custom exception hierarchy - Base class for all calculator exceptions
class CalculatorError extends Exception {
    public CalculatorError(String message) {
        super(message);
    }
}

// Input validation exceptions
class InvalidInputError extends CalculatorError {
    public InvalidInputError(String input) {
        super("Invalid input: " + input);
    }
}

// Math operation exceptions
class DivisionByZeroError extends CalculatorError {
    public DivisionByZeroError() {
        super("Division by zero is not allowed");
    }
}

class NegativeRootError extends CalculatorError {
    public NegativeRootError() {
        super("Cannot take square root of negative number");
    }
}

// Syntax exceptions
class ParenthesisMismatchError extends CalculatorError {
    public ParenthesisMismatchError() {
        super("Mismatched parentheses in expression");
    }
}

// Arithmetic exceptions
class OverflowError extends CalculatorError {
    public OverflowError(String operation) {
        super("Overflow occurred during " + operation);
    }
}

public class SmartCalculator {
    // Logger for error logging
    private static final String LOG_FILE = "calculator_errors.log";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String calculator = "=====================" + "\n" +
                "== " + "[             ]" + " ==" + "\n" +
                "== " + " 7   8   9   / " + " ==" + "\n" +
                "== " + " 4   5   6   * " + " ==" + "\n" +
                "== " + " 1   2   3   + " + " ==" + "\n" +
                "== " + " .   0   ^   - " +  " ==" + "\n" +
                "=====================" + "\n";

        System.out.println("\n\nSmart Calculator\n\n" +
                calculator +
                "'q' to quit");

        while (true) {
            System.out.print("Enter an input: ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("q")) {
                System.out.println("Exiting the calculator. Goodbye!");
                break;
            }

            // Input validation using assertions (disabled by default, enable with -ea)
            assert input != null && !input.trim().isEmpty() : "Input cannot be null or empty";

            try {
                // First try block for input validation
                try {
                    validateInput(input);
                } catch (InvalidInputError e) {
                    logError(e); // Error logging
                    System.out.println("Input Error: " + e.getMessage());
                    continue;
                }

                // Second try block for evaluation
                double result = 0;
                try {
                    result = evaluateExpression(input);
                } catch (DivisionByZeroError e) {
                    logError(e);
                    System.out.println("Math Error: " + e.getMessage());
                    continue;
                } catch (OverflowError | NegativeRootError e) {
                    // Capture multiple exceptions in one catch block
                    logError(e);
                    System.out.println("Math Error: " + e.getMessage());
                    continue;
                } catch (ParenthesisMismatchError e) {
                    logError(e);
                    System.out.println("Syntax Error: " + e.getMessage());
                    continue;
                } catch (CalculatorError e) {
                    logError(e);
                    System.out.println("Calculation Error: " + e.getMessage());
                    continue;
                }

                // Else block executes when no exceptions occur
                System.out.println("Result: " + result);

            } finally {
                // This block always executes for cleanup
                System.out.println("Calculation attempt completed");

                // Interactive debugging - could be enhanced with a debug flag
                if (System.getProperty("debug") != null) {
                    System.out.println("[DEBUG] Current expression: " + input);
                }
            }
        }
        scanner.close();
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

    // Input validation with assertions and regex checks
    private static void validateInput(String input) throws InvalidInputError {
        // Check for empty input
        if (input.trim().isEmpty()) {
            throw new InvalidInputError("Empty expression");
        }

        // Allow numbers, operators, and parentheses
        if (!input.matches("^[0-9+\\-*/.^()\\s]*$")) {
            throw new InvalidInputError("Contains invalid characters");
        }

        // Additional validation for expression structure
        if (input.matches(".*[+\\-*/^]{2,}.*")) {
            throw new InvalidInputError("Consecutive operators");
        }
    }

    // Main evaluation method with exception handling for mathematical expressions
    private static double evaluateExpression(String input) throws CalculatorError {
        try {
            // Convert to postfix notation
            String postfix = infixToPostfix(input);

            // Evaluate postfix expression
            return evaluatePostfix(postfix);
        } catch (ArithmeticException e) {
            // Convert standard Java exceptions to our custom exceptions
            if (e.getMessage().contains("/ by zero")) {
                throw new DivisionByZeroError();
            } else if (e.getMessage().contains("negative")) {
                throw new NegativeRootError();
            }
            throw new CalculatorError(e.getMessage());
        } catch (NumberFormatException e) {
            throw new InvalidInputError("Invalid number format");
        }
    }

    // Converts infix to postfix notation with parenthesis validation
    private static String infixToPostfix(String infix) throws ParenthesisMismatchError, InvalidInputError {
        StringBuilder output = new StringBuilder();
        Stack<String> stack = new Stack<>();
        String[] tokens = infix.replaceAll("\\s+", "").split("(?<=[-+*/^()])|(?=[-+*/^()])");

        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];

            if (token.matches("[0-9]+(\\.[0-9]+)?")) {
                // If the token is a number, add it to the output
                output.append(token).append(' ');
            } else if (token.equals("-") && (i == 0 || tokens[i - 1].equals("(") || isOperator(tokens[i - 1].charAt(0)))) {
                // Handle negative numbers (e.g., -2)
                if (i + 1 < tokens.length && tokens[i + 1].matches("[0-9]+(\\.[0-9]+)?")) {
                    output.append(token).append(tokens[++i]).append(' ');
                } else {
                    throw new InvalidInputError("Invalid negative number format");
                }
            } else if (isOperator(token.charAt(0))) {
                // Handle operators with precedence
                while (!stack.isEmpty() && isOperator(stack.peek().charAt(0)) &&
                        precedence(token.charAt(0)) <= precedence(stack.peek().charAt(0))) {
                    output.append(stack.pop()).append(' ');
                }
                stack.push(token);
            } else if (token.equals("(")) {
                stack.push(token);
            } else if (token.equals(")")) {
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    output.append(stack.pop()).append(' ');
                }
                if (stack.isEmpty()) {
                    throw new ParenthesisMismatchError();
                }
                stack.pop(); // Remove '('
            } else {
                throw new InvalidInputError("Unknown token: " + token);
            }
        }

        while (!stack.isEmpty()) {
            if (stack.peek().equals("(")) {
                throw new ParenthesisMismatchError();
            }
            output.append(stack.pop()).append(' ');
        }

        return output.toString().trim();
    }

    // Evaluates postfix expression with stack operations
    private static double evaluatePostfix(String postfix) throws CalculatorError {
        Stack<Double> stack = new Stack<>();
        String[] tokens = postfix.split("\\s+");

        for (String token : tokens) {
            if (token.matches("-?[0-9]+(\\.[0-9]+)?")) { // Match both positive and negative numbers
                stack.push(Double.parseDouble(token));
            } else if (isOperator(token.charAt(0))) {
                if (stack.size() < 2) {
                    throw new InvalidInputError("Insufficient operands for operator " + token);
                }
                double b = stack.pop();
                double a = stack.pop();
                stack.push(performOperation(a, b, token.charAt(0)));
            } else {
                throw new InvalidInputError("Unknown token: " + token);
            }
        }

        if (stack.size() != 1) {
            throw new InvalidInputError("Invalid expression");
        }

        return stack.pop();
    }

    // Performs mathematical operations with overflow checks
    private static double performOperation(double a, double b, char op) throws CalculatorError {
        switch (op) {
            case '+':
                double sum = a + b;
                if ((a > 0 && b > 0 && sum < 0) || (a < 0 && b < 0 && sum > 0)) {
                    throw new OverflowError("addition");
                }
                return sum;
            case '-':
                double diff = a - b;
                if ((a > 0 && b < 0 && diff < 0) || (a < 0 && b > 0 && diff > 0)) {
                    throw new OverflowError("subtraction");
                }
                return diff;
            case '*':
                double product = a * b;
                if (a != 0 && product / a != b) {
                    throw new OverflowError("multiplication");
                }
                return product;
            case '/':
                if (b == 0) {
                    throw new DivisionByZeroError();
                }
                return a / b;
            case '^':
                if (a < 0 && b != (int)b) {
                    throw new NegativeRootError();
                }
                double power = Math.pow(a, b);
                if (Double.isInfinite(power)) {
                    throw new OverflowError("exponentiation");
                }
                return power;
            default:
                throw new CalculatorError("Unknown operator: " + op);
        }
    }

    // Helper methods
    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
    }

    private static int precedence(char op) {
        switch (op) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            case '^':
                return 3;
            default:
                return 0;
        }
    }
}