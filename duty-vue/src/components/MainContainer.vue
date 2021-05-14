<template>
  <div>
    <v-snackbar
        v-model="msg.type"
        multi-line
        outlined
        color="error"
        top
        right
        vertical
        v-for="msg in this.$store.state.messages"
        :key="msg.id"
        timeout="-1"
    >
      {{ msg.message }}
      <template v-slot:action="{ attrs }">
        <v-btn
            color="red"
            text
            v-bind="attrs"
            @click="removeMessage(msg)">
          <v-icon>
            mdi-close
          </v-icon>
        </v-btn>
      </template>
    </v-snackbar>
    <v-app-bar>
      <v-btn>
        <router-link to="/">Home</router-link>
      </v-btn>
      <v-btn>
        <router-link to="/rang">Rang</router-link>
      </v-btn>
      <v-btn>
        <router-link to="/group">EmployeeGroups</router-link>
      </v-btn>
      <v-btn @click="toggleLoading">
        <v-icon>mdi-cog-outline</v-icon>
      </v-btn>
      <v-progress-linear
          :active="loading"
          :indeterminate="loading"
          absolute
          bottom
          color="deep-purple accent-4"
      ></v-progress-linear>

    </v-app-bar>

    <v-container>
      <transition>
        <router-view></router-view>
      </transition>
    </v-container>
  </div>
</template>

<script>

import {getLoading, setLoading} from "@/store/loading";


export default {
  name: "MainContainer",
  data() {
    return {
      messageId: 1,
      messages: [{message: "Какая то ошибка", type: "error", id: 0}],
    }
  },
  methods: {
    removeMessage(msg) {
      this.$store.dispatch('REMOVE_MESSAGE', msg)
    },
    toggleLoading() {
      const value = !this.loading;
      this.loading = value
    }
  },
  computed: {
    loading: {
      get: function () {
        const loading = getLoading();
        return loading;
      },
      set: function (loading) {
        setLoading(loading)
      }
    }
  }
}
</script>

<style scoped>

</style>