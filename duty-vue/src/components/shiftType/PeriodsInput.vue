<template>

  <v-input :messages="messages">
    <v-card>
      <v-card-actions>
        {{messages}}
        <v-spacer/>
        <v-btn>
          <v-icon :dense="dense" @click="add()">mdi-plus</v-icon>
        </v-btn>
      </v-card-actions>
      <v-container>
        <v-row
            v-for="(key,index) in Object.keys(value)"
            :key="key">
          <v-col>
            {{ index+1 }}
          </v-col>
          <v-col>
            <period-input class="v-sheet--outlined" v-model="value[key]"/>
          </v-col>
          <v-col>
            <v-btn @click="remove(key)" :dense="dense">
              <v-icon :dense="dense">mdi-trash-can-outline</v-icon>
            </v-btn>
          </v-col>
        </v-row>
      </v-container>
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
    return {periods: null}
  },

  methods: {
    remove(index) {
      if (index > -1) {
        this.value.splice(index, 1);
      }
    },
    add() {
      this.value.push({start: '09:00:00'})
    }
  },
  computed: {
    messages() {
      return "Общая продолжительность: " + durationAsString(totalDuration(this.value))
    }
  },
  name: "PeriodsInput"
}
</script>

<style scoped>

</style>
