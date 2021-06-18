<template>
  <v-container>
    <select-icon></select-icon>
  </v-container>
</template>

<script>
import SelectIcon from "@/components/shiftType/SelectIcon";

export default {
  components: {SelectIcon},
  data() {
    return {
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
