import MapsService from "@/services/maps";

export default {
    namespaced: false,
    state: {
        mapsSettings: {},
    },
    actions: {
        async getMaps({ commit }) {
            MapsService.getDefault()
                .then((maps) => {
                    commit("getMaps", maps);
                })
                .catch((err) => {
                    console.log(err.response);
                });
        },
    },

    getters: {},
    mutations: {
        getMaps(state, payload) {
            state.mapsSettings = payload.data;
        },
    },
};