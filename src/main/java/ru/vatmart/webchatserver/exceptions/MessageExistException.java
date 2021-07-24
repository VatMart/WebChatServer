package ru.vatmart.webchatserver.exceptions;

public class MessageExistException extends RuntimeException {
    public MessageExistException(String message) {
        super(message);
    }
}
