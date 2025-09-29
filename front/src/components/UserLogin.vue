<template>
  <div class="login-page">
    <!-- Background decorativo -->
    <div class="login-background">
      <div class="bg-shape shape-1"></div>
      <div class="bg-shape shape-2"></div>
      <div class="bg-shape shape-3"></div>
    </div>

    <div class="login-container">
      <div class="login-card">
        <!-- Header do card -->
        <div class="login-header">
          <div class="login-logo">
            <img src="@/assets/logo3.png" alt="Logo" class="logo-image" />
          </div>
          <h1 class="login-title">Bem-vindo de volta!</h1>
          <p class="login-subtitle">Entre na sua conta para continuar</p>
        </div>

        <!-- Formulário -->
        <form @submit.prevent="handleLogin" class="login-form">
          <!-- Alerta de erro -->
          <div v-if="error" class="error-alert">
            <i class="fas fa-exclamation-circle"></i>
            <span>{{ error }}</span>
          </div>

          <!-- Campo Email -->
          <div class="form-group">
            <label for="username" class="form-label">
              <i class="fas fa-envelope"></i>
              Email
            </label>
            <div class="input-wrapper">
              <input
                type="email"
                id="username"
                v-model="username"
                @input="clearError"
                @focus="focusField('email')"
                @blur="blurField('email')"
                class="form-input"
                :class="{ 'focused': focusedField === 'email', 'has-value': username }"
                placeholder="Digite seu email"
                required
                :disabled="loading"
              />
              <div class="input-border"></div>
            </div>
          </div>

          <!-- Campo Senha -->
          <div class="form-group">
            <label for="password" class="form-label">
              <i class="fas fa-lock"></i>
              Senha
            </label>
            <div class="input-wrapper">
              <input
                :type="showPassword ? 'text' : 'password'"
                id="password"
                v-model="password"
                @input="clearError"
                @focus="focusField('password')"
                @blur="blurField('password')"
                class="form-input"
                :class="{ 'focused': focusedField === 'password', 'has-value': password }"
                placeholder="Digite sua senha"
                required
                :disabled="loading"
              />
              <button 
                type="button" 
                class="password-toggle"
                @click="togglePassword"
                :disabled="loading"
              >
                <i :class="showPassword ? 'fas fa-eye-slash' : 'fas fa-eye'"></i>
              </button>
              <div class="input-border"></div>
            </div>
          </div>

          <!-- Botão de login -->
          <button type="submit" class="login-button" :disabled="loading">
            <span v-if="loading" class="button-loading">
              <i class="fas fa-spinner fa-spin"></i>
              Entrando...
            </span>
            <span v-else class="button-content">
              <i class="fas fa-sign-in-alt"></i>
              Entrar
            </span>
          </button>

          <!-- Links adicionais -->
          <div class="login-footer">
            <p class="register-link">
              Não tem uma conta? 
              <router-link to="/userregistration" class="link">
                Cadastre-se aqui
              </router-link>
            </p>
          </div>
        </form>
      </div>

      <!-- Informações adicionais -->
      <div class="login-info">
        <h3>Acesse produtos frescos</h3>
        <p>Conecte-se com produtores locais e encontre alimentos frescos da sua região.</p>
        <div class="info-features">
          <div class="feature-item">
            <i class="fas fa-leaf"></i>
            <span>Produtos frescos</span>
          </div>
          <div class="feature-item">
            <i class="fas fa-map-marker-alt"></i>
            <span>Produtores locais</span>
          </div>
          <div class="feature-item">
            <i class="fas fa-shield-alt"></i>
            <span>Compra segura</span>
          </div>
        </div>
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
      error: null,
      showPassword: false,
      focusedField: null
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
    },

    // Toggle para mostrar/ocultar senha
    togglePassword() {
      this.showPassword = !this.showPassword;
    },

    // Gerenciar foco dos campos
    focusField(field) {
      this.focusedField = field;
    },

    blurField(field) {
      if (this.focusedField === field) {
        this.focusedField = null;
      }
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
/* Layout Principal */
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  position: relative;
  overflow: hidden;
}

/* Background Decorativo */
.login-background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1;
}

.bg-shape {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  animation: float 6s ease-in-out infinite;
}

.shape-1 {
  width: 200px;
  height: 200px;
  top: 10%;
  left: 10%;
  animation-delay: 0s;
}

.shape-2 {
  width: 150px;
  height: 150px;
  top: 60%;
  right: 15%;
  animation-delay: 2s;
}

.shape-3 {
  width: 100px;
  height: 100px;
  bottom: 20%;
  left: 20%;
  animation-delay: 4s;
}

@keyframes float {
  0%, 100% { transform: translateY(0px) rotate(0deg); }
  50% { transform: translateY(-20px) rotate(180deg); }
}

/* Container Principal */
.login-container {
  display: grid;
  grid-template-columns: 1fr 1fr;
  max-width: 1000px;
  width: 100%;
  margin: 0 auto;
  padding: 2rem;
  gap: 3rem;
  position: relative;
  z-index: 2;
}

/* Card de Login */
.login-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 24px;
  padding: 3rem;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.2);
  animation: slideInLeft 0.8s ease;
}

@keyframes slideInLeft {
  from {
    opacity: 0;
    transform: translateX(-50px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

/* Header do Login */
.login-header {
  text-align: center;
  margin-bottom: 2.5rem;
}

.login-logo {
  width: 100px;
  height: 100px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 1.5rem;
  border-radius: 50%;
  background: white;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  padding: 10px;
}

.logo-image {
  width: 100%;
  height: 100%;
  object-fit: contain;
  border-radius: 50%;
}

.login-title {
  font-size: 2rem;
  font-weight: 700;
  color: #212529;
  margin-bottom: 0.5rem;
}

.login-subtitle {
  color: #6c757d;
  font-size: 1rem;
  margin: 0;
}

/* Formulário */
.login-form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

/* Alerta de Erro */
.error-alert {
  background: rgba(220, 53, 69, 0.1);
  border: 1px solid rgba(220, 53, 69, 0.2);
  color: #dc3545;
  padding: 1rem;
  border-radius: 12px;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.9rem;
  animation: shake 0.5s ease;
}

@keyframes shake {
  0%, 100% { transform: translateX(0); }
  25% { transform: translateX(-5px); }
  75% { transform: translateX(5px); }
}

/* Grupos de Formulário */
.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.form-label {
  font-weight: 600;
  color: #495057;
  font-size: 0.9rem;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.form-label i {
  color: #6c757d;
}

/* Input Wrapper */
.input-wrapper {
  position: relative;
}

.form-input {
  width: 100%;
  padding: 1rem 1.25rem;
  border: 2px solid #e9ecef;
  border-radius: 12px;
  font-size: 1rem;
  background: #f8f9fa;
  transition: all 0.3s ease;
  outline: none;
}

.form-input:focus,
.form-input.focused {
  border-color: #0d6efd;
  background: white;
  box-shadow: 0 0 0 3px rgba(13, 110, 253, 0.1);
}

.form-input.has-value {
  background: white;
}

.form-input:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

/* Borda Animada */
.input-border {
  position: absolute;
  bottom: 0;
  left: 50%;
  width: 0;
  height: 2px;
  background: linear-gradient(90deg, #0d6efd, #6610f2);
  transition: all 0.3s ease;
  transform: translateX(-50%);
}

.form-input:focus + .input-border,
.form-input.focused + .input-border {
  width: 100%;
}

/* Toggle de Senha */
.password-toggle {
  position: absolute;
  right: 1rem;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  color: #6c757d;
  cursor: pointer;
  padding: 0.5rem;
  border-radius: 6px;
  transition: all 0.2s ease;
}

.password-toggle:hover {
  background: rgba(108, 117, 125, 0.1);
  color: #495057;
}

/* Botão de Login */
.login-button {
  background: linear-gradient(135deg, #0d6efd 0%, #6610f2 100%);
  color: white;
  border: none;
  padding: 1rem 2rem;
  border-radius: 12px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
  margin-top: 1rem;
}

.login-button:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 10px 30px rgba(13, 110, 253, 0.3);
}

.login-button:disabled {
  opacity: 0.7;
  cursor: not-allowed;
  transform: none;
}

.button-loading,
.button-content {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
}

/* Footer do Login */
.login-footer {
  text-align: center;
  margin-top: 1.5rem;
}

.register-link {
  color: #6c757d;
  font-size: 0.9rem;
  margin: 0;
}

.link {
  color: #0d6efd;
  text-decoration: none;
  font-weight: 600;
  transition: color 0.2s ease;
}

.link:hover {
  color: #0b5ed7;
  text-decoration: underline;
}

/* Informações Laterais */
.login-info {
  display: flex;
  flex-direction: column;
  justify-content: center;
  color: white;
  animation: slideInRight 0.8s ease;
}

@keyframes slideInRight {
  from {
    opacity: 0;
    transform: translateX(50px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

.login-info h3 {
  font-size: 2.5rem;
  font-weight: 700;
  margin-bottom: 1rem;
  line-height: 1.2;
}

.login-info p {
  font-size: 1.1rem;
  opacity: 0.9;
  margin-bottom: 2rem;
  line-height: 1.6;
}

/* Features */
.info-features {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1rem;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  backdrop-filter: blur(10px);
}

.feature-item i {
  font-size: 1.5rem;
  color: #28a745;
}

.feature-item span {
  font-weight: 500;
}

/* Responsividade */
@media (max-width: 768px) {
  .login-container {
    grid-template-columns: 1fr;
    gap: 2rem;
    padding: 1rem;
  }
  
  .login-card {
    padding: 2rem;
  }
  
  .login-info {
    text-align: center;
    order: -1;
  }
  
  .login-info h3 {
    font-size: 2rem;
  }
  
  .info-features {
    flex-direction: row;
    justify-content: center;
    flex-wrap: wrap;
  }
  
  .feature-item {
    flex: 1;
    min-width: 150px;
  }
}

@media (max-width: 480px) {
  .login-card {
    padding: 1.5rem;
  }
  
  .login-title {
    font-size: 1.5rem;
  }
  
  .login-info h3 {
    font-size: 1.75rem;
  }
  
  .info-features {
    flex-direction: column;
  }
}
</style>
