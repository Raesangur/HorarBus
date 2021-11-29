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
                    console.log(events.data)
                    for (let i in events.data.events) {
                        events.data.events[i].open = false;
                    }
                    commit("getEvents", events.data.events);
                })
                .catch((err) => {
                    console.log(err);
                });
        },
    },
    getters: {},
    mutations: {
        getEvents(state, payload) {
            state.events = payload;
        },
    },
};