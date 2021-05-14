import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)
export default new Vuex.Store({
    state: {
        messageSeq: 0,
        messages: [],
        loading: true
    },
    mutations: {
        ADD_MESSAGE: (state, msg) => {
            state.messageSeq++
            if ((typeof msg) === "string") {
                msg = {message: msg, type: "error"}
            }
            msg["id"] = state.messageSeq
            state.messages.unshift(msg)

        },
        REMOVE_MESSAGE: (state, msg) => state.messages = state.messages.filter(el => el.id !== msg.id),
        SET_LOADING: (state, loading) => state.loading = loading
    },
    getters: {
        GET_MESSAGES: (state) => state.messages,
        GET_LOADING: (state) => state.loading
    },
    actions: {
        ADD_MESSAGE: (context, message) => {
            context.commit('ADD_MESSAGE', message)
            setTimeout(() => context.commit('REMOVE_MESSAGE', message), 10000)
        },
        REMOVE_MESSAGE: (context, message) => context.commit('REMOVE_MESSAGE', message),
        SET_LOADING: (ctx, loading) => ctx.commit("SET_LOADING", loading)
    }
})
