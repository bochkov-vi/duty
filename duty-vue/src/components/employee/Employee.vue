<template>
  <v-container>
    <crud-page locale-prefix="employee"
               entity-uri="employee"
               :headers="headers"
               uri="employees">
      <template #inputs="{item}">
        <validation-provider :rules="{required:true}">
          <rang-autocomplete v-model="item.rang"/>
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

Validator.extend('required', {
  ...required,
  message: 'Это поле обязательно для заполнения'
})
export default {
  components: {RangAutocomplete, CrudPage, ValidationProvider},
  data() {
    return {
      headers: [
        {
          text: 'id',
          align: 'start',
          sortable: true,
          value: 'id',
        }, {
          text: 'name',
          align: 'start',
          sortable: true,
          value: 'name',
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
