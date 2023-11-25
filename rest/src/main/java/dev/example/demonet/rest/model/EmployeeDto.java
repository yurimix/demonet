package dev.example.demonet.rest.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize
@Schema(name = "Employee")
public class EmployeeDto {

    @Schema(name = "id", example = "1", description = "User identifier")
    private int id;

    @JsonInclude(NON_NULL)
    @Schema(name = "firstName", example = "John", description = "User first name")
    private String firstName;

    @JsonInclude(NON_NULL)
    @Schema(name = "lastName", example = "Smith", description = "User last name")
    private String lastName;

    @JsonInclude(NON_NULL)
    @Schema(name = "position", example = "Manager", description = "One of 'Manager', 'Develolper' or 'QA'")
    private String position;

    @JsonInclude(NON_EMPTY)
    @Schema(name = "projects", description = "List of projects in which the user is involved")
    private List<ProjectDto> projects;

}
