package ru.ulstu.is.sbapp.rate.service.exception;

public class SubjectNotFoundException extends RuntimeException{
    public SubjectNotFoundException (Long id) {super(String.format("Subject with id [%d] is not found", id));}
}
