<template>
  <div class="dialog dialog_main" v-if="isShow">
    <div class="dialog_contents">
      <div class="dialog-body">
        <div class="dialog_title">로그인 / 회원가입</div>
        <div class="dialog_message">
          건설모모 계정에 로그인 또는 회원가입 하려면 휴대전화번호를 입력하세요.
        </div>
        <text-input
          type="tel"
          placeholder="휴대전화번호 입력"
          maxlength="13"
          minlength="12"
        />
      </div>
      <div class="dialog_foot">
        <button type="button" class="btn_dialog btn_primary" @click="moveTo">
          다음
        </button>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'login',
    data() {
      return {
        slider: null
      }
    },
    components: {
      textInput: () => import('@/components/common/input')
    },
    mounted() {
      this.init()
    },
    methods: {
      init() {
        this.$on('dialog:open', () => {
          console.log('오픈하고 실행')
        })
      },
      setClose() {
        this.$dialog.close()
      },
      onClose() {
        this.setClose()
      },
      moveTo() {
        this.setClose()

        //TODO router를 못찾음.
        console.log('회원가입 페이지로 이동. 근데 router를 못찾음...')
        // this.$router.push({name : 'SignIn'})
      }
    }
  }
</script>

<style scoped></style>
