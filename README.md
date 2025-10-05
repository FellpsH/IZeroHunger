# 🌱 IZero Hunger
Uma plataforma completa para otimizar a distribuição de alimentos, conectando fornecedores e compradores para reduzir o desperdício alimentar e beneficiar comunidades e instituições sociais.

## 📋 Sumário

- [Visão Geral do Projeto](#-visão-geral-do-projeto)
- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Arquitetura do Sistema](#-arquitetura-do-sistema)
- [Frontend](#-frontend)
- [Backend](#-backend)
- [Banco de Dados](#-banco-de-dados)
- [Testes](#-testes)
- [CI/CD e DevOps](#-cicd-e-devops)
- [Docker](#-docker)
- [Instalação e Configuração](#-instalação-e-configuração)
- [API Endpoints](#-api-endpoints)
- [Funcionalidades](#-funcionalidades)
- [Contribuição](#-contribuição)

## 🎯 Visão Geral do Projeto

O **IZero Hunger** é um sistema web desenvolvido para simplificar o gerenciamento e distribuição de alimentos, conectando:

- **Fornecedores**: Podem cadastrar produtos, gerenciar estoque e acompanhar vendas
- **Compradores**: Podem navegar produtos, fazer pedidos e acompanhar entregas
- **Administradores**: Gerenciam usuários, categorias e monitoram o sistema

O sistema implementa autenticação JWT, controle de acesso baseado em roles e uma interface moderna e responsiva.

## 🛠️ Tecnologias Utilizadas

### Frontend
- **Vue.js 2.6.14** - Framework JavaScript progressivo
- **Vue Router 3.6.5** - Roteamento SPA
- **Bootstrap 5.3.3** - Framework CSS responsivo
- **Element UI 2.15.14** - Biblioteca de componentes Vue
- **Font Awesome 6.6.0** - Ícones
- **Axios** - Cliente HTTP para APIs

### Backend
- **Java 17** - Linguagem de programação
- **Spring Boot 3.3.4** - Framework Java
- **Spring Security** - Autenticação e autorização
- **Spring Data JPA** - Persistência de dados
- **JWT (JSON Web Tokens)** - Autenticação stateless
- **Gradle** - Gerenciamento de dependências

### Banco de Dados
- **MySQL** - Banco de dados principal (produção)
- **H2 Database** - Banco em memória (testes)

### Testes
- **JUnit 5** - Framework de testes unitários
- **Mockito** - Framework de mocking
- **Spring Boot Test** - Testes de integração
- **TestContainers** - Testes com containers

## 🏗️ Arquitetura do Sistema

```
┌─────────────────┐    HTTP/REST    ┌─────────────────┐    JPA/Hibernate    ┌─────────────────┐
│                 │ ◄──────────────► │                 │ ◄──────────────────► │                 │
│   Frontend      │                 │    Backend      │                     │   Database      │
│   (Vue.js)      │                 │  (Spring Boot)  │                     │    (MySQL)      │
│                 │                 │                 │                     │                 │
└─────────────────┘                 └─────────────────┘                     └─────────────────┘
```

### Camadas do Backend
- **Controller Layer**: Endpoints REST para comunicação com frontend
- **Service Layer**: Lógica de negócios e regras de validação
- **Repository Layer**: Acesso a dados com Spring Data JPA
- **Security Layer**: Autenticação JWT e controle de acesso
- **Model Layer**: Entidades JPA e DTOs

## 🎨 Frontend

### Estrutura de Componentes
```
src/
├── components/
│   ├── NavBar.vue              # Barra de navegação principal
│   ├── ProductsList.vue        # Lista de produtos com filtros
│   ├── ProductRegistration.vue # Cadastro de produtos (fornecedores)
│   ├── ProductsCart.vue        # Carrinho de compras
│   ├── UserLogin.vue           # Tela de login
│   ├── UserRegistration.vue    # Cadastro de usuários
│   ├── OrderTracking.vue       # Rastreamento de pedidos
│   └── SplashScreen.vue        # Tela de carregamento
├── services/
│   ├── authService.js          # Serviços de autenticação
│   ├── httpClient.js           # Cliente HTTP configurado
│   └── pedidoService.js        # Serviços de pedidos
├── mixins/
│   └── authMixin.js            # Mixin para autenticação
└── router/
    ├── index.js                # Configuração de rotas
    └── authGuard.js            # Guards de autenticação
```

### Funcionalidades do Frontend
- ✅ **Interface Responsiva**: Adaptável para desktop e mobile
- ✅ **Autenticação JWT**: Login seguro com tokens
- ✅ **Controle de Acesso**: Diferentes views para fornecedores e compradores
- ✅ **Carrinho de Compras**: Gerenciamento de itens em tempo real
- ✅ **Filtros Avançados**: Busca por categoria, localização e preço
- ✅ **Upload de Imagens**: Cadastro de produtos com imagens
- ✅ **Validação em Tempo Real**: Feedback imediato nos formulários

### Rotas Principais
- `/` - Lista de produtos (público)
- `/userlogin` - Login de usuários
- `/userregistration` - Cadastro de usuários
- `/productregistration` - Cadastro de produtos (fornecedores)
- `/productscart` - Carrinho de compras
- `/order-tracking` - Rastreamento de pedidos

## ⚙️ Backend

### Estrutura do Projeto
```
src/main/java/com/zeroHunger/demo/
├── controller/           # Controllers REST
│   ├── AuthController.java
│   ├── UsuarioController.java
│   ├── ProductController.java
│   ├── CartController.java
│   └── CategoryController.java
├── service/             # Lógica de negócios
│   ├── UsuarioService.java
│   └── CartService.java
├── repository/          # Acesso a dados
│   ├── UsuarioRepository.java
│   ├── ProductRepository.java
│   ├── CartRepository.java
│   └── CategoryRepository.java
├── model/              # Entidades JPA
│   ├── Usuario.java
│   ├── Product.java
│   ├── Cart.java
│   ├── CartItem.java
│   ├── Category.java
│   ├── Pedido.java
│   └── ItemPedido.java
├── security/           # Configurações de segurança
│   ├── SecurityConfig.java
│   ├── JwtUtil.java
│   ├── JwtAuthenticationFilter.java
│   └── CustomUserDetailsService.java
├── DTO/               # Data Transfer Objects
│   ├── AuthResponse.java
│   ├── LoginRequest.java
│   ├── CartDTO.java
│   └── CompraRequest.java
└── exceptions/        # Tratamento de exceções
    └── ResourceNotFoundException.java
```

### Principais Entidades

#### Usuario
```java
@Entity
public class Usuario {
    private Long id;
    private String email;      // Único
    private String senha;      // Criptografada
    private String nome;
    private LocalDate dataNascimento;
    private String telefone;
    private String endereco;
    private String tipo;       // COMPRADOR, FORNECEDOR, ADMIN
}
```

#### Product
```java
@Entity
public class Product {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer quantity;
    private String expiryDate;
    private String location;
    private String image;      // Base64 ou URL
    private List<Category> categories;
    private Usuario fornecedor;
}
```

#### Cart & CartItem
```java
@Entity
public class Cart {
    private Long id;
    private List<CartItem> items;
    private Usuario user;
    private LocalDateTime purchaseDate;
    private BigDecimal totalPrice;
}
```

## 🗄️ Banco de Dados

### Principais Tabelas
- **usuarios** - Dados dos usuários (compradores, fornecedores, admins)
- **products** - Catálogo de produtos
- **categories** - Categorias de produtos
- **carts** - Carrinhos de compras
- **cart_items** - Itens dos carrinhos
- **pedidos** - Pedidos realizados
- **itens_pedido** - Itens dos pedidos
- **product_categories** - Relacionamento produtos-categorias

### Relacionamentos
- Usuario 1:N Product (fornecedor)
- Usuario 1:N Cart
- Cart 1:N CartItem
- Product N:M Category
- Usuario 1:N Pedido
- Pedido 1:N ItemPedido

## 🧪 Testes

O projeto implementa uma suíte completa de testes com **JUnit 5** e **Mockito**:

### Tipos de Testes Implementados
- ✅ **Testes Unitários** - Services com mocks
- ✅ **Testes de Integração** - Controllers com MockMvc
- ✅ **Testes de Repository** - Com H2 database
- ✅ **Testes Parametrizados** - Múltiplos cenários
- ✅ **Testes de Exceções** - Validação de erros

### Executar Testes
```bash
# Todos os testes
./gradlew test

# Testes específicos
./gradlew test --tests "*UsuarioServiceTest"

# Com relatório detalhado
./gradlew test --info
```

### Cobertura de Testes
- **UsuarioService**: Testes unitários completos
- **CartService**: Testes com mocks avançados
- **Controllers**: Testes de integração HTTP
- **Repositories**: Testes com banco H2

📖 **Guia Completo**: Veja `back/JUNIT_GUIDE.md` para documentação detalhada dos testes.

## 🚀 CI/CD e DevOps

O projeto implementa pipelines simples e eficazes com **GitHub Actions**:

### 🔄 Pipelines Automatizados

#### Backend Build & Test (`backend-ci.yml`)
- ✅ **Build automatizado** com Gradle
- 🧪 **Execução de testes** JUnit 5 + Mockito
- 📊 **Relatórios de testes** automáticos
- ☕ **Java 17** com Temurin

#### Frontend Build & Test (`frontend-ci.yml`)
- ✅ **Build automatizado** com npm
- 🟢 **Node.js 20** com cache
- 📦 **Dependências** otimizadas
- 🏗️ **Build de produção**

### 📊 Benefícios

- ✅ **Builds rápidos** e confiáveis
- ✅ **Testes automatizados** em cada commit
- ✅ **Feedback imediato** em Pull Requests
- ✅ **Cache inteligente** para performance 

## 🐳 Docker

### Desenvolvimento Local
```bash
# Subir toda a stack
docker-compose up -d

# Apenas backend + banco
docker-compose up -d mysql backend

# Logs em tempo real
docker-compose logs -f
```

### Produção
```bash
# Backend
docker build -t izero-hunger-backend ./back
docker run -p 8080:8080 izero-hunger-backend

# Frontend
docker build -t izero-hunger-frontend ./front
docker run -p 80:80 izero-hunger-frontend
```

### 🏗️ Multi-stage Builds
- **Backend**: Gradle build + OpenJDK runtime
- **Frontend**: Node build + Nginx serve
- **Otimização**: Imagens mínimas e seguras
- **Health checks**: Monitoramento automático

## 🚀 Instalação e Configuração

### Pré-requisitos
- **Node.js 18+** e **npm**
- **Java 17+**
- **MySQL 8.0+**
- **Git**

### 1. Clone o Repositório
```bash
git clone https://github.com/ICEI-PUC-Minas-PBR-SI/pbr-si-2024-2-p5-tias-t1-2758101-pbr-si-2024-2-p1-alimentacao-social.git
cd pbr-si-2024-2-p5-tias-t1-2758101-pbr-si-2024-2-p1-alimentacao-social
```

### 2. Configurar Backend
```bash
cd back

# Configurar banco de dados no application.properties
# spring.datasource.url=jdbc:mysql://localhost:3306/zero_hunger
# spring.datasource.username=seu_usuario
# spring.datasource.password=sua_senha

# Executar aplicação
./gradlew bootRun
```

### 3. Configurar Frontend
```bash
cd front

# Instalar dependências
npm install

# Configurar variáveis de ambiente
# Criar arquivo .env.local com:
# VUE_APP_API_MODE=http://localhost:8080/api

# Executar em desenvolvimento
npm run serve
```

### 4. Acessar Aplicação
- **Frontend**: http://localhost:8080
- **Backend API**: http://localhost:8080/api
- **H2 Console** (testes): http://localhost:8080/h2-console

## 🔌 API Endpoints

### Autenticação
```
POST /api/auth/login          # Login com JWT
POST /api/usuarios/criar      # Cadastro de usuário
GET  /api/usuarios/{id}       # Buscar usuário
GET  /api/usuarios/email      # Buscar por email
```

### Produtos
```
GET    /api/products          # Listar produtos
POST   /api/products          # Criar produto (fornecedor)
PUT    /api/products/{id}     # Atualizar produto
DELETE /api/products/{id}     # Deletar produto
GET    /api/categories        # Listar categorias
```

### Carrinho e Pedidos
```
POST /api/carts/{id}/items    # Adicionar item ao carrinho
POST /api/carts/registraCompra # Finalizar compra
GET  /api/pedidos/{id}        # Buscar pedido
```

### Exemplo de Uso da API
```javascript
// Login
const response = await fetch('/api/auth/login', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    email: 'usuario@email.com',
    senha: 'minhasenha'
  })
});

const { token, user } = await response.json();

// Usar token nas próximas requisições
const authHeaders = {
  'Authorization': `Bearer ${token}`,
  'Content-Type': 'application/json'
};
```

## ✨ Funcionalidades

### Para Compradores
- 🛒 **Navegar Produtos**: Visualizar catálogo com filtros
- 🔍 **Busca Avançada**: Por categoria, localização, preço
- 🛍️ **Carrinho de Compras**: Adicionar/remover itens
- 💳 **Finalizar Compra**: Processo de checkout
- 📦 **Rastrear Pedidos**: Acompanhar status de entrega

### Para Fornecedores
- ➕ **Cadastrar Produtos**: Adicionar ao catálogo
- 📊 **Gerenciar Estoque**: Controlar quantidades
- 🏷️ **Definir Categorias**: Organizar produtos
- 📈 **Acompanhar Vendas**: Relatórios de pedidos
- 🖼️ **Upload de Imagens**: Fotos dos produtos

### Para Administradores
- 👥 **Gerenciar Usuários**: CRUD de usuários
- 🏷️ **Gerenciar Categorias**: Organização do sistema
- 📊 **Relatórios**: Métricas do sistema
- 🔒 **Controle de Acesso**: Permissões e roles

### Recursos Técnicos
- 🔐 **Autenticação JWT**: Segurança stateless
- 🛡️ **Controle de Acesso**: Role-based (RBAC)
- 📱 **Design Responsivo**: Mobile-first
- ⚡ **Performance**: Lazy loading e otimizações
- 🧪 **Testes Automatizados**: Cobertura completa
- 📝 **Validação**: Frontend e backend
- 🔄 **Estado Reativo**: Atualizações em tempo real

## 🤝 Contribuição

### Como Contribuir
1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/nova-funcionalidade`)
3. Commit suas mudanças (`git commit -m 'Adiciona nova funcionalidade'`)
4. Push para a branch (`git push origin feature/nova-funcionalidade`)
5. Abra um Pull Request

### Padrões de Código
- **Frontend**: ESLint + Vue Style Guide
- **Backend**: Google Java Style Guide
- **Commits**: Conventional Commits
- **Testes**: Cobertura mínima de 80%

### Estrutura de Commits
```
feat: adiciona nova funcionalidade
fix: corrige bug específico
docs: atualiza documentação
test: adiciona ou modifica testes
refactor: refatora código sem alterar funcionalidade
```

---

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

**🌱 IZero Hunger**
