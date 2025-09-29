# Alimentação Social

Este repositório abriga o projeto "Alimentação Social", criado para otimizar a distribuição de alimentos, beneficiando comunidades e instituições sociais. O projeto é composto por duas partes principais: frontend e backend, empregando tecnologias modernas para oferecer uma experiência de usuário intuitiva e uma arquitetura de software robusta.

## Sumário

- [Visão Geral do Projeto](#visão-geral-do-projeto)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Frontend](#frontend)
- [Backend](#backend)
- [Instalação](#instalação)

## Visão Geral do Projeto

O sistema foi desenvolvido com o intuito de simplificar o gerenciamento de fornecimento de alimentos, a distribuição. O frontend oferece uma interface amigável para os usuários, enquanto o backend lida com a lógica de negócios e armazenamento de dados. 

## Tecnologias Utilizadas

- **Frontend:** Vue.js, Element UI, Bootstrap, Font Awesome Icons
- **Backend:** Java, Spring Boot, JPA (Java Persistence API)
- **Banco de Dados:** MySQL
- **Outras Ferramentas:** Git, GitHub, JSON para troca de dados, HTML5, CSS3, JavaScript

## Frontend

A aplicação frontend foi construída utilizando o Vue.js, um framework JavaScript progressivo que facilita a criação de interfaces dinâmicas. As bibliotecas **Element UI** e **Bootstrap** foram usadas para estilizar e fornecer componentes pré-construídos, permitindo um desenvolvimento mais rápido e uma aparência visualmente agradável.

### Funcionalidades Principais

- **Carrinho de Compras:** A interface do usuário permite adicionar e remover itens de um carrinho de compras, além de visualizar o total atualizado em tempo real.
- **Validação de Dados em Tempo Real:** Validações de formulário são feitas via JavaScript para assegurar que os dados sejam corretos antes de serem enviados ao backend.
- **API de Integração:** O frontend se comunica com a API do backend para buscar e enviar dados, mantendo o estado sincronizado entre cliente e servidor.

### Estrutura do Projeto

- **`src/components`**: Contém os componentes Vue reutilizáveis, como `ProductsCart.vue` para o gerenciamento do carrinho de compras.
- **`src/services`**: Contém serviços para lidar com a comunicação com a API do backend. (Teste com json-server sem a nessecidade de subir o Back-End e o Banco de Dados) 

## Backend

O backend foi desenvolvido em Java, utilizando o framework Spring Boot para simplificar a criação de serviços RESTful. A aplicação se conecta a um banco de dados MySQL para armazenar e recuperar informações, como usuários, produtos, e transações financeiras.

### Funcionalidades Principais

- **Controle de Faturamento:** Gera faturas automaticamente e preenche dados como o profissional executante e o status padrão.
- **Validação e Autenticação:** Validação de dados de entrada e controle de acesso para assegurar a segurança e integridade do sistema.
- **Scripts SQL e Auditoria:** Scripts são gerados para tabelas e suas versões de auditoria, para manter um histórico de mudanças de dados.

### Estrutura do Projeto

- **`src/main/java`**: Contém o código Java dividido em pacotes para controladores, serviços, repositórios e entidades.
- **`src/main/resources`**: Inclui arquivos de configuração, como `application.properties` para a conexão com o banco de dados.
- **Entidades Principais**: `Products`, `Usuarios`, entre outras, com mapeamentos JPA para o banco de dados.

## Instalação

### Pré-requisitos

- **Node.js 18** e **npm**: Necessários para rodar o frontend.
- **Java 11+**: Necessário para rodar o backend.
- **MySQL**: Para o banco de dados.
- **Git**: Para clonar o repositório.

### Passos para Instalação

1. **Clone o Repositório:**
   ```bash
   git clone https://github.com/ICEI-PUC-Minas-PBR-SI/pbr-si-2024-2-p5-tias-t1-2758101-pbr-si-2024-2-p1-alimentacao-social.git
   cd pbr-si-2024-2-p5-tias-t1-2758101-pbr-si-2024-2-p1-alimentacao-social
