package org.example.entity.invoice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CreateInvoiceDTO(
    UUID companyId,
    UUID clientId,
    String number,
    LocalDateTime dueDate,
    Float vatRate,
    List<InvoiceItemDTO> items
) {}
