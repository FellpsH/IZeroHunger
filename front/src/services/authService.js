// Serviço de autenticação JWT
class AuthService {
  constructor() {
    this.apiUrl = process.env.VUE_APP_API_URL || 'http://localhost:8080/api';
    this.tokenKey = 'jwt_token';
    this.userKey = 'user_data';
  }

  // Fazer login e obter token
  async login(email, password) {
    try {
      const response = await fetch(`${this.apiUrl}/usuarios/login`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          email: email,
          senha: password,
        }),
      });

      if (response.ok) {
        const userData = await response.json();
        
        // Salvar dados do usuário no localStorage
        localStorage.setItem(this.userKey, JSON.stringify(userData));
        
        // Se o backend retornar um token JWT, salvar também
        if (userData.token) {
          localStorage.setItem(this.tokenKey, userData.token);
        }
        
        return { success: true, user: userData };
      } else {
        const errorData = await response.json().catch(() => ({ message: 'Erro de autenticação' }));
        return { success: false, error: errorData.message || 'Credenciais inválidas' };
      }
    } catch (error) {
      console.error('Erro no login:', error);
      return { success: false, error: 'Erro de conexão. Tente novamente.' };
    }
  }

  // Fazer login usando o endpoint de auth (com JWT)
  async loginWithJWT(email, password) {
    try {
      const response = await fetch(`${this.apiUrl}/auth/login`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          email: email,
          senha: password,
        }),
      });

      if (response.ok) {
        const authData = await response.json();
        
        // Salvar token JWT
        if (authData.token) {
          localStorage.setItem(this.tokenKey, authData.token);
        }
        
        // Salvar dados do usuário
        const userData = {
          id: authData.userId,
          email: authData.email,
          nome: authData.nome,
          tipo: authData.tipo
        };
        localStorage.setItem(this.userKey, JSON.stringify(userData));
        
        return { success: true, user: userData, token: authData.token };
      } else {
        const errorData = await response.json().catch(() => ({ message: 'Erro de autenticação' }));
        return { success: false, error: errorData.message || 'Credenciais inválidas' };
      }
    } catch (error) {
      console.error('Erro no login JWT:', error);
      return { success: false, error: 'Erro de conexão. Tente novamente.' };
    }
  }

  // Obter token armazenado
  getToken() {
    return localStorage.getItem(this.tokenKey);
  }

  // Obter dados do usuário
  getUser() {
    const userData = localStorage.getItem(this.userKey);
    return userData ? JSON.parse(userData) : null;
  }

  // Verificar se está autenticado
  isAuthenticated() {
    const token = this.getToken();
    const user = this.getUser();
    return !!(token && user);
  }

  // Fazer logout
  logout() {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem(this.userKey);
  }

  // Fazer requisições autenticadas
  async authenticatedRequest(url, options = {}) {
    const token = this.getToken();
    
    if (!token) {
      throw new Error('Token não encontrado. Faça login novamente.');
    }

    const defaultOptions = {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
        ...options.headers
      }
    };

    const response = await fetch(url, { ...defaultOptions, ...options });
    
    // Se token expirou, fazer logout
    if (response.status === 401) {
      this.logout();
      throw new Error('Sessão expirada. Faça login novamente.');
    }

    return response;
  }

  // Validar token no servidor
  async validateToken() {
    try {
      const token = this.getToken();
      if (!token) {
        return { valid: false, error: 'Token não encontrado' };
      }

      const response = await this.authenticatedRequest(`${this.apiUrl}/auth/validate`);
      
      if (response.ok) {
        const data = await response.json();
        return { valid: data.valid, user: data.user };
      } else {
        return { valid: false, error: 'Token inválido' };
      }
    } catch (error) {
      return { valid: false, error: error.message };
    }
  }
}

// Exportar instância única
export default new AuthService();
