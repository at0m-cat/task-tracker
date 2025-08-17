package matveyodintsov.api.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import matveyodintsov.api.converter.ProjectDtoConverter;
import matveyodintsov.api.dto.ProjectDto;
import matveyodintsov.api.exeptions.BadRequestException;
import matveyodintsov.api.exeptions.NotFoundException;
import matveyodintsov.store.entity.ProjectEntity;
import matveyodintsov.store.repository.ProjectRepositroy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Transactional
public class ProjectService {

    ProjectRepositroy repo;
    ProjectDtoConverter converter;

    @Transactional(readOnly = true)
    public List<ProjectDto> fetchProject(Optional<String> prefixName) {
        Stream<ProjectEntity> projectStream = prefixName
                .map(repo::streamAllByNameStartingWithIgnoreCase)
                .orElseGet(repo::streamAllBy);

        return projectStream
                .map(converter::makeProjectDto)
                .collect(Collectors.toList());
    }

    public ProjectDto createProject(String projectName) {
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

    public ProjectDto editProject(Long projectId, String projectName) {
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

    public void deleteProject(Long projectId) {
        getProjectOrThrowException(projectId);
        repo.deleteById(projectId);

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
