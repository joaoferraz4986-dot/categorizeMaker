CREATE TABLE projeto_item (
    id_projeto_item INT AUTO_INCREMENT PRIMARY KEY,
    id_projeto INT NOT NULL,
    id_item INT NOT NULL,
    quantidade_usada INT NOT NULL DEFAULT 0,
    data_alocacao DATETIME DEFAULT CURRENT_TIMESTAMP,
    data_devolucao DATETIME NULL,
    UNIQUE KEY uk_projeto_item (id_projeto, id_item),
    INDEX (id_projeto),
    CHECK (quantidade_usada >= 0),
    FOREIGN KEY (id_projeto) REFERENCES projeto(id_projeto) ON DELETE CASCADE, 
    FOREIGN KEY (id_item) REFERENCES item(id_item) ON DELETE RESTRICT
);