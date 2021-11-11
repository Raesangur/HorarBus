const applyMiddleware = (currentRoute) => {
    const routesToRedirect = ["/api", "/auth"];

    if (process.env.NODE_ENV === "development") {
        if (routesToRedirect.some((route) => currentRoute.startsWith(route))) {
            return {
                routeHasChanged: true,
                route: `${window.location.protocol}//${window.location.hostname}${currentRoute}`,
            };
        }
    }

    return { routeHasChanged: false, route: currentRoute };
};

export { applyMiddleware };