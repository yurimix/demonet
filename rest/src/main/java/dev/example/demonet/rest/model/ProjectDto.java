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
@Schema(name = "Project")
public class ProjectDto {

    @Schema(name = "id", example = "1", description = "Project identifier")
    private int id;

    @JsonInclude(NON_NULL)
    @Schema(name = "name", example = "Smart Link", description = "Project name")
    private String name;

    @JsonInclude(NON_EMPTY)
    @Schema(name = "employees", description = "List of project employees")
    private List<EmployeeDto> employees;
}
