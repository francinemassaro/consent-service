# Resumo Técnico

## Objetivo
Implementar e evoluir um microserviço para gerenciamento de consentimentos, corrigindo falhas, adicionando funcionalidades e aplicando boas práticas de engenharia de software.

---

## O que foi feito

### Refatoração
- Projeto transformado em aplicação Spring Boot com Maven
- Injeção de dependência via construtor (seguindo SOLID)

### Integração com bancos
- **Consent** salvo no PostgreSQL (SQL), usando Spring Data JPA
- **Consentimentos revogados** salvos no MongoDB (NoSQL) para rastreamento

### Docker
- `docker-compose.yml` com PostgreSQL e MongoDB
- Aplicação configurada para ler conexões em `localhost:5432` e `localhost:27017`

### Endpoints REST
- `POST /consents` — criar
- `PATCH /consents/{id}/revoke` — revogar
- `GET /consents` — listar todos
- `GET /consents/active` — listar ativos
- `GET /consents/revoked` — listar revogados (MongoDB)

### Testes
- Testes unitários criados com JUnit e Mockito
- Cobrem criação, revogação, listagens e casos de erro

---

## Justificativas

- Uso de MongoDB para revogados isola dados de auditoria e evita sobrecarga no banco principal
- Aplicação simples, mas com código limpo, testável e com boa separação de responsabilidades
- Docker usado para garantir portabilidade e evitar dependência local

---

A solução imediata foi mantida simples, funcional e com foco em clareza, qualidade de código e organização.
