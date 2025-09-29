<template>
  <nav class="navbar navbar-expand-lg navbar-dark navbar-elevated sticky-top">
    <div class="container-fluid">
      <!-- Logo e nome -->
      <router-link class="navbar-brand d-flex align-items-center" :to="{ name: 'ProductsList' }">
        <img
          src="@/assets/logo3.png"
          alt="Logo"
          class="brand-logo"
        />
        <span class="brand-title">Alimentação Social</span>
      </router-link>
      
      <!-- Toggler button (para dispositivos móveis) -->
      <button
        class="navbar-toggler"
        type="button"
        data-bs-toggle="collapse"
        data-bs-target="#navbarNav"
        aria-controls="navbarNav"
        aria-expanded="false"
        aria-label="Toggle navigation"
      >
        <span class="navbar-toggler-icon"></span>
      </button>
      
      <!-- Menu colapsado -->
      <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav ms-auto align-items-center gap-2">
          <!-- Botão para adicionar produto (apenas fornecedores) -->
          <li v-if="showAddProduct" class="nav-item">
            <router-link :to="{ name: 'ProductRegistration' }" @click.native.prevent="$router.push({ name: 'ProductRegistration' })" class="btn btn-outline-light btn-pill">
              <i class="fas fa-plus"></i> Adicionar
            </router-link>
          </li>

          <!-- Carrinho com badge -->
          <li v-if="showCart" class="nav-item">
            <router-link :to="{ name: 'ProductsCart' }" @click.native.prevent="$router.push({ name: 'ProductsCart' })" class="btn btn-cart btn-pill position-relative">
              <i class="fas fa-shopping-cart cart-icon"></i>
              <span class="cart-text">Carrinho</span>
              <span v-if="cartCount > 0" class="badge cart-badge animate-pulse">{{ cartCount }}</span>
            </router-link>
          </li>

          <!-- Menu do usuário autenticado -->
          <li v-if="isAuthenticated" class="nav-item dropdown">
            <a class="nav-link dropdown-toggle user-chip" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
              <i class="fas fa-user-circle me-1"></i>
              <span class="d-none d-md-inline">{{ user.nome }}</span>
              <small class="role-badge ms-2">{{ user.tipo }}</small>
            </a>
            <ul class="dropdown-menu dropdown-menu-end shadow">
              <li class="px-3 py-2 text-muted small">
                Autenticado como<br />
                <strong>{{ user.email }}</strong>
              </li>
              <li><hr class="dropdown-divider" /></li>
              <li>
                <button class="dropdown-item text-danger" @click="logout">
                  <i class="fas fa-sign-out-alt me-1"></i> Sair
                </button>
              </li>
            </ul>
          </li>

          <!-- Botões de login/registro (se não autenticado) -->
          <template v-else>
            <li class="nav-item">
              <router-link to="/userlogin" class="btn btn-outline-light btn-pill">
                <i class="fas fa-sign-in-alt"></i> Entrar
              </router-link>
            </li>
            <li class="nav-item">
              <router-link to="/userregistration" class="btn btn-light btn-pill text-primary">
                <i class="fas fa-user-plus"></i> Registrar
              </router-link>
            </li>
          </template>
        </ul>
      </div>
    </div>
  </nav>
</template>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>

<script>
import authMixin from '../mixins/authMixin';

export default {
  name: 'NavBar',
  mixins: [authMixin],
  props: {
    cartItems: {
      type: Array,
      default: () => [],
    },
  },
  computed: {
    // Mostrar botões baseado no tipo de usuário
    showAddProduct() {
      return this.isFornecedor();
    },
    showCart() {
      return this.isAuthenticated; // ambos podem ver o carrinho
    },
    cartCount() {
      try {
        const items = JSON.parse(localStorage.getItem('cartItems')) || [];
        return items.length;
      } catch (e) {
        return 0;
      }
    }
  }
};
</script>

<style>
/* Ajustes gerais de estilo */
.navbar-elevated {
  background: linear-gradient(90deg, #0d6efd 0%, #0b5ed7 50%, #0a58ca 100%);
  box-shadow: 0 4px 16px rgba(13, 110, 253, 0.25);
}

.brand-logo {
  width: 40px;
  height: 40px;
  margin-right: 10px;
  filter: drop-shadow(0 2px 4px rgba(0,0,0,0.25));
}

.brand-title {
  font-weight: 700;
  letter-spacing: 0.2px;
}

.btn-pill {
  border-radius: 999px;
  padding: 6px 14px;
}

.btn-cart {
  background: linear-gradient(135deg, #ff6b6b, #ff8e53);
  color: #fff;
  border: none;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(255, 107, 107, 0.3);
}

.btn-cart:hover {
  background: linear-gradient(135deg, #ff5252, #ff7043);
  color: #fff;
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(255, 107, 107, 0.4);
}

.cart-icon {
  font-size: 1.1rem;
  margin-right: 6px;
  transition: transform 0.3s ease;
}

.btn-cart:hover .cart-icon {
  transform: scale(1.1);
}

.cart-text {
  font-weight: 600;
  letter-spacing: 0.3px;
}

.cart-badge {
  position: absolute;
  top: -10px;
  right: -10px;
  background: #dc3545;
  color: white;
  font-size: 0.7rem;
  font-weight: 700;
  min-width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 2px solid #fff;
  box-shadow: 0 2px 8px rgba(220, 53, 69, 0.4);
  line-height: 1;
  padding: 0;
}

.animate-pulse {
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0% { transform: scale(1); }
  50% { transform: scale(1.1); }
  100% { transform: scale(1); }
}

.user-chip {
  display: inline-flex;
  align-items: center;
}

.role-badge {
  background: rgba(255,255,255,0.2);
  padding: 2px 8px;
  border-radius: 999px;
}

/* Responsividade para telas pequenas */
@media (max-width: 992px) {
  .navbar-nav {
    width: 100%; /* Faz os itens da navbar ocuparem toda a largura */
    text-align: center;
  }

  .navbar-toggler {
    margin-left: auto; /* Garante que o botão de toggler fique na extremidade direita */
  }

  .navbar-collapse {
    text-align: center;
  }

  /* Botões não devem ocupar a tela inteira */
  .navbar-nav .btn { width: auto; margin: 5px 0; }
}

/* Estilos para telas muito pequenas (mobile) */
@media (max-width: 576px) {
  .navbar-brand img {
    width: 36px;
    height: 36px;
  }

  .btn { font-size: 0.875rem; }
}
</style>
