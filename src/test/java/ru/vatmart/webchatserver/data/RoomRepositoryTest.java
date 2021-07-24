package ru.vatmart.webchatserver.data;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.vatmart.webchatserver.entities.Room;
import ru.vatmart.webchatserver.entities.User;
import ru.vatmart.webchatserver.entities.enums.Role;
import ru.vatmart.webchatserver.repositories.RoomRepository;
import ru.vatmart.webchatserver.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RoomRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoomRepository roomRepository;

    @Test
    public void testGetUserRooms() {
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

        Room room2 = new Room();
        room2.setCreator(user1);
        room2.setRoom_name("nametest3");

        user1.addRoom(room1);
        user1.addRoom(room2);
        user2.addRoom(room1);
        user3.addRoom(room1);

        roomRepository.saveAndFlush(room1);
        roomRepository.saveAndFlush(room2);

        User user = userRepository.findByLogin("logtest1").get();
        List<Room> rooms = roomRepository.findByUsersContaining(user).get();
        rooms.forEach(System.out::println);
    }
}
