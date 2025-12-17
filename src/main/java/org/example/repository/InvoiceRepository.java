package org.example.repository;

import jakarta.persistence.EntityManagerFactory;
import org.example.entity.Invoice;

import java.util.UUID;

public class InvoiceRepository extends BaseRepository<Invoice, UUID>{
    protected InvoiceRepository(EntityManagerFactory emf) {
        super(emf, Invoice.class);
    }

    public Invoice createInvoice(Invoice invoice) {
        return runInTransaction(em -> {
            em.persist(invoice);
            return invoice;
        });

    }
}
