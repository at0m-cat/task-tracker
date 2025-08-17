package matveyodintsov.store.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "task_states")
public class TaskStateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true)
    String name;

    Long ordinal;

    @Builder.Default
    Instant createdAt = Instant.now();

    @Builder.Default
    @OneToMany
    @JoinColumn(name = "task_id", referencedColumnName = "id")
    List<TaskEntity> taskEntity = new ArrayList<>();

}
