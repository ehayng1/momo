<template>
  <section class="mypage page">
    <back :path="'Main'" />
    <div class="info">
      <h1 class="title">마이페이지</h1>
      <p class="phone">
        <a :href="`tel:${phone}`">{{ phone }}</a>
      </p>
      <!-- <p class="occupation">현장관리자</p> -->
      <div class="wallet">
        <div class="card">
          <div class="card_nickname">
            <div class="preview" style="min-width: 100px">
              <input
                @change="onChangeImages"
                placeholder=""
                type="file"
                ref="images"
                accept="image/*"
                hidden
              />

              <img
                ref="preview"
                :src="image"
                alt="ddd"
                style="width: 100px; height: 100px"
              />
            </div>
            <button
              class="imageButton"
              type="button"
              @click="onClickImageUpload"
            >
              {{ image ? '이미지 변경' : '이미지 등록' }}
            </button>
          </div>
          <div class="rightblock">
            <input :value="name" disabled />
            <!-- <chain class="icon" /> -->
            <span class="num" @click="doCopy">{{ blockchain }}</span>
            <div class="btn_class">
              <div style="display: flex; width: 100%; height: 100%">
                <input
                  type="text"
                  v-model="nickname"
                  placeholder="닉네임을 설정해주세요"
                  maxlength="15"
                />
                <button type="button" @click="changeNickName">적용</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <nav class="terms">
      <ul>
        <li class="bag">
          <router-link :to="{ name: 'WorkHistory' }">업무이력</router-link>
        </li>
        <li class="checked">
          <router-link :to="{ name: 'Application' }">나의 활동내역</router-link>
        </li>
        <li class="document">
          <router-link :to="{ name: 'IssueHistory' }">
            나의 커뮤니티
          </router-link>
        </li>
        <li class="heart">
          <router-link :to="{ name: 'LikeHistory' }">찜한 목록</router-link>
        </li>
      </ul>
    </nav>

    <nav class="terms">
      <ul>
        <li class="service"><a href="tel:010-5133-0379">고객센터</a></li>
        <li class="document">
          <a @click="getTerms(1)">이용약관</a>
        </li>
        <li class="lock">
          <a @click="getTerms(2)">개인정보 취급방침</a>
        </li>
      </ul>
    </nav>

    <button type="button" class="logout_btn" @click="logout">로그아웃</button>
    <modal :termsContent="termsContent" />
  </section>
</template>
<script>
  // import chain from '@/static/images/common/chain.svg'
  import api from '@/api/api'
  import back from '@/components/layout/back'
  import modal from '@/components/popup/modal'

  export default {
    // components: { chain, back, modal },
    components: { back, modal },
    data() {
      return {
        basic: require('@/static/images/common/chainUser2.png'),
        blockchain: '',
        phone: '',
        name: '',
        nickname: '',
        termsContent: '',
        changeName: '',
        image: '',
        photo: ''
      }
    },
    mounted() {
      this.getInfo()
    },

    methods: {
      goPage(text) {
        this.$router.push({ name: text })
      },
      async onClickImageUpload() {
        this.$refs.images.click()
      },
      //image 미리보기
      async onChangeImages(e) {
        if (e.target.files && e.target.files[0]) {
          const reader = new FileReader()

          reader.onload = e => {
            this.$refs.preview.src = e.target.result
          }

          console.log(this.photo)
          try {
            this.photo = e.target.files[0]
            reader.readAsDataURL(e.target.files[0])

            const config = {
              headers: {
                'Content-Type': 'multipart/form-data',
                'Access-Control-Allow-Origin': '*',
                authorization: `Bearer ${this.$store.getters.token}`
              }
            }
            let formData = new FormData()
            if (this.photo.size !== undefined) {
              formData.append('user_image', this.photo)
            }

            await api
              .post(`mypage/myprofilephoto`, formData, config)
              .then(result => {
                console.log(result)
                if (result.data.success) {
                  this.$dialog.show({
                    type: 'alert',
                    title: '사진이 성공적으로 변경되었습니다'
                  })
                } else {
                  this.showAlert(result.data.message)
                }
              })
              .catch(err => console.log(err))
          } catch (err) {
            console.log(err)
          }
        }
      },
      onInput($event) {
        this.nickname = $event.target.value // const v = e.target

        // const v = e.target.value
        // console.log(e)
      },
      async changeNickName() {
        try {
          const config = {
            headers: {
              'Content-Type': 'application/json; charset = utf-8',
              'Access-Control-Allow-Origin': '*',
              authorization: `Bearer ${this.$store.getters.token}`
            }
          }
          const body = {
            nickname: this.nickname
          }
          await api
            .post('auth/open/nickname', body, config)
            .then(result => {
              console.log(body)
              if (result.data.success) {
                this.$dialog.show({
                  type: 'alert',
                  title: '닉네임이 성공적으로 변경되었습니다'
                })
              } else {
                this.$dialog.show({
                  type: 'alert',
                  title: result.data.message
                })
              }
            })
            .catch(err => console.log(err))
        } catch (err) {
          console.log(err)
        }
      },

      async getInfo() {
        console.log(this.$store.getters.token)
        if (this.$store.getters.token === '') {
          this.$dialog.show({
            type: 'alert',
            title: '로그인정보가 없습니다'
          })
          // .$on('dialog:confirm', () => {
          //   this.$router.push({ name: 'Main' })
          // })
        } else {
          try {
            const config = {
              headers: {
                'Content-Type': 'application/json; charset = utf-8',
                'Access-Control-Allow-Origin': '*',
                authorization: `Bearer ${this.$store.getters.token}`
              }
            }
            await api
              .get('mypage/info', config)
              .then(result => {
                console.log(result, '123')
                if (result.data.success) {
                  console.log('success')
                  if (
                    result.data.data !== undefined &&
                    result.data.data !== null
                  ) {
                    this.blockchain = result.data.data.result.code_blockchain
                    this.phone = result.data.data.result.user_phone
                    this.name = result.data.data.result.user_name
                    this.nickname = result.data.data.result.nickname
                    this.image = result.data.data.result.user_image
                    if (
                      !this.image.includes('/userImage') ||
                      this.image === null
                    ) {
                      this.image = this.basic
                    }
                  }
                } else {
                  this.$dialog.show({
                    type: 'alert',
                    title: result.data.message
                  })
                }
              })
              .catch(err => console.log(err))
          } catch (err) {
            console.log(err)
          }
        }
      },
      logout() {
        this.$store.dispatch('clearUser')
        this.$router.push({ name: 'Main' })
      },
      doCopy() {
        this.$copyText(this.blockchain).then(
          () => {
            this.$dialog.show({
              type: 'alert',
              title: '복사되었습니다.'
            })
          },
          () => {
            this.$dialog.show({
              type: 'alert',
              title: '복사하지 못했습니다.'
            })
          }
        )
      },
      async getTerms(v) {
        try {
          const config = {
            headers: {
              'Content-Type': 'application/json; charset = utf-8',
              'Access-Control-Allow-Origin': '*',
              authorization: `Bearer ${this.token}`
            }
          }
          await api
            .get(`terms/open/detail/${v}`, config)
            .then(result => {
              console.log(result)
              if (result.data.success) {
                console.log('success')
                if (
                  result.data.data !== undefined &&
                  result.data.data !== null
                ) {
                  this.termsContent = result.data.data.result.term_content
                  console.log(this.termsContent)
                  this.$modal.show('terms')
                }
              } else {
                this.showAlert(result.data.message)
              }
            })
            .catch(err => console.log(err))
        } catch (err) {
          console.log(err)
        }
      }
    }
  }
</script>
