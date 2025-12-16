package org.example;


import org.example.dto.UserDTO;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    void testCreateUser() {
        String email = "test2@email.com";
        String ssn = "1234567-0000";

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(userRepository.existsBySsn(ssn)).thenReturn(false);

        UserDTO userDTO = userService.create(
            "test",
            "test",
            email,
            "password",
            ssn
        );

        assertNotNull(userDTO);
        assertEquals(email, userDTO.email());
        assertEquals("test", userDTO.firstName());

        verify(userRepository, times(1)).save(any());
    }

    @Test
    void testCreateUserEmailAlreadyExists() {
        String email = "exists@email.com";

        when(userRepository.existsByEmail(email)).thenReturn(true);

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            userService.create("test", "test", email, "pass", "123");
        });

        assertEquals("Email already in use", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testCreateUserSsnAlreadyExists() {
        String ssn = "1234567-0000";

        when(userRepository.existsBySsn(ssn)).thenReturn(true);

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            userService.create("test", "test", "exists@email.com", "pass", ssn);
        });

        assertEquals("SSN already in use", exception.getMessage());
        verify(userRepository, never()).save(any());
    }
}
