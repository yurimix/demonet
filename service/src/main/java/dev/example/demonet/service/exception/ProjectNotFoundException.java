package dev.example.demonet.service.exception;

public class ProjectNotFoundException extends EntityNotFoundException {
    private static final long serialVersionUID = 1L;

    public ProjectNotFoundException(Integer id) {
        super("Project not found", id);
    }
}
