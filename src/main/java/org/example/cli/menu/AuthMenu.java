package org.example.cli.menu;

import org.example.cli.CliContext;
import org.example.cli.InputHelper;
import org.example.cli.ServiceContainer;
import org.example.entity.user.CreateUserDTO;
import org.example.entity.user.UserDTO;
import org.example.exception.AuthenticationException;
import org.example.exception.BusinessRuleException;
import org.example.exception.ValidationException;


public class AuthMenu {
    private final CliContext context;
    private final InputHelper input;
    private final ServiceContainer services;

    public AuthMenu(CliContext context, InputHelper input, ServiceContainer services) {
        this.context = context;
        this.input = input;
        this.services = services;
    }

    public boolean show() {
        System.out.println("--- Authentication ---");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.print("Choose option (1-2): ");

        int choice = input.readInt();

        if (choice == 1) {
            return login();
        } else if (choice == 2) {
            return register();
        } else {
            System.out.println("Invalid choice.");
            return false;
        }
    }

    private boolean login() {
        String email = input.readLine("Email: ");
        String password = input.readPassword("Password: ");

        try {
            UserDTO user = services.getAuthService().authenticate(email, password);
            context.setCurrentUser(user);
            System.out.println("✓ Login successful! Welcome, " + user.firstName() + " " + user.lastName());
            return true;

        } catch (AuthenticationException e) {
            System.out.println("✗ Login failed: " + e.getMessage());
            return false;
        }
    }

    private boolean register() {
        String firstName = input.readLine("First Name: ");
        String lastName = input.readLine("Last Name: ");
        String email = input.readLine("Email: ");
        String password = input.readPassword("Password: ");

        try {
            CreateUserDTO dto = new CreateUserDTO(firstName, lastName, email, password);
            UserDTO user = services.getUserService().register(dto);
            context.setCurrentUser(user);
            System.out.println("✓ Registration successful! Welcome, " + user.firstName() + " " + user.lastName());
            return true;

        } catch (ValidationException e) {
            System.out.println("✗ Invalid Registration Data: " + e.getMessage());
            return false;

        } catch (BusinessRuleException e) {
            System.out.println("✗ " + e.getMessage());
            return false;

        } catch (Exception e) {
            System.out.println("✗ Something went wrong: " + e.getMessage());
            return false;
        }
    }
}


