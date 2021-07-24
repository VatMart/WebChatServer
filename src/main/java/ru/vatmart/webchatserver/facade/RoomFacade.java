package ru.vatmart.webchatserver.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.vatmart.webchatserver.dto.RoomDTO;
import ru.vatmart.webchatserver.entities.Room;

import java.util.stream.Collectors;

@Component
public class RoomFacade {
    @Autowired
    private UserFacade userFacade;

    public RoomDTO roomToRoomDTO(Room room) {
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setCreator_id(room.getCreator().getUser_id());
        roomDTO.setId(room.getRoomId());
        roomDTO.setUsers(room.getUsers().
                stream().
                map(userFacade::userToUserDTO).
                collect(Collectors.toList()));
        roomDTO.setName(room.getRoom_name());
        return roomDTO;
    }
}
