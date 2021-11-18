import Vue from "vue";
import { i18n } from "./plugins/i18n";
import App from "./App.vue";
import { router } from "./plugins/router";
import store from "./store";
import Keycloak from "keycloak-js";
import vuetify from "@/plugins/vuetify";
import * as VueGoogleMaps from "vue2-google-maps"
import { BootstrapVue, IconsPlugin } from "bootstrap-vue";
import Vue2TouchEvents from 'vue2-touch-events'


import "bootstrap/dist/css/bootstrap.css";
import "bootstrap-vue/dist/bootstrap-vue.css";
import "./assets/style/horaire.css";
import "./assets/style/fuckup2.css";
import "./assets/style/fuckup.css";

Vue.use(BootstrapVue);
// Optionally install the BootstrapVue icon components plugin
Vue.use(IconsPlugin);
Vue.use(Vue2TouchEvents)
Vue.config.productionTip = false;
Vue.config.devtools = true;
Vue.use(VueGoogleMaps, {
    load: {
        key: process.env.VUE_APP_API_KEY,
        libraries: 'places',
    }
});
let initOptions = {
    url: `https://${window.location.hostname}/auth`,
    realmPublicKey: "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAr5vDexhhB+UG5rfzPMKpO8LFgYSnmRvqDoUio/hIo6G9AbZC6UMc9jVB1s10NNFNNbiEl/hvqWE3oMLMvgdZBkeauEP1H/toB2CQkSO+syMLMHkDKYpXoP7Kyfu3/nxgHhBolbdVnORtUSWBxoku4kmm3dOUGOi8dT8O4UiPPCvtee1KzJwdDL/pwKVbPpjP+K3dB6kFjnRnTABJLQu29olnv6zf/9E1NVDmHDwLXIiN7BjAjaYFfkfiJLjDbGD0jGQNWGFFOkYMlprpqsBaf1WzyPY4PEHzc7W6WkR/u5ODCUpBNPoUdK3iSkgT0dMdjOOvvY/YoAROoM5KTdvsHQIDAQAB",
    realm: "horarbus",
    clientId: "frontend",
    onLoad: "login-required",
};
let keycloak = Keycloak(initOptions);
/////////////////without keycloak
// new Vue({
//     router,
//     store,
//     i18n,
//     vuetify,
//     render: (h) => h(App),
// }).$mount("#app");

keycloak
    .init({ onLoad: initOptions.onLoad, checkLoginIframe: false })
    .then((auth) => {
        console.log(auth);
        if (!auth) {
            window.location.reload();
        } else {
            console.info("Authenticated");
            Vue.prototype.$keycloak = keycloak;
            new Vue({
                router,
                store,
                vuetify,
                i18n,
                render: (h) => h(App),
            }).$mount("#app");
        }

        localStorage.setItem("vue-token", keycloak.token);
        localStorage.setItem("vue-refresh-token", keycloak.refreshToken);

        setInterval(() => {
            //console.log('here')
            keycloak
                .updateToken(0)
                .then((refreshed) => {
                    //console.log(refreshed)
                    if (refreshed) {
                        //console.info("Token refreshed" + refreshed);
                        localStorage.setItem("vue-token", keycloak.token);
                        localStorage.setItem("vue-refresh-token", keycloak.refreshToken);
                    } else {
                        // console.info(
                        //     "Token not refreshed, valid for " +
                        //     Math.round(
                        //         keycloak.tokenParsed.exp +
                        //         keycloak.timeSkew -
                        //         new Date().getTime() / 1000
                        //     ) +
                        //     " seconds"
                        // );
                    }
                })
                .catch(() => {
                    console.error("Failed to refresh token");
                });
        }, 10000);
    })
    .catch(() => {
        console.error("Authenticated Failed");
    });
Vue.directive("click-outside", {
    bind: function(el, binding, vnode) {
        el.clickOutsideEvent = function(event) {
            // here I check that click was outside the el and his children
            if (!(el == event.target || el.contains(event.target))) {
                // and if it did, call method provided in attribute value
                vnode.context[binding.expression](event);
            }
        };
    },
    unbind: function(el) {
        document.body.removeEventListener("click", el.clickOutsideEvent);
    },
});