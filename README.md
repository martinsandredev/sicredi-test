# Projeto de teste Sicredi

Este é um projeto utilizando Spring Boot com Kotlin, banco de dados PostgreSQL e mensageria com Kafka.

## Pré-requisitos
Certifique-se de ter os seguintes pré-requisitos instalados em seu ambiente de desenvolvimento:

- Docker: [Instalação do Docker](https://docs.docker.com/get-docker/)

## Configuração do Docker Compose

O arquivo `docker-compose.yml` na raiz do projeto contém toda a configuração para rodar a aplicação.

1. Abra o terminal e navegue até o diretório raiz do projeto.
2. Execute o seguinte comando para iniciar os serviços:

```shell
docker-compose up -d
```

## Testando a Aplicação
A docoumentação da API por meio da OpenAPI se encontra em http://localhost:8080/swagger-ui/index.html
