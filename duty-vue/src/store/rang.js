import Vue from 'vue'
import Vuex from 'vuex'
import restStore from "@/store/restStore";


Vue.use(Vuex)

const rangs = restStore("http://localhost:8080/duty/rest/rangs","RANGS");
export default rangs
