<template>
  <v-autocomplete :dense="dense"
                  :items="items"
                  :value="value"
                  @input="$emit('input',$event)">
    <template v-slot:item="{item}">
      <v-icon>mdi-{{ item }}</v-icon>
      <v-spacer/>
      <span>{{ item }}</span>
    </template>
  </v-autocomplete>
</template>

<script>
import {icons} from './icons.js';

export default {
  props: {
    dense: {
      default() {
        return true
      }
    }, value: null
  },
  data() {
    return {
      items: icons
    }
  },
  name: "SelectIcon"
}
</script>

<style scoped>

</style>
