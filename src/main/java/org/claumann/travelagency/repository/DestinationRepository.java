package org.claumann.travelagency.repository;

import org.claumann.travelagency.entity.Destination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DestinationRepository extends JpaRepository<Destination, Long> {

    List<Destination> findByNameContainingIgnoreCaseOrLocationContainingIgnoreCase(final String name, final String location);

}
