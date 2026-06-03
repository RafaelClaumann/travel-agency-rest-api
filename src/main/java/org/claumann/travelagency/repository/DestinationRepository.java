package org.claumann.travelagency.repository;

import org.claumann.travelagency.repository.entity.DestinationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DestinationRepository extends JpaRepository<DestinationEntity, Long> {

    List<DestinationEntity> findByNameContainingIgnoreCaseOrLocationContainingIgnoreCase(final String name, final String location);

}
