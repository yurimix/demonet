package dev.example.demonet.ws.endpoint;

import java.util.List;

import dev.example.demonet.service.model.Employee;
import dev.example.demonet.service.model.Position;
import dev.example.demonet.service.model.Project;
import dev.example.demonet.ws.model.EmployeeDto;
import dev.example.demonet.ws.model.PositionDto;
import dev.example.demonet.ws.model.ProjectDto;

public class DataMapper {

    public static ProjectDto toProjectDto(Project project, List<Employee> employees) {
        var dto = toProjectDto(project);
        employees.stream().map(DataMapper::toEmployeeDto).forEach(e -> dto.getEmployees().add(e));
        return dto;
    }

    public static EmployeeDto toEmployeeDto(Employee employee, List<Project> projects) {
        var dto = toEmployeeDto(employee);
        projects.stream().map(DataMapper::toProjectDto).forEach(p -> dto.getProjects().add(p));
        return dto;
    }

    public static Project fromProjectDto(ProjectDto dto) {
        return new Project(dto.getId(), dto.getName());
    }

    private static ProjectDto toProjectDto(Project project) {
        var dto = new ProjectDto();
        dto.setId(project.id());
        dto.setName(project.name());
        return dto;
    }

    private static EmployeeDto toEmployeeDto(Employee employee) {
        var dto = new EmployeeDto();
        dto.setId(employee.id());
        dto.setFirstName(employee.firstName());
        dto.setLastName(employee.lastName());
        dto.setPosition(toPositionDto(employee.position()));
        return dto;
    }

    private static PositionDto toPositionDto(Position position) {
        return switch (position) {
        case Manager -> PositionDto.MANAGER;
        case Developer -> PositionDto.DEVELOPER;
        case QA -> PositionDto.QA;
        };
    }

}
