package ru.vatmart.webchatserver.exceptions;

public class ImageExistException   extends RuntimeException {
    public ImageExistException(String message) {
        super(message);
    }
}