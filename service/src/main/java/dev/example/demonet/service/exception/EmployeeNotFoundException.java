package dev.example.demonet.service.exception;

public class EmployeeNotFoundException extends EntityNotFoundException {
    private static final long serialVersionUID = 1L;

    public EmployeeNotFoundException(Integer id) {
        super("Employee not found", id);
    }
}
