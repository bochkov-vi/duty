<template>

  <v-input :messages="messages" :dense="dense">
    <v-text-field type="time"
                  v-model="start"
                  :dense="dense"
                  step="300"/>
    <v-spacer/>
    <v-text-field type="time"
                  :dense="dense"
                  v-model="end"
                  step="300"/>
    <v-spacer/>
    <v-checkbox label="Следующих суток"
                v-model="nextDay"
                :dense="dense"/>
  </v-input>


</template>

<script>

import {DateTimeFormatter, LocalTime} from "@js-joda/core";
import {calculateDuration, durationAsString, getEndTime, getStartTime} from "@/components/shiftType/period";

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
          const start = getStartTime(this.value);
          const {end, nextDay} = getEndTime(this.value)
          this.start = start ? start.format(DateTimeFormatter.ofPattern("HH:mm")) : null
          this.end = end
          this.nextDay = nextDay
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
      const period = {}
      if (this.start)
        period.start = LocalTime.parse(this.start, DateTimeFormatter.ofPattern("HH:mm")).format(DateTimeFormatter.ofPattern("HH:mm:SS"))
      if (this.duration)
        period.duration = this.duration

      this.$emit('input', period)
    }
    ,
    calculateDuration() {
      const duration = calculateDuration(this.start, this.end, this.nextDay)
      this.duration = duration.isZero() ? null : duration.toString()
      this.messages = durationAsString(duration)
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
  name: "PeriodInput"
}
</script>

<style scoped>

</style>
