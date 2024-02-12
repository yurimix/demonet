package dev.example.demonet.service.exception;

public class EmployeeDuplicateException extends EntityDuplicateException {
    private static final long serialVersionUID = 1L;

    public EmployeeDuplicateException(Integer id) {
        super("The employee already exists", id);
    }
}
