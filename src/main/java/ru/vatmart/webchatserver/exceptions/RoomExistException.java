package ru.vatmart.webchatserver.exceptions;

public class RoomExistException  extends RuntimeException {
    public RoomExistException(String message) {
        super(message);
    }
}
