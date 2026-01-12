package org.example.cli;

import jakarta.persistence.EntityManagerFactory;
import org.example.auth.AuthService;
import org.example.repository.*;
import org.example.service.*;
import org.example.util.JpaUtil;

/**
 * Container for all services used by the CLI application.
 * Centralizes dependency injection and service initialization.
 */
public class ServiceContainer {
    private final EntityManagerFactory emf;

    // Repositories
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final CompanyUserRepository companyUserRepository;
    private final ClientRepository clientRepository;
    private final InvoiceRepository invoiceRepository;

    // Services
    private final UserService userService;
    private final AuthService authService;
    private final CompanyService companyService;
    private final CompanyUserService companyUserService;
    private final ClientService clientService;
    private final InvoiceService invoiceService;

    public ServiceContainer() {
        this.emf = JpaUtil.getEntityManagerFactory();

        // Initialize repositories
        this.userRepository = new UserRepository(emf);
        this.companyRepository = new CompanyRepository(emf);
        this.companyUserRepository = new CompanyUserRepository(emf);
        this.clientRepository = new ClientRepository(emf);
        this.invoiceRepository = new InvoiceRepository(emf);

        // Initialize services
        this.userService = new UserService(userRepository, companyUserRepository);
        this.authService = new AuthService(userRepository, userService);
        this.companyService = new CompanyService(companyRepository, companyUserRepository, userRepository);
        this.companyUserService = new CompanyUserService(userRepository, companyUserRepository, companyRepository);
        this.clientService = new ClientService(clientRepository, companyRepository);
        this.invoiceService = new InvoiceService(invoiceRepository, companyRepository, clientRepository);
    }

    public EntityManagerFactory getEmf() {
        return emf;
    }

    public UserService getUserService() {
        return userService;
    }

    public AuthService getAuthService() {
        return authService;
    }

    public CompanyService getCompanyService() {
        return companyService;
    }

    public CompanyUserService getCompanyUserService() {
        return companyUserService;
    }

    public ClientService getClientService() {
        return clientService;
    }

    public InvoiceService getInvoiceService() {
        return invoiceService;
    }
}


