package matveyodintsov.api.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import matveyodintsov.api.converter.ProjectDtoConverter;
import matveyodintsov.api.dto.AckDto;
import matveyodintsov.api.dto.ProjectDto;
import matveyodintsov.api.exeptions.BadRequestException;
import matveyodintsov.api.exeptions.NotFoundException;
import matveyodintsov.store.entity.ProjectEntity;
import matveyodintsov.store.repository.ProjectRepositroy;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ProjectController {

    ProjectRepositroy repo;
    ProjectDtoConverter converter;

    public static final String FETCH_PROJECT = "/api/projects";
    public static final String CREATE_PROJECT = "/api/projects";
    public static final String EDIT_PROJECT = "/api/projects/{project_id}";
    public static final String DELETE_PROJECT = "/api/projects/{project_id}";
    public static final String CREATE_OR_UPDATE_PROJECT = "/api/projects";

    @GetMapping(FETCH_PROJECT)
    public List<ProjectDto> fetchProjects(
            @RequestParam(value = "prefix_name", required = false) Optional<String> optionalPrefixName
    ) {

        optionalPrefixName = optionalPrefixName.filter(prefixName -> !prefixName.trim().isEmpty());

        Stream<ProjectEntity> projectStream = optionalPrefixName
                .map(repo::streamAllByNameStartingWithIgnoreCase)
                .orElseGet(repo::streamAll);

        return projectStream
                .map(converter::makeProjectDto)
                .collect(Collectors.toList());

    }

    @PostMapping(CREATE_PROJECT)
    public ProjectDto createProject(
            @RequestParam(value = "project_name", required = false) String projectName
    ) {

        if (projectName == null || projectName.trim().isEmpty()) {
            throw new BadRequestException("Name can't be is empty.");
        }

        repo.findByName(projectName)
                .ifPresent(project -> {
                    throw new BadRequestException(
                            String.format(
                                    "Project \"%s\" already exist.",
                                    projectName
                            )
                    );
                });

        ProjectEntity project = repo.saveAndFlush(
                ProjectEntity.builder()
                        .name(projectName)
                        .build()
        );

        return converter.makeProjectDto(project);
    }

    @PutMapping(CREATE_OR_UPDATE_PROJECT)
    public ProjectDto createOrUpdateProject(
            @RequestParam(value = "project_id", required = false) Optional<Long> optionalProjectId,
            @RequestParam(value = "project_name", required = false) Optional<String> optionalProjectName
    ) {

        optionalProjectName = optionalProjectName.filter(projectName -> !projectName.trim().isEmpty());
        boolean isCreate = !optionalProjectId.isPresent();

        if (isCreate && !optionalProjectName.isPresent()) {
            throw new BadRequestException("Name can't be is empty.");
        }
        final ProjectEntity project = optionalProjectId
                .map(this::getProjectOrThrowException)
                .orElseGet(() -> ProjectEntity.builder().build());

        optionalProjectName
                .ifPresent(projectName -> {
                            repo.findByName(projectName)
                                    .filter(anotherProject -> !Objects.equals(project.getId(), anotherProject.getId()))
                                    .ifPresent(anotherProject -> {
                                        throw new BadRequestException(
                                                String.format(
                                                        "Project \"%s\" already exist.",
                                                        projectName
                                                )
                                        );
                                    });
                            project.setName(projectName);
                        }

                );

       final ProjectEntity savedProject = repo.saveAndFlush(project);

        return converter.makeProjectDto(savedProject);
    }

    @PatchMapping(EDIT_PROJECT)
    public ProjectDto editProject(
            @PathVariable("project_id") Long projectId,
            @RequestParam(value = "project_name", required = false) String projectName
    ) {

        if (projectName == null || projectName.trim().isEmpty()) {
            throw new BadRequestException("Name can't be is empty.");
        }

        ProjectEntity project = getProjectOrThrowException(projectId);


        repo.findByName(projectName)
                .filter(anotherProject -> !Objects.equals(projectId, anotherProject.getId()))
                .ifPresent(anotherProject -> {
                    throw new BadRequestException(
                            String.format(
                                    "Project \"%s\" already exist.",
                                    projectName
                            )
                    );
                });


        project.setName(projectName);
        project = repo.saveAndFlush(project);

        return converter.makeProjectDto(project);

    }

    @DeleteMapping(DELETE_PROJECT)
    public AckDto deleteProject(
            @PathVariable("project_id") Long projectId
    ) {

        getProjectOrThrowException(projectId);

        repo.deleteById(projectId);

        return AckDto.makeDefault(true);

    }

    private ProjectEntity getProjectOrThrowException(Long projectId) {
        return repo.findById(projectId)
                .orElseThrow(
                        () -> new NotFoundException(
                                String.format(
                                        "Project with \"%s\" doesn't exist.",
                                        projectId
                                )
                        )
                );
    }

}
