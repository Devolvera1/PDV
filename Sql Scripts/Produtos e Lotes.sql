CREATE TABLE produtos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(50) NOT NULL,
    nome VARCHAR(255) NOT NULL,
    descricao TEXT,
    preco DECIMAL(10, 2) NOT NULL,
    quantidade_estoque INT NOT NULL,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_modificacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE lotes_produtos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    produto_id INT NOT NULL,
    quantidade INT NOT NULL,
    data_validade DATE NOT NULL,
    data_compra TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (produto_id) REFERENCES produtos(id)
);

INSERT INTO produtos (codigo, nome, descricao, preco, quantidade_estoque) VALUES
('P001', 'Arroz', 'Arroz tipo 1, 5kg', 15.50, 200),
('P002', 'Feijão', 'Feijão preto, 1kg', 8.90, 150),
('P003', 'Macarrão', 'Macarrão espaguete, 500g', 4.30, 300),
('P004', 'Óleo', 'Óleo de soja, 900ml', 7.60, 120),
('P005', 'Açúcar', 'Açúcar cristal, 1kg', 3.10, 250);

INSERT INTO lotes_produtos (produto_id, quantidade, data_validade) VALUES
(1, 100, '2025-06-30'),  -- Lote de Arroz
(1, 150, '2025-12-31'),  -- Novo lote de Arroz
(2, 80, '2025-07-15'),   -- Lote de Feijão
(2, 70, '2025-09-01'),   -- Novo lote de Feijão
(3, 200, '2026-01-01'),  -- Lote de Macarrão
(3, 100, '2026-06-30'),  -- Novo lote de Macarrão
(4, 120, '2025-03-20'),  -- Lote de Óleo
(4, 100, '2025-10-10'),  -- Novo lote de Óleo
(5, 150, '2025-08-01'),  -- Lote de Açúcar
(5, 100, '2025-11-15');  -- Novo lote de Açúcar
