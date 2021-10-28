<template>
  <b-row style="margin-top: 0">
    <b-col style="padding: 0">
      <b-navbar toggleable="md" sticky type="dark" class="navbar">
        <b-navbar-nav class="navbar-menu mx-auto dateZone">
          <button class="caret" @click="prev()">
            <b-icon-caret-left-fill></b-icon-caret-left-fill>
          </button>
          <a class="date" v-if="$refs.calendar" @click="showChoseDate()">
            {{ this.$refs.calendar.title }}
          </a>
          <a class="date" v-else @click="showChoseDate()"> {{ today }} </a>
          <button class="caret" @click="next()">
            <b-icon-caret-right-fill></b-icon-caret-right-fill>
          </button>
        </b-navbar-nav>
        <b-navbar-nav class="navbar-menu mx-auto" v-if="largeur >= 768">
          <div class="nom" style="cursor: default">HorarBus</div>
        </b-navbar-nav>
        <b-navbar-toggle
          class="toggle"
          v-b-toggle.sidebar-menu
          target="#"
        ></b-navbar-toggle>
        <b-navbar-nav
          class="navbar-menu mx-auto"
          style="margin-right: 0 !important"
          v-if="largeur >= 768"
        >
          <b-nav-item-dropdown right class="drop" no-caret>
            <!-- Using 'button-content' slot -->
            <template v-slot:button-content>
              <a class="nom"> John Doe </a>
            </template>
            <!--<b-dropdown-item href="/profile"><font-awesome-icon :icon="shoppingCog" /> Profile</b-dropdown-item>-->
            <b-dropdown-item
              @click="
                showPref();
                toggle;
              "
            >
              Mes paramètres
            </b-dropdown-item>
            <b-dropdown-item @click="logout">Se déconnecter</b-dropdown-item>
          </b-nav-item-dropdown>
        </b-navbar-nav>
        <b-sidebar
          id="sidebar-menu"
          right
          backdrop
          bg-variant="dark"
          text-variant="light"
          shadow="dark"
        >
          <div style="padding: 35px; color: #ffffff">
            <b-row>
              <b-col
                class="sideItem"
                @click="showPref()"
                v-b-toggle.sidebar-menu
              >
                Mes paramètres
              </b-col>
            </b-row>
            <b-row>
              <b-col class="sideItem" v-b-toggle.sidebar-menu>
                Se déconnecter
              </b-col>
            </b-row>
          </div>
        </b-sidebar>
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
        <b-row>
          <b-col cols="12" class="sectionPref">
            Dark Mode
            <b-form-checkbox
              v-model="darkMode"
              name="check-button"
              switch
              size="lg"
            ></b-form-checkbox>
          </b-col>
        </b-row>
      </b-modal>
      <b-modal
        ref="chooseDate"
        hide-footer
        hide-header
        body-class="modal-calendar"
        dialog-class="modal-left"
      >
        <b-row>
          <b-col cols="10" class="title"> {{ value }} </b-col>
          <b-col cols="2" class="zoneClose">
            <button @click="hideChoseDate()" class="close">x</button>
          </b-col>
        </b-row>
        <b-row>
          <b-col>
            <b-row>
              <b-col>
                <b-calendar
                  v-model="value"
                  locale="fr"
                  :hide-header="true"
                  label-help=""
                  block
                  selected-variant="primary"
                  today-variant="dark"
                  nav-button-variant="primary"
                >
                </b-calendar>
              </b-col>
            </b-row>
            <b-row>
              <b-col>
                <b-button class="today" @click="setToday"> Set Today </b-button>
              </b-col>
            </b-row>
          </b-col>
        </b-row>
      </b-modal>
      <v-calendar
        ref="calendar"
        :events="events"
        :dark="darkMode"
        v-model="value"
        color="black"
        locale="fr"
        event-color="#1867c0"
        @click:event="showEvent"
        :type="type"
        class="calendar"
        v-if="largeur >= 768"
      >
        <template v-slot:day-body="{ date, week }">
          <div
            class="v-current-time"
            :class="{ first: date === week[0].date }"
            :style="{ top: nowY }"
          ></div>
        </template>
      </v-calendar>
      <div class="agenda" v-else>
        <table style="background:#ffffff">
        <tbody v-for="(day, index) in days" :key="index">
          <tr>
            <td class="day">
              {{
                day
              }}
            </td>
          </tr>
          <tr v-for="(event,i) in events" :key="i">
            <td class="tdEvent">
              <b-row class="tdEvent">
                <b-col cols="3" style="text-align:right;padding:12px 0px">
                  {{event.heure}}
                  
                  <br>
                  
                  <div class="local">
                    {{event.local}}
                    
                  </div>
                </b-col>
                <b-col cols="1" >
                  <span class="dot" style="background-color:blueviolet"></span>
                </b-col>
                <b-col cols="8" style="text-align:left">
                  
                  <a style="font-size: larger;">
                    <!-- Projet -->
                    {{event.name}}
                  </a>
                  <p style="margin-top: 15px; margin-bottom:0">
                    {{event.description1}}
                    <!-- Conception d'un système informatique distribué -->
                    <br>
                    <br>
                    {{event.description2}}
                    <!-- Port du masque de procédure obligatoire -->
                  </p>
                </b-col>
              </b-row>
            </td>
          </tr>
        </tbody>
      </table>
      </div>
    </b-col>
  </b-row>
</template>

<script>
import { mapState } from "vuex";
export default {
  data: () => ({
    today: new Date(),
    value: new Date(),
    type: "week",
    ready: false,
    largeur: 0,
    darkMode: true,
    colors: [
      "blue",
      "indigo",
      "deep-purple",
      "cyan",
      "green",
      "orange",
      "grey darken-1",
    ],
    events: [
      {
        start: "2021-10-27 14:00",
        end: "2021-10-27 16:00",
        name: "Projet,Conception d'un système informatique distribué,Port du masque de Procédure obligatoire".split(",")[0],
        description1: "Projet,Conception d'un système informatique distribué,Port du masque de Procédure obligatoire".split(",")[1],
        description2: "Projet,Conception d'un système informatique distribué,Port du masque de Procédure obligatoire".split(",")[2],
        heure: "2021-10-27 14:00".split(" ")[1]+" - "+"2021-10-27 16:00".split(" ")[1],
        local: "C1-5006",
      },
      {
        name: `Thomas' Birthday`,
        start: "2021-10-11",
      },
      {
        name: "Mash Potatoes",
        start: "2021-10-14 12:30",
        end: "2021-10-14 15:30",
      },
      {
        name: "poutine",
        start: "2021-10-13 12:30",
        end: "2021-10-13 15:30",
      },
    ],
    days: [
      "Dimanche",
      "Lundi",
      "Mardi",
      "Mercredi",
      "Jeudi",
      "Vendredi",
      "Samedi",
    ],
  }),

  mounted() {
    this.resize();
    this.getToday();
    this.$refs.calendar.checkChange();
    this.ready = true;
    this.scrollToTime();
    this.updateTime();
  },

  computed: {
    cal() {
      return this.ready ? this.$refs.calendar : null;
    },
    nowY() {
      return this.cal ? this.cal.timeToY(this.cal.times.now) + "px" : "-10px";
    },
    ...mapState({
      dateSelect: (state) => state.index.dateSelect,
    }),
  },
  methods: {
    onResize() {
      this.resize();
    },
    resize() {
      this.largeur = window.innerWidth;
      if (this.largeur >= 768) {
        this.type = "week";
      } else {
        this.type = "custom-weekly";
      }
    },
    showPref() {
      this.$refs["pref"].show();
    },
    hidePref() {
      this.$refs["pref"].hide();
    },
    showChoseDate() {
      this.$refs["chooseDate"].show();
    },
    hideChoseDate() {
      this.$refs["chooseDate"].hide();
    },
    getCurrentTime() {
      return this.cal
        ? this.cal.times.now.hour * 60 + this.cal.times.now.minute
        : 0;
    },
    scrollToTime() {
      const time = this.getCurrentTime();
      const first = Math.max(0, time - (time % 30) - 30);

      this.cal.scrollToTime(first);
    },
    updateTime() {
      setInterval(() => this.cal.updateTimes(), 60 * 1000);
    },

    getEventColor(event) {
      return event.color;
    },
    next() {
      this.$refs.calendar.next();
    },
    prev() {
      this.$refs.calendar.prev();
    },
    showEvent() {},
    setToday() {
      const now = new Date();
      let val = now.toISOString().split("T");
      this.value = val[0];
    },
    getToday() {
      let now = new Date();
      let month = now.getMonth() + 1;
      switch (month) {
        case 1:
          month = "Janvier ";
          break;
        case 2:
          month = "Février ";
          break;
        case 3:
          month = "Mars ";
          break;
        case 4:
          month = "Avril ";
          break;
        case 5:
          month = "Mai ";
          break;
        case 6:
          month = "Juin ";
          break;
        case 7:
          month = "Juillet ";
          break;
        case 8:
          month = "Aout ";
          break;
        case 9:
          month = "Septembre ";
          break;
        case 10:
          month = "Octobre ";
          break;
        case 11:
          month = "Novembre ";
          break;
        case 12:
          month = "Décembre ";
          break;
        default:
          break;
      }
      let year = now.getFullYear();
      let val = now.toISOString().split("T");
      this.value = val[0];
      this.today = month + year;
    },
    logout() {
      this.$keycloak.logout();
    },
  },
  created() {
    window.addEventListener("resize", this.onResize);
  },
  destroyed() {
    window.removeEventListener("resize", this.onResize);
  },
};
</script>

<style scoped>
.my-event {
  white-space: nowrap;
  border-radius: 2px;
  background-color: #1867c0;
  color: #ffffff;
  border: 1px solid #1867c0;
  font-size: 12px;
  padding: 3px;
  cursor: pointer;
  margin-bottom: 1px;
  left: 4px;
  margin-right: 8px;
  position: absolute;
  overflow: unset;
}
/deep/.v-calendar-daily__scroll-area {
  overflow: unset;
}
.my-event.with-time {
  position: absolute;
  right: 4px;
  margin-right: 0px;
}
.v-current-time {
  height: 2px;
  background-color: #ff0000;
  position: absolute;
  left: 0px;
  right: 0px;
  pointer-events: none;
}
/deep/.v-btn:not(.v-btn--outlined).primary {
  color: #000000;
}
.first::before {
  content: "";
  position: absolute;
  background-color: #ea4335;
  width: 12px;
  height: 12px;
  left: 0;
  border-radius: 50%;
  margin-top: -5px;
  margin-left: -6.5px;
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
/deep/.dropdown-menu {
  background-color: #1867c0 !important;
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
  background: #1867c0;
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
/deep/.preference {
  background: #222222;
  border-radius: 11px;
  color: #ffffff;
}
/deep/.modal-calendar {
  background: #222222;
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
/deep/.theme--dark.v-calendar-events .v-event-timed {
  color: #ffffff !important;
  font-size: 15px;
  font-weight: bolder;
  border: 0 !important;
}
/deep/.v-calendar .v-event {
  color: #ffffff !important;
  font-size: 14px;
  font-weight: bolder;
  border: 0 !important;
}
.today {
  border-radius: 5px;
  width: 100%;
  color: #ffffff;
  font-size: 18px;
  font-weight: bolder;
  background: transparent;
  border: 1px solid #ffffff;
}
.today:hover {
  border-radius: 5px;
  width: 100%;
  color: #000000;
  background: #ffffff;
  border: none;
}
/deep/.btn-outline-primary {
  color: #ffffff !important;
  border-color: none !important;
  background: #007bff;
}
/deep/.btn-outline-primary:hover {
  color: #ffffff !important;
  border-color: none !important;
  background: #007bff;
  opacity: 0.9;
}
/deep/.btn-outline-dark {
  color: #ffffff !important;
  border-color: none !important;
  background: #343a40;
}
/deep/.btn-outline-dark:hover {
  color: #ffffff !important;
  border-color: none !important;
  background: #0062cc;
}
/deep/.btn-outline-light {
  color: #f8f9fa;
  background-color: #f8f9fa;
}
/deep/.btn-outline-light:hover {
  color: #ffffff !important;
  border-color: none !important;
  background: #0062cc;
}
/deep/.btn-light {
  color: #ffffff !important;
  border-color: none !important;
  background: #007bff !important;
}
/deep/.theme--dark.v-calendar-daily {
  background-color: #222222 !important;
}
/deep/.modal-left {
  margin-left: 0px;
}
/deep/.b-calendar-grid {
  background: none;
  border: none;
}
/deep/.form-control {
  color: #ffffff;
  background: #222222;
}
/deep/.form-control:focus {
  color: #ffffff;
  background: #222222;
}
/deep/.b-calendar .b-calendar-nav .btn {
  margin: 0 1px;
  padding: 0;
}
.sectionPref > .custom-switch > label {
  color: #ffffff;
  font-weight: bold;
  font-size: 13px;
}
/deep/.black {
  background-color: #0062cc !important;
  color: white !important;
}
.dateZone {
  width: 270px !important;
  margin-left: 0px !important;
  position: unset;
}
/deep/.pl-1{
  text-align: left;
}
/deep/.v-calendar{
  margin: 0;
}
@media (max-width: 992px) {
  /deep/.modal {
    width: 100%;
  }
}
@media (max-width: 768px) {
  .sideItem {
    font-size: 20px;
    font-weight: bold;
    background: transparent;
  }
  .sideItem:hover {
    background: #1867c0;
    cursor: pointer;
  }
  .calendar {
    height: calc(100%-56px);
    position: unset;
  }
  .toggle {
    border: none;
  }
  /deep/.navbar-dark .navbar-toggler-icon {
    background-image: url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' width='30' height='30' viewBox='0 0 30 30'%3e%3cpath stroke='rgba%28255, 255, 255, 1%29' stroke-linecap='round' stroke-miterlimit='10' stroke-width='2' d='M4 7h22M4 15h22M4 23h22'/%3e%3c/svg%3e");
  }
  .dateZone {
    position: relative;
    left: 50%;
    transform: translateX(-50%);
  }
  .agenda{
    min-height: 100vh !important;
    background: #222222;
  }
  .day{
    text-align: left;
    padding: 8px 14px;
    font-size: 30px;
    text-transform: uppercase;
    font-weight: bolder;
    background: #EEEEEE;
    color:#000000;
  }
  .tdEvent{
    padding: 0px !important;
    min-height: 133px;
    margin: 0px !important;
  }
  .local{
    font-weight: bold;
    text-align: right;
    text-transform: uppercase;

  }
  .dot{
    display: inline-block;
    width: 10px;
    height: 10px;
    border-radius: 5px;
  }
}
</style>
