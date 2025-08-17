package matveyodintsov.store.repository;

import matveyodintsov.store.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface ProjectRepositroy extends JpaRepository<ProjectEntity, Long> {

    Optional<ProjectEntity> findByName(String name);

    Stream<ProjectEntity> streamAllBy();

    Stream<ProjectEntity> streamAllByNameStartingWithIgnoreCase(String prefixName);

}
