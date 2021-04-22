import Vue from 'vue';
import Vuetify from 'vuetify/lib/framework';
import ru from 'vuetify/es5/locale/ru.js'
import en from 'vuetify/es5/locale/en.js'

Vue.use(Vuetify);


export default new Vuetify({
    lang: {
        locales: { en, ru },
        current: 'ru',
    }
});
