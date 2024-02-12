package dev.example.demonet.service.exception;

public class ProjectDuplicateException extends EntityDuplicateException {
    private static final long serialVersionUID = 1L;

    public ProjectDuplicateException(Integer id) {
        super("The project already exists", id);
    }
}
