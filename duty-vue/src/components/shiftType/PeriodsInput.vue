<template>

  <v-input :error-messages="errors">
    <template v-slot:append>
      <v-btn @click="add()">
        <v-icon :dense="dense">mdi-tab-plus</v-icon>
      </v-btn>
    </template>
    <v-container fluid>
      <v-row>
        <v-col cols="12">
          <div class="body-2">{{ duration }}</div>
        </v-col>
      </v-row>
      <v-row v-if="value && value.length>0">
        <v-col
            v-for="(key,index) in Object.keys(value)"
            :key="key">
          <v-card>
            <v-card-actions>
              {{ index + 1 }}
              <v-spacer/>
              <v-btn @click="remove(key)" :dense="dense">
                <v-icon :dense="dense">mdi-trash-can-outline</v-icon>
              </v-btn>
            </v-card-actions>
            <period-input :error="intersectedPeriodIndexes.includes(index)" v-model="value[key]"/>
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
      required: true
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
    return {}
  },

  methods: {
    remove(index) {
      if (index >= 0) {
        this.value.splice(index, 1)
      }
    },
    add() {
      this.value.push({start:null,duration:null})
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
  watch: {
    value(val) {
      this.$emit('input', val)
    }
  },
  name: "PeriodsInput"
}
</script>

<style scoped>

</style>
