import axios from "axios";

export default function restStore(baseUrl, entityName) {

    const state = {};
    state[entityName] = {
        CURRENT: {},
        PAGE: {_embedded: {}, page: {totalElements: 0}},
        LOADING: false,
        OPTIONS: {page: 1, itemsPerPage: 5}
    };
    const mutations = {};
    mutations[entityName + '_SET_CURRENT'] = (state, payload) => state[entityName].CURRENT = payload
    mutations[entityName + '_SET_PAGE'] = (state, payload) => state[entityName].PAGE = payload
    mutations[entityName + '_SET_LOADING'] = (state, payload) => state[entityName].LOADING = payload
    mutations[entityName + '_SET_OPTIONS'] = (state, payload) => state[entityName].OPTIONS = payload

    const getters = {};
    getters[entityName + "_GET_CURRENT"] = (state) => state[entityName].CURRENT
    getters[entityName + "_GET_PAGE"] = (state) => state[entityName].PAGE
    getters[entityName + "_GET_LOADING"] = (state) => state[entityName].LOADING


    const actions = {};
    actions[entityName + '_SET_CURRENT'] = (context, payload) => context.commit(`${entityName}_SET_CURRENT`, payload);
    actions[entityName + '_SET_PAGE'] = (context, payload) => {
        context.commit(`${entityName}_SET_PAGE`, payload)
    };

    actions[entityName + '_LOAD_PAGE'] = (context, options) => {
        context.commit(`${entityName}_SET_LOADING`, true)
        context.commit(`${entityName}_SET_OPTIONS`, options)

        axios.get(baseUrl, {
            params: {
                page: options.page - 1,
                size: options.itemsPerPage,
                sort: options.sortBy[0] + "," + options.sortDesc[0]
            }
        }).then(
            (response) => {
                context.commit(`${entityName}_SET_PAGE`, response.data)
                context.commit(`${entityName}_SET_LOADING`, false)
            }
        ).catch((e) => {
            console.log(e)
            context.commit(`${entityName}_SET_LOADING`, false)
        })

    };
    actions[entityName + '_LOAD_CURRENT'] = (context, id) => {
        context.commit(`${entityName}_SET_LOADING`, true)
        axios.get(baseUrl + "/" + id).then(
            (response) => {
                context.commit(`${entityName}_SET_CURRENT`, response.data)
                context.commit(`${entityName}_SET_LOADING`, false)
            }
        ).catch((e) => {
            console.log(e)
            context.commit(`${entityName}_SET_LOADING`, false)
        })
    };
    actions[entityName + '_LOAD'] = (context, id) => {
        context.commit(`${entityName}_SET_LOADING`, true)
        const result = axios.get(baseUrl + "/" + id).then(
            (response) => response.data
        ).catch((e) => {
            console.log(e)
            context.commit(`${entityName}_SET_LOADING`, false)
        })
        return result;
    };
    actions[entityName + '_SAVE'] = (context, item) => {
        context.commit(`${entityName}_SET_LOADING`, true)
        if (item.new) {
            axios.post(baseUrl, item).then(
                () => {
                    context.commit(`${entityName}_SET_CURRENT`, {})
                    context.dispatch(`${entityName}_LOAD_PAGE`, context.state[entityName].OPTIONS)
                    context.commit(`${entityName}_SET_LOADING`, false)
                }
            ).catch((e) => {
                console.log(e)
                context.commit(`${entityName}_SET_LOADING`, false)
            })
        } else {
            axios.put(baseUrl + "/" + item.id, item).then(
                () => {
                    context.commit(`${entityName}_SET_CURRENT`, {})
                    context.dispatch(`${entityName}_LOAD_PAGE`, context.state[entityName].OPTIONS)
                    context.commit(`${entityName}_SET_LOADING`, false)
                }
            ).catch((e) => {
                console.log(e)
                context.commit(`${entityName}_SET_LOADING`, false)
            })
        }

    };

    actions[entityName + '_DELETE'] = (context, id) => {
        context.commit(`${entityName}_SET_LOADING`, true)
        if (id && id > 0) {
            axios.delete(baseUrl + "/" + id).then(
                () => {
                    context.commit(`${entityName}_SET_CURRENT`, {})
                    context.dispatch(`${entityName}_LOAD_PAGE`, context.state[entityName].OPTIONS)
                    context.commit(`${entityName}_SET_LOADING`, false)
                }
            ).catch((e) => {
                console.log(e)
                context.commit(`${entityName}_SET_LOADING`, false)
            })
        }

    };
    return {
        state,
        mutations,
        getters,
        actions
    }

}