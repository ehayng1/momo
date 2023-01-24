<template>
  <header style="padding: 7px 0px">
    <div class="header_inner">
      <h1 class="logo" v-if="path !== ''">
        <router-link :to="{ name: path }">
          <close />
        </router-link>
      </h1>
      <h1 class="logo" v-else>
        <a href="javascript:history.go(-1)">
          <close />
        </a>
      </h1>
    </div>
  </header>
</template>
<script>
  import { mapGetters } from 'vuex'
  export default {
    components: {
      close: () => import('@/static/images/common/close.svg')
    },
    computed: {
      ...mapGetters({
        // token: 'token',
        login: 'login'
      })
    },
    props: {
      path: {
        type: String,
        default: ''
      }
    }
  }
</script>
