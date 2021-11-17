import MapsService from "@/services/maps";

export default {
    namespaced: false,
    state: {
        mapsSettings: {},
        geolocation: {
            lat: "",
            lng: "",
        },
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
        async setGeo({ commit }, payload) {
            commit("setGeo", payload);
        },
    },

    getters: {},
    mutations: {
        getMaps(state, payload) {
            state.mapsSettings = payload.data;
        },
        setGeo(state, payload) {
            state.geolocation.lng = payload.coords.longitude;
            state.geolocation.lat = payload.coords.latitude;
        },
    },
};