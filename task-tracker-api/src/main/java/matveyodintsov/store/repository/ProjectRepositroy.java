package matveyodintsov.store.repository;

import matveyodintsov.store.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepositroy extends JpaRepository<ProjectEntity, Long> {
}
