package dev.example.demonet.rest.controller;

import static org.springframework.http.HttpStatus.OK;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dev.example.demonet.rest.model.EmployeeDto;
import dev.example.demonet.rest.model.ProjectDto;
import dev.example.demonet.rest.service.AdaptedProjectManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Project Management API")
public class ProjectManagementRestController {

    private final AdaptedProjectManagementService service;

    public record AddProjectRequest(
            @Schema(name = "projectId", description = "The new project ID, must be unique")
            int projectId,
            @Schema(name = "projectName", description = "The new project name")
            String projectName) {
    }

    public ProjectManagementRestController(AdaptedProjectManagementService service) {
        this.service = service;
    }

    @Operation(summary = "Get a project by id", description = "Returns a project as per the id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved"), 
        @ApiResponse(responseCode = "404", description = "Not found - The project was not found")
    })
    @GetMapping("/projects/{projectId}")
    public @ResponseBody ProjectDto getProject(@PathVariable final int projectId,
            @RequestParam(required = false) final boolean withEmployees) {
        return service.getProject(projectId, withEmployees);
    }

    @Operation(summary = "Get all projects", description = "Returns list of all existing projects")
    @GetMapping("/projects")
    public @ResponseBody List<ProjectDto> getProjects(@RequestParam(required = false) final boolean withEmployees) {
        return service.getProjects(withEmployees);
    }

    @Operation(summary = "Get an employee by id", description = "Returns an employee as per the id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved"), 
        @ApiResponse(responseCode = "404", description = "Not found - The employee was not found")
    })
    @GetMapping("/employees/{employeeId}")
    public @ResponseBody EmployeeDto getEmployee(@PathVariable final int employeeId,
            @RequestParam(required = false) final boolean withProjects) {
        return service.getEmployee(employeeId, withProjects);
    }

    @Operation(summary = "Get all employees", description = "Returns list of all existing employees")
    @GetMapping("/employees")
    public @ResponseBody List<EmployeeDto> getEmployees(@RequestParam(required = false) final boolean withProjects) {
        return service.getEmployees(withProjects);
    }

    @Operation(summary = "Add a new project", description = "Add a new project into the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully added"), 
        @ApiResponse(responseCode = "400", description = "Duplicated project ID")
    })
    @PostMapping("/projects")
    @ResponseStatus(OK)
    public void addProject(@RequestBody final AddProjectRequest project) {
        service.addProject(project.projectId(), project.projectName());
    }

    @Operation(summary = "Attach an employee to the project", description = "Return 'true' in success or 'false' if the user is already attached to the project")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully processed"), 
        @ApiResponse(responseCode = "404", description = "Not found - projectId or employeeId was not found")
    })
    @PutMapping("/projects/{projectId}/{employeeId}")
    public boolean attachToProject(@PathVariable final int projectId, @PathVariable final int employeeId) {
        return service.attachToProject(projectId, employeeId);
    }

    @Operation(summary = "Detach an employee from the project", description = "Return 'true' in success or 'false' if the user is not attached to the project")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully processed"), 
        @ApiResponse(responseCode = "404", description = "Not found - projectId or employeeId was not found")
    })
    @DeleteMapping("/projects/{projectId}/{employeeId}")
    public boolean detachFromProject(@PathVariable final int projectId, @PathVariable final int employeeId) {
        return service.detachFromProject(projectId, employeeId);
    }

}
