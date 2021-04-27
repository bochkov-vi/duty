<template>
  <v-card >
    <v-card-title v-if="!(entity.new)">Редактирование объекта '{{ entity.fullName }}' ({{ entity.id }})</v-card-title>
    <v-card-title v-if="entity.new">Создание нового объекта</v-card-title>
    <v-container>
      <v-text-field
          dense
          label="Наименование"
          v-model="entity.name"
      ></v-text-field>
      <v-text-field
          dense
          label="Полное наименование"
          v-model="entity.fullName"
      ></v-text-field>
    </v-container>
    <v-card-text v-if="!entity.new">Создано:{{ formatedCreatedDate }}</v-card-text>
  </v-card>

</template>
<script>


export default {
  name: "RangInput",
  props: {
    entity: {
      type: Object,
      required: true,
      default: () => {
        return {name: null, fullName: null, createdDate: null, id: null, new: true}
      }
    }
  },
  data() {

    return {}
  },
  methods: {},
  computed: {
    formatedCreatedDate() {
      return new Date(this.entity.createdDate).toLocaleString()
    }
  },
  watch: {},
  mounted() {

  }
}
</script>

<style scoped>

</style>
