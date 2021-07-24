package ru.vatmart.webchatserver.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.vatmart.webchatserver.entities.ImageModel;
import ru.vatmart.webchatserver.entities.User;
import ru.vatmart.webchatserver.exceptions.ImageExistException;
import ru.vatmart.webchatserver.repositories.ImageModelRepository;
import ru.vatmart.webchatserver.repositories.UserRepository;

import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
public class ImageService {
    public static final Logger LOG = LoggerFactory.getLogger(ImageService.class);

    private ImageModelRepository imageRepository;

    private UserRepository userRepository;

    @Autowired
    public ImageService(ImageModelRepository imageRepository, UserRepository userRepository) {
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ImageModel uploadImageToUser(MultipartFile file, Principal principal) throws IOException, SQLException {
        User user = getUserByPrincipal(principal);
        LOG.info("Uploading image profile to User {}", user.getUsername());

        ImageModel userProfileImage = imageRepository.findByUserId(user.getId()).orElse(null);
        if (!ObjectUtils.isEmpty(userProfileImage)) {
            imageRepository.delete(userProfileImage);
        }
        ImageModel imageModel = new ImageModel();
        imageModel.setUserId(user.getId());
        //imageModel.setImageBytes(compressBytes(file.getBytes()));
        imageModel.setImageBytes(file.getBytes());
        imageModel.setName(file.getOriginalFilename());
        LOG.info("Uploading image bytes {}", imageModel.getImageBytes());
        return imageRepository.save(imageModel);
    }

    @Transactional
    public ImageModel getImageToUser(Principal principal) throws ImageExistException {
        User user = getUserByPrincipal(principal);

        ImageModel imageModel = imageRepository.findByUserId(user.getId()).orElseThrow(() -> new ImageExistException("Image for user doesn't exist"));
//        if (!ObjectUtils.isEmpty(imageModel)) {
//            //imageModel.setImageBytes(decompressBytes(imageModel.getImageBytes()));
//            imageModel.setImageBytes(imageModel.getImageBytes());
//        }
        return imageModel;
    }

    @Transactional
    public ImageModel getImageToUser(Long userId) throws UsernameNotFoundException, ImageExistException {
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found with id " + userId));
        ImageModel imageModel = imageRepository.findByUserId(user.getId()).orElseThrow(() -> new ImageExistException("Image not found for user id "+ userId));
//        if (!ObjectUtils.isEmpty(imageModel)) {
//            //imageModel.setImageBytes(decompressBytes(imageModel.getImageBytes()));
//            imageModel.setImageBytes(imageModel.getImageBytes());
//        }
        return imageModel;
    }

    private User getUserByPrincipal(Principal principal) {
        String login = principal.getName();
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username " + login));

    }

    //TODO Загрузка и выгрузка изображений для сообщений

    private byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            LOG.error("Cannot compress Bytes");
        }
        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
        return outputStream.toByteArray();
    }

    private static byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException | DataFormatException e) {
            LOG.error("Cannot decompress Bytes");
        }
        return outputStream.toByteArray();
    }
}
