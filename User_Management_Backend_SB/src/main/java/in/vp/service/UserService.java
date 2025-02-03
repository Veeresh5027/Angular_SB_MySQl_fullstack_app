package in.vp.service;


import in.vp.dto.*;
import java.util.List;

public interface UserService {
	public List<UserDTO> getAllUsers();
    public UserDTO getUserById(Integer id);
    public UserDTO saveUser(UserDTO userDTO);
    public UserDTO updateUser(Integer id, UserDTO userDTO);
    public void deleteUser(Integer id);
}

