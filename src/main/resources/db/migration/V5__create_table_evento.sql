CREATE TABLE evento (
    id_evento BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo VARCHAR(50) NOT NULL,
    entidade VARCHAR(50) NOT NULL,
    entidade_id INT NULL,
    titulo VARCHAR(150) NOT NULL,
    descricao VARCHAR(255),
    data_evento DATETIME DEFAULT CURRENT_TIMESTAMP,
    id_usuario BINARY(16) NULL,
    INDEX idx_evento_data (data_evento),
    INDEX idx_evento_entidade (entidade, entidade_id),
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON DELETE SET NULL
);

INSERT INTO evento (tipo, entidade, entidade_id, titulo, descricao, data_evento, id_usuario)
SELECT 'PROJETO_CRIADO', 'PROJETO', id_projeto, 'Projeto criado', nome, COALESCE(data_inicio, CURRENT_TIMESTAMP), id_usuario
FROM projeto;

INSERT INTO evento (tipo, entidade, entidade_id, titulo, descricao, data_evento)
SELECT 'ITEM_CRIADO', 'ITEM', id_item, 'Item cadastrado', nome, CURRENT_TIMESTAMP
FROM item;
