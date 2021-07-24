package ru.vatmart.webchatserver.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.vatmart.webchatserver.entities.Message;
import ru.vatmart.webchatserver.entities.Room;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    Optional<Message> findBySenderNickname(String nickname);
    Optional<List<Message>> findAllByRoomRoomId(Long roomId);
    //@Query("select m from Message m where m.room.roomId = :roomId order by m.sendingDate desc")
    Optional<Message> findFirstByRoomOrderBySendingDateDesc(Room room);

    //@Query("select m from Message m ")
    //List<Message> findAllMessages(Pageable page);
}
