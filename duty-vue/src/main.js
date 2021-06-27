import Vue from 'vue'
import App from './App.vue'
import vuetify from './plugins/vuetify';
import i18n from "@/i18n";
import router from './router/router'
import store from './store/store'
import {Trans} from "@/plugins/Translation";
import '@mdi/font/css/materialdesignicons.css'
import "@fontsource/roboto";

Vue.prototype.$i18nRoute = Trans.i18nRoute.bind(Trans)
Vue.config.productionTip = false
console.log(process.env)
new Vue({
    vuetify,
    i18n,
    router,
    store,
    render: h => h(App)
}).$mount('#app')
