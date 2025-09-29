<template>
  <div class="registration-page">
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
            <img src="@/assets/logo3.png" alt="Logo" class="logo-image" />
          </div>
          <h1 class="registration-title">Criar Conta</h1>
          <p class="registration-subtitle">Preencha os dados para se cadastrar</p>
        </div>

        <!-- Formulário -->
        <form @submit.prevent="submitForm" class="registration-form">
          <!-- Alerta de erro -->
          <div v-if="error" class="error-alert">
            <i class="fas fa-exclamation-circle"></i>
            <span>{{ error }}</span>
      </div>

          <!-- Seção de Informações Pessoais -->
          <div class="form-section">
            <h3 class="section-title">
              <i class="fas fa-user"></i>
              Informações Pessoais
            </h3>
            
            <div class="form-row">
              <div class="form-group">
                <label class="form-label">Nome Completo</label>
                <div class="input-wrapper" :class="{ 'focused': focusedField === 'nome' }">
                  <input
                    v-model="form.nome"
                    @focus="focusField('nome')"
                    @blur="blurField('nome')"
                    type="text"
                    class="form-input"
                    placeholder="Digite seu nome completo"
                    required
                  />
                  <div class="input-border"></div>
                </div>
              </div>

              <div class="form-group">
                <label class="form-label">Email</label>
                <div class="input-wrapper" :class="{ 'focused': focusedField === 'email' }">
                  <input
                    v-model="form.email"
                    @focus="focusField('email')"
                    @blur="blurField('email')"
                    type="email"
                    class="form-input"
                    placeholder="Digite seu email"
                    required
                  />
                  <div class="input-border"></div>
                </div>
              </div>
            </div>

            <div class="form-row">
              <div class="form-group">
                <label class="form-label">Telefone</label>
                <div class="input-wrapper" :class="{ 'focused': focusedField === 'telefone' }">
                  <input
                    v-model="form.telefone"
                    @focus="focusField('telefone')"
                    @blur="blurField('telefone')"
                    type="tel"
                    class="form-input"
                    placeholder="(11) 99999-9999"
                    required
                  />
                  <div class="input-border"></div>
                </div>
              </div>

              <div class="form-group">
                <label class="form-label">Data de Nascimento</label>
                <div class="input-wrapper" :class="{ 'focused': focusedField === 'dataNascimento' }">
                  <input
                    v-model="form.dataNascimento"
                    @focus="focusField('dataNascimento')"
                    @blur="blurField('dataNascimento')"
                    type="date"
                    class="form-input"
                    required
                  />
                  <div class="input-border"></div>
                </div>
              </div>
            </div>
          </div>

          <!-- Seção de Endereço -->
          <div class="form-section">
            <h3 class="section-title">
              <i class="fas fa-map-marker-alt"></i>
              Endereço
            </h3>
            
            <div class="form-row">
              <div class="form-group flex-2">
                <label class="form-label">Logradouro</label>
                <div class="input-wrapper" :class="{ 'focused': focusedField === 'logradouro' }">
                  <input
                    v-model="form.logradouro"
                    @focus="focusField('logradouro')"
                    @blur="blurField('logradouro')"
                    type="text"
                    class="form-input"
                    placeholder="Rua, Avenida, etc."
                    required
                  />
                  <div class="input-border"></div>
                </div>
              </div>

              <div class="form-group flex-1">
                <label class="form-label">Número</label>
                <div class="input-wrapper" :class="{ 'focused': focusedField === 'numero' }">
                  <input
                    v-model="form.numero"
                    @focus="focusField('numero')"
                    @blur="blurField('numero')"
                    type="text"
                    class="form-input"
                    placeholder="123"
                    required
                  />
                  <div class="input-border"></div>
                </div>
              </div>
            </div>
          </div>

          <!-- Seção de Conta -->
          <div class="form-section">
            <h3 class="section-title">
              <i class="fas fa-key"></i>
              Dados da Conta
            </h3>
            
            <div class="form-group">
              <label class="form-label">Tipo de Usuário</label>
              <div class="select-wrapper">
                <select v-model="form.tipoUsuario" class="form-select" required>
                  <option value="">Selecione o tipo</option>
                  <option value="COMPRADOR">Comprador</option>
                  <option value="FORNECEDOR">Fornecedor</option>
                </select>
                <i class="fas fa-chevron-down select-arrow"></i>
              </div>
            </div>

            <div class="form-row">
              <div class="form-group">
                <label class="form-label">Senha</label>
                <div class="input-wrapper" :class="{ 'focused': focusedField === 'senha' }">
                  <input
                    v-model="form.senha"
                    @focus="focusField('senha')"
                    @blur="blurField('senha')"
                    :type="showPassword ? 'text' : 'password'"
                    class="form-input"
                    placeholder="Digite sua senha"
                    required
                  />
                  <button
                    type="button"
                    @click="togglePassword"
                    class="password-toggle"
                  >
                    <i :class="showPassword ? 'fas fa-eye-slash' : 'fas fa-eye'"></i>
                  </button>
                  <div class="input-border"></div>
                </div>
              </div>

              <div class="form-group">
                <label class="form-label">Confirmar Senha</label>
                <div class="input-wrapper" :class="{ 'focused': focusedField === 'confirmarSenha' }">
                  <input
                    v-model="form.confirmarSenha"
                    @focus="focusField('confirmarSenha')"
                    @blur="blurField('confirmarSenha')"
                    :type="showConfirmPassword ? 'text' : 'password'"
                    class="form-input"
                    placeholder="Confirme sua senha"
                    required
                  />
                  <button
                    type="button"
                    @click="toggleConfirmPassword"
                    class="password-toggle"
                  >
                    <i :class="showConfirmPassword ? 'fas fa-eye-slash' : 'fas fa-eye'"></i>
                  </button>
                  <div class="input-border"></div>
                </div>
              </div>
            </div>
          </div>

          <!-- Botão de cadastro -->
          <button type="submit" class="registration-button" :disabled="loading">
            <span v-if="loading" class="button-loading">
              <i class="fas fa-spinner fa-spin"></i>
              Criando conta...
            </span>
            <span v-else class="button-content">
              <i class="fas fa-user-plus"></i>
              Criar Conta
            </span>
          </button>
        </form>

        <!-- Footer -->
        <div class="registration-footer">
          <p>Já tem uma conta? 
            <router-link to="/login" class="login-link">
              <span class="link">Faça login</span>
            </router-link>
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'UserRegistration',
  data() {
    return {
      apiUrl: process.env.VUE_APP_API_MODE,
      loading: false,
      error: '',
      showPassword: false,
      showConfirmPassword: false,
      focusedField: '',
      form: {
        nome: '',
        email: '',
        senha: '',
        confirmarSenha: '',
        tipoUsuario: '',
        dataNascimento: '',
        telefone: '',
        logradouro: '',
        numero: ''
      }
    };
  },
  methods: {
    async submitForm() {
      // Validações básicas
      if (!this.validateForm()) {
        return;
      }

      this.loading = true;
      this.error = '';

      try {
          // Concatena logradouro e número
          const endereco = `${this.form.logradouro} N ${this.form.numero}`;

          const userData = {
            nome: this.form.nome,
            email: this.form.email,
            senha: this.form.senha,
            tipo: this.form.tipoUsuario,
            dataNascimento: this.form.dataNascimento,
            telefone: this.form.telefone,
          endereco
        };

        const response = await fetch(`${this.apiUrl}/usuarios/criar`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(userData)
        });

        if (!response.ok) {
          const errorData = await response.json();
          throw new Error(errorData.message || 'Erro ao criar conta');
        }

        await response.json();
        
        // Sucesso - mostrar mensagem e redirecionar
        this.showSuccessToast();
        
        // Redirecionar para login após 2 segundos
        setTimeout(() => {
          this.$router.push({ name: 'UserLogin' });
        }, 2000);

      } catch (error) {
        this.error = error.message;
        console.error('Erro ao criar conta:', error);
      } finally {
        this.loading = false;
      }
    },

    validateForm() {
      // Validar campos obrigatórios
      if (!this.form.nome || !this.form.email || !this.form.senha || 
          !this.form.confirmarSenha || !this.form.tipoUsuario || 
          !this.form.dataNascimento || !this.form.telefone || 
          !this.form.logradouro || !this.form.numero) {
        this.error = 'Por favor, preencha todos os campos obrigatórios.';
        return false;
      }

      // Validar email
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if (!emailRegex.test(this.form.email)) {
        this.error = 'Por favor, insira um email válido.';
        return false;
      }

      // Validar senhas
      if (this.form.senha !== this.form.confirmarSenha) {
        this.error = 'As senhas não coincidem.';
        return false;
      }

      if (this.form.senha.length < 6) {
        this.error = 'A senha deve ter pelo menos 6 caracteres.';
        return false;
      }

      return true;
    },

    togglePassword() {
      this.showPassword = !this.showPassword;
    },

    toggleConfirmPassword() {
      this.showConfirmPassword = !this.showConfirmPassword;
    },

    focusField(field) {
      this.focusedField = field;
    },

    blurField(field) {
      if (this.focusedField === field) {
        this.focusedField = '';
      }
    },

    showSuccessToast() {
      // Criar elemento do toast
      const toast = document.createElement('div');
      toast.className = 'custom-toast success';
      toast.innerHTML = `
        <div class="toast-content">
          <div class="toast-icon">
            <i class="fas fa-check-circle"></i>
          </div>
          <div class="toast-message">
            <h4>Conta criada com sucesso!</h4>
            <p>Você será redirecionado para a tela de login</p>
          </div>
        </div>
        <div class="toast-progress"></div>
      `;

      // Adicionar ao DOM
      document.body.appendChild(toast);

      // Animar entrada
      setTimeout(() => {
        toast.classList.add('show');
      }, 100);

      // Remover automaticamente após 3 segundos
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
  }
};
</script>

<style scoped>
/* Layout Principal */
.registration-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 2rem 1rem;
  position: relative;
  overflow: hidden;
}

/* Background Decorativo */
.registration-background {
  position: absolute;
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
  width: 100%;
  max-width: 900px;
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

.registration-title {
  font-size: 2.5rem;
  font-weight: 700;
  color: #212529;
  margin-bottom: 0.5rem;
  background: linear-gradient(135deg, #667eea, #764ba2);
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
  border-bottom: 2px solid rgba(102, 126, 234, 0.2);
}

.section-title i {
  color: #667eea;
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

.form-group.flex-1 {
  grid-column: span 1;
}

.form-group.flex-2 {
  grid-column: span 2;
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
  border-color: #667eea;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.1);
}

.input-wrapper.focused {
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.input-wrapper.focused .input-border {
  transform: scaleX(1);
}

/* Inputs */
.form-input {
  width: 100%;
  padding: 1rem 1.25rem;
  border: none;
  background: transparent;
  font-size: 1rem;
  color: #495057;
  outline: none;
  transition: all 0.3s ease;
}

.form-input::placeholder {
  color: #adb5bd;
  transition: all 0.3s ease;
}

.form-input:focus::placeholder {
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
  background: linear-gradient(135deg, #667eea, #764ba2);
  transform: scaleX(0);
  transition: transform 0.3s ease;
  transform-origin: left;
}

/* Select Personalizado */
.select-wrapper {
  position: relative;
  background: white;
  border-radius: 12px;
  border: 2px solid #e9ecef;
  transition: all 0.3s ease;
}

.select-wrapper:hover {
  border-color: #667eea;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.1);
}

.form-select {
  width: 100%;
  padding: 1rem 1.25rem;
  padding-right: 3rem;
  border: none;
  background: transparent;
  font-size: 1rem;
  color: #495057;
  outline: none;
  appearance: none;
  cursor: pointer;
}

.select-arrow {
  position: absolute;
  right: 1rem;
  top: 50%;
  transform: translateY(-50%);
  color: #6c757d;
  pointer-events: none;
  transition: all 0.3s ease;
}

.form-select:focus + .select-arrow {
  color: #667eea;
  transform: translateY(-50%) rotate(180deg);
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
  display: flex;
  align-items: center;
  justify-content: center;
}

.password-toggle:hover {
  color: #667eea;
  background: rgba(102, 126, 234, 0.1);
}

/* Botão de Registro */
.registration-button {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  border-radius: 12px;
  padding: 1rem 2rem;
  color: white;
  font-size: 1.1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
  margin-top: 1rem;
  position: relative;
  overflow: hidden;
}

.registration-button:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.5);
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

/* Footer */
.registration-footer {
  text-align: center;
  margin-top: 2rem;
  padding-top: 2rem;
  border-top: 1px solid rgba(0, 0, 0, 0.1);
}

.registration-footer p {
  color: #6c757d;
  margin: 0;
  font-size: 0.95rem;
}

.login-link {
  text-decoration: none;
}

.link {
  color: #667eea;
  font-weight: 600;
  transition: all 0.2s ease;
  position: relative;
}

.link:hover {
  color: #764ba2;
}

.link::after {
  content: '';
  position: absolute;
  bottom: -2px;
  left: 0;
  width: 0;
  height: 2px;
  background: linear-gradient(135deg, #667eea, #764ba2);
  transition: width 0.3s ease;
}

.link:hover::after {
  width: 100%;
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

:global(.custom-toast.success.show) {
  transform: translateX(0);
  opacity: 1;
}

:global(.custom-toast.success.hide) {
  transform: translateX(100%);
  opacity: 0;
}

:global(.custom-toast.success .toast-content) {
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

:global(.custom-toast.success .toast-message) {
  flex: 1;
  min-width: 0;
}

:global(.custom-toast.success .toast-message h4) {
  margin: 0 0 0.5rem 0;
  font-size: 1.1rem;
  font-weight: 700;
  color: #212529;
}

:global(.custom-toast.success .toast-message p) {
  margin: 0;
  font-size: 0.9rem;
  color: #6c757d;
  line-height: 1.4;
}

:global(.custom-toast.success .toast-progress) {
  position: absolute;
  bottom: 0;
  left: 0;
  height: 3px;
  background: linear-gradient(90deg, #28a745, #20c997);
  width: 100%;
  transform-origin: left;
  animation: toast-progress 3s linear forwards;
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
  .registration-page {
    padding: 1rem;
  }

  .registration-card {
    padding: 2rem 1.5rem;
  }

  .registration-title {
    font-size: 2rem;
  }

  .form-row {
    grid-template-columns: 1fr;
    gap: 1rem;
  }

  .form-group.flex-2 {
    grid-column: span 1;
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
  .form-select {
    padding: 0.875rem 1rem;
    font-size: 0.95rem;
  }

  .registration-button {
    padding: 0.875rem 1.5rem;
    font-size: 1rem;
  }

  :global(.custom-toast.success) {
    right: 10px;
    left: 10px;
    min-width: auto;
    max-width: none;
  }
}
</style>
