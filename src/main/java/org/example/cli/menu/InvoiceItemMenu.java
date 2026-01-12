package org.example.cli.menu;

import org.example.cli.CliContext;
import org.example.cli.InputHelper;
import org.example.cli.ServiceContainer;
import org.example.entity.invoice.InvoiceDTO;
import org.example.entity.invoice.InvoiceItemDTO;
import org.example.entity.invoice.UpdateInvoiceDTO;
import org.example.exception.BusinessRuleException;
import org.example.exception.EntityNotFoundException;
import org.example.exception.ValidationException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Handles invoice item operations - add, update, remove items from invoices.
 */
public class InvoiceItemMenu {
    private final CliContext context;
    private final InputHelper input;
    private final ServiceContainer services;
    private final Supplier<InvoiceDTO> invoiceSelector;

    public InvoiceItemMenu(CliContext context, InputHelper input, ServiceContainer services,
                           Supplier<InvoiceDTO> invoiceSelector) {
        this.context = context;
        this.input = input;
        this.services = services;
        this.invoiceSelector = invoiceSelector;
    }

    public void show() {
        while (true) {
            System.out.println("\n--- Invoice Items ---");
            System.out.println("1. List Invoice Items");
            System.out.println("2. Add Invoice Item");
            System.out.println("3. Update Invoice Item");
            System.out.println("4. Remove Invoice Item");
            System.out.println("5. Back to Invoice Menu");
            System.out.print("Choose option (1-5): ");

            int choice = input.readInt();

            switch (choice) {
                case 1 -> listInvoiceItems();
                case 2 -> addInvoiceItem();
                case 3 -> updateInvoiceItem();
                case 4 -> removeInvoiceItem();
                case 5 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void listInvoiceItems() {
        InvoiceDTO invoice = invoiceSelector.get();
        if (invoice == null) return;

        if (invoice.items().isEmpty()) {
            System.out.println("No items for this invoice.");
            return;
        }

        System.out.println("\nInvoice Items:");
        for (int i = 0; i < invoice.items().size(); i++) {
            InvoiceItemDTO item = invoice.items().get(i);
            System.out.println((i + 1) + ". " + item.name());
            System.out.println("   Quantity: " + item.quantity());
            System.out.println("   Unit Price: " + item.unitPrice());
            System.out.println("   ---");
        }
    }

    private void addInvoiceItem() {
        InvoiceDTO invoice = invoiceSelector.get();
        if (invoice == null) return;

        try {
            String name = input.readLine("Item name: ");

            System.out.print("Quantity: ");
            int quantity = input.readInt();

            System.out.print("Unit price: ");
            BigDecimal unitPrice = new BigDecimal(input.readLine());

            List<InvoiceItemDTO> updated = new ArrayList<>(invoice.items());
            updated.add(InvoiceItemDTO.builder()
                .name(name)
                .quantity(quantity)
                .unitPrice(unitPrice)
                .build()
            );

            services.getInvoiceService().updateInvoice(
                new UpdateInvoiceDTO(invoice.id(), null, null, updated, null)
            );
            System.out.println("✓ Invoice item added");
        } catch (NumberFormatException e) {
            System.out.println("✗ Invalid price format.");
        } catch (EntityNotFoundException e) {
            System.out.println("✗ Failed to add item: " + e.getMessage());
        }
    }

    private void updateInvoiceItem() {
        InvoiceDTO invoice = invoiceSelector.get();
        if (invoice == null) return;

        List<InvoiceItemDTO> items = invoice.items();
        if (items.isEmpty()) {
            System.out.println("No items to update.");
            return;
        }

        for (int i = 0; i < items.size(); i++) {
            InvoiceItemDTO it = items.get(i);
            System.out.println((i + 1) + ". " + it.name() + " | Qty: " + it.quantity() + " | Price: " + it.unitPrice());
        }

        System.out.print("Select item to update: ");
        int index = input.readInt() - 1;
        if (index < 0 || index >= items.size()) {
            System.out.println("Invalid selection");
            return;
        }

        InvoiceItemDTO item = items.get(index);

        try {
            System.out.println("Leave blank to keep current value.");

            String nameInput = input.readLine("Item name [" + item.name() + "]: ");
            String name = nameInput.isEmpty() ? item.name() : nameInput;

            System.out.print("Quantity [" + item.quantity() + "]: ");
            String qtyInput = input.readLine();
            int quantity = qtyInput.isEmpty() ? item.quantity() : Integer.parseInt(qtyInput);

            System.out.print("Unit Price [" + item.unitPrice() + "]: ");
            String priceInput = input.readLine();
            BigDecimal unitPrice = priceInput.isEmpty() ? item.unitPrice() : new BigDecimal(priceInput);

            List<InvoiceItemDTO> updated = items.stream()
                .map(i -> i.id().equals(item.id())
                    ? InvoiceItemDTO.builder().id(i.id()).name(name).quantity(quantity).unitPrice(unitPrice).build()
                    : i)
                .toList();

            services.getInvoiceService().updateInvoice(
                new UpdateInvoiceDTO(invoice.id(), null, null, updated, null)
            );
            System.out.println("✓ Invoice item updated");

        } catch (NumberFormatException e) {
            System.out.println("✗ Invalid format. Please use numbers.");
        } catch (EntityNotFoundException e) {
            System.out.println("✗ Update failed: " + e.getMessage());
        } catch (ValidationException e) {
            System.out.println("✗ Validation error: " + e.getMessage());
        }
    }

    private void removeInvoiceItem() {
        InvoiceDTO invoice = invoiceSelector.get();
        if (invoice == null) return;

        List<InvoiceItemDTO> items = invoice.items();
        if (items.isEmpty()) {
            System.out.println("No items to remove.");
            return;
        }

        for (int i = 0; i < items.size(); i++) {
            InvoiceItemDTO it = items.get(i);
            System.out.println((i + 1) + ". " + it.name() + " | Qty: " + it.quantity() + " | Price: " + it.unitPrice());
        }

        System.out.print("Select item to remove: ");
        int index = input.readInt() - 1;
        if (index < 0 || index >= items.size()) {
            System.out.println("Invalid selection");
            return;
        }

        InvoiceItemDTO item = items.get(index);

        try {
            List<InvoiceItemDTO> updated = items.stream()
                .filter(i -> !i.id().equals(item.id()))
                .toList();

            services.getInvoiceService().updateInvoice(
                new UpdateInvoiceDTO(invoice.id(), null, null, updated, null)
            );
            System.out.println("✓ Invoice item removed");

        } catch (EntityNotFoundException e) {
            System.out.println("✗ Removal failed: " + e.getMessage());
        } catch (BusinessRuleException e) {
            System.out.println("✗ Rule violation: " + e.getMessage());
        }
    }

    /**
     * Reads invoice items from user input during invoice creation.
     * @return list of InvoiceItemDTOs entered by the user
     */
    public List<InvoiceItemDTO> readInvoiceItems() {
        List<InvoiceItemDTO> items = new ArrayList<>();

        while (true) {
            System.out.print("Add item? (y/n): ");
            String choice = input.readLine();

            if (!choice.equalsIgnoreCase("y")) break;

            String name = input.readLine("Item name: ");

            System.out.print("Quantity: ");
            int quantity = input.readInt();

            System.out.print("Unit price: ");
            BigDecimal unitPrice = new BigDecimal(input.readLine());

            items.add(InvoiceItemDTO.builder()
                .name(name)
                .quantity(quantity)
                .unitPrice(unitPrice)
                .build()
            );
        }

        return items;
    }
}


