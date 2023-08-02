<template>
  <div class="container">
    <text-input
      type="number"
      pattern="\d*"
      ref="phoneNumber"
      id="user-phone"
      placeholder="인증번호 입력"
      maxlength="6"
      v-model="number"
      @input="(...args) => this.onInputNumber([6, ...args])"
    />
  </div>
</template>
<script>
  import textInput from '@/components/common/input'

  export default {
    components: {
      textInput
    },
    data() {
      return {
        number: ''
      }
    },
    methods: {
      onInputNumber(args) {
        console.log(args)
        if (args[1].length > args[0]) {
          this.number = args[1].slice(0, args[0])
          this.$emit('handleCode', args[1].slice(0, args[0]))
        } else {
          this.number = args[1]
          this.$emit('handleCode', args[1])
        }
      },
      getTokenFcm(data) {
        this.$emit('token', data)
      }
    }
  }
</script>
