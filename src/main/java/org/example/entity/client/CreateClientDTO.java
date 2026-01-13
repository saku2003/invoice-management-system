package org.example.entity.client;

import jakarta.validation.constraints.*;

import java.util.UUID;

public record CreateClientDTO(
    @NotNull(message = "Company ID cannot be null")
    UUID companyId,

    @NotBlank(message = "First name cannot be blank")
    @Size(max = 100, message = "First name must not exceed 100 characters")
    String firstName,

    @NotBlank(message = "Last name cannot be blank")
    @Size(max = 100, message = "Last name must not exceed 100 characters")
    String lastName,

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email must be a valid email address")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    String email,

    @Size(max = 255, message = "Address must not exceed 255 characters")
    String address,

    @Size(max = 100, message = "Country must not exceed 100 characters")
    String country,

    @Size(max = 100, message = "City must not exceed 100 characters")
    String city,

    @Size(max = 50, message = "Phone number must not exceed 50 characters")
    @Pattern(regexp = "^[+]?[0-9\\s\\-()]*$", message = "Phone number contains invalid characters")
    String phoneNumber
) {
}
