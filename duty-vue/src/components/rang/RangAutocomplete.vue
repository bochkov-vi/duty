<template>
  <v-autocomplete :value="value"
                  @input="$emit('input', $event)"
                  :error-messages="errors"
                  :search-input.sync="search"
                  :items="items"
                  :label="label"
                  no-filter
                  clearable>
    <template v-slot:selection="data">
      <span>{{ data.item.fullName }}</span>
    </template>
    <template v-slot:item="data">
      <v-list-item-content v-text="data.item.fullName"></v-list-item-content>
    </template>
  </v-autocomplete>
</template>

<script>
import axios from "axios";

export default {
  props: ['value', 'errors', 'label'],
  data() {
    return {
      items: [],
      search: null
    }
  },
  methods: {
    load(txt) {
      axios.get("http://localhost:8080/duty/rangs/findByLike", {
        params: {
          search: txt
        }
      }).then((r) => {
        if (r.data._embedded)
          this.items = r.data._embedded.items
      })
    }
  },
  watch: {
    search(txt) {
      this.load(txt)
    }
  },
  mounted() {
    this.load(this.search)
  },
  name: "RangAutocomplete"
}
</script>

<style scoped>

</style>
