<template>
  <v-container>
    <crud-page locale-prefix="employee"
               item-name="employee"
               resource="employees"
               :headers="headers">
      <template #item.fullName="{item}">
        {{ item.lastName }} {{ item.firstName.substr(0, 1) }}.{{ item.middleName.substr(0, 1) }}.
      </template>
      <template #item.shiftTypes="{item}">
        {{ item.shiftTypes.map((i) => i.name).join(', ') }}
      </template>

      <template #inputs="{item}">
        <validation-provider :rules="{required:true}" v-slot="{errors}">
          <rang-autocomplete v-model="item.rang"
                             :errors="errors"
                             :label="$i18n.t('employee.rang')"
                             dense/>
        </validation-provider>
        <validation-provider :rules="{required:true}"
                             v-slot="{errors}">
          <v-text-field dense
                        v-model="item.post"
                        :error-messages="errors"
                        :label="$i18n.t('employee.post')"/>
        </validation-provider>
        <validation-provider
            :rules="{required:true,fioUnique:{id:item.id,firstName:item.firstName,middleName:item.middleName,lastName:item.lastName}}"
            v-slot="{errors}">
          <v-text-field v-model="item.firstName"
                        :error-messages="errors"
                        :label="$i18n.t('employee.firstName')"
                        dense/>
        </validation-provider>
        <validation-provider
            :rules="{required:true,fioUnique:{id:item.id,firstName:item.firstName,middleName:item.middleName,lastName:item.lastName}}"
            v-slot="{errors}">
          <v-text-field v-model="item.middleName"
                        :error-messages="errors"
                        :label="$i18n.t('employee.middleName')"
                        dense/>
        </validation-provider>
        <validation-provider
            :rules="{required:true,fioUnique:{id:item.id,firstName:item.firstName,middleName:item.middleName,lastName:item.lastName}}"
            v-slot="{errors}">
          <v-text-field v-model="item.lastName"
                        :error-messages="errors"
                        :label="$i18n.t('employee.lastName')"
                        dense/>
        </validation-provider>
        <validation-provider :rules="{required:false}" v-slot="{errors}">
          <employee-group-select v-model="item.employeeGroup"
                                 :error-messages="errors"
                                 :label="$i18n.t('employee.employeeGroup')"
                                 dense/>
        </validation-provider>
        <validation-provider :rules="{required:true}" v-slot="{errors}">
          <ShiftTypeAutocomplete v-model="item.shiftTypes"
                                 :errors="errors"
                                 :label="$i18n.t('employee.shiftTypes')"
                                 dense/>
        </validation-provider>
      </template>

    </crud-page>
  </v-container>
</template>

<script>

import CrudPage from "@/components/CrudPage.vue";
import * as Validator from "vee-validate";
import {ValidationProvider} from "vee-validate";
import {required} from "vee-validate/dist/rules";
import RangAutocomplete from "@/components/rang/RangAutocomplete";
import ShiftTypeAutocomplete from "@/components/shiftType/ShiftTypeAutocomplete";
import EmployeeGroupSelect from "@/components/employeeGroup/EmployeeGroupSelect";
import axios from "axios";
import {REST_BASE_URL} from "@/http_client";

Validator.extend('required', {
  ...required,
  message: 'Это поле обязательно для заполнения'
})

Validator.extend('fioUnique', {
  params: ["id", "firstName", "middleName", "lastName"],
  validate(value, args) {
    console.debug("fio validation" + args)
    console.debug(args)
    const currentID = args.id;
    const result = axios.get(REST_BASE_URL + "/rest/employees/search/findByFIO", {
      params: {...args}
    }).then((resp) => {
          const valid = !(resp.data._embedded.employees.filter((item) => item.id !== currentID).length > 0);
          return {valid: valid};
        }
    )
    return result;
  },
  message: 'Сотрудник с таким ФИО существует'
})
export default {
  components: {EmployeeGroupSelect, ShiftTypeAutocomplete, RangAutocomplete, CrudPage, ValidationProvider},
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
          text: 'fullName',
          align: 'start',
          sortable: true,
          value: 'fullName',
        }, {
          text: 'employeeGroup',
          align: 'start',
          sortable: true,
          value: 'employeeGroup.name',
        }, {
          text: 'shiftTypes',
          align: 'start',
          sortable: true,
          value: 'shiftTypes'
        }
      ],
    }
  },
  name: "EmployeePage",
  watch: {}
}
</script>

<style scoped>

</style>
