<template>
	<b-row style="margin-top: 0">
		<b-col style="padding: 0">
			<b-navbar toggleable="lg" sticky type="dark" class="navbar">
				<b-navbar-nav class="navbar-menu mx-auto" style="margin-left: 0 !important">
					<button class="caret" @click="prev()">
						<b-icon-caret-left-fill></b-icon-caret-left-fill>
					</button>
					<a class="date" v-if="$refs.calendar" @click="showChoseDate()">
						{{ this.$refs.calendar.title }}
					</a>
					<a class="date" v-else @click="showChoseDate()"> {{ today }}oidnaoidanoidpandanpwd </a>
					<button class="caret" @click="next()">
						<b-icon-caret-right-fill></b-icon-caret-right-fill>
					</button>
				</b-navbar-nav>
				<b-navbar-nav class="navbar-menu mx-auto">
					<div class="nom" style="cursor: default">HorarBus?</div>
				</b-navbar-nav>
				<b-navbar-nav class="navbar-menu mx-auto" style="margin-right: 0 !important">
					<b-nav-item-dropdown right class="drop" no-caret>
						<!-- Using 'button-content' slot -->
						<template v-slot:button-content>
							<a class="nom"> John Doe </a>
						</template>
						<!--<b-dropdown-item href="/profile"><font-awesome-icon :icon="shoppingCog" /> Profile</b-dropdown-item>-->
						<b-dropdown-item @click="showPref()"> Mes paramètres </b-dropdown-item>
						<b-dropdown-item>Voir tous les groupes</b-dropdown-item>
						<b-dropdown-item>Se déconnecter</b-dropdown-item>
					</b-nav-item-dropdown>
				</b-navbar-nav>
			</b-navbar>
			<b-modal ref="pref" hide-footer hide-header :centered="true" body-class="preference">
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
			<b-modal ref="chooseDate" hide-footer hide-header body-class="modal-calendar" dialog-class="modal-left">
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
				:dark="true"
				v-model="value"
				color="black"
				locale="fr"
				event-color="#1867c0"
				@click:event="showEvent"
				:type="type"
			>
				<template v-slot:day-body="{ date, week }">
					<div class="v-current-time" :class="{ first: date === week[0].date }" :style="{ top: nowY }"></div>
				</template>
			</v-calendar>
		</b-col>
	</b-row>
</template>

<script>
export default {
	data: () => ({
		today: new Date(),
		value: new Date(),
		type: "week",
		ready: false,
		colors: ["blue", "indigo", "deep-purple", "cyan", "green", "orange", "grey darken-1"],
		events: [
			{
				name: "Weekly Meeting",
				start: "2021-10-15 09:00",
				end: "2021-10-15 10:00",
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
	}),

	mounted() {
		this.$refs.calendar.checkChange();
		this.ready = true;
		this.scrollToTime();
		this.updateTime();
		this.getToday();
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
			return this.cal ? this.cal.times.now.hour * 60 + this.cal.times.now.minute : 0;
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
}
/deep/.b-calendar .b-calendar-nav .btn {
	margin: 0 1px;
	padding: 0;
}
</style>
