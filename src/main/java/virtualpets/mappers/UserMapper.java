package virtualpets.mappers;

import virtualpets.dtos.UserDTO;
import virtualpets.models.User;

public class UserMapper {

    public static UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setRole(user.getRole().name());
        return dto;
    }
}
