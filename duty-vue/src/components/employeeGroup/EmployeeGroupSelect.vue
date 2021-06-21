<template>
  <v-autocomplete :value="value"
                  @input="$emit('input',$event)"
                  :error-messages="errorMessages"
                  :items="items"
                  :label="label"
                  item-value="_links.self.href"
                  :loading="loading"
                  :search-input.sync="search"
                  no-filter
                  clearable
                  :dense="dense">
    <template v-slot:selection="data">
      {{ data.item.name }}
    </template>
    <template v-slot:item="{item}">
      {{ item.name }}
    </template>
  </v-autocomplete>
</template>

<script>
import axios from "axios";
import {error} from "@/store/store";
import {REST_BASE_URL} from "@/http_client";

export default {
  props: {
    "errorMessages": {
      type: Array
    }, value: null, label: null,
    size: {
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
      search: null,
      items: [], loading: false,
      page: 0
    }
  },
  methods: {
    loadItems() {
      this.loading = true


      axios.get(REST_BASE_URL +"/employeeGroups/findByLike", {
        params: {
          search: this.search,
          page: this.page,
          size: this.size
        }
      }).then(resp => {
        if (resp.data._embedded) {
          this.items = resp.data._embedded.employeeGroups;
        } else {
          this.items = []
        }
      }).catch(e => error(e)).finally(() => this.loading = false)
    }
  },
  watch: {
    search() {
      this.loadItems()
    }
  },
  mounted() {
    if (this.value)
      axios.get(this.value).then(resp => this.items = [resp.data])
    else
      this.items =[]
    axios.get(REST_BASE_URL+"/employeeGroups/findByLike", {
      params: {
        search: this.search,
        page: this.page,
        size: this.size
      }
    }).then(resp => {
      if (resp.data._embedded) {
        this.items = [...this.items,...resp.data._embedded.employeeGroups];
      }
    }).catch(e => error(e)).finally(() => this.loading = false)

  },
  name: "EmployeeGroupSelect"

}
</script>

<style scoped>

</style>
