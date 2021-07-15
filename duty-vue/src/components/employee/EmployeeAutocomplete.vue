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
import {getEmployeeFio} from "@/components/employee/employee";

export default {
  props: {
    "errors": null, "dense": {
      default() {
        return true
      }
    }, label: null, value: null
  },
  name: "EmployeeAutocomplete",
  data() {
    return {
      items: [], search: null
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
      return getEmployeeFio(item)
    },
    load() {
      setLoading(true)
      return axios.get(REST_BASE_URL + "/employees/findByLike", {
        params: {
          search: this.search,
          "projection": "full-data"
        }
      }).then((r) => {
        if (r.data._embedded) return r.data._embedded.employees
        return []
      }).then((array) => this.items = array
      ).then(() => {
        if (this.value) {
          return axios.get(this.value, {
            params: {
              "projection": "full-data"
            }
          }).then(resp => this.items.unshift(resp.data))
        }
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
