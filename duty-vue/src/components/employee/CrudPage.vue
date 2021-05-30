<template>
  <v-container>
    <ValidationObserver v-if="item"
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
            <router-link :to="$i18nRoute({path:'.'})">
              <v-btn
                  outlined
                  color="secondary"
                  small>
                <v-icon>mdi-cancel</v-icon>
                <span class="hidden-xs-only">{{ $t('label.cancel') }}</span>
              </v-btn>
            </router-link>
          </v-card-actions>
        </v-card>
      </v-form>
    </ValidationObserver>
    <v-data-table
        v-if="!(item)"
        calculate-widths
        :headers="[...translatedHeaders,{value:'actions'}]"
        :items="page._embedded.items"
        :options.sync="options"
        :server-items-length="page.page.totalElements"
        dense
        :loading-text="$i18n.t('label.loading')"
        :no-data-text="$i18n.t('label.no-data')"
        class="mb-sm-10">
      <template
          v-for="header in headers"
          v-slot:[`item.${header.value}`]="{ item }"
      >
        <slot :name="[`item.${header.value}`]" :item="item">
          {{ getVal(item, header.value) }}
        </slot>
      </template>
      <template v-slot:top>
        <v-toolbar flat>
          <router-link :to="$i18nRoute({path:basePath+'-1'})">
            <v-btn small>
              <v-icon>mdi-table-plus</v-icon>
              {{ $i18n.t('label.new-item') }}
            </v-btn>
          </router-link>
        </v-toolbar>
      </template>
      <template v-slot:item.actions="{ item }">
        <router-link :to="$i18nRoute({path:`${basePath}/${item.id}`})">
          <v-icon>mdi-pencil-box-outline</v-icon>
        </router-link>
        <v-icon @click="item=row">mdi-trash-can-outline</v-icon>
      </template>

    </v-data-table>
  </v-container>
</template>

<script>
import {ValidationObserver} from "vee-validate";
import {error} from "@/store/store";
import axios from "axios";
import traverson from "traverson";
import JsonHalAdapter from "traverson-hal";

traverson.registerMediaType(JsonHalAdapter.mediaType, JsonHalAdapter);

export default {
  methods: {
    getVal(item, path) {
      return path.split(".").reduce((res, prop) => res[prop], item);
    },
    cancel() {
      this.$router.push({path: this.basePath})
    },
    submit() {
      const valid = this.$refs.validator.validate();
      if (valid) {
        this.save(this.item)
      }
    },
    save(item) {
      if (item) {
        let save ;
        if (item.new)
          save = this.rest.post(item)
              .then((saved) => {
                if (saved)
                  this.item = saved
                return saved;
              })
              .catch((e) => error(e))
        else
          save = this.rest.put(item._links.self.href, item)
              .then((saved) => {
                return saved
              })
              .catch((e) => error(e))
                
      }
    },
    remove(item) {
      this.rest.delete(item).then(() => this.$router.push({path: this.basePath}))
    },
    loadItemById(id) {
      if (id > 0)
        this.rest.get(id).then((resp) => {
          const data = resp.data;
          for (const prop in data._links) {
            if ("self" === prop || "item" === prop)
              continue
            const link = data._links[prop].href;
            data[prop] = null
            this.rest.get(link).then((resp) => {
              if (resp.data._embedded && resp.data._embedded.items) {
                data[prop] = resp.data._embedded.items.map((item) => item._links.self.href)
              } else {
                data[prop] = resp.data._links.self.href
              }
            }).catch(() => {
            })
          }
          this.item = data
        })
      else if (id < 0)
        this.item = {new: true}
      else
        this.item = null;
    },
    loadPage() {
      const sort = this.options.sortBy.map(function (srt, index) {
        let result = srt;
        if (this.options.sortDesc[index])
          result = result + ",desc";
        return result;
      });
      const params = {
        size: this.options.itemsPerPage,
        page: this.options.page - 1,
        sort: sort,
        projection: 'full-data'
      }
      this.rest.get("", {params: params}).then(resp => this.page = resp.data)
    },
    calculateEditMode() {
      return this.item
    },
    refreshItem() {
      this.loadItemById(this.$route.params.id)
    }
  }, created() {
    this.rest = axios.create({
      baseURL: this.url,
      paramsSerializer(params) {
        const searchParams = new URLSearchParams();
        for (const key of Object.keys(params)) {
          const param = params[key];
          if (Array.isArray(param)) {
            for (const p of param) {
              searchParams.append(key, p);
            }
          } else {
            searchParams.append(key, param);
          }
        }
        return searchParams.toString();
      }
    });
    this.basePath = this.$route.path.replace(/\/\d+/g, '')
  }, mounted() {
    this.refreshItem()
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
      editMode: false,
      rest: null,
      loading: false,
      basePath: null
    }
  },
  props: {
    url: {
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
    item: function () {
      this.calculateEditMode()
    },
    options: {
      handler() {
        this.loadPage()
      },
      deep: true,
    },
    '$route.params.id': function () {
      this.refreshItem()
    }
  },
  components: {ValidationObserver}
}
</script>

<style scoped>

</style>
