<template>
  <crud-page :headers="headers"
             item-name="vacation"
             locale-prefix="vacation"
             resource="vacations">
    <template #inputs="{item}">
      <employee-autocomplete v-model="item.employee"/>
      <v-text-field type="number" v-model.number="item.year" :label="$i18n.t('vacation.year')" />
      <v-text-field v-model="item.type" :label="$i18n.t('vacation.type')"/>
      <date-picker v-model="item.start" :label="$i18n.t('vacation.start')"/>
      <date-picker v-model="item.end" :label="$i18n.t('vacation.end')"/>
      <v-text-field v-model="item.note" :label="$i18n.t('vacation.note')"/>
      <v-text-field type="number" v-model.number="item.dayCount" :label="$i18n.t('vacation.dayCount')" />
    </template>
  </crud-page>
</template>

<script>
import CrudPage from "@/components/CrudPage";
import axios from "axios";
import {REST_BASE_URL} from "@/http_client";
import EmployeeAutocomplete from "@/components/employee/EmployeeAutocomplete";
import DatePicker from "@/components/vacation/DatePicker";

export default {
  components: {DatePicker, EmployeeAutocomplete, CrudPage},
  data() {
    return {
      headers: [{value: "id", text: "id", align: 'start', sortable: true},
        {value: "employee", text: "employee", align: 'start', sortable: true},
        {value: "type", text: "type", align: 'start', sortable: true},
        {value: "start", text: "start", align: 'start', sortable: true},
        {value: "end", text: "end", align: 'start', sortable: true},
        {value: "note", text: "note", align: 'start', sortable: true},
        {value: "dayCount", text: "dayCount", align: 'start', sortable: true}]
    }
  }, methods: {
    findByLike() {
      this.loading = true;
      return axios.get(REST_BASE_URL + "/vacations/findByLike", {
        params: {
          search: this.search
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
  name: "VacationPage"
}
</script>

<style scoped>

</style>
