package ru.ulstu.is.sbapp.rate.service.exception;

public class StydentNotFoundException extends RuntimeException {
    public StydentNotFoundException(Long id) {
        super(String.format("Student with id [%d] is not found", id));
    }
}
