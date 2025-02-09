package in.vp.test;

import in.vp.dto.UserDTO;
import in.vp.enttity.UserEntity;
import in.vp.repository.UserRepository;
import in.vp.service.impl.UserServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private UserEntity userEntity;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        userEntity = new UserEntity();
        userEntity.setId(1);
        userEntity.setName("John Doe");
        userEntity.setEmail("john@example.com");
        userEntity.setMobileNo("1234567890");
        userEntity.setAddress("New York");

        userDTO = new UserDTO();
        userDTO.setId(1);
        userDTO.setName("John Doe");
        userDTO.setEmail("john@example.com");
        userDTO.setMobileNo("1234567890");
        userDTO.setAddress("New York");
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAllByOrderByCreatedAtDesc()).thenReturn(Arrays.asList(userEntity));

        List<UserDTO> users = userService.getAllUsers();

        assertEquals(1, users.size());
        assertEquals("John Doe", users.get(0).getName());
        verify(userRepository, times(1)).findAllByOrderByCreatedAtDesc();
    }

    @Test
    void testGetUserById() {
        when(userRepository.findById(1)).thenReturn(Optional.of(userEntity));

        UserDTO result = userService.getUserById(1);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void testSaveUser() {
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        UserDTO savedUser = userService.saveUser(userDTO);

        assertNotNull(savedUser);
        assertEquals("John Doe", savedUser.getName());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    /** 
     * Test case for updating a user (when user exists)
     */
    @Test
    public void testUpdateUserSuccess() {
        when(userRepository.findById(1)).thenReturn(Optional.of(userEntity));
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        userDTO.setName("Updated Name");
        UserDTO updatedUser = userService.updateUser(1, userDTO);

        assertNotNull(updatedUser);
        assertEquals("Updated Name", updatedUser.getName());
    }

    /** 
     * Test case for updating a non-existing user (covering return null)
     */
    @Test
    public void testUpdateUserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());  // User not found

        UserDTO updatedUser = userService.updateUser(1, userDTO);

        assertNull(updatedUser); // Ensures return null is executed
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userRepository).deleteById(1);

        userService.deleteUser(1);

        verify(userRepository, times(1)).deleteById(1);
    }
}
