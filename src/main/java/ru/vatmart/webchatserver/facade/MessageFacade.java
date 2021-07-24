package ru.vatmart.webchatserver.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.vatmart.webchatserver.dto.MessageDTO;
import ru.vatmart.webchatserver.entities.Message;

@Component
public class MessageFacade {
    private final UserFacade userFacade;

    @Autowired
    public MessageFacade(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    public MessageDTO messageToMessageDTO(Message message) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setId(message.getMessage_id());
        messageDTO.setSendingDate(message.getSendingDate());
        messageDTO.setText(message.getText());
        messageDTO.setSender_id(message.getSender().getUser_id());
        messageDTO.setRoom_id(message.getRoom().getRoomId());
        return messageDTO;
    }
}
