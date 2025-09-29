// Guard de rota para autenticação
import authService from '../services/authService';

export function requireAuth(to, from, next) {
  if (authService.isAuthenticated()) {
    next();
  } else {
    next({ name: 'UserLogin' });
  }
}

export function requireFornecedor(to, from, next) {
  if (authService.isAuthenticated()) {
    const user = authService.getUser();
    if (user && user.tipo === 'FORNECEDOR') {
      next();
    } else {
      next({ name: 'ProductsList' }); // Redirecionar para lista de produtos
    }
  } else {
    next({ name: 'UserLogin' });
  }
}

export function requireComprador(to, from, next) {
  if (authService.isAuthenticated()) {
    const user = authService.getUser();
    if (user && (user.tipo === 'COMPRADOR' || user.tipo === 'FORNECEDOR')) {
      next();
    } else {
      next({ name: 'ProductsList' }); // Redirecionar para lista de produtos
    }
  } else {
    next({ name: 'UserLogin' });
  }
}

export function guestOnly(to, from, next) {
  if (authService.isAuthenticated()) {
    next({ name: 'ProductsList' }); // Se já estiver logado, ir para produtos
  } else {
    next();
  }
}
