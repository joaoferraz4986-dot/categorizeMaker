CREATE TABLE item (
    id INT AUTO_INCREMENT PRIMARY KEY,
    categoria VARCHAR(100) NOT NULL,
    nome VARCHAR(255) NOT NULL,
    tipo VARCHAR(255),
    quantidade INT NOT NULL,
    estado VARCHAR(100) NOT NULL,
    imagem_item LONGTEXT
);