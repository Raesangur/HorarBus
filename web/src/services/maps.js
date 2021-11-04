import Api from "@/services/api";

const basic = "maps";
export default {
  getDefault(param) {
    return Api.default().get("/api" + basic + "/", param);
  },
};
