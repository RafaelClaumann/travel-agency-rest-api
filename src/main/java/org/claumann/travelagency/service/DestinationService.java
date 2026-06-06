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

    /**
     * Avalia um destino com uma nota de 1 a 10 e recalcula a média.
     * <p>
     * PASSO A PASSO:
     * 1. Busque a entity do destino usando o findById() do repository (não do service).
     * Lance DestinationNotFoundException se não encontrar.
     * 2. Calcule a nova média usando a fórmula:
     * novaMedia = ((mediaAtual * totalAvaliacoes) + novaNota) / (totalAvaliacoes + 1)
     * 3. Atualize o averageRating e incremente o totalRatings na entity.
     * 4. Salve a entity atualizada com repository.save().
     * 5. Converta para model e retorne.
     * <p>
     * DICA: A fórmula garante que a média seja recalculada corretamente sem precisar
     * guardar todas as notas individualmente. Exemplo:
     * - Média atual: 8.0, total: 2 avaliações, nova nota: 10
     * - Nova média: ((8.0 * 2) + 10) / 3 = 26 / 3 = 8.67
     * <p>
     * BOAS PRÁTICAS:
     * - Valide se a nota está entre 1 e 10 antes de calcular.
     * Lance uma IllegalArgumentException se estiver fora do range.
     * - Busque sempre a entity diretamente do banco antes de atualizar,
     * nunca confie em dados vindos do request para sobrescrever tudo.
     */
    public Destination rate(final Long id, final Double rating) {
        throw new UnsupportedOperationException("Implemente a avaliação de destino.");
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