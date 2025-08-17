package matveyodintsov.store.repository;

import matveyodintsov.store.entity.TaskStateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskStateRepository extends JpaRepository<TaskStateEntity, Long> {
}
