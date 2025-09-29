<template>
  <div class="products-page">
    <!-- Splash Screen -->
    <SplashScreen/>

    <!-- Navbar -->
    <Navbar />

    <!-- Hero Section -->
    <div class="hero-section">
      <div class="container">
        <div class="hero-content">
          <h1 class="hero-title">
            <i class="fas fa-leaf me-3"></i>
            Produtos Frescos
          </h1>
          <p class="hero-subtitle">
            Descubra alimentos frescos e saudáveis da sua região
          </p>
          <div class="hero-stats">
            <div class="stat-item">
              <span class="stat-number">{{ filteredProducts.length }}</span>
              <span class="stat-label">Produtos</span>
            </div>
            <div class="stat-item">
              <span class="stat-number">{{ categories.length }}</span>
              <span class="stat-label">Categorias</span>
            </div>
            <div class="stat-item">
              <span class="stat-number">{{ locations.length }}</span>
              <span class="stat-label">Locais</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Filtros -->
    <div class="filters-section">
      <div class="container">
        <div class="filters-card">
          <div class="row g-3">
            <div class="col-md-4">
              <div class="filter-group">
                <label class="filter-label">
                  <i class="fas fa-search me-2"></i>Buscar produtos
                </label>
                <el-input
                  v-model="searchQuery"
                  placeholder="Digite o nome do produto..."
                  suffix-icon="el-icon-search"
                  class="filter-input"
                />
              </div>
            </div>
            <div class="col-md-4">
              <div class="filter-group">
                <label class="filter-label">
                  <i class="fas fa-tags me-2"></i>Categoria
                </label>
                <el-select v-model="selectedCategory" placeholder="Todas as categorias" class="filter-select">
                  <el-option value="">Todas as categorias</el-option>
                  <el-option
                    v-for="category in categories"
                    :key="category.id"
                    :value="category.name"
                  >{{ category.name }}</el-option>
                </el-select>
              </div>
            </div>
            <div class="col-md-4">
              <div class="filter-group">
                <label class="filter-label">
                  <i class="fas fa-map-marker-alt me-2"></i>Localização
                </label>
                <el-select v-model="selectedLocation" placeholder="Todas as localizações" class="filter-select">
                  <el-option value="">Todas as localizações</el-option>
                  <el-option
                    v-for="location in locations"
                    :key="location"
                    :value="location"
                  >{{ location }}</el-option>
                </el-select>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Lista de Produtos -->
    <div class="products-section">
      <div class="container">
        <div class="section-header">
          <h2 class="section-title">Produtos Disponíveis</h2>
          <p class="section-subtitle">{{ filteredProducts.length }} produtos encontrados</p>
        </div>

        <div class="products-grid">
          <div 
            v-for="product in filteredProducts" 
            :key="product.id"
            class="product-card-wrapper"
          >
            <div class="product-card">
              <div class="product-image">
                <img 
                  v-if="product.image && product.image !== ''" 
                  :src="product.image" 
                  :alt="product.name"
                />
                <div v-else class="no-image">
                  <i class="fas fa-apple-alt fa-3x"></i>
                </div>
                <div class="product-badge">
                  <span class="badge-text">Fresco</span>
                </div>
              </div>
              
              <div class="product-content">
                <h3 class="product-name">{{ product.name }}</h3>
                <p class="product-description">{{ product.description }}</p>
                
                <div class="product-details">
                  <div class="detail-item">
                    <i class="fas fa-tag text-primary"></i>
                    <span class="detail-label">Preço:</span>
                    <span class="detail-value price">R$ {{ parseFloat(product.price).toFixed(2) }}</span>
                  </div>
                  
                  <div class="detail-item">
                    <i class="fas fa-box text-success"></i>
                    <span class="detail-label">Estoque:</span>
                    <span class="detail-value">{{ product.quantity }} unidades</span>
                  </div>
                  
                  <div class="detail-item">
                    <i class="fas fa-calendar-alt text-warning"></i>
                    <span class="detail-label">Validade:</span>
                    <span class="detail-value">{{ product.expiryDate }}</span>
                  </div>
                  
                  <div class="detail-item">
                    <i class="fas fa-map-marker-alt text-info"></i>
                    <span class="detail-label">Local:</span>
                    <span class="detail-value">{{ product.location }}</span>
                  </div>
                </div>
                
                <div class="product-categories">
                  <span 
                    v-for="(category, index) in product.categories" 
                    :key="index"
                    class="category-tag"
                  >
                    {{ category.name }}
                  </span>
                </div>
                
                <button class="btn-add-cart" @click="addToCart(product)">
                  <i class="fas fa-cart-plus me-2"></i>
                  Adicionar ao Carrinho
                </button>
              </div>
            </div>
          </div>
        </div>

        <!-- Estado Vazio -->
        <div v-if="filteredProducts.length === 0" class="empty-state">
          <div class="empty-content">
            <i class="fas fa-search empty-icon"></i>
            <h3>Nenhum produto encontrado</h3>
            <p>Tente ajustar os filtros ou buscar por outros termos</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Navbar from './NavBar.vue';
import SplashScreen from './SplashScreen.vue';

export default {
  name: 'ProductsList',

  components: {
    Navbar,
    SplashScreen,
  },
  
  data() {
    return {
      apiUrl: process.env.VUE_APP_API_MODE,
      searchQuery: '',
      selectedCategory: '',
      selectedLocation: '',
      products: [], // Array para os produtos
      categories: [], // Categorias como array vazio inicialmente
      locations: [], // Agora começa vazia e será carregada dinamicamente
      cartItems: [] // Array para armazenar os itens do carrinho
    };
  },
  
  computed: {
    filteredProducts() {
      const normalizedSearchQuery = this.normalizeString(this.searchQuery);
      return this.products.filter(product => {
        const normalizedProductName = this.normalizeString(product.name);
        const matchesSearch = normalizedProductName.includes(normalizedSearchQuery);
        const matchesCategory = this.selectedCategory ? product.categories.some(category => category.name === this.selectedCategory) : true;
        const matchesLocation = this.selectedLocation ? product.location === this.selectedLocation : true;
        return matchesSearch && matchesCategory && matchesLocation;
      });
    },
  },
  
  methods: {
    normalizeString(str) {
      return str
        .normalize('NFD') // Normaliza para decompor caracteres acentuados
        .replace(/[\u0300-\u036f]/g, '') // Remove acentos
        .toLowerCase(); // Transforma em minúsculas
    },
    
    addToCart(product) {
      this.cartItems.push(product); // Adiciona o produto ao carrinho
      localStorage.setItem('cartItems', JSON.stringify(this.cartItems)); // Salva no localStorage
      
      // Emitir evento para atualizar contador do carrinho
      this.$root.$emit('cart-updated');
      
      this.showSuccessToast(product);
    },

    showSuccessToast(product) {
      // Criar elemento do toast
      const toast = document.createElement('div');
      toast.className = 'custom-toast';
      toast.innerHTML = `
        <div class="toast-content">
          <div class="toast-icon">
            <i class="fas fa-check-circle"></i>
          </div>
          <div class="toast-message">
            <h4>Produto adicionado!</h4>
            <p><strong>${product.name}</strong> foi adicionado ao seu carrinho</p>
          </div>
          <button class="toast-close" onclick="this.parentElement.parentElement.remove()">
            <i class="fas fa-times"></i>
          </button>
        </div>
        <div class="toast-progress"></div>
      `;

      // Adicionar ao DOM
      document.body.appendChild(toast);

      // Animar entrada
      setTimeout(() => {
        toast.classList.add('show');
      }, 100);

      // Remover automaticamente após 4 segundos
      setTimeout(() => {
        if (toast.parentElement) {
          toast.classList.add('hide');
          setTimeout(() => {
            if (toast.parentElement) {
              toast.remove();
            }
          }, 300);
        }
      }, 4000);
    },

    async loadProducts() {
      try {
        const response = await fetch(`${this.apiUrl}/products`);
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        const data = await response.json();
        console.log(data);
        this.products = data;
        
        // Gera uma lista única de localizações a partir dos produtos
        this.locations = [...new Set(this.products.map(product => product.location))];
      } catch (error) {
        this.$message.error('Erro ao carregar produtos');
        console.error(error);
      }
    },

    async loadCategories() {
      try {
        const response = await fetch(`${this.apiUrl}/categories`); // Certifique-se de que esta URL está correta
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        const data = await response.json();
        this.categories = data; // Atribui as categorias carregadas
      } catch (error) {
        this.$message.error('Erro ao carregar categorias');
        console.error(error); // Mostra o erro no console
      }
    },
  },

  async mounted() {
    await this.loadProducts();
    await this.loadCategories();
  }
};
</script>

<style scoped>
/* Layout Principal */
.products-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
}

/* Hero Section */
.hero-section {
  background: linear-gradient(135deg, #28a745 0%, #20c997 100%);
  color: white;
  padding: 4rem 0;
  position: relative;
  overflow: hidden;
}

.hero-section::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100"><defs><pattern id="grain" width="100" height="100" patternUnits="userSpaceOnUse"><circle cx="25" cy="25" r="1" fill="rgba(255,255,255,0.1)"/><circle cx="75" cy="75" r="1" fill="rgba(255,255,255,0.1)"/></pattern></defs><rect width="100" height="100" fill="url(%23grain)"/></svg>');
  opacity: 0.3;
}

.hero-content {
  text-align: center;
  position: relative;
  z-index: 2;
}

.hero-title {
  font-size: 3.5rem;
  font-weight: 700;
  margin-bottom: 1rem;
  text-shadow: 0 2px 4px rgba(0,0,0,0.2);
}

.hero-subtitle {
  font-size: 1.25rem;
  margin-bottom: 3rem;
  opacity: 0.9;
}

.hero-stats {
  display: flex;
  justify-content: center;
  gap: 3rem;
  flex-wrap: wrap;
}

.stat-item {
  text-align: center;
}

.stat-number {
  display: block;
  font-size: 2.5rem;
  font-weight: 700;
  line-height: 1;
}

.stat-label {
  font-size: 0.9rem;
  opacity: 0.8;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

/* Filtros */
.filters-section {
  padding: 2rem 0;
  margin-top: -2rem;
  position: relative;
  z-index: 3;
}

.filters-card {
  background: white;
  border-radius: 20px;
  padding: 2rem;
  box-shadow: 0 10px 40px rgba(0,0,0,0.1);
  backdrop-filter: blur(10px);
}

.filter-group {
  margin-bottom: 1rem;
}

.filter-label {
  display: block;
  font-weight: 600;
  color: #495057;
  margin-bottom: 0.5rem;
  font-size: 0.9rem;
}

.filter-input,
.filter-select {
  width: 100%;
  border-radius: 12px;
}

/* Seção de Produtos */
.products-section {
  padding: 3rem 0;
}

.section-header {
  text-align: center;
  margin-bottom: 3rem;
}

.section-title {
  font-size: 2.5rem;
  font-weight: 700;
  color: #212529;
  margin-bottom: 0.5rem;
}

.section-subtitle {
  font-size: 1.1rem;
  color: #6c757d;
  margin: 0;
}

/* Grid de Produtos */
.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 2rem;
  margin-bottom: 3rem;
}

.product-card-wrapper {
  animation: fadeInUp 0.6s ease forwards;
}

.product-card {
  background: white;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 8px 30px rgba(0,0,0,0.1);
  transition: all 0.3s ease;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.product-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 20px 50px rgba(0,0,0,0.15);
}

/* Imagem do Produto */
.product-image {
  position: relative;
  height: 200px;
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.product-card:hover .product-image img {
  transform: scale(1.05);
}

.no-image {
  color: #6c757d;
  opacity: 0.5;
}

.product-badge {
  position: absolute;
  top: 1rem;
  right: 1rem;
  background: linear-gradient(135deg, #28a745, #20c997);
  color: white;
  padding: 0.25rem 0.75rem;
  border-radius: 50px;
  font-size: 0.75rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

/* Conteúdo do Produto */
.product-content {
  padding: 1.5rem;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.product-name {
  font-size: 1.25rem;
  font-weight: 700;
  color: #212529;
  margin-bottom: 0.5rem;
  line-height: 1.3;
}

.product-description {
  color: #6c757d;
  margin-bottom: 1rem;
  line-height: 1.5;
  flex: 1;
}

/* Detalhes do Produto */
.product-details {
  margin-bottom: 1rem;
}

.detail-item {
  display: flex;
  align-items: center;
  margin-bottom: 0.5rem;
  font-size: 0.9rem;
}

.detail-item i {
  width: 16px;
  margin-right: 0.5rem;
}

.detail-label {
  color: #6c757d;
  margin-right: 0.5rem;
}

.detail-value {
  font-weight: 600;
  color: #495057;
}

.detail-value.price {
  color: #28a745;
  font-size: 1.1rem;
}

/* Tags de Categoria */
.product-categories {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  margin-bottom: 1.5rem;
}

.category-tag {
  background: rgba(13, 110, 253, 0.1);
  color: #0d6efd;
  padding: 0.25rem 0.75rem;
  border-radius: 50px;
  font-size: 0.75rem;
  font-weight: 500;
}

/* Botão Adicionar */
.btn-add-cart {
  width: 100%;
  background: linear-gradient(135deg, #0d6efd 0%, #0b5ed7 100%);
  color: white;
  border: none;
  padding: 0.75rem 1rem;
  border-radius: 12px;
  font-weight: 600;
  font-size: 0.95rem;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: auto;
}

.btn-add-cart:hover {
  background: linear-gradient(135deg, #0b5ed7 0%, #0a58ca 100%);
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(13, 110, 253, 0.3);
}

/* Estado Vazio */
.empty-state {
  text-align: center;
  padding: 4rem 0;
}

.empty-content {
  max-width: 400px;
  margin: 0 auto;
}

.empty-icon {
  font-size: 4rem;
  color: #6c757d;
  margin-bottom: 1.5rem;
  opacity: 0.5;
}

.empty-content h3 {
  font-size: 1.75rem;
  font-weight: 600;
  color: #495057;
  margin-bottom: 1rem;
}

.empty-content p {
  color: #6c757d;
  font-size: 1.1rem;
}

/* Animações */
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* Responsividade */
@media (max-width: 768px) {
  .hero-title {
    font-size: 2.5rem;
  }
  
  .hero-stats {
    gap: 2rem;
  }
  
  .stat-number {
    font-size: 2rem;
  }
  
  .products-grid {
    grid-template-columns: 1fr;
    gap: 1.5rem;
  }
  
  .filters-card {
    padding: 1.5rem;
  }
  
  .section-title {
    font-size: 2rem;
  }
}

@media (max-width: 576px) {
  .hero-section {
    padding: 3rem 0;
  }
  
  .hero-title {
    font-size: 2rem;
  }
  
  .hero-stats {
    flex-direction: column;
    gap: 1.5rem;
  }
  
  .products-grid {
    grid-template-columns: 1fr;
  }
  
  .product-image {
    height: 180px;
  }
}

/* Toast Personalizado */
:global(.custom-toast) {
  position: fixed;
  top: 20px;
  right: 20px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 10px 40px rgba(0,0,0,0.15);
  min-width: 350px;
  max-width: 400px;
  z-index: 9999;
  transform: translateX(100%);
  opacity: 0;
  transition: all 0.3s cubic-bezier(0.68, -0.55, 0.265, 1.55);
  overflow: hidden;
  border-left: 4px solid #28a745;
}

:global(.custom-toast.show) {
  transform: translateX(0);
  opacity: 1;
}

:global(.custom-toast.hide) {
  transform: translateX(100%);
  opacity: 0;
}

:global(.toast-content) {
  display: flex;
  align-items: flex-start;
  padding: 1.5rem;
  gap: 1rem;
}

:global(.toast-icon) {
  flex-shrink: 0;
  width: 40px;
  height: 40px;
  background: linear-gradient(135deg, #28a745, #20c997);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 1.2rem;
}

:global(.toast-message) {
  flex: 1;
  min-width: 0;
}

:global(.toast-message h4) {
  margin: 0 0 0.5rem 0;
  font-size: 1.1rem;
  font-weight: 700;
  color: #212529;
}

:global(.toast-message p) {
  margin: 0;
  font-size: 0.9rem;
  color: #6c757d;
  line-height: 1.4;
}

:global(.toast-close) {
  flex-shrink: 0;
  width: 24px;
  height: 24px;
  border: none;
  background: rgba(108, 117, 125, 0.1);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #6c757d;
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 0.8rem;
}

:global(.toast-close:hover) {
  background: rgba(220, 53, 69, 0.1);
  color: #dc3545;
  transform: scale(1.1);
}

:global(.toast-progress) {
  position: absolute;
  bottom: 0;
  left: 0;
  height: 3px;
  background: linear-gradient(90deg, #28a745, #20c997);
  width: 100%;
  transform-origin: left;
  animation: toast-progress 4s linear forwards;
}

@keyframes toast-progress {
  from {
    transform: scaleX(1);
  }
  to {
    transform: scaleX(0);
  }
}

/* Responsividade do Toast */
@media (max-width: 480px) {
  :global(.custom-toast) {
    right: 10px;
    left: 10px;
    min-width: auto;
    max-width: none;
  }
  
  :global(.toast-content) {
    padding: 1rem;
  }
  
  :global(.toast-message h4) {
    font-size: 1rem;
  }
  
  :global(.toast-message p) {
    font-size: 0.85rem;
  }
}
</style>
