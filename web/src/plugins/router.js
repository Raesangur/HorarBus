import Vue from "vue";
import Router from "vue-router";
import { applyMiddleware } from "../services/middleware";
import Horaire from "../views/Horaire.vue";

Vue.use(Router);

const router = new Router({
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

router.beforeEach((to, from, next) => {
  const { routeHasChanged, route } = applyMiddleware(to.fullPath);
  if (routeHasChanged) {
    window.location.href = route;
  } else {
    next();
  }
});

export { router };
