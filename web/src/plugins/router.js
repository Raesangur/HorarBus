import Vue from "vue";
import Router from "vue-router";
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

if (process.env.NODE_ENV === "development") {
	router.beforeEach((to, from, next) => {
		if (to.fullPath.startsWith("/api")) {
			window.location.href = `http://${window.location.hostname}${to.fullPath}`;
		} else {
			next();
		}
	});
}

export { router };
