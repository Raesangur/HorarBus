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
            <b-dropdown-item @click="showPref()">
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

        <!--Mode de transport-->
        <b-row>
          <b-col cols="12" class="sectionPref"> Mode de transport </b-col>
        </b-row>
        <b-form-group v-slot="{ ariaDescribedby }">
          <b-form-radio
            v-model="transport"
            :aria-describedby="ariaDescribedby"
            name="some-radios"
            value="walk"
            button
            class="buttonTransport"
            ><img :src="require('../assets/walk.png')"
          /></b-form-radio>
          <b-form-radio
            v-model="transport"
            :aria-describedby="ariaDescribedby"
            name="some-radios"
            value="bike"
            button
            class="buttonTransport"
            ><img :src="require('../assets/bike.png')"
          /></b-form-radio>
          <b-form-radio
            v-model="transport"
            :aria-describedby="ariaDescribedby"
            name="some-radios"
            value="bus"
            button
            class="buttonTransport"
            ><img :src="require('../assets/bus.png')"
          /></b-form-radio>
          <b-form-radio
            v-model="transport"
            :aria-describedby="ariaDescribedby"
            name="some-radios"
            value="car"
            button
            class="buttonTransport"
            ><img :src="require('../assets/car.png')"
          /></b-form-radio>
        </b-form-group>
          
        <!--Notification-->
        <b-row>
          <b-col cols="12" class="sectionPref"> Notifications </b-col>
        </b-row>
        
        <b-row>
            <b-form-checkbox v-model="notification_enable" name="check-button" switch class="checkboxNotification"> 
                Activer notification <b>(Checked: {{ notification_enable }})</b>     
            </b-form-checkbox>
        </b-row>

        <b-row>
            <input v-model.number="temps" type="number" class="tempsSelect">
            <p>Message is: {{ temps }}</p>
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
      >
        <template v-slot:event="{ event }">
          <div class="event" v-click-outside="dismissEvent">
            {{ event.heure }}
            <br />
            <a style="font-weight: 700">{{ event.name }}</a>
            <br />
            {{ event.local }}
            <div v-if="event.open">
              <div class="centerBorder"></div>
              {{ event.description1 }}
              <br />
              <br />
              {{ event.description2 }}
              <div class="centerBorder" v-if="event.prof"></div>
              {{ event.prof }}
              <div class="centerBorder" v-if="event.session"></div>
              {{ event.session }}
            </div>
          </div>
        </template>
        <template v-slot:day-body="{ date, week }">
          <div
            class="v-current-time"
            :class="{ first: date === week[0].date }"
            :style="{ top: nowY }"
          ></div>
        </template>
      </v-calendar>
      <div class="calendar-mobile">
        <Fullcalendar
          ref="fullCalendar"
          :options="calendarOptions"
          :events="events"
          @load="eventRender"
        />
      </div>
    </b-col>
  </b-row>
</template>

<script>
import EventModal from "../components/EventModal";
require("@fullcalendar/list/main.css");
import Fullcalendar from "@fullcalendar/vue";
import InteractionPlugin from "@fullcalendar/interaction";
import ListPlugin from "@fullcalendar/list";
import allLocales from "@fullcalendar/core/locales-all";

export default {
  components: {
    Fullcalendar,
  },

  data: () => ({
      transport: "bus",
      notification_enable: true,
    transportOption: [
      { text: "Walk", value: "walk" },
      { text: "Bike", value: "bike" },
      { text: "Bus", value: "bus" },
      { text: "Car", value: "car" },
    ],
    position: "",
    today: new Date(),
    value: new Date(),
    type: "week",

    calendarOptions: {
      plugins: [InteractionPlugin, ListPlugin],
      initialView: "listWeek",
      locales: allLocales,
      //eventClick: this.handleEventClick(),
      eventDidMount: function (arg) {
        let p = document.createElement("p");
        p.innerHTML =
          arg.event.extendedProps.description1 +
          "<br><br>" +
          arg.event.extendedProps.description2;
        p.setAttribute("style", "margin-top: 15px; margin-bottom:15px");
        let div = document.createElement("div");
        div.textContent = arg.event.extendedProps.local;
        div.setAttribute("class", "local");
        if (
          arg.event.extendedProps.description1 &&
          arg.event.extendedProps.description2
        ) {
          arg.el.cells[2].appendChild(p);
        }
        if (arg.event.extendedProps.local) {
          arg.el.cells[0].appendChild(div);
        }
      },
      locale: "fr",
      headerToolbar: false,
      events: [
        {
          start: "2021-11-01 14:00",
          end: "2021-11-01 16:00",
          title:
            "Projet,Conception d'un système informatique distribué,Port du masque de Procédure obligatoire".split(
              ","
            )[0],
          description1:
            "Projet,Conception d'un système informatique distribué,Port du masque de Procédure obligatoire".split(
              ","
            )[1],
          description2:
            "Projet,Conception d'un système informatique distribué,Port du masque de Procédure obligatoire".split(
              ","
            )[2],
          local: "C1-5006",
        },
        {
          start: "2021-11-02 12:00",
          end: "2021-11-02 14:00",
          title:
            "Projet,Conception d'un système informatique distribué,Port du masque de Procédure obligatoire".split(
              ","
            )[0],
          description1:
            "Projet,Conception d'un système informatique distribué,Port du masque de Procédure obligatoire".split(
              ","
            )[1],
          description2:
            "Projet,Conception d'un système informatique distribué,Port du masque de Procédure obligatoire".split(
              ","
            )[2],
          local: "C1-5014",
        },
      ],
      eventColor: "#1867c0",
    },
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
        start: "2021-11-01 14:00",
        end: "2021-11-01 16:00",
        name: "Projet,Conception d'un système informatique distribué,Port du masque de Procédure obligatoire".split(
          ","
        )[0],
        description1:
          "Projet,Conception d'un système informatique distribué,Port du masque de Procédure obligatoire".split(
            ","
          )[1],
        description2:
          "Projet,Conception d'un système informatique distribué,Port du masque de Procédure obligatoire".split(
            ","
          )[2],
        heure:
          "2021-11-01 14:00".split(" ")[1] +
          " - " +
          "2021-11-01 16:00".split(" ")[1],
        local: "C1-5006",
        open: false,
        prof: "Bernie",
        session: "Session 3 génie informatique",
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
    this.$refs.calendar.checkChange();
    this.ready = true;
    this.scrollToTime();
    this.updateTime();
    this.getToday();
    this.darkMode = false;
    const successCallback = (position) => {

        /*
        if (position.coords.accuracy >= 1000) {
        //let infoPosition = prompt("Entrer votre addresse", "");
        this.position = infoPosition;
      } else {
        this.position = position;
      }*/
      console.log(position);
    };
    const errorCallback = (error) => {
      console.error(error);
    };
    navigator.geolocation.watchPosition(successCallback, errorCallback);
  },
  watch: {
    value() {
      let calendarApi = this.$refs.fullCalendar.getApi();
      calendarApi.gotoDate(this.value);
    },
    darkMode() {
      if (this.darkMode == true) {
        for (
          let i = 0;
          i < document.getElementsByClassName("fc-list-day-cushion").length;
          i++
        ) {
          document
            .getElementsByClassName("fc-list-day-cushion")
            [i].classList.add("fc-list-day-dark");
        }
        for (
          let i = 0;
          i < document.getElementsByClassName("fc-event").length;
          i++
        ) {
          document
            .getElementsByClassName("fc-event")
            [i].classList.add("fc-event-dark");
        }
        document.getElementById("app").classList.add("dark");
        document.getElementsByTagName("footer")[0].style.color = "#ffffff";
      } else {
        for (
          let i = 0;
          i < document.getElementsByClassName("fc-list-day-cushion").length;
          i++
        ) {
          document
            .getElementsByClassName("fc-list-day-cushion")
            [i].classList.remove("fc-list-day-dark");
        }
        for (
          let i = 0;
          i < document.getElementsByClassName("fc-event").length;
          i++
        ) {
          document
            .getElementsByClassName("fc-event")
            [i].classList.remove("fc-event-dark");
        }
        document.getElementById("app").classList.remove("dark");
        document.getElementsByTagName("footer")[0].style.color = "#000000";
      }
    },
  },

  computed: {
    cal() {
      return this.ready ? this.$refs.calendar : null;
    },
    nowY() {
      return this.cal ? this.cal.timeToY(this.cal.times.now) + "px" : "-10px";
    },
  },
  methods: {
    onResize() {
      this.resize();
    },
    handleEventClick(arg) {
      this.$modal.show(EventModal, {
        text: "This is from the component",
        event: arg.event,
      });
    },
    resize() {
      this.largeur = window.innerWidth;
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
    eventRender(event) {
      let p = document.createElement("p");
      let text = document.createTextNode(
        event.description1 + "<br><br>" + event.description2
      );
      p.appendChild(text);
      p.setAttribute("style", "margin-top: 15px; margin-bottom: 15px");

      let div = document.createElement("div");
      let local = document.createTextNode(event.local);
      div.appendChild(local);
      div.setAttribute("class", "local");
      if (event.description1 && event.description2) {
        event.element.find(".fc-list-event-title").after(p);
      }
      if (event.local) {
        event.element.find(".fc-list-event-time").after(div);
      }
    },
    getEventColor(event) {
      return event.color;
    },
    next() {
      if (this.$refs.fullCalendar) {
        let calendarApi = this.$refs.fullCalendar.getApi();
        calendarApi.next();
      }
      if (this.$refs.calendar) {
        this.$refs.calendar.next();
      }
    },
    prev() {
      if (this.$refs.fullCalendar) {
        let calendarApi = this.$refs.fullCalendar.getApi();
        calendarApi.prev();
      }
      if (this.$refs.calendar) {
        this.$refs.calendar.prev();
      }
    },
    showEvent(event) {
      for (let event in this.events) {
        this.events[event].open = false;
      }
      event.event.open = true;
      event.nativeEvent.target.style.height = "fit-content";
      event.nativeEvent.path[1].style.height = "fit-content";
    },
    dismissEvent() {
      for (let event in this.events) {
        this.events[event].open = false;
      }
    },
    setToday() {
      const now = new Date();
      let val = now.toISOString();
      this.value = val;
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
      //let val = now.toISOString().split("T");
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
    .checkboxNotification {        
        margin-left: 15px;
    }
    /deep/.checkboxNotification > .active {
        background-color: #021a36; 
        /*!important;*/
    }



.buttonTransport {
  margin: 0 10px;
}
/deep/.buttonTransport > .btn {
  background: #1867c0 !important;
}

/deep/.buttonTransport > .active {
  background: #021a36 !important;
}

.buttonTransport:first-child {
  margin-left: 0px;
}

/deep/.v-calendar .v-event-timed:hover {
  opacity: 0.95;
}
.my-event {
  white-space: normal;
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
.event {
  font-weight: 300;
  text-align: left;
  padding: 10px;
  border-radius: 5px;
  white-space: normal;
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
  border-radius: 10px;
}
/deep/.custom-control-input:checked ~ .custom-control-label::before {
  background: #222222;
  border-color: #ffffff;
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
  border: 0;
}
/deep/.theme--light.v-calendar-daily {
  border: 0;
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
/deep/.pl-1 {
  text-align: left;
}
/deep/.v-calendar .v-event-timed-container {
  margin-right: 0 !important;
  border-radius: 10px;
}
.calendar-mobile {
  display: none;
}
.centerBorder {
  width: 95%;
  text-align: center;
  border-bottom: 0.5px solid rgba(255, 255, 255, 0.8);
  line-height: 0.1em;
  float: none !important;
  margin: 20px auto;
}
@media (max-width: 992px) {
  /deep/.modal {
    width: 100%;
  }
}
@media (max-width: 768px) {
  .calendar-mobile {
    display: unset;
  }
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
    display: none;
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
  .agenda {
    min-height: 100vh !important;
    background: #222222;
  }
  .day {
    text-align: left;
    padding: 8px 14px;
    font-size: 30px;
    text-transform: uppercase;
    font-weight: bolder;
    background: #eeeeee;
    color: #000000;
  }
  .tdEvent {
    padding: 0px !important;
    min-height: 133px;
    margin: 0px !important;
  }
  /deep/.local {
    font-weight: bold !important;
    text-align: right !important;
    text-transform: uppercase !important;
  }
  .dot {
    display: inline-block;
    width: 10px;
    height: 10px;
    border-radius: 5px;
  }
  /deep/.fc {
    min-height: calc(100vh - 97px);
    cursor: unset !important;
  }
  /deep/.fc-list-empty-cushion {
    font-size: 20px;
  }
  /deep/.fc-event:hover {
    cursor: unset;
  }
  /deep/.fc-event {
    min-height: 133px;
    width: 100%;
    font-size: 14px;
    background: #ffffff;
    border-top: 1px solid rgba(0, 0, 0, 0.2);
    border-bottom: 1px solid rgba(0, 0, 0, 0.2);
    color: #000000;
    cursor: unset !important;
  }
  /deep/.fc-event-dark {
    min-height: 133px;
    width: 100%;
    font-size: 14px;
    background: #222222;
    border-top: 1px solid rgba(255, 255, 255, 0.2);
    border-bottom: 1px solid rgba(255, 255, 255, 0.2);
    color: #ffffff;
    cursor: unset !important;
  }
  /deep/.fc-event-dark:hover td {
    background: transparent;
    color: #ffffff !important;
  }
  /deep/.fc-list-event-graphic {
    width: 7%;
  }
  /deep/.fc-event:hover td {
    background: transparent;
    color: #000000;
  }
  /deep/.fc-list-day-cushion {
    text-align: left;
    padding: 8px 14px;
    background: #eeeeee;
    color: #000000;
    text-transform: none;
  }
  /deep/.fc-list-day-dark {
    background: #2c2c2c;
    color: #ffffff !important;
  }
  /deep/.fc-list-day-dark a {
    color: #ffffff !important;
  }
  /deep/.fc-list-day-cushion a {
    color: #000000;
  }
  /deep/.fc-list-day-cushion a:hover {
    text-decoration: unset;
    font-weight: unset !important;
  }
  /deep/.fc-list-day-text {
    font-size: 30px;
    text-transform: uppercase;
  }
  /deep/.fc tr td {
    min-height: 133px !important;
  }
  /deep/.fc-event > td {
    border: 0;
  }
  /deep/.fc-list-event-time {
    width: 15%;
  }
  /deep/.fc-list-event-title {
    width: 70%;
  }
  /deep/.fc-list-event-title a {
    font-size: 16px;
  }
  /deep/.fc-theme-standard .fc-list {
    border: 0 !important;
  }
}
</style>
