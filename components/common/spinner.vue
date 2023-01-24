<template>
  <div class="wrapper" v-if="loading">
    <div style="width: 100%">
      <div class="lds-facebook">
        <div></div>
        <div></div>
        <div></div>
      </div>
    </div>
  </div>
</template>
<script>
  export default {
    props: {
      loading: {
        type: Boolean,
        required: true
      }
    }
  }
</script>
<style>
  .wrapper {
    position: absolute;
    width: 100vw;
    height: 100vh;
    top: 0;
    left: 0;
    z-index: 9999;
    background-color: gray;
    opacity: 0.6;
    overflow: hidden;
    margin: 0 auto;

    position: fixed;
  }
  .lds-facebook {
    display: flex;
    align-items: center;
    justify-content: center;
    margin: 0 auto;
    /* display: inline-block; */
    position: absolute;
    width: 70px;
    height: 70px;
    /* background-color: red; */
    /* left: 47%; */
    /* top: 10%; */
    /* top: calc(100vw - 40%); */
    /* right: 3%; */
    left: calc(100vw - 57%);
    top: calc(100vh - 55%);
  }
  .lds-facebook div {
    display: inline-block;
    position: absolute;
    left: 6px;
    width: 13px;
    background: #42b883;
    animation: lds-facebook 1.2s cubic-bezier(0, 0.5, 0.5, 1) infinite;
  }
  .lds-facebook div:nth-child(1) {
    left: 6px;
    animation-delay: -0.24s;
  }
  .lds-facebook div:nth-child(2) {
    left: 26px;
    animation-delay: -0.12s;
  }
  .lds-facebook div:nth-child(3) {
    left: 45px;
    animation-delay: 0;
  }
  @keyframes lds-facebook {
    0% {
      top: 6px;
      height: 51px;
    }
    50%,
    100% {
      top: 19px;
      height: 25px;
    }
  }
</style>
