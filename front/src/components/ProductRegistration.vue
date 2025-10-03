<template>
  <div class="product-registration-page">
    <Navbar />
    
    <!-- Background decorativo -->
    <div class="registration-background">
      <div class="bg-shape shape-1"></div>
      <div class="bg-shape shape-2"></div>
      <div class="bg-shape shape-3"></div>
    </div>

    <div class="registration-container">
      <div class="registration-card">
        <!-- Header do card -->
        <div class="registration-header">
          <div class="registration-logo">
            <i class="fas fa-plus-circle"></i>
          </div>
          <h1 class="registration-title">Cadastrar Produto</h1>
          <p class="registration-subtitle">Adicione um novo produto ao seu catálogo</p>
        </div>

        <!-- Formulário -->
        <form @submit.prevent="handleSubmit" class="registration-form">
          <!-- Alerta de erro -->
          <div v-if="error" class="error-alert">
            <i class="fas fa-exclamation-circle"></i>
            <span>{{ error }}</span>
          </div>

          <!-- Seção de Informações Básicas -->
          <div class="form-section">
            <h3 class="section-title">
              <i class="fas fa-info-circle"></i>
              Informações Básicas
            </h3>
            
            <div class="form-row">
              <div class="form-group">
                <label class="form-label">Nome do Produto</label>
                <div class="input-wrapper" :class="{ 'focused': focusedField === 'name' }">
                  <input
                    v-model="product.name"
                    @focus="focusField('name')"
                    @blur="blurField('name')"
                    type="text"
                    class="form-input"
                    placeholder="Digite o nome do produto"
                    required
                  />
                  <div class="input-border"></div>
                </div>
              </div>

              <div class="form-group">
                <label class="form-label">Preço (R$)</label>
                <div class="input-wrapper" :class="{ 'focused': focusedField === 'price' }">
                  <input
                    v-model="product.price"
                    @focus="focusField('price')"
                    @blur="blurField('price')"
                    type="text"
                    class="form-input"
                    placeholder="0,00"
                    required
                  />
                  <div class="input-border"></div>
                </div>
              </div>
            </div>

            <div class="form-group">
              <label class="form-label">Descrição</label>
              <div class="input-wrapper" :class="{ 'focused': focusedField === 'description' }">
                <textarea
                  v-model="product.description"
                  @focus="focusField('description')"
                  @blur="blurField('description')"
                  class="form-textarea"
                  placeholder="Descreva seu produto detalhadamente"
                  rows="4"
                  required
                ></textarea>
                <div class="input-border"></div>
              </div>
            </div>
          </div>

          <!-- Seção de Estoque e Validade -->
          <div class="form-section">
            <h3 class="section-title">
              <i class="fas fa-warehouse"></i>
              Estoque e Validade
            </h3>
            
            <div class="form-row">
              <div class="form-group">
                <label class="form-label">Quantidade</label>
                <div class="input-wrapper" :class="{ 'focused': focusedField === 'quantity' }">
                  <input
                    v-model="product.quantity"
                    @focus="focusField('quantity')"
                    @blur="blurField('quantity')"
                    type="number"
                    class="form-input"
                    placeholder="0"
                    min="1"
                    required
                  />
                  <div class="input-border"></div>
                </div>
              </div>

              <div class="form-group">
                <label class="form-label">Data de Validade</label>
                <div class="input-wrapper" :class="{ 'focused': focusedField === 'expiryDate' }">
                  <input
                    v-model="product.expiryDate"
                    @focus="focusField('expiryDate')"
                    @blur="blurField('expiryDate')"
                    type="date"
                    class="form-input"
                    required
                  />
                  <div class="input-border"></div>
                </div>
              </div>
            </div>
          </div>

          <!-- Seção de Localização e Categorias -->
          <div class="form-section">
            <h3 class="section-title">
              <i class="fas fa-map-marker-alt"></i>
              Localização e Categorias
            </h3>
            
            <div class="form-group">
              <label class="form-label">Localização</label>
              <div class="input-wrapper" :class="{ 'focused': focusedField === 'location' }">
                <input
                  v-model="product.location"
                  @focus="focusField('location')"
                  @blur="blurField('location')"
                  type="text"
                  class="form-input"
                  placeholder="Cidade - Estado"
                  required
                />
                <div class="input-border"></div>
              </div>
            </div>

            <div class="form-group">
              <label class="form-label">Categorias</label>
              <div class="select-wrapper">
                <el-select
                  v-model="selectedCategories"
                  multiple
                  class="form-select-multiple"
                  clearable
                  placeholder="Selecione até 5 categorias"
                  @change="limitSelection"
                >
                  <el-option
                    v-for="category in categories"
                    :key="category.id"
                    :label="category.name"
                    :value="category.name"
                  />
                </el-select>
              </div>
              <small class="form-hint">Selecione as categorias que melhor descrevem seu produto</small>
            </div>
          </div>

          <!-- Seção de Imagem -->
          <div class="form-section">
            <h3 class="section-title">
              <i class="fas fa-camera"></i>
              Imagem do Produto
            </h3>
            
            <div class="form-group">
              <label class="form-label">Foto do Produto</label>
              <div class="image-upload-wrapper">
                <input
                  type="file"
                  @change="handleFileUpload"
                  class="image-input"
                  accept="image/*"
                  required
                  id="imageUpload"
                />
                <label for="imageUpload" class="image-upload-label">
                  <div v-if="!imagePreview" class="upload-placeholder">
                    <i class="fas fa-cloud-upload-alt"></i>
                    <span>Clique para selecionar uma imagem</span>
                    <small>JPG, PNG ou GIF até 5MB</small>
                  </div>
                  <div v-else class="image-preview">
                    <img :src="imagePreview" alt="Preview" />
                    <div class="image-overlay">
                      <i class="fas fa-edit"></i>
                      <span>Alterar imagem</span>
                    </div>
                  </div>
                </label>
              </div>
            </div>
          </div>

          <!-- Botão de cadastro -->
          <button type="submit" class="registration-button" :disabled="loading">
            <span v-if="loading" class="button-loading">
              <i class="fas fa-spinner fa-spin"></i>
              Cadastrando...
            </span>
            <span v-else class="button-content">
              <i class="fas fa-plus-circle"></i>
              Cadastrar Produto
            </span>
          </button>
        </form>
      </div>
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
      loading: false,
      error: '',
      focusedField: '',
      product: {
        name: '',
        description: '',
        price: '',
        quantity: '',
        expiryDate: '',
        location: '',
        image: null,
      },
      imagePreview: null,
      categories: [],
      selectedCategories: [],
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
          this.error = 'Erro ao carregar categorias';
        }
      } catch (error) {
        this.error = 'Erro de conexão ao carregar categorias';
        console.error('Erro de rede ao buscar categorias:', error);
      }
    },

    limitSelection() {
      if (this.selectedCategories.length > 5) {
        this.selectedCategories.pop();
        this.showWarningToast('Você pode selecionar até 5 categorias');
      }
    },

    handleFileUpload(event) {
      const file = event.target.files[0];
      if (file) {
        // Validar tamanho do arquivo (5MB)
        if (file.size > 5 * 1024 * 1024) {
          this.error = 'A imagem deve ter no máximo 5MB';
          event.target.value = '';
          return;
        }

        // Validar tipo do arquivo
        if (!file.type.startsWith('image/')) {
          this.error = 'Por favor, selecione apenas arquivos de imagem';
          event.target.value = '';
          return;
        }

        this.product.image = file;
        this.imagePreview = URL.createObjectURL(file);
        this.error = '';
      } else {
        this.imagePreview = null;
      }
    },

    focusField(field) {
      this.focusedField = field;
    },

    blurField(field) {
      if (this.focusedField === field) {
        this.focusedField = '';
      }
    },

    validateForm() {
      // Validar campos obrigatórios
      if (!this.product.name || !this.product.description || !this.product.price || 
          !this.product.quantity || !this.product.expiryDate || !this.product.location) {
        this.error = 'Por favor, preencha todos os campos obrigatórios';
        return false;
      }

      // Validar preço
      const price = parseFloat(this.product.price.replace(',', '.'));
      if (isNaN(price) || price <= 0) {
        this.error = 'Por favor, insira um preço válido';
        return false;
      }

      // Validar quantidade
      if (parseInt(this.product.quantity) <= 0) {
        this.error = 'A quantidade deve ser maior que zero';
        return false;
      }

      // Validar data de validade
      const today = new Date();
      const expiryDate = new Date(this.product.expiryDate);
      if (expiryDate <= today) {
        this.error = 'A data de validade deve ser futura';
        return false;
      }

      // Validar imagem
      if (!this.product.image) {
        this.error = 'Por favor, selecione uma imagem do produto';
        return false;
      }

      return true;
    },
    async handleSubmit() {
      // Validar formulário
      if (!this.validateForm()) {
        return;
      }

      this.loading = true;
      this.error = '';

      try {
        // Verificar se o usuário está logado
        const user = JSON.parse(localStorage.getItem('user'));
        if (!user) {
          this.error = 'Você precisa estar logado para cadastrar um produto';
          this.$router.push('/login');
          return;
        }

        // Preparar dados do produto
        const formattedPrice = parseFloat(this.product.price.replace(',', '.'));
        const productData = {
          name: this.product.name,
          description: this.product.description,
          price: formattedPrice,
          quantity: parseInt(this.product.quantity),
          expiryDate: this.product.expiryDate,
          location: this.product.location,
          categories: this.selectedCategories.map(category => ({ name: category })),
        };

        // Preparar FormData
        const formData = new FormData();
        formData.append('product', JSON.stringify(productData));
        formData.append('image', this.product.image);
        formData.append('userId', JSON.stringify(user.id));

        // Enviar requisição
        const response = await fetch(`${process.env.VUE_APP_API_MODE}/products`, {
          method: 'POST',
          body: formData,
        });

        if (response.ok) {
          this.showSuccessToast();
          this.resetForm();
          
          // Redirecionar para lista de produtos após 2 segundos
          setTimeout(() => {
            this.$router.push({ name: 'ProductsList' });
          }, 2000);
        } else {
          const errorData = await response.json().catch(() => ({}));
          throw new Error(errorData.message || 'Erro ao cadastrar produto');
        }
      } catch (error) {
        this.error = error.message;
        console.error('Erro ao cadastrar produto:', error);
      } finally {
        this.loading = false;
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
      this.selectedCategories = [];
      this.imagePreview = null;
      this.error = '';
      this.focusedField = '';
      
      // Limpar input de arquivo
      const fileInput = document.getElementById('imageUpload');
      if (fileInput) {
        fileInput.value = '';
      }
    },

    showSuccessToast() {
      const toast = document.createElement('div');
      toast.className = 'custom-toast success';
      toast.innerHTML = `
        <div class="toast-content">
          <div class="toast-icon">
            <i class="fas fa-check-circle"></i>
          </div>
          <div class="toast-message">
            <h4>Produto cadastrado com sucesso!</h4>
            <p>Você será redirecionado para a lista de produtos</p>
          </div>
        </div>
        <div class="toast-progress"></div>
      `;

      document.body.appendChild(toast);

      setTimeout(() => {
        toast.classList.add('show');
      }, 100);

      setTimeout(() => {
        if (toast.parentElement) {
          toast.classList.add('hide');
          setTimeout(() => {
            if (toast.parentElement) {
              toast.remove();
            }
          }, 300);
        }
      }, 3000);
    },

    showWarningToast(message) {
      const toast = document.createElement('div');
      toast.className = 'custom-toast warning';
      toast.innerHTML = `
        <div class="toast-content">
          <div class="toast-icon">
            <i class="fas fa-exclamation-triangle"></i>
          </div>
          <div class="toast-message">
            <h4>Atenção!</h4>
            <p>${message}</p>
          </div>
        </div>
        <div class="toast-progress"></div>
      `;

      document.body.appendChild(toast);

      setTimeout(() => {
        toast.classList.add('show');
      }, 100);

      setTimeout(() => {
        if (toast.parentElement) {
          toast.classList.add('hide');
          setTimeout(() => {
            if (toast.parentElement) {
              toast.remove();
            }
          }, 300);
        }
      }, 3000);
    }
  },
};
</script>

<style scoped>
/* Layout Principal */
.product-registration-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #28a745 0%, #20c997 100%);
  position: relative;
  overflow-x: hidden;
}

/* Background Decorativo */
.registration-background {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 1;
}

.bg-shape {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  animation: float 6s ease-in-out infinite;
}

.shape-1 {
  width: 300px;
  height: 300px;
  top: -150px;
  right: -150px;
  animation-delay: 0s;
}

.shape-2 {
  width: 200px;
  height: 200px;
  bottom: -100px;
  left: -100px;
  animation-delay: 2s;
}

.shape-3 {
  width: 150px;
  height: 150px;
  top: 50%;
  left: -75px;
  animation-delay: 4s;
}

@keyframes float {
  0%, 100% {
    transform: translateY(0px) rotate(0deg);
  }
  50% {
    transform: translateY(-20px) rotate(10deg);
  }
}

/* Container Principal */
.registration-container {
  position: relative;
  z-index: 2;
  padding: 2rem 1rem;
  max-width: 900px;
  margin: 0 auto;
}

/* Card de Registro */
.registration-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 24px;
  padding: 3rem;
  box-shadow: 0 25px 50px rgba(0, 0, 0, 0.15);
  border: 1px solid rgba(255, 255, 255, 0.2);
  animation: slideInUp 0.8s cubic-bezier(0.68, -0.55, 0.265, 1.55);
  margin-top: 2rem;
}

@keyframes slideInUp {
  from {
    opacity: 0;
    transform: translateY(50px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* Header */
.registration-header {
  text-align: center;
  margin-bottom: 3rem;
}

.registration-logo {
  width: 80px;
  height: 80px;
  background: linear-gradient(135deg, #28a745, #20c997);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 1.5rem;
  color: white;
  font-size: 2rem;
  box-shadow: 0 10px 30px rgba(40, 167, 69, 0.3);
}

.registration-title {
  font-size: 2.5rem;
  font-weight: 700;
  color: #212529;
  margin-bottom: 0.5rem;
  background: linear-gradient(135deg, #28a745, #20c997);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.registration-subtitle {
  font-size: 1.1rem;
  color: #6c757d;
  margin: 0;
}

/* Formulário */
.registration-form {
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

/* Alerta de Erro */
.error-alert {
  background: linear-gradient(135deg, #ff6b6b, #ee5a52);
  color: white;
  padding: 1rem 1.5rem;
  border-radius: 12px;
  display: flex;
  align-items: center;
  gap: 0.75rem;
  font-weight: 500;
  box-shadow: 0 4px 15px rgba(255, 107, 107, 0.3);
  animation: shake 0.5s ease-in-out;
}

@keyframes shake {
  0%, 100% { transform: translateX(0); }
  25% { transform: translateX(-5px); }
  75% { transform: translateX(5px); }
}

.error-alert i {
  font-size: 1.2rem;
}

/* Seções do Formulário */
.form-section {
  background: rgba(248, 249, 250, 0.8);
  border-radius: 16px;
  padding: 2rem;
  border: 1px solid rgba(0, 0, 0, 0.05);
}

.section-title {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  font-size: 1.3rem;
  font-weight: 600;
  color: #495057;
  margin-bottom: 1.5rem;
  padding-bottom: 0.75rem;
  border-bottom: 2px solid rgba(40, 167, 69, 0.2);
}

.section-title i {
  color: #28a745;
  font-size: 1.2rem;
}

/* Layout de Linhas */
.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1.5rem;
  margin-bottom: 1.5rem;
}

.form-row:last-child {
  margin-bottom: 0;
}

/* Grupos de Formulário */
.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  margin-bottom: 1.5rem;
}

.form-group:last-child {
  margin-bottom: 0;
}

.form-label {
  font-weight: 600;
  color: #495057;
  font-size: 0.95rem;
  margin-bottom: 0.5rem;
}

/* Input Wrapper */
.input-wrapper {
  position: relative;
  background: white;
  border-radius: 12px;
  border: 2px solid #e9ecef;
  transition: all 0.3s ease;
  overflow: hidden;
}

.input-wrapper:hover {
  border-color: #28a745;
  box-shadow: 0 4px 15px rgba(40, 167, 69, 0.1);
}

.input-wrapper.focused {
  border-color: #28a745;
  box-shadow: 0 0 0 3px rgba(40, 167, 69, 0.1);
}

.input-wrapper.focused .input-border {
  transform: scaleX(1);
}

/* Inputs */
.form-input,
.form-textarea {
  width: 100%;
  padding: 1rem 1.25rem;
  border: none;
  background: transparent;
  font-size: 1rem;
  color: #495057;
  outline: none;
  transition: all 0.3s ease;
  resize: none;
}

.form-input::placeholder,
.form-textarea::placeholder {
  color: #adb5bd;
  transition: all 0.3s ease;
}

.form-input:focus::placeholder,
.form-textarea:focus::placeholder {
  color: #ced4da;
  transform: translateY(-2px);
}

/* Borda Animada */
.input-border {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 2px;
  background: linear-gradient(135deg, #28a745, #20c997);
  transform: scaleX(0);
  transition: transform 0.3s ease;
  transform-origin: left;
}

/* Select Personalizado */
.select-wrapper {
  position: relative;
}

.form-select-multiple {
  width: 100%;
  border-radius: 12px;
  border: 2px solid #e9ecef;
  transition: all 0.3s ease;
}

.form-select-multiple:hover {
  border-color: #28a745;
  box-shadow: 0 4px 15px rgba(40, 167, 69, 0.1);
}

.form-hint {
  color: #6c757d;
  font-size: 0.875rem;
  margin-top: 0.5rem;
  font-style: italic;
}

/* Upload de Imagem */
.image-upload-wrapper {
  position: relative;
}

.image-input {
  display: none;
}

.image-upload-label {
  display: block;
  cursor: pointer;
  border: 2px dashed #e9ecef;
  border-radius: 12px;
  transition: all 0.3s ease;
  overflow: hidden;
}

.image-upload-label:hover {
  border-color: #28a745;
  background: rgba(40, 167, 69, 0.05);
}

.upload-placeholder {
  padding: 3rem 2rem;
  text-align: center;
  color: #6c757d;
}

.upload-placeholder i {
  font-size: 3rem;
  color: #28a745;
  margin-bottom: 1rem;
  display: block;
}

.upload-placeholder span {
  display: block;
  font-size: 1.1rem;
  font-weight: 600;
  margin-bottom: 0.5rem;
  color: #495057;
}

.upload-placeholder small {
  font-size: 0.875rem;
  color: #6c757d;
}

.image-preview {
  position: relative;
  width: 100%;
  height: 200px;
  overflow: hidden;
}

.image-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.image-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.7);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: white;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.image-preview:hover .image-overlay {
  opacity: 1;
}

.image-overlay i {
  font-size: 2rem;
  margin-bottom: 0.5rem;
}

.image-overlay span {
  font-weight: 600;
}

/* Botão de Registro */
.registration-button {
  background: linear-gradient(135deg, #28a745 0%, #20c997 100%);
  border: none;
  border-radius: 12px;
  padding: 1rem 2rem;
  color: white;
  font-size: 1.1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(40, 167, 69, 0.4);
  margin-top: 1rem;
  position: relative;
  overflow: hidden;
}

.registration-button:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(40, 167, 69, 0.5);
}

.registration-button:active {
  transform: translateY(0);
}

.registration-button:disabled {
  opacity: 0.7;
  cursor: not-allowed;
  transform: none;
}

.button-content,
.button-loading {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.75rem;
}

.button-loading i {
  animation: spin 1s linear infinite;
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

/* Toast Personalizado */
:global(.custom-toast.success) {
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

:global(.custom-toast.warning) {
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
  border-left: 4px solid #ffc107;
}

:global(.custom-toast.success.show),
:global(.custom-toast.warning.show) {
  transform: translateX(0);
  opacity: 1;
}

:global(.custom-toast.success.hide),
:global(.custom-toast.warning.hide) {
  transform: translateX(100%);
  opacity: 0;
}

:global(.custom-toast .toast-content) {
  display: flex;
  align-items: flex-start;
  padding: 1.5rem;
  gap: 1rem;
}

:global(.custom-toast.success .toast-icon) {
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

:global(.custom-toast.warning .toast-icon) {
  flex-shrink: 0;
  width: 40px;
  height: 40px;
  background: linear-gradient(135deg, #ffc107, #fd7e14);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 1.2rem;
}

:global(.custom-toast .toast-message) {
  flex: 1;
  min-width: 0;
}

:global(.custom-toast .toast-message h4) {
  margin: 0 0 0.5rem 0;
  font-size: 1.1rem;
  font-weight: 700;
  color: #212529;
}

:global(.custom-toast .toast-message p) {
  margin: 0;
  font-size: 0.9rem;
  color: #6c757d;
  line-height: 1.4;
}

:global(.custom-toast .toast-progress) {
  position: absolute;
  bottom: 0;
  left: 0;
  height: 3px;
  width: 100%;
  transform-origin: left;
  animation: toast-progress 3s linear forwards;
}

:global(.custom-toast.success .toast-progress) {
  background: linear-gradient(90deg, #28a745, #20c997);
}

:global(.custom-toast.warning .toast-progress) {
  background: linear-gradient(90deg, #ffc107, #fd7e14);
}

@keyframes toast-progress {
  from {
    transform: scaleX(1);
  }
  to {
    transform: scaleX(0);
  }
}

/* Responsividade */
@media (max-width: 768px) {
  .registration-container {
    padding: 1rem;
  }

  .registration-card {
    padding: 2rem 1.5rem;
    margin-top: 1rem;
  }

  .registration-title {
    font-size: 2rem;
  }

  .form-row {
    grid-template-columns: 1fr;
    gap: 1rem;
  }

  .form-section {
    padding: 1.5rem;
  }

  .section-title {
    font-size: 1.2rem;
  }
}

@media (max-width: 480px) {
  .registration-card {
    padding: 1.5rem 1rem;
    border-radius: 16px;
  }

  .registration-title {
    font-size: 1.8rem;
  }

  .registration-subtitle {
    font-size: 1rem;
  }

  .form-input,
  .form-textarea {
    padding: 0.875rem 1rem;
    font-size: 0.95rem;
  }

  .registration-button {
    padding: 0.875rem 1.5rem;
    font-size: 1rem;
  }

  :global(.custom-toast.success),
  :global(.custom-toast.warning) {
    right: 10px;
    left: 10px;
    min-width: auto;
    max-width: none;
  }
}
</style>
