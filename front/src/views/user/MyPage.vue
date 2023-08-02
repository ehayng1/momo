<template>
  <section class="mypage page" style="padding: 0px">
    <!-- <back :path="'Main'" /> -->

    <div style="padding: 24px">
      <div class="info">
        <!-- <p class="phone">
        <a :href="`tel:${phone}`">{{ phone }}</a>
      </p> -->
        <!-- <p class="occupation">현장관리자</p> -->
        <div style="margin-top: 5%; display: flex; flex-direction: row">
          <img
            ref="preview"
            :src="image"
            alt="ddd"
            style="width: 75px; height: 75px; background-size: contain"
          />
          <div
            style="
              display: flex;
              flex-direction: column;
              margin-left: 5%;
              margin-top: 2%;
            "
          >
            <div style="font-weight: bold; font-size: 22px">{{ name }}</div>
            <div style="color: #868e97; font-size: 14px; margin-top: 5%">
              양현우 / {{ phone }}
            </div>
          </div>
        </div>

        <button
          style="
            background-color: #eef1f5;
            width: 100%;
            margin-top: 5%;
            padding: 2%;
            border-radius: 10px;
            font-size: 18px;
            color: #2d3238;
          "
        >
          편집하기
        </button>
        <div
          style="
            display: flex;
            flex-direction: row;
            margin-top: 20px;
            margin-bottom: 15px;
            align-items: center;
          "
        >
          <!-- <div style="display: flex; flex-direction: column; flex: 1">
          <div
            style="
              display: flex;
              font-size: 16px;
              display: flex;
              flex-direction: row;
            "
          >
            <div>구직프로필</div>
            <div style="font-weight: 600; margin-left: 10px">미작성</div>
          </div>

          <div style="margin-top: 10px; color: #868e97; font-size: 13px">
            프로필을 작성하면 일자리 신청을 할 수 있어요
          </div>
        </div> -->
          <!-- <button
          style="
            padding: 0px 10px;
            height: 35px;
            background-color: #e8f5ff;
            border-radius: 6px;
            margin-right: 10px;
          "
        >
          <div style="color: #008ff6; font-size: 14px">작성하기</div>
        </button> -->
        </div>

        <!-- <div
        style="
          display: flex;
          flex-direction: row;
          padding-top: 5%;
          justify-content: center;
          align-items: center;
          font-size: 14px;
        "
      >
        <router-link
          :to="{ name: 'WorkHistory' }"
          style="
            padding: 5%;
            padding-right: 10%;
            padding-left: 10%;
            border-top: 1px solid #d3d9e0;
            flex-grow: 1;
          "
          >근로 이력</router-link
        >
        <router-link
          :to="{ name: 'Application' }"
          style="
            padding: 10%;
            padding-top: 5%;
            padding-bottom: 5%;
            flex-grow: 1;
            border-right: 1px solid #d3d9e0;
            border-top: 1px solid #d3d9e0;
            border-left: 1px solid #d3d9e0;
          "
          >작성 내역</router-link
        >
        <router-link
          :to="{ name: 'LikeHistory' }"
          style="
            flex-grow: 1;
            padding: 5%;
            padding-left: 10%;
            padding-right: 10%;
            border-top: 1px solid #d3d9e0;
          "
          >관심 목록</router-link
        >
      </div> -->

        <!-- <div class="wallet">
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
      </div> -->
      </div>

      <div
        style="
          color: #2d3238;
          font-size: 18px;
          font-weight: 600;
          margin-bottom: 2rem;
          padding-top: 20px;
        "
      >
        나의 활동
      </div>

      <nav class="">
        <ul>
          <!-- <li class="bag">
          <router-link :to="{ name: 'WorkHistory' }">근로이력</router-link>
        </li>
        <li class="checked">
          <router-link :to="{ name: 'Application' }">나의 활동내역</router-link>
        </li> -->
          <li class="document">
            <router-link :to="{ name: 'IssueHistory' }">
              내가 쓴 게시글
            </router-link>
          </li>
          <li class="openchat">
            <router-link :to="{ name: 'IssueHistory' }">
              스크랩한 게시글
            </router-link>
          </li>
          <!-- <li class="heart">
          <router-link :to="{ name: 'LikeHistory' }">찜한 목록</router-link>
        </li> -->
        </ul>
      </nav>

      <div
        style="
          color: #2d3238;
          font-size: 18px;
          font-weight: 600;
          margin-bottom: 2rem;
          margin-top: 4rem;
        "
      >
        기타
      </div>

      <nav class="">
        <ul>
          <li class="service"><a href="tel:010-5133-0379">고객센터</a></li>
          <li class="checked">
            <a @click="getTerms(1)">이용약관</a>
          </li>
          <li class="lock">
            <a @click="getTerms(2)">개인정보 취급방침</a>
          </li>
        </ul>
      </nav>

      <button
        type="button"
        class="logout_btn"
        @click="logout"
        style="font-weight: 500; margin-top: 4rem"
      >
        로그아웃
      </button>
      <button
        type="button"
        class="deleteAccount_btn"
        @click="logout"
        style="font-weight: 500; margin-bottom: 2rem"
      >
        회원탈퇴
      </button>
      <modal :termsContent="termsContent" />
    </div>
    <div class="main_footer">
      <div class="main_footer_content" style="">
        <div style="margin-bottom: 10rem">
          <div style="margin: 5rem 0px 0rem 0px">(주) 피앤에이치홀딩스</div>
          <p>주소 : 서울특별시 강남구 선릉로92길 28, 4층 4104호</p>
          <p>사업자번호 : 299-86-02219</p>
          <p>업태 : 정보통신업 종목 : 포털 및 기타 인터넷 정보 매개 서비스업</p>
          <p>TEL : 010-2111-3921</p>
          <p>Mail : gunseolmm@gmail.com</p>
        </div>
      </div>
    </div>
  </section>
</template>
<script>
  // import chain from '@/static/images/common/chain.svg'
  import api from '@/api/api'
  // import back from '@/components/layout/back'
  import modal from '@/components/popup/modal'

  export default {
    // components: { chain, back, modal },
    components: { modal },
    data() {
      return {
        border_style:
          'width: 84vw; height: 0.1rem; background-color: #eef1f5; margin-bottom: 1rem',
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
