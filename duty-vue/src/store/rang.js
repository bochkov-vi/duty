import Vue from 'vue'
import Vuex from 'vuex'
import Vapi from "vuex-rest-api"

Vue.use(Vuex)

const rangs = new Vapi({
    baseURL: "http://localhost:8080/duty/rest",
    state: {
        rangs: []
    }
})
    // Step 3
    .get({
        action: "getRang",
        property: "entity",
        path: ({id}) => `/rangs/${id}`
    })
    .get({
        action: "pageRang",
        property: "page",
        path: ({page, size}) => `/rangs?page=${page}&size=${size}`,
        queryParams: true
    })
    .post({
        action: "updateRang",
        property: "entity",
        path: ({id}) => `/rangs/${id}`
    })
    // Step 4
    .getStore()

export default rangs
