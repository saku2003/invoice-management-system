package org.example.service;

import org.example.repository.CompanyRepository;
import org.example.repository.CompanyUserRepository;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

class CompanyServiceTest {

    private UserRepository userRepository;
    private CompanyUserRepository companyUserRepository;
    private CompanyRepository companyRepository;
    private CompanyService companyService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        companyUserRepository = mock(CompanyUserRepository.class);
        companyRepository = mock(CompanyRepository.class);
        companyService = new CompanyService(companyRepository, companyUserRepository, userRepository);
    }

    @Test
    void create() {
    }

    @Test
    void toDto() {
    }

    @Test
    void update() {
    }

    @Test
    void deleteCompany() {
    }
}
