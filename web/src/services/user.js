import Api from "@/services/api";

const basic = "user";
export default {
  getUser() {
    return Api.default().get("/api/" + basic + "/");
  },
  putUser(params) {
    return Api.default().put("/api/" + basic + "/", params);
  },
};
