package org.example.cli.menu;

import org.example.cli.CliContext;
import org.example.cli.DisplayFormatter;
import org.example.cli.InputHelper;
import org.example.cli.ServiceContainer;
import org.example.entity.client.ClientDTO;
import org.example.entity.client.CreateClientDTO;
import org.example.entity.client.UpdateClientDTO;
import org.example.exception.EntityNotFoundException;
import org.example.exception.ValidationException;

import java.util.List;


public class ClientMenu {
    private final CliContext context;
    private final InputHelper input;
    private final ServiceContainer services;

    public ClientMenu(CliContext context, InputHelper input, ServiceContainer services) {
        this.context = context;
        this.input = input;
        this.services = services;
    }

    public void show() {
        while (true) {
            System.out.println("\n--- Client Management ---");
            System.out.println("1. List Clients");
            System.out.println("2. Create Client");
            System.out.println("3. Update Client");
            System.out.println("4. Delete Client");
            System.out.println("5. Back to Main Menu");
            System.out.print("Choose option (1-5): ");

            int choice = input.readInt();

            switch (choice) {
                case 1 -> listClients();
                case 2 -> createClient();
                case 3 -> updateClient();
                case 4 -> deleteClient();
                case 5 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void listClients() {
        if (!context.hasCompanySelected()) {
            System.out.println("✗ No company selected.");
            return;
        }
        try {
            List<ClientDTO> clients = services.getClientService()
                .getClientsByCompany(context.getCurrentCompanyId());

            if (clients.isEmpty()) {
                System.out.println("No clients found.");
                return;
            }
            DisplayFormatter.printClientList(clients);
        } catch (EntityNotFoundException e) {
            System.out.println("✗ Failed to list clients: " + e.getMessage());
        }
    }

    private void createClient() {
        if (!context.hasCompanySelected()) {
            System.out.println("✗ No company selected.");
            return;
        }
        System.out.println("\n--- Create Client ---");

        String firstName = input.readLine("First Name: ");
        String lastName = input.readLine("Last Name: ");
        String email = input.readLine("Email: ");
        String address = input.readLine("Address: ");
        String city = input.readLine("City: ");
        String country = input.readLine("Country: ");
        String phoneNumber = input.readLine("Phone Number: ");

        try {
            CreateClientDTO dto = new CreateClientDTO(
                context.getCurrentCompanyId(),
                firstName, lastName, email, address, country, city, phoneNumber
            );

            ClientDTO client = services.getClientService().createClient(dto);

            System.out.println("✓ Client created successfully!");
            System.out.println("  Name: " + client.firstName() + " " + client.lastName());

        } catch (EntityNotFoundException e) {
            System.out.println("✗ Client creation failed: " + e.getMessage());
        } catch (ValidationException e) {
            System.out.println("✗ Input Error: " + e.getMessage());
        }
    }

    private void updateClient() {
        if (!context.hasCompanySelected()) {
            System.out.println("✗ No company selected.");
            return;
        }
        ClientDTO client = selectClient();
        if (client == null) return;

        try {
            System.out.println("Leave blank to keep current value.");

            String firstName = input.readLine("First Name [" + nullSafe(client.firstName()) + "]: ");
            String lastName = input.readLine("Last Name [" + nullSafe(client.lastName()) + "]: ");
            String email = input.readLine("Email [" + nullSafe(client.email()) + "]: ");
            String address = input.readLine("Address [" + nullSafe(client.address()) + "]: ");
            String city = input.readLine("City [" + nullSafe(client.city()) + "]: ");
            String country = input.readLine("Country [" + nullSafe(client.country()) + "]: ");
            String phoneNumber = input.readLine("Phone Number [" + nullSafe(client.phoneNumber()) + "]: ");

            UpdateClientDTO updateDto = new UpdateClientDTO(
                client.id(),
                firstName.isEmpty() ? null : firstName,
                lastName.isEmpty() ? null : lastName,
                email.isEmpty() ? null : email,
                address.isEmpty() ? null : address,
                country.isEmpty() ? null : country,
                city.isEmpty() ? null : city,
                phoneNumber.isEmpty() ? null : phoneNumber
            );

            ClientDTO updated = services.getClientService().updateClient(updateDto);

            System.out.println("✓ Client updated successfully!");
            System.out.println("  Name: " + updated.firstName() + " " + updated.lastName());
        } catch (EntityNotFoundException e) {
            System.out.println("✗ Client update failed: " + e.getMessage());
        } catch (ValidationException e) {
            System.out.println("✗ Input Error: " + e.getMessage());
        }
    }

    private void deleteClient() {
        if (!context.hasCompanySelected()) {
            System.out.println("✗ No company selected.");
            return;
        }
        ClientDTO client = selectClient();
        if (client == null) return;

        if (input.confirmInline("Are you sure you want to delete " +
                client.firstName() + " " + client.lastName() + "? (yes/no): ")) {
            try {
                services.getClientService().deleteClient(client.id());
                System.out.println("✓ Client deleted successfully!");
            } catch (EntityNotFoundException e) {
                System.out.println("✗ Client deletion failed: " + e.getMessage());
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    /**
     * Displays a list of clients and allows the user to select one.
     * @return the selected ClientDTO, or null if cancelled/no clients
     */
    public ClientDTO selectClient() {
        if (!context.hasCompanySelected()) {
            System.out.println("✗ No company selected.");
            return null;
        }
        try {
            List<ClientDTO> clients = services.getClientService()
                .getClientsByCompany(context.getCurrentCompanyId());

            if (clients.isEmpty()) {
                System.out.println("No clients found for this company.");
                return null;
            }

            DisplayFormatter.printClientSelectionList(clients);

            System.out.print("Select client (1-" + clients.size() + "): ");
            int index = input.readInt() - 1;

            if (index < 0 || index >= clients.size()) {
                System.out.println("✗ Invalid selection");
                return null;
            }

            return clients.get(index);
        } catch (EntityNotFoundException e) {
            System.out.println("✗ Failed to list clients: " + e.getMessage());
            return null;
        }
    }

    private String nullSafe(String value) {
        return value != null ? value : "";
    }
}


