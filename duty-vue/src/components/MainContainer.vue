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
    }
  }
}
</script>

<style scoped>

</style>