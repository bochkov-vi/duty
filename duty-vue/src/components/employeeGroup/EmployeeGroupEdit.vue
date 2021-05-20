<template>
  <validation-observer v-slot="{ invalid }" ref="validator">
    <DeleteDialog
        :item="deleted"
        i18n_prefix="group"
        @delete="remove(deleted)"
        :fields="['id','name','createdDate']"
    />
    <form @submit.prevent="submit">
      <v-card>
        <v-card-title>{{ title }}</v-card-title>
        <v-container>
          <validation-provider :rules="{required:true,uniqueName:{id:entity.id}}"
                               v-slot="{ errors }">
            <v-text-field dense
                          :label="$t('group.name')"
                          :error-messages="errors"
                          v-model="entity.name"/>
          </validation-provider>
        </v-container>
        <v-card-actions>
          <v-btn
              outlined
              color="primary"
              small
              type="submit"
              :disabled="invalid">
            <v-icon v-if="!(entity.new)">mdi-content-save-outline</v-icon>
            <span v-if="!(entity.new)"
                  class="hidden-xs-only">{{ $t('label.save') }}</span>
            <v-icon v-if="entity.new">mdi-content-save-outline</v-icon>
            <span v-if="entity.new"
                  class="hidden-xs-only">{{ $t('label.create') }}</span>
          </v-btn>
          <v-btn
              outlined
              color="warning"
              v-if="!(entity.new)"
              small
              @click="deleted=entity">
            <v-icon>mdi-trash-can-outline</v-icon>
            <span class="hidden-xs-only">{{ $t('label.delete') }}</span>
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
    </form>
  </validation-observer>
</template>

<script>
import {restService} from "@/rest_crud_operations";
import {error, setLoading} from "@/store/store";
import * as Validator from "vee-validate";
import {ValidationObserver, ValidationProvider} from "vee-validate";
import axios from "axios";
import {required} from "vee-validate/dist/rules";
import {Trans} from "@/plugins/Translation";
import DeleteDialog from "@/components/employeeGroup/DeleteDialog";

Validator.extend('uniqueName', {
  params: ["id"],
  validate: (value, args) => {
    if (value) {
      const currentID = args.id;
      const result = axios.get("http://localhost:8080/duty/rest/employeeGroups/search/findByName", {
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
    return 'Запись с таким полем уже есть в базе'
  }
})
Validator.extend('required', {
  ...required,
  message: 'Это поле обязательно для заполнения'
});
const service = restService("http://localhost:8080/duty/rest/employeeGroups")
export default {
  name: "EmployeeGroupEdit",
  props: {
    id: {
      required: true
    }
  },
  data() {
    return {
      entity: {},
      deleted: {}
    }
  },
  methods: {
    submit() {
      this.$refs.validator.validate();
      service.save(this.entity).then(saved => {
        this.entity = saved;
        this.$router.push(Trans.i18nRoute({path: "."}))
      }).catch(e => {
        error(e)
      })

    },
    load() {
      setLoading(true)
      if (this.id < 0) {
        this.entity = {new: true}
        setLoading()
      } else {
        service.get(this.id).then(loaded => {
          this.entity = loaded
          setLoading()
        }).catch((e) => {
          setLoading()
          error(e)
        })
      }
    }, remove(item) {
      service.remove(item).then(() => this.$router.push(Trans.i18nRoute({path: "."})))
    }
  },
  computed: {
    title() {
      if (this.entity.new) {
        return "Создание новой записи"
      } else {
        return `Редактирование объекта '${this.entity.name}' (${this.entity.id})`
      }
    }
  },
  mounted() {
    this.load()
  }, components: {
    ValidationProvider, ValidationObserver, DeleteDialog
  }
}
</script>

<style scoped>

</style>
