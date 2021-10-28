import axios from "axios";

axios.interceptors.request.use(
  (config) => {
    if (process.env.NODE_ENV === "development") {
      if (config.url.startsWith("/api")) {
        config.url = `http://${window.location.hostname}${config.url}`;
      }
    }

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
