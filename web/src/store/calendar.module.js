import CalendarService from "@/services/calendar";

export default {
  namespaced: false,
  state: {
    events: {},
  },
  actions: {
    async getevents({ commit }) {
      try {
        const events = await CalendarService.getevents();
        commit("getUser", events);
      } catch (err) {
        console.log(err);
      }
    },
  },

  getters: {},
  mutations: {
    getEvents(state, payload) {
      state.events = payload.data;
    },
  },
};
