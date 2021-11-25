<template>
  <div>
    <GmapMap :center="center" :zoom="zoom" style="width: 100%; height: 80vh" />
  </div>
</template>
<script>
import { mapState } from "vuex";
export default {
  computed: {
    ...mapState({
      mapsSettings: (state) => state.maps.mapsSettings,
      geolocation: (state) => state.maps.geolocation,
    }),
  },
  watch: {},
  mounted() {
    this.center = {
      lat: this.geolocation.lat,
      lng: this.geolocation.lng,
    };
    // if(this.mapsSettings.latitude){
    //   this.center.lat = parseInt(this.mapsSettings.latitude);
    // }
    // if(this.mapsSettings.longitude){
    //   this.center.longitude = parseInt(this.mapsSettings.longitude);
    // }
    // if(this.mapsSettings.zoom){
    //   this.zoom = parseInt(this.mapsSettings.zoom);
    // }
  },
  data() {
    return {
      center: { lat: 0, lng: 0 },
      currentPlace: null,
      zoom: 15,
    };
  },
};
</script>
