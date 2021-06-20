<template>
  <v-container>
    <select-icon v-model="icon"></select-icon>
  </v-container>
</template>

<script>
import SelectIcon from "@/components/shiftType/SelectMdiIcon";

export default {
  components: {SelectIcon},
  data() {
    return {
      icon:null,
      periods: [{
        start: '09:00:00',
        duration: 'PT4H30M'
      }, {
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
