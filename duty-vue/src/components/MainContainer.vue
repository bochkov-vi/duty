<template>
  <div>
    {{ $store.state.messages }}
    <v-snackbar
        v-model="messages.length"
        multi-line
        outlined
        :color="msg.type"
        top
        right
        vertical
        v-for="msg in messages"
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
        <router-link :to="$i18nRoute({name:'home'})">{{ $t('home') }}</router-link>
      </v-btn>
      <v-btn>
        <router-link :to="$i18nRoute({name:'rangs'})">{{ $t('rangs') }}</router-link>
      </v-btn>
      <v-btn>
        <router-link :to="$i18nRoute({name:'groups'})">{{ $t('groups') }}</router-link>
      </v-btn>
      <v-btn>
        <router-link :to="$i18nRoute({name:'employees'})">{{ $t('employees') }}</router-link>
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

import {getLoading, setLoading} from "@/store/store";
import messages_mixin from "@/mixins/messages_mixin";


export default {
  mixins: [messages_mixin],
  name: "MainContainer",
  data() {
    return {}
  },
  methods: {
    toggleLoading() {
      this.error("test")
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
