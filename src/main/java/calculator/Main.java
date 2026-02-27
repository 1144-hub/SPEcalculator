package calculator;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Calculator calc = new Calculator();
        int choice = 0;

        System.out.println("Welcome to the Scientific Calculator!");

        do {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Square Root (√x)");
            System.out.println("2. Factorial (x!)");
            System.out.println("3. Natural Logarithm (ln(x))");
            System.out.println("4. Exit");
            System.out.print("Enter your choice (1-4): ");

            try {
                choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        System.out.print("Enter a number to find its Square Root: ");
                        double num1 = scanner.nextDouble();
                        System.out.println("Result (√" + num1 + "): " + calc.squareRoot(num1));
                        break;
                    case 2:
                        System.out.print("Enter an integer to find its Factorial: ");
                        int num2 = scanner.nextInt();
                        System.out.println("Result (" + num2 + "!): " + calc.factorial(num2));
                        break;
                    case 3:
                        System.out.print("Enter a number to find its Natural Logarithm: ");
                        double num3 = scanner.nextDouble();
                        System.out.println("Result (ln(" + num3 + ")): " + calc.naturalLog(num3));
                        break;
                    case 4:
                        System.out.println("Exiting Calculator. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice! Please select an option from 1 to 4.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Invalid input format. Please enter a valid number.");
                scanner.nextLine(); // Clear the bad input from the scanner buffer
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }

        } while (choice != 4);

        scanner.close();
    }
}