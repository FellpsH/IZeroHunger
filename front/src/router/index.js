// src/router/index.js
import Vue from 'vue';
import Router from 'vue-router';
import Login from '../components/UserLogin.vue';
import ProductsList from '../components/ProductsList.vue';
import ProductRegistration from '../components/ProductRegistration.vue';
import ProductsCart from '../components/ProductsCart.vue';
import UserRegistration from '../components/UserRegistration.vue';

Vue.use(Router);

const routes = [
  {
    path: '/userlogin',
    name: 'Login',
    component: Login,
  },
  {
    path: '/productslist',
    name: 'ProductsList',
    component: ProductsList,
  },
  {
    path: '/productregistration',
    name: 'ProductRegistration',
    component: ProductRegistration,
  },
  {
    path: '/productscart',
    name: 'ProductsCart',
    component: ProductsCart,
  },
  {
    path: '/userregistration',
    name: 'UserRegistration',
    component: UserRegistration,
  },
  {
    path: '/',
    redirect: '/productslist', // Redireciona para a lista de produtos como a rota padrão
  },
];

const router = new Router({
  mode: 'history', // Use o modo history para URLs mais limpas
  routes,
});

export default router;
