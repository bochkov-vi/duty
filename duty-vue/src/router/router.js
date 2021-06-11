import Vue from 'vue'
import VueRouter from 'vue-router'
import {Trans} from "@/plugins/Translation";
import i18n from "@/i18n";

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
            component: () => import(/* webpackChunkName: "about" */ '../components/HomePage.vue'),
            meta: {
                title: "title"
            }

        },
        {
            path: 'rang',
            name: 'rangs',
            component: () => import(/* webpackChunkName: "about" */ '../components/rang/RangPage.vue'),
            meta: {
                title: "rangs"
            }
        }, {
            path: 'group',
            name: 'groups',
            component: () => import(/* webpackChunkName: "about" */ '../components/employeeGroup/EmployeeGroupPage.vue'),
            meta: {
                title: "groups"
            }
        },
        {
            path: 'employees',
            name: 'employees',
            props: true,
            component: () => import(/* webpackChunkName: "about" */ '../components/employee/EmployeePage.vue'),
            meta: {
                title: "employees"
            }
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
router.beforeEach((to, from, next) => {
    document.title = i18n.t(to.meta.title)
    next()
});

export default router
