<template>
  <v-container fluid>
    <v-card>
      <v-app-bar v-if="!entity.new">Редактирование объекта '{{ entity.fullName }}' ({{ entity.id }})</v-app-bar>
      <v-app-bar v-if="entity.new">Создание нового объекта</v-app-bar>
      <v-text-field
          label="Наименование"
          v-model="edited.name"
      ></v-text-field>
      <v-text-field
          label="Полное наименование"
          v-model="edited.fullName"
      ></v-text-field>
      <v-card-text v-if="!entity.new">Создано:{{ formatedCreatedDate }}</v-card-text>
    </v-card>
    <SaveButton :original="edited" @update="onUpdate" @create="onCreate"/>
  </v-container>
</template>
<script>
import SaveButton from "@/components/rang/SaveButton";

export default {
  name: "RangInput",
  components: {SaveButton},
  props: {
    entity: {
      type: Object,
      required: true
    }
  },
  data() {
    const {...copy} = this.entity
    return {
      edited: copy
    }
  },
  methods: {
    onUpdate: function (item) {
      this.$emit("update", item);
      this.item={};
    },
    onCreate: function (item) {
      this.$emit("create", item);
      this.item={};
    }
  },
  computed: {
    formatedCreatedDate() {
      return new Date(this.entity.createdDate).toLocaleString()
    }
  },
  watch: {
    entity: function (n) {
      const {...copy} = n
      this.edited = copy;
      console.log(copy);
    }
  },
  mounted() {
    this.name = this.entity.name;
    this.fullName = this.entity.fullName;
  }
}
</script>

<style scoped>

</style>
