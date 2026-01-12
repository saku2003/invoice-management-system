package org.example.cli;

import org.example.entity.client.ClientDTO;
import org.example.entity.company.CompanyDTO;
import org.example.entity.company.CompanyUser;
import org.example.entity.invoice.InvoiceDTO;
import org.example.entity.invoice.InvoiceItemDTO;
import org.example.entity.user.UserDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Utility class for formatting CLI output displays.
 * Provides consistent, professional formatting for entities.
 */
public class DisplayFormatter {
    
    private static final String DIVIDER = "────────────────────────────────────────";
    private static final String DOUBLE_DIVIDER = "════════════════════════════════════════";
    private static final String DOT_DIVIDER = "  · · · · · · · · · · · · · · · · · · · ·";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // ═══════════════════════════════════════════════════════════════════════════
    // MENU HEADERS
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Prints a consistent menu header.
     */
    public static void printMenuHeader(String title) {
        System.out.println("\n" + DOUBLE_DIVIDER);
        System.out.println("  " + title);
        System.out.println(DOUBLE_DIVIDER);
    }

    /**
     * Prints a section header.
     */
    public static void printSectionHeader(String title) {
        System.out.println("\n" + DIVIDER);
        System.out.println("  " + title);
        System.out.println(DIVIDER);
    }

    /**
     * Prints a footer with count.
     */
    public static void printFooter(int count, String itemName) {
        System.out.println(DOUBLE_DIVIDER);
        System.out.println("  Total: " + count + " " + itemName + (count != 1 ? "s" : ""));
        System.out.println(DOUBLE_DIVIDER);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // USER DISPLAY
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Prints welcome message for logged-in user.
     */
    public static void printWelcome(UserDTO user) {
        System.out.println("\n" + DIVIDER);
        System.out.println("  👤 Welcome, " + user.firstName() + " " + user.lastName());
        System.out.println("     " + user.email());
        System.out.println(DIVIDER);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // CLIENT DISPLAY
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Prints a list of clients.
     */
    public static void printClientList(List<ClientDTO> clients) {
        printMenuHeader("CLIENTS");
        
        for (int i = 0; i < clients.size(); i++) {
            printClientCard(i + 1, clients.get(i));
        }
        
        printFooter(clients.size(), "client");
    }

    /**
     * Prints a single client card.
     */
    public static void printClientCard(int index, ClientDTO client) {
        System.out.println(DIVIDER);
        System.out.println("  #" + index + "  " + client.firstName() + " " + client.lastName());
        System.out.println(DIVIDER);
        System.out.println("  Email   : " + nullSafe(client.email()));
        System.out.println("  Phone   : " + nullSafe(client.phoneNumber()));
        System.out.println("  Address : " + nullSafe(client.address()));
        System.out.println("  City    : " + nullSafe(client.city()));
        System.out.println("  Country : " + nullSafe(client.country()));
    }

    /**
     * Prints client selection list (compact).
     */
    public static void printClientSelectionList(List<ClientDTO> clients) {
        printSectionHeader("SELECT CLIENT");
        
        for (int i = 0; i < clients.size(); i++) {
            ClientDTO client = clients.get(i);
            System.out.printf("  [%d] %s %s%n", i + 1, client.firstName(), client.lastName());
            System.out.println("      📧 " + nullSafe(client.email()));
            System.out.println("      📍 " + nullSafe(client.city()) + ", " + nullSafe(client.country()));
            
            if (i < clients.size() - 1) {
                System.out.println(DOT_DIVIDER);
            }
        }
        System.out.println(DIVIDER);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // COMPANY DISPLAY
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Prints company details.
     */
    public static void printCompanyDetails(CompanyDTO company) {
        printMenuHeader("COMPANY DETAILS");
        System.out.println(DIVIDER);
        System.out.println("  " + company.name());
        System.out.println(DIVIDER);
        System.out.println("  Org Number : " + company.orgNum());
        System.out.println("  Email      : " + nullSafe(company.email()));
        System.out.println("  Phone      : " + nullSafe(company.phoneNumber()));
        System.out.println("  Address    : " + nullSafe(company.address()));
        System.out.println("  City       : " + nullSafe(company.city()));
        System.out.println("  Country    : " + nullSafe(company.country()));
        System.out.println(DIVIDER);
        System.out.println("  Created    : " + formatDateTime(company.createdAt()));
        System.out.println("  Updated    : " + formatDateTime(company.updatedAt()));
        System.out.println(DOUBLE_DIVIDER);
    }

    /**
     * Prints company summary (for menu headers).
     */
    public static void printCompanySummary(CompanyDTO company) {
        System.out.println("  🏢 " + company.name());
        System.out.println("     Org: " + company.orgNum() + "  |  " + nullSafe(company.email()));
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // COMPANY USER DISPLAY
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Prints list of company users.
     */
    public static void printCompanyUserList(List<CompanyUser> users) {
        printMenuHeader("COMPANY USERS");
        
        for (int i = 0; i < users.size(); i++) {
            CompanyUser cu = users.get(i);
            System.out.println(DIVIDER);
            System.out.println("  #" + (i + 1) + "  " + cu.getUser().getFirstName() + " " + cu.getUser().getLastName());
            System.out.println(DIVIDER);
            System.out.println("  Email : " + cu.getUser().getEmail());
        }
        
        printFooter(users.size(), "user");
    }

    /**
     * Prints company user selection list (compact).
     */
    public static void printCompanyUserSelectionList(List<CompanyUser> users) {
        printSectionHeader("SELECT USER");
        
        for (int i = 0; i < users.size(); i++) {
            CompanyUser cu = users.get(i);
            System.out.printf("  [%d] %s %s%n", i + 1, 
                cu.getUser().getFirstName(), 
                cu.getUser().getLastName());
            System.out.println("      📧 " + cu.getUser().getEmail());
            
            if (i < users.size() - 1) {
                System.out.println(DOT_DIVIDER);
            }
        }
        System.out.println(DIVIDER);
    }

    /**
     * Formats a list of invoices for display.
     */
    public static void printInvoiceList(List<InvoiceDTO> invoices) {
        System.out.println("\n" + DOUBLE_DIVIDER);
        System.out.println("  INVOICES");
        System.out.println(DOUBLE_DIVIDER);
        
        for (int i = 0; i < invoices.size(); i++) {
            InvoiceDTO inv = invoices.get(i);
            printInvoiceCard(i + 1, inv);
        }
        
        System.out.println(DOUBLE_DIVIDER);
        System.out.println("  Total: " + invoices.size() + " invoice(s)");
        System.out.println(DOUBLE_DIVIDER);
    }

    /**
     * Formats a single invoice as a card display.
     */
    public static void printInvoiceCard(int index, InvoiceDTO invoice) {
        System.out.println(DIVIDER);
        System.out.println("  #" + index + "  Invoice: " + invoice.number());
        System.out.println(DIVIDER);
        System.out.println("  Status      : " + formatStatus(invoice.status().name()));
        System.out.println("  Due Date    : " + (invoice.dueDate() != null ? invoice.dueDate().format(DATETIME_FORMAT) : "N/A"));
        System.out.println("  Items       : " + invoice.items().size());
        System.out.println("  VAT Rate    : " + formatVatRate(invoice.vatRate()));
        System.out.println("  VAT Amount  : " + formatCurrency(invoice.vatAmount()));
        System.out.println("  Total Amount: " + formatCurrency(invoice.amount()));
        System.out.println("  Created     : " + (invoice.createdAt() != null ? invoice.createdAt().format(DATETIME_FORMAT) : "N/A"));
    }

    /**
     * Formats a compact invoice line for selection lists.
     */
    public static void printInvoiceSelectionList(List<InvoiceDTO> invoices) {
        System.out.println("\n" + DIVIDER);
        System.out.println("  SELECT INVOICE");
        System.out.println(DIVIDER);
        
        for (int i = 0; i < invoices.size(); i++) {
            InvoiceDTO inv = invoices.get(i);
            System.out.printf("  [%d] %s%n", i + 1, inv.number());
            System.out.printf("      Status: %-12s  Amount: %s%n", 
                formatStatus(inv.status().name()), 
                formatCurrency(inv.amount()));
            System.out.printf("      Due: %s  Items: %d%n",
                inv.dueDate() != null ? inv.dueDate().format(DATE_FORMAT) : "N/A",
                inv.items().size());
            if (i < invoices.size() - 1) {
                System.out.println("  · · · · · · · · · · · · · · · · · · · ·");
            }
        }
        System.out.println(DIVIDER);
    }

    /**
     * Formats invoice items for display.
     */
    public static void printInvoiceItemList(InvoiceDTO invoice, List<InvoiceItemDTO> items) {
        System.out.println("\n" + DOUBLE_DIVIDER);
        System.out.println("  INVOICE ITEMS - " + invoice.number());
        System.out.println(DOUBLE_DIVIDER);
        
        BigDecimal subtotal = BigDecimal.ZERO;
        
        for (int i = 0; i < items.size(); i++) {
            InvoiceItemDTO item = items.get(i);
            BigDecimal lineTotal = item.unitPrice().multiply(BigDecimal.valueOf(item.quantity()));
            subtotal = subtotal.add(lineTotal);
            
            System.out.println(DIVIDER);
            System.out.println("  #" + (i + 1) + "  " + item.name());
            System.out.println(DIVIDER);
            System.out.println("  Quantity   : " + item.quantity());
            System.out.println("  Unit Price : " + formatCurrency(item.unitPrice()));
            System.out.println("  Line Total : " + formatCurrency(lineTotal));
        }
        
        System.out.println(DOUBLE_DIVIDER);
        System.out.println("  Subtotal   : " + formatCurrency(subtotal));
        if (invoice.vatRate() != null && invoice.vatRate().compareTo(BigDecimal.ZERO) > 0) {
            System.out.println("  VAT (" + formatVatRate(invoice.vatRate()) + "): " + formatCurrency(invoice.vatAmount()));
        }
        System.out.println("  TOTAL      : " + formatCurrency(invoice.amount()));
        System.out.println(DOUBLE_DIVIDER);
    }

    /**
     * Formats invoice items for selection (compact view).
     */
    public static void printInvoiceItemSelectionList(List<InvoiceItemDTO> items) {
        System.out.println("\n" + DIVIDER);
        System.out.println("  SELECT ITEM");
        System.out.println(DIVIDER);
        
        for (int i = 0; i < items.size(); i++) {
            InvoiceItemDTO item = items.get(i);
            BigDecimal lineTotal = item.unitPrice().multiply(BigDecimal.valueOf(item.quantity()));
            
            System.out.printf("  [%d] %s%n", i + 1, item.name());
            System.out.printf("      Qty: %d × %s = %s%n",
                item.quantity(),
                formatCurrency(item.unitPrice()),
                formatCurrency(lineTotal));
            
            if (i < items.size() - 1) {
                System.out.println("  · · · · · · · · · · · · · · · · · · · ·");
            }
        }
        System.out.println(DIVIDER);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // HELPER METHODS
    // ═══════════════════════════════════════════════════════════════════════════

    private static String formatCurrency(BigDecimal amount) {
        if (amount == null) return "0.00";
        return String.format("%,.2f", amount);
    }

    private static String formatVatRate(BigDecimal vatRate) {
        if (vatRate == null || vatRate.compareTo(BigDecimal.ZERO) == 0) {
            return "N/A";
        }
        return String.format("%.0f%%", vatRate.multiply(BigDecimal.valueOf(100)));
    }

    private static String formatStatus(String status) {
        return switch (status) {
            case "CREATED" -> "📝 Created";
            case "SENT" -> "📤 Sent";
            case "PAID" -> "✅ Paid";
            case "OVERDUE" -> "⚠️  Overdue";
            case "CANCELLED" -> "❌ Cancelled";
            default -> status;
        };
    }

    private static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return "N/A";
        return dateTime.format(DATETIME_FORMAT);
    }

    private static String nullSafe(String value) {
        return value != null && !value.isBlank() ? value : "—";
    }
}

