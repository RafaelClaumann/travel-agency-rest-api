# Profiles — Configuração de Banco de Dados

A aplicação possui dois profiles configurados, permitindo alternar entre H2 (desenvolvimento)
e PostgreSQL (testes com banco real) sem alterar o código.

---

## Profile padrão — H2 (default)

Ativado automaticamente quando nenhum profile é especificado.

Banco em memória — os dados são perdidos ao reiniciar a aplicação.
Ideal para desenvolvimento rápido sem dependências externas.

Console H2 disponível em `http://localhost:8080/h2-console`:
- JDBC URL: `jdbc:h2:mem:traveldb`
- Usuário: `sa`
- Senha: *(vazio)*

---

## Profile postgres

Ativado explicitamente quando se deseja persistência real com PostgreSQL.

Os dados sobrevivem a restarts da aplicação.
Requer os containers Docker rodando antes de subir a aplicação.

### Como ativar

```bash
# via Gradle
./gradlew bootRun --args='--spring.profiles.active=postgres'

# via variável de ambiente
SPRING_PROFILES_ACTIVE=postgres ./gradlew bootRun
```

No IntelliJ: `Edit Configurations → Active profiles → postgres`

### Infraestrutura necessária

Suba os containers antes de iniciar a aplicação:

```bash
cd docker
docker compose up -d
```

PostgreSQL disponível em `localhost:5432`.
pgAdmin disponível em `http://localhost:5050` — login `admin@admin.com` / `admin`.
O servidor PostgreSQL já aparece pré-configurado no pgAdmin sem necessidade de configuração manual.

---

## Como funciona a herança de profiles

O Spring carrega sempre o `application.properties` primeiro.
Quando um profile é ativado, o arquivo correspondente (`application-{profile}.properties`)
é carregado em seguida, sobrescrevendo apenas as propriedades redefinidas.

Dessa forma o `application-postgres.properties` declara apenas o que muda — datasource
e dialect — herdando segurança, JPA e demais configurações do arquivo padrão.

---

## Resumo

| | H2 (default) | PostgreSQL |
|---|---|---|
| Ativação | automática | `--spring.profiles.active=postgres` |
| Persistência | em memória | em disco |
| Docker necessário | não | sim |
| Console | `http://localhost:8080/h2-console` | pgAdmin `http://localhost:5050` |
| Credenciais banco | `sa` / *(vazio)* | `travel` / `travel` |