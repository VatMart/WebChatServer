package ru.vatmart.webchatserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vatmart.webchatserver.entities.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
}
