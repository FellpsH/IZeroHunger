// Mixin para gerenciar autenticação em todos os componentes
import authService from '../services/authService';

export default {
  data() {
    return {
      user: null,
      isAuthenticated: false
    };
  },

  methods: {
    // Verificar se está autenticado
    checkAuth() {
      this.isAuthenticated = authService.isAuthenticated();
      this.user = authService.getUser();
      return this.isAuthenticated;
    },

    // Fazer logout
    logout() {
      authService.logout();
      this.isAuthenticated = false;
      this.user = null;
      this.$router.push({ name: 'UserLogin' });
    },

    // Verificar se usuário tem permissão
    hasPermission(requiredType) {
      if (!this.user) return false;
      return this.user.tipo === requiredType;
    },

    // Verificar se é fornecedor
    isFornecedor() {
      return this.hasPermission('FORNECEDOR');
    },

    // Verificar se é comprador
    isComprador() {
      return this.hasPermission('COMPRADOR');
    },

    // Fazer requisição autenticada
    async authenticatedRequest(url, options = {}) {
      try {
        return await authService.authenticatedRequest(url, options);
      } catch (error) {
        if (error.message.includes('Sessão expirada')) {
          this.logout();
        }
        throw error;
      }
    }
  },

  mounted() {
    this.checkAuth();
  }
};
