import axios from "axios";
import { applyMiddleware } from "./middleware";

export default {
  default() {
    const instance = axios.create({
      baseURL: "",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
    });

    instance.interceptors.request.use(
      (config) => {
        config.url = applyMiddleware(config.url).route;
        const token = "Bearer " + localStorage.getItem("vue-token");
        if (token) {
          config.headers["Authorization"] = token;
        }
        config.headers["Content-Type"] = "application/json";
        return config;
      },
      (error) => {
        return Promise.reject(error);
      }
    );

    return instance;
  },
};
