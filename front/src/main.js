import './styles.css';
import Vue from 'vue';
import App from './App.vue';
import router from './router';
import ElementUI from 'element-ui';
import locale from 'element-ui/lib/locale/lang/pt-br';
import 'element-ui/lib/theme-chalk/index.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import '@fortawesome/fontawesome-free/css/all.css';

Vue.config.productionTip = false;

Vue.use(ElementUI,  { locale });


new Vue({
  router,
  render: h => h(App),
}).$mount('#app');
