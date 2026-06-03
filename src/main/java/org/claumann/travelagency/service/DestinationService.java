package org.claumann.travelagency.service;

import org.claumann.travelagency.model.Destination;
import org.claumann.travelagency.repository.DestinationRepository;
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

    /**
     * Cadastra um novo destino de viagem.
     * <p>
     * PASSO A PASSO:
     * 1. Receba o objeto Destination (model) como parâmetro.
     * 2. Converta o model para entity usando o destinationMapper.toEntity().
     * 3. Salve a entity no banco usando destinationRepository.save().
     * 4. Converta a entity salva de volta para model usando o destinationMapper.toModel().
     * 5. Retorne o model convertido.
     * <p>
     * DICA: O .save() do JpaRepository já retorna a entity salva com o ID gerado,
     * por isso convertemos de volta para model após salvar e não antes.
     * <p>
     * BOAS PRÁTICAS:
     * - O service nunca deve expor entities para fora — sempre retorne models.
     * - Não faça validações de negócio aqui ainda, por enquanto só salve.
     */
    public Destination create(final Destination destination) {
        throw new UnsupportedOperationException("Implemente o cadastro de destino.");
    }

    /**
     * Retorna todos os destinos cadastrados.
     * <p>
     * PASSO A PASSO:
     * 1. Busque todos os destinos usando destinationRepository.findAll().
     * 2. Converta a lista de entities para uma lista de models.
     * DICA: Use stream() + map() + toList() para converter a lista inteira de uma vez.
     * Exemplo: lista.stream().map(destinationMapper::toModel).toList()
     * 3. Retorne a lista de models.
     * <p>
     * BOAS PRÁTICAS:
     * - Nunca retorne uma lista de entities diretamente — sempre converta para model.
     * - Se não houver destinos, retorne uma lista vazia, nunca null.
     * O findAll() já faz isso por padrão.
     */
    public List<Destination> findAll() {
        throw new UnsupportedOperationException("Implemente a listagem de destinos.");
    }

    /**
     * Pesquisa destinos por nome ou localização.
     * <p>
     * PASSO A PASSO:
     * 1. Use o método customizado do repository:
     * destinationRepository.findByNameContainingIgnoreCaseOrLocationContainingIgnoreCase(name, location)
     * 2. Converta a lista de entities para models (mesmo esquema do findAll).
     * 3. Retorne a lista de models.
     * <p>
     * DICA: O método do repository já faz a pesquisa case-insensitive e parcial,
     * ou seja, pesquisar "rio" vai encontrar "Rio de Janeiro".
     * <p>
     * BOAS PRÁTICAS:
     * - Não filtre a lista manualmente aqui no service — deixe o banco fazer o trabalho.
     * - Se nenhum resultado for encontrado, retorne lista vazia (o repository já faz isso).
     */
    public List<Destination> search(final String name, final String location) {
        throw new UnsupportedOperationException("Implemente a pesquisa de destinos.");
    }

    /**
     * Retorna um destino específico pelo ID.
     * <p>
     * PASSO A PASSO:
     * 1. Use destinationRepository.findById(id) para buscar o destino.
     * Ele retorna um Optional<DestinationEntity>.
     * 2. Se não encontrar, lance uma exceção:
     * throw new DestinationNotFoundException(id)
     * 3. Se encontrar, converta a entity para model e retorne.
     * <p>
     * DICA: O Optional tem um método chamado orElseThrow() que faz exatamente isso:
     * retorna o valor se presente ou lança uma exceção se vazio.
     * Exemplo: repository.findById(id).orElseThrow(() -> new DestinationNotFoundException(id))
     * <p>
     * BOAS PRÁTICAS:
     * - Nunca retorne null quando não encontrar — sempre lance uma exceção.
     * - A exceção deve ser do tipo RuntimeException para o Spring conseguir capturá-la
     * automaticamente no @RestControllerAdvice.
     */
    public Destination findById(final Long id) {
        throw new UnsupportedOperationException("Implemente a busca por ID.");
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
