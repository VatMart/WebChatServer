package ru.vatmart.webchatserver.facade;

import org.springframework.stereotype.Component;
import ru.vatmart.webchatserver.dto.UserDTO;
import ru.vatmart.webchatserver.entities.User;

@Component
public class UserFacade {
    public UserDTO userToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setNickname(user.getNickname());
        userDTO.setRegistrationDate(user.getRegistrationDate());
        return userDTO;
    }

}
