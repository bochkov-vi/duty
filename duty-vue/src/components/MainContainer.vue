<template>
  <div>
    <v-app-bar>
      <v-btn>
        <router-link to="/">Home</router-link>
      </v-btn>
      <v-btn>
        <router-link to="/rang">Rang</router-link>
      </v-btn>
      <v-btn>
        <router-link to="/about">about</router-link>
      </v-btn>
    </v-app-bar>
    <v-container>
      <transition>
        <router-view></router-view>
      </transition>
    </v-container>
  </div>
</template>

<script>
export default {
  name: "MainContainer"
}
</script>

<style scoped>

</style>