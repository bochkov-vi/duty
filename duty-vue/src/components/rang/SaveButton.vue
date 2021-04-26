<template>

    <v-btn
        small
        class="px-2"
        @click="saveItem(original)">
      <v-icon>
        mdi-content-save-outline
      </v-icon>
      <span v-if="!original.new">Сохранить</span>
      <span v-if="original.new">Создать</span>
    </v-btn>

</template>

<script>
import axios from "axios";

export default {
  name: "SaveButton",
  props: {
    original: {
      required: true,
      type: Object
    }
  },
  methods: {
    async saveItem(item) {
      if (item) {
        if (!item.new)
          axios.put(item._links.self.href, item)
              .then(response => {
                Object.assign(this.original, response.data)
                this.$emit("update", item)
              })
              .catch((e) => console.log(e));

        else
          await axios.post("http://localhost:8080/duty/rest/rangs", item)
              .then(response => {
                Object.assign(this.original, response.data)
                this.$emit("create", item)
              }).catch((e) => console.log(e));
      }
    }
  }

}
</script>

<style scoped>

</style>