package dev.example.demonet.service.exception;

public class EntityNotFoundException extends ProjectManagementException {

    private static final long serialVersionUID = 1L;

    public EntityNotFoundException(String message) {
        super(message);
    }
}
