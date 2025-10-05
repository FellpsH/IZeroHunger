-- Script de inicialização para popular o banco H2 com dados de exemplo

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

-- Inserir usuario fornecedor Fellipe
INSERT INTO usuarios (id, nome, email, senha, telefone, endereco, data_nascimento, tipo) 
VALUES (3, 'fellipe', 'fellipe@teste.com', '$2a$10$ANiNoenAridLvXN3oK/Neuh1DA6LGu7HXvo3FKuT8bctENIO9A71O', '11777777777', 'Rua das Flores, 123', '1998-09-08', 'FORNECEDOR');

-- Inserir produtos com imagens atrativas
INSERT INTO products (id, name, description, price, quantity, location, expiry_date, fornecedor, image) 
VALUES (1, 'Banana Prata Premium', 'Banana prata fresca, doce e nutritiva', 3.50, 50, 'Sao Paulo - SP', '2025-12-20', 1, 'https://images.unsplash.com/photo-1571771894821-ce9b6c11b08e?w=400&h=300&fit=crop&crop=center');

INSERT INTO products (id, name, description, price, quantity, location, expiry_date, fornecedor, image) 
VALUES (2, 'Maca Gala Organica', 'Maca gala vermelha, crocante e saborosa', 4.20, 30, 'Sao Paulo - SP', '2025-12-25', 1, 'https://images.unsplash.com/photo-1560806887-1e4cd0b6cbd6?w=400&h=300&fit=crop&crop=center');

INSERT INTO products (id, name, description, price, quantity, location, expiry_date, fornecedor, image) 
VALUES (3, 'Alface Crespa Hidroponica', 'Alface crespa fresca, cultivada sem agrotoxicos', 2.80, 25, 'Sao Paulo - SP', '2025-12-18', 1, 'https://images.unsplash.com/photo-1622206151226-18ca2c9ab4a1?w=400&h=300&fit=crop&crop=center');

INSERT INTO products (id, name, description, price, quantity, location, expiry_date, fornecedor, image) 
VALUES (4, 'Tomate Cereja Especial', 'Tomate cereja doce, perfeito para saladas', 5.90, 40, 'Sao Paulo - SP', '2025-12-22', 1, 'https://images.unsplash.com/photo-1592924357228-91a4daadcfea?w=400&h=300&fit=crop&crop=center');

INSERT INTO products (id, name, description, price, quantity, location, expiry_date, fornecedor, image) 
VALUES (5, 'Cenoura Organica Baby', 'Cenoura organica, doce e crocante', 3.20, 35, 'Sao Paulo - SP', '2025-12-30', 1, 'https://images.unsplash.com/photo-1598170845058-32b9d6a5da37?w=400&h=300&fit=crop&crop=center');

INSERT INTO products (id, name, description, price, quantity, location, expiry_date, fornecedor, image) 
VALUES (6, 'Arroz Integral Premium', 'Arroz integral 1kg, rico em fibras', 8.50, 20, 'Sao Paulo - SP', '2025-12-31', 1, 'https://images.unsplash.com/photo-1586201375761-83865001e31c?w=400&h=300&fit=crop&crop=center');

INSERT INTO products (id, name, description, price, quantity, location, expiry_date, fornecedor, image) 
VALUES (7, 'Leite Integral Fresh', 'Leite integral 1L, fonte de calcio', 4.80, 15, 'Sao Paulo - SP', '2025-12-28', 1, 'https://images.unsplash.com/photo-1550583724-b2692b85b150?w=400&h=300&fit=crop&crop=center');

INSERT INTO products (id, name, description, price, quantity, location, expiry_date, fornecedor, image) 
VALUES (8, 'Pao Frances Artesanal', 'Pao frances fresco, crocante por fora', 1.20, 100, 'Sao Paulo - SP', '2025-12-17', 1, 'https://images.unsplash.com/photo-1509440159596-0249088772ff?w=400&h=300&fit=crop&crop=center');

INSERT INTO products (id, name, description, price, quantity, location, expiry_date, fornecedor, image) 
VALUES (9, 'Peito de Frango Premium', 'Peito de frango 1kg, sem hormonios', 12.90, 10, 'Sao Paulo - SP', '2025-12-19', 1, 'https://images.unsplash.com/photo-1604503468506-a8da13d82791?w=400&h=300&fit=crop&crop=center');

INSERT INTO products (id, name, description, price, quantity, location, expiry_date, fornecedor, image) 
VALUES (10, 'Queijo Minas Artesanal', 'Queijo minas frescal, cremoso e saboroso', 15.50, 8, 'Sao Paulo - SP', '2025-12-26', 1, 'https://images.unsplash.com/photo-1552767059-ce182ead6c1b?w=400&h=300&fit=crop&crop=center');

-- Produtos adicionais para diversificar o catalogo
INSERT INTO products (id, name, description, price, quantity, location, expiry_date, fornecedor, image) 
VALUES (11, 'Abacaxi Perola', 'Abacaxi perola doce e suculento', 6.90, 15, 'Sao Paulo - SP', '2025-12-23', 1, 'https://images.unsplash.com/photo-1550258987-190a2d41a8ba?w=400&h=300&fit=crop&crop=center');

INSERT INTO products (id, name, description, price, quantity, location, expiry_date, fornecedor, image) 
VALUES (12, 'Brocolis Organico', 'Brocolis fresco, rico em vitaminas', 4.50, 20, 'Sao Paulo - SP', '2025-12-20', 1, 'https://images.unsplash.com/photo-1459411621453-7b03977f4bfc?w=400&h=300&fit=crop&crop=center');

INSERT INTO products (id, name, description, price, quantity, location, expiry_date, fornecedor, image) 
VALUES (13, 'Batata Doce Roxa', 'Batata doce roxa, fonte de antioxidantes', 3.80, 25, 'Sao Paulo - SP', '2025-01-15', 1, 'https://images.unsplash.com/photo-1586754780923-ca7a8b5c7de8?w=400&h=300&fit=crop&crop=center');

INSERT INTO products (id, name, description, price, quantity, location, expiry_date, fornecedor, image) 
VALUES (14, 'Feijao Preto Premium', 'Feijao preto 1kg, rico em proteinas', 7.20, 30, 'Sao Paulo - SP', '2025-06-30', 1, 'https://images.unsplash.com/photo-1586201375761-83865001e31c?w=400&h=300&fit=crop&crop=center');

INSERT INTO products (id, name, description, price, quantity, location, expiry_date, fornecedor, image) 
VALUES (15, 'Iogurte Natural', 'Iogurte natural cremoso 500g', 5.90, 12, 'Sao Paulo - SP', '2025-12-21', 1, 'https://images.unsplash.com/photo-1571212515416-fac8c2b1e2c9?w=400&h=300&fit=crop&crop=center');

INSERT INTO products (id, name, description, price, quantity, location, expiry_date, fornecedor, image) 
VALUES (16, 'Salmao Fresco', 'File de salmao fresco 500g', 28.90, 5, 'Sao Paulo - SP', '2025-12-18', 1, 'https://images.unsplash.com/photo-1544943910-4c1dc44aab44?w=400&h=300&fit=crop&crop=center');

INSERT INTO products (id, name, description, price, quantity, location, expiry_date, fornecedor, image) 
VALUES (17, 'Massa Integral Penne', 'Macarrao penne integral 500g', 4.90, 40, 'Sao Paulo - SP', '2025-08-15', 1, 'https://images.unsplash.com/photo-1551892374-ecf8754cf8b0?w=400&h=300&fit=crop&crop=center');

INSERT INTO products (id, name, description, price, quantity, location, expiry_date, fornecedor, image) 
VALUES (18, 'Morango Organico', 'Morango organico doce e aromatico', 8.50, 18, 'Sao Paulo - SP', '2025-12-19', 1, 'https://images.unsplash.com/photo-1464965911861-746a04b4bca6?w=400&h=300&fit=crop&crop=center');

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

-- Associações dos novos produtos
INSERT INTO product_categories (product_id, category_id) VALUES (11, 1); -- Abacaxi -> Frutas
INSERT INTO product_categories (product_id, category_id) VALUES (12, 2); -- Brocolis -> Verduras
INSERT INTO product_categories (product_id, category_id) VALUES (13, 3); -- Batata Doce -> Legumes
INSERT INTO product_categories (product_id, category_id) VALUES (14, 4); -- Feijao -> Cereais
INSERT INTO product_categories (product_id, category_id) VALUES (15, 5); -- Iogurte -> Laticinios
INSERT INTO product_categories (product_id, category_id) VALUES (16, 6); -- Salmao -> Carnes
INSERT INTO product_categories (product_id, category_id) VALUES (17, 7); -- Massa -> Paes e Massas
INSERT INTO product_categories (product_id, category_id) VALUES (18, 1); -- Morango -> Frutas
