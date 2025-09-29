// Cliente HTTP com interceptors para JWT
import authService from './authService';

class HttpClient {
  constructor() {
    this.baseURL = process.env.VUE_APP_API_URL || 'http://localhost:8080/api';
  }

  // Método genérico para fazer requisições
  async request(endpoint, options = {}) {
    const url = `${this.baseURL}${endpoint}`;
    const token = authService.getToken();

    const defaultOptions = {
      headers: {
        'Content-Type': 'application/json',
        ...(token && { 'Authorization': `Bearer ${token}` }),
        ...options.headers
      }
    };

    try {
      const response = await fetch(url, { ...defaultOptions, ...options });
      
      // Se token expirou, fazer logout automático
      if (response.status === 401) {
        authService.logout();
        // Redirecionar para login se estiver em uma rota protegida
        if (window.location.pathname !== '/login') {
          window.location.href = '/login';
        }
        throw new Error('Sessão expirada. Faça login novamente.');
      }

      return response;
    } catch (error) {
      console.error('Erro na requisição HTTP:', error);
      throw error;
    }
  }

  // Métodos HTTP específicos
  async get(endpoint, options = {}) {
    return this.request(endpoint, { ...options, method: 'GET' });
  }

  async post(endpoint, data, options = {}) {
    return this.request(endpoint, {
      ...options,
      method: 'POST',
      body: JSON.stringify(data)
    });
  }

  async put(endpoint, data, options = {}) {
    return this.request(endpoint, {
      ...options,
      method: 'PUT',
      body: JSON.stringify(data)
    });
  }

  async delete(endpoint, options = {}) {
    return this.request(endpoint, { ...options, method: 'DELETE' });
  }

  // Métodos específicos da API
  async login(email, password) {
    const response = await this.post('/usuarios/login', { email, password });
    return response.json();
  }

  async loginWithJWT(email, password) {
    const response = await this.post('/auth/login', { email, password });
    return response.json();
  }

  async validateToken() {
    const response = await this.get('/auth/validate');
    return response.json();
  }

  async getProducts() {
    const response = await this.get('/products');
    return response.json();
  }

  async getCategories() {
    const response = await this.get('/categories');
    return response.json();
  }

  async getUserProfile() {
    const response = await this.get('/usuarios/profile');
    return response.json();
  }
}

// Exportar instância única
export default new HttpClient();
