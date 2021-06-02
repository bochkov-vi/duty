<template>
  <v-container>
    <v-data-table v-if="!(item)"
                  calculate-widths
                  :headers="[...translatedHeaders,{value:'actions'}]"
                  :items="page._embedded.items"
                  :options.sync="options"
                  :server-items-length="page.page.totalElements"
                  dense
                  :loading-text="$i18n.t('label.loading')"
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
                 @click="()=>editById()">
            <v-icon>mdi-table-plus</v-icon>
            {{ $i18n.t('label.new-item') }}
          </v-btn>
        </v-toolbar>
      </template>
      <template v-slot:item.actions="{ item:row }">
        <v-btn small
               min-width="36"
               class="mr-1"
               @click="editById(row.id)">
          <v-icon>mdi-pencil-box-outline</v-icon>
        </v-btn>
        <v-btn small
               min-width="36">
          <v-icon @click="item=row">mdi-trash-can-outline</v-icon>
        </v-btn>
      </template>
    </v-data-table>
    <v-dialog v-model="item">
      <v-card>
        <p v-for="header in headers" :key="header.value">{{ header.value }}:{{ getVal(item, header.value) }}</p>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
import {restService} from "@/rest_crud_operations";


export default {
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
    del(item) {
      this.rest.del(item).then(() => this.$router.push({path: this.basePath}))
    },
    loadPage() {
      this.rest.page(this.options).then(data => this.page = data)
    },
    editById(id) {
      const config = {name: this.editRouteName}
      if (id)
        config.params = {id: id}
      this.$router.push(this.$i18nRoute(config))
    }
  },
  created() {
    this.rest = restService(this.entityUri, {projection: "full-data"});
  },
  data() {
    return {
      item: null,
      page: {
        _embedded: {
          items: []
        },
        page: {}
      },
      options: null,
      rest: null,
      loading: false
    }
  },
  props: {
    localePrefix: {
      required: true
    },
    headers: {
      required: true,
      type: Array
    },
    entityUri: {
      required: true,
      type: String
    }
  },
  name: "TablePage",
  computed: {
    editRouteName() {
      return this.$route.name + ".edit"
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
  },
  watch: {
    options: {
      handler() {
        this.loadPage()
      },
      deep: true,
    }
  }
}
</script>

<style scoped>

</style>
