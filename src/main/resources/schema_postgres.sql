-- SCHEMA PARA O POSTGRESQL

CREATE SEQUENCE table_usuario_id_seq;
CREATE TABLE USUARIO(
    ID INTEGER PRIMARY KEY DEFAULT nextval('table_usuario_id_seq'),
    LOGIN VARCHAR(50) NOT NULL,
    SENHA VARCHAR(255) NOT NULL,
    ADMIN BOOL DEFAULT FALSE
);
ALTER SEQUENCE table_usuario_id_seq
OWNED BY table_usuario.id;

CREATE SEQUENCE table_cliente_id_seq;
CREATE TABLE CLIENTE (
    ID INTEGER PRIMARY KEY DEFAULT nextval('table_cliente_id_seq'),
    NOME VARCHAR(100),
    CPF VARCHAR(11)
);
ALTER SEQUENCE table_cliente_id_seq
OWNED BY table_cliente.id;

CREATE SEQUENCE table_produto_id_seq;
CREATE TABLE PRODUTO (
    ID INTEGER PRIMARY KEY DEFAULT nextval('table_produto_id_seq'),
    DESCRICAO VARCHAR(100),
    PRECO_UNITARIO NUMERIC(20,2)
);
ALTER SEQUENCE table_produto_id_seq
OWNED BY table_produto.id;

CREATE SEQUENCE table_pedido_id_seq;
CREATE TABLE PEDIDO (
    ID INTEGER PRIMARY KEY DEFAULT nextval('table_pedido_id_seq'),
    CLIENTE_ID INTEGER REFERENCES CLIENTE (ID),
    DATA_PEDIDO TIMESTAMP,
    STATUS VARCHAR(20),
    TOTAL NUMERIC(20,2)
);
ALTER SEQUENCE table_pedido_id_seq
OWNED BY table_pedido.id;

CREATE SEQUENCE table_item_pedido_id_seq;
CREATE TABLE ITEM_PEDIDO (
    ID INTEGER PRIMARY KEY DEFAULT nextval('table_item_pedido_id_seq'),
    PEDIDO_ID INTEGER REFERENCES PEDIDO (ID),
    PRODUTO_ID INTEGER REFERENCES PRODUTO (ID),
    QUANTIDADE INTEGER
);
ALTER SEQUENCE table_item_pedido_id_seq
OWNED BY table_item_pedido.id;