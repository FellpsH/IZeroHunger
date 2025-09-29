<template>
  <div>
    <Navbar />
    <div class="container mt-4">
      <h1 class="text-center mb-4">Carrinho!</h1>
      <div v-if="cartItems.length > 0">
        <ul class="list-group mb-4">
          <li
            v-for="item in cartItems"
            :key="item.id"
            class="list-group-item d-flex justify-content-between align-items-center flex-column flex-md-row"
          >
            <div class="d-flex align-items-center mb-3 mb-md-0">
              <img 
                v-if="item.image" 
                :src="item.image" 
                alt="Imagem do produto" 
                class="img-thumbnail me-3" 
                style="width: 80px; height: 80px; object-fit: cover;"
              />
              <div>
                <h5 class="mb-1">{{ item.name }}</h5>
                <p class="mb-0"><i class="fas fa-tag"></i> Preço: R$ {{ formatPrice(item.price) }}</p>
                <p class="mb-0"><i class="fas fa-box"></i> Disponível: {{ item.quantity }} unidades</p>
              </div>
            </div>
            <div class="input-group" style="width: 180px;">
              <button
                class="btn btn-outline-secondary"
                @click="decreaseQuantity(item)"
                :disabled="item.selectedQuantity <= 1"
              >
                <i class="fas fa-minus"></i>
              </button>
              <input
                type="number"
                class="form-control"
                v-model.number="item.selectedQuantity"
                min="1"
                :max="item.quantity"
                @input="updateTotal"
                @change="validateQuantity(item)"
              />
              <button
                class="btn btn-outline-secondary"
                @click="increaseQuantity(item)"
                :disabled="item.selectedQuantity >= item.quantity"
              >
                <i class="fas fa-plus"></i>
              </button>
            </div>
            <button class="btn btn-danger ms-2" @click="removeItem(item)">
              <i class="fas fa-trash-alt"></i> Remover
            </button>
          </li>
        </ul>
        <div class="d-flex justify-content-between align-items-center mb-3">
          <h4>Total: R$ {{ formatPrice(total) }}</h4>
          <div class="d-flex">
            <button class="btn btn-primary me-2" @click="openFinalizeModal">
              <i class="fas fa-check"></i> Finalizar Compra
            </button>
            <button class="btn btn-danger" @click="clearCart">
              <i class="fas fa-trash"></i> Limpar Carrinho
            </button>
          </div>
        </div>
      </div>
      <div v-else>
        <p class="text-center">O carrinho está vazio.</p>
      </div>
    </div>

    <!-- Modal de Finalização de Compra -->
    <el-dialog :visible.sync="isFinalizeModalVisible" width="80%" title="Finalizar Compra"  @close="isLoadingModalVisible = false">
      <p>Você precisa de uma conta para continuar a compra.</p>
      <p>Deseja usar sua conta existente ou criar uma nova conta?</p>
      <div class="d-flex justify-content-center mt-4">
        <button class="btn btn-primary me-2" @click="showLoginModal">
          <i class="fas fa-sign-in-alt"></i> Usar minha conta
        </button>
        <button class="btn btn-success" @click="showRegistrationModal">
          <i class="fas fa-user-plus"></i> Criar nova conta
        </button>
      </div>
    </el-dialog>

    <!-- Modais de Login e Cadastro -->
    <el-dialog :visible.sync="isLoginModalVisible" width="80%" title="Login">
      <UserLogin @login-success="onLoginSuccess" />
    </el-dialog>

    <el-dialog :visible.sync="isRegistrationModalVisible" width="80%" title="Criar Conta">
      <UserRegistration />
    </el-dialog>

    <!-- Modal de Carregamento/Confirmação -->
    <el-dialog :visible.sync="isLoadingModalVisible" width="400px" :show-close="false" custom-class="loading-modal"  @close="redirectToProductList">
      <div v-if="isLoading" class="loading-content">
        <div class="spinner"></div>
        <p class="loading-text">Processando seu pedido...</p>
      </div>
      <div v-else class="success-content">
        <div class="checkmark-animation">
          <span class="checkmark">
            <div class="checkmark-stem"></div>
            <div class="checkmark-kick"></div>
          </span>
        </div>
        <p class="success-text">Compra finalizada com sucesso!</p>
        <p class="success-text">
          Seu pedido será entregue no seguinte endereço:
        </p>
        <p class="address-text">{{ userAddress }}</p>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Navbar from './NavBar.vue';
import UserLogin from './UserLogin.vue';
import UserRegistration from './UserRegistration.vue';

export default {
  name: 'ProductsCart',
  components: {
    Navbar,
    UserLogin,
    UserRegistration,
  },
  data() {
    return {
      cartItems: [],
      total: 0,
      isFinalizeModalVisible: false,
      isLoginModalVisible: false,
      isRegistrationModalVisible: false,
      isLoggedIn: false,
      isLoading: true,
      isLoadingModalVisible: false,
      userAddress: '',
    };
  },
  mounted() {
    const storedCartItems = localStorage.getItem('cartItems');
    const userString = localStorage.getItem('user');
    
    // Verificar se existe dados do usuário no localStorage
    let user = null;
    if (userString) {
      try {
        user = JSON.parse(userString);
      } catch (error) {
        console.error('Erro ao fazer parse do usuário:', error);
      }
    }
    
    // Atualiza flag de login
    this.isLoggedIn = !!user;
    
    // Verificar se o usuário existe antes de acessar suas propriedades
    if (user && user.endereco) {
      console.log(user.endereco);
      this.userAddress = user.endereco;
    } else {
      console.log('Usuário não encontrado ou sem endereço');
      this.userAddress = 'Endereço não informado';
    }
    if (storedCartItems) {
      this.cartItems = JSON.parse(storedCartItems).map(item => ({
        ...item,
        selectedQuantity: 1,
        price: typeof item.price === 'string' ? parseFloat(item.price.replace('R$', '').replace(',', '.')) : item.price,
      }));
      this.calculateTotal();
    }
  },
  methods: {
    formatPrice(value) {
      return (Number(value) || 0).toFixed(2);
    },
    updateTotal() {
      this.calculateTotal(); 
    },
    validateQuantity(item) {
      if (item.selectedQuantity > item.quantity) {
        item.selectedQuantity = item.quantity;
      }
      if (item.selectedQuantity < 1) {
        item.selectedQuantity = 1;
      }
      this.updateTotal();
    },
    calculateTotal() {
      this.total = this.cartItems.reduce((sum, item) => {
        return sum + (Number(item.price) || 0) * (item.selectedQuantity || 1);
      }, 0);
    },
    increaseQuantity(item) {
      if (item.selectedQuantity < item.quantity) {
        item.selectedQuantity++;
        this.updateTotal();
      }
    },
    decreaseQuantity(item) {
      if (item.selectedQuantity > 1) {
        item.selectedQuantity--;
        this.updateTotal();
      }
    },
    removeItem(item) {
      this.cartItems = this.cartItems.filter(i => i.id !== item.id);
      this.calculateTotal();
      localStorage.setItem('cartItems', JSON.stringify(this.cartItems));
    },
    clearCart() {
      this.cartItems = [];
      localStorage.removeItem('cartItems');
      this.total = 0;
    },
  
    openFinalizeModal() {
      if (this.isLoggedIn) {
        // Usuário já autenticado: segue direto para finalizar
        this.finalizePurchase();
      } else {
        // Usuário não autenticado: mostra modal para login/cadastro
        this.isFinalizeModalVisible = true;
      }
    },
    showLoginModal() {
      this.isFinalizeModalVisible = false;
      this.isLoginModalVisible = true;
    },
    showRegistrationModal() {
      this.isFinalizeModalVisible = false;
      this.isRegistrationModalVisible = true;
    },

    redirectToProductList() {
      this.clearCart(); // Limpa o carrinho
      this.$router.push('/productslist'); // Redireciona para a lista de produtos
    },

    async finalizePurchase() {
      this.isLoginModalVisible = false;
      this.isLoadingModalVisible = true;
      this.isLoading = true;

      try {
        const user = JSON.parse(localStorage.getItem('user'));
        const token = localStorage.getItem('jwt_token');
        const apiBase = process.env.VUE_APP_API_URL || process.env.VUE_APP_API_MODE || 'http://localhost:8080/api';
        const response = await fetch(`${apiBase}/carts/registraCompra`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                ...(token ? { 'Authorization': `Bearer ${token}` } : {}),
            },
            body: JSON.stringify({
                userId: user.id,
                totalPrice: this.total,
                items: this.cartItems.map(item => ({
                    productId: item.id,
                    quantity: item.selectedQuantity,
                })),
            }),
        });

          if (!response.ok) {
              throw new Error('Erro ao finalizar a compra');
          }

          await new Promise(resolve => setTimeout(resolve, 400));
          this.isLoading = false;
      } catch (error) {
          this.$message.error('Erro ao processar compra');
          console.error(error);
          this.isLoading = false;
      }
    },
  
    // Método chamado após o login do usuário
    onLoginSuccess() {
      this.isLoggedIn = true;
      this.isLoginModalVisible = false;
      this.finalizePurchase(); // Finaliza a compra após o login
    },
  },
};
</script>


<style>
/* Adicionando responsividade com breakpoints */
@media (max-width: 768px) {
  .container {
    padding-left: 15px;
    padding-right: 15px;
  }

  .list-group-item {
    flex-direction: column;
    align-items: flex-start;
    margin-bottom: 10px;
  }

  .list-group-item img {
    width: 60px;
    height: 60px;
  }

  .input-group {
    width: 100%;
    margin-top: 10px;
  }

  .input-group button {
    width: 40px;
  }

  .input-group input {
    width: 60px;
  }

  .d-flex {
    flex-direction: column;
    align-items: flex-start;
  }

  .d-flex button {
    width: 100%;
    margin-top: 5px;
  }
}

@media (max-width: 480px) {
  .list-group-item h5 {
    font-size: 1rem;
  }

  .list-group-item p {
    font-size: 0.875rem;
  }

  .btn {
    width: 100%;
    padding: 10px;
  }

  .btn-primary,
  .btn-danger {
    font-size: 1rem;
  }
}

.list-group-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border: 1px solid #ced4da;
  border-radius: 0.375rem;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-bottom: 15px;
}

input[type="number"]::-webkit-outer-spin-button,
input[type="number"]::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

input[type=number] {
  -moz-appearance: textfield;
}

h1 {
  font-family: 'Arial', sans-serif;
  font-weight: bold;
  font-size: 2rem;
}

h4 {
  font-family: 'Arial', sans-serif;
  font-weight: bold;
  font-size: 1.5rem;
}

.btn {
  transition: background-color 0.3s, color 0.3s;
  width: auto;
}

.btn-primary:hover {
  background-color: #0056b3;
}

.btn-danger:hover {
  background-color: #c82333;
}

/* Estilo para o modal de carregamento e sucesso */
.loading-modal .el-dialog__body {
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
  padding: 30px;
}

.loading-content,
.success-content {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.loading-text,
.success-text {
  font-size: 1.2rem;
  font-weight: 500;
  margin-top: 10px;
  color: #555;
}

/* Spinner customizado */
.spinner {
  border: 4px solid rgba(0, 0, 0, 0.1);
  border-top-color: #007bff;
  border-radius: 50%;
  width: 50px;
  height: 50px;
  animation: spin 1s infinite linear;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* Novo estilo de animação para checkmark */
.success-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.checkmark {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  display: inline-block;
  position: relative;
  background-color: transparent;
  border: 4px solid #28a745;
  box-sizing: border-box;
  animation: checkmarkCircle 0.6s ease forwards;
}

@keyframes checkmarkCircle {
  0% {
    transform: scale(0);
    opacity: 0;
  }
  50% {
    opacity: 1;
    transform: scale(1.1);
  }
  100% {
    transform: scale(1);
  }
}

.checkmark::before {
  content: '';
  position: absolute;
  width: 8px;
  height: 24px;
  border-right: 4px solid #28a745;
  border-bottom: 4px solid #28a745;
  top: 14px;
  left: 18px;
  transform: rotate(45deg);
  transform-origin: bottom left;
  animation: checkmarkStroke 0.5s ease forwards 0.6s;
}

@keyframes checkmarkStroke {
  0% {
    width: 0;
    height: 0;
  }
  100% {
    width: 8px;
    height: 24px;
  }
}

.success-text {
  font-size: 1.2rem;
  font-weight: 500;
  margin-top: 15px;
  color: #28a745;
}

</style>