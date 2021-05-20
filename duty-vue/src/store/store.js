import Vue from 'vue'
import Vuex from 'vuex'
import {LOADING_VUEX_MODULE} from "@/store/loading";
import {MESSAGES_VUEX_MODULE} from "@/store/message";

Vue.use(Vuex)
const store = new Vuex.Store({
    modules: {LOADING_VUEX_MODULE, MESSAGES_VUEX_MODULE}
})

export default store;

export function setLoading(loading) {
    const value = loading === true
    store.dispatch('SET_LOADING', value);
}

export function getLoading() {
    const loading = store.getters.GET_LOADING
    return loading;
}

export function error(msg) {
    const message = {message: msg, type: 'error'};
    console.error(message)
    store.dispatch("ADD_MESSAGE", message)
}

export function info(msg) {
    const message = {message: msg, type: 'info'};
    console.info(message)
    store.dispatch("ADD_MESSAGE", message)
}
