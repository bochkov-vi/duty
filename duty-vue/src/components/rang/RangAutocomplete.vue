<template>
  <v-autocomplete :value="value"
                  @input="input"
                  :error-messages="errors"
                  :search-input.sync="search"
                  :items="items"
                  :label="label"
                  no-filter
                  clearable
                  item-value="id"
                  item-text="fullName"
                  return-object
                  :loading="loading">
  </v-autocomplete>
</template>

<script>
import axios from "axios";

export default {
  props: ['value', 'errors', 'label'],
  data() {
    return {
      loading: false,
      items: [],
      search: null,
      page: 0
    }
  },
  methods: {
    input(event) {
      this.$emit('input', event)
    },
    findByLike() {
      this.loading = true;
      return axios.get("http://localhost:8080/duty/rangs/findByLike", {
        params: {
          search: this.search,
          page: this.page,
          size: 50
        }
      }).then((r) => {
        return r.data
      }).finally(() => this.loading = false)
    },
    load() {
      this.findByLike().then((d) => {
        if (d._embedded) {
          this.items = d._embedded.items;
        }
      })
    }
  },
  watch: {
    search(txt) {
      this.load(txt)
    },
    page() {
      this.load(this.search)
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
