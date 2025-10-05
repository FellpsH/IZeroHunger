// Serviço para gerenciar pedidos
import httpClient from './httpClient';

class PedidoService {
  constructor() {
    this.apiUrl = process.env.VUE_APP_API_MODE || 'http://localhost:8080/api';
  }

  // Criar pedido a partir do carrinho
  async criarPedido(usuarioId, cartItems, totalPrice) {
    try {
      // Preparar dados da compra
      const compraData = {
        userId: usuarioId,
        totalPrice: Number(totalPrice), // Garantir que é número
        items: cartItems.map(item => ({
          productId: item.id,
          quantity: item.selectedQuantity || 1
        }))
      };

      const response = await httpClient.post(`/carts/registraCompra`, compraData);
      
      if (response.status === 200) {
        return { success: true, message: 'Compra realizada com sucesso' };
      } else {
        return { success: false, error: 'Erro ao criar pedido' };
      }
    } catch (error) {
      console.error('Erro ao criar pedido:', error);
      return { 
        success: false, 
        error: error.message || 'Erro de conexão ao criar pedido' 
      };
    }
  }

  async buscarPedido() {
    return { 
      success: false, 
      error: 'Endpoint não implementado - usando dados locais'
    };
  }

  // Formatar data para exibição
  formatarData(dataString) {
    const data = new Date(dataString);
    return data.toLocaleDateString('pt-BR', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  // Formatar preço
  formatarPreco(valor) {
    return (Number(valor) || 0).toFixed(2);
  }

  // Mapear status para descrição em português
  mapearStatus(status) {
    const statusMap = {
      'CONFIRMADO': 'Pedido Confirmado',
      'PREPARANDO': 'Preparando Pedido',
      'SAIU_PARA_ENTREGA': 'Saiu para Entrega',
      'ENTREGUE': 'Entregue',
      'CANCELADO': 'Cancelado'
    };
    return statusMap[status] || status;
  }

  // Mapear status para ícone
  mapearIconeStatus(status) {
    const iconeMap = {
      'CONFIRMADO': 'fas fa-check-circle',
      'PREPARANDO': 'fas fa-box',
      'SAIU_PARA_ENTREGA': 'fas fa-truck',
      'ENTREGUE': 'fas fa-home',
      'CANCELADO': 'fas fa-times-circle'
    };
    return iconeMap[status] || 'fas fa-question-circle';
  }
}

// Exportar instância única
export default new PedidoService();
