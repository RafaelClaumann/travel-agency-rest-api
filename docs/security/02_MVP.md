# Etapa 1 — MVP: Autenticação JWT básica

Plano detalhado para implementação de autenticação JWT na Travel Agency API.

---

## Objetivo

Proteger todos os endpoints da API, permitindo acesso apenas a usuários autenticados.
O cliente autentica via `POST /auth/login`, recebe um token JWT e o envia no header
`Authorization: Bearer` nas requisições seguintes. O servidor não armazena sessão —
cada requisição é validada de forma independente através do token.

---

## Dependências

```groovy
implementation 'org.springframework.boot:spring-boot-starter-security'
implementation 'org.springframework.boot:spring-boot-starter-oauth2-resource-server'
```

O `oauth2-resource-server` já inclui suporte nativo a JWT sem necessidade de bibliotecas
externas como `jjwt`. O Spring Boot configura automaticamente o `JwtDecoder` e o
`BearerTokenAuthenticationFilter` quando essa dependência está presente.

---

## application.properties

```properties
# Chave secreta usada para assinar e validar os tokens JWT
api.security.token.secret=travel-agency-secret-key

# Expiração em segundos — 28800 = 8 horas
api.security.token.expiration=28800

# CORS — origem permitida
api.security.cors.allowed-origin=http://192.168.1.10:3000
```

> Em produção, a chave secreta deve ser externalizada para uma variável de ambiente,
> nunca commitada no repositório.

---

## Banco de dados

### Nova tabela: `users`

| Campo | Tipo | Restrições |
|---|---|---|
| `id` | `Long` | PK, gerada automaticamente |
| `username` | `String` | único, não nulo |
| `password` | `String` | hash bcrypt, não nulo |
| `role` | `String` | `ADMIN` ou `USER`, não nulo |

### Seed no data.sql

A senha precisa ser inserida já como hash bcrypt. O `data.sql` é executado diretamente
no banco — o Spring não processa o valor pelo `PasswordEncoder` nessa etapa.

```sql
-- senha: admin123
INSERT INTO users (username, password, role) VALUES
('admin', '$2a$10$...hashbcrypt...', 'ADMIN');

-- senha: user123
INSERT INTO users (username, password, role) VALUES
('user', '$2a$10$...hashbcrypt...', 'USER');
```

> Para gerar o hash bcrypt, crie um teste ou um `main` temporário:
> `System.out.println(new BCryptPasswordEncoder().encode("suasenha"));`

---

## Componentes

### UserEntity

Entidade JPA mapeada para a tabela `users`. Implementa a interface `UserDetails` do
Spring Security para integração direta com o mecanismo de autenticação, evitando a
necessidade de uma classe adaptadora separada.

Anotações necessárias: `@Entity`, `@Table(name = "users")`, `@Getter`, `@NoArgsConstructor`.

Campos: `id`, `username`, `password`, `role`.

Métodos da interface `UserDetails` a implementar:

| Método | Retorno | Descrição |
|---|---|---|
| `getUsername()` | `String` | Retorna o username |
| `getPassword()` | `String` | Retorna o hash da senha |
| `getAuthorities()` | `Collection<GrantedAuthority>` | Retorna a role como `SimpleGrantedAuthority` |
| `isAccountNonExpired()` | `true` | Sem controle de expiração de conta por enquanto |
| `isAccountNonLocked()` | `true` | Sem controle de bloqueio por enquanto |
| `isCredentialsNonExpired()` | `true` | Sem controle de expiração de credenciais por enquanto |
| `isEnabled()` | `true` | Sem controle de ativação por enquanto |

---

### UserRepository

Estende `JpaRepository<UserEntity, Long>` com um método customizado para busca por username:

```java
Optional<UserEntity> findByUsername(String username);
```

O Spring Data gera a query automaticamente pelo nome do método — sem necessidade de JPQL.

---

### UserDetailsServiceImpl

Implementa a interface `UserDetailsService` do Spring Security. É o ponto de integração
entre o Spring Security e o banco de dados — o Spring chama esse serviço automaticamente
durante o processo de autenticação.

Responsabilidade única: dado um `username`, buscar o usuário no banco via `UserRepository`
e retorná-lo como `UserDetails`.

Fluxo do método `loadUserByUsername(String username)`:
1. Chama `userRepository.findByUsername(username)`
2. Se não encontrar, lança `UsernameNotFoundException`
3. Se encontrar, retorna a `UserEntity` diretamente (já implementa `UserDetails`)

> O Spring Security captura o `UsernameNotFoundException` internamente e retorna
> `401 Unauthorized` — não é necessário tratar no `RestExceptionHandler`.

---

### TokenService

Responsável por gerar e validar tokens JWT. Lê a chave secreta e o tempo de expiração
do `application.properties` via `@Value`.

#### Estrutura do token JWT gerado

```
header.payload.signature
```

**Payload (claims):**

| Claim | Valor | Descrição |
|---|---|---|
| `sub` | username | Subject — identifica o usuário |
| `iat` | timestamp | Data e hora de emissão |
| `exp` | timestamp | Data e hora de expiração |

#### Método `generateToken(UserDetails user)`

1. Lê a chave secreta do `application.properties`
2. Define `issuedAt` como o momento atual
3. Define `expiresAt` como `issuedAt + expiration`
4. Assina o token com a chave secreta usando algoritmo **HMAC SHA-256**
5. Retorna o token como `String`

#### Método `validateToken(String token)`

1. Decodifica o token usando a chave secreta
2. Valida a assinatura — qualquer alteração no payload invalida o token
3. Verifica se o token não está expirado
4. Retorna o `username` contido no subject (`sub`)
5. Lança exceção se o token for inválido ou expirado

---

### AuthRequest

DTO de entrada para o endpoint de login:

| Campo | Tipo | Validação |
|---|---|---|
| `username` | `String` | `@NotBlank` |
| `password` | `String` | `@NotBlank` |

---

### AuthResponse

DTO de saída retornado após autenticação bem-sucedida:

| Campo | Tipo | Descrição |
|---|---|---|
| `token` | `String` | JWT gerado pelo `TokenService` |

---

### AuthController

Único endpoint público da API — não exige autenticação.

```
POST /auth/login
```

Fluxo interno do método `login(@RequestBody @Valid AuthRequest request)`:

1. Cria um `UsernamePasswordAuthenticationToken` com `username` e `password` do request
2. Chama `AuthenticationManager.authenticate()` com o token criado
3. O `AuthenticationManager` delega para o `UserDetailsServiceImpl` e o `PasswordEncoder`
4. Se as credenciais forem inválidas, o Spring Security lança `BadCredentialsException` → `401 Unauthorized`
5. Se válidas, o `Authentication` retornado contém o `UserDetails` do usuário autenticado
6. Chama `TokenService.generateToken()` passando o `UserDetails`
7. Retorna `200 OK` com `AuthResponse { token: "eyJ..." }`

> O `AuthenticationManager` precisa ser exposto como bean na `SecurityConfig`
> para ser injetado no `AuthController`.

---

### SecurityConfig

Classe central de configuração do Spring Security. Anotada com `@Configuration` e
`@EnableWebSecurity`.

#### SecurityFilterChain

Define as regras de acesso por endpoint e as configurações gerais:

```
POST /auth/login      → público, sem autenticação
GET  /h2-console/**   → público, ambiente de desenvolvimento
/**                   → qualquer outra rota exige JWT válido
```

Configurações adicionais aplicadas:

| Configuração | Valor | Motivo |
|---|---|---|
| CSRF | desabilitado | Desnecessário com JWT stateless |
| Sessão | `STATELESS` | O servidor não armazena estado entre requisições |
| CORS | configurado | Permite requisições do frontend na LAN |
| Resource Server | JWT | Habilita o `BearerTokenAuthenticationFilter` |

#### Beans registrados na SecurityConfig

| Bean | Tipo | Finalidade |
|---|---|---|
| `SecurityFilterChain` | `SecurityFilterChain` | Define as regras de acesso |
| `AuthenticationManager` | `AuthenticationManager` | Injetado no `AuthController` |
| `PasswordEncoder` | `BCryptPasswordEncoder` | Injetado no `UserDetailsServiceImpl` |
| `CorsConfigurationSource` | `CorsConfigurationSource` | Configura as origens permitidas |

#### Configuração de CORS

Permite requisições apenas da origem configurada no `application.properties`.
Métodos permitidos: `GET`, `POST`, `PUT`, `PATCH`, `DELETE`, `OPTIONS`.
Headers permitidos: todos (`*`).

> O preflight `OPTIONS` precisa ser liberado para o CORS funcionar corretamente
> com o Spring Security.

---

## Estrutura de pacotes

```
src/main/java/org/claumann/travelagency/
├── auth/
│   ├── AuthController.java
│   ├── TokenService.java
│   ├── UserDetailsServiceImpl.java
│   └── dto/
│       ├── AuthRequest.java
│       └── AuthResponse.java
├── config/
│   └── SecurityConfig.java
└── repository/
    └── entity/
        └── UserEntity.java
```

---

## Fluxo completo

### Login

```
1. POST /auth/login { username, password }
         ↓
2. AuthController recebe AuthRequest com @Valid
         ↓
3. Cria UsernamePasswordAuthenticationToken
         ↓
4. AuthenticationManager.authenticate()
         ↓
5. UserDetailsServiceImpl.loadUserByUsername()
   busca o usuário no banco via UserRepository
         ↓
6. BCryptPasswordEncoder compara a senha com o hash do banco
         ↓
   [inválido] → BadCredentialsException → 401 Unauthorized
         ↓
   [válido] → TokenService.generateToken()
         ↓
7. Retorna 200 OK { "token": "eyJ..." }
```

### Requisição autenticada

```
1. Cliente envia:
   GET /destination
   Authorization: Bearer eyJ...
         ↓
2. BearerTokenAuthenticationFilter intercepta o header
         ↓
3. JwtDecoder valida a assinatura e a expiração do token
         ↓
   [inválido ou expirado] → 401 Unauthorized
         ↓
   [válido] → SecurityContext armazena o usuário autenticado
         ↓
4. Controller executa e retorna a resposta normalmente
```

---

## Tratamento de erros

| Situação | Status |
|---|---|
| Credenciais inválidas no login | `401 Unauthorized` |
| Token ausente na requisição | `401 Unauthorized` |
| Token expirado | `401 Unauthorized` |
| Token com assinatura inválida | `401 Unauthorized` |
| Campos obrigatórios ausentes no login | `422 Unprocessable Entity` |
| Acesso a endpoint não permitido para a role | `403 Forbidden` *(Etapa 2)* |

---

## Testes com curl

```bash
# Login com credenciais válidas — esperado 200 OK com token
curl -s -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "admin", "password": "admin123"}'
echo ""
echo "--------------------------------------------"

# Login com credenciais inválidas — esperado 401 Unauthorized
curl -s -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "admin", "password": "senhaerrada"}'
echo ""
echo "--------------------------------------------"

# Login sem username — esperado 422 Unprocessable Entity
curl -s -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"password": "admin123"}'
echo ""
echo "--------------------------------------------"

# Requisição autenticada — esperado 200 OK
curl -s -X GET http://localhost:8080/destination \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
echo ""
echo "--------------------------------------------"

# Requisição sem token — esperado 401 Unauthorized
curl -s -X GET http://localhost:8080/destination
echo ""
echo "--------------------------------------------"

# Requisição com token inválido — esperado 401 Unauthorized
curl -s -X GET http://localhost:8080/destination \
  -H "Authorization: Bearer token.invalido.aqui"
echo ""
echo "--------------------------------------------"
```

---

## Próximos passos

- **Etapa 2 — Roles:** diferenciar permissões entre `ADMIN` e `USER` por endpoint usando `@PreAuthorize`
- **Etapa 3 — Cadastro:** endpoint público `POST /auth/register` com validação e `409 Conflict` para username duplicado
- **Etapa 4 — Produção:** chave secreta via variável de ambiente, CORS restrito ao domínio do frontend, rate limiting no login
