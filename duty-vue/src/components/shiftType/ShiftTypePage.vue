<template>
  <v-container>
    <crud-page locale-prefix="shiftType"
               item-name="shiftType"
               resource="shiftTypes"
               base-url="http://localhost:8080/duty/rest"
               :headers="headers">


      <template #inputs="{item}">
        <validation-provider :rules="{required:true,uniqueName:{id:item.id}}"
                             v-slot="{errors}">
          <v-text-field dense
                        v-model="item.name"
                        :error-messages="errors"
                        :label="$i18n.t('shiftType.name')"/>
        </validation-provider>


        <div>
          <label class="v-label v-label--active">
            {{$i18n.t('shiftType.daysToWeekend')}}
          </label>
          <v-row>
            <v-col cols="2">
              <v-checkbox
                  v-model="item.daysToWeekend"
                  label="0"
                  :value="0"/>
              <v-checkbox
                  v-model="item.daysToWeekend"
                  label="1"
                  :value="1"/>
            </v-col>
            <v-col cols="2">
              <v-checkbox
                  v-model="item.daysToWeekend"
                  label="2"
                  :value="2"/>
              <v-checkbox
                  v-model="item.daysToWeekend"
                  label="3"
                  :value="3"/>
            </v-col>
            <v-col cols="2">
              <v-checkbox
                  v-model="item.daysToWeekend"
                  label="4"
                  :value="4"/>
              <v-checkbox
                  v-model="item.daysToWeekend"
                  label="5"
                  :value="5"/>
            </v-col>
            <v-col cols="2">
              <v-checkbox
                  v-model="item.daysToWeekend"
                  label="6"
                  :value="6"/>
            </v-col>
          </v-row>
        </div>


        <validation-provider v-slot="{errors}">
          <v-text-field v-model="item.daysFromWeekend"
                        :error-messages="errors"
                        :label="$i18n.t('shiftType.daysFromWeekend')"
                        dense/>
        </validation-provider>
        <validation-provider v-slot="{errors}">
          <v-text-field v-model="item.periods"
                        :error-messages="errors"
                        :label="$i18n.t('shiftType.periods')"
                        dense/>
        </validation-provider>
      </template>

    </crud-page>
  </v-container>
</template>

<script>

import CrudPage from "@/components/CrudPage.vue";
import {ValidationProvider} from "vee-validate";


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
          text: 'uiOptions.label',
          align: 'start',
          sortable: true,
          value: 'uiOptions',
        }, {
          text: 'daysToWeekend',
          align: 'start',
          sortable: true,
          value: 'daysToWeekend',
        }, {
          text: 'daysFromWeekend',
          align: 'start',
          sortable: true,
          value: 'daysFromWeekend',
        }, {
          text: 'periods',
          align: 'start',
          sortable: true,
          value: 'periods',
        }
      ],
    }
  },
  name: "ShiftTypePage",
  watch: {}
}
</script>

<style scoped>

</style>
