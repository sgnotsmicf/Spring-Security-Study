import { createRouter, createWebHistory } from 'vue-router';
import Login from '../components/Login.vue';
import UserList from '../components/UserList.vue';

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/',
    name: 'UserList',
    component: UserList
  }
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
});

export default router;