<template>
  <nav class="navbar navbar-expand-lg navbar-light bg-light shadow">
    <div class="container-fluid">
      <!-- Logo e nome -->
      <a class="navbar-brand mx-auto d-flex align-items-center justify-content-center" href="ProductsList">
        <img
          src="@/assets/logo3.png"
          alt="Logo"
          style="width: 80px; height: 80px; margin-right: 8px;" 
        />
        Alimentação Social
      </a>
      
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
        <ul class="navbar-nav ms-auto">
          <!-- Informações do usuário logado -->
          <li v-if="isAuthenticated" class="nav-item me-2">
            <span class="navbar-text">
              <i class="fas fa-user"></i> 
              {{ user.nome }} ({{ user.tipo }})
            </span>
          </li>

          <!-- Botão para adicionar produto (apenas fornecedores) -->
          <li v-if="showAddProduct" class="nav-item me-2">
            <router-link :to="{ name: 'ProductRegistration' }" @click.native.prevent="$router.push({ name: 'ProductRegistration' })" class="btn btn-success">
              <i class="fas fa-plus"></i> Adicionar Produto
            </router-link>
          </li>
          
          <!-- Botão do carrinho (compradores e fornecedores) -->
          <li v-if="showCart" class="nav-item me-2">
            <router-link :to="{ name: 'ProductsCart' }" @click.native.prevent="$router.push({ name: 'ProductsCart' })" class="btn btn-info">
              <i class="fas fa-shopping-cart"></i> Carrinho
            </router-link>
          </li>

          <!-- Botão de logout (se autenticado) -->
          <li v-if="isAuthenticated" class="nav-item me-2">
            <button @click="logout" class="btn btn-outline-danger">
              <i class="fas fa-sign-out-alt"></i> Sair
            </button>
          </li>

          <!-- Botões de login/registro (se não autenticado) -->
          <template v-if="!isAuthenticated">
            <li class="nav-item me-2">
              <router-link to="/userlogin" class="btn btn-primary">
                <i class="fas fa-sign-in-alt"></i> Login
              </router-link>
            </li>
            <li class="nav-item">
              <router-link to="/userregistration" class="btn btn-outline-secondary">
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
    }
  }
};
</script>

<style>
/* Ajustes gerais de estilo */
.navbar-brand {
  flex-grow: 1;
  text-align: center;
}

.navbar .container-fluid {
  display: flex;
  justify-content: space-between;
  align-items: center;
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
  .navbar-nav .btn {
    width: auto; /* Evita que os botões ocupem toda a tela */
    margin: 5px 0; /* Espaçamento entre os botões */
  }
}

/* Estilos para telas muito pequenas (mobile) */
@media (max-width: 576px) {
  .navbar-brand img {
    width: 50px; /* Reduz o tamanho da logo em telas menores */
    height: 50px;
  }

  .btn {
    font-size: 0.875rem; /* Reduz o tamanho dos botões em telas muito pequenas */
  }
}
</style>
