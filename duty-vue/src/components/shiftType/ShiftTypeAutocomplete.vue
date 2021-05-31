<template>
  <v-autocomplete :value="value"
                  @input="$emit('input',$event)"
                  :items="page._embedded.items"
                  :error-messages="errors"
                  :search-input.sync="search"
                  :label="label"
                  no-filter
                  clearable
                  item-value="_links.item.href"
                  item-text="name"
                  :loading="loading"
                  multiple></v-autocomplete>
</template>

<script>
import axios from "axios";

export default {
  methods: {
    findByLike() {
      this.loading = true;
      return axios.get("http://localhost:8080/duty/shiftTypes/findByLike", {
        params: {
          search: this.search,
          page: this.pageNumber,
          size: 50
        }
      }).then((r) => {
        return r.data
      }).finally(() => this.loading = false)
    },
    load() {
      this.findByLike().then((page) => {
        if (page) {
          this.page = page
        }
      })
    }
  },
  watch: {
    search(txt) {
      this.load(txt)
    },
    pageNumber() {
      this.load(this.search)
    }
  },
  mounted() {
    this.load(this.search)
  },
  props: ["value", "label"],
  data() {
    return {
      search: null,
      errors: [],
      loading: false,
      page: {_embedded: {items: []}},
      pageNumber: 0
    }
  },
  name: "ShiftTypeAutocomplete"
}
</script>

<style scoped>

</style>
