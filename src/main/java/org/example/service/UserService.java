package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.auth.PasswordEncoder;
import org.example.entity.company.CompanyUser;
import org.example.entity.user.CreateUserDTO;
import org.example.entity.user.UserDTO;
import org.example.entity.user.User;
import org.example.exception.BusinessRuleException;
import org.example.exception.EntityNotFoundException;
import org.example.exception.ValidationException;
import org.example.repository.CompanyUserRepository;
import org.example.repository.UserRepository;
import org.example.util.LogUtil;

import java.util.List;
import java.util.UUID;


@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final CompanyUserRepository companyUserRepository;

    public UserService(UserRepository userRepository, CompanyUserRepository companyUserRepository) {
        this.userRepository = userRepository;
        this.companyUserRepository = companyUserRepository;
    }

    public UserDTO register(CreateUserDTO dto) {

        log.debug("User registration started for email={}", LogUtil.maskEmail(dto.email()));

        boolean emailValid = dto.email() != null && dto.email().matches(
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9]([A-Za-z0-9-]*[A-Za-z0-9])?(\\.[A-Za-z0-9]([A-Za-z0-9-]*[A-Za-z0-9])?)*\\.[A-Za-z]{2,}$"
        );
        boolean passwordValid = dto.password() != null && dto.password().length() >= 8;

        if (!emailValid) {
            log.debug("Registration failed: invalid email format for email={}", LogUtil.maskEmail(dto.email()));
            log.warn("User registration failed due to invalid input");
            throw new ValidationException("Invalid registration data");
        }

        if (userRepository.existsByEmail(dto.email())) {
            log.debug("Registration failed: email already exists for email={}", LogUtil.maskEmail(dto.email()));
            log.warn("User registration failed due to invalid input");
            throw new BusinessRuleException("Invalid registration data");
        }

        if (!passwordValid) {
            log.debug("Registration failed: password validation failed");
            log.warn("User registration failed due to invalid input");
            throw new ValidationException("Password must be at least 8 characters");
        }

        User user = User.fromDTO(dto);
        user.setPassword(PasswordEncoder.hash(dto.password()));
        userRepository.create(user);

        log.info("User registered successfully with id={}", user.getId());
        return UserDTO.fromEntity(user);
    }


    public void deleteUser(UUID userId) {

        log.debug("User deletion requested for userId={}", userId);

        User user = userRepository.findById(userId)
            .orElseThrow(() -> {
                log.warn("User deletion failed: user not found for userId={}", userId);
                return new EntityNotFoundException("User", userId);
            });

        List<CompanyUser> companyUsers = companyUserRepository.findByUserId(userId);

        log.debug("Found {} company associations for userId={}", companyUsers.size(), userId);

        // delete all company associations before deleting the actual User
        int deletedCount = companyUserRepository.deleteByUserId(userId);
        log.debug("Deleted {} company associations for userId={}", deletedCount, userId);

        userRepository.delete(user);
        log.info("User deleted successfully with userId={}, removed from {} companies",
            userId, deletedCount);
    }
}
