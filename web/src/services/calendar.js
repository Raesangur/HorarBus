import Api from "@/services/api";

const basic = "calendar";
export default {
  getevents(param) {
    return Api.default().get("/api" + basic + "/", param);
  },
};
