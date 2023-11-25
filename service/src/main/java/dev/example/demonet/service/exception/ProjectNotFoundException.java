package dev.example.demonet.service.exception;

public class ProjectNotFoundException extends EntityNotFoundException {
    private static final long serialVersionUID = 1L;

    public ProjectNotFoundException(String message) {
        super(message);
    }
}
