<template>
  <nav class="sub_tab">
    <ul>
      <li
        v-for="(item, key) in list"
        :key="key"
        :class="[{ active: active(item) }, 'tab']"
        @click="$emit('input', item.id, item.category)"
      >
        {{ item.label }}
      </li>
    </ul>
    <slot name="btn"></slot>
  </nav>
</template>

<script>
  export default {
    name: 'tab',
    data() {
      return {
        value: 1
      }
    },
    props: {
      // id: Number,
      // label: String,
      // value: Number
      list: {
        type: Array,
        default: () => []
      },
      current: {
        type: Number,
        default: 1
      }
    },
    computed: {
      active() {
        return item => {
          return this.current === item.id
        }
      }
    },
    mounted() {
      this.value = this.current
    }
  }
</script>

<style scoped></style>
