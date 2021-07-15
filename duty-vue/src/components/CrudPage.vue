<template>
  <v-container>
    <v-dialog v-model="editMode" max-width="900">
      <ValidationObserver v-if="editMode"
                          v-slot="{ invalid }"
                          ref="validator">
        <v-form @submit.prevent="submit">
          <v-card class="pa-5">
            <v-container>
              <slot name="inputs" v-bind:item="item"></slot>
            </v-container>

            <div v-if="item.createdDate" class="text-body-2 font-weight-thin d-flex flex-row justify-space-around">
              <div>{{ $i18n.t('label.createdDate') }} {{ createdDate }}</div>
              <div>{{ $i18n.t('label.createdTime') }} {{ createdTime }}</div>
            </div>
            <v-card-actions>
              <v-spacer/>
              <v-btn type="submit"
                     :disabled="invalid"
                     small>
                <v-icon>mdi-content-save</v-icon>
                <span v-if="!(item.new)"
                      class="hidden-xs-only">{{ $i18n.t('label.save') }}</span>
                <span v-if="item.new"
                      class="hidden-xs-only">{{ $i18n.t('label.create') }}</span>
              </v-btn>

              <v-btn
                  outlined
                  color="secondary"
                  small
                  @click="item=null">
                <v-icon>mdi-cancel</v-icon>
                <span class="hidden-xs-only">{{ $t('label.cancel') }}</span>
              </v-btn>

            </v-card-actions>
          </v-card>
        </v-form>
      </ValidationObserver>
    </v-dialog>
    <v-dialog v-model="deleteMode">
      <v-card>
        <v-container>
          <slot name="details">
            <div class="d-flex flex-row justify-center " v-for="header in translatedHeaders" :key="header.value">
              <v-col cols="6">{{ header.text }}</v-col>
              <v-col cols="6">{{ getVal(itemToDelete, header.value) }}</v-col>
            </div>
          </slot>
        </v-container>
        <v-card-actions>
          <v-spacer/>
          <v-btn small
                 min-width="36"
                 @click="deleteItem">
            <v-icon>mdi-trash-can-outline</v-icon>
            {{ $i18n.t('label.delete') }}
          </v-btn>
          <v-btn
              outlined
              color="secondary"
              small
              @click="itemToDelete=null">
            <v-icon>mdi-cancel</v-icon>
            <span class="hidden-xs-only">{{ $t('label.cancel') }}</span>
          </v-btn>
        </v-card-actions>
      </v-card>

    </v-dialog>
    <v-data-table calculate-widths
                  :headers="[...translatedHeaders,{value:'actions'}]"
                  :items="page._embedded[resource]"
                  :options.sync="options"
                  :server-items-length="page.page.totalElements"
                  dense
                  :no-data-text="$i18n.t('label.no-data')"
                  class="mb-sm-10">
      <template v-for="header in headers"
                v-slot:[`item.${header.value}`]="{ item }">
        <slot :name="[`item.${header.value}`]"
              :item="item">
          {{ getVal(item, header.value) }}
        </slot>
      </template>
      <template v-slot:top>
        <v-toolbar flat>
          <v-btn small
                 @click="item=newitem">
            <v-icon>mdi-table-plus</v-icon>
            {{ $i18n.t('label.new-item') }}
          </v-btn>
        </v-toolbar>
      </template>
      <template v-slot:item.actions="{ item:row }">
        <v-icon @click="editItem(row)">mdi-pencil-box-outline</v-icon>
        <v-icon @click="itemToDelete=row">mdi-trash-can-outline</v-icon>
      </template>
    </v-data-table>

  </v-container>

</template>

<script>
import {ValidationObserver} from "vee-validate";
import {traverson_mixin} from "@/mixins/traverson_mixin"

export default {
  mixins: [traverson_mixin],
  methods: {
    getVal(item, path) {
      if (item)
        return path.split(".").reduce((res, prop) => {
          if (res)
            return res[prop]
          return null
        }, item);
      else return null
    },
    cancel() {
      this.item = null;
    },
    submit() {
      const valid = this.$refs.validator.validate();
      if (valid) {
        this.saveItem()
      }
    }
  },
  data() {
    return {}
  },
  props: {
    localePrefix: {
      required: true
    },
    headers: {
      required: true,
      type: Array
    },
    newitem: {
      default() {
        return {}
      }
    }
  },
  name: "CrudPage",
  computed: {
    translatedHeaders() {
      return this.headers.map((h) => {
        return {
          value: h.value,
          text: this.$i18n.t(this.localePrefix + "." + h.text),
          align: h.align,
          sortable: h.sortable
        }
      })
    }, createdDate() {
      return new Date(this.item.createdDate).toLocaleDateString()
    }, createdTime() {
      return new Date(this.item.createdDate).toLocaleTimeString()
    }
  },
  watch: {
    item:{
      deep:true,
      handler(val) {
        this.$emit("update:item", val)
      }
    }
  },
  components: {ValidationObserver}
}
</script>

<style scoped>

</style>
