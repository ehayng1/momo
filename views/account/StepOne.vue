<template>
  <div class="container">
    <text-input
      type="tel"
      placeholder="휴대전화번호 입력"
      v-model="phoneNumber"
      @input="onInputPhoneNumber"
      maxlength="13"
      minlength="12"
    />
    <!-- <input
      type="text"
      v-model="fcm_token"
      @input="onTokenChange"
      @change="onTokenChange"
      @
      id="temp"
    /> -->
  </div>
</template>
<script>
  export default {
    components: {
      textInput: () => import('@/components/common/input')
    },
    data() {
      return {
        phoneNumber: '',
        fcm_token: ''
      }
    },
    methods: {
      onInputPhoneNumber(value) {
        this.$emit('handlePhone', this.$options.filters.phoneValidation(value))
        this.phoneNumber = this.$options.filters.phoneValidation(value)
      }
      // onTokenChange(value) {
      //   this.$emit('handleToken', value)
      //   this.fcm_token = value
      // }
    }
  }
</script>
