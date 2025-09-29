<template>
  <div class="container d-flex justify-content-center align-items-center min-vh-100">
    <div class="card shadow" style="width: 400px; background-color: #f8f9fa;">
      <div class="card-body">
        <h1 class="text-center mb-4"><i class="fa fa-lock"></i> Login</h1>
        <form @submit.prevent="handleLogin">
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
                class="form-control"
                placeholder="Enter your email"
                required
              />
            </div>
          </div>
          <div class="mb-3">
            <label for="password" class="form-label">Password</label>
            <div class="input-group">
              <span class="input-group-text">
                <i class="fa fa-lock"></i>
              </span>
              <input
                type="password"
                id="password"
                v-model="password"
                class="form-control"
                placeholder="Enter your password"
                required
              />
            </div>
          </div>
          <button type="submit" class="btn btn-primary w-100 mt-3">
            <i class="fa fa-sign-in"></i> Login
          </button>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'UserLogin',
  data() {
    return {
      username: '',
      password: '',
      apiUrl: process.env.VUE_APP_API_MODE, // Certifique-se de que o URL da API esteja configurado corretamente
    };
  },
  methods: {
    async handleLogin() {
      try {
        const response = await fetch(`${this.apiUrl}/usuarios/login`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            email: this.username,
            senha: this.password,
          }),
        });

        if (response.ok) {
          const data = await response.json();
          localStorage.setItem('user', JSON.stringify(data));
          this.$emit('login-success');
          this.$router.push({ name: 'ProductsList' }); // Navegar para a lista de produtos
        } else {
          alert('Credenciais inválidas');
        }
      } catch (error) {
        console.error(error);
        alert('Erro ao tentar fazer login. Tente novamente.');
      }
    },
    logout() {
      localStorage.removeItem('user');
      this.$router.push({ name: 'UserLogin' }); // Redireciona para a página de login
    },
  },
  mounted() {
    console.log('Componente de login carregado!');
    const user = localStorage.getItem('user');
    if (user) {
      this.$emit('login-success');
      
    }
  },
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
