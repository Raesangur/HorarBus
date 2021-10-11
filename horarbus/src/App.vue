<template>
  <div id="app">
    <b-navbar toggleable="lg" sticky type="dark" class="navbar">
      <b-navbar-nav
        class="navbar-menu mx-auto"
        style="margin-left: 0 !important"
      >
        <button class="caret" @click="prev()">
          <b-icon-caret-left-fill></b-icon-caret-left-fill>
        </button>
        <a class="date">
          {{ today }}
        </a>
        <button class="caret" @click="next()">
          <b-icon-caret-right-fill></b-icon-caret-right-fill>
        </button>
      </b-navbar-nav>
      <b-navbar-nav class="navbar-menu mx-auto">
        <div class="nom" style="cursor: default">HorarBus</div>
      </b-navbar-nav>
      <b-navbar-nav
        class="navbar-menu mx-auto"
        style="margin-right: 0 !important"
      >
        <b-nav-item-dropdown right class="drop" no-caret>
          <!-- Using 'button-content' slot -->
          <template v-slot:button-content>
            <a class="nom"> John Doe </a>
          </template>
          <!--<b-dropdown-item href="/profile"><font-awesome-icon :icon="shoppingCog" /> Profile</b-dropdown-item>-->
          <b-dropdown-item @click="showPref()">
            Mes paramètres
          </b-dropdown-item>
          <b-dropdown-item
            >Voir tous les groupes</b-dropdown-item
          >
          <b-dropdown-item
            >Se déconnecter</b-dropdown-item
          >
        </b-nav-item-dropdown>
      </b-navbar-nav>
    </b-navbar>
    <b-modal
      ref="pref"
      hide-footer
      hide-header
      :centered="true"
      body-class="preference"
    >
      <b-row>
        <b-col cols="10" class="title"> Mes préférences </b-col>
        <b-col cols="2" class="zoneClose">
          <button @click="hidePref()" class="close">x</button>
        </b-col>
      </b-row>
      <b-row>
        <b-col cols="12" class="sectionPref"> Mode de transport </b-col>
      </b-row>
      <b-row>
        <b-col cols="12" class="sectionPref"> Notifications </b-col>
      </b-row>
      <b-row>
        <b-col cols="12" class="sectionPref"> Avance minimum </b-col>
      </b-row>
      <b-row>
        <b-col cols="12"> </b-col>
      </b-row>
      <b-row>
        <b-col cols="12" class="sectionPref"> Domicile </b-col>
      </b-row>
    </b-modal>
    <router-view />
    <footer>
      © The Council - Tous droits réservés 2021 / Mise à jour 2021-09-10
    </footer>
  </div>
</template>

<script>
import horaire2Vue from './views/Horaire2.vue';
export default {
  data: () => ({
    today: "",
  }),
  methods: {
    showPref() {
      this.$refs["pref"].show();
    },
    hidePref() {
      this.$refs["pref"].hide();
    },
    next(){
      horaire2Vue.next()
    },
    prev(){
      horaire2Vue.prev()
    },
  },
  mounted() {
    let today = new Date();
    let month = today.getMonth()+1;
    switch (month) {
      case 1:
        month = "Janvier "
        break;
      case 2:
        month = "Février "
        break;
        case 3:
        month = "Mars "
        break;
        case 4:
        month = "Avril "
        break;
        case 5:
        month = "Mai "
        break;
        case 6:
        month = "Juin "
        break;
        case 7:
        month = "Juillet "
        break;
        case 8:
        month = "Aout "
        break;
        case 9:
        month = "Septembre "
        break;
        case 10:
        month = "Octobre "
        break;
        case 11:
        month = "Novembre "
        break;
        case 12:
        month = "Décembre "
        break;
      default:
        break;
    }
    let year = today.getFullYear();
    this.today = month + year;
  },
};
</script>
<style>
footer {
  position: relative;
  margin-top: 20px;
  left: 50%;
  transform: translate(-50%, -50%) !important;
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
.horarbus {
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
.preference {
  background: #da8300;
  border-radius: 11px;
  color: #ffffff;
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
