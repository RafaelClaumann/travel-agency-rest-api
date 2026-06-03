package org.claumann.travelagency.repository.mapper;

import org.claumann.travelagency.model.Destination;
import org.claumann.travelagency.repository.entity.DestinationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DestinationMapper {

    Destination toModel(final DestinationEntity entity);

    DestinationEntity toEntity(final Destination model);

}
