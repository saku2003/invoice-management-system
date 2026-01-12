package org.example.cli.menu;

import org.example.cli.CliContext;
import org.example.cli.DisplayFormatter;
import org.example.cli.InputHelper;
import org.example.cli.ServiceContainer;
import org.example.entity.company.CompanyUser;
import org.example.exception.BusinessRuleException;
import org.example.exception.EntityNotFoundException;
import org.example.exception.ValidationException;

import java.util.List;
import java.util.UUID;

/**
 * Handles company user management - add/remove users from the current company.
 */
public class CompanyUserMenu {
    private final CliContext context;
    private final InputHelper input;
    private final ServiceContainer services;

    public CompanyUserMenu(CliContext context, InputHelper input, ServiceContainer services) {
        this.context = context;
        this.input = input;
        this.services = services;
    }

    public void show() {
        if (!context.hasCompanySelected()) {
            System.out.println("✗ No company selected.");
            return;
        }
        while (true) {
            System.out.println("\n--- Company Users ---");
            System.out.println("1. List Company Users");
            System.out.println("2. Add User to Company");
            System.out.println("3. Remove User from Company");
            System.out.println("4. Back to Main Menu");
            System.out.print("Choose option (1-4): ");

            int choice = input.readInt();

            switch (choice) {
                case 1 -> listCompanyUsers();
                case 2 -> addUserToCompany();
                case 3 -> removeUserFromCompany();
                case 4 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void listCompanyUsers() {
        try {
            List<CompanyUser> companyUsers = services.getCompanyUserService()
                .getCompanyUsers(context.getCurrentCompanyId());

            if (companyUsers.isEmpty()) {
                System.out.println("No users associated with this company.");
                return;
            }
            DisplayFormatter.printCompanyUserList(companyUsers);
        } catch (ValidationException e) {
            System.out.println("✗ Invalid request: " + e.getMessage());
        } catch (EntityNotFoundException e) {
            System.out.println("✗ Company not found.");
        }
    }

    private void addUserToCompany() {
        String email = input.readLine("\nEnter user email to invite: ");

        try {
            services.getCompanyUserService().addUserToCompanyByEmail(context.getCurrentCompanyId(), email);
            System.out.println("✓ User added to company successfully!");
        } catch (ValidationException e) {
            System.out.println("✗ Invalid input: " + e.getMessage());
        } catch (EntityNotFoundException e) {
            System.out.println("✗ Company or user not found: " + e.getMessage());
        } catch (BusinessRuleException e) {
            System.out.println("✗ Business rule violation: " + e.getMessage());
        }
    }

    private void removeUserFromCompany() {
        try {
            List<CompanyUser> companyUsers = services.getCompanyUserService()
                .getCompanyUsers(context.getCurrentCompanyId());

            if (companyUsers.isEmpty()) {
                System.out.println("No users to remove.");
                return;
            }

            DisplayFormatter.printCompanyUserSelectionList(companyUsers);

            System.out.print("Select user to remove (1-" + companyUsers.size() + "): ");
            int choice = input.readInt();

            if (choice < 1 || choice > companyUsers.size()) {
                System.out.println("✗ Invalid selection.");
                return;
            }

            UUID userId = companyUsers.get(choice - 1).getUser().getId();
            if (userId.equals(context.getCurrentUserId())) {
                System.out.println("✗ Cannot remove yourself from the current company.");
                System.out.println("  Switch to another company first, or have another user remove you.");
                return;
            }

            services.getCompanyUserService().deleteUserFromCompany(context.getCurrentCompanyId(), userId);
            System.out.println("✓ User removed from company successfully!");

        } catch (ValidationException e) {
            System.out.println("✗ Invalid request: " + e.getMessage());
        } catch (EntityNotFoundException e) {
            System.out.println("✗ Company or user not found: " + e.getMessage());
        } catch (BusinessRuleException e) {
            System.out.println("✗ Business rule violation: " + e.getMessage());
        }
    }
}


