package dev.example.demonet.ws.endpoint;

import static dev.example.demonet.ws.config.WebServiceConfig.NAMESPACE;
import static dev.example.demonet.ws.endpoint.DataMapper.fromProjectDto;
import static dev.example.demonet.ws.endpoint.DataMapper.toEmployeeDto;
import static dev.example.demonet.ws.endpoint.DataMapper.toProjectDto;
import static java.util.Collections.emptyList;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import dev.example.demonet.service.ProjectManagementService;
import dev.example.demonet.ws.model.AddProjectRequest;
import dev.example.demonet.ws.model.AddProjectResponse;
import dev.example.demonet.ws.model.AttachToProjectRequest;
import dev.example.demonet.ws.model.AttachToProjectResponse;
import dev.example.demonet.ws.model.DetachFromProjectRequest;
import dev.example.demonet.ws.model.DetachFromProjectResponse;
import dev.example.demonet.ws.model.GetEmployeeRequest;
import dev.example.demonet.ws.model.GetEmployeeResponse;
import dev.example.demonet.ws.model.GetEmployeesRequest;
import dev.example.demonet.ws.model.GetEmployeesResponse;
import dev.example.demonet.ws.model.GetProjectRequest;
import dev.example.demonet.ws.model.GetProjectResponse;
import dev.example.demonet.ws.model.GetProjectsRequest;
import dev.example.demonet.ws.model.GetProjectsResponse;

@Endpoint
public class ProjectManagementEndpoint {

    private final ProjectManagementService service;

    public ProjectManagementEndpoint(ProjectManagementService service) {
        this.service = service;
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "getProjectRequest")
    public @ResponsePayload GetProjectResponse getProject(@RequestPayload GetProjectRequest request) {
        var dto = toProjectDto(service.getProject(request.getId()),
                request.isWithEmployees() ? service.getEmployees(request.getId()) : emptyList());
        var response = new GetProjectResponse();
        response.setProject(dto);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "getProjectsRequest")
    public @ResponsePayload GetProjectsResponse getProjects(@RequestPayload GetProjectsRequest request) {
        var projects = service.getProjects();
        var response = new GetProjectsResponse();
        projects.forEach(project -> {
            var dto = toProjectDto(project,
                    request.isWithEmployees() ? service.getEmployees(project.id()) : emptyList());
            response.getProjects().add(dto);
        });
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "getEmployeeRequest")
    public @ResponsePayload GetEmployeeResponse getEmployee(@RequestPayload GetEmployeeRequest request) {
        var dto = toEmployeeDto(service.getEmployee(request.getId()),
                request.isWithProjects() ? service.getProjects(request.getId()) : emptyList());
        var response = new GetEmployeeResponse();
        response.setEmployee(dto);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "getEmployeesRequest")
    public @ResponsePayload GetEmployeesResponse getEmployees(@RequestPayload GetEmployeesRequest request) {
        var employees = service.getEmployees();
        var response = new GetEmployeesResponse();
        employees.forEach(employee -> {
            var dto = toEmployeeDto(employee,
                    request.isWithProjects() ? service.getProjects(employee.id()) : emptyList());
            response.getEmployees().add(dto);
        });
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "attachToProjectRequest")
    public @ResponsePayload AttachToProjectResponse attachToProject(@RequestPayload AttachToProjectRequest request) {
        var response = new AttachToProjectResponse();
        response.setStatus(service.attachToProject(request.getProjectId(), request.getEmployeeId()));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "detachFromProjectRequest")
    public @ResponsePayload DetachFromProjectResponse detachFromProject(
            @RequestPayload DetachFromProjectRequest request) {
        var response = new DetachFromProjectResponse();
        response.setStatus(service.detachFromProject(request.getProjectId(), request.getEmployeeId()));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "addProjectRequest")
    public @ResponsePayload AddProjectResponse addProject(@RequestPayload AddProjectRequest request) {
        var response = new AddProjectResponse();
        response.setProject(toProjectDto(service.addProject(fromProjectDto(request.getProject())), emptyList()));
        return response;
    }

}
