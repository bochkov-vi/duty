<template>
  <v-row>
    <v-col cols="6">
      <v-text-field type="time"
                    v-model="time"/>
    </v-col>
    <v-col cols="2">
      <v-checkbox label="Следующих суток"
                  v-model="nextDay"/>
    </v-col>
  </v-row>
</template>

<script>
import {DateTimeFormatter, Duration, LocalTime} from "@js-joda/core";

export default {
  props: {
    value: null
  },
  data() {
    return {}
  },
  computed: {
    nextDay: {
      get() {
        try {
          const duration = Duration.parse(this.value)
          return duration.toDays() === 1
        } catch (e) {
          console.debug(e.message)
        }
        return null
      },
      set(val) {
        const value = this.calculateDuration(this.time, val)
        this.$emit("input", value)
      }
    },

    time: {
      get() {
        try {
          const duration = Duration.parse(this.value)
          const secondsOfDay = (duration.toMillis() / 1000) % 86400
          const time = LocalTime.ofSecondOfDay(secondsOfDay)
          return time.format(DateTimeFormatter.ofPattern("HH:mm"))
        } catch (e) {
          console.debug(e.message)
        }
        return null
      },
      set(val) {
        const value = this.calculateDuration(val, this.nextDay)
        this.$emit("input", value)
      }
    }
  },
  methods: {
    calculateDuration(time, nextDay) {
      let result = null;
      if (time)
        try {
          const tm = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"))
          const nd = !!nextDay
          result = Duration.ofSeconds(tm.toSecondOfDay()).plus(Duration.ofDays(nd ? 1 : 0)).toString()
        } catch (e) {
          console.debug(e)
        }
      return result
    }
  },
  mounted() {

  },
  name: "DurationField"
}
</script>

<style scoped>

</style>
