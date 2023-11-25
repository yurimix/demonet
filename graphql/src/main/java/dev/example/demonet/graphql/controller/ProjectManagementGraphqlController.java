package dev.example.demonet.graphql.controller;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import dev.example.demonet.service.ProjectManagementService;
import dev.example.demonet.service.model.Employee;
import dev.example.demonet.service.model.Project;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ProjectManagementGraphqlController {

    private final ProjectManagementService service;

    public ProjectManagementGraphqlController(ProjectManagementService service) {
        this.service = service;
    }

    @QueryMapping("project")
    public Project getProject(@Argument Integer id) {
        log.info("Get project by id: " + id);
        return service.getProject(id);
    }

    @QueryMapping("projects")
    public List<Project> getProjects() {
        log.info("Get all projects");
        return service.getProjects();
    }

    @QueryMapping("employee")
    public Employee getEmployee(@Argument Integer id) {
        log.info("Get employee by id: " + id);
        return service.getEmployee(id);
    }

    @QueryMapping("employees")
    public List<Employee> getEmployees() {
        log.info("Get all employees");
        return service.getEmployees();
    }

    @SchemaMapping("employees")
    public List<Employee> getEmployeesByProject(Project project) {
        log.info("Get employees of project: " + project);
        return service.getEmployees(project.id());
    }

    @SchemaMapping("projects")
    public List<Project> getEmployeesByProject(Employee employee) {
        log.info("Get projects of employee: " + employee);
        return service.getProjects(employee.id());
    }

    @MutationMapping
    public boolean attachToProject(@Argument Integer projectId, @Argument Integer employeeId) {
        log.info("Attach employee " + employeeId + " to project " + projectId);
        return service.attachToProject(projectId, employeeId);
    }

    @MutationMapping
    public boolean detachFromProject(@Argument Integer projectId, @Argument Integer employeeId) {
        log.info("Detach employee " + employeeId + " to project " + projectId);
        return service.detachFromProject(projectId, employeeId);
    }

    @MutationMapping
    public Project addProject(@Argument Project project) {
        log.info("Add project " + project);
        return service.addProject(project);
    }

}
