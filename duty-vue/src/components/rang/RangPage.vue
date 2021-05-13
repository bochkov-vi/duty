<template>
  <v-container>


    <v-container>


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
        <template v-slot:top>
          <v-toolbar flat>
            <v-dialog v-model="dialog"
                      max-width="800px">
              <template v-slot:activator="{ on, attrs }">
                <v-btn small
                       @click="newItem()"
                       v-bind="attrs"
                       v-on="on">
                  <v-icon>mdi-table-plus</v-icon>
                  Новая строка
                </v-btn>
              </template>
              <validation-observer v-slot="{ invalid }">
                <v-card>
                  <v-card-title>{{ editTitle }}</v-card-title>
                  <v-container>
                    <validation-provider :rules="{required:true,uniqueName:{id:editedItem.id}}"
                                         v-slot="{ errors }">
                      <v-text-field dense
                                    label="Наименование"
                                    :error-messages="errors"
                                    v-model="editedItem.name"/>
                    </validation-provider>
                    <validation-provider :rules="{required:true ,uniqueFullName:{id:editedItem.id}}" v-slot="{ errors }">
                      <v-text-field dense
                                    label="Полное наименование"
                                    v-model="editedItem.fullName"
                                    :error-messages="errors"/>

                    </validation-provider>
                  </v-container>
                  <v-card-text v-if="!editedItem.new">Создано:{{ createdDate }}</v-card-text>
                  <v-card-actions>
                    <v-btn
                        outlined
                        color="primary"
                        small
                        @click="saveItem(editedItem)"
                        :disabled="invalid">
                      <v-icon v-if="!(editedItem.new)">mdi-content-save-outline</v-icon>
                      <span v-if="!(editedItem.new)"
                            class="hidden-xs-only">Сохранить</span>
                      <v-icon v-if="editedItem.new">mdi-content-save-outline</v-icon>
                      <span v-if="editedItem.new"
                            class="hidden-xs-only">Создать</span>
                    </v-btn>
                    <v-btn
                        outlined
                        color="warning"
                        v-if="!(editedItem.new)"
                        small
                        @click="confirmDelete(editedItem)">
                      <v-icon>mdi-trash-can-outline</v-icon>
                      <span class="hidden-xs-only">Удалить</span>
                    </v-btn>
                    <v-btn
                        outlined
                        color="secondary"
                        small
                        @click="cancel">
                      <v-icon>mdi-cancel</v-icon>
                      <span class="hidden-xs-only">Отменить</span>
                    </v-btn>
                  </v-card-actions>
                </v-card>
              </validation-observer>
            </v-dialog>
            <v-dialog v-model="deletedDialog"
                      max-width="600px">
              <v-card>
                <v-card-title class="headline">Подтвердите удаление объекта?</v-card-title>
                <v-card-actions>
                  <v-spacer></v-spacer>
                  <v-btn outlined
                         color="secondary"
                         @click="closeDelete"
                         small>
                    <v-icon>mdi-cancel</v-icon>
                    <span class="hidden-xs-only">Отменить</span>
                  </v-btn>
                  <v-btn outlined
                         color="warning"
                         @click="deleteItem"
                         small>
                    <v-icon>mdi-trash-can-outline</v-icon>
                    <span class="hidden-xs-only">Подтвердить удаление</span>
                  </v-btn>
                  <v-spacer></v-spacer>
                </v-card-actions>
              </v-card>
            </v-dialog>
          </v-toolbar>
        </template>
        <template v-slot:item.createdDate="{ item }">
          {{ new Date(item.createdDate).toLocaleDateString() }}
        </template>
        <template v-slot:item.createdTime="{ item }">
          {{ new Date(item.createdDate).toLocaleTimeString() }}
        </template>
        <template v-slot:item.actions="{ item }">
          <v-icon @click="editItem(item)">mdi-pencil-box-outline</v-icon>
          <v-icon @click="confirmDelete(item)">mdi-trash-can-outline</v-icon>
        </template>
      </v-data-table>
    </v-container>
  </v-container>
</template>

<script>


import createMethods from "@/rest_crud_operations";
import {extend, ValidationObserver, ValidationProvider} from 'vee-validate';
import {required} from 'vee-validate/dist/rules';
import axios from "axios";

extend('required', {
  ...required,
  message: 'Это поле обязательно для заполнения'
});
extend('uniqueName', {
  params: ["id"],
  validate: (value, args) => {
    return axios.get("http://localhost:8080/duty/rest/rangs/search/findByName", {
      params: {search: value}
    }).then(
        (resp) => {
          const valid = !(resp.data._embedded.rangs.filter(finded => finded.id !== args.id).length > 0);
          return {valid}
        }
    )
  },
  message: 'Запись с таким полем уже есть в базе'
})
extend('uniqueFullName', {
  params: ["id"],
  validate: (value, args) => {
    return axios.get("http://localhost:8080/duty/rest/rangs/search/findByFullName", {
      params: {search: value}
    }).then(
        (resp) => {
          const valid = !(resp.data._embedded.rangs.filter(finded => finded.id !== args.id).length > 0);
          return {valid}
        }
    )
  },
  message: 'Запись с таким полем уже есть в базе'
})
export default {
  name: "RangPage",
  props: {},
  data: () => {
    return {
      deletedItem: {},
      page: {
        _embedded: {},
        page: {}
      },
      loading: false,
      editedItem: {},
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

    addMessage(message, type) {
      this.$store.dispatch('ADD_MESSAGE', {message: message, type: type});
    },
    ...createMethods("http://localhost:8080/duty/rest/rangs"),
    loadPage: function () {
      this.restPage(this.options).then(data => this.page = data)
    },
    editItem: function (item) {
      this.restGet(item.id).then((entity) => {
        this.editedItem = entity
      })
    },
    cancel: function () {
      this.editedItem = {}
    },
    closeDelete: function () {
      this.deletedItem = {}
    },
    confirmDelete(item) {
      this.deletedItem = item
    },
    deleteItem: function () {
      this.restDelete(this.deletedItem).then(() => this.loadPage());
      this.deletedItem = {};
      this.editedItem = {};
    },

    saveItem: function (item) {
      this.restSave(item).then(entity => {
        this.editedItem = entity;
        let index = -1;
        this.page._embedded.rangs.find((el, i) => {
          if (el.id === entity.id) {
            index = i;
            return true;
          }
        });
        if (index < 0) {
          this.page._embedded.rangs.unshift(entity);
        } else {
          const array = this.page._embedded.rangs;
          array[index] = entity;
          this.page._embedded.rangs = array;
        }
        this.editedItem = {};
      }).catch(e => {
        console.log(e);
        this.addMessage(e, "error")
      })
    },
    newItem: function () {
      this.editedItem = {new: true}
    }

  },
  computed: {
    dialog: {
      get: function () {
        return Object.keys(this.editedItem).length > 0
      },
      set: function (value) {
        if (value) {
          this.editedItem = {new: true}
        } else {
          this.editedItem = {}
        }
      }
    },
    deletedDialog: {
      get: function () {
        return Object.keys(this.deletedItem).length > 0
      },
      set: function (value) {
        if (value) {
          this.deletedItem = {new: true}
        } else {
          this.deletedItem = {}
        }
      }
    }, createdDate() {
      return new Date(this.editedItem.createdDate).toLocaleString()
    }, editTitle() {
      return this.editedItem.new ? "Добавление новой записи" : `Редактирование объекта "${this.editedItem.fullName}" (${this.editedItem.id})`;
    }
  },
  mounted() {

  },
  watch: {
    options: {
      handler() {
        this.loadPage()
      },
      deep: true,
    }
  },
  components: {ValidationProvider, ValidationObserver}
}
</script>

<style scoped>
tbody tr:nth-of-type(odd) {
  background-color: rgba(0, 0, 0, .05);
}
</style>
