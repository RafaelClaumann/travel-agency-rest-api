package org.claumann.travelagency.repository;

import org.claumann.travelagency.repository.entity.DestinationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DestinationRepository extends JpaRepository<DestinationEntity, Long> {

    @Query("SELECT d FROM DestinationEntity d WHERE " +
            "(:name IS NULL OR LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:location IS NULL OR LOWER(d.location) LIKE LOWER(CONCAT('%', :location, '%')))")
    List<DestinationEntity> searchByNameOrLocation(@Param("name") String name, @Param("location") String location);

}
