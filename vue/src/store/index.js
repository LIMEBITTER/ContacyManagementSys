import Vue from "vue";
import Vuex from "vuex";

Vue.use(Vuex);
export default new Vuex.Store({
    state: {
        message:null,
    },
    //同步更新状态
    mutations: {
        setMessage(state, { message, type }) {
            state.message = { message, type };
        },
        clearMessage(state) {
            state.message = null;
        },
    },
    getters: {
        getMessage: state => state.message
    },
    actions: {},
    modules: {}
});


