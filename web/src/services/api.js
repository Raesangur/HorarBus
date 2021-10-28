import axios from "axios";
import { applyMiddleware } from "./middleware";

axios.interceptors.request.use(
  (config) => {
    config.url = applyMiddleware(config.url).route;

    const token = "bearer " + localStorage.getItem("vue-token");
    if (token) {
      config.headers["Authorization"] = token;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export { axios };
