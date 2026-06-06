# Travel Agency REST API

API RESTful desenvolvida com Java e Spring Boot para gerenciamento de destinos de viagem.

---

## 🛠️ Stack

- Java 21
- Spring Boot 4.0.6
- Spring Data JPA + Hibernate
- H2 Database (em memória)
- MapStruct
- Lombok
- Jakarta Validation

---

## ▶️ Como executar

```bash
./gradlew bootRun
```

A aplicação sobe em `http://localhost:8080`.

O banco é inicializado automaticamente com 5 destinos e 5 pacotes de viagem via `data.sql`.

Console H2 disponível em `http://localhost:8080/h2-console`:

- JDBC URL: `jdbc:h2:mem:traveldb`
- Usuário: `sa`
- Senha: *(vazio)*

---

## 📦 Estrutura do projeto

```
src/main/java/org/claumann/travelagency/
├── controller/
│   └── DestinationController.java
├── service/
│   └── DestinationService.java
├── repository/
│   ├── DestinationRepository.java
│   └── entity/
│       ├── DestinationEntity.java
│       └── TravelPackageEntity.java
├── repository/mapper/
│   └── DestinationMapper.java
├── model/
│   └── Destination.java
├── dto/
│   ├── DestinationRequest.java
│   └── RatingRequest.java
└── exception/
    ├── DestinationNotFoundException.java
    ├── RestExceptionHandler.java
    └── dto/
        └── ErrorResponse.java
```

---

## 🗄️ Modelo de dados

### Destination

| Campo | Tipo | Descrição |
|---|---|---|
| `id` | `Long` | Identificador único gerado automaticamente |
| `name` | `String` | Nome do destino |
| `location` | `String` | Localização geográfica |
| `description` | `String` | Descrição do destino |
| `averageRating` | `Double` | Média das avaliações (calculada pelo sistema) |
| `totalRatings` | `Integer` | Total de avaliações recebidas (gerenciado pelo sistema) |

> `averageRating` e `totalRatings` são gerenciados internamente — não devem ser enviados pelo cliente.

---

## 📡 Endpoints

Base URL: `http://localhost:8080/destination`

---

### 1. Cadastrar destino

```
POST /destination
```

**Body:**

```json
{
  "name": "Gramado",
  "location": "Rio Grande do Sul, Brasil",
  "description": "Cidade serrana com arquitetura europeia e clima frio."
}
```

| Campo | Tipo | Obrigatório |
|---|---|---|
| `name` | `String` | ✅ |
| `location` | `String` | ✅ |
| `description` | `String` | ❌ |

**Respostas:**

| Status | Descrição |
|---|---|
| `201 Created` | Destino criado com sucesso |
| `422 Unprocessable Entity` | Campo obrigatório ausente ou em branco |

---

### 2. Listar todos os destinos

```
GET /destination
```

**Respostas:**

| Status | Descrição |
|---|---|
| `200 OK` | Lista de destinos (vazia se não houver nenhum) |

---

### 3. Pesquisar destinos

```
GET /destination/search?name={name}&location={location}
```

| Parâmetro | Tipo | Obrigatório | Descrição |
|---|---|---|---|
| `name` | `String` | ❌ | Filtra por nome (parcial, case-insensitive) |
| `location` | `String` | ❌ | Filtra por localização (parcial, case-insensitive) |

> Os parâmetros são independentes — é possível usar um, outro ou ambos. Sem parâmetros, retorna todos os destinos.

**Respostas:**

| Status | Descrição |
|---|---|
| `200 OK` | Lista de destinos encontrados (vazia se nenhum corresponder) |

---

### 4. Buscar destino por ID

```
GET /destination/{id}
```

**Respostas:**

| Status | Descrição |
|---|---|
| `200 OK` | Destino encontrado |
| `404 Not Found` | Destino não encontrado |

---

### 5. Avaliar destino

```
PATCH /destination/{id}/rating
```

**Body:**

```json
{
  "rating": 9.5
}
```

| Campo | Tipo | Regra |
|---|---|---|
| `rating` | `Double` | Obrigatório, entre `1.0` e `10.0` |

A nova média é calculada com base nas avaliações anteriores:

```
novaMedia = ((mediaAtual * totalAvaliacoes) + novaNota) / (totalAvaliacoes + 1)
```

**Respostas:**

| Status | Descrição |
|---|---|
| `200 OK` | Avaliação registrada e média atualizada |
| `404 Not Found` | Destino não encontrado |
| `422 Unprocessable Entity` | Nota ausente ou fora do range permitido |

---

### 6. Excluir destino

```
DELETE /destination/{id}
```

**Respostas:**

| Status | Descrição |
|---|---|
| `204 No Content` | Destino excluído com sucesso |
| `404 Not Found` | Destino não encontrado |

---

## ❌ Formato de erro

Todos os erros retornam o seguinte formato:

```json
{
  "timestamp": "2026-06-05T21:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Destination not found with id: 999",
  "path": "/destination/999"
}
```

---

## 🧪 Testando com curl

Execute o script de testes:

```bash
chmod +x requests_curl.sh
./requests_curl.sh
```

### POST /destination — Cadastrar destino com todos os campos

```bash
curl -s -X POST http://localhost:8080/destination \
  -H "Content-Type: application/json" \
  -d '{"name": "Gramado", "location": "Rio Grande do Sul, Brasil", "description": "Cidade serrana com arquitetura europeia e clima frio."}'
```

### POST /destination — Cadastrar destino sem description (campo opcional, esperado 201)

```bash
curl -s -X POST http://localhost:8080/destination \
  -H "Content-Type: application/json" \
  -d '{"name": "Bonito", "location": "Mato Grosso do Sul, Brasil"}'
```

### POST /destination — Cadastrar destino sem name (campo obrigatório, esperado 422)

```bash
curl -s -X POST http://localhost:8080/destination \
  -H "Content-Type: application/json" \
  -d '{"location": "Rio de Janeiro, Brasil", "description": "Sem nome informado."}'
```

### POST /destination — Cadastrar destino sem location (campo obrigatório, esperado 422)

```bash
curl -s -X POST http://localhost:8080/destination \
  -H "Content-Type: application/json" \
  -d '{"name": "Arraial do Cabo", "description": "Sem localização informada."}'
```

### POST /destination — Cadastrar destino com name em branco (esperado 422)

```bash
curl -s -X POST http://localhost:8080/destination \
  -H "Content-Type: application/json" \
  -d '{"name": "", "location": "Bahia, Brasil"}'
```

### GET /destination — Listar todos os destinos

```bash
curl -s -X GET http://localhost:8080/destination
```

### GET /destination/search?name=Gramado — Pesquisar por nome

```bash
curl -s -X GET "http://localhost:8080/destination/search?name=Gramado"
```

### GET /destination/search?location=Sul — Pesquisar por localização

```bash
curl -s -X GET "http://localhost:8080/destination/search?location=Sul"
```

### GET /destination/search?name=Gramado&location=Rio+Grande+do+Sul — Pesquisar por nome e localização

```bash
curl -s -X GET "http://localhost:8080/destination/search?name=Gramado&location=Rio+Grande+do+Sul"
```

### GET /destination/search — Pesquisar sem parâmetros (esperado retornar todos)

```bash
curl -s -X GET "http://localhost:8080/destination/search"
```

### GET /destination/1 — Buscar destino por ID existente (esperado 200)

```bash
curl -s -X GET http://localhost:8080/destination/1
```

### GET /destination/999 — Buscar destino por ID inexistente (esperado 404)

```bash
curl -s -X GET http://localhost:8080/destination/999
```

### PATCH /destination/1/rating — Avaliar destino com nota válida (esperado 200)

```bash
curl -s -X PATCH http://localhost:8080/destination/1/rating \
  -H "Content-Type: application/json" \
  -d '{"rating": 9.5}'
```

### PATCH /destination/1/rating — Nota acima do máximo (esperado 422)

```bash
curl -s -X PATCH http://localhost:8080/destination/1/rating \
  -H "Content-Type: application/json" \
  -d '{"rating": 11}'
```

### PATCH /destination/1/rating — Nota abaixo do mínimo (esperado 422)

```bash
curl -s -X PATCH http://localhost:8080/destination/1/rating \
  -H "Content-Type: application/json" \
  -d '{"rating": 0}'
```

### PATCH /destination/1/rating — Sem rating no body (esperado 422)

```bash
curl -s -X PATCH http://localhost:8080/destination/1/rating \
  -H "Content-Type: application/json" \
  -d '{}'
```

### PATCH /destination/999/rating — Avaliar destino inexistente (esperado 404)

```bash
curl -s -X PATCH http://localhost:8080/destination/999/rating \
  -H "Content-Type: application/json" \
  -d '{"rating": 8}'
```

### DELETE /destination/1 — Excluir destino existente (esperado 204)

```bash
curl -s -X DELETE http://localhost:8080/destination/1
```

### DELETE /destination/999 — Excluir destino inexistente (esperado 404)

```bash
curl -s -X DELETE http://localhost:8080/destination/999
```
