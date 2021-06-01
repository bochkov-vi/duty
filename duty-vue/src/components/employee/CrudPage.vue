<template>
  <v-container>
    <ValidationObserver v-slot="{ invalid }"
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
    <v-dialog v-model="showDelete">
      <v-card>
        <p v-for="header in headers" :key="header.value">{{ header.value }}:{{ getVal(item, header.value) }}</p>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
import {ValidationObserver} from "vee-validate";
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
    cancel() {
      this.$router.push({name: this.tableRouteName})
    },
    submit() {
      const valid = this.$refs.validator.validate();
      if (valid) {
        this.save(this.item)
      }
    },
    async save(item) {
      const routeId = this.$route.params.id
      const saved = await this.rest.save(item)
      if (saved.id !== routeId) {
        await this.$router.push({path: this.basePath, id: saved.id})
      }
    },
    deleteItem(item) {
      this.item = item
      this.showDelete = true
    },
    del(item) {
      this.rest.del(item).then(() => this.$router.push({path: this.basePath}))
    },
    loadItemById(id) {
      if (id && id > 0)
        this.rest.get(id).then((resp) => {
          this.item = resp
        })
      else
        this.item = {new: true}
    },
    loadPage() {
      this.rest.page(this.options).then(data => this.page = data)
    },
    refreshItem() {
      this.loadItemById(this.$route.params.id)
    }
  },
  created() {
    this.rest = restService(this.entityUri, {projection: "full-data"});
  }, mounted() {
    this.refreshItem()
  },
  data() {
    return {
      item: {new :true},
      rest: null,
      loading: false,
      showDelete: false
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
  name: "CrudPage",
  computed: {
    tableRouteName() {
      return this.$route.name.split(".")[0]
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
    showDelete(val) {
      if (!val) this.cancel()
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
