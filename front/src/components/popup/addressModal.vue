<template>
  <div class="modal">
    <div class="overlay" @click="$emit('close-modal')"></div>
    <div class="modal-card">
      <!-- <slot /> -->
      <vue-daum-postcode />
    </div>
  </div>
</template>

<script>
  //   import VueDaumPostcode from 'vue-daum-postcode'

  export default {
    // components: { VueDaumPostcode }
  }
</script>

<style>
  .modal,
  .overlay {
    width: 100%;
    height: 100%;
    position: fixed;
    left: 0;
    top: 0;
  }
  .overlay {
    opacity: 0.5;
    background-color: black;
  }
  .modal-card {
    position: relative;
    /* height: ; */
    overflow: scroll;
    max-width: 500px;
    height: 550px;
    top: 50%;
    left: 50%;
    right: auto;
    bottom: auto;
    transform: translate(-50%, -50%);

    text-align: center;
    padding: 20px;
    background-color: white;
    /* min-height: 500px; */
    z-index: 10;
    opacity: 1;
  }
</style>
