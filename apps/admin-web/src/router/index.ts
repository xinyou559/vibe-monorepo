import { createRouter, createWebHistory } from 'vue-router'
import Layout from '../layout/index.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      component: Layout,
      redirect: '/services',
      children: [
        {
          path: 'services',
          name: 'Services',
          component: () => import('../views/ServiceManage.vue'),
          meta: { title: '服务管理' }
        },
        {
          path: 'orders',
          name: 'Orders',
          component: () => import('../views/OrderManage.vue'),
          meta: { title: '订单管理' }
        },
        {
          path: 'users',
          name: 'Users',
          component: () => import('../views/UserManage.vue'),
          meta: { title: '用户与分销' }
        }
      ]
    }
  ]
})

export default router