package org.claumann.travelagency.service;

import org.claumann.travelagency.exception.DestinationNotFoundException;
import org.claumann.travelagency.model.Destination;
import org.claumann.travelagency.repository.DestinationRepository;
import org.claumann.travelagency.repository.entity.DestinationEntity;
import org.claumann.travelagency.repository.mapper.DestinationMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DestinationService {

    private final DestinationRepository destinationRepository;
    private final DestinationMapper destinationMapper;

    public DestinationService(DestinationRepository destinationRepository, DestinationMapper destinationMapper) {
        this.destinationRepository = destinationRepository;
        this.destinationMapper = destinationMapper;
    }

    public Destination create(final Destination destination) {
        var entity = destinationMapper.toEntity(destination);
        var savedEntity = destinationRepository.save(entity);
        return destinationMapper.toModel(savedEntity);
    }

    public List<Destination> findAll() {
        return destinationRepository.findAll().stream()
                .map(destinationMapper::toModel)
                .toList();
    }

    public List<Destination> search(final String name, final String location) {
        return destinationRepository.findByNameContainingIgnoreCaseOrLocationContainingIgnoreCase(name, location)
                .stream()
                .map(destinationMapper::toModel)
                .toList();
    }

    public Destination findById(final Long id) {
        var entity = getDestinationEntityById(id);
        return destinationMapper.toModel(entity);
    }

    public Destination rate(final Long id, final Double rating) {
        if (rating < 1 || rating > 10) {
            throw new IllegalArgumentException("Rating must be between 1 and 10.");
        }

        var entity = getDestinationEntityById(id);

        double currentAverageRating = entity.getAverageRating();
        Integer totalRatings = entity.getTotalRatings();

        double newAverageRating = ((currentAverageRating * totalRatings) + rating) / (totalRatings + 1);
        entity.setAverageRating(newAverageRating);
        entity.setTotalRatings(totalRatings + 1);

        var updatedEntity = destinationRepository.save(entity);
        return destinationMapper.toModel(updatedEntity);
    }

    public void delete(final Long id) {
        getDestinationEntityById(id);
        destinationRepository.deleteById(id);
    }

    private DestinationEntity getDestinationEntityById(Long id) {
        return destinationRepository.findById(id).orElseThrow(() -> new DestinationNotFoundException(id));
    }

}