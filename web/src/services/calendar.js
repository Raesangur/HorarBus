import Api from "@/services/api";

const basic = "calendar";
export default {
    getEvents() {
        return Api.default().get("/api/" + basic + "/");
    },
    putEvents() {
        return Api.default().put("/api/" + basic + "/");
    },
    postEvents() {
        return Api.default().post("/api/" + basic + "/");
    },
};