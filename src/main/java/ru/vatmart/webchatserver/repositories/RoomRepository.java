package ru.vatmart.webchatserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.vatmart.webchatserver.entities.Room;
import ru.vatmart.webchatserver.entities.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<Room> findByCreator_Login(String login);

    @Query("select r from Room r where :user member of r.users")
    Optional<List<Room>> findByUsersContaining(@Param("user") User user);
}
