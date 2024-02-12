package dev.example.demonet.graphql.controller;

import static dev.example.demonet.service.model.Position.Manager;
import static java.util.List.of;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.graphql.test.tester.GraphQlTester;

import dev.example.demonet.service.ProjectManagementService;
import dev.example.demonet.service.exception.EntityDuplicateException;
import dev.example.demonet.service.exception.ProjectNotFoundException;
import dev.example.demonet.service.model.Employee;
import dev.example.demonet.service.model.Project;

@GraphQlTest(ProjectManagementGraphqlController.class)
public class ProjectManagementGraphqlControllerTest {

    @Autowired
    private GraphQlTester graphQlTester;

    @MockBean
    private ProjectManagementService service;

    private final Project project = new Project(1, "TEST");
    private final Employee employee = new Employee(1, "JOHN", "SMITH", Manager);

    @Test
    public void getProjectTest() {

        doReturn(project).when(service).getProject(1);
        doReturn(of(employee)).when(service).getEmployees(project.id());

        this.graphQlTester.documentName("getProject").variable("id", "1").execute().errors().verify().path("project")
                .matchesJson("""
                        {
                            "id": "1",
                            "name": "TEST",
                            "employees": [
                                {
                                    "id": "1",
                                    "firstName": "JOHN",
                                    "lastName": "SMITH",
                                    "position": "Manager"
                                }
                            ]
                        }""");
    }

    @Test
    public void getProjectNotFoundTest() {
        doThrow(ProjectNotFoundException.class).when(service).getProject(1);

        this.graphQlTester.documentName("getProject").variable("id", "1").execute().errors()
                .expect(e -> e.getErrorType().equals(ErrorType.NOT_FOUND));
    }

    @Test
    public void getProjectsTest() {

        doReturn(of(project)).when(service).getProjects();
        doReturn(of(employee)).when(service).getEmployees(project.id());

        this.graphQlTester.documentName("getProjects").variable("id", "1").execute().errors().verify().path("projects")
                .matchesJson("""
                        [
                            {
                                "id":"1",
                                "name":"TEST",
                                "employees":[
                                    {
                                        "id":"1",
                                        "firstName":"JOHN",
                                        "lastName":"SMITH",
                                        "position":"Manager"
                                    }
                                ]
                            }
                        ]""");
    }

    @Test
    public void addProjectTest() {
        doReturn(project).when(service).addProject(project);

        this.graphQlTester.documentName("addProject").variable("id", project.id()).variable("name", project.name())
                .execute().errors().verify().path("addProject").matchesJson("""
                        {
                            "id": "1",
                            "name": "TEST"
                        }""");
    }

    @Test
    public void addDuplicateProjectTest() {
        doThrow(EntityDuplicateException.class).when(service).addProject(project);

        this.graphQlTester.documentName("addProject").variable("id", project.id()).variable("name", project.name())
                .execute().errors().expect(e -> e.getErrorType().equals(ErrorType.BAD_REQUEST));
    }
}
