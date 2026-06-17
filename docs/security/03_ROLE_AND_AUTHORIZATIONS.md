# Etapa 2 — Roles e autorização por endpoint

## Objetivo

Implementar controle de acesso baseado em perfis de usuário (Role-Based Access Control - RBAC), garantindo que determinadas operações da API possam ser executadas apenas por usuários autorizados.

## Perfis

A aplicação deve possuir dois perfis de acesso:

### USER

Usuários com perfil `USER` podem:

* Listar destinos
* Pesquisar destinos
* Visualizar detalhes de um destino
* Avaliar destinos

### ADMIN

Usuários com perfil `ADMIN` possuem todas as permissões do perfil `USER` e também podem:

* Cadastrar novos destinos
* Excluir destinos

## Alterações necessárias

### Modelo de usuário

Adicionar suporte a roles no modelo de usuário.

Exemplo:

```java
public enum Role {
    USER,
    ADMIN
}
```

O usuário deve possuir uma role associada que será utilizada durante o processo de autenticação e autorização.

### JWT

Incluir a role do usuário como claim no token JWT para que as permissões possam ser identificadas durante a autenticação.

Exemplo de payload:

```json
{
  "sub": "admin",
  "role": "ADMIN",
  "iat": 1710000000,
  "exp": 1710003600
}
```

### Configuração de segurança

Configurar o Spring Security para converter a role presente no token JWT em uma autoridade (`GrantedAuthority`).

As roles devem seguir o padrão:

```text
ROLE_USER
ROLE_ADMIN
```

## Proteção dos endpoints

A autorização pode ser implementada utilizando uma das abordagens abaixo:

### Opção 1 — Anotações nos controllers

Habilitar Method Security:

```java
@EnableMethodSecurity
```

Exemplo:

```java
@GetMapping
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public List<DestinationResponse> findAll() {
    ...
}
```

```java
@PostMapping
@PreAuthorize("hasRole('ADMIN')")
public DestinationResponse create(...) {
    ...
}
```

```java
@DeleteMapping("/{id}")
@PreAuthorize("hasRole('ADMIN')")
public void delete(...) {
    ...
}
```

### Opção 2 — Regras no SecurityFilterChain

Definir permissões diretamente na configuração de segurança:

```java
.authorizeHttpRequests(auth -> auth
    .requestMatchers(HttpMethod.GET, "/destinations/**")
        .hasAnyRole("USER", "ADMIN")
    .requestMatchers(HttpMethod.POST, "/destinations/**")
        .hasRole("ADMIN")
    .requestMatchers(HttpMethod.DELETE, "/destinations/**")
        .hasRole("ADMIN")
    .anyRequest()
        .authenticated()
)
```

## Critérios de aceite

* Usuários autenticados com role `USER` conseguem consultar e avaliar destinos.
* Usuários autenticados com role `USER` não conseguem cadastrar ou excluir destinos.
* Usuários autenticados com role `ADMIN` conseguem acessar todas as funcionalidades da aplicação.
* Requisições sem autenticação retornam `401 Unauthorized`.
* Requisições autenticadas sem permissão retornam `403 Forbidden`.
* A role do usuário é propagada corretamente durante a autenticação e reconhecida pelo Spring Security.
