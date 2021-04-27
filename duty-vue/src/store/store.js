import Vue from 'vue'
import Vuex from 'vuex'
import rangs from "@/store/rang";

Vue.use(Vuex)
export default new Vuex.Store({
    ...rangs
})
