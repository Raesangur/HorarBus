<template>
  <div>
    <GmapMap
      :center='center'
      :zoom='zoom'
      style='width:100%;  height: 400px;'
    />
  </div>
</template>
<script>
import { mapActions, mapState } from "vuex";
export default {
  computed: {
    ...mapState({
      mapsSettings: (state) => state.maps.mapsSettings,
    }),
  },
  mounted() {
    this.getMaps();
    this.geolocate();
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
    }
  },
  methods: {
    ...mapActions(["getMaps"]),
    setPlace(place) {
      this.currentPlace = place;
    },
    geolocate() {
      navigator.geolocation.getCurrentPosition(position => {
      console.log(position)
        this.center = {
          lat: position.coords.latitude,
          lng: position.coords.longitude,
        };
      });
    },
  },
};
</script>
