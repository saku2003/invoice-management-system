package org.example.service;

import org.example.entity.company.*;
import org.example.entity.user.User;
import org.example.exception.BusinessRuleException;
import org.example.exception.EntityNotFoundException;
import org.example.exception.ValidationException;
import org.example.repository.CompanyRepository;
import org.example.repository.CompanyUserRepository;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

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

    private UUID companyId;
    private UUID userId;
    private String email;
    private Company company;
    private User user;

    @BeforeEach
    void setup() {
        companyId = UUID.randomUUID();
        userId = UUID.randomUUID();
        email = "test@email.com";

        company = new Company();
        company.setId(companyId);

        user = new User();
        user.setId(userId);
        user.setEmail(email);
    }

    @Test
    @DisplayName("Should add user to company successfully")
    void addUserToCompanyByEmail_success() {
        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(companyUserRepository.findById(new CompanyUserId(userId, companyId)))
            .thenReturn(Optional.empty());

        companyUserService.addUserToCompanyByEmail(companyId, email);

        verify(companyUserRepository).create(argThat(cu ->
            cu.getUser().equals(user) &&
                cu.getCompany().equals(company)
        ));
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException if company not found")
    void addUserToCompanyByEmail_companyNotFound() {
        when(companyRepository.findById(companyId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
            () -> companyUserService.addUserToCompanyByEmail(companyId, email)
        );

        verify(companyRepository).findById(companyId);
        verifyNoInteractions(userRepository);
        verifyNoInteractions(companyUserRepository);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException if user not found")
    void addUserToCompanyByEmail_userNotFound() {
        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
            () -> companyUserService.addUserToCompanyByEmail(companyId, email)
        );

        verify(companyRepository).findById(companyId);
        verify(userRepository).findByEmail(email);
        verifyNoInteractions(companyUserRepository);
    }

    @Test
    @DisplayName("Should throw BusinessRuleException if user already associated")
    void addUserToCompanyByEmail_userAlreadyAssociated() {
        CompanyUserId id = new CompanyUserId(userId, companyId);

        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(companyUserRepository.findById(id)).thenReturn(Optional.of(new CompanyUser()));

        assertThrows(BusinessRuleException.class,
            () -> companyUserService.addUserToCompanyByEmail(companyId, email)
        );

        verify(companyUserRepository).findById(id);
        verify(companyUserRepository, never()).create(any());
    }

    @Test
    @DisplayName("Should delete user from company successfully")
    void deleteUserFromCompany_success() {
        CompanyUserId id = new CompanyUserId(userId, companyId);
        CompanyUser cu = new CompanyUser();

        when(companyUserRepository.findById(id)).thenReturn(Optional.of(cu));

        companyUserService.deleteUserFromCompany(companyId, userId);

        verify(companyUserRepository).delete(cu);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException if user not part of company")
    void deleteUserFromCompany_notFound() {
        CompanyUserId id = new CompanyUserId(userId, companyId);

        when(companyUserRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
            () -> companyUserService.deleteUserFromCompany(companyId, userId)
        );

        verify(companyUserRepository).findById(id);
        verify(companyUserRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Should return all users of a company")
    void getCompanyUsers_success() {
        List<CompanyUser> users = List.of(new CompanyUser(), new CompanyUser());

        when(companyUserRepository.findByCompanyId(companyId)).thenReturn(users);

        List<CompanyUser> result = companyUserService.getCompanyUsers(companyId);

        assertEquals(2, result.size());
        verify(companyUserRepository).findByCompanyId(companyId);
    }

    @Test
    @DisplayName("Should return all companies of a user")
    void getUserCompanies_success() {
        List<CompanyUser> companies = List.of(new CompanyUser(), new CompanyUser());

        when(companyUserRepository.findByUserId(userId)).thenReturn(companies);

        List<CompanyUser> result = companyUserService.getUserCompanies(userId);

        assertEquals(2, result.size());
        verify(companyUserRepository).findByUserId(userId);
    }
}
