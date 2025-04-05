# Prova Ambev

Avaliação técnica de uma API de Cadastro de Pedidos, desenvolvido em Java com Spring-Boot.


## Características

- API RESTful
- Validation
- Service Registry
- Service Discovery
- Message Queue
- MockMVC
- Clean Code

## Requisitos

- Java JDK 21
- Apache Maven >= 3.9.8
- PostgreSql 16
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
- Zookeeper
- Kafka
- Docker


## Instalação

```
git clone https://github.com/danilomeneghel/prova-ambev.git

cd prova-ambev
```


## PostgreSql

Abra seu PostgreSql e crie as 2 bases de dados:

order_db <br>
product_db<br>

Caso não tenha o PostgreSql 16 instalado, execute o seguinte comando Docker:

```
docker network create app_network

sudo docker run \
  --name postgres_ambev \
  --network app_network \
  -p 5432:5432 \
  -v /postgres/files/postgres-data:/var/lib/postgresql/data \
  -e POSTGRES_USER=root \
  -e POSTGRES_PASSWORD=secret \
  -e POSTGRES_DB=order_db \
  -d postgres:16

docker exec -it postgres_ambev psql -U root -c "CREATE DATABASE product_db;"
```


## Zookeeper

Rode o Zookeeper.<br>
Caso não tenha o Zookeeper instalado, execute o seguinte comando Docker:

```
docker run -d \
  --name zookeeper_ambev \
  --network app_network \
  -p 2181:2181 \
  -e ALLOW_ANONYMOUS_LOGIN=yes \
  bitnami/zookeeper:3.8.0
```

Para verificar os serviços registrados dentro do container Zookeeper, utilize o comando:

```
docker exec -it zookeeper_ambev /bin/bash
zkCli.sh
ls /services
```

Para ver se o serviço product-service foi registrado corretamente, utilize o comando:
```
ls /services/product-service
```

Após isso ele mostrará um ID único, onde com esse ID poderá ver todos os dados salvo desse serviço:

```
get /services/product-service/25626c1b-8678-4cfd-a3a8-d5538976b1cd
```


## Kafka

Rode o Kafka.<br>
Caso não tenha o Kafka instalado, execute o seguinte comando Docker:

```
docker run -d \
  --name kafka_ambev \
  --network app_network \
  -p 9092:9092 \
  -e KAFKA_BROKER_ID=1 \
  -e KAFKA_CFG_ZOOKEEPER_CONNECT=zookeeper_ambev:2181 \
  -e KAFKA_CFG_LISTENERS=PLAINTEXT://0.0.0.0:9092 \
  -e KAFKA_CFG_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 \
  -e ALLOW_PLAINTEXT_LISTENER=yes \
  bitnami/kafka:3.3.2
```


## Maven

Para carregar o projeto, digite no terminal:

```
cd order-service
mvn clean spring-boot:run -Dspring-boot.run.profiles=dev
```

Aguarde carregar todo o serviço web. <br>
Após concluído, digite o endereço abaixo em seu navegador: <br>

http://localhost:8080/order <br>


```
cd product-service
mvn clean spring-boot:run -Dspring-boot.run.profiles=dev
```

Aguarde carregar todo o serviço web. <br>
Após concluído, digite o endereço abaixo em seu navegador: <br>

http://localhost:8081/product <br>


## Spring Actuator

Para listar os endpoints habilitados, acesse o seguinte endereço:

http://localhost:8080/actuator <br>


Para verificar o status da aplicação, acesse o endereço:

http://localhost:8080/actuator/health <br>


Para verificar as informações do ambiente, acesse o endereço:

http://localhost:8080/actuator/env <br>


## Docker (Opcional)

Para rodar o projeto via Docker, bastar executar o seguinte comando:

```
docker compose up
```

Aguarde baixar as dependências e carregar todo o projeto, esse processo é demorado. <br>
Caso conclua e não rode pela primeira vez, tente novamente executando o mesmo comando. <br>

Para encerrar tudo digite:

```
docker compose down
```


## Swagger

Documentação da API RESTful:

http://localhost:8080/swagger-ui.html


## Testes

Para realizar os testes, execute o seguinte comando no terminal:

```
cd order-service
mvn test
```


## Uso da Messageria Kafka

Ao utilizar o Apache Kafka para lidar com alta disponibilidade e alto volume de dados, a melhor opção geralmente é priorizar o envio dos dados para o Kafka antes de persistir no banco de dados. Essa abordagem aproveita as características do Kafka para garantir a resiliência e a escalabilidade do sistema.<br> 

Vantagens de Enviar para o Kafka Primeiro:<br> 

- Alta Disponibilidade e Tolerância a Falhas<br>
O Kafka é projetado para ser altamente distribuído e tolerante a falhas.<br> 
Ao replicar dados entre brokers, ele garante que os dados não sejam perdidos, mesmo se um ou mais brokers falharem.<br>
Isso é crucial em cenários de alto volume, onde a perda de dados pode ter impactos significativos.<br>

- Escalabilidade<br>
O Kafka permite escalar horizontalmente, adicionando mais brokers ao cluster conforme necessário.<br> 
Isso o torna ideal para lidar com volumes crescentes de dados.<br>
Essa escalabilidade garante que o sistema possa manter o desempenho mesmo sob cargas pesadas.<br>

- Desacoplamento e Processamento Assíncrono<br>
O Kafka desacopla os produtores de dados dos consumidores, permitindo que os dados sejam processados de forma assíncrona.<br>
Isso melhora o desempenho geral do sistema, pois os produtores não precisam esperar que os dados sejam persistidos no banco de dados antes de continuar.<br>

- Retenção de Dados<br>
O Kafka armazena os dados por um período configurável, permitindo que os consumidores processem os dados em seu próprio ritmo e até mesmo reprocessem os dados se necessário.


## Screenshots

Swagger-UI <br>
![Screenshots](screenshots/screenshot01.png) <br><br>

Modelagem ER <br>
![Screenshots](screenshots/screenshot02.png) <br><br>

Testes Unitários <br>
![Screenshots](screenshots/screenshot03.png) <br><br>

Aplicação Spring Boot <br>
![Screenshots](screenshots/screenshot04.png) <br><br>


## Licença

Projeto licenciado sob <a href="LICENSE">The MIT License (MIT)</a>.<br><br>


Desenvolvido por<br>
Danilo Meneghel<br>
danilo.meneghel@gmail.com<br>
http://danilomeneghel.github.io/<br>
