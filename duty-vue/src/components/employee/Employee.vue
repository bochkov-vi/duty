<template>
  <v-container>
    <crud-page locale-prefix="employee"
               entity-uri="employees"
               :headers="headers"
               :request-params="{projection:'full-data'}">


        <template #item.fullName="{item}">
          {{ item.lastName }} {{item.firstName.substr(0,1)}}.{{item.middleName.substr(0,1) }}.
        </template>

      <template #inputs="{item}">
        <validation-provider :rules="{required:true}" v-slot="{errors}">
          <rang-autocomplete v-model="item.rang" :errors="errors" :label="$i18n.t('employee.rang')"/>
        </validation-provider>
        <validation-provider :rules="{required:true}"
                             v-slot="{errors}">
          <v-text-field v-model="item.firstName"
                        :error-messages="errors"
                        :label="$i18n.t('employee.firstName')"
          ></v-text-field>
        </validation-provider>
        <validation-provider :rules="{required:true}"
                             v-slot="{errors}">
          <v-text-field v-model="item.middleName"
                        :error-messages="errors"
                        :label="$i18n.t('employee.middleName')"
          ></v-text-field>
        </validation-provider>
        <validation-provider :rules="{required:true}" v-slot="{errors}">
          <v-text-field v-model="item.lastName"
                        :error-messages="errors"
                        :label="$i18n.t('employee.lastName')"
          ></v-text-field>
        </validation-provider>
        <validation-provider :rules="{required:true}" v-slot="{errors}">
          <ShiftTypeAutocomplete v-model="item.shiftTypes"
                                 :error-messages="errors"
                                 :label="$i18n.t('employee.shiftTypes')"
          ></ShiftTypeAutocomplete>
        </validation-provider>
      </template>
    </crud-page>
  </v-container>
</template>

<script>

import CrudPage from "@/components/employee/CrudPage.vue";
import * as Validator from "vee-validate";
import {ValidationProvider} from "vee-validate";
import {required} from "vee-validate/dist/rules";
import RangAutocomplete from "@/components/rang/RangAutocomplete";
import ShiftTypeAutocomplete from "@/components/shiftType/ShiftTypeAutocomplete";

Validator.extend('required', {
  ...required,
  message: 'Это поле обязательно для заполнения'
})
export default {
  components: {ShiftTypeAutocomplete, RangAutocomplete, CrudPage, ValidationProvider},
  data() {
    return {
      headers: [
        {
          text: 'id',
          align: 'start',
          sortable: true,
          value: 'id',
        }, {
          text: 'rang',
          align: 'start',
          sortable: true,
          value: 'rang.name',
        }, {
          text: 'firstName',
          align: 'start',
          sortable: true,
          value: 'firstName',
        }, {
          text: 'fullName',
          align: 'start',
          sortable: true,
          value: 'fullName',
        }, {
          text: 'lastName',
          align: 'start',
          sortable: true,
          value: 'lastName'
        }
      ],
    }
  },
  name: "Employee",
  watch: {
    item: {
      handler() {
        console.log("slot change item:" + this.item)
      },
      deep: true
    }
  }
}
</script>

<style scoped>

</style>
