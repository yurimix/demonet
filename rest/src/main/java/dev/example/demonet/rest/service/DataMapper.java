package dev.example.demonet.rest.service;

import static java.util.Collections.emptyList;

import java.util.List;

import dev.example.demonet.rest.model.EmployeeDto;
import dev.example.demonet.rest.model.ProjectDto;
import dev.example.demonet.service.model.Employee;
import dev.example.demonet.service.model.Project;

public class DataMapper {

    public static ProjectDto toProjectDto(Project project) {
        return new ProjectDto(project.id(), project.name(), emptyList());
    }

    public static ProjectDto toProjectDto(Project project, List<Employee> employees) {
        var dto = toProjectDto(project);
        dto.setEmployees(employees.stream().map(DataMapper::toEmployeeDto).toList());
        return dto;
    }

    public static EmployeeDto toEmployeeDto(Employee employee) {
        return new EmployeeDto(employee.id(), employee.firstName(), employee.lastName(), employee.position().name(),
                emptyList());
    }

    public static EmployeeDto toEmployeeDto(Employee employee, List<Project> projects) {
        var dto = toEmployeeDto(employee);
        dto.setProjects(projects.stream().map(DataMapper::toProjectDto).toList());
        return dto;
    }

}
