package dev.example.demonet.service.exception;

public class EntityException extends ProjectManagementException {
    private static final long serialVersionUID = 1L;

    private final Integer id;

    public EntityException(String message, Integer id) {
        super(message);
        this.id = id;
    }

    public Integer getEntityId() {
        return id;
    }
}
