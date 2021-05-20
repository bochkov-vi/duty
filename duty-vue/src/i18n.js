import Vue from 'vue'
import VueI18n from 'vue-i18n'
import en from '@/locales/en.json'
import ru from '@/locales/ru.json'
Vue.use(VueI18n)


const i18n = new VueI18n({
    locale: process.env.VUE_APP_I18N_LOCALE || 'ru',
    fallbackLocale: process.env.VUE_APP_I18N_FALLBACK_LOCALE || 'en',
    messages: { en ,ru}
})

export default i18n;
