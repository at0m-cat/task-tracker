package matveyodintsov.api.converter;

import matveyodintsov.api.dto.TaskDto;
import matveyodintsov.store.entity.TaskEntity;
import org.springframework.stereotype.Component;

@Component
public class TaskDtoConverter {

    public TaskDto makeTaskDto(TaskEntity taskEntity) {

        return TaskDto.builder()
                .id(taskEntity.getId())
                .name(taskEntity.getName())
                .createdAt(taskEntity.getCreatedAt())
                .description(taskEntity.getDescription())
                .build();
    }

}
