import axios from "axios";

if (process.env.NODE_ENV === "development") {
}

console.log(backUrl);

export default {
  default() {
    const instance = axios.create({
      baseURL: backUrl,
      /*headers: {
              Accept: "application/json",
              "Content-Type": "application/json",
            },*/
    });

    instance.interceptors.request.use(
      (config) => {
        const token = "bearer " + localStorage.getItem("vue-token");
        if (token) {
          config.headers["Authorization"] = token;
        }
        //console.log(config)
        return config;
      },
      (error) => {
        return Promise.reject(error);
      }
    );

    return instance;
  },
};
