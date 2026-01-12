package org.example.cli.menu;

import org.example.cli.CliContext;
import org.example.cli.InputHelper;
import org.example.cli.ServiceContainer;

/**
 * Handles account-related operations like deletion.
 */
public class AccountMenu {
    private final CliContext context;
    private final InputHelper input;
    private final ServiceContainer services;

    public AccountMenu(CliContext context, InputHelper input, ServiceContainer services) {
        this.context = context;
        this.input = input;
        this.services = services;
    }

    /**
     * Shows the account menu.
     * @return true if user wants to continue, false if account was deleted
     */
    public boolean show() {
        while (true) {
            System.out.println("\n=== Account Menu ===");
            System.out.println("1. Delete Account");
            System.out.println("2. Continue to Company Setup");
            System.out.print("Choose option (1-2): ");

            int choice = input.readInt();
            switch (choice) {
                case 1 -> {
                    if (deleteAccount()) {
                        return false; // Account was deleted
                    }
                }
                case 2 -> {
                    return true; // Continue to company setup
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private boolean deleteAccount() {
        System.out.println("Deleting this account will remove all associated data and company associations.");
        if (input.confirm("Are you sure you want to delete your account? (yes/no)")) {
            services.getUserService().deleteUser(context.getCurrentUserId());
            System.out.println("Account deleted.");
            context.clearSession();
            return true;
        }
        return false;
    }
}


