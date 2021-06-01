<template>
  <v-container>
    <table-page locale-prefix="employee"
                entity-uri="employees"
                base-path="employee"
                :headers="headers"
                :request-params="{projection:'full-data'}">
      <template #item.fullName="{item}">
        {{ item.lastName }} {{ item.firstName.substr(0, 1) }}.{{ item.middleName.substr(0, 1) }}.
      </template>
      <template #item.shiftTypes="{item}">
        {{ item.shiftTypes.map((i) => i.name).join(', ') }}
      </template>

      <template #inputs="{item}">
        <validation-provider :rules="{required:true}" v-slot="{errors}">
          <rang-autocomplete v-model="item.rang" :errors="errors" :label="$i18n.t('employee.rang')"/>
        </validation-provider>
        <validation-provider :rules="{required:true}"
                             v-slot="{errors}">
          <v-text-field v-model="item.firstName"
                        :error-messages="errors"
                        :label="$i18n.t('employee.firstName')"/>
        </validation-provider>
        <validation-provider :rules="{required:true}"
                             v-slot="{errors}">
          <v-text-field v-model="item.middleName"
                        :error-messages="errors"
                        :label="$i18n.t('employee.middleName')"/>
        </validation-provider>
        <validation-provider :rules="{required:true}" v-slot="{errors}">
          <v-text-field v-model="item.lastName"
                        :error-messages="errors"
                        :label="$i18n.t('employee.lastName')"/>
        </validation-provider>
        <validation-provider :rules="{required:true}" v-slot="{errors}">
          <ShiftTypeAutocomplete v-model="item.shiftTypes"
                                 :error-messages="errors"
                                 :label="$i18n.t('employee.shiftTypes')"/>
        </validation-provider>
      </template>
    </table-page>
  </v-container>
</template>

<script>

import TablePage from "@/components/employee/TablePage";

export default {
  components: {TablePage},
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
          text: 'shiftTypes',
          align: 'start',
          sortable: true,
          value: 'shiftTypes'
        }
      ],
    }
  },
  name: "Employee",
  watch: {}
}
</script>

<style scoped>

</style>
