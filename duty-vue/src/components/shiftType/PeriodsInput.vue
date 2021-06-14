<template>

  <div class="d-flex flex-column">
    <v-card v-for="p in Object.keys(periods)"
            :key="p">
      <period-input
          :dense="dense"
          v-model="periods[p]"/>
    </v-card>
  </div>


</template>

<script>
import PeriodInput from "@/components/shiftType/PeriodInput";

export default {
  components: {PeriodInput},
  props: {
    value: {
      type: Array,
      required: true
    },
    dense: {
      default() {
        return true
      }
    }
  },
  data() {
    return {periods: null}
  },
  methods: {
    load() {
      if (this.value) {
        const periods = {}
        this.value.forEach((el) => periods[el.start] = el)
        this.periods = periods
      } else {
        this.periods = []
      }
    },
    unload() {
      const new_val = [];
      for (const key in this.periods) {
        new_val.push(this.periods[key])
      }
      this.$emit("input", new_val)
    }
  }, created() {
    this.load()
  }, watch: {
    periods: {
      deep: true,
      handler: function () {
        this.unload()
      }
    }
  },
  name: "PeriodsInput"
}
</script>

<style scoped>

</style>
