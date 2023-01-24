<template>
  <div class="terms">
    <modal
      name="terms"
      :scrollable="true"
      :resizable="false"
      :height="'auto'"
      :width="'90%'"
      :minWidth="this.width * 0.8"
      :minHeight="this.height * 0.9"
    >
      <!-- <div
          style="width: 90%; background-color: brown; position: fixed"
          class="terms_close_modal"
        > -->
      <div class="terms_close_modal">
        <!-- X 버튼 -->
        <!-- <p style="position: fixed; color: red; cursor: pointer">x</p> -->
        <!-- <fa :icon="['fa', 'xmark']" /> -->
        <!-- <div style="width: 100%; display: flex; justify-content: right">
          <div @click="hide" class="close" />
        </div> -->
        <!--  -->
      </div>
      <!-- </div> -->
      <div style="padding-top: 40px" v-html="termsContent" @click="hide" />

      <!-- {{ termsContent }}  -->
    </modal>
  </div>
</template>

<script>
  export default {
    name: 'MyComponent',
    data() {
      return {
        width: screen.width,
        height: screen.height
      }
    },
    methods: {
      show() {
        this.$modal.show('terms')
      },
      hide() {
        this.$modal.hide('terms')
      }
    },
    mount() {
      this.show()
    },
    props: {
      termsContent: {
        type: String,
        default: ''
      }
    }
  }
</script>
