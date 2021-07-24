package ru.vatmart.webchatserver.websocket.wscontrollers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import ru.vatmart.webchatserver.dto.MessageDTO;
import ru.vatmart.webchatserver.dto.RoomDTO;
import ru.vatmart.webchatserver.entities.Message;
import ru.vatmart.webchatserver.exceptions.MessageExistException;
import ru.vatmart.webchatserver.facade.MessageFacade;
import ru.vatmart.webchatserver.payloads.requests.CreateChatMessageRequest;
import ru.vatmart.webchatserver.services.MessageService;
import ru.vatmart.webchatserver.services.RoomService;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class WSMessageChatController {
    public static final Logger logger = LoggerFactory.getLogger(WSMessageChatController.class);

    @Autowired
    private RoomService roomService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private MessageFacade messageFacade;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/room/{room-id}/add-message")
    public void sendMessage(@DestinationVariable("room-id") Long roomId,
                                  @Payload CreateChatMessageRequest messageRequest,
                                  Principal principal) {
        MessageService.MessageCreatedResult messageCreatedResult =
                messageService.createMessage(messageRequest, roomId, principal);
        Message message = messageCreatedResult.getMessage();
        MessageDTO messageDTO = messageFacade.messageToMessageDTO(message);
        messageCreatedResult.getMembersSet().stream().forEach(user -> {
            messagingTemplate.convertAndSendToUser(user.getLogin(),"/queue/"+roomId+"/message", messageDTO);
        });
    }

    @MessageMapping("/room/{room-id}/get-all-messages")
    @SendToUser("/queue/{room-id}/all-messages")
    public List<MessageDTO> getAllMessages(@DestinationVariable("room-id") Long roomId,
                                           Principal principal) {
        List<Message> messages = messageService.getAllMessages(roomId, principal);
        List<MessageDTO> messageDTOS = messages.stream().
                map(message -> messageFacade.messageToMessageDTO(message)).
                collect(Collectors.toList());
        logger.info("Try gets all Messages by user = {}. In room = {}", principal.getName(), roomId);
        return messageDTOS;
    }
}
