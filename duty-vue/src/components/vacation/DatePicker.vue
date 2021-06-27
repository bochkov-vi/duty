<template>
  <v-menu
      v-model="menu"
      :close-on-content-click="false"
      max-width="290"
  >
    <template v-slot:activator="{ on, attrs }">
      <v-text-field
          :value="formatedDate"
          clearable
          :label="label"
          readonly
          v-bind="attrs"
          v-on="on"
          @click:clear="date = null"
          :dense="dense"
      ></v-text-field>
    </template>
    <v-date-picker
        v-model="date"
        @change="menu = false"
    ></v-date-picker>
  </v-menu>
</template>

<script>
import {DateTimeFormatter, LocalDate} from "@js-joda/core";

export default {
  props: {
    'value': null, "label": null, "dense": {
      default() {
        return true
      }
    }
  },
  data: () => ({
    menu: false,
    formatter: DateTimeFormatter.ofPattern("dd.MM.yy"),
    date: null,
    label: null
  }),

  computed: {
    formatedDate() {
      return this.date ? LocalDate.parse(this.date).format(this.formatter) : ''
    }
  },
  watch: {
    date() {
      this.$emit('input', this.date)
    }
  },
  name: "DatePicker"
}
</script>

<style scoped>

</style>
