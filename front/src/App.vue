<template>
  <div id="app">
    <router-view />
    <spinner :loading="this.$store.getters.LoadingStatus" />
  </div>
</template>

<script>
  import Spinner from './components/common/spinner'
  export default {
    components: {
      Spinner
    }
  }
</script>

<style lang="scss">
  @import '~@/static/scss/index';
</style>
