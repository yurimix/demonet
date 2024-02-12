package dev.example.demonet.ws.endpoint;

import static dev.example.demonet.service.model.Position.Manager;
import static dev.example.demonet.ws.config.WebServiceConfig.NAMESPACE;
import static java.util.List.of;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.ws.test.server.RequestCreators.withPayload;
import static org.springframework.ws.test.server.ResponseMatchers.noFault;
import static org.springframework.ws.test.server.ResponseMatchers.payload;
import static org.springframework.ws.test.server.ResponseMatchers.serverOrReceiverFault;
import static org.springframework.ws.test.server.ResponseMatchers.validPayload;
import static org.springframework.ws.test.server.ResponseMatchers.xpath;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.webservices.server.WebServiceServerTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.test.server.MockWebServiceClient;
import org.springframework.xml.transform.StringSource;

import dev.example.demonet.service.ProjectManagementService;
import dev.example.demonet.service.exception.ProjectNotFoundException;
import dev.example.demonet.service.model.Employee;
import dev.example.demonet.service.model.Project;

@WebServiceServerTest
class ProjectManagementEndpointTest {

    @Autowired
    private MockWebServiceClient client;

    @MockBean
    private ProjectManagementService service;

    private final Project project = new Project(1, "TEST");
    private final Employee employee = new Employee(1, "JOHN", "SMITH", Manager);
    private final Map<String, String> NAMESPACE_MAPPING = namespaceMappings();

    @Test
    void getProjectTest() throws IOException {
        doReturn(project).when(service).getProject(1);
        doReturn(of(employee)).when(service).getEmployees(project.id());

        StringSource request = new StringSource("""
                <gs:getProjectRequest xmlns:gs="%s">
                    <gs:id>1</gs:id>
                    <gs:withEmployees>true</gs:withEmployees>
                </gs:getProjectRequest>""".formatted(NAMESPACE));

        StringSource response = new StringSource("""
                <gs:getProjectResponse xmlns:gs="%s">
                    <gs:project>
                        <gs:id>1</gs:id>
                        <gs:name>TEST</gs:name>
                        <gs:employees>
                            <gs:id>1</gs:id>
                            <gs:firstName>JOHN</gs:firstName>
                            <gs:lastName>SMITH</gs:lastName>
                            <gs:position>MANAGER</gs:position>
                        </gs:employees>
                    </gs:project>
                </gs:getProjectResponse>""".formatted(NAMESPACE));

        client.sendRequest(withPayload(request)).andExpect(noFault())
                .andExpect(validPayload(new ClassPathResource("ws/project-management.xsd")))
                .andExpect(payload(response))
                .andExpect(xpath("/gs:getProjectResponse/gs:project/gs:id", NAMESPACE_MAPPING).evaluatesTo("1"));
    }

    @Test
    void getProjectNotFoundTest() throws IOException {
        doThrow(new ProjectNotFoundException(1)).when(service).getProject(1);

        StringSource request = new StringSource("""
                <gs:getProjectRequest xmlns:gs="%s">
                    <gs:id>1</gs:id>
                    <gs:withEmployees>true</gs:withEmployees>
                </gs:getProjectRequest>""".formatted(NAMESPACE));

        client.sendRequest(withPayload(request)).andExpect(serverOrReceiverFault("Project not found"))
                .andExpect(xpath("/SOAP-ENV:Fault/detail/code", NAMESPACE_MAPPING).evaluatesTo("NOT_FOUND"));
    }

    private static Map<String, String> namespaceMappings() {
        var mappings = new HashMap<String, String>();
        mappings.put("gs", NAMESPACE);
        mappings.put("SOAP-ENV", "http://schemas.xmlsoap.org/soap/envelope/");
        return mappings;
    }

}
