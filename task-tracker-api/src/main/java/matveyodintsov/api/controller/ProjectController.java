package matveyodintsov.api.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import matveyodintsov.api.converter.ProjectDtoConverter;
import matveyodintsov.api.dto.ProjectDto;
import matveyodintsov.api.exeptions.BadRequestException;
import matveyodintsov.store.entity.ProjectEntity;
import matveyodintsov.store.repository.ProjectRepositroy;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ProjectController {

    ProjectRepositroy projectRepositroy;
    ProjectDtoConverter projectDtoConverter;

    public static final String CREATE_PROJECT = "/api/projects";

    @PostMapping(CREATE_PROJECT)
    public ProjectDto createProject(@RequestParam(required = false) String name) {

        if (name == null || name.trim().isEmpty()) {
            throw new BadRequestException("Name can't be is empty.");
        }

        projectRepositroy
                .findByName(name)
                .ifPresent(project -> {
                    throw new BadRequestException(String.format("Project \"%s\" already exist.", name));
                });

        ProjectEntity project = projectRepositroy.saveAndFlush(

                ProjectEntity.builder()
                        .name(name)
                        .build()

        );

        return projectDtoConverter.makeProjectDto(project);
    }

}
