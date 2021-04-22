<template>
  <v-app>
    <main-container></main-container>
  </v-app>
</template>

<script>
import MainContainer from "@/components/MainContainer";
import router from "@/router/router"

export default {
  name: 'App',
  router,
  components: {
    MainContainer
  },

  data: () => ({
    //
  }),
};
</script>
