<template>
  <v-dialog v-model="show"
            max-width="600px">
    <v-card>
      <v-card-title class="headline">{{ $t('label.confirmDelete') }}</v-card-title>
      <v-card-text>{{ item }}</v-card-text>
      <v-container>
        <v-list v-if="Object.keys(item).length>1">
          <v-list-item v-for="k in fields" :key="k">
            <v-list-item-content>
              {{ $t(`${i18n_prefix}.${k}`) }} : {{ item[k] }} : {{ k }}
            </v-list-item-content>
          </v-list-item>
        </v-list>
      </v-container>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn outlined
               color="secondary"
               @click="close"
               small>
          <v-icon>mdi-cancel</v-icon>
          <span class="hidden-xs-only">{{ $t('label.cancel') }}</span>
        </v-btn>
        <v-btn outlined
               color="warning"
               @click="deleteItem(item)"
               small>
          <v-icon>mdi-trash-can-outline</v-icon>
          <span class="hidden-xs-only">{{ $t('label.delete') }}</span>
        </v-btn>
        <v-spacer></v-spacer>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>

export default {
  name: "DeleteDialog",
  props: {
    item: {
      required: true
    },
    i18n_prefix: {
      type: String,
      required: true
    },
    fields: {
      type: Array,
      default() {
        return []
      }
    }
  },
  methods: {
    close() {
      this.$emit("close")
    },
    deleteItem(item) {
      this.$emit("delete", item)
    }
  },
  computed: {
    show: {
      get() {
        return Object.keys(this.item).length > 0;
      }
    }
  }
}
</script>

<style scoped>

</style>