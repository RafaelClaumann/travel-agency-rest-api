package org.claumann.travelagency.repository;

import org.claumann.travelagency.repository.entity.TravelPackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TravelPackageRepository extends JpaRepository<TravelPackageEntity, Long> {

}
