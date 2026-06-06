package org.claumann.travelagency.service;

import org.claumann.travelagency.model.Destination;
import org.claumann.travelagency.repository.DestinationRepository;
import org.claumann.travelagency.repository.mapper.DestinationMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

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
        var entity = destinationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Destination with ID " + id + " not found"));
        return destinationMapper.toModel(entity);
    }

    public Destination rate(final Long id, final Double rating) {
        if (rating < 1 || rating > 10) {
            throw new IllegalArgumentException("Rating must be between 1 and 10.");
        }

        var entity = destinationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Destination with ID " + id + " not found"));

        double currentAverageRating = entity.getAverageRating();
        Integer totalRatings = entity.getTotalRatings();

        double newAverageRating = ((currentAverageRating * totalRatings) + rating) / (totalRatings + 1);
        entity.setAverageRating(newAverageRating);
        entity.setTotalRatings(totalRatings + 1);

        var updatedEntity = destinationRepository.save(entity);
        return destinationMapper.toModel(updatedEntity);
    }

    /**
     * Exclui um destino pelo ID.
     * <p>
     * PASSO A PASSO:
     * 1. Verifique se o destino existe usando findById() do repository.
     * Lance DestinationNotFoundException se não encontrar.
     * 2. Delete o destino usando destinationRepository.deleteById(id).
     * <p>
     * DICA: Sempre verifique se o recurso existe antes de deletar.
     * O deleteById() do JpaRepository não lança exceção se o ID não existir,
     * então sem essa verificação o DELETE de um ID inexistente retornaria 200 OK
     * em vez de 404 Not Found — o que seria incorreto.
     * <p>
     * BOAS PRÁTICAS:
     * - Métodos de delete geralmente retornam void.
     * - O controller deve retornar HTTP 204 No Content para deleções bem-sucedidas.
     */
    public void delete(final Long id) {
        throw new UnsupportedOperationException("Implemente a exclusão de destino.");
    }

}