<template>
  <v-container>
    <v-data-table
        :v-model="!editMode"
        calculate-widths
        :headers="translatedHeaders"
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
      <template v-slot:item.actions="{ row }">
        <router-link :to="$i18nRoute({name:editRouteName,params:{id:row.id}})">
          <v-icon>mdi-pencil-box-outline</v-icon>
        </router-link>
        <v-icon @click="item=row">mdi-trash-can-outline</v-icon>
      </template>
    </v-data-table>
  </v-container>
</template>

<script>
import {restService} from "@/rest_crud_operations";

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
    id: null,
    uri: {
      required: true
    },
    localePrefix: {
      required: true
    },
    headers: {
      required: true,
      type: Array
    }
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
    resetItem() {
      this.item = {}
    },
    editItem(item) {
      this.item = item
    },
    saveItem() {
      this.service.save(this.item).then((saved) => this.item = saved)
    },
    deleteItem() {
      this.service.remove(this.item).then(() => this.resetItem())
    },
    deleteConfirmed() {
      this.service.remove(this.item).then(() => this.resetItem())
    },
    loadItem(id) {
      if (id)
        this.service.get(id).then((loaded) => this.item = loaded)
    },
    refreshItem() {
      this.id = this.item.id
      this.loadItem()
    },
    loadPage() {
      this.service.page(this.options).then(data => this.page = data)
    }
  }, mounted() {
    this.service = restService(this.uri)
  }, watch: {
    options: {
      handler() {
        this.loadPage()
      },
      deep: true,
    },
    id: () => {
      if (this.id !== undefined) {
        if (this.id > 0) {
          this.loadItem(this.id)
          this.editMode = true
        } else if (this.id < -1) {
          this.item = {}
          this.editMode = true
        }
      } else {
        this.editMode = false;
        this.item = {}
      }
    }
  }
}
</script>

<style scoped>

</style>