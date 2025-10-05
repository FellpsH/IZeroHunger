<template>
  <div class="tracking-page">
    <Navbar />
    
    <!-- Header do Rastreamento -->
    <div class="tracking-header">
      <div class="container">
        <div class="row align-items-center">
          <div class="col-md-8">
            <h1 class="tracking-title">
              <i class="fas fa-truck me-3"></i>
              Rastreamento do Pedido
            </h1>
            <p class="tracking-subtitle" v-if="orderData">
              Pedido #{{ orderData.orderId }}
            </p>
          </div>
          <div class="col-md-4 text-end">
            <router-link :to="{ name: 'ProductsList' }" class="btn btn-outline-light btn-pill">
              <i class="fas fa-arrow-left me-2"></i>
              Voltar às Compras
            </router-link>
          </div>
        </div>
      </div>
    </div>

    <div class="container py-4">
      <div v-if="orderData" class="row">
        <!-- Timeline de Rastreamento -->
        <div class="col-lg-8">
          <div class="tracking-timeline">
            <h4 class="timeline-title">
              <i class="fas fa-route me-2"></i>
              Status do Pedido
            </h4>
            
            <div class="timeline">
              <div 
                v-for="(step, index) in trackingSteps" 
                :key="index"
                class="timeline-item"
                :class="{ 
                  'completed': step.completed, 
                  'active': step.active,
                  'current': step.current
                }"
              >
                <div class="timeline-marker">
                  <i :class="step.icon" v-if="!step.current"></i>
                  <div v-else class="pulse-marker">
                    <i :class="step.icon"></i>
                  </div>
                </div>
                <div class="timeline-content">
                  <h5 class="timeline-step-title">{{ step.title }}</h5>
                  <p class="timeline-step-description">{{ step.description }}</p>
                  <small class="timeline-step-time" v-if="step.time">{{ step.time }}</small>
                </div>
              </div>
            </div>
          </div>
          
          <!-- Mapa Simulado -->
          <div class="delivery-map">
            <h4 class="map-title">
              <i class="fas fa-map-marked-alt me-2"></i>
              Localização da Entrega
            </h4>
            <div class="map-container">
              <div class="map-placeholder">
                <div class="delivery-truck" :style="{ left: truckPosition + '%' }">
                  <i class="fas fa-truck"></i>
                </div>
                <div class="route-line"></div>
                <div class="location-marker start">
                  <i class="fas fa-store"></i>
                  <span>Loja</span>
                </div>
                <div class="location-marker end">
                  <i class="fas fa-home"></i>
                  <span>Destino</span>
                </div>
              </div>
              <p class="map-info">
                <i class="fas fa-info-circle me-2"></i>
                Tempo estimado de entrega: {{ estimatedTime }}
              </p>
            </div>
          </div>
        </div>
        
        <!-- Resumo do Pedido -->
        <div class="col-lg-4">
          <div class="order-summary-tracking">
            <h4 class="summary-title">
              <i class="fas fa-receipt me-2"></i>
              Resumo do Pedido
            </h4>
            
            <div class="order-info">
              <div class="info-item">
                <span class="info-label">Pedido:</span>
                <span class="info-value">#{{ orderData.orderId }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">Data:</span>
                <span class="info-value">{{ formatDate(orderData.orderDate) }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">Total:</span>
                <span class="info-value">R$ {{ formatPrice(orderData.total) }}</span>
              </div>
            </div>
            
            <hr class="summary-divider">
            
            <div class="delivery-info">
              <h5 class="delivery-title">
                <i class="fas fa-map-marker-alt me-2"></i>
                Endereço de Entrega
              </h5>
              <p class="delivery-address">{{ orderData.address }}</p>
            </div>
            
            <hr class="summary-divider">
            
            <div class="items-list">
              <h5 class="items-title">
                <i class="fas fa-shopping-bag me-2"></i>
                Itens do Pedido
              </h5>
              <div 
                v-for="item in orderData.items" 
                :key="item.id"
                class="item-summary"
              >
                <div class="item-info">
                  <span class="item-name">{{ item.name }}</span>
                  <span class="item-quantity">{{ item.selectedQuantity || 1 }}x</span>
                </div>
                <span class="item-price">R$ {{ formatPrice(item.price * (item.selectedQuantity || 1)) }}</span>
              </div>
            </div>
            
            <div class="contact-support">
              <h5 class="support-title">
                <i class="fas fa-headset me-2"></i>
                Precisa de Ajuda?
              </h5>
              <p class="support-text">Entre em contato conosco</p>
              <button class="btn btn-support">
                <i class="fas fa-phone me-2"></i>
                (11) 9999-9999
              </button>
            </div>
          </div>
        </div>
      </div>
      
      <!-- Pedido não encontrado -->
      <div v-else class="no-order">
        <div class="no-order-content">
          <i class="fas fa-search no-order-icon"></i>
          <h3>Pedido não encontrado</h3>
          <p>Não foi possível encontrar informações sobre este pedido.</p>
          <router-link :to="{ name: 'ProductsList' }" class="btn btn-primary btn-lg">
            <i class="fas fa-arrow-left me-2"></i>
            Voltar às Compras
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Navbar from './NavBar.vue';
import pedidoService from '../services/pedidoService';
import { formatCurrency } from '../utils/currency';

export default {
  name: 'OrderTracking',
  components: {
    Navbar,
  },
  data() {
    return {
      orderData: null,
      truckPosition: 25,
      estimatedTime: '25-35 minutos',
      trackingSteps: [
        {
          title: 'Pedido Confirmado',
          description: 'Seu pedido foi recebido e confirmado',
          icon: 'fas fa-check-circle',
          completed: true,
          active: false,
          current: false,
          time: null
        },
        {
          title: 'Preparando Pedido',
          description: 'Estamos separando seus produtos',
          icon: 'fas fa-box',
          completed: true,
          active: false,
          current: false,
          time: null
        },
        {
          title: 'Saiu para Entrega',
          description: 'Seu pedido está a caminho',
          icon: 'fas fa-truck',
          completed: false,
          active: true,
          current: true,
          time: null
        },
        {
          title: 'Entregue',
          description: 'Pedido entregue com sucesso',
          icon: 'fas fa-home',
          completed: false,
          active: false,
          current: false,
          time: null
        }
      ]
    };
  },
  mounted() {
    this.loadOrderData();
    this.startSimulation();
  },
  beforeDestroy() {
    // Limpar intervalos para evitar vazamentos de memória
    if (this.statusUpdateInterval) {
      clearInterval(this.statusUpdateInterval);
    }
    if (this.truckInterval) {
      clearInterval(this.truckInterval);
    }
  },
  methods: {
    async loadOrderData() {
      const orderString = localStorage.getItem('currentOrder');
      if (orderString) {
        try {
          const localOrderData = JSON.parse(orderString);
          
          // Log para debug
          console.log('Dados locais do pedido:', localOrderData);
          console.log('Total local:', localOrderData.total);
          
          // Se temos um orderId, buscar dados atualizados do backend
          if (localOrderData.orderId) {
            const result = await pedidoService.buscarPedido(localOrderData.orderId);
            
            if (result.success) {
              // Usar dados do backend, mas manter total local se backend retornar 0
              this.orderData = {
                orderId: result.pedido.pedidoId,
                items: result.pedido.itens || localOrderData.items,
                total: result.pedido.total || localOrderData.total, // Usar total local se backend retornar 0
                address: result.pedido.enderecoEntrega || localOrderData.address,
                orderDate: result.pedido.dataPedido,
                status: result.pedido.status,
                statusDescricao: result.pedido.statusDescricao
              };
              
              // Atualizar timeline baseado no status real
              this.updateTrackingFromStatus(result.pedido.status);
            } else {
              // Fallback para dados locais se não conseguir buscar do backend
              console.warn('Não foi possível buscar dados do backend, usando dados locais');
              this.orderData = localOrderData;
            }
          } else {
            // Usar dados locais se não tiver orderId
            this.orderData = localOrderData;
          }
          
          // Log final para debug
          console.log('OrderData final:', this.orderData);
          console.log('Total final:', this.orderData.total);
          
          this.updateTrackingTimes();
        } catch (error) {
          console.error('Erro ao carregar dados do pedido:', error);
        }
      }
    },
    
    updateTrackingFromStatus(status) {
      // Mapear status do backend para os steps da timeline
      const statusMap = {
        'CONFIRMADO': 0,
        'PREPARANDO': 1,
        'SAIU_PARA_ENTREGA': 2,
        'ENTREGUE': 3,
        'CANCELADO': -1
      };
      
      const currentStepIndex = statusMap[status];
      
      if (currentStepIndex === -1) {
        // Pedido cancelado
        this.trackingSteps.forEach((step) => {
          step.completed = false;
          step.active = false;
          step.current = false;
        });
        return;
      }
      
      // Atualizar steps baseado no status atual
      this.trackingSteps.forEach((step, index) => {
        if (index < currentStepIndex) {
          step.completed = true;
          step.active = false;
          step.current = false;
        } else if (index === currentStepIndex) {
          step.completed = true;
          step.active = true;
          step.current = true;
        } else {
          step.completed = false;
          step.active = false;
          step.current = false;
        }
      });
      
      // Atualizar posição do caminhão baseado no status
      const truckPositions = {
        'CONFIRMADO': 10,
        'PREPARANDO': 25,
        'SAIU_PARA_ENTREGA': 50,
        'ENTREGUE': 85
      };
      
      this.truckPosition = truckPositions[status] || 25;
      
      // Atualizar tempo estimado baseado no status
      const estimatedTimes = {
        'CONFIRMADO': '30-40 minutos',
        'PREPARANDO': '20-30 minutos',
        'SAIU_PARA_ENTREGA': '10-20 minutos',
        'ENTREGUE': 'Entregue!'
      };
      
      this.estimatedTime = estimatedTimes[status] || '25-35 minutos';
    },
    
    updateTrackingTimes() {
      if (!this.orderData) return;
      
      const orderDate = new Date(this.orderData.orderDate);
      
      // Simular horários baseados na data do pedido
      this.trackingSteps[0].time = this.formatTime(orderDate);
      this.trackingSteps[1].time = this.formatTime(new Date(orderDate.getTime() + 5 * 60000)); // +5 min
      this.trackingSteps[2].time = this.formatTime(new Date(orderDate.getTime() + 15 * 60000)); // +15 min
    },
    
    startSimulation() {
      // Atualizar status do pedido periodicamente (a cada 30 segundos)
      this.statusUpdateInterval = setInterval(async () => {
        if (this.orderData && this.orderData.orderId) {
          const result = await pedidoService.buscarPedido(this.orderData.orderId);
          
          if (result.success && result.pedido.status !== this.orderData.status) {
            // Status mudou, atualizar dados
            this.orderData.status = result.pedido.status;
            this.orderData.statusDescricao = result.pedido.statusDescricao;
            this.updateTrackingFromStatus(result.pedido.status);
            
            // Atualizar localStorage
            localStorage.setItem('currentOrder', JSON.stringify(this.orderData));
          }
        }
      }, 30000);
      
      // Simular movimento do caminhão apenas se não estiver entregue
      this.truckInterval = setInterval(() => {
        if (this.orderData && this.orderData.status !== 'ENTREGUE' && this.truckPosition < 75) {
          this.truckPosition += 0.5;
          
          // Atualizar tempo estimado apenas se não for status final
          if (!['ENTREGUE', 'CANCELADO'].includes(this.orderData.status)) {
            const remaining = Math.max(5, Math.floor((75 - this.truckPosition) * 0.8));
            this.estimatedTime = `${remaining}-${remaining + 10} minutos`;
          }
        }
      }, 3000);
    },
    
    formatPrice(value) {
      return formatCurrency(value);
    },
    
    formatDate(dateString) {
      const date = new Date(dateString);
      return date.toLocaleDateString('pt-BR', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
      });
    },
    
    formatTime(date) {
      return date.toLocaleTimeString('pt-BR', {
        hour: '2-digit',
        minute: '2-digit'
      });
    }
  }
};
</script>

<style scoped>
/* Layout Principal */
.tracking-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
}

/* Header do Rastreamento */
.tracking-header {
  background: linear-gradient(135deg, #007bff 0%, #0056b3 100%);
  color: white;
  padding: 2rem 0;
  box-shadow: 0 4px 20px rgba(0, 123, 255, 0.3);
}

.tracking-title {
  font-size: 2.5rem;
  font-weight: 700;
  margin: 0;
  text-shadow: 0 2px 4px rgba(0,0,0,0.2);
}

.tracking-subtitle {
  font-size: 1.1rem;
  opacity: 0.9;
  margin: 0.5rem 0 0 0;
}

/* Timeline de Rastreamento */
.tracking-timeline {
  background: white;
  border-radius: 16px;
  padding: 2rem;
  box-shadow: 0 4px 20px rgba(0,0,0,0.08);
  margin-bottom: 2rem;
}

.timeline-title {
  font-size: 1.5rem;
  font-weight: 700;
  color: #212529;
  margin-bottom: 2rem;
  padding-bottom: 1rem;
  border-bottom: 2px solid #f8f9fa;
}

.timeline {
  position: relative;
  padding-left: 2rem;
}

.timeline::before {
  content: '';
  position: absolute;
  left: 1rem;
  top: 0;
  bottom: 0;
  width: 2px;
  background: #e9ecef;
}

.timeline-item {
  position: relative;
  margin-bottom: 2rem;
  padding-left: 2rem;
}

.timeline-item:last-child {
  margin-bottom: 0;
}

.timeline-marker {
  position: absolute;
  left: -2rem;
  top: 0;
  width: 2rem;
  height: 2rem;
  background: #e9ecef;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #6c757d;
  font-size: 0.9rem;
  z-index: 2;
}

.timeline-item.completed .timeline-marker {
  background: #28a745;
  color: white;
}

.timeline-item.active .timeline-marker {
  background: #007bff;
  color: white;
}

.timeline-item.current .timeline-marker {
  background: #007bff;
  color: white;
  box-shadow: 0 0 0 4px rgba(0, 123, 255, 0.2);
}

.pulse-marker {
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0% { transform: scale(1); }
  50% { transform: scale(1.1); }
  100% { transform: scale(1); }
}

.timeline-content {
  padding-top: 0.25rem;
}

.timeline-step-title {
  font-size: 1.1rem;
  font-weight: 600;
  color: #212529;
  margin-bottom: 0.25rem;
}

.timeline-step-description {
  color: #6c757d;
  margin-bottom: 0.25rem;
  font-size: 0.95rem;
}

.timeline-step-time {
  color: #007bff;
  font-weight: 500;
}

/* Mapa de Entrega */
.delivery-map {
  background: white;
  border-radius: 16px;
  padding: 2rem;
  box-shadow: 0 4px 20px rgba(0,0,0,0.08);
}

.map-title {
  font-size: 1.5rem;
  font-weight: 700;
  color: #212529;
  margin-bottom: 1.5rem;
}

.map-container {
  position: relative;
}

.map-placeholder {
  height: 200px;
  background: linear-gradient(135deg, #e3f2fd 0%, #bbdefb 100%);
  border-radius: 12px;
  position: relative;
  overflow: hidden;
  border: 2px solid #e1f5fe;
}

.route-line {
  position: absolute;
  top: 50%;
  left: 10%;
  right: 10%;
  height: 4px;
  background: linear-gradient(90deg, #28a745 0%, #007bff 50%, #ffc107 100%);
  border-radius: 2px;
  transform: translateY(-50%);
}

.delivery-truck {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  background: #007bff;
  color: white;
  width: 40px;
  height: 40px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.2rem;
  box-shadow: 0 4px 12px rgba(0, 123, 255, 0.3);
  transition: left 3s ease-in-out;
  z-index: 3;
}

.location-marker {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  background: white;
  padding: 0.5rem;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  display: flex;
  flex-direction: column;
  align-items: center;
  font-size: 0.8rem;
  font-weight: 600;
  z-index: 2;
}

.location-marker.start {
  left: 5%;
  color: #28a745;
}

.location-marker.end {
  right: 5%;
  color: #ffc107;
}

.location-marker i {
  font-size: 1.2rem;
  margin-bottom: 0.25rem;
}

.map-info {
  margin-top: 1rem;
  padding: 1rem;
  background: rgba(0, 123, 255, 0.1);
  border-radius: 8px;
  color: #007bff;
  font-weight: 500;
  text-align: center;
}

/* Resumo do Pedido */
.order-summary-tracking {
  background: white;
  border-radius: 16px;
  padding: 2rem;
  box-shadow: 0 4px 20px rgba(0,0,0,0.08);
  position: sticky;
  top: 2rem;
}

.summary-title {
  font-size: 1.5rem;
  font-weight: 700;
  color: #212529;
  margin-bottom: 1.5rem;
  padding-bottom: 1rem;
  border-bottom: 2px solid #f8f9fa;
}

.order-info {
  margin-bottom: 1.5rem;
}

.info-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.75rem;
}

.info-label {
  color: #6c757d;
  font-weight: 500;
}

.info-value {
  color: #212529;
  font-weight: 600;
}

.summary-divider {
  margin: 1.5rem 0;
  border-color: #dee2e6;
}

.delivery-title,
.items-title,
.support-title {
  font-size: 1.1rem;
  font-weight: 600;
  color: #495057;
  margin-bottom: 1rem;
}

.delivery-address {
  color: #6c757d;
  margin-bottom: 0;
  padding: 1rem;
  background: #f8f9fa;
  border-radius: 8px;
}

.items-list {
  margin-bottom: 1.5rem;
}

.item-summary {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.75rem 0;
  border-bottom: 1px solid #f8f9fa;
}

.item-summary:last-child {
  border-bottom: none;
}

.item-info {
  display: flex;
  flex-direction: column;
  flex: 1;
}

.item-name {
  font-weight: 500;
  color: #212529;
  margin-bottom: 0.25rem;
}

.item-quantity {
  font-size: 0.9rem;
  color: #6c757d;
}

.item-price {
  font-weight: 600;
  color: #007bff;
}

.contact-support {
  text-align: center;
}

.support-text {
  color: #6c757d;
  margin-bottom: 1rem;
}

.btn-support {
  background: linear-gradient(135deg, #28a745 0%, #20c997 100%);
  border: none;
  border-radius: 8px;
  padding: 0.75rem 1.5rem;
  color: white;
  font-weight: 600;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(40, 167, 69, 0.3);
}

.btn-support:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(40, 167, 69, 0.4);
}

/* Pedido não encontrado */
.no-order {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 60vh;
}

.no-order-content {
  text-align: center;
  max-width: 400px;
}

.no-order-icon {
  font-size: 4rem;
  color: #6c757d;
  margin-bottom: 1.5rem;
  opacity: 0.5;
}

.no-order-content h3 {
  font-size: 1.75rem;
  font-weight: 600;
  color: #495057;
  margin-bottom: 1rem;
}

.no-order-content p {
  color: #6c757d;
  margin-bottom: 2rem;
  font-size: 1.1rem;
}

/* Responsividade */
@media (max-width: 992px) {
  .order-summary-tracking {
    margin-top: 2rem;
    position: static;
  }
}

@media (max-width: 768px) {
  .tracking-title {
    font-size: 2rem;
  }
  
  .tracking-header {
    padding: 1.5rem 0;
  }
  
  .tracking-timeline,
  .delivery-map,
  .order-summary-tracking {
    padding: 1.5rem;
  }
  
  .timeline {
    padding-left: 1.5rem;
  }
  
  .timeline-item {
    padding-left: 1.5rem;
  }
  
  .timeline-marker {
    left: -1.5rem;
  }
}

@media (max-width: 576px) {
  .tracking-title {
    font-size: 1.75rem;
  }
  
  .map-placeholder {
    height: 150px;
  }
  
  .delivery-truck {
    width: 30px;
    height: 30px;
    font-size: 1rem;
  }
  
  .location-marker {
    padding: 0.25rem;
    font-size: 0.7rem;
  }
  
  .location-marker i {
    font-size: 1rem;
  }
}
</style>
