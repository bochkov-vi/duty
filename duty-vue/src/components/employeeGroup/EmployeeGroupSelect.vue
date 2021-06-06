<template>
  <v-autocomplete :value="value"
                  @input="$emit('input',$event)"
                  :error-messages="error-messages"
                  :items="items"
                  item-value="_links.self.href"
                  :search-input.sync="search">
    <template v-slot:item="{item:row}">
      {{row.name}}
    </template>
  </v-autocomplete>
</template>

<script>
export default {
  props: {
    "error-messages": {
      type: Array
    }
  },
  data() {
    return {
      search: null,
      value: null,
      items: null
    }
  },
  name: "EmployeeGroupSelect"

}
</script>

<style scoped>

</style>