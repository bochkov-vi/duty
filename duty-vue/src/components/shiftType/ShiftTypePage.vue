<template>
  <v-container>
    <crud-page locale-prefix="shiftType"
               item-name="shiftType"
               resource="shiftTypes"
               :headers="headers"
               :item.sync="item">
      <template #inputs="{item}">
        <validation-provider :rules="{required:true,uniqueName:{id:item.id}}"
                             v-slot="{errors}">
          <v-text-field dense
                        v-model="item.name"
                        :error-messages="errors"
                        :label="$i18n.t('shiftType.name')"/>
        </validation-provider>
        <v-select :label="$i18n.t('shiftType.daysToWeekend')"
                  :items="daysOfWeekend"
                  item-value="value"
                  item-text="label"
                  v-model="item.daysToWeekend"
                  multiple>
          <template v-slot:selection="{attrs, item:chipItem, select, selected}">
            <v-chip v-bind="attrs"
                    :input-value="selected"
                    close
                    label
                    @click="select"
                    @click:close="item.daysToWeekend = item.daysToWeekend.filter(el => el !== chipItem.value)">
              {{ chipItem.label }}
            </v-chip>
          </template>
        </v-select>
        <v-select :label="$i18n.t('shiftType.daysFromWeekend')"
                  :items="daysOfWeekend"
                  item-value="value"
                  item-text="label"
                  v-model="item.daysFromWeekend"
                  multiple>
          <template v-slot:selection="{attrs, item:chipItem, select, selected}">
            <v-chip v-bind="attrs"
                    :input-value="selected"
                    close
                    label
                    @click="select"
                    @click:close="item.daysFromWeekend = item.daysFromWeekend.filter(el => el !== chipItem.value)">
              {{ chipItem.label }}
            </v-chip>
          </template>
        </v-select>
        <validation-provider rules="periodsIntersects|periodsEmpty"
                             v-slot="{errors}">
          <periods-input :dense="true"
                         v-model="item.periods"
                         :errors="errors"/>
        </validation-provider>
        <select-mdi-icon :dense="true"
                         :label="$i18n.t('shiftType.icon')"
                         v-model="item.icon"/>
        <v-text-field v-model="item.label"
                      size="3"
                      maxlength="3"
                      :label="$i18n.t('shiftType.label')"
                      dense/>

      </template>
      <template #item.daysToWeekend="{item}">
        {{ item.daysToWeekend.join(', ') }}
      </template>
      <template #item.daysFromWeekend="{item}">
        {{ item.daysFromWeekend.join(', ') }}
      </template>
      <template #item.periods="{item}">
        {{ item.periods.map(period => formatPeriod(period)).join("; ") }}
      </template>
      <template #item.icon="{item}">
        <v-icon dense>{{ item.icon }}</v-icon>
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
import {DateTimeFormatter, Duration, LocalDate, LocalDateTime, LocalTime} from "@js-joda/core";
import PeriodsInput from "@/components/shiftType/PeriodsInput";
import {isPeriodsIntersects} from "@/components/shiftType/period";
import SelectMdiIcon from "@/components/shiftType/SelectMdiIcon";
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
      const result = axios.get(REST_BASE_URL + "/rest/shiftTypes/search/findByName", {
        params: {search: value}
      }).then((resp) => {
            const valid = !(resp.data._embedded.shiftTypes.filter((item) => item.id !== currentID).length > 0);
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

Validator.extend('periodsIntersects', {
  validate: (value) => {
    if (value) {
      const result = {"valid": !isPeriodsIntersects(value).length};
      return result;
    }
  },
  message: function () {
    return "Периоды пересекаются"
  }
})
Validator.extend('periodsEmpty', {
  validate: (value) => {
    if (value) {
      const result = {"valid": !value.find(p => !p.start || !p.duration)};
      return result;
    }
  },
  message: function () {
    return "В наличии пустой период"

  }
})
export default {
  methods: {
    formatPeriod(p) {
      const start = LocalDateTime.of(LocalDate.now(), LocalTime.parse(p.start))
      const end = start.plus(Duration.parse(p.duration))
      const formatter = DateTimeFormatter.ofPattern("HH:mm")
      return start.format(formatter) + "-" + end.format(formatter)
    },
    formatTime(str) {
      const time = LocalTime.parse(str)
      return time.format(DateTimeFormatter.ofPattern("HH:mm"))
    },
    formatDuration(str) {
      if (str) {
        const d = Duration.parse(str)
        const hours = String(d.toHours()).padStart(2, '0');
        const minutes = String(d.toMinutes() % 60).padStart(2, '0');
        const result = hours + ":" + minutes
        return result
      }
      return null
    }
  },
  components: {SelectMdiIcon, PeriodsInput, CrudPage, ValidationProvider},
  data() {
    return {
      item: null,
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
          text: 'icon',
          align: 'start',
          sortable: true,
          value: 'icon',
        }, {
          text: 'label',
          align: 'start',
          sortable: true,
          value: 'label',
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
      daysOfWeekend: [{
        value: 0, label: "0 Days"
      }, {
        value: 1, label: "1 Days"
      }, {
        value: 2, label: "2 Days"
      }, {
        value: 3, label: "3 Days"
      }, {
        value: 4, label: "4 Days"
      }, {
        value: 5, label: "5 Days"
      }, {
        value: 6, label: "6 Days"
      }]
    }
  },
  name: "ShiftTypePage",
  watch: {}
}
</script>

<style scoped>

</style>
