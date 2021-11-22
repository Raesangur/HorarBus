import Api from "@/services/api";

const basic = "maps";
export default {
    getDefault() {
        return Api.default().get("/api/" + basic + "/");
    },
    getItinerary(params) {
        return Api.default().get("/api/" + "itinerary", params);
    },
    getPlace(params) {
        return Api.default().get("/api/" + "itinerary/place", params);
    },
};