package dev.example.demonet.grpc.service;

import static dev.example.demonet.service.model.Position.Manager;
import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;

import dev.example.demonet.grpc.model.GetProjectRequest;
import dev.example.demonet.grpc.model.GetProjectsRequest;
import dev.example.demonet.grpc.model.ProjectManagementServiceGrpc.ProjectManagementServiceBlockingStub;
import dev.example.demonet.service.ProjectManagementService;
import dev.example.demonet.service.model.Employee;
import dev.example.demonet.service.model.Project;
import net.devh.boot.grpc.client.inject.GrpcClient;

@SpringBootTest(properties = { "grpc.server.inProcessName=test", "grpc.server.port=-1",
        "grpc.client.inProcess.address=in-process:test" })
@DirtiesContext
public class ProjectManagementServiceImplTest {

    @GrpcClient("inProcess")
    private ProjectManagementServiceBlockingStub grpcService;

    @MockBean
    private ProjectManagementService service;

    private final Project project = new Project(1, "TEST");
    private final Employee employee = new Employee(1, "JOHN", "SMITH", Manager);

    @Test
    @DirtiesContext
    public void getProjectTest() {

        doReturn(project).when(service).getProject(1);
        doReturn(of(employee)).when(service).getEmployees(project.id());

        var request = GetProjectRequest.newBuilder().setId(1).build();
        var response = grpcService.getProject(request);
        assertNotNull(response);
        assertTrue(response.getEmployeesList().isEmpty());
        assertEquals(1, response.getId());
    }

    @Test
    @DirtiesContext
    public void getProjectWithEmployeesTest() {

        doReturn(project).when(service).getProject(1);
        doReturn(of(employee)).when(service).getEmployees(project.id());

        var request = GetProjectRequest.newBuilder().setId(1).setWithEmployees(true).build();
        var response = grpcService.getProject(request);
        assertNotNull(response);
        assertFalse(response.getEmployeesList().isEmpty());
        assertEquals(1, response.getId());
    }

    @Test
    @DirtiesContext
    public void getProjectsTest() {
        doReturn(of(project)).when(service).getProjects();
        doReturn(of(employee)).when(service).getEmployees(project.id());

        var request = GetProjectsRequest.newBuilder().build();
        var response = grpcService.getProjects(request);
        assertNotNull(response);

        while (response.hasNext()) {
            var project = response.next();
            assertEquals(1, project.getId());
            assertTrue(project.getEmployeesList().isEmpty());
        }
    }

    // TODO: and so forth..
}
