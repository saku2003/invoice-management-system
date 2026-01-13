package org.example.cli;

import org.example.cli.menu.AccountMenu;
import org.example.cli.menu.AuthMenu;
import org.example.cli.menu.CompanySetupMenu;
import org.example.cli.menu.MainMenu;

import java.util.Scanner;


public class CliApp {
    private final CliContext context;
    private final InputHelper input;
    private final ServiceContainer services;

    // Menus
    private final AuthMenu authMenu;
    private final AccountMenu accountMenu;
    private final CompanySetupMenu companySetupMenu;
    private final MainMenu mainMenu;

    public CliApp() {
        this.context = new CliContext();
        this.input = new InputHelper(new Scanner(System.in));
        this.services = new ServiceContainer();

        // Initialize menus
        this.authMenu = new AuthMenu(context, input, services);
        this.accountMenu = new AccountMenu(context, input, services);
        this.companySetupMenu = new CompanySetupMenu(context, input, services, accountMenu);
        this.mainMenu = new MainMenu(context, input, services, companySetupMenu);
    }

    public void run() {
        printBanner();

        // Step 1: Authentication (with retry)
        while (!authMenu.show()) {
            System.out.println("\nWould you like to try again? (yes/no): ");
            String retry = input.readLine().toLowerCase();
            if (!"yes".equals(retry)) {
                System.out.println("Exiting...");
                return;
            }
        }

        // Step 2: Account setting
        if (!accountMenu.show()) {
            System.out.println("Account deleted. Returning to authentication...");
            run();
            return;
        }

        // Step 3: Company setup (with retry)
        while (!companySetupMenu.show()) {
            System.out.println("\nWould you like to try again? (yes/no): ");
            String retry = input.readLine().toLowerCase();
            if (!"yes".equals(retry)) {
                System.out.println("Exiting...");
                return;
            }
        }

        // Step 4: Main menu loop
        mainMenu.show();

        input.close();
    }

    private void printBanner() {
        System.out.println("""
            ██╗███╗   ██╗██╗   ██╗ ██████╗ ██╗ ██████╗██╗███╗   ██╗ ██████╗      █████╗ ██████╗ ██████╗
            ██║████╗  ██║██║   ██║██╔═══██╗██║██╔════╝██║████╗  ██║██╔════╝     ██╔══██╗██╔══██╗██╔══██╗
            ██║██╔██╗ ██║██║   ██║██║   ██║██║██║     ██║██╔██╗ ██║██║  ███╗    ███████║██████╔╝██████╔╝
            ██║██║╚██╗██║╚██╗ ██╔╝██║   ██║██║██║     ██║██║╚██╗██║██║   ██║    ██╔══██║██╔═══╝ ██╔═══╝
            ██║██║ ╚████║ ╚████╔╝ ╚██████╔╝██║╚██████╗██║██║ ╚████║╚██████╔╝    ██║  ██║██║     ██║
            ╚═╝╚═╝  ╚═══╝  ╚═══╝   ╚═════╝ ╚═╝ ╚═════╝╚═╝╚═╝  ╚═══╝ ╚═════╝     ╚═╝  ╚═╝╚═╝     ╚═╝
            """);
    }
}


