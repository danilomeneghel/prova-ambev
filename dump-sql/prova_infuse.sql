CREATE DATABASE IF NOT EXISTS prova_ambev;
USE prova_ambev;

CREATE TABLE IF NOT EXISTS pedido (
    id INTEGER NOT NULL AUTO_INCREMENT,
	numero_controle VARCHAR(30) NOT NULL,
    data_cadastro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    nome VARCHAR(30) NOT NULL,
    valor_unitario FLOAT NOT NULL,
    quantidade INTEGER NOT NULL DEFAULT 1,
    codigo_cliente INTEGER NOT NULL,
    valor_total FLOAT NOT NULL,

    PRIMARY KEY (id)
);