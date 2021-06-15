<template>

  <v-input :messages="messages">
    <v-card class="d-flex flex-column px-10">
      <v-card-actions>
        <v-spacer/>
        <v-btn>
          <v-icon :dense="dense">mdi-plus</v-icon>
        </v-btn>
      </v-card-actions>
      <div class="d-flex flex-nowrap"
           v-for="p in Object.keys(periods)"
           :key="p">
        <period-input
            :dense="dense"
            v-model="periods[p]"/>
        <v-icon @click="remove(p)" :dense="dense">mdi-trash-can-outline</v-icon>
      </div>
    </v-card>
  </v-input>
</template>

<script>
import PeriodInput from "@/components/shiftType/PeriodInput";
import {durationAsString, totalDuration} from "@/components/shiftType/period";

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
    return {periods: null, messages: null}
  },

  methods: {
    remove(start) {
      const periods = this.periods
      delete periods[start]
      this.periods = {...periods}
    },
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
      this.messages = durationAsString(totalDuration(new_val))
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
