<template>
  <div class="container d-flex justify-content-center align-items-center min-vh-100">
    <div class="card shadow" style="width: 400px; background-color: #f8f9fa;">
      <div class="card-body">
        <h1 class="text-center mb-4"><i class="fa fa-lock"></i> Login</h1>
        <form @submit.prevent="handleLogin">
          <!-- Exibir erro se houver -->
          <div v-if="error" class="alert alert-danger" role="alert">
            <i class="fa fa-exclamation-triangle"></i> {{ error }}
          </div>

          <div class="mb-3">
            <label for="username" class="form-label">Email</label>
            <div class="input-group">
              <span class="input-group-text">
                <i class="fa fa-envelope"></i>
              </span>
              <input
                type="text"
                id="username"
                v-model="username"
                @input="clearError"
                class="form-control"
                placeholder="Digite seu email"
                required
                :disabled="loading"
              />
            </div>
          </div>
          <div class="mb-3">
            <label for="password" class="form-label">Senha</label>
            <div class="input-group">
              <span class="input-group-text">
                <i class="fa fa-lock"></i>
              </span>
              <input
                type="password"
                id="password"
                v-model="password"
                @input="clearError"
                class="form-control"
                placeholder="Digite sua senha"
                required
                :disabled="loading"
              />
            </div>
          </div>
          <button type="submit" class="btn btn-primary w-100 mt-3" :disabled="loading">
            <i v-if="loading" class="fa fa-spinner fa-spin"></i>
            <i v-else class="fa fa-sign-in"></i> 
            {{ loading ? 'Entrando...' : 'Entrar' }}
          </button>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import authService from '../services/authService';

export default {
  name: 'UserLogin',
  data() {
    return {
      username: '',
      password: '',
      loading: false,
      error: null
    };
  },
  methods: {
    async handleLogin() {
      this.loading = true;
      this.error = null;

      try {
        // Tentar primeiro com o endpoint que retorna JWT
        let result = await authService.loginWithJWT(this.username, this.password);
        
        // Se falhar, tentar com o endpoint simples
        if (!result.success) {
          result = await authService.login(this.username, this.password);
        }

        if (result.success) {
          console.log('Login bem-sucedido:', result.user);
          this.$emit('login-success', result.user);
          this.$router.push({ name: 'ProductsList' });
        } else {
          this.error = result.error || 'Credenciais inválidas';
        }
      } catch (error) {
        console.error('Erro no login:', error);
        this.error = 'Erro de conexão. Tente novamente.';
      } finally {
        this.loading = false;
      }
    },
    
    logout() {
      authService.logout();
      this.$router.push({ name: 'UserLogin' });
    },

    // Limpar erro quando usuário digitar
    clearError() {
      this.error = null;
    }
  },
  
  mounted() {
    console.log('Componente de login carregado!');
    
    // Verificar se já está autenticado
    if (authService.isAuthenticated()) {
      const user = authService.getUser();
      this.$emit('login-success', user);
      this.$router.push({ name: 'ProductsList' });
    }
  }
};
</script>

<style scoped>
html, body {
  height: 100%;
  margin: 0;
}

.container {
  height: 100%;
}

.card {
  border-radius: 1rem;
  background-color: #f8f9fa;
  color: #343a40;
}

.card-body {
  padding: 20px;
}

.btn-primary {
  background-color: #007bff;
  border-color: #007bff;
  transition: background-color 0.3s ease, border-color 0.3s ease;
}

.btn-primary:hover {
  background-color: #0056b3;
  border-color: #0056b3;
}

.form-label {
  color: #495057;
}
</style>
