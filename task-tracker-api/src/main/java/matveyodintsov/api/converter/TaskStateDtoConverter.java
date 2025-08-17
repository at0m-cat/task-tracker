package matveyodintsov.api.converter;

import matveyodintsov.api.dto.TaskStateDto;
import matveyodintsov.store.entity.TaskStateEntity;
import org.springframework.stereotype.Component;

@Component
public class TaskStateDtoConverter {

    public TaskStateDto makeTaskStateDto(TaskStateEntity taskStateEntity) {

        return TaskStateDto.builder()
                .id(taskStateEntity.getId())
                .name(taskStateEntity.getName())
                .ordinal(taskStateEntity.getOrdinal())
                .createdAt(taskStateEntity.getCreatedAt())
                .build();
    }

}
