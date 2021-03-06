import UserService from "@/services/user";

export default {
    namespaced: false,
    state: {
        user: {},
        pref: {},
    },
    actions: {
        getUser({ commit }) {
            UserService.getUser()
                .then((user) => {
                    commit("getUser", user.data);
                    commit("getPref", user.data.preferences);
                })
                .catch((err) => {
                    console.log(err);
                });
        },
        async putUser({ dispatch }, payload) {
            UserService.putUser(payload)
                .then(() => {
                    if (payload.Ical) {
                        location.reload();
                    }
                    dispatch("getUser");
                })
                .catch((err) => {
                    console.log(err.response);
                });
        },
    },

    getters: {},
    mutations: {
        getUser(state, payload) {
            state.user.firstname = payload.firstname;
            state.user.lastname = payload.lastname;
        },
        getPref(state, payload) {
            state.pref = payload;
        },
    },
};