<template>
  <div>
    <Navbar />
    <div class="container mt-4">
      <h1 class="text-center mb-4">Cadastrar Produto</h1>
      <form @submit.prevent="handleSubmit" class="p-4 border rounded shadow">
        <!-- Campos do formulário -->
        <div class="mb-3">
          <label for="name" class="form-label">
            <i class="fas fa-box"></i> Nome
          </label>
          <input
            type="text"
            id="name"
            v-model="product.name"
            class="form-control"
            placeholder="Digite o nome do produto"
            required
          />
        </div>
        <div class="mb-3">
          <label for="description" class="form-label">
            <i class="fas fa-pencil-alt"></i> Descrição
          </label>
          <textarea
            id="description"
            v-model="product.description"
            class="form-control"
            placeholder="Digite a descrição do produto"
            required
          ></textarea>
        </div>
        <div class="mb-3">
          <label for="price" class="form-label">
            <i class="fas fa-tag"></i> Preço
          </label>
          <input
            type="text"
            id="price"
            v-model="product.price"
            class="form-control"
            placeholder="R$ 0,00"
            required
          />
        </div>
        <div class="mb-3">
          <label for="quantity" class="form-label">
            <i class="fas fa-sort-numeric-up"></i> Quantidade
          </label>
          <input
            type="number"
            id="quantity"
            v-model="product.quantity"
            class="form-control"
            placeholder="Digite a quantidade"
            required
          />
        </div>
        <div class="mb-3">
          <label for="expiryDate" class="form-label">
            <i class="fas fa-calendar-alt"></i> Data de Validade
          </label>
          <input
            type="date"
            id="expiryDate"
            v-model="product.expiryDate"
            class="form-control"
            required
          />
        </div>
        <div class="mb-3">
          <label for="image" class="form-label">
            <i class="fas fa-image"></i> Imagem
          </label>
          <input
            type="file"
            @change="handleFileUpload"
            class="form-control"
            required
          />
          <div v-if="imagePreview" class="mt-2">
            <img :src="imagePreview" alt="Imagem do produto" class="img-thumbnail" style="max-width: 200px;"/>
          </div>
        </div>
        <div class="mb-3">
          <label for="location" class="form-label">
            <i class="fas fa-map-marker-alt"></i> Localização
          </label>
          <input
            type="text"
            id="location"
            v-model="product.location"
            class="form-control"
            placeholder="Digite a localização do produto"
            required
          />
        </div>
        <div class="mb-3">
          <label for="categories" class="form-label">
            <i class="fa-solid fa-list"></i> Categorias
          </label>
          <el-select
            v-model="selectedCategories"
            multiple
            class="form-control"
            clearable
            placeholder="Selecione categorias"
            @change="limitSelection"
          >
            <el-option
              v-for="category in categories"
              :key="category.id"
              :label="category.name"
              :value="category.name"
            />
          </el-select>
          <small class="text-muted">Selecione até 5 categorias.</small>
        </div>
        <button type="submit" class="btn btn-success w-100">
          <i class="fas fa-plus-circle"></i> Cadastrar Produto
        </button>
      </form>
    </div>
  </div>
</template>

<script>
import Navbar from './NavBar.vue';

export default {
  components: {
    Navbar,
  },
  data() {
    return {
      product: {
        name: '',
        description: '',
        price: '',
        quantity: '',
        expiryDate: '',
        location: '',
        categories: [], // Removido, pois será substituído por selectedCategories
        image: null,
      },
      imagePreview: null, // Para mostrar a pré-visualização da imagem
      categories: [], // Lista de categorias do backend
      selectedCategories: [], // Categorias selecionadas
    };
  },
  async created() {
    await this.fetchCategories(); // Chama a função para buscar categorias
  },
  methods: {
    async fetchCategories() {
      try {
        const response = await fetch(`${process.env.VUE_APP_API_MODE}/categories`);
        if (response.ok) {
          this.categories = await response.json();
        } else {
          console.error('Erro ao buscar categorias');
        }
      } catch (error) {
        console.error('Erro de rede ao buscar categorias:', error);
      }
    },
    limitSelection() {
      if (this.selectedCategories.length > 5) {
        this.selectedCategories.pop(); // Remove a última seleção se exceder 5
        this.$message({ message: 'Você pode selecionar até 5 categorias.', type: 'warning' });
      }
    },
    handleFileUpload(event) {
      const file = event.target.files[0];
      if (file) {
        this.product.image = file;

        // Cria uma URL para a pré-visualização do arquivo
        this.imagePreview = URL.createObjectURL(file);
      } else {
        this.imagePreview = null; // Limpa a pré-visualização se nenhum arquivo for selecionado
      }
    },
    async handleSubmit() {
      // Verifique se o usuário está logado
      const user = JSON.parse(localStorage.getItem('user'));

      if (!user) {
        // Se o usuário não estiver logado, avise e redirecione para a tela de login
        this.$message({ message: 'Você precisa estar logado para cadastrar um produto.', type: 'warning' });
        this.$router.push('/login');  // Redireciona para a página de login
        return;
      }

      // Pegue o ID do usuário
      const userId = user.id;

      // Prepare os dados do produto, incluindo o ID do usuário
      const formattedPrice = parseFloat(this.product.price.replace(',', '.'));
      const productData = {
        name: this.product.name,
        description: this.product.description,
        price: formattedPrice,
        quantity: this.product.quantity,
        expiryDate: this.product.expiryDate,
        location: this.product.location,
        categories: this.selectedCategories.map(category => ({ name: category })),
      };

      // Prepare o FormData
      const formData = new FormData();
      formData.append('product', JSON.stringify(productData));  // JSON string
      if (this.product.image) {
        formData.append('image', this.product.image);  // Arquivo de imagem
      }
      formData.append('userId', JSON.stringify(userId));
      try {
        // Envie a requisição para o backend
        const response = await fetch(`${process.env.VUE_APP_API_MODE}/products`, {
          method: 'POST',
          body: formData,  // Enviando como FormData
        });

        if (response.ok) { // Verifica se a resposta é 200
          this.$message({ message: 'Produto cadastrado com sucesso!', type: 'success' });
          this.resetForm();
        } else {
          this.$message({ message: 'Erro ao cadastrar produto!', type: 'error' });
        }
      } catch (error) {
        console.error('Erro ao cadastrar produto:', error);
        this.$message({ message: 'Erro de rede ao cadastrar produto!', type: 'error' });
      }
    },
    resetForm() {
      this.product = {
        name: '',
        description: '',
        price: '',
        quantity: '',
        expiryDate: '',
        location: '',
        image: null,
      };
      this.selectedCategories = []; // Limpa as categorias selecionadas
      this.imagePreview = null; // Limpa a pré-visualização da imagem
    },
  },
};
</script>

<style scoped>
.container {
  max-width: 600px; /* Limitar a largura do formulário */
  margin: auto; /* Centralizar o formulário na página */
}

.form-label {
  font-weight: bold;
}

button {
  transition: background-color 0.3s;
}

button:hover {
  background-color: #218838;
}
</style>
