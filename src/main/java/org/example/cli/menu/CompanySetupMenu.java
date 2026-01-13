package org.example.cli.menu;

import org.example.cli.CliContext;
import org.example.cli.InputHelper;
import org.example.cli.ServiceContainer;
import org.example.entity.company.Company;
import org.example.entity.company.CompanyDTO;
import org.example.entity.company.CompanyUser;
import org.example.entity.company.CreateCompanyDTO;
import org.example.exception.BusinessRuleException;
import org.example.exception.EntityNotFoundException;
import org.example.exception.ValidationException;

import java.util.List;


public class CompanySetupMenu {
    private final CliContext context;
    private final InputHelper input;
    private final ServiceContainer services;
    private final AccountMenu accountMenu;

    public CompanySetupMenu(CliContext context, InputHelper input, ServiceContainer services, AccountMenu accountMenu) {
        this.context = context;
        this.input = input;
        this.services = services;
        this.accountMenu = accountMenu;
    }

    public boolean show() {
        while (true) {
            System.out.println("\n--- Company Setup ---");
            System.out.println("1. Create new company");
            System.out.println("2. Select existing company");
            System.out.println("3. Go back to Account settings");
            System.out.print("Choose option (1-3): ");

            int choice = input.readInt();

            switch (choice) {
                case 1 -> {
                    if (createCompany()) {
                        return true;
                    }
                }
                case 2 -> {
                    if (selectCompany()) {
                        return true;
                    }
                }
                case 3 -> {
                    if (!accountMenu.show()) {
                        // User deleted account
                        return false;
                    }
                    // User chose "Continue to Company Setup" - loop again
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private boolean createCompany() {
        System.out.println("\n--- Create Company ---");

        String orgNum = input.readLine("Organization Number: ");
        String email = input.readLine("Email: ");
        String phoneNumber = input.readLine("Phone Number: ");
        String name = input.readLine("Company Name: ");
        String address = input.readLine("Address: ");
        String city = input.readLine("City: ");
        String country = input.readLine("Country: ");

        try {
            CreateCompanyDTO createDto = new CreateCompanyDTO(
                orgNum, email, phoneNumber, name, address, city, country
            );

            CompanyDTO company = services.getCompanyService().create(context.getCurrentUserId(), createDto);
            context.setCurrentCompany(company);

            System.out.println("✓ Company created successfully!");
            System.out.println("  Company: " + company.name() + " (" + company.orgNum() + ")");
            System.out.println("  You have been automatically associated with this company.");
            return true;

        } catch (ValidationException e) {
            System.out.println("✗ Invalid input: " + e.getMessage());
            return false;

        } catch (BusinessRuleException e) {
            System.out.println("✗ " + e.getMessage());
            return false;

        } catch (EntityNotFoundException e) {
            System.out.println("✗ Creator user not found.");
            return false;
        }
    }

    private boolean selectCompany() {
        try {
            List<CompanyUser> userCompanies = services.getCompanyUserService()
                .getUserCompanies(context.getCurrentUserId());

            if (userCompanies.isEmpty()) {
                System.out.println("✗ You are not associated with any companies.");
                System.out.println("Please create a new company first.");
                return false;
            }

            while (true) {
                System.out.println("\n--- Your Companies ---");
                for (int i = 0; i < userCompanies.size(); i++) {
                    CompanyUser cu = userCompanies.get(i);
                    Company company = cu.getCompany();
                    System.out.println((i + 1) + ". " + company.getName() + " (" + company.getOrgNum() + ")");
                }

                System.out.print("\nSelect company (1-" + userCompanies.size() + ") or 'cancel' to go back: ");
                String inputStr = input.readLine();

                if (inputStr.equalsIgnoreCase("cancel")) {
                    return false;
                }

                int choice;
                try {
                    choice = Integer.parseInt(inputStr);
                } catch (NumberFormatException e) {
                    System.out.println("✗ Invalid input: please enter a number.");
                    continue;
                }

                if (choice < 1 || choice > userCompanies.size()) {
                    System.out.println("✗ Invalid selection: number out of range.");
                    continue;
                }

                Company selectedCompany = userCompanies.get(choice - 1).getCompany();
                context.setCurrentCompany(CompanyDTO.fromEntity(selectedCompany));

                System.out.println("✓ Company selected: " +
                    context.getCurrentCompany().name() + " (" + context.getCurrentCompany().orgNum() + ")");
                return true;
            }

        } catch (ValidationException e) {
            System.out.println("✗ Invalid request: " + e.getMessage());
            return false;
        } catch (EntityNotFoundException e) {
            System.out.println("✗ Company not found.");
            return false;
        }
    }
}


