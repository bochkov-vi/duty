<template>
  <crud-page :headers="headers"
             item-name="vacation"
             locale-prefix="vacation"
             resource="vacations"
             :newitem="newitem"
             @update:item="calculateDayCount($event)"
             :on-page-loaded="onPageLoaded">
    <template #inputs="{item}">
      <validation-provider rules="required" v-slot="{ errors }">
        <employee-autocomplete v-model="item.employee"
                               :errors="errors"
                               :label="$i18n.t('vacation.employee')"/>
      </validation-provider>
      <validation-provider rules="required" v-slot="{ errors }">
        <v-text-field type="number"
                      v-model.number="item.year"
                      :label="$i18n.t('vacation.year')"
                      :error-messages="errors"/>
      </validation-provider>
      <validation-provider rules="required" v-slot="{ errors }">
        <v-select v-model="item.type"
                  :label="$i18n.t('vacation.type')"
                  :items="vacationTypes"
                  item-value="type"
                  item-text="label"
                  :error-messages="errors"
                  dense/>
      </validation-provider>
      <validation-provider rules="required" v-slot="{ errors }">
        <date-picker v-model="item.start"
                     :label="$i18n.t('vacation.start')"
                     :errors="errors"/>
      </validation-provider>
      <validation-provider rules="required" v-slot="{ errors }">
        <date-picker v-model="item.end"
                     :label="$i18n.t('vacation.end')"
                     :errors="errors"/>
      </validation-provider>
      <v-text-field v-model="item.note"
                    :label="$i18n.t('vacation.note')"/>
      <v-text-field type="number"
                    disabled
                    v-model.number="dayCount"
                    :label="$i18n.t('vacation.dayCount')"/>
    </template>
    <template #item.start="{item}">{{ $d(new Date(item.start)) }}</template>
    <template #item.end="{item}">{{ $d(new Date(item.end)) }}</template>
    <template #item.employee="{item}">{{ item.employeeFio }}</template>
  </crud-page>
</template>

<script>
import CrudPage from "@/components/CrudPage";
import axios from "axios";
import {REST_BASE_URL} from "@/http_client";
import EmployeeAutocomplete from "@/components/employee/EmployeeAutocomplete";
import DatePicker from "@/components/vacation/DatePicker";
import {Duration, LocalDate, LocalTime} from "@js-joda/core";
import * as Validator from "vee-validate";
import {ValidationProvider} from "vee-validate";
import {required} from "vee-validate/dist/rules";
import {getEmployeeFio} from "@/components/employee/employee";

Validator.extend('required', {
  ...required,
  message: 'Это поле обязательно для заполнения'
})
export default {
  components: {DatePicker, EmployeeAutocomplete, CrudPage, ValidationProvider},
  data() {
    return {
      dayCount: 0,
      "vacationTypes": [{type: "MAIN", label: "Основной"}, {type: "ADVANCED", label: "Дополнительный"}],
      headers: [{value: "employee", text: "employee", align: 'start', sortable: true},
        {value: "type", text: "type", align: 'start', sortable: true},
        {value: "start", text: "start", align: 'start', sortable: true},
        {value: "end", text: "end", align: 'start', sortable: true},
        {value: "note", text: "note", align: 'start', sortable: true},
        {value: "dayCount", text: "dayCount", align: 'start', sortable: true}]
    }
  }, methods: {
    onPageLoaded(page) {
      console.debug("pageLoaded")
      console.debug(page)
    },
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
    }, calculateDayCount(vacation) {
      let result = 0;
      if (vacation && vacation.start && vacation.end) {
        const start = LocalDate.parse(vacation.start).atTime(LocalTime.of(0));
        const end = LocalDate.parse(vacation.end).atTime(LocalTime.of(0)).plusDays(1)
        const duration = Duration.between(start, end)
        result = duration.toDays()
      }
      this.dayCount = result;
      return result
    }, getFio(item) {
      return getEmployeeFio(item);
    }
  },
  computed: {
    newitem: function () {
      return {"year": LocalDate.now().year() + 1, "type": "MAIN"}
    }
  },
  name: "VacationPage"
}
</script>

<style scoped>

</style>
