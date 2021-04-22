<template>
  <v-container>
    <v-data-table calculate-widths
                  :headers="headers"
                  :items="rangs"
                  :options.sync="options"
                  :server-items-length="totalElements"
                  dense
                  :loading="loading"
                  loading-text="Загрузка"
    >
      <template v-slot:item.edit="{ item }">
        <v-btn
            @click="editItem(item)"
            color="secondary"
            outlined
            min-width="34px"
            class="pa-0"
        >
          <v-icon small>mdi-pencil</v-icon>
        </v-btn>
      </template>

    </v-data-table>
  </v-container>
</template>

<script>
import {AXIOS} from "@/httpclient.js";

export default {
  name: "RangPage",


  props: {
    items: {
      type: Object
    }
  },
  data: () => {
    return {
      loading: true,
      totalElements: 0,
      options: {},
      headers: [
        {
          text: 'Код',
          align: 'start',
          sortable: true,
          value: 'id',
        }, {
          text: 'Наименование',
          align: 'start',
          sortable: true,
          value: 'name',
        }, {
          text: 'Полное наименование',
          align: 'start',
          sortable: true,
          value: 'fullName',
        },
        {
          text: "Дата создания",
          value: "createdDate"
        }, {
          value: "edit"
        }
      ], rangs: []


    }
  },
  methods: {
    getDataFromApi() {
      this.loading = true;
      const {sortBy, sortDesc, page, itemsPerPage} = this.options
      let url = "rangs?size=" + itemsPerPage + "&page=" + (page - 1);
      if (sortBy.length === 1 && sortDesc.length === 1) {
        url = url + "&sort=" + sortBy[0];
        if (sortDesc[0])
          url = url + ",desc";
      }
      console.debug(url);
      AXIOS.get(url)
          .then(response => {
            let items = response.data._embedded.rangs.map(function (item) {
              item.createdDate = new Date(item.createdDate).toLocaleDateString() + " " + new Date(item.createdDate).toLocaleTimeString();
              return item;
            });
            const total = response.data.page.totalElements;
            this.totalElements = total;
            this.rangs = items;
            this.loading = false;
          })
    }
  },
  mounted() {
    this.getDataFromApi();
  },
  watch: {
    options: {
      handler() {
        this.getDataFromApi()
      },
      deep: true,
    },
  },

}
</script>

<style scoped>

</style>