<template>
  <v-container>
    {{ entity.id }}
  </v-container>
</template>

<script>
export default {
  name: "RangEditPage",
  props: {
    entity: {
      type: Object,
      required: true
    }
  }
}
</script>

<style scoped>

</style>