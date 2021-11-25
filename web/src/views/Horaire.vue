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
          <!-- <img src="@/assets/Horarbus_Esquisse.png" class="logo"> -->
          <div class="horarbus nom" style="cursor: default">HorarBus</div>
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
              <a class="nom">{{ user.firstname }} {{ user.lastname }}</a>
            </template>
            <!--<b-dropdown-item href="/profile"><font-awesome-icon :icon="shoppingCog" /> Profile</b-dropdown-item>-->
            <b-dropdown-item @click="showPref()">
              Mes paramètres
            </b-dropdown-item>
            <b-dropdown-item @click="showFAQ()"> FAQ </b-dropdown-item>
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
              <b-col class="sideItem" v-b-toggle.sidebar-menu @click="logout">
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
            v-model="pref.transport"
            :aria-describedby="ariaDescribedby"
            name="some-radios"
            value="WALKING"
            button
            class="buttonTransport"
            ><img :src="require('../assets/walk.png')"
          /></b-form-radio>
          <b-form-radio
            v-model="pref.transport"
            :aria-describedby="ariaDescribedby"
            name="some-radios"
            value="BICYCLING"
            button
            class="buttonTransport"
            ><img :src="require('../assets/bike.png')"
          /></b-form-radio>
          <b-form-radio
            v-model="pref.transport"
            :aria-describedby="ariaDescribedby"
            name="some-radios"
            value="TRANSIT"
            button
            class="buttonTransport"
            ><img :src="require('../assets/bus.png')"
          /></b-form-radio>
          <b-form-radio
            v-model="pref.transport"
            :aria-describedby="ariaDescribedby"
            name="some-radios"
            value="DRIVING"
            button
            class="buttonTransport"
            ><img :src="require('../assets/car.png')"
          /></b-form-radio>
        </b-form-group>

        <!--Notification-->
        <b-row>
          <b-col cols="12" class="sectionPref">
            Notifications
            <b-row style="margin: 0">
              <b-form-checkbox
                v-model="pref.notification_enable"
                name="check-button"
                class="checkboxNotification"
              >
              </b-form-checkbox>
              <b-col>
                <input
                  v-model.number="pref.temps_avance_notification"
                  id="timenotif"
                  :disabled="!pref.notification_enable"
                  type="number"
                  class="tempsSelect"
                />
              </b-col>
            </b-row>
            <div style="font-size: 12px">
              Entrer le temps avant le départ pour recevoir une notification.
            </div>
          </b-col>
        </b-row>

        <!--Temps d'avance minimum-->
        <b-row>
          <b-col cols="12" class="sectionPref">
            Avance minimum
            <br />
            <input
              v-model.number="pref.temps_avance"
              type="number"
              class="tempsSelect"
            />
          </b-col>
        </b-row>

        <!--Domicile-->
        <b-row>
          <b-col cols="12" class="sectionPref">
            Domicile
            <br />
            <input
              v-model="pref.adresse_maison"
              placeholder="Adresse du domicile"
              class="tempsSelect"
            />
          </b-col>
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
        <b-row>
          <b-col style="display: flex; justify-content: center">
            <b-button variant="dark" class="saveButton" @click="sendPref">
              Enregistrer les informations
            </b-button>
          </b-col>
        </b-row>
      </b-modal>

      <b-modal
        ref="faq"
        hide-footer
        hide-header
        :centered="true"
        body-class="preference"
      >
        <b-row>
          <b-col cols="10" class="title"> Foire aux questions </b-col>
          <b-col cols="2" class="zoneClose">
            <button @click="hideFAQ()" class="close">x</button>
          </b-col>
        </b-row>

        <b-row>
            <b-col cols="12" @click="question1 = !question1" class="titreTexteFAQ">
                <strong>HorarBus &#x3a; qu’est-ce que c’est?</strong>
            </b-col> <br />
            <b-col cols="12" class="texteFAQ" v-if="question1">
                HorarBus est une application construite pour la communauté étudiante de l’Université de Sherbrooke en génie et a comme mission de faire sauver du temps aux étudiants en prévoyant pour eux leurs déplacements. L’application affiche principalement un calendrier qui comprend l’horaire de l’usager ainsi que ces déplacements. HorarBus ajoute automatiquement toutes les plages horaires nécessaires pour le transport en se basant sur l’emplacement de l’évènement, l’adresse au domicile de l’étudiant et le type de transport par défaut. Plusieurs paramètres peuvent être ajustés pour convenir à tous les étudiants. Pour être certain que l’usager ne manque aucun cours, il est possible de paramétrer une notification qui l’empêcherait de passer tout droit.
            </b-col>

            <br /><br />
            <b-col cols="12" @click="question2 = !question2" class="titreTexteFAQ">
                <strong>Sur quel type d’appareil peut-on utiliser l’application?</strong> <br />
            </b-col> <br />
            <b-col cols="12" class="texteFAQ" v-if="question2">
                HorarBus sur site web est accessible sur tous les appareils, que ce soit sur ordinateur Windows, MacOS et Linux, ainsi que sur téléphone Android et Apple. Par contre, l’application pour téléphone est seulement accessible sur Android.
            </b-col>

            <br /><br />
            <b-col cols="12" @click="question3 = !question3" class="titreTexteFAQ">
                <strong>Quel calendrier apparait dans HorarBus?</strong> <br />
            </b-col> <br />
            <b-col cols="12" class="texteFAQ" v-if="question3">
                Vous verrez apparaitre dans HorarBus l'horaire à laquelle votre clé iCal correspond. Tous les évènements de votre calendrier qui sont reliés à votre clé iCal seront affichés. Par contre, il n’est pas possible d’inclure d’autre calendrier. Vous pouvez fournir seulement une clé iCal.
            </b-col>

            <!--tuto clé ical-->
            <br /><br />
            <b-col cols="12" @click="question4 = !question4" class="titreTexteFAQ">
                <strong>Où trouver ma clé iCal?</strong> <br />
            </b-col> <br />
            <b-col cols="12" class="texteFAQ" v-if="question4">
                Rendez-vous sur Horarius, puis cliquez sur l’icône iCal qui est à gauche de votre nom. L'image suivante représente l'icône sur laquelle vous devez cliquer. <br />
            </b-col>
            <b-col cols="12" class="text-center" v-if="question4">
                <!-- <img :src="require('../assets/icone-ical.png')" /> -->
            </b-col>
            <b-col cols="12" class="texteFAQ" v-if="question4">
                Vous devriez voir apparaitre une fenêtre contenant votre clé iCal (voir l'image qui suit). Vous pouvez simplement appuyer sur le bouton “COPIER” et nous fournir le résultat.
            </b-col>
            <b-col cols="12" class="text-center" v-if="question4">
                <!-- <img :src="require('../assets/cle-ical.png')" class="text-center" /> -->
            </b-col>

            <br /><br />
            <b-col cols="12" @click="question5 = !question5" class="titreTexteFAQ">
                <strong>Puis-je modifier mes préférences?</strong> <br />
            </b-col> <br />
                     <b-col cols="12" class="texteFAQ" v-if="question5">
                         Il est possible que chaque utilisateur ait ses propres préférences. Pour les modifier, il faut simplement cliquer sur votre nom, puis sur "Mes préférences". Une fois que vous avez fini de modifier vos préférences, il faut absolument peser sur le bouton "Enregistrer les informations", sinon vous perdrez votre sélection.<br /><br />
                         <strong> Puis-je modifier le moyen de transport pour un évènement en particulier? </strong><br />
                         Oui, même si vous avez un mode de transport par défaut, il est possible de modifier le mode de transport individuel de chaque évènement. <br /><br />
                         <strong> À quoi servent les notifications </strong><br />
                         Les notifications peuvent se faire entendre quelques minutes avant votre départ pour s’assurer que vous ne passiez pas tout droit. C’est une des grandes forces de l’application HorarBus. Même si vous avez complètement oublié un évènement, HorarBus vous le rappellera, mais surtout, il vous permettra de ne pas arriver en retard. Il est également possible de choisir combien de temps d’avance vous souhaitez recevoir les notifications.<br /><br />
                         <strong> Que signifie avance minimum? </strong><br />
                         Ce paramètre sert à décider combien de temps d’avance vous voulez arriver à vos évènements. Par défaut, il est fixé à 0 minute. En fonction de ce paramètre, HorarBus vous fera partir plus ou moins tôt.<br /><br />
                         <strong> Pourquoi me demande-t-on mon adresse au domicile? </strong><br />
                         Vous avez l’opportunité de mettre votre adresse au domicile, ou non. Si jamais vous décidez de la mettre, HorarBus s’en servira pour prévoir votre retour à la maison à la fin de vos évènements.
                     </b-col>




            <b-col></b-col>
        </b-row>
      </b-modal>


      <b-modal
        ref="mapsSetting"
        hide-footer
        hide-header
        :centered="true"
        body-class="preference"
      >
        <b-row>
          <b-col cols="10" class="title"> Trajet </b-col>
          <b-col cols="2" class="zoneClose">
            <button @click="hideMapsSetting()" class="close">x</button>
          </b-col>
        </b-row>

        <!--Mode de transport-->
        <b-row>
          <b-col cols="12" class="sectionPref"> Mode de transport </b-col>
        </b-row>
        <b-form-group v-slot="{ ariaDescribedby }">
          <b-form-radio
            v-model="map.transport"
            :aria-describedby="ariaDescribedby"
            name="some-radios"
            value="WALKING"
            button
            class="buttonTransport"
            ><img :src="require('../assets/walk.png')"
          /></b-form-radio>
          <b-form-radio
            v-model="map.transport"
            :aria-describedby="ariaDescribedby"
            name="some-radios"
            value="BICYCLING"
            button
            class="buttonTransport"
            ><img :src="require('../assets/bike.png')"
          /></b-form-radio>
          <b-form-radio
            v-model="map.transport"
            :aria-describedby="ariaDescribedby"
            name="some-radios"
            value="TRANSIT"
            button
            class="buttonTransport"
            ><img :src="require('../assets/bus.png')"
          /></b-form-radio>
          <b-form-radio
            v-model="map.transport"
            :aria-describedby="ariaDescribedby"
            name="some-radios"
            value="DRIVING"
            button
            class="buttonTransport"
            ><img :src="require('../assets/car.png')"
          /></b-form-radio>
        </b-form-group>

        <!--Notification-->
        <b-row>
          <b-col cols="12" class="sectionPref">
            Notifications
            <b-row style="margin: 0">
              <b-form-checkbox
                v-model="map.notification_enable"
                name="check-button"
                class="checkboxNotification"
              >
              </b-form-checkbox>
              <b-col>
                <input
                  v-model.number="map.temps_avance_notification"
                  id="timenotif"
                  :disabled="!map.notification_enable"
                  type="number"
                  class="tempsSelect"
                />
              </b-col>
            </b-row>
            <div style="font-size: 12px">
              Entrer le temps avant le départ pour recevoir une notification.
            </div>
          </b-col>
        </b-row>

        <!--Temps d'avance minimum-->
        <b-row>
          <b-col cols="12" class="sectionPref">
            Temps d'avance désirer
            <br />
            <input
              v-model.number="map.temps_avance"
              type="number"
              class="tempsSelect"
            />
          </b-col>
        </b-row>

        <b-row>
          <b-col style="display: flex; justify-content: center">
            <b-button variant="dark" class="saveButton" @click="sendMaps">
              Enregistrer les informations
            </b-button>
          </b-col>
        </b-row>
      </b-modal>

      <b-modal
        ref="maps"
        hide-footer
        hide-header
        :centered="true"
        body-class="maps"
        dialog-class="modal-maps"
        style="padding-right:0"
      >
        <b-row>
          <b-col cols="10" class="title"> Maps </b-col>
          <b-col cols="2" class="zoneClose">
            <button @click="hideMaps()" class="close">x</button>
          </b-col>
        </b-row>
        <Maps />
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
        :event-color="getEventColor"
        v-model="value"
        color="black"
        locale="fr"
        :type="type"
        class="calendar"
      >
        <template
          v-slot:event="{ event }"
          :style="{ backgroundColor: event.color }"
        >
          <div
            class="event"
            @mouseenter="setHeight(event.id)"
            v-click-outside="dismissEvent"
            v-if="!event.trajet"
            :id="event.id"
            v-on:click="showEvent"
            @click="openEvent(event)"
          >
            {{ event.heure }}
            <br />
            <a style="font-weight: 700">{{ event.summary }}</a>
            <br />
            {{ event.location }}

            <div v-if="event.open === true">
              <div class="centerBorder"></div>
              {{ event.description1 }}
              <br />
              <br />
              {{ event.description2 }}
              <div v-if="event.description3">
                <br />
                {{ event.description3 }}
              </div>
              <div class="centerBorder" v-if="event.prof"></div>
              {{ event.prof }}
              <div class="centerBorder" v-if="event.session"></div>
              <a :href="event.url">{{ event.session }}</a>
            </div>
          </div>
          <div
            class="event"
            v-click-outside="dismissEvent"
            @mouseenter="setHeight(event.id)"
            :id="event.id"
            v-else
            v-on:click="showEvent"
            @click="openEvent(event)"
          >
            {{ event.heureDepart }} -
            <a style="font-weight: 700">{{ event.summary }}</a>
            <button class="editButton" @click="showMapsSetting">
              <b-icon-pencil></b-icon-pencil>
            </button>
            <br />
            
            <div v-if="event.open">
              <div v-if="event.heureArrive">
                Arrivé prévue à {{ event.heureArrive }}
              </div>
              <div class="centerBorder"></div>
              <div v-if="event.description1">
                {{ event.description1 }}
              </div>
              <div v-if="event.description2">
                {{ event.description2 }}
              </div>
              <div v-if="event.description3">
                {{ event.description3 }}
              </div>
              <div class="centerBorder"></div>
              <a @click="showMaps" class="link">voir la carte</a>
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
      <div class="calendar-mobile" v-touch:swipe.left="next" v-touch:swipe.right="prev">
        <Fullcalendar ref="fullCalendar" :options="calendarOptions" />
      </div>
    </b-col>
  </b-row>
</template>

<script>
require("@fullcalendar/list/main.css");
import Fullcalendar from "@fullcalendar/vue";
import InteractionPlugin from "@fullcalendar/interaction";
import ListPlugin from "@fullcalendar/list";
import allLocales from "@fullcalendar/core/locales-all";
import Maps from "@/components/Maps";
import { mapState, mapActions } from "vuex";

export default {
  components: {
    Fullcalendar,
    Maps,
  },

  data: () => ({
    question1: false,
    question2: false,
    question3: false,
    question4: false,
    question5: false,
    question6: false,
    pref: {
      adresse_maison: "",
      temps_avance: 0,
      transport: "TRANSIT",
      notification_enable: false,
      temps_avance_notification: 0,
    },
    map: {
      temps_avance: 0,
      transport: "TRANSIT",
      notification_enable: false,
      temps_avance_notification: 0,
    },
    position: "",
    today: new Date(),
    value: new Date(),
    type: "week",
    initialHeight: "",
    eventActive: "",
    calendarOptions: {
      plugins: [InteractionPlugin, ListPlugin],
      initialView: "listWeek",
      locales: allLocales,
      eventDidMount: function (arg) {
        let p = document.createElement("p");
        if (arg.event.extendedProps.description3) {
          p.innerHTML =
            arg.event.extendedProps.description1 +
            "<br><br>" +
            arg.event.extendedProps.description2 +
            "<br>" +
            arg.event.extendedProps.description3;
        } else {
          p.innerHTML =
            arg.event.extendedProps.description1 +
            "<br><br>" +
            arg.event.extendedProps.description2;
        }
        p.setAttribute("style", "margin-top: 15px; margin-bottom:15px");
        let div = document.createElement("div");
        div.textContent = arg.event.extendedProps.location;
        div.setAttribute("class", "local");
        if (
          arg.event.extendedProps.description1 &&
          arg.event.extendedProps.description2
        ) {
          arg.el.cells[2].appendChild(p);
        }
        if (arg.event.extendedProps.location) {
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
          location: "C1-5006",
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
          location: "C1-5014",
        },
        {
        id: 2,
        start: "2021-11-04 13:00",
        end: "2021-11-04 13:30",
        summary:
          "Départ UdeS,Terminus intersection Blvd - Rue,Autobus #69,Arrive dans 420s".split(
            ","
          )[0],
        description1:
          "Départ UdeS,Terminus intersection Blvd - Rue,Autobus #69".split(
            ","
          )[1],
        description2:
          "Départ UdeS,Terminus intersection Blvd - Rue,Autobus #69".split(
            ","
          )[2],
        color: "orange",
        heureDepart: "2021-11-04 13:00".split(" ")[1],
        heureArrive: "2021-11-04 14:00".split(" ")[1],
        open: false,
        trajet: true,
      },
      ],
      eventColor: "#1867c0",
    },
    ready: false,
    largeur: 0,
    darkMode: false,
    events: [
      {
        id: 0,
        start: "2021-11-01 14:00",
        end: "2021-11-01 16:00",
        summary:
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
        heure:
          "2021-11-01 14:00".split(" ")[1] +
          " - " +
          "2021-11-01 16:00".split(" ")[1],
        location: "C1-5006",
        open: false,
        prof: "Bernie",
        session: "Session 3 génie informatique",
        trajet: false,
      },
      {
        id: 1,
        start: "2021-11-04 14:00",
        end: "2021-11-04 16:00",
        summary:
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
        heure:
          "2021-11-04 14:00".split(" ")[1] +
          " - " +
          "2021-11-04 16:00".split(" ")[1],
        location: "C1-5006",
        open: false,
        prof: "Bernie",
        trajet: false,
        session: "Session 3 génie informatique",
      },
      {
        id: 2,
        start: "2021-11-04 13:00",
        end: "2021-11-04 13:30",
        summary:
          "Départ UdeS,Terminus intersection Blvd - Rue,Autobus #69,Arrive dans 420s".split(
            ","
          )[0],
        description1:
          "Départ UdeS,Terminus intersection Blvd - Rue,Autobus #69".split(
            ","
          )[1],
        description2:
          "Départ UdeS,Terminus intersection Blvd - Rue,Autobus #69".split(
            ","
          )[2],
        color: "orange",
        heureDepart: "2021-11-04 13:00".split(" ")[1],
        heureArrive: "2021-11-04 14:00".split(" ")[1],
        open: false,
        trajet: true,
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
  updated(){
    this.switchTheme();
  },
  mounted() {
    this.getUser();
    this.getEvents();
    this.resize();
    this.$refs.calendar.checkChange();
    this.ready = true;
    this.scrollToTime();
    this.updateTime();
    this.getToday();
    const successCallback = (position) => {
      // if(position.coords.accuracy >= 1000){
      //   let infoPosition = prompt("Entrer votre addresse", "");
      //   this.position = infoPosition;
      // }
      // else{
      //   this.position = position;
      // }
      console.log(position);
      this.setGeo(position);
    };
    const errorCallback = (error) => {
      console.error(error);
    };
    navigator.geolocation.watchPosition(successCallback, errorCallback);
  },
  watch: {
    user() {
      this.getEvents();
    },
    eventsState() {
      for (let i in this.eventsState) {
        this.eventsState[i].open = false;
        let now = new Date();
        var offset = now.getTimezoneOffset() / 60;
        let start = new Date(this.eventsState[i].start)
        start.setHours(start.getHours() - offset);
        start = start.toISOString();
        this.eventsState[i].start = start;
        let end = new Date(this.eventsState[i].end);
        end.setHours(end.getHours() - offset);
        end = end.toISOString();
        this.eventsState[i].end = end;
      }
      this.events = this.eventsState;
      this.calendarOptions.events = this.eventsState;
      for (let i in this.eventsState) {
        this.events[i].start =
          this.eventsState[i].start.split("T")[0] + 
          " " +
          this.eventsState[i].start.split("T")[1].split(":")[0] +
          ":" +
          this.eventsState[i].start.split("T")[1].split(":")[1];
        this.events[i].end =
          this.eventsState[i].end.split("T")[0] + 
          " " +
          this.eventsState[i].end.split("T")[1].split(":")[0] +
          ":" +
          this.eventsState[i].end.split("T")[1].split(":")[1];
        
        this.events[i].heure =
          this.events[i].start.split(" ")[1] +
          " - " +
          this.events[i].end.split(" ")[1];
        this.events[i].description1 = this.events[i].description.split("\n")[0];
        this.events[i].description2 = this.events[i].description.split("\n")[2];
        this.events[i].id = "eventFullWindow" + i;
        if (this.events[i].description.split("\n")[3]) {
          this.events[i].description3 =
            this.events[i].description.split("\n")[3];
        }
        //this.events[i].style.backgroundColor = this.events[i].color;
        this.calendarOptions.events[i].start = this.events[i].start;
        this.calendarOptions.events[i].end = this.events[i].end;
        this.calendarOptions.events[i].heure =
          this.calendarOptions.events[i].start.split(" ")[1] +
          " - " +
          this.calendarOptions.events[i].end.split(" ")[1];
        this.calendarOptions.events[i].title =
          this.calendarOptions.events[i].summary;
        this.calendarOptions.events[i].description1 =
          this.calendarOptions.events[i].description.split("\n")[0];
        this.calendarOptions.events[i].description2 =
          this.calendarOptions.events[i].description.split("\n")[2];
        if (this.calendarOptions.events[i].description.split("\n")[3]) {
          this.calendarOptions.events[i].description3 =
            this.calendarOptions.events[i].description.split("\n")[3];
        }
      }
    },
    prefState() {
      this.pref.transport = this.prefState.transport;
      this.pref.temps_avance = this.prefState.preparation_time;
      this.pref.temps_avance_notification = this.prefState.notification.time;
      this.pref.notification_enable = this.prefState.notification.enabled;
      this.pref.adresse_maison = this.prefState.local_address;
      this.darkMode = this.prefState.dark_mode;
    },
    value() {
      let calendarApi = this.$refs.fullCalendar.getApi();
      calendarApi.gotoDate(this.value);
    },
    darkMode() {
      this.switchTheme();
    },
  },

  computed: {
    ...mapState({
      user: (state) => state.user.user,
      prefState: (state) => state.user.pref,
      eventsState: (state) => state.calendar.events,
    }),
    cal() {
      return this.ready ? this.$refs.calendar : null;
    },
    nowY() {
      return this.cal ? this.cal.timeToY(this.cal.times.now) + "px" : "-10px";
    },
  },
  methods: {
    ...mapActions(["putUser", "putPref", "getEvents","setGeo","getUser"]),
    sendPref() {
      let pref = {
        preparation_time: "",
        transport: "",
        notification: {
          time: "",
          enabled: "",
        },
        dark_mode: "",
      };
      pref.preparation_time = this.pref.temps_avance;
      pref.transport = this.pref.transport;
      pref.local_address = this.pref.adresse_maison;
      pref.notification = {
        time: this.pref.temps_avance_notification,
        enabled: this.pref.notification_enable,
      };
      pref.dark_mode = this.darkMode;
      this.putUser(pref);
      this.hidePref();
    },
    sendMaps() {
      let map = {
        preparation_time: "",
        transport: "",
        notification: {
          time: "",
          enabled: "",
        },
      };
      map.preparation_time = this.map.temps_avance;
      map.transport = this.map.transport;
      map.notification = {
        time: this.map.temps_avance_notification,
        enabled: this.map.notification_enable,
      };
      console.log(map);
      //this.putMaps(pref);
      this.hideMaps();
    },
    onResize() {
      this.resize();
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
    showMaps() {
      this.$refs["maps"].show();
    },
    hideMaps() {
      this.$refs["maps"].hide();
    },
    showMapsSetting() {
      this.$refs["mapsSetting"].show();
    },
    hideMapsSetting() {
      this.$refs["mapsSetting"].hide();
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
      var cvs, ctx;
      cvs = document.createElement("canvas");
      cvs.height = 1;
      cvs.width = 1;
      ctx = cvs.getContext("2d");
      if (event.color) {
        ctx.fillStyle = event.color;
      } else {
        return "#1867c0";
      }
      ctx.fillRect(0, 0, 1, 1);
      return "rgba(" + ctx.getImageData(0, 0, 1, 1).data + ")";
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
    switchTheme() {
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
    openEvent(event){
      event.open= true;
    },
    showEvent(event) {
      event.target.style.height = "fit-content";
      if(event.path){
        event.path[1].style.zIndex = "1000";
      }
      if(event.target.offsetParent){
        event.target.offsetParent.style.zIndex = "1000";
      }
      event.target.style.zIndex = "1000";
      if (this.eventActive.event === undefined) {
        if(event.path){
          this.initialHeight = event.path[1].style.height;
        }
        if(event.target.offsetParent){
          this.initialHeight = event.target.offsetParent.style.height;
        }
        
      }
      this.eventActive = event;
      if(event.path){
        event.path[1].style.height = "fit-content";
      }
      if(event.target.offsetParent){
        event.target.offsetParent.style.height = "fit-content";
      }
      if (this.eventActive === event) {
        document.body.addEventListener(
          "click",
          event.target.clickOutsideEvent
        );
      } else {
        this.dismissEvent();
      }
    },
    dismissEvent() {
      for(let i in this.events){
        this.events[i].open = false;
      }
      //this.eventActive.event.open = false;
      if(this.eventActive.path){
        this.eventActive.path[1].style.zIndex = "0";
        this.eventActive.path[1].style.height = this.initialHeight;
      }
      if(this.eventActive.target.offsetParent){
        this.eventActive.target.offsetParent.style.zIndex = "0";
        this.eventActive.target.offsetParent.style.height = this.initialHeight;
      }
      document.body.removeEventListener(
        "click",
        this.eventActive.target.clickOutsideEvent
      );
      this.eventActive = "";
    },
    setHeight(id) {
      document.getElementById(id).style.height =
        document.getElementById(id).parentElement.style.height;
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
.logo{
  height: 56px !important;
}
.saveButton {
  font-size: 16px;
  text-align: center;
  color: #222222;
  height: 100%;
  background: #ffffff;
  border: 1px solid #222222;
  border-radius: 10px;
  font-weight: bold;
}
.editButton {
  border-radius: 100%;
  background-color: #ffbf5f;
  width: 20px;
  height: 20px;
  float: right;
}
.saveButton:hover {
  background: transparent;
  color: #ffffff;
  border: 1px solid #ffffff;
}
.checkboxNotification {
  vertical-align: middle;
  display: flex;
  flex-direction: column;
  margin-top: 8px;
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
.tempsSelect {
  color: #ffffff !important;
  width: 80% !important;
  border-bottom: 1px solid rgba(255, 255, 255, 0.7) !important;
  height: 30px !important;
  background: transparent !important;
}
.tempsSelect:disabled {
  background: #3d3a3a !important;
  color: #c0c0c0 !important;
  border-bottom: 1px solid rgba(255, 255, 255, 0.3) !important;
}
.tempsSelect:focus-within {
  background: transparent !important;
  border-bottom: 1px solid rgba(255, 255, 255, 1) !important;
}
.tempsSelect:focus {
  box-shadow: none !important;
  border-bottom: 1px solid rgba(255, 255, 255, 1) !important;
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
  padding: 1px;
  border-radius: 5px;
  white-space: normal;
  color: #ffffff;
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

.link{
  color: inherit;
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
  padding: 30px !important;
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
  padding: 7.5px 15px !important;
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
/deep/.modal{
  padding-right: 0px !important;
}

/deep/.modal-open .modal{
  overflow-y: unset !important;
}
/deep/.maps {
  background: #222222;
  border-radius: 11px;
  color: #ffffff;
  width: 100%;
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
  font-size: 16px;
  font-weight: bold;
}
/deep/.theme--dark.v-calendar-events .v-event-timed {
  color: #ffffff !important;
  font-size: 15px;
  border: 1px solid #ffffff;
  font-weight: bolder;
  border-radius: 10px;
}
/deep/.custom-control-input:checked ~ .custom-control-label::before {
  background: #222222;
  border-color: #ffffff;
}
.labelTemps {
  color: #ffffff !important;
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
/deep/.modal-maps  {
  width: 100%;
  max-width: none;
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
  font-size: 16px;
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
button:focus {
  background: transparent !important;
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
