package dev.example.demonet.service.exception;

public class EntityNotFoundException extends EntityException {
    private static final long serialVersionUID = 1L;

    public EntityNotFoundException(String message, Integer id) {
        super(message, id);
    }
}
