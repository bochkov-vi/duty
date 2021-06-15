<template>
  <v-container>
    {{periods}}
    <periods-input v-model="periods"/>
  </v-container>
</template>

<script>
import PeriodsInput from "@/components/shiftType/PeriodsInput";
export default {
  components: {PeriodsInput},
  data() {
    return {
      periods: [{
        start: '09:00:00',
        duration: 'PT4H30M'
      },{
        start: '13:45:00',
        duration: 'PT4H15M'
      }]
    }
  },
  name: "HomePage"
}
</script>

<style scoped>

</style>
