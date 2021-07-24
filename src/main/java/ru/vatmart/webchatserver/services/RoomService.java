package ru.vatmart.webchatserver.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.vatmart.webchatserver.entities.Room;
import ru.vatmart.webchatserver.entities.User;
import ru.vatmart.webchatserver.exceptions.CreatingRoomException;
import ru.vatmart.webchatserver.exceptions.NoUserInRoomException;
import ru.vatmart.webchatserver.exceptions.RoomExistException;
import ru.vatmart.webchatserver.exceptions.UserExistException;
import ru.vatmart.webchatserver.payloads.requests.CreateRoomRequest;
import ru.vatmart.webchatserver.repositories.RoomRepository;
import ru.vatmart.webchatserver.repositories.UserRepository;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.List;

@Service
public class RoomService {
    public static final Logger logger = LoggerFactory.getLogger(RoomService.class);

    private RoomRepository roomRepository;

    private UserRepository userRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository, UserRepository userRepository) {
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    //TODO TEST THIS
    @Transactional()
    public Room createRoom(CreateRoomRequest createRoomRequest, Principal principal) throws CreatingRoomException{
        User user = getUserByPrincipal(principal);
        Room room = new Room();
        room.setRoom_name(createRoomRequest.getName());
        room.setCreator(user);
        user.addRoom(room);
        try {
            logger.info("Saving Room {}", createRoomRequest.getName());
            Room newRoom = roomRepository.save(room);
            return newRoom;
        } catch (Exception e) {
            logger.error("Error on saving room. {}", createRoomRequest.getName());
            throw new CreatingRoomException("Error on saving room. " + e.getMessage());
        }
    }

    @Transactional
    public List<Room> getUserRooms(Principal principal) {
        User user = getUserByPrincipal(principal);
        return roomRepository.findByUsersContaining(user).orElseThrow(() -> new RoomExistException("User doesn't have rooms."));
    }

    @Transactional
    public Room getUserRoom(Long roomId, Principal principal) throws NoUserInRoomException, RoomExistException {
        User user = getUserByPrincipal(principal);
        if (!hasUserInRoom(user, roomId)) {
            logger.error("Error on getting room. User doesn't exist in room {}", roomId);
            throw new NoUserInRoomException("User doesn't exist in room");
        }
       return roomRepository.findById(roomId).orElseThrow(() -> new RoomExistException("Room doesn't exist."));
    }

    //TODO TEST THIS
    @Transactional
    public Room addUserToRoom(String nickname, Long roomId, Principal principal) throws NoUserInRoomException, RoomExistException, UserExistException {
        User user = getUserByPrincipal(principal);
        if (!hasUserInRoom(user, roomId)) {
            logger.error("Error on adding user to room. User doesn't exist in room {}", roomId);
            throw new NoUserInRoomException("User doesn't exist in room");
        }
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new RoomExistException("Room doesn't exist."));
        if (room.getUsers().stream().anyMatch(u -> u.getNickname().equals(nickname))) {
           logger.error("Error on adding user to room. User {} already exist in room {}", nickname, roomId);
           throw new UserExistException("User already exist in room");
        }
        User addUser = userRepository.findByNickname(nickname).orElseThrow(() -> new UserExistException("Adding user nickname doesn't exist"));
        //room.getUsers().add(addUser); // Test and delete this
        addUser.addRoom(room);
        return roomRepository.save(room);
    }

    private boolean hasUserInRoom(User user, Long roomId) {
        return user.getRooms().stream().anyMatch(room -> room.getRoomId().equals(roomId));
    }

//    private Set<User> createListUserFromListIds(List<Long> usersIds) {
//        return usersIds.stream().
//                map(id -> userRepository.findById(id).orElseThrow(
//                        () ->  new UsernameNotFoundException("Username not found with id " +id))
//                )
//                .collect(Collectors.toSet());
//    }

    private User getUserByPrincipal(Principal principal) {
        String login = principal.getName();
        return userRepository.findByLogin(login)
                .orElseThrow(() ->  new UsernameNotFoundException("Username not found with login " +login));
    }
}
