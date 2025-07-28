# Resumo Técnico

## Objetivo
Implementar e evoluir um microserviço para gerenciamento de consentimentos, corrigindo falhas, adicionando funcionalidades e aplicando boas práticas de engenharia de software.

---

## O que foi feito

### Refatoração
- Projeto transformado em aplicação Spring Boot com Maven
- Injeção de dependência via construtor (seguindo SOLID)
- Organização dos pacotes com separação entre `model`, `dto`, `repository`, `service`, `controller` e `mapper`
- Criação de DTOs (`request` e `response`) para evitar exposição direta das entidades
- Criação automática de `id` no formato URN, baseado na instituição
- Validação de instituição enviada no POST para garantir que seja válida
- `createdAt` gerado automaticamente na criação
- Logs inseridos nos pontos principais (controller e service)
- Exceções tratadas diretamente via `ResponseStatusException` para respostas HTTP apropriadas
- Service separada entre interface e implementação

### Integração com bancos
- **Consent** salvo no PostgreSQL (SQL), usando Spring Data JPA
- **Consentimentos revogados** salvos no MongoDB (NoSQL) para rastreamento

### Docker
- `docker-compose.yml` com PostgreSQL e MongoDB

### Endpoints REST
- `POST /consents/{institutionId}` — criar
- `PATCH /consents/{id}/revoke` — revogar
- `GET /consents` — listar todos
- `GET /consents/active` — listar ativos
- `GET /consents/revoked` — listar revogados (MongoDB)

### Testes
- Testes unitários com JUnit e Mockito para as regras de serviço
- Testes integrados com MockMvc simulando chamadas reais à API
- Casos cobrem sucesso e falhas: criação, revogação, listagens e tratamento de erros

---

## Justificativas

- Uso de MongoDB para revogados isola dados de auditoria e evita sobrecarga no banco principal
- DTOs garantem segurança, controle e clareza na troca de dados entre cliente e servidor
- Respostas HTTP corretas facilitam a integração e respeitam os padrões da web
- Exclusão condicional de campos nulos com `@JsonInclude(Include.NON_NULL)` melhora a legibilidade das respostas
- Aplicação simples, mas com código limpo, testável e com boa separação de responsabilidades
- Docker usado para garantir portabilidade e evitar dependência local
- Logs ajudam na observação e rastreabilidade da aplicação
- Service separada em interface e impl para melhor facilidade de implementação, princípio Dependency Inversion Principle

---

Reestruturação simples, levando em consideração boas práticas :)
