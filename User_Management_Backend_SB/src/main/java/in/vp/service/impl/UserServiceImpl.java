package in.vp.service.impl;

import in.vp.dto.UserDTO;
import in.vp.enttity.UserEntity;
import in.vp.repository.UserRepository;
import in.vp.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(Integer id) {
        return userRepository.findById(id).map(this::mapToDto).orElse(null);
    }

    @Override
    public UserDTO saveUser(UserDTO userDTO) {
        UserEntity userEntity = mapToEntity(userDTO);
        UserEntity savedUser = userRepository.save(userEntity);
        return mapToDto(savedUser);
    }

    @Override
    public UserDTO updateUser(Integer id, UserDTO userDTO) {
        UserEntity existingUser = userRepository.findById(id).orElse(null);
        if (existingUser != null) {
            existingUser.setName(userDTO.getName());
            existingUser.setEmail(userDTO.getEmail());
            existingUser.setMobileNo(userDTO.getMobileNo());
            existingUser.setAddress(userDTO.getAddress());
            return mapToDto(userRepository.save(existingUser));
        }
        return null;
    }

    @Override
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    private UserDTO mapToDto(UserEntity user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setMobileNo(user.getMobileNo());
        dto.setAddress(user.getAddress());
        return dto;
    }

    private UserEntity mapToEntity(UserDTO dto) {
        UserEntity user = new UserEntity();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setMobileNo(dto.getMobileNo());
        user.setAddress(dto.getAddress());
        return user;
    }
}
