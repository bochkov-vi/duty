<template>

  <v-input :messages="messages" :dense="dense">
    <v-text-field type="time"
                  v-model="start"
                  :dense="dense"/>
    <v-spacer/>
    <v-text-field type="time"
                  :dense="dense"
                  v-model="end"/>
    <v-spacer/>
    <v-checkbox label="Следующих суток"
                v-model="nextDay"
                :dense="dense"/>
  </v-input>


</template>

<script>

import {DateTimeFormatter, Duration, LocalTime} from "@js-joda/core";

export default {
  props: {
    value: null,
    dense: {
      default() {
        return false
      }
    }
  },
  data() {
    return {
      start: null,
      end: null,
      nextDay: false,
      duration: null,
      messages: null
    }
  },
  created() {
    if (this.value) {
      if (this.value.start)
        try {
          const start = LocalTime.parse(this.value.start);
          this.start = start.format(DateTimeFormatter.ofPattern("HH:mm"))
          if (this.value.duration) {
            const duration = Duration.parse(this.value.duration)
            const secondsOfDay = ((duration.toMillis() / 1000) + start.toSecondOfDay()) % 86400
            const time = LocalTime.ofSecondOfDay(secondsOfDay)
            this.end = time.format(DateTimeFormatter.ofPattern("HH:mm"))
            this.nextDay = duration.toDays() === 1

          }
        } catch
            (e) {
          console.debug(e.message)
        }
    }
  }
  ,
  methods: {
    periodChanged() {
      this.calculateDuration();
      const period = {
        start: LocalTime.parse(this.start, DateTimeFormatter.ofPattern("HH:mm")).format(DateTimeFormatter.ofPattern("HH:mm:SS")),
        duration: this.duration
      }
      this.$emit('input', period)
    }
    ,
    calculateDuration() {
      let duration = null;
      if (this.end)
        try {
          const start = Duration.ofSeconds(LocalTime.parse(this.start, DateTimeFormatter.ofPattern("HH:mm")).toSecondOfDay())
          const end = Duration.ofSeconds(LocalTime.parse(this.end, DateTimeFormatter.ofPattern("HH:mm")).toSecondOfDay())
          const nextDay = !!this.nextDay
          duration = end.plus(Duration.ofDays(nextDay ? 1 : 0))
          duration = duration.minus(start)
          let message = "Продолжительность: "
          const hours = duration.toHours()
          const h = Number.parseInt(hours.toString().slice(-1))
          if (h > 0)
            if (h === 1) {
              message += hours + " час"
            } else if (h <= 4 && h >= 2) {
              message += hours + " часа"
            } else if (h <= 9 && h >= 5) {
              message += hours + " часов"
            }
          const minutes = duration.toMinutes() % 60
          const m = Number.parseInt(minutes.toString().slice(-1))
          if (m > 0) {
            if (h > 0)
              message += " "
            if (m === 1) {
              message += minutes + " минута"
            } else if (m <= 4 && m >= 2) {
              message += minutes + " минуты"
            } else if (m <= 9 && m >= 5) {
              message += minutes + " минут"
            }
          }
          this.messages = message
        } catch (e) {
          console.debug(e)
        }
      this.duration = duration ? duration.toString() : null


    }
  }
  ,
  watch: {
    end() {
      this.periodChanged()
    }
    ,
    nextDay() {
      this.periodChanged()
    }
    ,
    start() {
      this.periodChanged()
    }
  }
  ,
  name: "PeriodInput",
  components
:
{
}

}
</script>

<style scoped>

</style>
