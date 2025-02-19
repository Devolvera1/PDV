CREATE DATABASE PDV_BCO;

USE PDV_BCO;
CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    nome_completo VARCHAR(255) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    funcao VARCHAR(255),
    status VARCHAR(50),
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ultimo_acesso TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    criado_por VARCHAR(255),
    data_modificacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modificado_por VARCHAR(255),
    endereco_ip VARCHAR(50)
);

INSERT INTO usuarios (username, nome_completo, senha, funcao, status, data_criacao, ultimo_acesso, criado_por, data_modificacao, modificado_por, endereco_ip) 
VALUES
('admin', 'Administrador', 'admin123', 'Administrador', 'Ativo', NOW(), NOW(), 'admin', NOW(), 'admin', '192.168.1.1'),
('jdoe', 'John Doe', '123456', 'Administrador', 'Ativo', '2023-01-01 10:00:00', '2025-02-17 15:30:00', 'admin', '2025-02-17 15:30:00', 'admin', '192.168.1.10'),
('asmith', 'Alice Smith', 'senha123', 'Vendedor', 'Ativo', '2023-05-10 14:25:00', '2025-02-17 16:00:00', 'admin', '2025-02-17 16:00:00', 'admin', '192.168.1.11'),
('mjones', 'Michael Jones', 'caixa789', 'Caixa', 'Inativo', '2022-11-05 09:45:00', '2025-02-16 12:00:00', 'admin', '2025-02-16 12:00:00', 'admin', '192.168.1.12'),
('mparker', 'Mary Parker', 'gerente456', 'Gerente', 'Ativo', '2023-03-15 08:30:00', '2025-02-17 13:30:00', 'admin', '2025-02-17 13:30:00', 'admin', '192.168.1.13'),
('bwilson', 'Bruce Wilson', 'tecnico321', 'Técnico', 'Ativo', '2024-06-23 11:20:00', '2025-02-17 10:00:00', 'admin', '2025-02-17 10:00:00', 'admin', '192.168.1.14'),
('lgreen', 'Laura Green', 'estagiaria159', 'Estagiária', 'Inativo', '2023-08-10 16:00:00', '2025-02-15 17:00:00', 'admin', '2025-02-15 17:00:00', 'admin', '192.168.1.15'),
('cclark', 'Chris Clark', 'assistente852', 'Assistente', 'Ativo', '2022-12-20 13:10:00', '2025-02-17 09:30:00', 'admin', '2025-02-17 09:30:00', 'admin', '192.168.1.16');
