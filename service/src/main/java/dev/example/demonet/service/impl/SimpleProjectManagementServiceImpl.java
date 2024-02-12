package dev.example.demonet.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.ToIntFunction;

import org.springframework.stereotype.Service;

import dev.example.demonet.service.ProjectManagementService;
import dev.example.demonet.service.exception.EntityException;
import dev.example.demonet.service.exception.EmployeeDuplicateException;
import dev.example.demonet.service.exception.EmployeeNotFoundException;
import dev.example.demonet.service.exception.ProjectDuplicateException;
import dev.example.demonet.service.exception.ProjectNotFoundException;
import dev.example.demonet.service.model.Employee;
import dev.example.demonet.service.model.Project;

@Service
public class SimpleProjectManagementServiceImpl implements ProjectManagementService {

    private Map<Integer, Project> projects = new HashMap<>();
    private Map<Integer, Employee> employees = new HashMap<>();
    private List<Relation> relations = new ArrayList<>();

    private record Relation(Integer projectId, Integer employeeId) {}

    @Override
    public Project addProject(Project project) throws ProjectDuplicateException {
        if (this.projects.containsKey(project.id())) {
            throw new ProjectDuplicateException(project.id());
        }
        this.projects.put(project.id(), project);
        return project;
    }

    @Override
    public Project getProject(Integer id) throws ProjectNotFoundException {
        return Optional.ofNullable(this.projects.get(id))
                .orElseThrow(() -> new ProjectNotFoundException(id));
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
    public Employee addEmployee(Employee employee) throws EmployeeDuplicateException {
        if (this.employees.containsKey(employee.id())) {
            throw new EmployeeDuplicateException(employee.id());
        }
        this.employees.put(employee.id(), employee);
        return employee;
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
                .orElseThrow(() -> new EmployeeNotFoundException(id));
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

}
