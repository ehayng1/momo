<template>
  <div class="dialog dialog_main" v-if="isShow">
    <div class="dialog_contents">
      <div class="dialog_body">
        <div class="dialog_title">{{ text }}</div>
      </div>
      <div class="dialog_foot">
        <button type="button" class="btn_dialog btn_primary" @click="check">
          확인
        </button>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'alertDialog',
    props: {
      text: {
        type: String,
        default: ''
      }
    },
    data() {
      return {}
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
      check() {
        //TODO 산재확인 api 연동후 성공하면 close하기.
        this.setClose()
      }
    }
  }
</script>

<style scoped></style>
