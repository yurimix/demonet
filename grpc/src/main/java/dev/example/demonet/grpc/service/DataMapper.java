package dev.example.demonet.grpc.service;

import java.util.List;

import dev.example.demonet.grpc.model.EmployeeDto;
import dev.example.demonet.grpc.model.PositionDto;
import dev.example.demonet.grpc.model.ProjectDto;
import dev.example.demonet.service.model.Employee;
import dev.example.demonet.service.model.Position;
import dev.example.demonet.service.model.Project;

public class DataMapper {

    public static ProjectDto toProjectDto(Project project, List<Employee> employees) {
        var pbuilder = toProjectDtoBuilder(project);
        employees.stream().map(DataMapper::toEmployeeDto).forEach(pbuilder::addEmployees);
        return pbuilder.build();
    }

    public static EmployeeDto toEmployeeDto(Employee employee, List<Project> projects) {
        var ebuilder = toEmployeeDtoBuilder(employee);
        projects.stream().map(DataMapper::toProjectDto).forEach(ebuilder::addProjects);
        return ebuilder.build();
    }

    private static ProjectDto toProjectDto(Project project) {
        return toProjectDtoBuilder(project).build();
    }

    private static EmployeeDto toEmployeeDto(Employee employee) {
        return toEmployeeDtoBuilder(employee).build();
    }

    private static ProjectDto.Builder toProjectDtoBuilder(Project project) {
        return ProjectDto.newBuilder().setId(project.id()).setName(project.name());
    }

    private static EmployeeDto.Builder toEmployeeDtoBuilder(Employee employee) {
        return EmployeeDto.newBuilder().setId(employee.id()).setFirstName(employee.firstName())
                .setLastName(employee.lastName()).setPosition(toPositionDto(employee.position()));
    }

    private static PositionDto toPositionDto(Position position) {
        return switch (position) {
        case Manager -> PositionDto.MANAGER;
        case Developer -> PositionDto.DEVELOPER;
        case QA -> PositionDto.QA;
        };
    }

}
