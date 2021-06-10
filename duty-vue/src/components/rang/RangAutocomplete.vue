<template>
  <v-autocomplete :value="value"
                  @input="input"
                  :error-messages="errors"
                  :search-input.sync="search"
                  :items="items"
                  :label="label"
                  no-filter
                  clearable
                  item-value="_links.self.href"
                  :loading="loading">
    <template v-slot:selection="data">
      {{ data.item.fullName }}
    </template>
    <template v-slot:item="{item}">
      {{ item.fullName }}
    </template>
  </v-autocomplete>
</template>

<script>
import axios from "axios";

export default {
  props: {
    'value': null, 'errors': null, 'label': null, size: {
      default() {
        return 10
      }
    }
  },
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
          size: this.size
        }
      }).then((r) => {
        return r.data
      }).finally(() => this.loading = false)
    },
    load() {
      this.findByLike().then((d) => {
        if (d._embedded) {
          this.items = d._embedded.rangs;
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
  created() {
    if (this.value)
      axios.get(this.value).then((resp) => this.items = [resp.data])
  },
  name: "RangAutocomplete"
}
</script>

<style scoped>

</style>
