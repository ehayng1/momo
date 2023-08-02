<template>
  <header style="padding: 7px 0px">
    <div class="header_inner">
      <h1 class="logo" v-if="path !== ''">
        <router-link :to="{ name: path }">
          <back />
        </router-link>
        <!-- {{ path }} -->
      </h1>
      <h1 class="logo" v-else>
        <a href="javascript:history.go(-1)">
          <back />
        </a>
        {{ path }}
      </h1>
    </div>
  </header>
</template>
<script>
  import { mapGetters } from 'vuex'
  export default {
    components: {
      back: () => import('@/static/images/common/back.svg')
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
