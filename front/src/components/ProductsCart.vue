<template>
  <div class="cart-page">
    <Navbar />
    
    <!-- Header do Carrinho -->
    <div class="cart-header">
      <div class="container">
        <div class="row align-items-center">
          <div class="col-md-8">
            <h1 class="cart-title">
              <i class="fas fa-shopping-cart me-3"></i>
              Meu Carrinho
            </h1>
            <p class="cart-subtitle" v-if="cartItems.length > 0">
              {{ cartItems.length }} {{ cartItems.length === 1 ? 'item' : 'itens' }} no seu carrinho
            </p>
          </div>
          <div class="col-md-4 text-end" v-if="cartItems.length > 0">
            <button class="btn btn-clear-cart" @click="clearCart">
              <i class="fas fa-trash-alt me-2"></i>
              <span>Limpar Carrinho</span>
            </button>
          </div>
        </div>
      </div>
    </div>

    <div class="container py-4">
      <!-- Itens do Carrinho -->
      <div v-if="cartItems.length > 0" class="row">
        <div class="col-lg-8">
          <div class="cart-items">
            <div
              v-for="item in cartItems"
              :key="item.id"
              class="cart-item"
            >
              <div class="item-image">
                <img 
                  v-if="item.image" 
                  :src="item.image" 
                  alt="Imagem do produto"
                />
                <div v-else class="no-image">
                  <i class="fas fa-box fa-2x"></i>
                </div>
              </div>
              
              <div class="item-details">
                <h5 class="item-name">{{ item.name }}</h5>
                <p class="item-price">
                  <i class="fas fa-tag me-1"></i>
                  R$ {{ formatPrice(item.price) }}
                </p>
                <p class="item-stock">
                  <i class="fas fa-box me-1"></i>
                  {{ item.quantity }} disponível
                </p>
              </div>
              
              <div class="item-controls">
                <div class="quantity-control">
                  <button
                    class="btn-quantity"
                    @click="decreaseQuantity(item)"
                    :disabled="item.selectedQuantity <= 1"
                  >
                    <i class="fas fa-minus"></i>
                  </button>
                  <input
                    type="number"
                    class="quantity-input"
                    v-model.number="item.selectedQuantity"
                    min="1"
                    :max="item.quantity"
                    @input="updateTotal"
                    @change="validateQuantity(item)"
                  />
                  <button
                    class="btn-quantity"
                    @click="increaseQuantity(item)"
                    :disabled="item.selectedQuantity >= item.quantity"
                  >
                    <i class="fas fa-plus"></i>
                  </button>
                </div>
                
                <div class="item-total">
                  R$ {{ formatPrice(item.price * item.selectedQuantity) }}
                </div>
                
                <button class="btn-remove" @click="removeItem(item)">
                  <i class="fas fa-trash-alt"></i>
                </button>
              </div>
            </div>
          </div>
        </div>
        
        <!-- Resumo do Pedido -->
        <div class="col-lg-4">
          <div class="order-summary">
            <h4 class="summary-title">
              <i class="fas fa-receipt me-2"></i>
              Resumo do Pedido
            </h4>
            
            <div class="summary-line">
              <span>Subtotal ({{ cartItems.length }} {{ cartItems.length === 1 ? 'item' : 'itens' }})</span>
              <span class="summary-value">R$ {{ formatPrice(total) }}</span>
            </div>
            
            <div class="summary-line">
              <span>Frete</span>
              <span class="summary-value text-success">Grátis</span>
            </div>
            
            <hr class="summary-divider">
            
            <div class="summary-total">
              <span>Total</span>
              <span class="total-value">R$ {{ formatPrice(total) }}</span>
            </div>
            
            <button class="btn btn-checkout" @click="openFinalizeModal">
              <i class="fas fa-credit-card me-2"></i>
              Finalizar Compra
            </button>
            
            <div class="security-info">
              <i class="fas fa-shield-alt me-2"></i>
              Compra 100% segura e protegida
            </div>
          </div>
        </div>
      </div>
      
      <!-- Carrinho Vazio -->
      <div v-else class="empty-cart">
        <div class="empty-cart-content">
          <i class="fas fa-shopping-cart empty-icon"></i>
          <h3>Seu carrinho está vazio</h3>
          <p>Adicione produtos incríveis ao seu carrinho e finalize sua compra!</p>
          <router-link :to="{ name: 'ProductsList' }" class="btn btn-primary btn-lg">
            <i class="fas fa-arrow-left me-2"></i>
            Continuar Comprando
          </router-link>
        </div>
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
      
      // Emitir evento para atualizar contador do carrinho
      this.$root.$emit('cart-updated');
    },
    clearCart() {
      this.cartItems = [];
      localStorage.removeItem('cartItems');
      this.total = 0;
      
      // Emitir evento para atualizar contador do carrinho
      this.$root.$emit('cart-updated');
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
      this.clearCart(); // Limpa o carrinho (já emite o evento cart-updated)
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
/* Layout Principal */
.cart-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
}

/* Header do Carrinho */
.cart-header {
  background: linear-gradient(135deg, #0d6efd 0%, #0b5ed7 100%);
  color: white;
  padding: 2rem 0;
  box-shadow: 0 4px 20px rgba(13, 110, 253, 0.3);
}

.cart-title {
  font-size: 2.5rem;
  font-weight: 700;
  margin: 0;
  text-shadow: 0 2px 4px rgba(0,0,0,0.2);
}

.cart-subtitle {
  font-size: 1.1rem;
  opacity: 0.9;
  margin: 0.5rem 0 0 0;
}

.btn-clear-cart {
  background: #dc3545 !important;
  border: 2px solid #dc3545 !important;
  color: white;
  padding: 0.75rem 1.5rem;
  border-radius: 50px;
  font-weight: 600;
  font-size: 0.95rem;
  transition: all 0.3s ease;
  display: inline-flex;
  align-items: center;
  box-shadow: 0 4px 15px rgba(220, 53, 69, 0.3);
}

.btn-clear-cart:hover {
  background: #c82333 !important;
  border-color: #c82333 !important;
  color: white;
  transform: translateY(-1px);
  box-shadow: 0 6px 20px rgba(200, 35, 51, 0.4);
}

.btn-clear-cart i {
  font-size: 1rem;
  transition: transform 0.3s ease;
}

.btn-clear-cart:hover i {
  transform: scale(1.1);
}

/* Itens do Carrinho */
.cart-items {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.cart-item {
  background: white;
  border-radius: 16px;
  padding: 1.5rem;
  box-shadow: 0 4px 20px rgba(0,0,0,0.08);
  display: flex;
  align-items: center;
  gap: 1.5rem;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.cart-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 30px rgba(0,0,0,0.12);
}

.item-image {
  flex-shrink: 0;
  width: 100px;
  height: 100px;
  border-radius: 12px;
  overflow: hidden;
  background: #f8f9fa;
  display: flex;
  align-items: center;
  justify-content: center;
}

.item-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.no-image {
  color: #6c757d;
}

.item-details {
  flex: 1;
  min-width: 0;
}

.item-name {
  font-size: 1.25rem;
  font-weight: 600;
  color: #212529;
  margin-bottom: 0.5rem;
}

.item-price {
  font-size: 1.1rem;
  font-weight: 700;
  color: #0d6efd;
  margin-bottom: 0.25rem;
}

.item-stock {
  font-size: 0.9rem;
  color: #6c757d;
  margin: 0;
}

/* Controles do Item */
.item-controls {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1rem;
}

.quantity-control {
  display: flex;
  align-items: center;
  background: #f8f9fa;
  border-radius: 50px;
  padding: 4px;
}

.btn-quantity {
  width: 36px;
  height: 36px;
  border: none;
  background: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #0d6efd;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.btn-quantity:hover:not(:disabled) {
  background: #0d6efd;
  color: white;
  transform: scale(1.1);
}

.btn-quantity:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.quantity-input {
  width: 60px;
  height: 36px;
  border: none;
  background: transparent;
  text-align: center;
  font-weight: 600;
  color: #212529;
}

.quantity-input:focus {
  outline: none;
  background: white;
  border-radius: 8px;
}

.item-total {
  font-size: 1.2rem;
  font-weight: 700;
  color: #28a745;
}

.btn-remove {
  width: 40px;
  height: 40px;
  border: none;
  background: #dc3545;
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}

.btn-remove:hover {
  background: #c82333;
  transform: scale(1.1);
}

/* Resumo do Pedido */
.order-summary {
  background: white;
  border-radius: 16px;
  padding: 2rem;
  box-shadow: 0 4px 20px rgba(0,0,0,0.08);
  position: sticky;
  top: 2rem;
}

.summary-title {
  font-size: 1.5rem;
  font-weight: 700;
  color: #212529;
  margin-bottom: 1.5rem;
  padding-bottom: 1rem;
  border-bottom: 2px solid #f8f9fa;
}

.summary-line {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
  font-size: 1rem;
}

.summary-value {
  font-weight: 600;
}

.summary-divider {
  margin: 1.5rem 0;
  border-color: #dee2e6;
}

.summary-total {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 1.25rem;
  font-weight: 700;
  margin-bottom: 2rem;
  padding: 1rem;
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
  border-radius: 12px;
}

.total-value {
  color: #28a745;
  font-size: 1.5rem;
}

.btn-checkout {
  width: 100%;
  padding: 1rem;
  font-size: 1.1rem;
  font-weight: 600;
  background: linear-gradient(135deg, #28a745 0%, #20c997 100%);
  border: none;
  border-radius: 12px;
  color: white;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(40, 167, 69, 0.3);
}

.btn-checkout:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(40, 167, 69, 0.4);
  background: linear-gradient(135deg, #218838 0%, #17a2b8 100%);
}

.security-info {
  text-align: center;
  margin-top: 1rem;
  padding: 0.75rem;
  background: rgba(40, 167, 69, 0.1);
  border-radius: 8px;
  color: #28a745;
  font-size: 0.9rem;
  font-weight: 500;
}

/* Carrinho Vazio */
.empty-cart {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 60vh;
}

.empty-cart-content {
  text-align: center;
  max-width: 400px;
}

.empty-icon {
  font-size: 4rem;
  color: #6c757d;
  margin-bottom: 1.5rem;
  opacity: 0.5;
}

.empty-cart-content h3 {
  font-size: 1.75rem;
  font-weight: 600;
  color: #495057;
  margin-bottom: 1rem;
}

.empty-cart-content p {
  color: #6c757d;
  margin-bottom: 2rem;
  font-size: 1.1rem;
}

/* Responsividade */
@media (max-width: 992px) {
  .cart-item {
    flex-direction: column;
    text-align: center;
    gap: 1rem;
  }
  
  .item-controls {
    flex-direction: row;
    width: 100%;
    justify-content: space-between;
  }
  
  .order-summary {
    margin-top: 2rem;
    position: static;
  }
}

@media (max-width: 768px) {
  .cart-title {
    font-size: 2rem;
  }
  
  .cart-header {
    padding: 1.5rem 0;
  }
  
  .cart-item {
    padding: 1rem;
  }
  
  .item-image {
    width: 80px;
    height: 80px;
  }
}

@media (max-width: 576px) {
  .cart-title {
    font-size: 1.75rem;
  }
  
  .item-controls {
    flex-direction: column;
    gap: 0.75rem;
  }
  
  .quantity-control {
    order: 1;
  }
  
  .item-total {
    order: 2;
  }
  
  .btn-remove {
    order: 3;
  }
}

/* Remover spinners dos inputs number */
input[type="number"]::-webkit-outer-spin-button,
input[type="number"]::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

input[type=number] {
  appearance: textfield;
  -moz-appearance: textfield;
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