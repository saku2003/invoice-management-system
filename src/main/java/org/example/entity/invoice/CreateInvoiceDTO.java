package org.example.entity.invoice;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CreateInvoiceDTO(
    @NotNull(message = "Company ID cannot be null")
    UUID companyId,

    @NotNull(message = "Client ID cannot be null")
    UUID clientId,

    @NotBlank(message = "Invoice number cannot be blank")
    String number,

    @NotNull(message = "Due date cannot be null")
    LocalDateTime dueDate,

    @DecimalMin(value = "0.0", inclusive = false, message = "VAT rate must be greater than 0")
    @DecimalMax(value = "1.0", inclusive = true, message = "VAT rate must not exceed 1.0")
    BigDecimal vatRate,

    @NotNull(message = "Items cannot be null")
    @NotEmpty(message = "Invoice must have at least one item")
    @Valid
    List<InvoiceItemDTO> items
) {
}
