<template>
  <div class="container">
    <text-input
      type="tel"
      placeholder="휴대전화번호 입력"
      v-model="phoneNumber"
      @input="onInputPhoneNumber"
      maxlength="13"
      minlength="12"
    />
    <div class="loginBtnContainer">
      <!-- <img
        class="loginBtn"
        :src="require('@/static/images/common/kakao_login.png')"
        @click="kakaoLogin"
      /> -->
      <!-- 카카오로그인 -->
      <div
        style="
          background-color: rgb(254, 229, 2);
          width: 160px;
          height: 49px;
          border-radius: 10px;
          margin-bottom: 2rem;
        "
        @click="kakaoLogin"
      >
        <div
          style="
            color: rgb(24, 22, 0);
            font-size: 2rem;
            font-weight: 600;

            text-align: center;
            padding: 1rem;
          "
        >
          카카오로그인
        </div>
      </div>
      <!-- 네이버로그인 -->
      <div
        style="
          background-color: #03c75b;
          width: 160px;
          height: 49px;
          border-radius: 10px;
        "
        @click="naverLogin"
      >
        <div
          style="
            color: white;
            font-size: 2rem;
            font-weight: 600;

            text-align: center;
            padding: 1rem;
          "
        >
          네이버로그인
        </div>
      </div>
    </div>
    <!-- <div id="naverIdLogin"></div> -->
    <!-- <input
      type="text"
      v-model="fcm_token"
      @input="onTokenChange"
      @change="onTokenChange"
      @
      id="temp"
    /> -->
  </div>
</template>
<script>
  import api from '@/api/api'
  // import naverService from '@/utils/naverService'

  export default {
    components: {
      textInput: () => import('@/components/common/input')
    },
    data() {
      return {
        phoneNumber: '',
        fcm_token: ''
      }
    },
    mounted() {
      // console.log(naverService)
      // const naverIdLogin = naverService.setNaver()
      // window.naverIdLogin = naverIdLogin
      // const naver_id_login = new window.naver_id_login(
      //   `${process.env.VUE_APP_NAVER_CLIENT_ID}`,
      //   'http://localhost:3015/account/naver/1'
      // )
      // const state = naver_id_login.getUniqState()
      // naver_id_login.setButton('white', 2, 40)
      // naver_id_login.setState(state)
      // naver_id_login.init_naver_id_login()
    },
    methods: {
      onInputPhoneNumber(value) {
        this.$emit('handlePhone', this.$options.filters.phoneValidation(value))
        this.phoneNumber = this.$options.filters.phoneValidation(value)
      },
      async kakaoLogin() {
        try {
          const config = {
            headers: {
              'Content-Type': 'multipart/form-data',
              'Access-Control-Allow-Origin': '*'
            }
          }
          await api
            .get(`auth/open/kakaoLogin`, config)
            .then(result => {
              console.log(result)
              if (result.data.success) {
                window.open(result.data.data, '_self')
              } else {
                this.showAlert(result.data.message)
              }
            })
            .catch(err => console.log(err))
        } catch (err) {
          console.log(err)
        }
      },
      async naverLogin() {
        const state = 'test'
        // const callback = 'http://localhost:3015/account/naver/1' // 로컬
        const callback = 'https://web.buildmomo.com/account/naver/1' // 실서버
        const url = `https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=${process.env.VUE_APP_NAVER_CLIENT_ID}&redirect_uri=${callback}&state=${state}`

        console.log('==================url====================')
        console.log(url)

        window.location.href = url
        // try {
        //   const config = {
        //     headers: {
        //       'Content-Type': 'multipart/form-data',
        //       'Access-Control-Allow-Origin': '*'
        //     }
        //   }
        //   await api
        //     .get(`auth/open/kakaoLogin`, config)
        //     .then(result => {
        //       console.log(result)
        //       if (result.data.success) {
        //         window.open(result.data.data, '_self')
        //       } else {
        //         this.showAlert(result.data.message)
        //       }
        //     })
        //     .catch(err => console.log(err))
        // } catch (err) {
        //   console.log(err)
        // }
      }
      // onTokenChange(value) {
      //   this.$emit('handleToken', value)
      //   this.fcm_token = value
      // }
    }
  }
</script>
