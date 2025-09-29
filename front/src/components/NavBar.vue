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
          <!-- Botão para adicionar produto -->
          <li class="nav-item me-2">
            <router-link to="/ProductRegistration" class="btn btn-success">
              <i class="fas fa-plus"></i> Adicionar Produto
            </router-link>
          </li>
          <!-- Botão do carrinho -->
          <li class="nav-item me-2">
            <router-link to="/ProductsCart" class="btn btn-info">
              <i class="fas fa-shopping-cart"></i> Carrinho
            </router-link>
          </li>
          <!-- Botão para tela de login -->
          <li class="nav-item me-2">
            <router-link to="/userlogin" class="btn btn-primary">
              <i class="fas fa-sign-in-alt"></i> Login
            </router-link>
          </li>
          <!-- Botão para tela de registro -->
          <li class="nav-item">
            <router-link to="/userregistration" class="btn btn-outline-secondary">
              <i class="fas fa-user-plus"></i> Registrar
            </router-link>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>

<script>
export default {
  name: 'NavBar',
  props: {
    cartItems: {
      type: Array,
      default: () => [],
    },
  },
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
