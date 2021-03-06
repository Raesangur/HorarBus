import Vue from "vue";
import Vuex from "vuex";

import user from "@/store/user.module";
import calendar from "@/store/calendar.module";
import maps from "@/store/maps.module";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {},
  mutations: {},
  actions: {},
  modules: {
    user,
    calendar,
    maps,
  },
});
