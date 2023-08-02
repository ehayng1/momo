<template>
  <!-- eslint-disable-next-line vue/no-v-html -->
  <div :is="tag" v-html="contents"></div>
</template>

<script>
  import { unescape } from 'lodash'
  export default {
    props: {
      value: {
        required: true
      },
      tag: {
        type: String,
        default: 'div'
      }
    },
    computed: {
      contents: {
        get() {
          return this.value && this.$sanitize(unescape(this.value))
        },
        set(val) {
          this.value = val
        }
      }
    }
  }
</script>
