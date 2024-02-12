package dev.example.demonet.service.impl;

import java.util.stream.Stream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import dev.example.demonet.service.ProjectManagementService;
import dev.example.demonet.service.model.Employee;
import dev.example.demonet.service.model.Position;
import dev.example.demonet.service.model.Project;

@Component
public class DataLoader implements CommandLineRunner{

    private final ProjectManagementService service;

    public DataLoader(ProjectManagementService service) {
        this.service = service;
    }

    @Override
    public void run(String... args) throws Exception {
        Stream.of(new Employee[] {
                new Employee(1, "Raymond", "King", Position.Manager),
                new Employee(2, "Raul", "Simmons", Position.Developer),
                new Employee(3, "Jonathan", "Davidson", Position.Developer),
                new Employee(4, "Lillian", "Smith", Position.Developer),
                new Employee(5, "Kenneth", "Collier", Position.Developer),
                new Employee(6, "Mary", "Williams", Position.QA),
                new Employee(7, "Dennis", "Lowe", Position.QA) }
        ).forEach(this.service::addEmployee);

        Stream.of(new Project[] {
                new Project(1, "Data Sync"),
                new Project(2, "Cloud Connect"),
                new Project(3, "Smart Link") }
        ).forEach(this.service::addProject);

        this.service.attachToProject(1, 1);
        this.service.attachToProject(1, 2);
        this.service.attachToProject(1, 3);
        this.service.attachToProject(1, 6);

        this.service.attachToProject(2, 1);
        this.service.attachToProject(2, 4);
        this.service.attachToProject(2, 5);
        this.service.attachToProject(2, 7);

        this.service.attachToProject(3, 1);
        this.service.attachToProject(3, 2);
        this.service.attachToProject(3, 5);
        this.service.attachToProject(3, 6);
        this.service.attachToProject(3, 7);
    }

}
