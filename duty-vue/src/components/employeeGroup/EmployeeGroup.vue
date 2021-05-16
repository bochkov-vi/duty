<template>
  <v-container>
    <DeleteDialog
        :item="selected"
        i18n_prefix="rang"
        @delete="remove(selected)"
        @close="selected={}"
        :fields="headers.map(h=>h.value).filter((e)=>e!==undefined)"
    />
    <v-data-table
        calculate-widths
        :headers="headers"
        :items="page._embedded.items"
        :options.sync="options"
        :server-items-length="page.page.totalElements"
        dense
        :loading="loading"
        loading-text="Загрузка"
        no-data-text="Нет данных"
        class="mb-sm-10">
      <template v-slot:top>
        <v-toolbar flat>
          <router-link :to="$i18nRoute({name:'EmployeeGroupEdit',params:{id:-1}})">
            <v-btn small>
              <v-icon>mdi-table-plus</v-icon>
              Новая строка
            </v-btn>
          </router-link>
        </v-toolbar>
      </template>
      <template v-slot:item.createdDate="{ item }">
        {{ new Date(item.createdDate).toLocaleDateString() }}
      </template>
      <template v-slot:item.createdTime="{ item }">
        {{ new Date(item.createdDate).toLocaleTimeString() }}
      </template>
      <template v-slot:item.actions="{ item }">
        <router-link :to="$i18nRoute({name:'EmployeeGroupEdit',params:{id:item.id}})">
          <v-icon>mdi-pencil-box-outline</v-icon>
        </router-link>
        <v-icon @click="selected=item">mdi-trash-can-outline</v-icon>
      </template>
    </v-data-table>
  </v-container>
</template>

<script>
import {error, getLoading, setLoading} from "@/store/store";
import restService from "@/rest_crud_operations";
import i18n from "@/i18n";
import DeleteDialog from "@/components/employeeGroup/DeleteDialog";

const service = restService("http://localhost:8080/duty/rest/employeeGroups")
export default {
  name: "EmployeeGroup",
  components: {DeleteDialog},
  computed: {
    loading: {
      get: function () {
        return getLoading();
      }
    }
  },
  methods: {
    remove(item) {
      service.remove(item).then(() => {
        this.loadPage()
        this.selected = null;
      })
    },
    loadPage: function () {
      setLoading(true);
      service.page(this.options).then(data => {
        this.page = data;
        setLoading();
      }).catch((e) => {
            error(e)
            setLoading()
          }
      )
    }
  },
  watch: {
    options: {
      handler() {
        this.loadPage()
      },
      deep: true,
    },
  },
  data() {
    return {
      selected: {},
      options: {},
      page: {
        _embedded: {
          items: []
        },
        page: {
          totalElements: 0
        }
      },
      headers: [
        {
          text: i18n.t('group.id'),
          align: 'start',
          sortable: true,
          value: 'id',
        }, {
          text: i18n.t('group.name'),
          align: 'start',
          sortable: true,
          value: 'name',
        }, {
          text: i18n.t('group.createdDate'),
          value: "createdDate"
        }, {
          text: i18n.t('group.createdTime'),
          value: "createdTime"
        }, {
          value: "actions"
        }
      ]
    }
  }
}
</script>

<style scoped>

</style>