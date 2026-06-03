package org.claumann.travelagency.repository.mapper;

import org.claumann.travelagency.model.TravelPackage;
import org.claumann.travelagency.repository.entity.TravelPackageEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TravelPackageMapper {

    TravelPackage toModel(final TravelPackageEntity entity);

    TravelPackageEntity toEntity(final TravelPackage model);

}
