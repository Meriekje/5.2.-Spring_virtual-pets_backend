package virtualpets.dtos;

import lombok.Data;


@Data
public class UserDTO {

    private Long id;
    private String username;
    private String role;
}
