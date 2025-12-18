package org.example.service;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import org.example.dto.InvoiceDTO;
import org.example.dto.InvoiceItemDTO;
import org.example.entity.Invoice;
import org.example.entity.InvoiceItem;
import org.example.entity.InvoiceStatus;
import org.example.repository.InvoiceItemRepository;
import org.example.repository.InvoiceRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

//One Service-class which is an Aggregate Root, An invoice and its lines/items are logically connected.
// One line/item has no reason to exist without an invoice.
// ie.  the method createInvoiceWithItems is more secure when both the lines/items and invoice are saved in the same transaction
public class InvoiceService {

    private final InvoiceItemRepository invoiceItemRepository;
    private final InvoiceRepository invoiceRepository;


    public InvoiceService(InvoiceItemRepository invoiceItemRepository, InvoiceRepository invoiceRepository) {
        this.invoiceItemRepository = invoiceItemRepository;
        this.invoiceRepository = invoiceRepository;
    }
//user sends us a draftDTO with no ID no created_at and total:amount not confirmed
    // returns a DTO to the frontend:
    //input DTO: "I want to create this"
    //Method: Translating dto to entity
    //Return DTO: Confirmation: this has been created with the definitive details)
    //we don't want to return entities because of safety or LazyInitializationException
    public InvoiceDTO createInvoice(InvoiceDTO dto) {
        // Translating DTO to Entity (to save in DB)
        Invoice invoice = new Invoice();
        invoice.setNumber(dto.number());
        invoice.setDueDate(dto.dueDate());
        invoice.setStatus(InvoiceStatus.CREATED);

        // creates the entity (DB creates ID and timestamp)
        Invoice savedInvoice = invoiceRepository.create(invoice);

        // translate entity to DTO (to give the user a complete receipt)
        return mapToDTO(savedInvoice);
    }

    public Optional<InvoiceDTO> getInvoiceById(UUID id) {}

    public void updateStatus(UUID id, InvoiceStatus newStatus) {
        Invoice invoice=invoiceRepository.findById(id)
            .orElseThrow(()->new EntityNotFoundException("Invoice not found"));

        //updates the status of the entity
        invoice.setStatus(newStatus);

        //saves the update to the database
        invoiceRepository.update(invoice);

    }

    public void deleteInvoice(UUID id) {
        invoiceRepository.deleteById(id);
    }

    //method to update items on an existing invoice
    public InvoiceDTO updateInvoiceItems(UUID id, Set<InvoiceItemDTO> newItemDtos) {
        Invoice invoice=invoiceRepository.findByIdWithItems(id)
            .orElseThrow(()->new EntityNotFoundException("Invoice not found"));

        //clears the current set to handle deletions (orphan removal handles SQL)
        invoice.getItems().clear();

        //Maps the new DTOs to entities and adds them
        for (InvoiceItemDTO itemDto : newItemDtos) {
            InvoiceItem item = new InvoiceItem();
            item.setQuantity(itemDto.quantity());
            item.setUnitPrice(itemDto.unitPrice());
            item.setInvoice(invoice); // Viktigt för @ManyToOne-kopplingen

            invoice.getItems().add(item);

            //saves the complete Invoice
            Invoice updatedInvoice=invoiceRepository.update(invoice);

            //returns the updated invoice as DTO
            return mapToDTO(updatedInvoice);
        }
    }



    //method that converts data from entity to DTO (easy for user)
    private InvoiceDTO mapToDTO(Invoice invoice) {
        return InvoiceDTO.builder()
            .id(invoice.getId())
            .number(invoice.getNumber())
            .status(invoice.getStatus())
            .dueDate(invoice.getDueDate())
            .createdAt(invoice.getCreatedAt())
            // Calculate the total amount based on the number of items
            .amount(calculateTotal(invoice.getItems()))
            .build();
    }

    private BigDecimal calculateTotal(Set<InvoiceItem> items) {
        if (items == null) return BigDecimal.ZERO;
        return items.stream()
            .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }








}
