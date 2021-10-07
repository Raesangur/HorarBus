import Vue from "vue";
import Router from "vue-router";
import Horaire from "../views/Horaire.vue";
import Horaire2 from "../views/Horaire2.vue";

Vue.use(Router);

export const router = new Router({
    mode: "history",
    base: process.env.BASE_URL,
    routes: [{
            path: "/",
            name: "horaire",
            component: Horaire,
        },
        {
            path: "/2",
            name: "horaire2",
            component: Horaire2,
        },
    ],
});

export default router;