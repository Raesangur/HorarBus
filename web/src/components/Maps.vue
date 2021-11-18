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
<<<<<<< Updated upstream
    this.getMaps();
    this.initMap();
=======
    this.center = {
      lat: this.geolocation.lat,
      lng: this.geolocation.lng,
    };
    if(this.mapsSettings.latitude){
      this.center.lat = parseInt(this.mapsSettings.latitude);
    }
    if(this.mapsSettings.longitude){
      this.center.longitude = parseInt(this.mapsSettings.longitude);
    }
    if(this.mapsSettings.zoom){
      this.zoom = parseInt(this.mapsSettings.zoom);
    }
  },
  data() {
    return {
      center: { lat: 0, lng: 0 },
      currentPlace: null,
      zoom: 15,
    }
>>>>>>> Stashed changes
  },
};
</script>
