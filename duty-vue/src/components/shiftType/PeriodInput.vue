<template>

  <v-input :success-messages="messages" :dense="dense" :error="error">
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
                title="Следующих суток"
                v-model="nextDay"
                :dense="dense"/>
  </v-input>


</template>

<script>

import {DateTimeFormatter, LocalTime} from "@js-joda/core";
import {calculateDuration, durationAsString, getEndTime, getStartTime} from "@/components/shiftType/period";

export default {
  props: {
    value: {
      required: true,
      type: Object
    },
    dense: {
      default() {
        return false
      }
    }, error: {
      default() {
        return false;
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
  mounted() {
    this.load()
  },

  methods: {
    reset() {
      this.start = null
      this.end = null
      this.nextDay = false
    },
    load() {
      if (this.value) {
        if (this.value.start) {
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
        } else {
          this.reset()
        }
      } else {
        this.reset()
      }
    },
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
    },
    start() {
      this.periodChanged()
    },
    value: {
      deep: true,
      handler() {
        this.load()
      }
    }

  }
  ,
  name: "PeriodInput"
}
</script>

<style scoped>

</style>
