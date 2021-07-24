package ru.vatmart.webchatserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vatmart.webchatserver.dto.MessageDTO;
import ru.vatmart.webchatserver.entities.Message;
import ru.vatmart.webchatserver.exceptions.MessageExistException;
import ru.vatmart.webchatserver.facade.MessageFacade;
import ru.vatmart.webchatserver.services.MessageService;

import java.security.Principal;

/** Used only for getting last message in room
 *
 */
@RestController
@RequestMapping("api/message")
public class MessageController {
    @Autowired
    private MessageService messageService;
    @Autowired
    private MessageFacade messageFacade;

    @GetMapping("/room/{roomId}/last-message")
    public MessageDTO getLastMessageInRoom(@PathVariable("roomId") Long roomId,
                                           Principal principal) {
        //System.out.println("ITS WORK");
        Message message;
        try {
            message = messageService.getLastMessageInRoom(roomId, principal);
        } catch (MessageExistException e) {
            //System.out.println("NULL");
            return null;
        }
        return messageFacade.messageToMessageDTO(message);
    }

}
