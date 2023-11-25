package dev.example.demonet.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import dev.example.demonet.service.ProjectManagementService;
import dev.example.demonet.service.exception.DuplicateProjectException;
import dev.example.demonet.service.exception.EmployeeNotFoundException;
import dev.example.demonet.service.exception.ProjectNotFoundException;
import dev.example.demonet.service.model.Employee;
import dev.example.demonet.service.model.Position;
import dev.example.demonet.service.model.Project;

@Service("DummyProjectManagementService")
public class ProjectManagementServiceImpl implements ProjectManagementService {

    private Map<Integer, Project> projects;

    private Map<Integer, Employee> employees;

    private record Relation(Integer projectId, Integer employeeId) {
    }

    private List<Relation> relations;

    public ProjectManagementServiceImpl() {
        this.init();
    }

    @Override
    public Project addProject(Project project) throws DuplicateProjectException {
        if (this.projects.containsKey(project.id())) {
            throw new DuplicateProjectException("Duplicated project " + project.id(), project);
        }
        this.projects.put(project.id(), new Project(project.id(), project.name()));
        return project;
    }

    @Override
    public Project getProject(Integer id) throws ProjectNotFoundException {
        return Optional.ofNullable(this.projects.get(id))
                .orElseThrow(() -> new ProjectNotFoundException("Project not found: " + id));
    }

    @Override
    public List<Project> getProjects() {
        return getValues(this.projects, Project::id);
    }

    @Override
    public List<Project> getProjects(Integer employeeId) {
        return this.relations.stream()
                .filter(r -> r.employeeId().equals(employeeId))
                .map(Relation::projectId)
                .map(this.projects::get).toList();
    }

    @Override
    public List<Employee> getEmployees() {
        return getValues(this.employees, Employee::id);
    }

    @Override
    public List<Employee> getEmployees(Integer projectId) throws ProjectNotFoundException {
        return this.relations.stream()
                .filter(r -> r.projectId().equals(projectId))
                .map(Relation::employeeId)
                .map(this.employees::get).toList();
    }

    @Override
    public Employee getEmployee(Integer id) throws EmployeeNotFoundException {
        return Optional.ofNullable(this.employees.get(id))
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found: " + id));
    }

    @Override
    public boolean attachToProject(Integer projectId, Integer employeeId)
            throws EmployeeNotFoundException, ProjectNotFoundException {
        var project = getProject(projectId);
        var employee = getEmployee(employeeId);

        var relation = findRelation(employee.id(), project.id());
        if (relation.isPresent()) {
            return false;
        } else {
            this.relations.add(new Relation(project.id(), employee.id()));
            return true;
        }
    }

    private Optional<Relation> findRelation(Integer employeeId, Integer projectId) {
        return this.relations.stream()
                .filter(r -> r.projectId().equals(projectId) && r.employeeId().equals(employeeId))
                .findFirst();
    }

    @Override
    public boolean detachFromProject(Integer projectId, Integer employeeId)
            throws EmployeeNotFoundException, ProjectNotFoundException {
        Project project = getProject(projectId);
        Employee employee = getEmployee(employeeId);

        var relation = findRelation(employee.id(), project.id());
        if (relation.isPresent()) {
            this.relations.remove(relation.get());
            return true;
        } else {
            return false;
        }
    }

    private <T> List<T> getValues(Map<Integer, T> map, ToIntFunction<T> comparator) {
        return map.values().stream().sorted(Comparator.comparingInt(comparator)).toList();
    }

    private void init() {
        // dummy data initialization
        this.employees = Stream.of(new Employee[] {
                new Employee(1, "Raymond", "King", Position.Manager),
                new Employee(2, "Raul", "Simmons", Position.Developer),
                new Employee(3, "Jonathan", "Davidson", Position.Developer),
                new Employee(4, "Lillian", "Smith", Position.Developer),
                new Employee(5, "Kenneth", "Collier", Position.Developer),
                new Employee(6, "Mary", "Williams", Position.QA),
                new Employee(7, "Dennis", "Lowe", Position.QA) }
        ).collect(Collectors.toMap(Employee::id, p -> p));

        this.projects = Stream.of(new Project[] {
                new Project(1, "Data Sync"),
                new Project(2, "Cloud Connect"),
                new Project(3, "Smart Link") }
        ).collect(Collectors.toMap(Project::id, p -> p));

        this.relations = new ArrayList<>(
                Arrays.asList(
                        new Relation(1, 1),
                        new Relation(1, 2),
                        new Relation(1, 3),
                        new Relation(1, 6),

                        new Relation(2, 1),
                        new Relation(2, 4),
                        new Relation(2, 5),
                        new Relation(2, 7),

                        new Relation(3, 1),
                        new Relation(3, 2),
                        new Relation(3, 5),
                        new Relation(3, 6),
                        new Relation(3, 7))
                );
    }

}
