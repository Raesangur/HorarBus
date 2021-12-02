import CalendarService from "@/services/calendar";

export default {
    namespaced: false,
    state: {
        events: {},
        trajects: {},
        todayEvents: {},
    },
    actions: {
        getEvents({ commit }) {
            CalendarService.getEvents()
                .then((events) => {
                    for (let i in events.data.events) {
                        events.data.events[i].open = false;
                    }
                    for (let i in events.data.trajects) {
                        events.data.trajects[i].open = false;
                        events.data.trajects[i].traject.transport =
                            events.data.trajects[i].traject.transport.toUpperCase();

                    }
                    events.data.trajects = events.data.trajects.filter(trajects => trajects.end !== null && trajects.start !== null);
                    commit("getEvents", events.data.events);
                    commit("getTrajet", events.data.trajects);
                })
                .catch((err) => {
                    console.log(err);
                });
        },
        TodayEvents({ commit }) {
            CalendarService.getEvents()
                .then((events) => {
                    commit("getToday", events.data.trajects);
                })
                .catch((err) => {
                    console.log(err);
                });
        },
        async putEvents({ dispatch }, payload) {
            CalendarService.putEvents(payload)
                .then(() => {
                    dispatch("getEvents");
                })
                .catch((err) => {
                    console.log(err.response);
                });
        },
        async postEvents({ dispatch }, payload) {
            CalendarService.postEvents(payload)
                .then(() => {
                    dispatch("TodayEvents");
                })
                .catch((err) => {
                    console.log(err.response);
                });
        },
    },
    getters: {},
    mutations: {
        getEvents(state, payload) {
            state.events = payload;
        },
        getTrajet(state, payload) {
            state.trajects = payload;
        },
        getToday(state, payload) {
            state.todayEvents = payload;
        },
    },
};