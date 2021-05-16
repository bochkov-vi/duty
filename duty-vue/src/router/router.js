import Vue from 'vue'
import VueRouter from 'vue-router'
import {Trans} from "@/plugins/Translation";

Vue.use(VueRouter)

const routes = [{
    path: '/:locale',
    beforeEnter: Trans.routeMiddleware,
    component: {
        template: "<router-view></router-view>"
    },
    children: [
        {
            path: '',
            name: 'home',
            component: () => import(/* webpackChunkName: "about" */ '../components/HomePage.vue')
        },
        {
            path: 'rang',
            name: 'rangs',
            component: () => import(/* webpackChunkName: "about" */ '../components/rang/RangPage.vue')
        }, {
            path: 'group',
            name: 'groups',
            component: () => import(/* webpackChunkName: "about" */ '../components/employeeGroup/EmployeeGroup.vue')
        },
        {
            path: 'group/:id',
            props: true,
            name: 'EmployeeGroupEdit',
            component: () => import(/* webpackChunkName: "about" */ '../components/employeeGroup/EmployeeGroupEdit.vue')
        }
    ]
}, {
    path: '*',
    redirect() {
        return Trans.defaultLocale;
    }
}]

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})


export default router
