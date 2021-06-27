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
                  :loading="loading"
                  deletable-chips
                  small-chips
                  :dense="dense">
    <template #item="{item}">
      {{ getFio(item) }}
    </template>
    <template #selection="{item}">
      {{ getFio(item) }}
    </template>
  </v-autocomplete>
</template>

<script>
import {getLoading, setLoading} from "@/store/store";
import axios from "axios";
import {REST_BASE_URL} from "@/http_client";

export default {
  props: {
    "errors": null, "dense": {
      default() {
        return true
      }
    }, label: null
  },
  name: "EmployeeAutocomplete",
  data() {
    return {
      value: null, items: [], search: null
    }
  },
  computed: {
    loading() {
      return getLoading()
    }
  },
  watch: {
    value() {
      this.load()
    }
  }, methods: {
    getFio(item) {

      return item.rang.name + " " + item.lastName + " " + item.firstName + " " + item.middleName
    },
    load() {
      setLoading(true)
      return axios.get(REST_BASE_URL + "/employees/findByLike", {
        params: {
          search: this.search
        }
      }).then((r) => {
        if (r.data._embedded) return r.data._embedded.employees
        return []
      }).then((array) => this.items = array
      ).then(() => {
        if (this.value) this.items.unshift(this.value)
      }).finally(() => setLoading()
      )
    }
  }, mounted() {
    this.load()
  }
}
</script>

<style scoped>

</style>
