package dev.example.demonet.service;

import java.util.List;

import dev.example.demonet.service.exception.DuplicateProjectException;
import dev.example.demonet.service.exception.EmployeeNotFoundException;
import dev.example.demonet.service.exception.ProjectNotFoundException;
import dev.example.demonet.service.model.Employee;
import dev.example.demonet.service.model.Project;

public interface ProjectManagementService {

    Project addProject(Project project) throws DuplicateProjectException;

    Project getProject(Integer id) throws ProjectNotFoundException;

    List<Project> getProjects();

    List<Project> getProjects(Integer employeeId);

    Employee getEmployee(Integer id) throws EmployeeNotFoundException;

    List<Employee> getEmployees();

    List<Employee> getEmployees(Integer projectId) throws ProjectNotFoundException;

    boolean attachToProject(Integer projectId, Integer employeeId)
            throws EmployeeNotFoundException, ProjectNotFoundException;

    boolean detachFromProject(Integer projectId, Integer employeeId)
            throws EmployeeNotFoundException, ProjectNotFoundException;
}
