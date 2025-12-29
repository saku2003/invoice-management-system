package org.example.service;

import org.example.repository.CompanyRepository;
import org.example.repository.CompanyUserRepository;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CompanyUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CompanyUserRepository companyUserRepository;

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private CompanyUserService companyUserService;

    @Test
    void addUserToCompanyByEmail() {
    }

    @Test
    void deleteUserFromCompany() {
    }

    @Test
    void isUserAssociatedWithCompany() {
    }

    @Test
    void getCompanyUsers() {
    }

    @Test
    void getUserCompanies() {
    }
}
