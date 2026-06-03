# Desafio: API para Agência de Viagem

## 📋 Informações da Entrega

| Critério | Data e Hora |
|----------|-------------|
| **Entrega para 100% da nota** | Até **05/06/2026** às 23h59min (Horário de Brasília) |
| **Entrega para 60% da nota** | Até **12/06/2026** às 23h59min (Horário de Brasília) |

---

## 🎯 Objetivo

Desenvolvimento de uma API RESTful para uma agência de viagens, utilizando **Java** e **Spring Boot**. A API permitirá que empresas de turismo e aplicativos de terceiros integrem-se ao sistema da agência.

---

## 📌 Contexto

Você está trabalhando como desenvolvedor em uma agência de viagens que deseja criar uma API para auxiliar os clientes a planejar suas viagens. A agência já possui um site de reserva de viagens, mas agora deseja disponibilizar uma API para permitir que outras empresas de turismo e aplicativos de terceiros integrem-se ao seu sistema.

A agência lida com informações sobre **destinos**, **pacotes de viagens**, **disponibilidade de hotéis** e **atividades turísticas**.

---

## ✅ Funcionalidades para Clientes e Parceiros

- Cadastrar os destinos de viagem
- Listar todos os destinos de viagem disponíveis
- Pesquisar destinos por nome ou localização
- Visualizar informações detalhadas sobre um destino específico
- Reservar pacotes de viagens para um destino
- Excluir destino

---

## 🔧 Requisitos Técnicos

Sua API deve ser desenvolvida como uma **API RESTful**, com os seguintes elementos:

- Endpoints com os métodos HTTP apropriados: `GET`, `POST`, `PUT`, `PATCH` e `DELETE`
- Camada de serviço para processar as solicitações
- Separação clara entre controladores e lógica de negócios

---

## 📡 Endpoints a Serem Implementados

### 1. Cadastro de Destinos de Viagem
Criar um endpoint que permita **inserir um novo destino de viagem**.

### 2. Listagem de Destinos de Viagem
Criar um endpoint que retorne a **lista de todos os destinos disponíveis**.

### 3. Pesquisa de Destinos
Permitir a **pesquisa de destinos por nome ou localização** e retornar os resultados correspondentes.

### 4. Visualização de Informações Detalhadas
Implementar um endpoint que permita **visualizar informações detalhadas sobre um destino específico**.

### 5. Avaliação de Destino de Viagem
Criar um endpoint que permita receber uma **nota de avaliação de 1 a 10**. O sistema deve:
- Receber a avaliação
- Calcular a nova nota com base na nota já existente (atualização da média)

### 6. Exclusão de Destinos de Viagem
Criar um endpoint que permita **excluir um determinado destino de viagem**.

---

## 📊 Critérios de Avaliação

| Critério | Descrição |
|----------|-----------|
| **Implementação dos Endpoints** | Cada endpoint deve ser implementado corretamente com os métodos HTTP adequados |
| **Camada de Serviço** | Criar uma camada de serviço que lide com a lógica de negócios, separando-a dos controladores |
| **Funcionalidade** | Os endpoints devem funcionar corretamente, atendendo aos requisitos especificados |
| **Qualidade do Código** | Avaliar boas práticas de programação, nomeação adequada de variáveis e métodos, e organização do projeto |

---

## ⚠️ Observações

> **Não é necessário** se preocupar com:
> - Integração com banco de dados
> - Segurança
> - Conceitos avançados

> O foco principal é a **criação dos endpoints** e a **organização da camada de serviço**.

---

## 🏆 Resultado Esperado

Uma **API RESTful usando Java e Spring Boot** que ofereça todas as funcionalidades mencionadas, com endpoints implementados com os métodos HTTP apropriados e uma camada de serviço bem estruturada para processar as solicitações, seguindo todas as especificações descritas.
