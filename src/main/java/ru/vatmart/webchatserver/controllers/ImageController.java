package ru.vatmart.webchatserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.vatmart.webchatserver.dto.ImageDTO;
import ru.vatmart.webchatserver.entities.ImageModel;
import ru.vatmart.webchatserver.exceptions.ImageExistException;
import ru.vatmart.webchatserver.facade.ImageFacade;
import ru.vatmart.webchatserver.payloads.responses.MessageResponse;
import ru.vatmart.webchatserver.services.ImageService;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;

@RestController
@RequestMapping("api/image")
//@CrossOrigin
public class ImageController {

    private ImageService imageService;
    private ImageFacade imageFacade;

    @Autowired
    public ImageController(ImageService imageService, ImageFacade imageFacade) {
        this.imageFacade = imageFacade;
        this.imageService = imageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<MessageResponse> uploadImageToUser(@RequestParam("file") MultipartFile file,
                                                             Principal principal)  {
        try {
            imageService.uploadImageToUser(file, principal);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new MessageResponse("Error on uploading image"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(new MessageResponse("Image Uploaded Successfully"));
    }

    @GetMapping("/profileImage")
    public ResponseEntity<ImageDTO> getImageForUser(Principal principal) {
        ImageModel userImage;
        try {
            userImage = imageService.getImageToUser(principal);
        } catch (ImageExistException e) {
            //TODO
            return null;
        }
        ImageDTO imageDTO = imageFacade.imageModelToImageDTO(userImage);
        return new ResponseEntity<>(imageDTO, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ImageDTO> getImageForUser(@PathVariable("userId") Long userId) {
        ImageModel userImage;
        try {
            userImage = imageService.getImageToUser(userId);
        } catch (ImageExistException e) {
            //TODO
            return null;
        }
        ImageDTO imageDTO = imageFacade.imageModelToImageDTO(userImage);
        //System.out.println("image = " + imageDTO);
        return new ResponseEntity<>(imageDTO, HttpStatus.OK);
    }

}
