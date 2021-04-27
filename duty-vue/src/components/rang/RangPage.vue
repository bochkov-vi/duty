<template>
  <v-container>


    <v-container v-if="Object.keys(editedItem).length>0">

      <v-btn
          outlined
          color="primary"
          small
          @click="save(editedItem)">
        <v-icon v-if="!(editedItem.new)">mdi-content-save-outline</v-icon>
        <span v-if="!(editedItem.new)" class="hidden-sm-and-down">Сохранить</span>
        <v-icon v-if="editedItem.new">mdi-content-save-outline</v-icon>
        <span v-if="editedItem.new" class="hidden-sm-and-down">Создать</span>
      </v-btn>
      <v-btn
          outlined
          color="warning"
          v-if="!(editedItem.new)"
          small
          @click="deleteItem(editedItem)">
        <v-icon>mdi-trash-can-outline</v-icon>
        <span class="hidden-sm-and-down">Удалить</span>
      </v-btn>

      <RangInput class="mt-sm-3" :entity="editedItem"></RangInput>
    </v-container>

    <v-container>

      <v-btn small @click="newItem()">
        <v-icon>mdi-table-plus</v-icon>
        Новая строка
      </v-btn>

      <v-data-table
          calculate-widths
          :headers="headers"
          :items="page._embedded.rangs"
          :options.sync="options"
          :server-items-length="page.page.totalElements"
          dense
          :loading="loading"
          loading-text="Загрузка"
          no-data-text="Нет данных"
          class="mb-sm-10"
      >
        <template v-slot:item.createdDate="{ item }">
          <p>{{ new Date(item.createdDate).toLocaleDateString() }}</p>
        </template>
        <template v-slot:item.createdTime="{ item }">
          <p>{{ new Date(item.createdDate).toLocaleTimeString() }}</p>
        </template>
        <template v-slot:item.actions="{ item }">
          <v-icon @click="edit(item)">mdi-pencil-box-outline</v-icon>
          <v-icon @click="deleteItem(item)">mdi-trash-can-outline</v-icon>
        </template>
      </v-data-table>
    </v-container>
  </v-container>
</template>

<script>


import RangInput from "@/components/rang/RangInput";

export default {
  name: "RangPage",

  props: {},

  data: () => {
    return {
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
          text: "Время создания",
          value: "createdTime"
        }, {
          value: "actions"
        }
      ]
    }
  },
  methods: {
    loadPage: function () {
      this.$store.dispatch("RANGS_LOAD_PAGE", this.options);
    },
    edit: function (item) {
      this.$store.dispatch('RANGS_LOAD_CURRENT', item.id)
    },
    save: function (item) {
      this.$store.dispatch('RANGS_SAVE', item)
    },
    deleteItem: function (item) {
      this.$store.dispatch('RANGS_DELETE', item.id)
    },
    newItem: function () {
      this.$store.dispatch('RANGS_SET_CURRENT', {new: true})
    }
  },
  computed: {
    page() {
      const page = this.$store.state.RANGS.PAGE;
      return page;
    },
    loading() {
      const loading = this.$store.state.RANGS.LOADING;
      return loading;
    },
    editedItem() {
      const current = this.$store.state.RANGS.CURRENT;
      const copy = {};
      Object.assign(copy, current)
      return copy;
    },
  },
  mounted() {

  },
  watch: {
    options: {
      handler() {
        this.loadPage()
      },
      deep: true,
    },
  },
  components: {RangInput}
}
</script>

<style scoped>
tbody tr:nth-of-type(odd) {
  background-color: rgba(0, 0, 0, .05);
}
</style>
