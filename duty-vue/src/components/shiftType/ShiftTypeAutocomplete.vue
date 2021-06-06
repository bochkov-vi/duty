<template>
  <v-autocomplete :value="value"
                  @input="$emit('input',$event)"
                  :items="items"
                  :error-messages="errors"
                  :search-input.sync="search"
                  :label="label"
                  no-filter
                  clearable
                  item-value="_links.self.href"
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
          if (page._embedded) {
            this.items = page._embedded.shiftTypes
          } else {
            this.items = []
          }
        } else {
          this.items = []
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
  props: {
    "value": null, "label": null, "errors": {
      type: Array
    }
  },
  data() {
    return {
      search: null,
      loading: false,
      page: {_embedded: {items: []}},
      pageNumber: 0,
      items: []
    }
  },
  name: "ShiftTypeAutocomplete"
}
</script>

<style scoped>

</style>
