<template>
  <v-container>
    <v-container>
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
            <v-dialog v-model="dialog"
                      max-width="800px">
              <template v-slot:activator="{ on, attrs }">
                <v-btn small
                       @click="newItem()"
                       v-bind="attrs"
                       v-on="on">
                  <v-icon>mdi-table-plus</v-icon>
                  {{$t('label.new-item')}}
                </v-btn>
              </template>
              <validation-observer v-slot="{ invalid }" ref="validator">
                <form @submit.prevent="submit">
                  <v-card>
                    <v-card-title>{{ editTitle }}</v-card-title>
                    <v-container>
                      <validation-provider :rules="{required:true,uniqueName:{id:editedItem.id}}"
                                           v-slot="{ errors }">
                        <v-text-field dense
                                      :label="$t('rang.name')"
                                      :error-messages="errors"
                                      v-model="editedItem.name"/>
                      </validation-provider>
                      <validation-provider :rules="{required:true,uniqueFullName:{id:editedItem.id}}"
                                           v-slot="{ errors }">
                        <v-text-field dense
                                      :label="$t('rang.fullName')"
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
                          type="submit"
                          :disabled="invalid">
                        <v-icon v-if="!(editedItem.new)">mdi-content-save-outline</v-icon>
                        <span v-if="!(editedItem.new)"
                              class="hidden-xs-only">{{$t('label.save')}}</span>
                        <v-icon v-if="editedItem.new">mdi-content-save-outline</v-icon>
                        <span v-if="editedItem.new"
                              class="hidden-xs-only">{{$t('label.create')}}</span>
                      </v-btn>
                      <v-btn
                          outlined
                          color="warning"
                          v-if="!(editedItem.new)"
                          small
                          @click="confirmDelete(editedItem)">
                        <v-icon>mdi-trash-can-outline</v-icon>
                        <span class="hidden-xs-only">{{$t('label.delete')}}</span>
                      </v-btn>
                      <v-btn
                          outlined
                          color="secondary"
                          small
                          @click="cancel">
                        <v-icon>mdi-cancel</v-icon>
                        <span class="hidden-xs-only">{{$t('label.cancel')}}</span>
                      </v-btn>
                    </v-card-actions>
                  </v-card>
                </form>
              </validation-observer>
            </v-dialog>
            <v-dialog v-model="deletedDialog"
                      max-width="600px">
              <v-card>
                <v-card-title class="headline">{{$t('label.confirmDelete')}}</v-card-title>
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
                    <span class="hidden-xs-only">{{$t('label.delete')}}</span>
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


import * as Validator from 'vee-validate';
import {ValidationObserver, ValidationProvider} from 'vee-validate';
import {required} from 'vee-validate/dist/rules';
import axios from "axios";
import {getLoading, setLoading} from "@/store/store";
import {restService} from "@/rest_crud_operations";
import i18n from "@/i18n";

const service = restService("http://localhost:8080/duty/rest/rangs");
Validator.extend('required', {
  ...required,
  message: i18n.t('label.fieldIsRequired')
});
Validator.extend('uniqueName', {
  params: ["id"],
  validate: (value, args) => {
    if (value) {
      const currentID = args.id;
      const result = axios.get("http://localhost:8080/duty/rest/rangs/search/findByName", {
        params: {search: value}
      }).then((resp) => {
            const valid = !(resp.data._embedded.items.filter((item) => item.id !== currentID).length > 0);
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

Validator.extend('uniqueFullName', {
  params: ["id"],
  validate: (value, args) => {
    if (value) {
      const currentID = args.id;
      const result = axios.get("http://localhost:8080/duty/rest/rangs/search/findByFullName", {
        params: {search: value}
      }).then((resp) => {
            const valid = !(resp.data._embedded.items.filter((item) => item.id !== currentID).length > 0);
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

      editedItem: {},
      options: {},
      headers: [
        {
          text: i18n.t('rang.id'),
          align: 'start',
          sortable: true,
          value: 'id',
        }, {
          text: i18n.t('rang.name'),
          align: 'start',
          sortable: true,
          value: 'name',
        }, {
          text: i18n.t('rang.fullName'),
          align: 'start',
          sortable: true,
          value: 'fullName',
        },
        {
          text: i18n.t('rang.createdDate'),
          value: "createdDate"
        }, {
          text: i18n.t('rang.createdTime'),
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

    loadPage: function () {
      service.page(this.options).then(data => {
        this.page = data;
      })
    },
    editItem: function (item) {
      service.get(item.id).then((entity) => {
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
      service.remove(this.deletedItem).then(() => this.loadPage());
      this.deletedItem = {};
      this.editedItem = {};
    },
    submit: function () {
      this.saveItem(this.editedItem)
    },
    saveItem: function (item) {
      this.$refs.validator.validate();
      service.save(item).then(entity => {
        this.editedItem = entity;
        let index = -1;
        this.page._embedded.items.find((el, i) => {
          if (el.id === entity.id) {
            index = i;
            return true;
          }
        });
        if (index < 0) {
          this.page._embedded.items.unshift(entity);
        } else {
          const array = this.page._embedded.items;
          array[index] = entity;
          this.page._embedded.items = array;
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
    loading: {
      get: function () {
        return getLoading()
      },
      set: function (v) {
        setLoading(v)
      }
    },
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
    },
    editedItem: {
      handler() {
        if (this.$refs.validator)
          this.$refs.validator.reset()
      }
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
