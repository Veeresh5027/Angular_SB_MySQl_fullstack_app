package in.vp.service;


import in.vp.dto.*;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface UserService {
	public List<UserDTO> getAllUsers();
    public UserDTO getUserById(Integer id);
    public UserDTO saveUser(UserDTO userDTO);
    public UserDTO updateUser(Integer id, UserDTO userDTO);
    public void deleteUser(Integer id);
    void importUsersFromExcel(MultipartFile file) throws IOException;

}

