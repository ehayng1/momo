<template>
  <div class="main">
    <Header />

    <router-view />
    <nav class="main_tab">
      <div class="main_image">
        <div @click="goPage('Main')" class="main_image_container">
          <img
            :src="
              pathName === 'Main' ||
              pathName === 'FreeBoard' ||
              pathName === 'WorkBoard'
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
                ? require('@/static/images/main/bottomImage/openChatB.png')
                : require('@/static/images/main/bottomImage/openChat.png')
            "
            alt=""
          />
          <br />
          <p
            :style="{
              color: pathName === 'HumanResources' ? '#2D3238' : '#2D3238'
            }"
          >
            오픈채팅
          </p>
        </div>
      </div>
      <div class="main_image">
        <div
          @click="
            pathName === 'FreeBoard'
              ? goPage('IssueRegistration')
              : pathName === 'WorkBoard'
              ? goPage('OpeningRegistration')
              : openModal()
          "
          class="main_image_container"
        >
          <img
            :src="require('@/static/images/main/bottomImage/write.png')"
            alt=""
          />
          <br />
          <p
            :style="{
              color: pathName === 'Labor' ? '#2D3238' : '#2D3238'
            }"
          >
            글쓰기
          </p>
        </div>
      </div>
      <div class="main_image" style="">
        <!-- <router-link
          :to="{ name: 'Chat' }"
        > -->
        <div @click="goPage('Message')" class="main_image_container">
          <img
            :src="
              pathName === 'Message'
                ? require('@/static/images/main/bottomImage/messageB.png')
                : require('@/static/images/main/bottomImage/message.png')
            "
            alt=""
          />
          <br />
          <p
            :style="{
              color: pathName === 'Labor' ? '#2D3238' : '#2D3238'
            }"
          >
            1:1 대화
          </p>
        </div>
        <!-- </router-link> -->
        <!-- <div @click="goPage('Labor')" class="main_image_container">
          <img
            :src="
              pathName === 'Labor'
                ? require('@/static/images/main/bottomImage/messageB.png')
                : require('@/static/images/main/bottomImage/message.png')
            "
            alt=""
          />
          <br />
          <p
            :style="{
              color: pathName === 'Labor' ? '#2D3238' : '#2D3238'
            }"
          >
            메세지
          </p>
        </div> -->
      </div>
      <div class="main_image">
        <div @click="goPage('Mypage')" class="main_image_container">
          <img
            :src="
              pathName === 'Mypage'
                ? require('@/static/images/main/bottomImage/issueB.png')
                : require('@/static/images/main/bottomImage/issue.png')
            "
            alt=""
          />
          <br />
          <p
            :style="{
              color: pathName === 'Mypage' ? '#2D3238' : '#2D3238'
            }"
          >
            나의모모
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
    <Modal :show="showModal" @close="closeModal">
      <!-- Modal content goes here -->

      <div
        style="font-size: 1.5rem; padding-bottom: 2rem"
        @click="goPage('IssueRegistration')"
      >
        자유게시판
      </div>
      <div style="font-size: 1.5rem" @click="goPage('OpeningRegistration')">
        일자리게시판
      </div>
    </Modal>
  </div>
</template>

<script>
  // import messageB from '@/static/images/main/bottomImage/messageB.svg'
  import Modal from '../../components/common/Modal.vue'
  // @ is an alias to /src
  export default {
    name: 'Home',
    data() {
      return {
        pathName: '',
        isActive: false,
        fileName: '',
        showModal: false
      }
    },
    components: {
      // community,
      // messageB,
      Modal,
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
      this.pathName = this.$router.history.current.name
      console.log(this.pathName)
    },
    methods: {
      goPage(name) {
        this.fileName = name + 'B'
        this.$router.push({ name: name })
      },
      openModal() {
        this.showModal = true
      },
      closeModal() {
        this.showModal = false
      }
    }
  }
</script>
