package in.vp.test;

import in.vp.controller.UserController;
import in.vp.dto.UserDTO;
import in.vp.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserController.class) // Optimized for controller layer testing
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc; // Used to perform HTTP requests

    @MockBean
    private UserService userService; // Mocking the service layer

    // 1. Test getAllUsers()
    @Test
    public void testGetAllUsers() throws Exception {
        UserDTO user1 = new UserDTO(1, "John Doe", "john@example.com", "9876543210", "Address 1");
        UserDTO user2 = new UserDTO(2, "Jane Doe", "jane@example.com", "9876543211", "Address 2");

        List<UserDTO> userList = Arrays.asList(user1, user2);
        when(userService.getAllUsers()).thenReturn(userList);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[1].name").value("Jane Doe"));

        verify(userService, times(1)).getAllUsers();
    }

    // 2. Test getUserById()
    @Test
    public void testGetUserById() throws Exception {
        UserDTO user = new UserDTO(1, "John Doe", "john@example.com", "9876543210", "Address 1");

        when(userService.getUserById(1)).thenReturn(user);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));

        verify(userService, times(1)).getUserById(1);
    }

    // 3. Test addUser()
    @Test
    public void testAddUser() throws Exception {
        UserDTO user = new UserDTO(1, "John Doe", "john@example.com", "9876543210", "Address 1");

        when(userService.saveUser(any(UserDTO.class))).thenReturn(user);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"John Doe\",\"email\":\"john@example.com\",\"mobileNo\":\"9876543210\",\"address\":\"Address 1\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));

        verify(userService, times(1)).saveUser(any(UserDTO.class));
    }

    // 4. Test updateUser()
    @Test
    public void testUpdateUser() throws Exception {
        UserDTO updatedUser = new UserDTO(1, "John Smith", "johnsmith@example.com", "9876543210", "New Address");

        when(userService.updateUser(eq(1), any(UserDTO.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"John Smith\",\"email\":\"johnsmith@example.com\",\"mobileNo\":\"9876543210\",\"address\":\"New Address\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Smith"))
                .andExpect(jsonPath("$.email").value("johnsmith@example.com"));

        verify(userService, times(1)).updateUser(eq(1), any(UserDTO.class));
    }

    // 5. Test deleteUser()
    @Test
    public void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(1);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteUser(1);
    }
}

