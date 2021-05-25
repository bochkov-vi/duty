<template>
  <v-container>
    <ValidationObserver v-if="item.id"
                        v-slot="{ invalid }"
                        ref="validator">
      <v-form @submit.prevent="submit">
        <v-card>
          <v-container>
            <slot name="inputs" v-bind:item="item"></slot>
          </v-container>
          <v-card-actions>
            <v-btn type="submit"
                   :disabled="invalid"
                   small>
              <v-icon>mdi-content-save</v-icon>
              <span v-if="!(item.new)"
                    class="hidden-xs-only">{{ $i18n.t('label.save') }}</span>
              <span v-if="item.new"
                    class="hidden-xs-only">{{ $i18n.t('label.create') }}</span>
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-form>
    </ValidationObserver>
    <v-data-table
        v-if="!(editMode)"
        calculate-widths
        :headers="[...translatedHeaders,{value:'actions'}]"
        :items="items._embedded.items"
        :options.sync="options"
        :server-items-length="items.page.totalElements"
        dense
        :loading-text="$i18n.t('label.loading')"
        :no-data-text="$i18n.t('label.no-data')"
        class="mb-sm-10">
      <template v-slot:top>
        <v-toolbar flat>
          <router-link :to="$i18nRoute({name:editRouteName,params:{id:-1}})">
            <v-btn small>
              <v-icon>mdi-table-plus</v-icon>
              {{ $i18n.t('label.new-item') }}
            </v-btn>
          </router-link>
        </v-toolbar>
      </template>
      <template v-slot:item.actions="{ item }">
        <router-link :to="$i18nRoute({name:editRouteName,params:{id:item.id}})">
          <v-icon>mdi-pencil-box-outline</v-icon>
        </router-link>
        <v-icon @click="item=row">mdi-trash-can-outline</v-icon>
      </template>
    </v-data-table>
  </v-container>
</template>

<script>
import {restService} from "@/rest_crud_operations";
import {ValidationObserver} from "vee-validate";
import {error} from "@/store/store";

export default {
  data() {
    return {
      item: {},
      items: {_embedded: {}, page: {}},
      options: {},
      service: {},
      editMode: false
    }
  },
  props: {
    uri: {
      required: true
    },
    localePrefix: {
      required: true
    },
    headers: {
      required: true,
      type: Array
    }, requestParams: null
  },
  name: "CrudPage",
  computed: {
    editRouteName() {
      return this.$route.name.replace(/\..*/g, '') + ".edit";
    },
    translatedHeaders() {
      return this.headers.map((h) => {
        return {
          value: h.value,
          text: this.$i18n.t(this.localePrefix + "." + h.text),
          align: h.align,
          sortable: h.sortable
        }
      })
    }
  }, methods: {
    submit() {
      const valid = this.$refs.validator.validate();
      if (valid) {
        this.saveItem()
      }
    },
    resetItem() {
      this.item = {}
    },
    editItem(item) {
      this.item = item
    },
    saveItem() {
      this.service.save(this.item)
          .then((saved) => {
            if (saved)
              this.item = saved
          })
          .catch((e) => error(e))
    },
    deleteItem() {
      this.service.remove(this.item).then(() => this.resetItem())
    },
    deleteConfirmed() {
      this.service.remove(this.item).then(() => this.resetItem())
    },
    loadItem(id) {
      console.log(`Item load by id=${id}`)
      if (id > 0)
        this.service.get(id).then((loaded) => this.item = loaded)
      else if (id < 0)
        this.item = {id: null, new: true}
      else
        this.item = {};
    },
    refreshItem() {
      this.id = this.item.id
      this.loadItem()
    },
    loadPage() {
      this.service.page(this.options).then(data => this.items = data)
    },
    calculateEditMode() {
      if (this.item && Object.keys(this.item).length > 0) {
        this.editMode = true;
      } else {
        this.editMode = false;
      }
    }
  }, mounted() {
    this.service = restService(this.uri,this.requestParams)
    this.loadItem(this.$route.params.id)
  }, watch: {
    item: function () {
      this.calculateEditMode()
    },
    options: {
      handler() {
        this.loadPage()
      },
      deep: true,
    },
    '$route.params.id': function (id) {
      this.loadItem(id)
    }
  },
  components: {ValidationObserver}
}
</script>

<style scoped>

</style>
