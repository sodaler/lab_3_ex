package ru.ulstu.is.sbapp.rate.service.exception;

public class GroupeNotFoundException extends RuntimeException {
    public GroupeNotFoundException(Long id) {
        super(String.format("Group with id [%d] is not found", id));
    }
}
