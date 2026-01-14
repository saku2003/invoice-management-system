package org.example.cli.menu;

import org.example.cli.CliContext;
import org.example.cli.DisplayFormatter;
import org.example.cli.InputHelper;
import org.example.cli.ServiceContainer;


public class AccountMenu {
    private final CliContext context;
    private final InputHelper input;
    private final ServiceContainer services;

    public AccountMenu(CliContext context, InputHelper input, ServiceContainer services) {
        this.context = context;
        this.input = input;
        this.services = services;
    }

    public boolean show() {
        while (true) {
            DisplayFormatter.printWelcome(context.getCurrentUser());

            System.out.println("1. Continue to Company Setup");
            System.out.println("2. Delete Account");
            System.out.print("Choose option (1-2): ");

            int choice = input.readInt();
            switch (choice) {
                case 1 -> {
                    return true;
                }
                case 2 -> {
                    if (deleteAccount()) {
                        return false;
                    }
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private boolean deleteAccount() {
        System.out.println("\n⚠️  WARNING: This action cannot be undone!");
        System.out.println("Deleting this account will remove:");
        System.out.println("  • Your user profile");
        System.out.println("  • All company associations");
        System.out.println("  • Related data\n");

        String confirmation = input.readLine("Type 'DELETE' to confirm: ");
        if ("DELETE".equals(confirmation)) {
            services.getUserService().deleteUser(context.getCurrentUserId());
            System.out.println("\n✓ Account deleted successfully.");
            context.clearSession();
            return true;
        }
        System.out.println("Account deletion cancelled.");
        return false;
    }
}


