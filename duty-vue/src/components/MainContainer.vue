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
        timeout="-1">
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
      <v-app-bar-nav-icon @click.stop="drawer=!drawer" class="d-md-none"/>
      <div class="d-none d-md-flex">
        <v-btn :disabled="$route.name==='home'" @click="$router.push($i18nRoute({name:'home'}))">
          {{ $t('home') }}
        </v-btn>
        <v-btn :disabled="$route.name==='rangs'" @click="$router.push($i18nRoute({name:'rangs'}))">
          {{ $t('rangs') }}
        </v-btn>
        <v-btn :disabled="$route.name==='groups'" @click="$router.push($i18nRoute({name:'groups'}))">
          {{ $t('groups') }}
        </v-btn>
        <v-btn :disabled="$route.name==='employees'" @click="$router.push($i18nRoute({name:'employees'}))">
          {{ $t('employees') }}
        </v-btn>
        <v-btn :disabled="$route.name==='shiftTypes'" @click="$router.push($i18nRoute({name:'shiftTypes'}))">
          {{ $t('shiftTypes') }}
        </v-btn>
      </div>

    </v-app-bar>
    <v-progress-linear
        :active="loading"
        :indeterminate="loading"
        absolute
        bottom
        color="deep-purple accent-4"
    ></v-progress-linear>
    <v-navigation-drawer v-model="drawer"
                         absolute>
      <v-list nav
              dense>
        <v-list-item :disabled="$route.name==='home'" @click="$router.push($i18nRoute({name:'home'}))">
          <v-list-item-content>{{ $t('home') }}</v-list-item-content>
        </v-list-item>
        <v-list-item :disabled="$route.name==='rangs'" @click="$router.push($i18nRoute({name:'rangs'}))">
          <v-list-item-content>{{ $t('rangs') }}</v-list-item-content>
        </v-list-item>
        <v-list-item :disabled="$route.name==='groups'" @click="$router.push($i18nRoute({name:'groups'}))">
          <v-list-item-content>{{ $t('groups') }}</v-list-item-content>
        </v-list-item>
        <v-list-item :disabled="$route.name==='employees'" @click="$router.push($i18nRoute({name:'employees'}))">
          <v-list-item-content>{{ $t('employees') }}</v-list-item-content>
        </v-list-item>
        <v-list-item :disabled="$route.name==='shiftTypes'" @click="$router.push($i18nRoute({name:'shiftTypes'}))">
          <v-list-item-content>{{ $t('shiftTypes') }}</v-list-item-content>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>
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
    return {drawer: false}
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
