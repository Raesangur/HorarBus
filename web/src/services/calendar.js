import Api from "@/services/api";

const basic = "calendar";
export default {
    getEvents() {
        return Api.default().get("/api/" + basic + "/");
    },
};