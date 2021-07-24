package ru.vatmart.webchatserver.data;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;
import ru.vatmart.webchatserver.entities.Message;
import ru.vatmart.webchatserver.entities.Room;
import ru.vatmart.webchatserver.entities.User;
import ru.vatmart.webchatserver.entities.enums.Role;
import ru.vatmart.webchatserver.repositories.MessageRepository;
import ru.vatmart.webchatserver.repositories.RoomRepository;
import ru.vatmart.webchatserver.repositories.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@DataJpaTest
public class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoomRepository roomRepository;

    @Test
    public void testBidirectionalAssociation() {
        // First part
        System.out.println("IT WORKS!");
        User user1 = new User();
        user1.setLogin("logtest2");
        user1.setPassword("pastest2");
        user1.setNickname("nicktest2");
        user1.getRoles().add(Role.ROLE_USER);

        Room room1 = new Room();
        room1.setCreator(user1);
        room1.setRoom_name("nametest2");

        user1.addRoom(room1);
        //userRepository.saveAndFlush(user1);

        System.out.println("Message");
        Message message = new Message();
        message.setSender(user1);
        message.setText("texttest2");

        room1.addMessage(message);
        messageRepository.saveAndFlush(message); // MUST SAVE USER AND ROOM TO
        System.out.println("Message saved");

//        userRepository.saveAndFlush(user1);
        System.out.println("USERS");
        userRepository.findAll().forEach(System.out::println);
        System.out.println("ROOMS");
        roomRepository.findAll().forEach(System.out::println);
        System.out.println("MESSAGES");
        messageRepository.findAll().forEach(System.out::println);

        // Second part
        System.out.println("Second part\nLoading data");
        User user2 = userRepository.findByLogin("logtest2").get();
        Room room2 = roomRepository.findByCreator_Login("logtest2").get();
        Message message2 = messageRepository.findBySenderNickname("nicktest2").get();

        System.out.println("Messages in user list: ");
        user2.getMessages().forEach(System.out::println);
        System.out.println("Messages in room list: ");
        room2.getMessages().forEach(System.out::println);
        System.out.println("Users in room list: ");
        room2.getUsers().forEach(System.out::println);
        System.out.println("Rooms in user list: ");
        user2.getRooms().forEach(System.out::println);
        System.out.println("Sender in message: ");
        System.out.println(message2.getSender());
        System.out.println("Room in message: ");
        System.out.println(message2.getRoom());


        System.out.println("END TEST");
    }

    @Test
    public void testPageableQuery() {
        System.out.println("Part 1. Create and save data\n");

        User user1 = new User();
        User user2 = new User();
        User user3 = new User();
        user1.setLogin("logtest1");
        user1.setPassword("pastest1");
        user1.setNickname("nicktest1");
        user1.getRoles().add(Role.ROLE_USER);
        user2.setLogin("logtest2");
        user2.setPassword("pastest2");
        user2.setNickname("nicktest2");
        user2.getRoles().add(Role.ROLE_USER);
        user3.setLogin("logtest3");
        user3.setPassword("pastest3");
        user3.setNickname("nicktest3");
        user3.getRoles().add(Role.ROLE_USER);

        Room room1 = new Room();
        room1.setCreator(user1);
        room1.setRoom_name("nametest2");

        user1.addRoom(room1);
        user2.addRoom(room1);
        user3.addRoom(room1);

        Message message1 = new Message();
        Message message2 = new Message();
        Message message3 = new Message();
        Message message4 = new Message();
        Message message5 = new Message();
        Message message6 = new Message();
        message1.setSender(user1);
        message1.setText("texttest1");
        message2.setSender(user2);
        message2.setText("texttest2");
        message3.setSender(user3);
        message3.setText("texttest3");
        message4.setSender(user2);
        message4.setText("texttest4");
        message5.setSender(user2);
        message5.setText("texttest5");
        message6.setSender(user1);
        message6.setText("texttest6");

        room1.addMessage(message1);
        room1.addMessage(message2);
        room1.addMessage(message3);
        room1.addMessage(message4);
        room1.addMessage(message5);
        room1.addMessage(message6);

        messageRepository.saveAndFlush(message1); // MUST SAVE USER AND ROOM TO
        messageRepository.saveAndFlush(message2);
        messageRepository.saveAndFlush(message3);
        messageRepository.saveAndFlush(message4);
        messageRepository.saveAndFlush(message5);
        messageRepository.saveAndFlush(message6);

        System.out.println("USERS");
        userRepository.findAll().forEach(System.out::println);
        System.out.println("ROOMS");
        roomRepository.findAll().forEach(System.out::println);
        System.out.println("MESSAGES");
        messageRepository.findAll().forEach(System.out::println);

        System.out.println("Part 2. Load and pageable test\n");
        Page<Message> page1 = messageRepository.findAll(PageRequest.of(0,2, Sort.by(Sort.Order.desc("sendingDate"))));
        page1.forEach(System.out::println);
        System.out.println("next page");
        Page<Message> page2 = messageRepository.findAll(PageRequest.of(1,2, Sort.by(Sort.Order.desc("sendingDate"))));
        page2.forEach(System.out::println);
    }

}
