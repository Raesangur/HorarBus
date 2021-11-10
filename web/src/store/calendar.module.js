import CalendarService from "@/services/calendar";

export default {
    namespaced: false,
    state: {
        events: {},
    },
    actions: {
        getEvents({ commit }) {
            CalendarService.getEvents()
                .then((events) => {
                    commit("getEvents", events.data.events);
                })
                .catch((err) => {
                    console.log(err);
                });
        }
    },
    getters: {},
    mutations: {
        getEvents(state, payload) {
            state.events = payload;
        },
    },
};