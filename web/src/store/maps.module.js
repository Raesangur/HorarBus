import MapsService from "@/services/maps";

export default {
  namespaced: false,
  state: {
    mapsSettings: {},
  },
  actions: {
    async getMaps({ commit }) {
      try {
        const maps = await MapsService.getDefault();
        commit("getMaps", maps);
      } catch (err) {
        console.log(err);
      }
    },
  },

  getters: {},
  mutations: {
    getMaps(state, payload) {
      state.mapsSettings = payload.data;
    },
  },
};
