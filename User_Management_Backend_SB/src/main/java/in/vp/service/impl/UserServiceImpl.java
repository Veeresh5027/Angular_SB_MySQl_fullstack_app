package in.vp.service.impl;

import in.vp.dto.UserDTO;
import in.vp.enttity.UserEntity;
import in.vp.repository.UserRepository;
import in.vp.service.UserService;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
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
    
    @Override
    public void importUsersFromExcel(MultipartFile file) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            
            for (int i = 1; i <= sheet.getLastRowNum(); i++) { // Skip header row
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String email = getCellValueAsString(row.getCell(2)).trim().toLowerCase();
                if (email.isEmpty()) continue; // Skip if no email
                
                // Check if user already exists
                Optional<UserEntity> existingUser = userRepository.findByEmail(email);
                
                UserDTO userDTO = new UserDTO();
                userDTO.setName(getCellValueAsString(row.getCell(1)));
                userDTO.setEmail(email);
                userDTO.setMobileNo(getCellValueAsString(row.getCell(3)));
                userDTO.setAddress(getCellValueAsString(row.getCell(4)));

                if (existingUser.isPresent()) {
                    // Update existing user if needed
                    UserEntity user = existingUser.get();
                    if (!user.getName().equals(userDTO.getName())) {
                        user.setName(userDTO.getName());
                    }
                    if (!user.getMobileNo().equals(userDTO.getMobileNo())) {
                        user.setMobileNo(userDTO.getMobileNo());
                    }
                    if (!user.getAddress().equals(userDTO.getAddress())) {
                        user.setAddress(userDTO.getAddress());
                    }
                    userRepository.save(user);
                } else {
                    // Save new user
                    saveUser(userDTO);
                }
            }
        }
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> "";
        };
    }

}
