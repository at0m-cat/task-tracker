package matveyodintsov.api.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import matveyodintsov.api.dto.AckDto;
import matveyodintsov.api.dto.ProjectDto;
import matveyodintsov.api.exeptions.BadRequestException;
import matveyodintsov.api.service.ProjectService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ProjectController {

    ProjectService service;

    public static final String FETCH_PROJECT = "/api/projects";
    public static final String CREATE_PROJECT = "/api/projects";
    public static final String EDIT_PROJECT = "/api/projects/{project_id}";
    public static final String DELETE_PROJECT = "/api/projects/{project_id}";

    @GetMapping(FETCH_PROJECT)
    public List<ProjectDto> fetchProjects(
            @RequestParam(value = "prefix_name", required = false) Optional<String> optionalPrefixName
    ) {

        optionalPrefixName = optionalPrefixName.filter(prefixName -> !prefixName.trim().isEmpty());

        return service.fetchProject(optionalPrefixName);

    }

    @PostMapping(CREATE_PROJECT)
    public ProjectDto createProject(
            @RequestParam(value = "project_name", required = false) String projectName
    ) {

        if (projectName == null || projectName.trim().isEmpty()) {
            throw new BadRequestException("Name can't be is empty.");
        }

        return service.createProject(projectName);
    }

    @PatchMapping(EDIT_PROJECT)
    public ProjectDto editProject(
            @PathVariable("project_id") Long projectId,
            @RequestParam(value = "project_name", required = false) String projectName
    ) {

        if (projectName == null || projectName.trim().isEmpty()) {
            throw new BadRequestException("Name can't be is empty.");
        }

        return service.editProject(projectId, projectName);

    }

    @DeleteMapping(DELETE_PROJECT)
    public AckDto deleteProject(
            @PathVariable("project_id") Long projectId
    ) {

        service.deleteProject(projectId);
        return AckDto.makeDefault(true);

    }

}
