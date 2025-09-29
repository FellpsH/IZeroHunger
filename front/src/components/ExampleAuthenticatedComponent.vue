<template>
  <div class="container mt-4">
    <div class="row">
      <div class="col-md-8 offset-md-2">
        <div class="card">
          <div class="card-header">
            <h4><i class="fas fa-shield-alt"></i> Página Protegida</h4>
          </div>
          <div class="card-body">
            <!-- Informações do usuário -->
            <div v-if="isAuthenticated" class="alert alert-success">
              <h5>Bem-vindo, {{ user.nome }}!</h5>
              <p><strong>Email:</strong> {{ user.email }}</p>
              <p><strong>Tipo:</strong> {{ user.tipo }}</p>
              <p><strong>ID:</strong> {{ user.id }}</p>
            </div>

            <!-- Botões baseados no tipo de usuário -->
            <div class="row">
              <div class="col-md-6">
                <div class="card">
                  <div class="card-body">
                    <h6>Teste de Permissões</h6>
                    <p v-if="isFornecedor()" class="text-success">
                      <i class="fas fa-check"></i> Você é um fornecedor
                    </p>
                    <p v-if="isComprador()" class="text-info">
                      <i class="fas fa-check"></i> Você é um comprador
                    </p>
                  </div>
                </div>
              </div>
              
              <div class="col-md-6">
                <div class="card">
                  <div class="card-body">
                    <h6>Teste de API</h6>
                    <button @click="testAuthenticatedRequest" class="btn btn-primary">
                      <i class="fas fa-test-tube"></i> Testar Requisição Autenticada
                    </button>
                    <div v-if="apiResponse" class="mt-2">
                      <small class="text-muted">{{ apiResponse }}</small>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <!-- Botão de logout -->
            <div class="mt-3">
              <button @click="logout" class="btn btn-danger">
                <i class="fas fa-sign-out-alt"></i> Fazer Logout
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import authMixin from '../mixins/authMixin';
import httpClient from '../services/httpClient';

export default {
  name: 'ExampleAuthenticatedComponent',
  mixins: [authMixin],
  data() {
    return {
      apiResponse: null
    };
  },
  methods: {
    async testAuthenticatedRequest() {
      try {
        const response = await httpClient.getUserProfile();
        this.apiResponse = `Sucesso! Resposta: ${JSON.stringify(response)}`;
      } catch (error) {
        this.apiResponse = `Erro: ${error.message}`;
      }
    }
  }
};
</script>

<style scoped>
.card {
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.alert {
  border-radius: 8px;
}
</style>
