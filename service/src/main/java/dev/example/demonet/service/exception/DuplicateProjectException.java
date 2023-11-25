package dev.example.demonet.service.exception;

import dev.example.demonet.service.model.Project;

public class DuplicateProjectException extends ProjectManagementException {
    private static final long serialVersionUID = 1L;

    private final Project project;

    public DuplicateProjectException(String message, Project project) {
        super(message);
        this.project = project;
    }

    public Project getProject() {
        return project;
    }
}
