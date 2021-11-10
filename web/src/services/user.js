import Api from "@/services/api";

const basic = "user";
export default {
    putPref(param) {
        return Api.default().put("/api/" + basic + "/pref", param);
    },
    getUser() {
        return Api.default().get("/api/" + basic + "/");
    },
    putUser(params) {
        return Api.default().put("/api/" + basic + "/", params);
    },
};