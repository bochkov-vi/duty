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
                  multiple
                  :dense="dense"></v-autocomplete>
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
        if (r.data._embedded)
          return r.data._embedded.shiftTypes
        return []
      }).finally(() => this.loading = false)
    },
    load() {
      this.findByLike(this.search).then(array => {
        this.items = array
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
    if (this.value)
      Promise.all([...this.value.map(href => axios.get(href).then((resp) => resp.data)),
        this.findByLike()])
          .then(result => this.items = result)
  },
  props: {
    "value": null, "label": null, "errors": {
      type: Array
    }, dense: {
      default() {
        return false;
      }
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
