package ru.vatmart.webchatserver.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.vatmart.webchatserver.entities.Message;
import ru.vatmart.webchatserver.entities.Room;
import ru.vatmart.webchatserver.entities.User;
import ru.vatmart.webchatserver.exceptions.MessageExistException;
import ru.vatmart.webchatserver.exceptions.NoUserInRoomException;
import ru.vatmart.webchatserver.exceptions.RoomExistException;
import ru.vatmart.webchatserver.payloads.requests.CreateChatMessageRequest;
import ru.vatmart.webchatserver.repositories.MessageRepository;
import ru.vatmart.webchatserver.repositories.RoomRepository;
import ru.vatmart.webchatserver.repositories.UserRepository;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.List;
import java.util.Set;

@Service
public class MessageService {
    public static final Logger logger = LoggerFactory.getLogger(MessageService.class);

    private UserRepository userRepository;
    private RoomRepository roomRepository;
    private MessageRepository messageRepository;

    @Autowired
    public MessageService(UserRepository userRepository, RoomRepository roomRepository, MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.messageRepository = messageRepository;
    }

    //TODO TEST THIS
    @Transactional
    public MessageCreatedResult createMessage(CreateChatMessageRequest createChatMessageRequest, Long roomId, Principal principal)
            throws NoUserInRoomException, RoomExistException {
        User user = getUserByPrincipal(principal);
        if (!hasUserInRoom(user, roomId)) {
            logger.error("Error on creating message. User with login {} doesn't exist in room {}", user.getLogin(), roomId);
            throw new NoUserInRoomException("User doesn't exist in room");
        }
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new RoomExistException("Room doesn't exist."));
        Message message = new Message();
        //message.setRoom(room); // test and delete this
        message.setSender(user);
        message.setText(createChatMessageRequest.getText());
        room.addMessage(message);
        Message result = messageRepository.save(message);
        return new MessageCreatedResult(result, room.getUsers());
    }

    @Transactional
    public Page<Message> getMessages(Long roomId, Principal principal) {
        //messageRepository.findAll(PageRequest.of(0,12, Sort.by(Sort.Order.desc("sending_date"))));

        return null;
    }

    /** USE ONLY FOR TESTS
     */
    @Transactional
    public List<Message> getAllMessages(Long roomId, Principal principal)
            throws NoUserInRoomException, RoomExistException, MessageExistException {
        User user = getUserByPrincipal(principal);
        if (!hasUserInRoom(user, roomId)) {
            logger.error("Error on getting messages. User with login {} doesn't exist in room {}", user.getLogin(), roomId);
            throw new NoUserInRoomException("User doesn't exist in room");
        }
        //comment this
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new RoomExistException("Room doesn't exist."));
        return messageRepository.findAllByRoomRoomId(room.getRoomId()).orElseThrow(() -> new MessageExistException("Messages doesn't exist in room="+room.getRoomId()));
    }


    private User getUserByPrincipal(Principal principal) {
        String login = principal.getName();
        return userRepository.findByLogin(login)
                .orElseThrow(() ->  new UsernameNotFoundException("Username not found with nickname " +login));
    }

    private boolean hasUserInRoom(User user, Long roomId) {
        return user.getRooms().stream().anyMatch(room -> room.getRoomId().equals(roomId));
    }
    private boolean hasUserInRoom(User user, Room room) {
        return room.getUsers().contains(user);
    }

    @Transactional
    public Message getLastMessageInRoom(Long roomId, Principal principal) throws MessageExistException{
        User user = getUserByPrincipal(principal);
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new RoomExistException("Room doesn't exist"));
        if (!hasUserInRoom(user, room)) {
            logger.error("Error on creating message. User with login {} doesn't exist in room {}", user.getLogin(), roomId);
            throw new NoUserInRoomException("User doesn't exist in room");
        }
        return messageRepository.findFirstByRoomOrderBySendingDateDesc(room).orElseThrow(() -> new MessageExistException("No messages in Room"));
    }

    public static class MessageCreatedResult {
        private Message message;
        private Set<User> membersSet;

        private MessageCreatedResult(){}
        private MessageCreatedResult(Message message, Set<User> membersSet) {
            this.message = message;
            this.membersSet = membersSet;
        }

        public Message getMessage() {
            return message;
        }

        public Set<User> getMembersSet() {
            return membersSet;
        }
    }
}

