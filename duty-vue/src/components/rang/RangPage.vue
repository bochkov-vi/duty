<template>
  <v-container>
    <crud-page locale-prefix="rang"
               item-name="rang"
               resource="rangs"
               :headers="headers">


      <template #inputs="{item}">
        <validation-provider :rules="{required:true,uniqueName:{id:item.id}}"
                             v-slot="{errors}">
          <v-text-field dense
                        v-model="item.name"
                        :error-messages="errors"
                        :label="$i18n.t('rang.name')"/>
        </validation-provider>
        <validation-provider :rules="{required:true,uniqueFullName:{id:item.id}}"
                             v-slot="{errors}">
          <v-text-field v-model="item.fullName"
                        :error-messages="errors"
                        :label="$i18n.t('rang.fullName')"
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
import axios from "axios";
import i18n from "@/i18n";
import {REST_BASE_URL} from "@/http_client";

Validator.extend('required', {
  ...required,
  message: 'Это поле обязательно для заполнения'
})
Validator.extend('uniqueName', {
  params: ["id"],
  validate: (value, args) => {
    if (value) {
      const currentID = args.id;
      const result = axios.get(REST_BASE_URL + "/rangs/search/findByName", {
        params: {search: value}
      }).then((resp) => {
            const valid = !(resp.data._embedded.rangs.filter((item) => item.id !== currentID).length > 0);
            const result = {valid: valid}
            return result;
          }
      )
      return result;
    }
  },
  message: function () {
    return i18n.t('label.fieldIsDuplicate')
  }
})
Validator.extend('uniqueFullName', {
  params: ["id"],
  validate: (value, args) => {
    if (value) {
      const currentID = args.id;
      const result = axios.get(REST_BASE_URL+"/rest/rangs/search/findByFullName", {
        params: {search: value}
      }).then((resp) => {
            const valid = !(resp.data._embedded.rangs.filter((item) => item.id !== currentID).length > 0);
            const result = {valid: valid}
            return result;
          }
      )
      return result;
    }
  },
  message: function () {
    return i18n.t('label.fieldIsDuplicate')
  }
})

export default {
  components: {CrudPage, ValidationProvider},
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
        }, {
          text: 'fullName',
          align: 'start',
          sortable: true,
          value: 'fullName',
        }
      ],
    }
  },
  name: "RangPage",
  watch: {}
}
</script>

<style scoped>

</style>
