import MapsService from "@/services/maps";

export default {
  namespaced: false,
  state: {
    mapsSettings: {},
    geolocation: {
      lat: "",
      lng: "",
    },
    itinerary: {},
    place: {},
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
    async getItinerary({ commit }, params) {
      MapsService.getItinerary({ body: params })
        .then((itinerary) => {
          console.log(itinerary);
          commit("getItinerary", itinerary);
        })
        .catch((err) => {
          console.log(err.response);
        });
    },
    async getPlace({ commit }, payload) {
      MapsService.getPlace(payload)
        .then((place) => {
          console.log(place);
          commit("getPlace", place);
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
    getItinerary(state, payload) {
      state.itinerary = payload;
    },
    getPlace(state, payload) {
      state.place = payload;
    },
  },
};
