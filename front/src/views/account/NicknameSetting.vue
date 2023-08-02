<template>
  <div class="container">
    <text-input
      type="text"
      placeholder="닉네임(최대 10자)"
      v-model="nickname"
      @input="onInputName"
      maxlength="10"
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
        nickname: ''
      }
    },
    methods: {
      onInputName(value) {
        this.$emit('handleNickName', value)
        this.nickname = value
      }
    }
  }
</script>
