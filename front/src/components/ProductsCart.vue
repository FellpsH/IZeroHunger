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
    <el-dialog :visible.sync="isFinalizeModalVisible" width="80%" title="Finalizar Compra" append-to-body @close="isLoadingModalVisible = false">
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
    <el-dialog :visible.sync="isLoginModalVisible" width="80%" title="Login" append-to-body>
      <UserLogin @login-success="onLoginSuccess" />
    </el-dialog>

    <el-dialog :visible.sync="isRegistrationModalVisible" width="80%" title="Criar Conta" append-to-body>
      <UserRegistration />
    </el-dialog>

    <!-- Modal de Carregamento/Confirmação -->
    <el-dialog :visible.sync="isLoadingModalVisible" width="500px" :show-close="false" custom-class="success-modal" append-to-body>
      <div v-if="isLoading" class="loading-content">
        <div class="loading-spinner">
          <div class="spinner-ring"></div>
          <div class="spinner-ring"></div>
          <div class="spinner-ring"></div>
        </div>
        <h3 class="loading-title">Processando seu pedido</h3>
        <p class="loading-subtitle">Aguarde enquanto finalizamos sua compra...</p>
      </div>
      <div v-else class="success-content">
        <div class="success-animation">
          <div class="success-checkmark">
            <svg class="checkmark-svg" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 52 52">
              <circle class="checkmark-circle" cx="26" cy="26" r="25" fill="none"/>
              <path class="checkmark-check" fill="none" d="m14.1 27.2l7.1 7.2 16.7-16.8"/>
            </svg>
          </div>
        </div>
        
        <div class="success-message">
          <h2 class="success-title">Compra Finalizada!</h2>
          <p class="success-subtitle">Seu pedido foi processado com sucesso</p>
          
          <div class="order-details">
            <div class="detail-item">
              <i class="fas fa-shopping-bag"></i>
              <span>{{ cartItems.length }} {{ cartItems.length === 1 ? 'item' : 'itens' }} comprados</span>
            </div>
            <div class="detail-item">
              <i class="fas fa-credit-card"></i>
              <span>Total: R$ {{ formatPrice(total) }}</span>
            </div>
            <div class="detail-item">
              <i class="fas fa-map-marker-alt"></i>
              <span>{{ userAddress }}</span>
            </div>
          </div>
          
          <div class="success-actions">
            <button class="btn btn-track-order" @click="goToOrderTracking">
              <i class="fas fa-truck me-2"></i>
              Acompanhar Pedido
            </button>
            <button class="btn btn-primary-custom" @click="redirectToProductList">
              <i class="fas fa-arrow-left me-2"></i>
              Continuar Comprando
            </button>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Navbar from './NavBar.vue';
import UserLogin from './UserLogin.vue';
import UserRegistration from './UserRegistration.vue';
import pedidoService from '../services/pedidoService';
import { formatCurrency } from '../utils/currency';

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
      isLoading: false,
      isLoadingModalVisible: false,
      userAddress: '',
      lastCreatedOrder: null,
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
      this.userAddress = user.endereco;
    } else {
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
    
    // Garantir que isLoading seja false após carregar
    this.isLoading = false;
  },
  methods: {
    formatPrice(value) {
      return formatCurrency(value);
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
      // Evitar múltiplas chamadas simultâneas
      if (this.isLoading) {
        return;
      }
      
      if (this.isLoggedIn) {
        // Usuário já autenticado: segue direto para finalizar
        this.finalizePurchase();
      } else {
        // Usuário não autenticado: mostra modal para login/cadastro
        this.$nextTick(() => {
          this.isFinalizeModalVisible = true;
        });
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

    goToOrderTracking() {
      // Usar dados do pedido criado (se disponível) ou dados do localStorage
      if (this.lastCreatedOrder) {
        localStorage.setItem('currentOrder', JSON.stringify(this.lastCreatedOrder));
      } else {
        // Fallback para dados simulados (caso não tenha pedido criado)
        const orderData = {
          orderId: 'PED' + Date.now(),
          items: this.cartItems,
          total: this.total,
          address: this.userAddress,
          orderDate: new Date().toISOString(),
          status: 'confirmed'
        };
        localStorage.setItem('currentOrder', JSON.stringify(orderData));
      }
      
      this.clearCart();
      this.$router.push('/order-tracking');
    },

    async finalizePurchase() {
      // Evitar execuções duplicadas
      if (this.isLoading) {
        return;
      }
      
      this.isLoginModalVisible = false;
      this.isLoadingModalVisible = true;
      this.isLoading = true;

      try {
        // Obter dados do usuário
        const userString = localStorage.getItem('user');
        let userId = null;
        
        if (userString) {
          try {
            const user = JSON.parse(userString);
            userId = user.id;
          } catch (error) {
            console.error('Erro ao fazer parse do usuário:', error);
          }
        }

        if (!userId) {
          throw new Error('Usuário não encontrado');
        }

        // Usar o serviço de pedidos (que usa o endpoint /carts/registraCompra)
        const result = await pedidoService.criarPedido(userId, this.cartItems, this.total);

        if (result.success) {
          // Salvar dados do pedido criado (simulado)
          this.lastCreatedOrder = {
            orderId: 'PED' + Date.now(),
            items: this.cartItems,
            total: this.total,
            address: this.userAddress,
            orderDate: new Date().toISOString(),
            status: 'CONFIRMADO',
            statusDescricao: 'Pedido Confirmado'
          };

          // Aguardar um pouco antes de mostrar o sucesso
          await new Promise(resolve => setTimeout(resolve, 1000));
          
          // Mudar para o conteúdo de sucesso (mantém o mesmo modal aberto)
          this.isLoading = false;
          
          // O modal de loading continua aberto, mas agora mostra o conteúdo de sucesso

        } else {
          throw new Error(result.error || 'Erro ao criar pedido');
        }

      } catch (error) {
        console.error('Error:', error);
        this.isLoadingModalVisible = false;
        this.isLoading = false;
        
        // Mostrar toast de erro
        this.showToast('Erro ao finalizar compra. Tente novamente.', 'error');
      }
    },
  
    // Método chamado após o login do usuário
    onLoginSuccess() {
      this.isLoggedIn = true;
      this.isLoginModalVisible = false;
      this.finalizePurchase(); // Finaliza a compra após o login
    },

    // Mostrar toast personalizado
    showToast(message, type = 'info') {
      // Usar Element UI message se disponível, senão usar alert
      if (this.$message) {
        if (type === 'error') {
          this.$message.error(message);
        } else if (type === 'success') {
          this.$message.success(message);
        } else {
          this.$message.info(message);
        }
      } else {
        // Fallback para alert nativo
        alert(message);
      }
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

/* Modal de Sucesso */
.success-modal .el-dialog {
  border-radius: 24px;
  overflow: hidden;
  box-shadow: 0 25px 50px rgba(0, 0, 0, 0.25);
}

.success-modal .el-dialog__body {
  padding: 0;
  background: linear-gradient(135deg, #f8f9fa 0%, #ffffff 100%);
}

/* Loading Content */
.loading-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 3rem 2rem;
  text-align: center;
}

.loading-spinner {
  position: relative;
  width: 80px;
  height: 80px;
  margin-bottom: 2rem;
}

.spinner-ring {
  position: absolute;
  width: 100%;
  height: 100%;
  border: 4px solid transparent;
  border-radius: 50%;
  animation: spin 1.5s linear infinite;
}

.spinner-ring:nth-child(1) {
  border-top-color: #28a745;
  animation-delay: 0s;
}

.spinner-ring:nth-child(2) {
  border-right-color: #20c997;
  animation-delay: 0.3s;
}

.spinner-ring:nth-child(3) {
  border-bottom-color: #17a2b8;
  animation-delay: 0.6s;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.loading-title {
  font-size: 1.5rem;
  font-weight: 700;
  color: #212529;
  margin-bottom: 0.5rem;
}

.loading-subtitle {
  font-size: 1rem;
  color: #6c757d;
  margin: 0;
}

/* Success Content */
.success-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 3rem 2rem;
  text-align: center;
}

.success-animation {
  margin-bottom: 2rem;
}

.success-checkmark {
  width: 100px;
  height: 100px;
  margin: 0 auto;
  animation: checkmark-appear 0.6s ease-in-out;
}

.checkmark-svg {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  display: block;
  stroke-width: 3;
  stroke: #fff;
  stroke-miterlimit: 10;
  box-shadow: 0 10px 30px rgba(40, 167, 69, 0.3);
  background: linear-gradient(135deg, #28a745, #20c997);
  padding: 10px;
  box-sizing: border-box;
}

.checkmark-circle {
  stroke-dasharray: 166;
  stroke-dashoffset: 166;
  stroke-width: 2;
  stroke-miterlimit: 10;
  stroke: rgba(255, 255, 255, 0.3);
  fill: none;
  animation: stroke 0.6s cubic-bezier(0.65, 0, 0.45, 1) forwards;
}

.checkmark-check {
  transform-origin: 50% 50%;
  stroke-dasharray: 48;
  stroke-dashoffset: 48;
  stroke-width: 3;
  stroke: #fff;
  stroke-linecap: round;
  animation: stroke-check 0.3s cubic-bezier(0.65, 0, 0.45, 1) 0.8s forwards;
}

@keyframes checkmark-appear {
  0% {
    transform: scale(0);
    opacity: 0;
  }
  50% {
    transform: scale(1.2);
    opacity: 1;
  }
  100% {
    transform: scale(1);
    opacity: 1;
  }
}

@keyframes stroke {
  100% {
    stroke-dashoffset: 0;
  }
}

@keyframes stroke-check {
  100% {
    stroke-dashoffset: 0;
  }
}

/* Success Message */
.success-message {
  width: 100%;
}

.success-title {
  font-size: 2rem;
  font-weight: 700;
  color: #212529;
  margin-bottom: 0.5rem;
  background: linear-gradient(135deg, #28a745, #20c997);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.success-subtitle {
  font-size: 1.1rem;
  color: #6c757d;
  margin-bottom: 2rem;
}

/* Order Details */
.order-details {
  background: rgba(40, 167, 69, 0.05);
  border-radius: 16px;
  padding: 1.5rem;
  margin-bottom: 2rem;
  border: 1px solid rgba(40, 167, 69, 0.1);
}

.detail-item {
  display: flex;
  align-items: center;
  gap: 1rem;
  margin-bottom: 1rem;
  font-size: 1rem;
  color: #495057;
}

.detail-item:last-child {
  margin-bottom: 0;
}

.detail-item i {
  width: 20px;
  color: #28a745;
  font-size: 1.1rem;
}

.detail-item span {
  font-weight: 500;
}

/* Success Actions */
.success-actions {
  margin-top: 1rem;
  display: flex;
  gap: 1rem;
  justify-content: center;
  flex-wrap: wrap;
}

.btn-primary-custom {
  background: linear-gradient(135deg, #28a745 0%, #20c997 100%);
  border: none;
  border-radius: 12px;
  padding: 1rem 2rem;
  color: white;
  font-size: 1.1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(40, 167, 69, 0.3);
  display: inline-flex;
  align-items: center;
  text-decoration: none;
}

.btn-primary-custom:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(40, 167, 69, 0.4);
  color: white;
  text-decoration: none;
}

.btn-track-order {
  background: linear-gradient(135deg, #007bff 0%, #0056b3 100%);
  border: none;
  border-radius: 12px;
  padding: 1rem 2rem;
  color: white;
  font-size: 1.1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(0, 123, 255, 0.3);
  display: inline-flex;
  align-items: center;
  text-decoration: none;
}

.btn-track-order:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0, 123, 255, 0.4);
  color: white;
  text-decoration: none;
  background: linear-gradient(135deg, #0056b3 0%, #004085 100%);
}

/* Responsividade */
@media (max-width: 576px) {
  .success-content {
    padding: 2rem 1rem;
  }
  
  .success-title {
    font-size: 1.75rem;
  }
  
  .success-checkmark {
    width: 80px;
    height: 80px;
  }
  
  .checkmark-svg {
    width: 80px;
    height: 80px;
  }
  
  .order-details {
    padding: 1rem;
  }
  
  .detail-item {
    font-size: 0.9rem;
  }
}

</style>