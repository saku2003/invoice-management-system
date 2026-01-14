package org.example.cli.menu;

import org.example.cli.CliContext;
import org.example.cli.DisplayFormatter;
import org.example.cli.InputHelper;
import org.example.cli.ServiceContainer;
import org.example.entity.client.ClientDTO;
import org.example.entity.invoice.CreateInvoiceDTO;
import org.example.entity.invoice.InvoiceDTO;
import org.example.entity.invoice.InvoiceItemDTO;
import org.example.entity.invoice.InvoiceStatus;
import org.example.exception.BusinessRuleException;
import org.example.exception.EntityNotFoundException;
import org.example.exception.ValidationException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public class InvoiceMenu {
    private final CliContext context;
    private final InputHelper input;
    private final ServiceContainer services;
    private final ClientMenu clientMenu;
    private final InvoiceItemMenu invoiceItemMenu;

    public InvoiceMenu(CliContext context, InputHelper input, ServiceContainer services, ClientMenu clientMenu) {
        this.context = context;
        this.input = input;
        this.services = services;
        this.clientMenu = clientMenu;
        this.invoiceItemMenu = new InvoiceItemMenu(context, input, services, this::selectInvoice);
    }

    public void show() {
        if (!context.hasCompanySelected()) {
            System.out.println("✗ No company selected.");
            return;
        }
        while (true) {
            System.out.println("\n--- Invoice Management ---");
            System.out.println("1. List Invoices");
            System.out.println("2. Create Invoice");
            System.out.println("3. Update Invoice Status");
            System.out.println("4. Invoice Items");
            System.out.println("5. Delete Invoice");
            System.out.println("6. Back to Main Menu");
            System.out.print("Choose option (1-6): ");

            int choice = input.readInt();

            switch (choice) {
                case 1 -> listInvoices();
                case 2 -> createInvoice();
                case 3 -> updateInvoiceStatus();
                case 4 -> invoiceItemMenu.show();
                case 5 -> deleteInvoice();
                case 6 -> {
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void listInvoices() {
        if (!context.hasCompanySelected()) {
            System.out.println("✗ No company selected.");
            return;
        }
        try {
            List<InvoiceDTO> invoices = services.getInvoiceService()
                .getInvoicesByCompany(context.getCurrentCompanyId());

            if (invoices.isEmpty()) {
                System.out.println("There are currently no invoices under this company.");
                return;
            }
            DisplayFormatter.printInvoiceList(invoices);
        } catch (EntityNotFoundException e) {
            System.out.println("✗ Failed to list invoices: " + e.getMessage());
        }
    }

    private void createInvoice() {
        if (!context.hasCompanySelected()) {
            System.out.println("✗ No company selected.");
            return;
        }
        try {
            System.out.println("\n--- Create Invoice ---");

            List<ClientDTO> clients = services.getClientService()
                .getClientsByCompany(context.getCurrentCompanyId());

            if (clients.isEmpty()) {
                System.out.println("No clients found for this company, please create a client first");
                return;
            }

            for (int i = 0; i < clients.size(); i++) {
                ClientDTO client = clients.get(i);
                System.out.println((i + 1) + ". " + client.firstName() + " " + client.lastName());
                System.out.println("   Email: " + client.email());
                System.out.println("   City: " + client.city());
                System.out.println("   ---");
            }

            System.out.print("Select client number: ");
            int clientIndex = input.readInt() - 1;

            if (clientIndex < 0 || clientIndex >= clients.size()) {
                System.out.println("Invalid client selection");
                return;
            }

            ClientDTO selectedClient = clients.get(clientIndex);

            String invoiceNumber = input.readLine("Invoice Number: ");
            String dueDateStr = input.readLine("Due date (yyyy-MM-dd): ");

            LocalDate dueDate = LocalDate.parse(dueDateStr);
            LocalDateTime dueDateTime = dueDate.atTime(23, 59);

            String vatRateStr = input.readLine("VAT Rate (e.g., 0.25 for 25%, press Enter for no VAT): ");
            BigDecimal vatRate = null;
            if (!vatRateStr.isEmpty()) {
                vatRate = new BigDecimal(vatRateStr);
            }

            List<InvoiceItemDTO> items = invoiceItemMenu.readInvoiceItems();

            if (items.isEmpty()) {
                System.out.println("Invoice must have at least one item");
                return;
            }

            CreateInvoiceDTO dto = new CreateInvoiceDTO(
                context.getCurrentCompanyId(),
                selectedClient.id(),
                invoiceNumber,
                dueDateTime,
                vatRate,
                items
            );

            services.getInvoiceService().createInvoice(dto);

            System.out.println("✓ Invoice created");
        } catch (java.time.format.DateTimeParseException e) {
            System.out.println("✗ Invalid date format. Please use yyyy-MM-dd.");
        } catch (BusinessRuleException e) {
            System.out.println("✗ Business Rule Violation: " + e.getMessage());
        } catch (EntityNotFoundException e) {
            System.out.println("✗ Creation failed: " + e.getMessage());
        } catch (ValidationException e) {
            System.out.println("✗ Validation error: " + e.getMessage());
        }
    }

    private void updateInvoiceStatus() {
        InvoiceDTO invoice = selectInvoice();
        if (invoice == null) return;

        System.out.println("Available statuses: CREATED, SENT, PAID, OVERDUE, CANCELLED");
        String statusStr = input.readLine("Enter new status: ").toUpperCase();

        try {
            InvoiceStatus status = InvoiceStatus.valueOf(statusStr);
            services.getInvoiceService().updateStatus(invoice.id(), status);
            System.out.println("✓ Invoice status updated successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("✗ Invalid status name. Please try again.");
        } catch (EntityNotFoundException e) {
            System.out.println("✗ Status update failed: " + e.getMessage());
        } catch (BusinessRuleException e) {
            System.out.println("✗ " + e.getMessage());
        }
    }

    private void deleteInvoice() {
        InvoiceDTO invoice = selectInvoice();
        if (invoice == null) return;

        if (input.confirmInline("Are you sure you want to delete this invoice? (yes/no): ")) {
            try {
                services.getInvoiceService().deleteById(invoice.id());
                System.out.println("✓ Invoice deleted successfully!");
            } catch (EntityNotFoundException e) {
                System.out.println("✗ Deletion failed: " + e.getMessage());
            } catch (BusinessRuleException e) {
                System.out.println("✗ Cannot delete: " + e.getMessage());
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    public InvoiceDTO selectInvoice() {
        if (!context.hasCompanySelected()) {
            System.out.println("✗ No company selected.");
            return null;
        }
        List<InvoiceDTO> invoices = services.getInvoiceService()
            .getInvoicesByCompany(context.getCurrentCompanyId());

        if (invoices.isEmpty()) {
            System.out.println("No invoices found for this company.");
            return null;
        }

        DisplayFormatter.printInvoiceSelectionList(invoices);

        System.out.print("Select invoice (1-" + invoices.size() + "): ");
        int index = input.readInt() - 1;

        if (index < 0 || index >= invoices.size()) {
            System.out.println("Invalid selection");
            return null;
        }

        return invoices.get(index);
    }
}


