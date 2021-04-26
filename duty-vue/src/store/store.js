import Vue from 'vue'
import Vuex from 'vuex'
import rangs from "@/store/rang";
import shiftTypes from "@/store/shiftTypes";

Vue.use(Vuex)
export default new Vuex.Store({
    state: {},
    mutations: {},
    actions: {

    },
    modules: {
        rangs: {namespaced: true, ...rangs},
        shiftType: {namespaced: true, ...shiftTypes}
    },

})
