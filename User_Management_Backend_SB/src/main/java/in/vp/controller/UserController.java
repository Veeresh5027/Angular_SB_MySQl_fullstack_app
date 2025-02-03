package in.vp.controller;

import in.vp.dto.UserDTO;
import in.vp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200") // Allow Angular requests
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable("id") Integer id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public UserDTO addUser(@RequestBody UserDTO userDTO) {
        return userService.saveUser(userDTO);
    }

    @PutMapping("/{id}")
    public UserDTO updateUser(@PathVariable("id") Integer id, @RequestBody UserDTO userDTO) {
        return userService.updateUser(id, userDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") Integer id) {
        userService.deleteUser(id);
    }
}
