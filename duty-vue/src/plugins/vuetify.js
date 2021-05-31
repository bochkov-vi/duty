import Vue from 'vue';
import Vuetify from 'vuetify/lib';
import '@mdi/font/css/materialdesignicons.css'
import i18n from "@/i18n";

Vue.use(Vuetify);


export default new Vuetify({
    icons: {
        iconfont: 'mdi', // default - only for display purposes
    },
    lang: {
        t: (key, ...params) => i18n.t(key, params),
    }
});
