import Vue from "vue";
import { i18n } from "./plugins/i18n";
import App from "./App.vue";
import { router } from "./plugins/router";
import store from "./store";
//import Keycloak from "keycloak-js";
import vuetify from '@/plugins/vuetify'
import { BootstrapVue, IconsPlugin } from "bootstrap-vue";

import "bootstrap/dist/css/bootstrap.css";
import "bootstrap-vue/dist/bootstrap-vue.css";
import "./assets/style/horaire.css";
import "./assets/style/fuckup2.css";
import "./assets/style/fuckup.css";

Vue.use(BootstrapVue);
// Optionally install the BootstrapVue icon components plugin
Vue.use(IconsPlugin);
Vue.config.productionTip = false;
Vue.config.devtools = true;

// let initOptions = {
//     url: "http://localhost/auth",
//     realmPublicKey: "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAr5vDexhhB+UG5rfzPMKpO8LFgYSnmRvqDoUio/hIo6G9AbZC6UMc9jVB1s10NNFNNbiEl/hvqWE3oMLMvgdZBkeauEP1H/toB2CQkSO+syMLMHkDKYpXoP7Kyfu3/nxgHhBolbdVnORtUSWBxoku4kmm3dOUGOi8dT8O4UiPPCvtee1KzJwdDL/pwKVbPpjP+K3dB6kFjnRnTABJLQu29olnv6zf/9E1NVDmHDwLXIiN7BjAjaYFfkfiJLjDbGD0jGQNWGFFOkYMlprpqsBaf1WzyPY4PEHzc7W6WkR/u5ODCUpBNPoUdK3iSkgT0dMdjOOvvY/YoAROoM5KTdvsHQIDAQAB",
//     realm: "usager",
//     clientId: "frontend",
//     onLoad: "login-required",
// };

//let keycloak = Keycloak(initOptions);
/////////////////without keycloak

new Vue({
    router,
    store,
    i18n,
    vuetify,
    render: (h) => h(App),
}).$mount("#app");

// keycloak
//     .init({ onLoad: initOptions.onLoad, checkLoginIframe: false })
//     .success((auth) => {
//         console.log(auth);
//         if (!auth) {
//             window.location.reload();
//         } else {
//             console.info("Authenticated");
//             Vue.prototype.$keycloak = keycloak;
//             new Vue({
//                 router,
//                 store,
//                 i18n,
//                 render: (h) => h(App),
//             }).$mount("#app");
//         }

//         localStorage.setItem("vue-token", keycloak.token);
//         localStorage.setItem("vue-refresh-token", keycloak.refreshToken);

//         setInterval(() => {
//             //console.log('here')
//             keycloak
//                 .updateToken(0)
//                 .success((refreshed) => {
//                     //console.log(refreshed)
//                     if (refreshed) {
//                         console.debug("Token refreshed" + refreshed);
//                         localStorage.setItem("vue-token", keycloak.token);
//                         localStorage.setItem("vue-refresh-token", keycloak.refreshToken);
//                     } else {
//                         console.warn(
//                             "Token not refreshed, valid for " +
//                             Math.round(
//                                 keycloak.tokenParsed.exp +
//                                 keycloak.timeSkew -
//                                 new Date().getTime() / 1000
//                             ) +
//                             " seconds"
//                         );
//                     }
//                 })
//                 .error(() => {
//                     console.error("Failed to refresh token");
//                 });
//         }, 10000);
//     })
//     .error(() => {
//         console.error("Authenticated Failed");
//     });