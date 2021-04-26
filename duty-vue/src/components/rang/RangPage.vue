<template>
  <v-container>

    <RangInput v-if="Object.keys(editedItem).length" :entity="editedItem" @update="onUpdate"
               @create="onUpdate"></RangInput>

    <v-btn small @click="newItem()">Новая строка</v-btn>
    <v-data-table
        calculate-widths
        :headers="headers"
        :items="rangs"
        :options.sync="options"
        :server-items-length="totalElements"
        dense
        :loading="loading"
        loading-text="Загрузка"
    >
      <template v-slot:item.createdDate="{ item }">
        <p>{{ new Date(item.createdDate).toLocaleDateString() }}</p>
      </template>
      <template v-slot:item.createdTime="{ item }">
        <p>{{ new Date(item.createdDate).toLocaleTimeString() }}</p>
      </template>
      <template v-slot:item.edit="{ item }">
        <div style="white-space: nowrap">
          <v-btn
              @click="editItem(item)"
              color="secondary"
              outlined
              min-width="24px"
              class="pa-0 mx-2"
              small>
            <v-icon small>mdi-pencil</v-icon>
          </v-btn>
          <v-btn
              @click="editItem(item)"
              color="red lighten-1"
              outlined
              min-width="24px"
              class="pa-0"
              small
          >
            <v-icon small>mdi-trash-can-outline</v-icon>
          </v-btn>
        </div>
      </template>

    </v-data-table>
  </v-container>
</template>

<script>
const rest_url = "http://localhost:8080/duty/rest/rangs?"
import RangInput from "@/components/rang/RangInput";
import axios from "axios";

export default {
  name: "RangPage",
  components: {RangInput},
  props: {
    items: {
      type: Object,
      default() {
        return {};
      }
    }
  },

  data: () => {
    return {
      loading: true,
      totalElements: 0,
      options: {},
      editedItem: {},
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
          text: "Время создания",
          value: "createdTime"
        }, {
          value: "edit"
        }, {
          value: "delete"
        }
      ], rangs: []


    }
  },
  methods: {
    editItem(item) {
      this.editedItem = item;
      console.log(this.editedItem)
    },
    onUpdate(/*item*/) {
      this.editedItem = {};
      this.getDataFromApi();
    },
    newItem() {
      this.editedItem = {new: true};
    },
    getDataFromApi() {
      this.loading = true;

      console.debug(this.url);
      axios.get(this.url)
          .then(response => {
            let items = response.data._embedded.rangs.map(function (item) {
              //item.createdDateAsString = new Date(item.createdDate).toLocaleDateString() + " " + new Date(item.createdDate).toLocaleTimeString();
              return item;
            });
            const total = response.data.page.totalElements;
            this.totalElements = total;
            this.rangs = items;
            this.loading = false;
          })
    }
  },
  computed: {
    url: function () {
      const {sortBy, sortDesc, page, itemsPerPage} = this.options
      let url = rest_url + "size=" + itemsPerPage + "&page=" + (page - 1);
      if (sortBy.length === 1 && sortDesc.length === 1) {
        url = url + "&sort=" + sortBy[0];
        if (sortDesc[0])
          url = url + ",desc";
      }
      return url;
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
