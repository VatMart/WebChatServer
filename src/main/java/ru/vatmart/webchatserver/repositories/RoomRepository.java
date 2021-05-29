package ru.vatmart.webchatserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vatmart.webchatserver.entities.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
}
