<template>
  <v-container>
    <validation-provider rules="required" v-slot="{ errors }">
      <v-text-field dense
                    label="Наименование"
                    :error-messages="errors"
                    v-model="item.name"/>
    </validation-provider>
    <validation-provider rules="required" v-slot="{ errors }">
      <v-text-field dense
                    label="Полное наименование"
                    v-model="item.fullName"
                    :error-messages="errors"/>
    </validation-provider>
  </v-container>
</template>

<script>
import {extend, ValidationObserver, ValidationProvider} from 'vee-validate';
import {required} from 'vee-validate/dist/rules';
import axios from "axios";
extend('required', {
  ...required,
  message: 'Это поле обязательно для заполнения'
});
extend('unique', {
  validate: (value) => {
    axios.get("http://localhost:8080/duty/rest/rangs/search/findByName", {
      params: {name: value}
    }).then(

    )
  },
  message: 'Запись с таким полем уже есть в базе'
})

export default {
  name: "Input",
  props: {
    item: {
      name: {
        type: String
      },
      fullName: {
        type: String
      }
    }
  },
  data() {
    return {}
  }
}
</script>

<style scoped>

</style>
