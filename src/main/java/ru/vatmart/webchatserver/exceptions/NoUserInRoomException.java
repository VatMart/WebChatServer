package ru.vatmart.webchatserver.exceptions;

public class NoUserInRoomException extends RuntimeException {
    public NoUserInRoomException(String message) {
        super(message);
    }
}
