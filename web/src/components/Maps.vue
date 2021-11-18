<template>
  <div id="map"></div>
</template>
<script>
import { mapActions, mapState } from "vuex";
export default {
  data: () => ({}),
  computed: {
    ...mapState({
      mapsSettings: (state) => state.maps.mapsSettings,
    }),
  },
  methods: {
    ...mapActions(["getMaps"]),
    // initMap() {
    //    let mapElement =  document.getElementById("map")
    //     let initialized = false;
    //   directionsService = new google.maps.DirectionsService();
    //   directionsRenderer = new google.maps.DirectionsRenderer();

    //   defaultLatPromise = fetchMapsSettings("/api/maps/defaultLat");
    //   defaultLngPromise = fetchMapsSettings("/api/maps/defaultLng");
    //   defaultZoomPromise = fetchMapsSettings("/api/maps/defaultZoom");
    //   Promise.all([
    //     defaultLatPromise,
    //     defaultLngPromise,
    //     defaultZoomPromise,
    //   ]).then(([defaultLat, defaultLng, defaultZoom]) => {
    //     let mapOptions = {
    //       zoom: parseFloat(defaultZoom),
    //       center: { lat: parseFloat(defaultLat), lng: parseFloat(defaultLng) },
    //     };
    //     map = new google.maps.Map(mapElement, mapOptions);
    //     directionsRenderer.setMap(map);

    //     initialized = "true";
    //   });
    // },
  },
  mounted() {
    this.getMaps();
    this.initMap();
  },
};
</script>
