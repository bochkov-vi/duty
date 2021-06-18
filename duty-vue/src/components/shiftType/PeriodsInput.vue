<template>

  <v-input append-icon="mdi-plus"
           @click:append="add()"
           :error-messages="errors">
    <v-container fluid>
      <v-row>
        <v-col cols="12">
          <div class="body-2">{{ duration }}</div>
        </v-col>
      </v-row>
      <v-row>
        <v-col v-for="(key,index) in Object.keys(value)"
               :key="key">
          <v-card>
            <v-card-actions>
              {{ index + 1 }}
              <v-spacer/>
              <v-btn @click="remove(key)" :dense="dense">
                <v-icon :dense="dense">mdi-trash-can-outline</v-icon>
              </v-btn>
            </v-card-actions>
            <period-input :error="intersectedPeriodIndexes.includes(index)" v-model="value[key]" @input="valueChanged"/>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </v-input>
</template>

<script>
import PeriodInput from "@/components/shiftType/PeriodInput";
import {durationAsString, isPeriodsIntersects, totalDuration} from "@/components/shiftType/period";

export default {
  components: {PeriodInput},
  props: {
    value: {
      type: Array,
      required: false,
      default(){
        return []
      }
    },
    errors: {
      default() {
        return null
      }
    },
    dense: {
      default() {
        return true
      }
    }
  },
  data() {
    return {
      periods: null
    }
  },

  methods: {
    remove(index) {
      if (index > -1) {
        this.value.splice(index, 1);
      }
    },
    add() {
      this.value.push({start: '09:00:00'})
    },
    valueChanged() {
      this.$emit('input', this.value)
    }
  },
  computed: {
    duration() {
      return "Общая продолжительность: " + durationAsString(totalDuration(this.value))
    },
    intersectedPeriodIndexes() {
      return isPeriodsIntersects(this.value)
    }
  },
  name: "PeriodsInput"
}
</script>

<style scoped>

</style>
