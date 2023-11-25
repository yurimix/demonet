# DemoNET
Several demo applications for Java network server services based on Spring Boot

## Data model

All demo projects use the same data model which is described as:

![](/.img/model.png)

Each employee can participate in many projects and each project can involve many employees (many-to-many relations):

![](/.img/relations.png)

A service that provides functionality with the described data model corresponds to the interface:
```java
package dev.example.demonet.service;

import java.util.List;

import dev.example.demonet.service.exception.DuplicateProjectException;
import dev.example.demonet.service.exception.EmployeeNotFoundException;
import dev.example.demonet.service.exception.ProjectNotFoundException;
import dev.example.demonet.service.model.Employee;
import dev.example.demonet.service.model.Project;

public interface ProjectManagementService {

    Project addProject(Project project) throws DuplicateProjectException;

    Project getProject(Integer id) throws ProjectNotFoundException;

    List<Project> getProjects();

    List<Project> getProjects(Integer employeeId);

    Employee getEmployee(Integer id) throws EmployeeNotFoundException;

    List<Employee> getEmployees();

    List<Employee> getEmployees(Integer projectId) throws ProjectNotFoundException;

    boolean attachToProject(Integer projectId, Integer employeeId)
            throws EmployeeNotFoundException, ProjectNotFoundException;

    boolean detachFromProject(Integer projectId, Integer employeeId)
            throws EmployeeNotFoundException, ProjectNotFoundException;
}

```

Accordingly, the functionality of all demo projects is based on this interface.

## Requirements
* JDK 17+
* Maven
* ... desire to learn

## How to build
Run maven in the project root
```
mvn clean package
```
## How to start/stop demo project
To start any demo project goto inside the corresponding directory and perform
```
mvn spring-boot:start
```
For example, to run `WS` project you need to go to into `ws` directory first.

To stop demo perform the following command
```
mvn spring-boot:stop
```
in the corresponding directory.

## How to play
### REST
Swagger is integrated into the project, so there is no need to use any additional tools,  
just open URL <http://localhost:8080/swagger-ui/index.html> to play with demo:

![](/.img/rest.png)

### GraphQL
To play with GraphQL, you need to use any client that implements this technology,  
for example, I use the Altair browser extension:

![](/.img/graphql.png)

### GRPC
[BloomRPC](https://appimage.github.io/BloomRPC/) tool is perfectly suited for working with GRPC services,   
it has a very simple UI which not needed to describe:

![](/.img/grpc.png)

### WS
The last technology in this brief is WEB Services. Now you won't see it used much compared to REST or even GRPS, but...  
To see, how it works, perform the following request:

```
curl -i --header "content-type: text/xml" -d @ws/request.xml http://localhost:8080/ws
```

## Conclusion
All the technologies considered have the right to life for building client-server applications.    
But from my point of view:
* REST is the de facto standard today because it is used most frequently
* GRPC is the fastest protocol, among other things, it also supports stream processing, which makes it the best solution for inter-service communications
* GraphQL is a relatively new and elegant solution, but perhaps not the fastest
* WS is the founder of everything, somewhat difficult both from the point of view of definition and from the point of view of data exchange since it actively uses XML

(C) Yuri
