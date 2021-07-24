package ru.vatmart.webchatserver.facade;

import org.springframework.stereotype.Component;
import ru.vatmart.webchatserver.dto.ImageDTO;
import ru.vatmart.webchatserver.dto.MessageDTO;
import ru.vatmart.webchatserver.entities.ImageModel;
import ru.vatmart.webchatserver.entities.Message;

@Component
public class ImageFacade {
    public ImageDTO imageModelToImageDTO(ImageModel imageModel) {
        ImageDTO imageDTO = new ImageDTO();
        imageDTO.setImage_id(imageModel.getImage_id());
        imageDTO.setImageBytes(imageModel.getImageBytes());
        imageDTO.setName(imageModel.getName());
        imageDTO.setMessageId(imageModel.getMessageId());
        imageDTO.setUserId(imageModel.getUserId());
        return imageDTO;
    }
}
