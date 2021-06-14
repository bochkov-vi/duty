<template>
  <v-container>
    {{period}}
    <period-input v-model="period"/>
  </v-container>
</template>

<script>
import PeriodInput from "@/components/shiftType/PeriodInput";
export default {
  components: {PeriodInput},
  data() {
    return {
      period: {
        start: '09:00:00',
        duration: 'PT4H30M'
      }
    }
  },
  name: "HomePage"
}
</script>

<style scoped>

</style>
