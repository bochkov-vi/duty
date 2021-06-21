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
                  :loading="loading"
                  :dense="dense">
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
import {REST_BASE_URL} from "@/http_client";

export default {
  directives: {
    dense: {
      inserted() {
        return false
      }
    }
  },
  props: {
    'value': null, 'errors': null, 'label': null, size: {
      default() {
        return 10
      }
    }, dense: {
      default() {
        return false;
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
      return axios.get(REST_BASE_URL+"/rangs/findByLike", {
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
    this.findByLike().then((data) => this.items = [...this.items, ...data._embedded.rangs])
  },
  name: "RangAutocomplete"
}
</script>

<style scoped>

</style>
