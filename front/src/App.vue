<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<script>
export default {
  name: 'App',

  created() {
    console.log(`API Mode: ${process.env.VUE_APP_API_MODE} - ${process.env.VUE_APP_API_URL}`);
  }

};
</script>
