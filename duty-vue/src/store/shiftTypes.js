import Vue from 'vue'
import Vuex from 'vuex'
import Vapi from "vuex-rest-api"
//http://localhost:8080/duty/rest/shiftTypes
Vue.use(Vuex)

const shiftTypes = new Vapi({
    baseURL: "http://localhost:8080/duty/rest",
    state: {
        rangs: []
    }
})
    // Step 3
    .get({
        action: "getShiftType",
        property: "shiftTypeEntity",
        path: ({id}) => `/shiftTypes/${id}`
    })
    .get({
        action: "pageShiftType",
        property: "shiftTypePage",
        path: ({page, size}) => `/shiftTypes?page=${page}&size=${size}`,
        queryParams: true
    })
    .post({
        action: "updateShiftType",
        property: "shiftTypeEntity",
        path: ({id}) => `/shiftTypes/${id}`
    })
    // Step 4
    .getStore()

export default shiftTypes
