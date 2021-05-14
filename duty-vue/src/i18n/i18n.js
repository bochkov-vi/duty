import VueI18n from 'vue-i18n'
import Vue from 'vue'
Vue.use(VueI18n)

const i18n = new VueI18n({
    locale: 'ru', // установка локализации по умолчанию
    messages // установка сообщений локализаций
})
export default i18n