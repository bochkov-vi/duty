import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'Home',
        component: () => import(/* webpackChunkName: "about" */ '../components/HomePage')
    },
    {
        path: '/rang',
        name: 'Rang',
        component: () => import(/* webpackChunkName: "about" */ '../components/rang/RangPage.vue')
    }, {
        path: '/group',
        name: 'EmployeeGroups',
        component: () => import(/* webpackChunkName: "about" */ '../components/employeeGroup/EmployeeGroup.vue')
    },
    {
        path: '/group/edit/:id',
        props: true,
        name: 'EmployeeGroupEdit',
        component: () => import(/* webpackChunkName: "about" */ '../components/employeeGroup/EmployeeGroupEdit.vue')
    }
]

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})

export default router
