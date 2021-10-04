import Vue from "vue";
import Router from "vue-router";
import Horaire from "../views/Horaire.vue";

Vue.use(Router);

export const router = new Router({
  mode: "history",
  base: process.env.BASE_URL,
  routes: [
    {
      path: "/",
      name: "horaire",
      component: Horaire,
    },
  ],
});

export default router;
