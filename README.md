# Prova Ambev

Avaliação técnica de uma API de Cadastro de Pedidos, desenvolvido em Java com Spring-Boot.

## Características

- API RESTful
- Validation
- Cache
- MessageQueue
- MockMVC

## Requisitos

- Java JDK 21
- Apache Maven >= 3.9.8
- MySql 8
- Zookeeper 3.8.0
- Kafka 3.3.2
- Docker (Opcional)

## Tecnologias

- Java
- JPA
- Hibernate
- Maven
- Spring
- Lombok
- Jakarta
- JSON
- MySql
- JUnit
- Kafka
- Docker

## Instalação

```
$ git clone https://github.com/danilomeneghel/prova-ambev.git

$ cd prova-ambev
```

## MySql

Abra seu MySql e crie a base de dados:

prova_ambev


## Maven

Primeiro rode o Kafka.<br>
Caso não tenha o Kafka instalado, execute o seguinte comando via Docker:

```
$ docker network create app-tier --driver bridge
$ docker run -d --name zookeeper-server --network app-tier -e ALLOW_ANONYMOUS_LOGIN=yes bitnami/zookeeper:3.8.0
$ docker run -d --name kafka-server --network app-tier -e ALLOW_PLAINTEXT_LISTENER=yes -e KAFKA_CFG_ZOOKEEPER_CONNECT=zookeeper-server:2181 bitnami/kafka:3.3.2
$ docker run -it --rm --network app-tier -e KAFKA_CFG_ZOOKEEPER_CONNECT=zookeeper-server:2181 bitnami/kafka:latest kafka-topics.sh --list  --bootstrap-server kafka-server:9092
```

Para carregar o projeto, digite no terminal:

```
$ mvn clean spring-boot:run -Dspring-boot.run.profiles=dev
```

Aguarde carregar todo o serviço web. <br>
Após concluído, digite o endereço abaixo em seu navegador, nele será listado os pedidos
cadastrados na API. <br>

http://localhost:8080/order


## Docker (Opcional)

Para rodar o projeto via Docker, bastar executar o seguinte comando:

```
$ docker build -t projeto .
$ docker run -p 8080:8080 -d projeto
```

Ou via Docker-Compose:

```
$ docker compose up
```

Aguarde baixar as dependências e carregar todo o projeto, esse processo é demorado. <br>
Caso conclua e não rode pela primeira vez, tente novamente executando o mesmo comando. <br>

Para encerrar tudo digite:

```
$ docker compose down
```


## Swagger

Documentação da API RESTful:

http://localhost:8080/swagger-ui.html


## Testes

Para realizar os testes, execute o seguinte comando no terminal:

```
$ mvn test
```

## Screenshots

Swagger-UI <br>
![Screenshots](screenshots/screenshot01.png) <br><br>

Modelagem ER <br>
![Screenshots](screenshots/screenshot02.png) <br><br>

Testes Unitários <br>
![Screenshots](screenshots/screenshot06.png) <br><br>


## Licença

Projeto licenciado sob <a href="LICENSE">The MIT License (MIT)</a>.<br><br>


Desenvolvido por<br>
Danilo Meneghel<br>
danilo.meneghel@gmail.com<br>
http://danilomeneghel.github.io/<br>
