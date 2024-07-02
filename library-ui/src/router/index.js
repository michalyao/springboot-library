import {createRouter, createWebHistory} from 'vue-router'
import Layout from '../layout/Layout.vue';

const routes = [
    {
        path: '/',
        name: 'layout',
        component: Layout,
        children: [
            {
                path: 'book',
                name: 'book',
                component: () => import("@/views/Book.vue")
            },
            {
                path: 'person',
                name: 'Person',
                component: () => import("@/views/Person.vue")
            },
            {
                path: 'password',
                name: 'Password',
                component: () => import("@/views/Password.vue")
            },
            {
                path:'user',
                name:'user',
                component:() => import("@/views/User.vue")
            },
        ]
    },
    {
        path: '/login',
        name: 'login',
        component: () => import("@/views/Login.vue")
    },
    {
        path: '/register',
        name: 'register',
        component: () => import("@/views/Register.vue")
    },

]

const router = createRouter({
    history: createWebHistory(),
    routes
})

router.beforeEach((to, from, next) => {
    const token = localStorage.getItem('token');
    if (to.name !== 'login' && to.name !== 'register' && !token) {
        next({ name: 'login' });
    } else {
        next();
    }
});

export default router
