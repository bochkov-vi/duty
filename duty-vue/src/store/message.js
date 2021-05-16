

const MESSAGES_VUEX_MODULE = {

    state: {
        messageSeq: 0,
        messages: [],
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
    },
    getters: {
        GET_MESSAGES: (state) => state.messages,
    },
    actions: {
        ADD_MESSAGE: (context, message) => {
            context.commit('ADD_MESSAGE', message)
            setTimeout(() => context.commit('REMOVE_MESSAGE', message), 10000)
        },
        REMOVE_MESSAGE: (context, message) => context.commit('REMOVE_MESSAGE', message),
    }

}

export {MESSAGES_VUEX_MODULE}
