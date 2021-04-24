import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'Home',
        component: Home
    },
    {
        path: '/rang',
        name: 'Rang',
        component: () => import(/* webpackChunkName: "about" */ '../components/rang/RangPage.vue')
    },
    {
        path: '/about',
        name: 'About',
        component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
    }, {
        path: '/rang/:id',
        name: 'RangEdit',
        component: () => import(/* webpackChunkName: "about" */ '../components/rang/RangInput')
    }
]

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})

export default router
