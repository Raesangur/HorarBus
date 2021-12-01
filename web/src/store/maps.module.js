import MapsService from "@/services/maps";

export default {
    namespaced: false,
    state: {
        mapsSettings: {},
        geolocation: {
            lat: "",
            lng: "",
        },
        position: "",
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
            if (payload.coords) {
                commit("setGeo", payload);
            } else {
                commit("setPosition", payload);
            }

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
        setPosition(state, payload) {
            state.position = payload;
        }
    },
};