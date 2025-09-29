<template>
  <div>
    <!-- Splash Screen -->
    <SplashScreen/>

    <!-- Navbar -->
    <Navbar />

    <!-- Conteúdo Principal -->
    <div class="container mt-4">
      <h1 class="mb-4 text-center">Lista de Produtos</h1>
      <div class="row mb-4">
        <div class="col-12 col-md-4 mb-2">
          <el-input
            v-model="searchQuery"
            placeholder="Buscar produtos..."
            suffix-icon="el-icon-search"
            class="form-control"
          />
        </div>
        <div class="col-12 col-md-4 mb-2">
          <el-select v-model="selectedCategory" placeholder="Todas as categorias" class="form-control">
            <el-option value="">Todas as categorias</el-option>
            <el-option
              v-for="category in categories"
              :key="category.id"
              :value="category.name"
            >{{ category.name }}</el-option>
          </el-select>
        </div>
        <div class="col-12 col-md-4 mb-2">
          <el-select v-model="selectedLocation" placeholder="Todas as localizações" class="form-control">
            <el-option value="">Todas as localizações</el-option>
            <el-option
              v-for="location in locations"
              :key="location"
              :value="location"
            >{{ location }}</el-option>
          </el-select>
        </div>
      </div>

      <div class="row">
        <div class="col-12 col-sm-6 col-md-4 mb-4" v-for="product in filteredProducts" :key="product.id">
          <el-card class="shadow-sm">
            <div class="card-img-top d-flex justify-content-center align-items-center" style="height: 150px;">
              <img v-if="product.image && product.image !== ''" :src="product.image" class="img-fluid" :alt="product.name" />
              <i v-else class="fas fa-box fa-3x text-muted"></i>
            </div>
            <div class="card-body">
              <h5 class="card-title">
                <i class="fas fa-box"></i> {{ product.name }}
              </h5>
              <p class="card-text">
                <i class="fas fa-info-circle"></i> {{ product.description }}
              </p>
              <p class="card-text">
                <i class="fas fa-tag"></i> Preço: <strong>R$ {{ parseFloat(product.price).toFixed(2) }}</strong> ({{ product.quantity }} disponível)
              </p>
              <p class="card-text">
                <i class="fas fa-calendar-alt"></i> Validade: {{ product.expiryDate }}
              </p>
              <p class="card-text">
                <i class="fas fa-carrot"></i> Categorias: 
                <span v-for="(category, index) in product.categories" :key="index">
                  {{ category.name }}<span v-if="index < product.categories.length - 1">, </span>
                </span>
              </p>
              <p class="card-text">
                <i class="fas fa-map-marker-alt"></i> Localização: {{ product.location }}
              </p>
              <el-button type="primary" class="w-100" @click="addToCart(product)">
                <i class="fas fa-cart-plus"></i> Adicionar ao Carrinho
              </el-button>
            </div>
          </el-card>
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
      this.$message.success('Produto adicionado ao carrinho!');
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
.card-img-top {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 150px; /* Ajuste a altura das imagens conforme necessário */
  background-color: #f8f9fa; /* Cor de fundo quando não houver imagem */
}
.card-img-top img {
  max-height: 100%; /* Para que a imagem não estoure o limite */
  max-width: 100%; /* Para que a imagem não estoure o limite */
  object-fit: cover; /* Cobre o espaço do card sem distorcer a imagem */
}

@media (max-width: 576px) {
  .card {
    margin-bottom: 20px; /* Adiciona espaçamento em telas menores */
  }
}
</style>
