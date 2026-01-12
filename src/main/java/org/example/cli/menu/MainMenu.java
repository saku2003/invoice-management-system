package org.example.cli.menu;

import org.example.cli.CliContext;
import org.example.cli.InputHelper;
import org.example.cli.ServiceContainer;

/**
 * Main navigation menu - hub for all major features.
 */
public class MainMenu {
    private final CliContext context;
    private final InputHelper input;
    private final ServiceContainer services;

    private final ClientMenu clientMenu;
    private final InvoiceMenu invoiceMenu;
    private final CompanyUserMenu companyUserMenu;
    private final CompanySettingsMenu companySettingsMenu;
    private final CompanySetupMenu companySetupMenu;

    public MainMenu(CliContext context, InputHelper input, ServiceContainer services,
                    CompanySetupMenu companySetupMenu) {
        this.context = context;
        this.input = input;
        this.services = services;
        this.companySetupMenu = companySetupMenu;

        // Initialize sub-menus
        this.clientMenu = new ClientMenu(context, input, services);
        this.invoiceMenu = new InvoiceMenu(context, input, services, clientMenu);
        this.companyUserMenu = new CompanyUserMenu(context, input, services);
        this.companySettingsMenu = new CompanySettingsMenu(context, input, services);
    }

    /**
     * Shows the main menu loop.
     * @return true if user logged out, false if setup failed
     */
    public boolean show() {
        while (true) {
            // Safety check: ensure company is selected
            if (!context.hasCompanySelected()) {
                System.out.println("\n✗ No company selected. Please select or create a company first.");
                if (!companySetupMenu.show()) {
                    System.out.println("Exiting...");
                    return false;
                }
                continue;
            }

            System.out.println("\n=== Main Menu ===");
            System.out.println("Current Company: " + context.getCurrentCompany().name() +
                " (" + context.getCurrentCompany().orgNum() + ")");
            System.out.println("1. Client Management");
            System.out.println("2. Invoice Management");
            System.out.println("3. Company Users");
            System.out.println("4. Company Settings");
            System.out.println("5. Switch Company");
            System.out.println("6. Logout");
            System.out.print("Choose option (1-6): ");

            int choice = input.readInt();

            switch (choice) {
                case 1 -> clientMenu.show();
                case 2 -> invoiceMenu.show();
                case 3 -> companyUserMenu.show();
                case 4 -> companySettingsMenu.show();
                case 5 -> {
                    if (!companySetupMenu.show()) {
                        return false; // User deleted account or setup failed
                    }
                }
                case 6 -> {
                    System.out.println("Logging out...");
                    return true;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }
}


