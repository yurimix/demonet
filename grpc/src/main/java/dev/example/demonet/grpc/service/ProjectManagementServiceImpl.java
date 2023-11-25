package dev.example.demonet.grpc.service;

import static dev.example.demonet.grpc.service.DataMapper.toEmployeeDto;
import static dev.example.demonet.grpc.service.DataMapper.toProjectDto;
import static java.util.Collections.emptyList;

import java.util.List;

import com.google.protobuf.BoolValue;

import dev.example.demonet.grpc.model.AddProjectRequest;
import dev.example.demonet.grpc.model.EmployeeDto;
import dev.example.demonet.grpc.model.GetEmployeeRequest;
import dev.example.demonet.grpc.model.GetEmployeesRequest;
import dev.example.demonet.grpc.model.GetProjectRequest;
import dev.example.demonet.grpc.model.GetProjectsRequest;
import dev.example.demonet.grpc.model.ProjectDto;
import dev.example.demonet.grpc.model.ProjectManagementRequest;
import dev.example.demonet.grpc.model.ProjectManagementServiceGrpc;
import dev.example.demonet.service.ProjectManagementService;
import dev.example.demonet.service.model.Employee;
import dev.example.demonet.service.model.Project;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class ProjectManagementServiceImpl extends ProjectManagementServiceGrpc.ProjectManagementServiceImplBase {

    private final ProjectManagementService service;

    public ProjectManagementServiceImpl(ProjectManagementService service) {
        this.service = service;
    }

    @Override
    public void getProject(GetProjectRequest request, StreamObserver<ProjectDto> responseObserver) {
        var project = service.getProject(request.getId());
        responseObserver.onNext(resolveEmployees(project, request.getWithEmployees()));
        responseObserver.onCompleted();
    }

    @Override
    public void getProjects(GetProjectsRequest request, StreamObserver<ProjectDto> responseObserver) {
        service.getProjects()
                .forEach(project -> responseObserver.onNext(resolveEmployees(project, request.getWithEmployees())));
        responseObserver.onCompleted();
    }

    @Override
    public void getEmployee(GetEmployeeRequest request, StreamObserver<EmployeeDto> responseObserver) {
        var employee = service.getEmployee(request.getId());
        responseObserver.onNext(resolveProjects(employee, request.getWithProjects()));
        responseObserver.onCompleted();
    }

    @Override
    public void getEmployees(GetEmployeesRequest request, StreamObserver<EmployeeDto> responseObserver) {
        service.getEmployees()
                .forEach(employee -> responseObserver.onNext(resolveProjects(employee, request.getWithProjects())));
        responseObserver.onCompleted();
    }

    @Override
    public void attachToProject(ProjectManagementRequest request, StreamObserver<BoolValue> responseObserver) {
        var res = service.attachToProject(request.getProjectId(), request.getEmployeeId());
        responseObserver.onNext(BoolValue.of(res));
        responseObserver.onCompleted();
    }

    @Override
    public void detachFromProject(ProjectManagementRequest request, StreamObserver<BoolValue> responseObserver) {
        var res = service.detachFromProject(request.getProjectId(), request.getEmployeeId());
        responseObserver.onNext(BoolValue.of(res));
        responseObserver.onCompleted();
    }

    @Override
    public void addProject(AddProjectRequest request, StreamObserver<ProjectDto> responseObserver) {
        var project = service.addProject(new Project(request.getId(), request.getName()));
        responseObserver.onNext(resolveEmployees(project, false));
        responseObserver.onCompleted();
    }

    private ProjectDto resolveEmployees(Project project, boolean withEmployees) {
        List<Employee> employees = withEmployees ? service.getEmployees(project.id()) : emptyList();
        return toProjectDto(project, employees);
    }

    private EmployeeDto resolveProjects(Employee employee, boolean withProjects) {
        List<Project> projects = withProjects ? service.getProjects(employee.id()) : emptyList();
        return toEmployeeDto(employee, projects);
    }
}
