# Plano Evolutivo — Spring Security

Plano de implementação de segurança da Travel Agency API, dividido em etapas incrementais.

---

## Etapa 1 — MVP: Autenticação JWT básica

Objetivo: proteger todos os endpoints da API, permitindo acesso apenas a usuários autenticados.

- Tabela `users` no banco com `username`, `password` (bcrypt) e `role`
- Usuário inicial criado via `data.sql` — sem cadastro de usuários ainda
- Endpoint público `POST /auth/login` que recebe credenciais, valida contra o banco e retorna um JWT com expiração de 8h
- Todos os endpoints de `/destination` passam a exigir o token no header `Authorization: Bearer`
- `SecurityConfig` com CORS configurado, CSRF desabilitado e sessão stateless
- `UserDetailsService` carregando o usuário do banco pelo username
- `TokenService` gerando e validando o JWT com chave secreta via `application.properties`

---

## Etapa 2 — Roles e autorização por endpoint

Objetivo: controle de acesso granular por perfil de usuário.

- Dois perfis: `ADMIN` e `USER`
- `USER` — pode listar, pesquisar, visualizar e avaliar destinos
- `ADMIN` — pode cadastrar e excluir destinos, além de tudo que `USER` pode
- Autorização aplicada via `@PreAuthorize` nos métodos do controller ou via regras no `SecurityFilterChain`

---

## Etapa 3 — Cadastro de usuários

Objetivo: permitir que novos usuários se registrem na API sem intervenção manual no banco.

- Endpoint público `POST /auth/register` com validação de campos via Jakarta Validation
- Senha sempre salva com BCrypt — nunca em texto puro
- Validação de username duplicado com resposta `409 Conflict`
- Role padrão `USER` atribuída automaticamente no cadastro

---

## Etapa 4 — Segurança em produção

Objetivo: hardening da configuração para um ambiente real.

- Chave secreta do JWT migrada de `application.properties` para variável de ambiente
- CORS restrito ao domínio exato do frontend em vez de IP da LAN
- Rate limiting no endpoint de login para evitar brute force

---

## Resumo

| Etapa | Descrição | PR sugerido |
|---|---|---|
| 1 | MVP — Autenticação JWT básica | `feature/security-jwt-basic` |
| 2 | Roles e autorização por endpoint | `feature/security-roles` |
| 3 | Cadastro de usuários | `feature/security-register` |
| 4 | Segurança em produção | `feature/security-hardening` |
