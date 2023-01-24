<template>
  <div class="main">
    <Header />

    <router-view />
    <nav class="main_tab">
      <div class="main_image">
        <div @click="goPage('Main')" class="main_image_container">
          <img
            :src="
              pathName === 'Main'
                ? require('@/static/images/main/bottomImage/homeB.png')
                : require('@/static/images/main/bottomImage/home.png')
            "
            alt=""
          />
          <br />
          <p
            :style="{
              color: pathName === 'Main' ? '#2D3238' : '#2D3238'
            }"
          >
            홈
          </p>
        </div>
      </div>
      <div class="main_image">
        <div @click="goPage('HumanResources')" class="main_image_container">
          <img
            :src="
              pathName === 'HumanResources'
                ? require('@/static/images/main/bottomImage/personB.png')
                : require('@/static/images/main/bottomImage/person.png')
            "
            alt=""
          />
          <br />
          <p
            :style="{
              color: pathName === 'HumanResources' ? '#2D3238' : '#2D3238'
            }"
          >
            일자리
          </p>
        </div>
      </div>
      <div class="main_image">
        <div @click="goPage('Equipment')" class="main_image_container">
          <img
            :src="
              pathName === 'Equipment'
                ? require('@/static/images/main/bottomImage/toolB.png')
                : require('@/static/images/main/bottomImage/tool.png')
            "
            alt=""
          />
          <br />
          <p
            :style="{
              color: pathName === 'Equipment' ? '#2D3238' : '#2D3238'
            }"
          >
            장비
          </p>
        </div>
      </div>
      <div class="main_image">
        <div @click="goPage('Labor')" class="main_image_container">
          <img
            :src="
              pathName === 'Labor'
                ? require('@/static/images/main/bottomImage/workB.png')
                : require('@/static/images/main/bottomImage/work.png')
            "
            alt=""
          />
          <br />
          <p
            :style="{
              color: pathName === 'Labor' ? '#2D3238' : '#2D3238'
            }"
          >
            노무
          </p>
        </div>
      </div>
      <div class="main_image">
        <div @click="goPage('Issue')" class="main_image_container">
          <img
            :src="
              pathName === 'Issue'
                ? require('@/static/images/main/bottomImage/issueB.png')
                : require('@/static/images/main/bottomImage/issue.png')
            "
            alt=""
          />
          <br />
          <p
            :style="{
              color: pathName === 'Issue' ? '#2D3238' : '#2D3238'
            }"
          >
            커뮤니티
          </p>
        </div>
      </div>
      <!-- <router-link :to="{ name: 'HumanResources' }">
        <fa
          :icon="['fa', 'building']"
          class="fa-xl"
          style="margin-bottom: 5px"
          :color="[pathName === 'HumanResources' ? '#00778B' : '#777']"
        />
        <br />
        일자리
      </router-link>
      <router-link :to="{ name: 'Equipment' }">
        <fa
          :icon="['fa', 'tools']"
          class="fa-xl"
          style="margin-bottom: 5px"
          :color="[pathName === 'Equipment' ? '#00778B' : '#777']"
        />
        <br />
        장비
      </router-link>
      <router-link :to="{ name: 'Labor' }">
        <fa
          :icon="['fa', 'address-card']"
          class="fa-xl"
          style="margin-bottom: 5px"
          :color="[pathName === 'Labor' ? '#00778B' : '#777']"
        />
        <br />
        노무
      </router-link>
      <router-link :to="{ name: 'Issue' }">
        <fa
          :icon="['fa', 'comment']"
          class="fa-xl"
          style="margin-bottom: 5px"
          :color="[pathName === 'Issue' ? '#00778B' : '#777']"
        />
        <br />
        이슈
      </router-link> -->
    </nav>
  </div>
</template>

<script>
  // import community from '@/static/images/main/bottomImage/community.svg'

  // @ is an alias to /src
  export default {
    name: 'Home',
    data() {
      return {
        pathName: '',
        isActive: false,
        fileName: ''
      }
    },
    components: {
      // community,
      Header: () => import('@/components/layout/header')
    },
    watch: {
      $route(to, from) {
        if (to.path !== from.path) {
          this.pathName = this.$router.history.current.name
        }
      }
    },
    mounted() {
      console.log(this.$router.history.current.name, '!23')
      this.pathName = this.$router.history.current.name
      console.log(this.pathName)
    },
    methods: {
      goPage(name) {
        this.fileName = name + 'B'
        this.$router.push({ name: name })
      }
    }
  }
</script>
