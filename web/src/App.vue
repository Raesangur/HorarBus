<template>
  <div id="app">
    <router-view />
    <footer>
      © The Council - Tous droits réservés 2021 / Mise à jour 2021-10-21
    </footer>
  </div>
</template>

<script>
import { mapActions } from "vuex";export default {
  data: () => ({}),
  methods: {
    ...mapActions(["getUser","getEvents"]),

  },
  mounted(){
    this.getUser();
    this.getEvents();
  }
};
</script>
<style>
body{
  padding: 0 !important;
}
footer {
  position: relative;
  margin-top: 20px;
  left: 50%;
  transform: translate(-50%, -50%) !important;
  color: #000000;
}
.navbar-dark .navbar-nav .nav-link:hover {
  background: transparent;
}

.nav-item:hover {
  background-color: transparent;
}
.nom {
  color: #ffffff !important;
  display: inline-block;
  font-size: 2.1rem;
  padding: 0;
  opacity: 1 !important;
}
.drop {
  display: flex;
  justify-content: flex-end;
  width: 186.2px;
}
.dropdown-menu {
  background-color: #00b7ff !important;
  color: #ffffff !important;
}
.dropdown-item {
  color: #ffffff !important;
}
.dropdown-item:hover,
.dropdown-item:focus {
  background-color: #5e5e5e !important;
}
.date {
  font-weight: bold;
  white-space: nowrap;
  width: 232px;
}
.date:hover {
  text-decoration: none;
  color: #ffffff !important;
}
.horbus {
  display: flex;
  justify-content: center;
  width: 186.2px;
}
.nom:hover {
  text-decoration: none;
  background-color: transparent;
}
.navbar-nav {
  flex-direction: row;
  display: flex;
  align-items: center;
}
.navbar-dark .navbar-nav .nav-link {
  color: #ffffff !important;
  display: inline-block;
  font-size: 2.1rem;
  padding: 0;
  opacity: 1 !important;
}
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  background-color: #ffffff;
}
.dark {
  background-color: #222222 !important;
  color: #ffffff !important;
}
#nav {
  padding: 30px;
}
nav ul li {
  width: 100%;
}
#nav a {
  font-weight: bold;
  color: #2c3e50;
}

#nav a.router-link-exact-active {
  color: #42b983;
}
.navbar {
  background: rgb(0, 183, 255);
  flex-wrap: nowrap !important;
}
.caret {
  border: none;
  background: transparent;
  color: #ffffff;
}
.navbar-menu {
  height: fit-content;
}
nav {
  height: 56px !important;
  line-height: 56px !important;
}
.zoneClose {
  vertical-align: middle;
  margin-top: 5px;
}
.close {
  vertical-align: middle;
  font-size: 30px !important;
  color: #ffffff;
  border: none;
  background: transparent;
  text-align: center;
}

.title {
  font-size: 25px;
  color: #ffffff;
  font-weight: bold;
}
.modal .modal-content {
  padding: 0 !important;
  border-radius: 15px;
}
.sectionPref {
  font-size: 13px;
  font-weight: bold;
}
</style>
