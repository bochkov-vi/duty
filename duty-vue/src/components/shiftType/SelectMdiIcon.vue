<template>
  <v-autocomplete :dense="dense"
                  :items="items"
                  :value="value"
                  :label="label"
                  item-value="value"
                  item-text="name"
                  @input="$emit('input',$event)">
    <template v-slot:selection="{item}">
      <v-icon>{{ item.value }}</v-icon>
      <v-spacer/>
      <span>{{ item.name }}</span>
    </template>
    <template v-slot:item="{item}">
      <v-icon>{{ item.value }}</v-icon>
      <v-spacer/>
      <span>{{ item.name }}</span>
    </template>
  </v-autocomplete>
</template>

<script>
import {icons} from './icons.js';

export default {
  props: {
    label: null,
    dense: {
      default() {
        return true
      }
    },
    value: null
  },
  data() {
    return {
      items: icons
    }
  },
  name: "SelectMdiIcon"
}
</script>

<style scoped>

</style>
