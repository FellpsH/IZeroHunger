-- Script de inicializacao para popular o banco H2 com dados de exemplo

-- Inserir categorias
INSERT INTO categories (id, name) VALUES (1, 'Frutas');
INSERT INTO categories (id, name) VALUES (2, 'Verduras');
INSERT INTO categories (id, name) VALUES (3, 'Legumes');
INSERT INTO categories (id, name) VALUES (4, 'Cereais');
INSERT INTO categories (id, name) VALUES (5, 'Laticinios');
INSERT INTO categories (id, name) VALUES (6, 'Carnes');
INSERT INTO categories (id, name) VALUES (7, 'Paes e Massas');

-- Inserir usuario fornecedor (senha: 123456)
INSERT INTO usuarios (id, nome, email, senha, telefone, endereco, data_nascimento, tipo) 
VALUES (1, 'Joao Silva', 'joao@fornecedor.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', '11999999999', 'Rua das Flores, 123', '1980-01-15', 'FORNECEDOR');

-- Inserir usuario comprador (senha: 123456)
INSERT INTO usuarios (id, nome, email, senha, telefone, endereco, data_nascimento, tipo) 
VALUES (2, 'Maria Santos', 'maria@comprador.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', '11888888888', 'Av. Principal, 456', '1985-05-20', 'COMPRADOR');

-- Inserir produtos
INSERT INTO products (id, name, description, price, quantity, location, expiry_date, fornecedor, image) 
VALUES (1, 'Banana', 'Banana prata fresca', 3.50, 50, 'Sao Paulo - SP', '2024-09-20', 1, null);

INSERT INTO products (id, name, description, price, quantity, location, expiry_date, fornecedor, image) 
VALUES (2, 'Maca', 'Maca gala vermelha', 4.20, 30, 'Sao Paulo - SP', '2024-09-25', 1, null);

INSERT INTO products (id, name, description, price, quantity, location, expiry_date, fornecedor, image) 
VALUES (3, 'Alface', 'Alface crespa fresca', 2.80, 25, 'Sao Paulo - SP', '2024-09-18', 1, null);

INSERT INTO products (id, name, description, price, quantity, location, expiry_date, fornecedor, image) 
VALUES (4, 'Tomate', 'Tomate cereja', 5.90, 40, 'Sao Paulo - SP', '2024-09-22', 1, null);

INSERT INTO products (id, name, description, price, quantity, location, expiry_date, fornecedor, image) 
VALUES (5, 'Cenoura', 'Cenoura organica', 3.20, 35, 'Sao Paulo - SP', '2024-09-30', 1, null);

INSERT INTO products (id, name, description, price, quantity, location, expiry_date, fornecedor, image) 
VALUES (6, 'Arroz', 'Arroz integral 1kg', 8.50, 20, 'Sao Paulo - SP', '2025-12-31', 1, null);

INSERT INTO products (id, name, description, price, quantity, location, expiry_date, fornecedor, image) 
VALUES (7, 'Leite', 'Leite integral 1L', 4.80, 15, 'Sao Paulo - SP', '2024-09-28', 1, null);

INSERT INTO products (id, name, description, price, quantity, location, expiry_date, fornecedor, image) 
VALUES (8, 'Pao', 'Pao frances fresco', 1.20, 100, 'Sao Paulo - SP', '2024-09-17', 1, null);

INSERT INTO products (id, name, description, price, quantity, location, expiry_date, fornecedor, image) 
VALUES (9, 'Frango', 'Peito de frango 1kg', 12.90, 10, 'Sao Paulo - SP', '2024-09-19', 1, null);

INSERT INTO products (id, name, description, price, quantity, location, expiry_date, fornecedor, image) 
VALUES (10, 'Queijo', 'Queijo minas frescal', 15.50, 8, 'Sao Paulo - SP', '2024-09-26', 1, null);

-- Associar produtos as categorias
INSERT INTO product_categories (product_id, category_id) VALUES (1, 1); -- Banana -> Frutas
INSERT INTO product_categories (product_id, category_id) VALUES (2, 1); -- Maca -> Frutas
INSERT INTO product_categories (product_id, category_id) VALUES (3, 2); -- Alface -> Verduras
INSERT INTO product_categories (product_id, category_id) VALUES (4, 2); -- Tomate -> Verduras
INSERT INTO product_categories (product_id, category_id) VALUES (5, 3); -- Cenoura -> Legumes
INSERT INTO product_categories (product_id, category_id) VALUES (6, 4); -- Arroz -> Cereais
INSERT INTO product_categories (product_id, category_id) VALUES (7, 5); -- Leite -> Laticinios
INSERT INTO product_categories (product_id, category_id) VALUES (8, 7); -- Pao -> Paes e Massas
INSERT INTO product_categories (product_id, category_id) VALUES (9, 6); -- Frango -> Carnes
INSERT INTO product_categories (product_id, category_id) VALUES (10, 5); -- Queijo -> Laticinios
