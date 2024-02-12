package dev.example.demonet.service.exception;

public class EntityDuplicateException extends EntityException {
    private static final long serialVersionUID = 1L;

    public EntityDuplicateException(String message, Integer id) {
        super(message, id);
    }
}
