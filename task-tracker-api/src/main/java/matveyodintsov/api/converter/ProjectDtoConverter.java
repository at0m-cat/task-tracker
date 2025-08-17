package matveyodintsov.api.converter;

import matveyodintsov.api.dto.ProjectDto;
import matveyodintsov.store.entity.ProjectEntity;
import org.springframework.stereotype.Component;

@Component
public class ProjectDtoConverter {

    public ProjectDto makeProjectDto(ProjectEntity projectEntity) {

        return ProjectDto.builder()
                .id(projectEntity.getId())
                .name(projectEntity.getName())
                .createdAt(projectEntity.getCreatedAt())
                .updatedAt(projectEntity.getUpdatedAt())
                .build();
    }

}
