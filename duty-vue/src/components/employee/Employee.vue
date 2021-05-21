<template>
  <v-container>
    <crud-page locale-prefix="employee"
               entity-uri="employee"
               :headers="headers"
               uri="employees"
               :id="id"></crud-page>
  </v-container>
</template>

<script>

import CrudPage from "@/components/employee/CrudPage.vue";

export default {
  components: {CrudPage},
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
  props: {
    id:null
  },
  name: "Employee"
}
</script>

<style scoped>

</style>