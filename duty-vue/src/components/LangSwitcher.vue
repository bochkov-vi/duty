<template>
  <v-select :items="langs" v-model="lang"></v-select>
</template>

<script>

import {i18n} from "@/i18n/i18n";

export default {
  name: "LangSwitcher",
  data() {
    return {
      langs: ["ru", "en"]
    }
  }, computed: {
    lang: {
      get() {
        return i18n.locale
      },
      set(lang) {
        i18n.locale = lang
      }
    }
  }

}
</script>

<style scoped>

</style>
