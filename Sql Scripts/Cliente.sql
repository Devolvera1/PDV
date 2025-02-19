USE PDV_BCO;

CREATE TABLE clientes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    tipo_documento ENUM('cpf', 'cnpj') NOT NULL, -- Identifica se é CPF ou CNPJ
    documento VARCHAR(18) UNIQUE,
    telefone VARCHAR(20),
    email VARCHAR(100) UNIQUE,
    endereco TEXT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status ENUM('ativo', 'inativo') DEFAULT 'ativo'
);

INSERT INTO clientes (nome, tipo_documento, documento, telefone, email, endereco)
VALUES
('Ana Oliveira', 'cpf', '123.456.789-11', '(11) 98765-4321', 'ana.oliveira@email.com', 'Rua dos Jacarandás, 34, São Paulo - SP'),
('Lucas Martins', 'cpf', '987.654.321-22', '(21) 87654-3210', 'lucas.martins@email.com', 'Avenida Brasil, 101, Rio de Janeiro - RJ'),
('Juliana Souza', 'cpf', '111.222.333-44', '(31) 99876-5432', 'juliana.souza@email.com', 'Rua das Margaridas, 78, Belo Horizonte - MG'),
('Eduardo Costa', 'cpf', '555.666.777-88', '(61) 95555-4444', 'eduardo.costa@email.com', 'Rua do Sol, 22, Brasília - DF');

INSERT INTO clientes (nome, tipo_documento, documento, telefone, email, endereco)
VALUES
('Tech Solutions LTDA', 'cnpj', '12.345.678/0001-90', '(11) 33445-5566', 'contato@techsolutions.com', 'Rua da Tecnologia, 500, São Paulo - SP'),
('Global Imports S/A', 'cnpj', '98.765.432/0001-20', '(21) 22112-3344', 'vendas@globalimports.com', 'Avenida das Nações, 200, Rio de Janeiro - RJ'),
('Supermercado ABC Ltda', 'cnpj', '11.223.344/0001-55', '(31) 33344-5555', 'sac@abc.com.br', 'Rua da Economia, 150, Belo Horizonte - MG'),
('Construtora XYZ Ltda', 'cnpj', '33.445.567/0001-33', '(61) 22334-4455', 'contato@construtoraxyz.com', 'Avenida Central, 300, Brasília - DF');
