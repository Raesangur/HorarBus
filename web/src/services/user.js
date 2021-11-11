import Api from "@/services/api";

const basic = "user";
export default {
    getPref(param) {
        return Api.default().get("/api" + basic + "/pref", param);
    },
    putPref(param) {
        return Api.default().put("/api" + basic + "/pref", param);
    },
    getUser() {
        return Api.default().get("/" + basic + "/");
    },
    putUser(params) {
        return Api.default().put("/" + basic + "/", params);
    },
};