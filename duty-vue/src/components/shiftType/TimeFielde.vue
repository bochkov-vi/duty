<template>
  <v-text-field type="time"
                v-model="time"/>
</template>

<script>
import {DateTimeFormatter, LocalTime} from "@js-joda/core";

export default {
  props: {
    value: null
  },
  data() {
    return {}
  },
  computed: {
    time: {
      get() {
        try {
          const time = LocalTime.parse(this.value)
          return time.format(DateTimeFormatter.ofPattern("HH:mm"))
        } catch (e) {
          console.debug(e.message)
        }
        return null
      },
      set(val) {
        let value = null
        try {
          const time = LocalTime.parse(val, DateTimeFormatter.ofPattern("HH:mm"))
          value = time.format(DateTimeFormatter.ofPattern("HH:mm:SS"))
        } catch (e) {
          console.debug(e.message)
        }
        this.$emit("input", value)
      }
    }
  },
  mounted() {

  },
  name: "TimeField"
}
</script>

<style scoped>

</style>
