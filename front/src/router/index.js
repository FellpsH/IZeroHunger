// src/router/index.js
import Vue from 'vue';
import Router from 'vue-router';
import Login from '../components/UserLogin.vue';
import ProductsList from '../components/ProductsList.vue';
import ProductRegistration from '../components/ProductRegistration.vue';
import ProductsCart from '../components/ProductsCart.vue';
import UserRegistration from '../components/UserRegistration.vue';
import { requireFornecedor, guestOnly } from './authGuard';

Vue.use(Router);

const routes = [
  {
    path: '/userlogin',
    name: 'UserLogin',
    component: Login,
    beforeEnter: guestOnly, // Apenas usuários não autenticados
  },
  {
    path: '/userregistration',
    name: 'UserRegistration',
    component: UserRegistration,
    beforeEnter: guestOnly, // Apenas usuários não autenticados
  },
  {
    path: '/productslist',
    name: 'ProductsList',
    component: ProductsList,
    // Público - qualquer um pode ver produtos
  },
  {
    path: '/productregistration',
    name: 'ProductRegistration',
    component: ProductRegistration,
    beforeEnter: requireFornecedor, // Apenas fornecedores
  },
  {
    path: '/productscart',
    name: 'ProductsCart',
    component: ProductsCart,
    // Rota liberada para autenticados de qualquer tipo
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
