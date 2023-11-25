package dev.example.demonet.rest.service;

import static dev.example.demonet.rest.service.DataMapper.toEmployeeDto;
import static dev.example.demonet.rest.service.DataMapper.toProjectDto;

import java.util.List;

import org.springframework.stereotype.Service;

import dev.example.demonet.rest.model.EmployeeDto;
import dev.example.demonet.rest.model.ProjectDto;
import dev.example.demonet.service.ProjectManagementService;
import dev.example.demonet.service.model.Project;

@Service
public class AdaptedProjectManagementService {

    private final ProjectManagementService baseService;

    public AdaptedProjectManagementService(ProjectManagementService baseService) {
        this.baseService = baseService;
    }

    public ProjectDto getProject(final int projectId, final boolean withEmployees) {
        if (withEmployees) {
            return toProjectDto(baseService.getProject(projectId), baseService.getEmployees(projectId));
        } else {
            return toProjectDto(baseService.getProject(projectId));
        }
    }

    public List<ProjectDto> getProjects(final boolean withEmployees) {
        var projects = baseService.getProjects();
        if (withEmployees) {
            return projects.stream().map(project -> toProjectDto(project, baseService.getEmployees(project.id())))
                    .toList();
        } else {
            return projects.stream().map(DataMapper::toProjectDto).toList();
        }
    }

    public EmployeeDto getEmployee(final int employeeId, final boolean withProjects) {
        if (withProjects) {
            return toEmployeeDto(baseService.getEmployee(employeeId), baseService.getProjects(employeeId));
        } else {
            return toEmployeeDto(baseService.getEmployee(employeeId));
        }
    }

    public List<EmployeeDto> getEmployees(final boolean withProjects) {
        var employees = baseService.getEmployees();
        if (withProjects) {
            return employees.stream().map(employee -> toEmployeeDto(employee, baseService.getProjects(employee.id())))
                    .toList();
        } else {
            return employees.stream().map(DataMapper::toEmployeeDto).toList();
        }
    }

    public void addProject(final int projectId, final String projectName) {
        baseService.addProject(new Project(projectId, projectName));
    }

    public boolean attachToProject(final int projectId, final int employeeId) {
        return baseService.attachToProject(projectId, employeeId);
    }

    public boolean detachFromProject(final int projectId, final int employeeId) {
        return baseService.detachFromProject(projectId, employeeId);
    }

}
