package org.example.cli.menu;

import org.example.cli.CliContext;
import org.example.cli.DisplayFormatter;
import org.example.cli.InputHelper;
import org.example.cli.ServiceContainer;
import org.example.entity.company.CompanyDTO;
import org.example.entity.company.UpdateCompanyDTO;

/**
 * Handles company settings - view and update company details.
 */
public class CompanySettingsMenu {
    private final CliContext context;
    private final InputHelper input;
    private final ServiceContainer services;

    public CompanySettingsMenu(CliContext context, InputHelper input, ServiceContainer services) {
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
            CompanyDTO company = context.getCurrentCompany();
            System.out.println("\n--- Company Settings ---");
            System.out.println("Company: " + company.name());
            System.out.println("Org Num: " + company.orgNum());
            System.out.println("Email: " + company.email());
            System.out.println("1. Update Company");
            System.out.println("2. View Company Details");
            System.out.println("3. Back to Main Menu");
            System.out.print("Choose option (1-3): ");

            int choice = input.readInt();

            switch (choice) {
                case 1 -> updateCompany();
                case 2 -> viewCompanyDetails();
                case 3 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void updateCompany() {
        if (!context.hasCompanySelected()) {
            System.out.println("✗ No company selected.");
            return;
        }
        CompanyDTO company = context.getCurrentCompany();

        System.out.println("\n--- Update Company ---");
        System.out.println("Leave blank to keep current value.");

        String name = input.readLine("Name [" + company.name() + "]: ");
        String email = input.readLine("Email [" + company.email() + "]: ");
        String address = input.readLine("Address [" + company.address() + "]: ");
        String city = input.readLine("City [" + company.city() + "]: ");
        String country = input.readLine("Country [" + company.country() + "]: ");
        String phoneNumber = input.readLine("Phone Number [" + company.phoneNumber() + "]: ");

        try {
            UpdateCompanyDTO updateDto = new UpdateCompanyDTO(
                context.getCurrentCompanyId(),
                email.isEmpty() ? null : email,
                phoneNumber.isEmpty() ? null : phoneNumber,
                name.isEmpty() ? null : name,
                address.isEmpty() ? null : address,
                city.isEmpty() ? null : city,
                country.isEmpty() ? null : country
            );

            CompanyDTO updated = services.getCompanyService().update(updateDto);
            context.setCurrentCompany(updated);

            System.out.println("✓ Company updated successfully!");
        } catch (Exception e) {
            System.out.println("✗ Company update failed: " + e.getMessage());
        }
    }

    private void viewCompanyDetails() {
        if (!context.hasCompanySelected()) {
            System.out.println("✗ No company selected.");
            return;
        }
        DisplayFormatter.printCompanyDetails(context.getCurrentCompany());
    }
}


