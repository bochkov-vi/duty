<template>
  <v-container fluid>
    <v-card>
      <v-app-bar color="indigo accent-1">Редактирование объекта '{{ entity.fullName }}' ({{ entity.id }})</v-app-bar>
      <v-text-field
          label="Наименование"
          v-model="name"
      ></v-text-field>
      <v-text-field
          label="Полное наименование"
          v-model="fullName"
      ></v-text-field>
      <v-card-text>Создано:{{ formatedCreatedDate }}</v-card-text>
    </v-card>
    <div class="pa-5">
      <v-btn
          color="primary"
          outlined
          class="px-2"
          @click="saveItem(entity)">
        <v-icon>
          mdi-content-save-outline
        </v-icon>
        Сохранить
      </v-btn>
    </div>
  </v-container>
</template>

<script>
import axios from "axios";

export default {
  name: "RangInput",
  props: {
    entity: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      name: this.entity.name,
      fullName: this.entity.fullName
    }
  },
  methods: {
    saveItem(item) {
      console.log(item);
      axios.put(item._links.self.href, item)
          .then(response => {
            console.log(response);
          })
          .catch((e) => console.log(e));
    }
  },
  computed: {
    formatedCreatedDate() {
      return new Date(this.entity.createdDate).toLocaleString()
    }
  }, mounted() {
    this.name = this.entity.name;
    this.fullName=this.entity.fullName;
  }
}
</script>

<style scoped>

</style>
