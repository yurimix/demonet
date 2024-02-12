package dev.example.demonet.rest.controller;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import dev.example.demonet.rest.model.EmployeeDto;
import dev.example.demonet.rest.model.ProjectDto;
import dev.example.demonet.rest.service.AdaptedProjectManagementService;
import dev.example.demonet.service.exception.ProjectNotFoundException;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ProjectManagementRestControllerTest {

    @LocalServerPort
    private int port;

    @MockBean
    private AdaptedProjectManagementService service;

    @Autowired
    private TestRestTemplate restTemplate;

    private final EmployeeDto employee1 = new EmployeeDto(1, "SAM", "GOLD", "MANAGER", emptyList());
    private final EmployeeDto employee2 = new EmployeeDto(2, "JOHN", "SILVER", "QA", emptyList());
    private final ProjectDto project1 = new ProjectDto(1, "TEST1", emptyList());
    private final ProjectDto project2 = new ProjectDto(2, "TEST2", asList(employee1, employee2));

    @Test
    public void getProjects() {
        doReturn(asList(project1, project2)).when(service).getProjects(false);
        var p = restTemplate.getForObject(getUrl("/projects"), ProjectDto[].class);
        assertEquals(2, p.length);
        // TODO: check response body if necessary
    }

    @Test
    public void getProjectById() {
        doReturn(project1).when(service).getProject(1, false);
        var p = restTemplate.getForObject(getUrl("/projects/1"), ProjectDto.class);
        assertEquals(1, p.getId());
        assertEquals("TEST1", p.getName());
        assertNull(p.getEmployees());
    }

    @Test
    public void getProjectByIdWithEmployees() {
        doReturn(project2).when(service).getProject(2, true);
        var p = restTemplate.getForObject(getUrl("/projects/2?withEmployees=true"), ProjectDto.class);
        assertEquals(2, p.getId());
        assertEquals("TEST2", p.getName());
        assertNotNull(p.getEmployees());
    }

    @Test
    public void handleProjectNotFoundException() {
        doThrow(new ProjectNotFoundException(10)).when(service).getProject(anyInt(), anyBoolean());
        var p = restTemplate.getForEntity(getUrl("/projects/10"), ProjectDto.class);
        verify(service, times(1)).getProject(10, false);
        assertEquals(org.springframework.http.HttpStatus.NOT_FOUND, p.getStatusCode());
    }

    // TODO: and so on..

    private String getUrl(String path) {
        return "http://localhost:" + port + path;
    }

}
