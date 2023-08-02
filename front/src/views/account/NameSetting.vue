<template>
  <div class="container">
    <text-input
      type="text"
      placeholder="이름(최대 6자)"
      v-model="name"
      @input="onInputName"
      maxlength="6"
    />
  </div>
</template>
<script>
  export default {
    components: {
      textInput: () => import('@/components/common/input')
    },
    data() {
      return {
        name: ''
      }
    },
    methods: {
      onInputName(value) {
        this.$emit('handleName', value)
        this.nickname = value
      }
    }
  }
</script>
