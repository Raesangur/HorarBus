<template>
  <v-calendar
    ref="calendar"
    :value="today"
    :events="events"
    :event-color="getEventColor"
    :type="type"

  >
    <template v-slot:day-body="{ date, week }">
      <div
        class="v-current-time"
        :class="{ first: date === week[0].date }"
        :style="{ top: nowY }"
      ></div>
    </template>
  </v-calendar>
</template>

<script>
export default {
  next(){
    this.$refs.calendar.next;
  },
  prev(){
    this.$refs.calendar.prev;
  },
  data: () => ({
    today: new Date(),
    value: "",
    type: "week",
    ready: false,
    colors: ['blue', 'indigo', 'deep-purple', 'cyan', 'green', 'orange', 'grey darken-1'],
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
    this.$refs.calendar.next();
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
    getEventColor (event) {
      return event.color
    },
    next(){
      this.$refs.calendar.next();
    },
    prev(){
      this.$refs.calendar.prev();
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
/deep/.v-calendar-daily__scroll-area{
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
  border-radius: 50%;
  margin-top: -5px;
  margin-left: -6.5px;
}
</style>
