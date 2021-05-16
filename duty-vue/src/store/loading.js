
const LOADING_VUEX_MODULE = {
    state: {
        loading: false
    },
    mutations: {
        SET_LOADING: (state, loading) => state.loading = loading
    },
    getters: {
        GET_LOADING: (state) => state.loading
    },
    actions: {
        SET_LOADING: (ctx, loading) => ctx.commit("SET_LOADING", loading)
    }
}

export {LOADING_VUEX_MODULE}


