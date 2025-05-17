import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.Stack;

// Custom exception hierarchy
class CalculatorError extends Exception {
    public CalculatorError(String message) {
        super(message);
    }
}

class InvalidInputError extends CalculatorError {
    public InvalidInputError(String input) {
        super("Invalid input: " + input);
    }
}

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

class ParenthesisMismatchError extends CalculatorError {
    public ParenthesisMismatchError() {
        super("Mismatched parentheses in expression");
    }
}

class OverflowError extends CalculatorError {
    public OverflowError(String operation) {
        super("Overflow occurred during " + operation);
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String calculator = "=====================" + "\n" +
                "== " + "[             ]" + " ==" + "\n" +
                "== " + " 7   8   9   / " + " ==" + "\n" +
                "== " + " 4   5   6   * " + " ==" + "\n" +
                "== " + " 1   2   3   + " + " ==" + "\n" +
                "== " + " .   0   ^   - " +  " ==" + "\n" +
                "== " + " sqrt  c  entr " +  " ==" + "\n" +
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
                    System.out.println("Input Error: " + e.getMessage());
                    continue;
                }
                // Second try block for evaluation
                double result = 0;
                try {
                    result = evaluateExpression(input);
                } catch (DivisionByZeroError e) {
                    System.out.println("Math Error: " + e.getMessage());
                    continue;
                } catch (OverflowError | NegativeRootError e) {
                    System.out.println("Math Error: " + e.getMessage());
                    continue;
                } catch (ParenthesisMismatchError e) {
                    System.out.println("Syntax Error: " + e.getMessage());
                    continue;
                } catch (CalculatorError e) {
                    System.out.println("Calculation Error: " + e.getMessage());
                    continue;
                }
                // Else block executes when no exceptions occur
                System.out.println("Result: " + result);
           } finally {
                // This block always executes for cleanup
                System.out.println("Calculation attempt completed");
            }
        }
        scanner.close();
    }

    private static void validateInput(String input) throws InvalidInputError {
        // Check for empty input
        if (input.trim().isEmpty()) {
            throw new InvalidInputError("Empty expression");
        }

        // Check for invalid characters
//        if (!input.matches("^[0-9+\\-*/.^()\\s]*$")) {
//            throw new InvalidInputError("Contains invalid characters");
//        }
    }

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
            }
            throw new CalculatorError(e.getMessage());
        }
    }

    private static String infixToPostfix(String infix) throws ParenthesisMismatchError {
        // Implementation of Shunting-yard algorithm
        StringBuilder output = new StringBuilder();
        Stack<Character> stack = new Stack<>();

        for (char token : infix.replaceAll("\\s+", "").toCharArray()) {
            if (Character.isDigit(token) || token == '.') {
                output.append(token);
            } else if (isOperator(token)) {
                output.append(' ');
                while (!stack.isEmpty() && isOperator(stack.peek()) &&
                        precedence(token) <= precedence(stack.peek())) {
                    output.append(stack.pop()).append(' ');
                }
                stack.push(token);
            } else if (token == '(') {
                stack.push(token);
            } else if (token == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    output.append(' ').append(stack.pop());
                }
                if (stack.isEmpty()) {
                    throw new ParenthesisMismatchError();
                }
                stack.pop(); // Remove '(' from stack
            }
        }

        while (!stack.isEmpty()) {
            if (stack.peek() == '(') {
                throw new ParenthesisMismatchError();
            }
            output.append(' ').append(stack.pop());
        }

        return output.toString();
    }

    private static double evaluatePostfix(String postfix) throws CalculatorError {
        Stack<Double> stack = new Stack<>();
        String[] tokens = postfix.split("\\s+");

        for (String token : tokens) {
            if (token.isEmpty()) continue;

            if (token.matches("[0-9]+(\\.[0-9]+)?")) {
                stack.push(Double.parseDouble(token));
            } else if (isOperator(token.charAt(0))) {
                if (stack.size() < 2) {
                    throw new InvalidInputError("Insufficient operands for operator " + token);
                }
                double b = stack.pop();
                double a = stack.pop();
                double result = performOperation(a, b, token.charAt(0));
                stack.push(result);
            } else if (token.equals("^")) {
                if (stack.size() < 2) {
                    throw new InvalidInputError("Insufficient operands for operator " + token);
                }
                double b = stack.pop();
                double a = stack.pop();
                double result = Math.pow(a, b);
                if (Double.isInfinite(result)) {
                    throw new OverflowError("exponentiation");
                }
                stack.push(result);
            } else if (token.equals("sqrt")) {
                if (stack.isEmpty()) {
                    throw new InvalidInputError("No operand for square root");
                }
                double a = stack.pop();
                if (a < 0) {
                    throw new NegativeRootError();
                }
                stack.push(Math.sqrt(a));
            }
        }

        if (stack.size() != 1) {
            throw new InvalidInputError("Invalid expression");
        }

        return stack.pop();
    }

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
                double power = Math.pow(a, b);
                if (Double.isInfinite(power)) {
                    throw new OverflowError("exponentiation");
                }
                return power;
            case 's':
                if (a < 0) {
                    throw new NegativeRootError();
                }
                return Math.sqrt(a);
            default:
                throw new CalculatorError("Unknown operator: " + op);
        }
    }

    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    private static int precedence(char op) {
        switch (op) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                return 0;
        }
    }
}
