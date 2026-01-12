package org.example.cli;

import java.util.Scanner;

/**
 * Utility class for handling user input from the command line.
 * Wraps Scanner and provides convenient methods for reading different types.
 */
public class InputHelper {
    private final Scanner scanner;

    public InputHelper(Scanner scanner) {
        this.scanner = scanner;
    }

    public String readLine() {
        return scanner.nextLine().trim();
    }

    public String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public String readPassword() {
        return scanner.nextLine().trim();
    }

    public String readPassword(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public int readInt() {
        try {
            String input = scanner.nextLine().trim();
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public int readInt(String prompt) {
        System.out.print(prompt);
        return readInt();
    }

    public int readIntOrDefault(String prompt, int defaultValue) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public boolean confirm(String message) {
        System.out.println(message);
        String response = scanner.nextLine().trim().toLowerCase();
        return "yes".equals(response) || "y".equals(response);
    }

    public boolean confirmInline(String prompt) {
        System.out.print(prompt);
        String response = scanner.nextLine().trim().toLowerCase();
        return "yes".equals(response) || "y".equals(response);
    }

    public void close() {
        scanner.close();
    }
}


