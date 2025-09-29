<template>
    <div v-if="isVisible" class="splash-screen">
      <img src="@/assets/logo3.png" alt="App Logo" class="logo" />
    </div>
  </template>
  
  <script>
  export default {
    name: 'SplashScreen',

    data() {
      return {
        isVisible: true
      };
    },
    mounted() {
      setTimeout(() => {
        this.isVisible = false;
      }, 2000); // Define a duração do splash screen
    }
  };
  </script>
  
  
<style scoped>
.splash-screen {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(255, 255, 255, 1.0); /* Fundo levemente transparente */
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.logo {
  width: 120px;
  animation: heartbeat 1.1s ease-in-out infinite; /* Animação de batimento cardíaco */
}

/* Animação de batimento cardíaco */
@keyframes heartbeat {
  0% {
    transform: scale(1);
    opacity: 0.9;
  }
  15% {
    transform: scale(1.2);
    opacity: 1;
  }
  30% {
    transform: scale(1);
    opacity: 0.9;
  }
  45% {
    transform: scale(1.2);
    opacity: 1;
  }
  60% {
    transform: scale(1);
    opacity: 0.9;
  }
  100% {
    transform: scale(1);
    opacity: 0.9;
  }
}
</style>