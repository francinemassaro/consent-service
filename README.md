# Consent Service - Open Finance Challenge

Este projeto é uma API REST para gerenciamento de consentimentos.

## Tecnologias
- Java 21
- Spring Boot
- PostgreSQL - docker
- MongoDB - docker
- JPA / Spring Data
- Spring Data MongoDB
- Testes com JUnit e Mockito

## Como executar localmente

### Pré-requisitos:
- Java 21
- Maven
- Docker

### Subir os bancos de dados

Na raíz do projeto, dê o comando:
```bash
docker-compose up -d
```
Isso deve subir todas as configurações do docker necessárias para rodar o projeto localmente.

### A aplicação está disponível na rota:
```bash
http://localhost:8080
```
__________________________________________________

####
## Curls insomnia/postman:
POST:
```bash
curl --request POST \
  --url http://localhost:8080/consents \
  --header 'Content-Type: application/json' \
  --data '{
    "id": "c1",
    "userId": "u1",
    "active": true,
    "createdAt": "2025-07-18T20:00:00"
}'
```

GET Active:
```bash
curl --request GET \
  --url http://localhost:8080/consents/active
```

PATCH Revoke:
```bash
curl --request PATCH \
  --url http://localhost:8080/consents/c1/revoke
```

GET All:
```bash
curl --request GET \
  --url http://localhost:8080/consents
```

GET Revoked mongo:
```bash
curl --request GET \
  --url http://localhost:8080/consents/revoked
```
