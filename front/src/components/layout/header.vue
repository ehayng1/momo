<template>
  <header>
    <div class="header_inner">
      <!-- <div class="img"></div> -->
      <router-link :to="{ name: 'Main' }">
        <img
          style="width: 100px; height: 26px"
          :src="require('@/static/images/main/logo.png')"
          alt=""
        />
      </router-link>

      <div class="header_nav">
        <!-- <div class="header_search">
          <router-link :to="{ name: 'Chat' }"><message /></router-link>
        </div> -->

        <div class="header_search">
          <router-link :to="{ name: 'Search' }"><search /></router-link>
        </div>
        <div class="header_search">
          <router-link :to="{ name: 'Alarm' }"><bell /></router-link>
        </div>

        <div class="members">
          <router-link v-if="this.login" :to="{ name: 'Mypage' }">
            <user />
          </router-link>

          <router-link
            v-else
            class="btn-login"
            :to="{ name: 'AccountStepOne' }"
          >
            로그인
          </router-link>
        </div>
      </div>
    </div>
  </header>
</template>
<script>
  import { mapGetters } from 'vuex'
  export default {
    components: {
      // message: () => import('@/static/images/common/message-svgrepo-com.svg'),
      // logo: () => import('@/static/images/main/logo3.png'),
      search: () => import('@/static/images/common/search.svg'),
      user: () => import('@/static/images/common/user.svg'),
      bell: () => import('@/static/images/common/Bell_light.svg')
    },
    computed: {
      ...mapGetters({
        // token: 'token',
        login: 'login'
      })
    }
  }
</script>
