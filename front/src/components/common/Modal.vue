<!-- Modal.vue -->
<template>
  <div class="mymodal" v-if="show">
    <div class="modal-overlay" @click="close"></div>
    <div class="modalContent">
      <slot></slot>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'Modal',
    props: {
      show: {
        type: Boolean,
        required: true
      }
    },
    methods: {
      close() {
        this.$emit('close')
      }
    }
  }
</script>

<style>
  /* Modal styles */
  .mymodal {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.6); /* Greyed out background */
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 999; /* Higher z-index to ensure the modal is on top of everything else */
  }

  .modalContent {
    position: fixed;
    bottom: 10%;
    left: 50%;
    background-color: #fff;
    cursor: pointer;
    padding-right: 10rem;
    padding-left: 1rem;
    padding-top: 2rem;
    padding-bottom: 2rem;
    border-radius: 10px; /* Rounded corners */
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.3); /* Add a box shadow for a slight elevation effect */
  }

  .modal-overlay {
    position: absolute;
    top: -20;
    left: 0;
    width: 100%;
    height: 100%;
  }
</style>
