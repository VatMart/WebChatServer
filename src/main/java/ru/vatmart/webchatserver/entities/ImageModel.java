package ru.vatmart.webchatserver.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "images")
public class ImageModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long image_id;
    @Column(nullable = false)
    private String name;
    @Lob
    private byte[] imageBytes;
    //@JsonIgnore
    @Column(name = "user_id", unique = true)
    private Long userId;
    //@JsonIgnore
    private Long messageId;

    public Long getImage_id() {
        return image_id;
    }

    public void setImage_id(Long image_id) {
        this.image_id = image_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    @Override
    public String toString() {
        return "ImageModel{" +
                "image_id=" + image_id +
                ", name='" + name + '\'' +
                ", imageBytes=" + Arrays.toString(imageBytes) +
                ", userId=" + userId +
                ", messageId=" + messageId +
                '}';
    }
}
