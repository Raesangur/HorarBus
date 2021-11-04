import UserService from "@/services/user";

export default {
    namespaced: false,
    state: {
        user: {},
        pref: {},
    },
    actions: {
        async getUser({ commit }) {
            try {
                const user = await UserService.getUser();
                commit("getUser", user);
            } catch (err) {
                console.log(err);
            }
        },
        async putUser({ dispatch }, payload) {
            try {
                UserService.putUser(payload).then(() => {
                    dispatch("getMe");
                });
            } catch (err) {
                console.log(err);
            }
        },
        async getPref({ commit }) {
            try {
                const pref = await UserService.getPref();
                commit("getPref", pref);
            } catch (err) {
                console.log(err);
            }
        },
        async putPref({ dispatch }, payload) {
            try {
                UserService.putPref(payload).then(() => {
                    dispatch("getPref");
                });
            } catch (err) {
                console.log(err);
            }
        },
    },

    getters: {},
    mutations: {
        getUser(state, payload) {
            state.me = payload.data;
        },
        getPref(state, payload) {
            state.pref = payload.data;
        },
    },
};