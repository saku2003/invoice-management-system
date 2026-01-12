package org.example.entity.invoice;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record UpdateInvoiceDTO(
    @NotNull(message = "Invoice ID cannot be null")
    UUID invoiceId,
    
    LocalDateTime dueDate,
    
    @DecimalMin(value = "0.0", inclusive = false, message = "VAT rate must be greater than 0")
    @DecimalMax(value = "1.0", inclusive = true, message = "VAT rate must not exceed 1.0")
    BigDecimal vatRate,
    
    @Valid
    List<InvoiceItemDTO> items,
    
    InvoiceStatus status
) {}
