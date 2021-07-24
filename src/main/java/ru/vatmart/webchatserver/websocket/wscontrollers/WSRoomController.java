package ru.vatmart.webchatserver.websocket.wscontrollers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import ru.vatmart.webchatserver.dto.MessageDTO;
import ru.vatmart.webchatserver.dto.RoomDTO;
import ru.vatmart.webchatserver.dto.UserDTO;
import ru.vatmart.webchatserver.entities.Message;
import ru.vatmart.webchatserver.entities.Room;
import ru.vatmart.webchatserver.entities.User;
import ru.vatmart.webchatserver.exceptions.CreatingRoomException;
import ru.vatmart.webchatserver.exceptions.UserExistException;
import ru.vatmart.webchatserver.facade.RoomFacade;
import ru.vatmart.webchatserver.facade.UserFacade;
import ru.vatmart.webchatserver.payloads.requests.CreateRoomRequest;
import ru.vatmart.webchatserver.services.MessageService;
import ru.vatmart.webchatserver.services.RoomService;
import ru.vatmart.webchatserver.services.UserService;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;


@Controller
public class WSRoomController {
    public static final Logger logger = LoggerFactory.getLogger(WSRoomController.class);

    @Autowired
    private RoomService roomService;
    @Autowired
    private RoomFacade roomFacade;
    @Autowired
    private UserFacade userFacade;
    @Autowired
    private UserService userService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/room/add-room")
    @SendToUser("/queue/room")
    public RoomDTO createRoom(@Payload CreateRoomRequest createRoomRequest, Principal principal) {
        Room room = roomService.createRoom(createRoomRequest, principal);
        RoomDTO roomDTO = roomFacade.roomToRoomDTO(room);
        User user = userService.getCurrentUser(principal);
        logger.info("Try create room by user = {}. roomRequest = {}", principal.getName(), createRoomRequest);
        return roomDTO;
//        messagingTemplate.convertAndSendToUser(
//                user.getId().toString(),
//                "/user/queue/rooms",
//                roomDTO);
    }

    @MessageMapping("/rooms")
    @SendToUser("/queue/rooms")
    public List<RoomDTO> getUserRooms(Principal principal) {
        List<Room> rooms = roomService.getUserRooms(principal);
        List<RoomDTO> roomDTOs = rooms.stream().
                map(room -> roomFacade.roomToRoomDTO(room)).
                collect(Collectors.toList());
        logger.info("Try gets Rooms by user = {}. rooms = {}", principal.getName(), roomDTOs);
        return roomDTOs;
    }

    @MessageMapping("/room/{room-id}/add-user/{nickname}")
    @SendTo("/room/{room-id}/users")
    public UserDTO addUser(@DestinationVariable("room-id") Long roomId,
                           @DestinationVariable("nickname") String nickname,
                           Principal principal) {
        User addUser = userService.getUserByNickname(nickname);
        Room room = roomService.addUserToRoom(nickname, roomId, principal);
        RoomDTO roomDTO = roomFacade.roomToRoomDTO(room);
        messagingTemplate.convertAndSendToUser(
                addUser.getLogin(),
                "/queue/room",
                roomDTO);
        return userFacade.userToUserDTO(addUser);
    }


}
