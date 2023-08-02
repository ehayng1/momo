<template>
  <section id="signin">
    <back :path="'Main'" />
    <!-- 회원가입은 하나의 인덱스의 데이터를 관리합니다 토큰 그래서  router-view안에있는 ex)@queryTypes ... ...
    등이 함수인데 이것이 하위컴포넌트에서 상위컴포넌트로 데이터를 받을때 emit이라는 함수를 통해서 데이터를 받게됩니다
    emit을 통해 모든 관련 입력정보를 여기서관리합니다
    -->
    <progress-bar
      :max="step.max"
      :value="step.value"
      :style="{ visibility: step.value !== 0 ? 'visible' : 'hidden' }"
    />

    <div class="info">
      <h1 class="title">{{ info[step.value - 1].title }}</h1>
      <p>{{ info[step.value - 1].desc }}</p>
    </div>

    <router-view
      @QueryTypes="QueryTypes"
      @EquipName="EquipName"
      @EquipPhone="EquipPhone"
      @EquipOwnerName="EquipOwnerName"
      @EquipCategory="EquipCategory"
      @EquipAddress="EquipAddress"
      @EquipPhoto="EquipPhoto"
      @EquipYear="EquipYear"
      @handleName="handleName"
      @handlePhone="handlePhone"
      @sendCode="sendCode"
      @handleCode="handleCode"
      @checkCode="checkCode"
      @handleSelected="handleSelected"
      @handleService="handleService"
      @handleCheck="handleCheck"
      @token="getTokenFcm"
      @handleInitialPhoto="handleInitialPhoto"
      @handleToken="handleToken"
      @handleNickName="handleNickName"
      @handleRandNum="handleRandNum"
    />

    <input type="text" ref="temp" id="temp" hidden />

    <!-- <router-link class="big_btn" :to="{ name: info[step.value - 1].link.name }">
      {{ info[step.value - 1].link.title }}
    </router-link> -->
    <button class="big_btn" @click="movePage2">
      {{ info[step.value - 1].link.title }}
    </button>
  </section>
</template>

<script>
  import api from '@/api/api'
  import { mapGetters, mapActions } from 'vuex'
  import back from '@/components/layout/back'
  import axios from 'axios'
  // import { naverService() } from '@/utils/naverService'
  export default {
    name: 'Index',
    components: {
      progressBar: () => import('@/components/common/progressBar'),
      back
    },
    data() {
      return {
        deviceIos: navigator.userAgent.toLowerCase(),
        step: {
          max: 6,
          value: 1
        },
        info: [
          {
            title: '로그인 / 회원가입',
            desc: '휴대전화번호를 입력해주세요.',
            link: {
              title: '다음',
              name: 'AccountStepTwo'
            }
          },
          {
            title: '인증번호 입력',
            desc: `${this.phone} 전화번호로 발송된\n6자리 인증번호를 입력해 주세요.`,
            link: {
              title: '완료',
              name: 'AccountStepFour'
            }
          },
          {
            title: '약관 동의',
            desc: '서비스 이용을 위해 아래 동의가 필요해요.',
            link: {
              title: '완료',
              name: 'AccountNameSetting'
            }
          },
          {
            title: '이름을 입력해주세요.',
            desc: '서비스를 원활히 이용하기 위한\n실명을 입력해주세요.',
            link: {
              title: '완료',
              name: 'AccountNicknameSetting'
            }
          },
          {
            title: '닉네임을 입력해주세요.',
            desc: '커뮤니티에서 사용할\n닉네임을 입력해주세요. (최대 10자)',
            link: {
              title: '완료',
              name: 'AccountStepFive'
            }
          },
          {
            title: '가입이 완료되었어요!',
            desc: `${this.name}님의 정보는 블록체인으로부터 보호받고 있어요.\n마이페이지에서 언제든 확인이 가능해요.`,
            link: {
              title: '홈으로 가기',
              name: 'Main'
            }
          }
        ],
        phone: '',
        code: '',
        userExist: false,
        selected: [],
        service: [],
        checkAll: false,
        check1: false,
        check2: false,
        check3: false,
        check4: false,
        check5: false,
        equpiment: [],
        fcm_token: '',
        nickname: '',
        Query: '',
        userInfoPhoto: '',
        name: '',
        imagePaths: '',
        additionalCareer: '',
        additionalAddress: '',
        additionalDescription: '',
        additionalInfo: '',
        handleEquipName: '',
        handleEquipPhone: '',
        handleEquipOwnerName: '',
        handleEquipAddress: '',
        handleEquipCategory: '',
        handleEquipPhoto: '',
        handleEquipYear: '',
        queryType: '',
        randNum: '',
        token: '', // 카카오/네이버 토큰
        state: {
          code: this.$route.query.code,
          states: this.$route.query.state, // csrf 공격을 방지하기 위해 애플리케이션에서 생성한 상태 토큰값으로 URL 인코딩을 적용한 값을 사용
          access_token: '', // 발급받은 access_token 저장을 위한 변수
          refresh_token: '', // 발급받은 refresh_token 저장을 위한 변수
          userid: '',
          userpw: '',
          role: 'CUSTOMER',
          birth: '', // DB에 넣을 생일
          birthday: '', // 가공할 생일 정보
          birthyear: '', // 가공할 연도 정보
          nickname: '',
          phone: '',
          apiimageurl: ''
        }
      }
    },
    computed: {
      ...mapGetters({
        token: 'token',
        login: 'login'
      })
    },
    watch: {
      $route(v) {
        this.moveNext(v.path)
      }
    },
    mounted() {
      // alert(this.deviceIos)
      if (this.login) {
        this.$router.push({ name: 'Main' })
      }
      this.moveNext(this.$route.path)
      if (this.$route.path.includes('kakao')) {
        this.token = this.$route.query.code
        this.getSocialInfo('kakao')
      } else if (this.$route.path.includes('naver')) {
        this.token = this.$route.query.code
        this.getSocialInfo('naver')
      }
    },
    beforeDestroy() {
      // window.removeEventListener('getTokenFcm', this.receiveMessage)
    },
    methods: {
      ...mapActions({
        setUser: 'setUser',
        setWallet: 'setWallet'
      }),

      movePage() {
        if (this.$store.getters.LoadingStatus) {
          return false
        } else if (this.step.value === 1) {
          // 휴대폰번호 입력
          this.sendCode()
        } else if (this.step.value === 2) {
          // 인증코드 입력
          this.checkCode()
          // } else if (this.step.value === 3) {
          //   // 지역 선택 ... 23/3/10 사라짐 JY
          //   if (!this.selected.length) {
          //     this.showAlert('지역을 한 개이상 선택해주세요')
          //   } else {
          //     this.$router.push({
          //       name: this.info[this.step.value - 1].link.name
          //     })
          //   }
          // } else if (this.step.value === 4) {
          //   // 업종 선택 ... 23/3/10 사라짐 JY
          //   if (!this.service.length) {
          //     this.showAlert('업종을 선택해주세요')
          //   } else {
          //     if (Object.values(this.service).toString() === '장비주') {
          //       this.$router
          //         .push({
          //           name: 'EquipAndNoMuInfo',
          //           query: { type: Object.values(this.service).toString() }
          //         })
          //         .catch(() => {})
          //     } else if (Object.values(this.service).toString() === '노무사') {
          //       this.$router
          //         .push({
          //           name: 'NoMuSa',
          //           query: { type: Object.values(this.service).toString() }
          //         })
          //         .catch(() => {})
          //     } else {
          //       this.$router.push({
          //         name: this.info[this.step.value - 1].link.name
          //       })
          //     }
          //   }

          //   // else if(this.$route)
          //   // this.$router.push({ name: 'AccountStepFour' })
          // } else if (this.step.value === 5) {
          //   // 장비주 정보입력 ... 23/3/10 사라짐 JY
          //   // this.deviceIos.indexOf('iphone') > -1 ||
          //   //   this.deviceIos.indexOf('ipad') > -1 ||
          //   //   this.deviceIos.indexOf('ipod')
          //   if (
          //     !this.handleEquipName ||
          //     // !this.handleEquipPhoto ||
          //     !this.handleEquipPhoto.size ||
          //     // !this.handleEquipPhoto ||
          //     !this.handleEquipPhone ||
          //     !this.handleEquipCategory ||
          //     !this.handleEquipYear
          //   ) {
          //     // alert(this.handleEquipPhoto.size)
          //     this.showAlert('빈칸을 모두 기입해주세요')
          //     // console.log('123')
          //   } else {
          //     this.$router.push({
          //       query: { type: this.queryType },
          //       name: this.info[this.step.value - 1].link.name
          //     })
          //   }
          // } else if (this.step.value === 6) {
          //   // 장비주 정보 입력 ... 23/3/10 사라짐 JY
          //   console.log(
          //     this.handleEquipName,
          //     this.handleEquipPhone,
          //     this.handleEquipPhoto.size,
          //     this.handleEquipYear,
          //     this.handleEquipOwnerName,
          //     this.handleEquipCategory,
          //     this.handleEquipAddress
          //   )
          //   if (
          //     // this.handleEquipName
          //     !this.handleEquipName ||
          //     !this.handleEquipPhoto.size ||
          //     // !this.handleEquipPhoto.name === 'undefinded' ||
          //     // !this.handleEquipPhoto ||
          //     // this.handleEquipPhoto.size !== 'undefined' ||
          //     !this.handleEquipPhone ||
          //     !this.handleEquipOwnerName ||
          //     !this.handleEquipCategory ||
          //     !this.handleEquipAddress ||
          //     // !this.handleEquipOwnerName ||
          //     !this.handleEquipYear
          //   ) {
          //     this.showAlert('빈칸을 모두 기입해주세요')
          //   } else {
          //     this.$router.push({
          //       query: { type: this.queryType },
          //       name: this.info[this.step.value - 1].link.name
          //     })
          //   }
        } else if (this.step.value === 7) {
          this.QueryTypes()
          this.enroll()
        } else if (this.step.value === 8) {
          // this.userInfoNickName()
          this.userInfoPhotoFun()

          // this.$router.push({ name: this.info[this.step.value - 1].link.name })
        } else {
          this.$router.push({ name: this.info[this.step.value - 1].link.name })
        }
      },
      movePage2() {
        if (this.$store.getters.LoadingStatus) {
          return false
        } else if (this.step.value === 1) {
          // 휴대폰번호 입력
          this.sendCode()
        } else if (this.step.value === 2) {
          // 인증코드 입력
          this.checkCode()
        } else if (this.step.value === 3) {
          // 약관 동의
          this.checkTerms()
        } else if (this.step.value === 4) {
          // 이름 입력
          this.checkName()
        } else if (this.step.value === 5) {
          // 닉네임 입력
          this.checkNickname()
        } else if (this.step.value === 8) {
          // this.userInfoPhotoFun()
          this.$router.push({ name: 'Main' })
          // this.$router.push({ name: this.info[this.step.value - 1].link.name })
        } else {
          this.$router.push({ name: this.info[this.step.value - 1].link.name })
        }
      },
      checkTerms() {
        if (!this.check1 || !this.check2) {
          console.log(this.$route.query)
          this.showAlert('필수항목에 동의해주세요.')
        } else {
          this.$router.push({
            name: this.info[this.step.value - 1].link.name
          })
        }
      },
      checkName() {
        if (this.name.trim().length === 0) {
          this.showAlert('이름을 입력해주세요.')
        } else {
          this.$router.push({
            name: this.info[this.step.value - 1].link.name
          })
        }
      },
      async checkNickname() {
        if (this.nickname.trim().length === 0) {
          this.showAlert('닉네임을 입력해주세요.')
        } else {
          try {
            const config = {
              headers: {
                'Content-Type': 'multipart/form-data',
                'Access-Control-Allow-Origin': '*',
                authorization: `Bearer ${this.$store.getters.token}`
              }
            }
            await api
              .get(`auth/open/nickname/${this.nickname}`, config)
              .then(result => {
                console.log(result)
                if (result.data.data.cnt === 0) {
                  this.newEnroll()
                } else {
                  this.showAlert('중복된 닉네임입니다.')
                }
              })
          } catch (e) {
            console.log(e)
            this.showAlert(e)
          }
        }
      },
      async userInfoPhotoFun() {
        try {
          const config = {
            headers: {
              'Content-Type': 'multipart/form-data',
              'Access-Control-Allow-Origin': '*',
              authorization: `Bearer ${this.$store.getters.token}`
            }
          }
          let formData = new FormData()
          // if (this.userInfoPhoto.length < 1) {
          //   formData.append('user_image', this.test1)
          // } else {
          if (this.userInfoPhoto.size !== undefined) {
            formData.append('user_image', this.userInfoPhoto)
            // }
          }
          formData.append('nickname', this.nickname)
          formData.append('user_name', this.name)
          formData.append('randNum', this.randNum)
          if (this.name === '' || this.nickname === '') {
            return this.$dialog.show({
              type: 'alert', //confirm or alert
              title: '빈칸을 모두 입력해주세요!'
            })
          }
          for (let key of formData.keys()) {
            console.log(key, ':', formData.get(key))
          }
          console.log(config.headers)
          await api
            .post(`auth/open/userInfoPhoto`, formData, config)
            .then(result => {
              console.log(result)
              if (result.data.success) {
                this.$router.push({ name: 'Main' })
              } else {
                this.showAlert(result.data.message)
              }
            })
            .catch(err => console.log(err))
        } catch (err) {
          console.log(err)
        }
      },
      QueryTypes(v) {
        this.queryType = v
        console.log(v, '?')
      },
      EquipName(v) {
        this.handleEquipName = v
        console.log(v, 'bane')
      },
      EquipPhone(v) {
        this.handleEquipPhone = v
        console.log(v, 'phone')
      },
      EquipOwnerName(v) {
        this.handleEquipOwnerName = v
        console.log(v, 'handleEquipOwnerName')
      },
      EquipAddress(v) {
        this.handleEquipAddress = v

        console.log(v, 'handleEquipAddress')
      },
      EquipCategory(v) {
        this.handleEquipCategory = v
        console.log(v, 'handleEquipCategory')
      },
      EquipPhoto(v) {
        // setTimeout(() => {
        //   this.deviceIos.includes('iPhone') === true
        //   this.handleEquipPhoto = v
        // }, 100)
        console.log(v, 'v')
        this.handleEquipPhoto = v
      },
      EquipYear(v) {
        this.handleEquipYear = v

        console.log(v, 'handleEquipYear')
      },
      handlePhone(v) {
        console.log(v)
        this.phone = v
        this.fcm_token = this.$refs.temp.value
        this.info[1].desc = `${v} 전화번호로 발송된 6자리 인증번호를 입력해주세요.`
      },
      handleToken(v) {
        this.fcm_token = v
      },
      handleNickName(v) {
        this.nickname = v
      },
      handleInitialPhoto(v) {
        console.log(v, 'phto')
        this.userInfoPhoto = v
      },
      handleRandNum(v) {
        console.log(v, '123')
        this.randNum = v
      },
      async sendCode() {
        if (!this.$options.filters.phone(this.phone)) {
          this.showAlert('번호를 정확히 입력해주세요.')
          return false
        } else {
          try {
            const config = {
              headers: {
                'Content-Type': 'application/json; charset = utf-8',
                'Access-Control-Allow-Origin': '*',
                // 'Access-Control-Allow-Headers': '*',
                // 'Access-Control-Allow-Methods': '*',
                authorization: `Bearer ${this.token}`
              }
            }
            const body = {
              phone: this.phone
            }
            await api
              .post(`auth/open/checkUserExist`, body, config)
              .then(result => {
                console.log(result)
                if (result.data.success) {
                  console.log('success')
                  if (
                    result.data.data !== undefined &&
                    result.data.data !== null
                  ) {
                    this.userExist = result.data.data.exist
                    this.$router.push({
                      name: this.info[this.step.value - 1].link.name
                    })
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
      },
      handleCode(v) {
        this.code = v
        this.fcm_token = this.$refs.temp.value
      },
      async checkCode() {
        // alert(JSON.stringify(TOKEN_FCM))
        // console.log(JSON.stringify(TOKEN_FCM))
        // window.Android.check()
        if (this.code.length < 6) {
          this.showAlert('인증번호 6자리를 입력해주세요.')
        } else {
          try {
            const config = {
              headers: {
                'Content-Type': 'application/json; charset = utf-8',
                'Access-Control-Allow-Origin': '*',
                // 'Access-Control-Allow-Headers': '*',
                // 'Access-Control-Allow-Methods': '*',
                authorization: `Bearer ${this.token}`
              }
            }
            const body = {
              phone: this.phone,
              code: this.code,
              exist: `${this.userExist}`,
              fcm_token:
                this.fcm_token === undefined || this.fcm_token === null
                  ? ''
                  : this.fcm_token
            }
            await api
              .post(`auth/open/loginUser`, body, config)
              .then(result => {
                console.log(result)
                if (result.data.success) {
                  console.log('success')
                  if (
                    result.data.data !== undefined &&
                    result.data.data !== null
                  ) {
                    if (this.userExist) {
                      // 엑세스토큰 값 저장 후 메인으로 보내기
                      this.setUser(result.data.data)
                      this.$router.push({ name: 'Main' })
                    } else {
                      // msg처리 및 가입처리 해야함
                      // result.data.data.code => 0 : 가입, 1: 번호틀림, 2: 횟수 초과
                      if (result.data.data.code === '0') {
                        this.$router.push({
                          name: this.info[this.step.value - 1].link.name
                        })
                      } else {
                        this.showAlert(result.data.data.msg)
                      }
                    }
                  }
                } else {
                  this.showAlert('문자 발송 실패')
                }
              })
              .catch(err => console.log(err))
          } catch (err) {
            console.log(err)
          }
        }
      },
      handleName(V) {
        this.name = V
        this.info[
          this.info.length - 1
        ].desc = `${this.name}님의 정보는 블록체인으로부터 보호받고 있어요.\n마이페이지에서 언제든 확인이 가능해요.`
      },
      handleSelected(v) {
        this.selected = v
      },
      handleService(v) {
        this.service = v
      },

      handleCheck(v) {
        console.log(v)
        if (v === 0) {
          if (this.checkAll) {
            this.checkAll = false
            this.check1 = false
            this.check2 = false
            this.check3 = false
            this.check4 = false
            this.check5 = false
          } else {
            this.checkAll = true
            this.check1 = true
            this.check2 = true
            this.check3 = true
            this.check4 = true
            this.check5 = true
          }
        } else if (v === 1) {
          this.check1 = !this.check1
        } else if (v === 2) {
          this.check2 = !this.check2
        } else if (v === 3) {
          this.check3 = !this.check3
        } else if (v === 4) {
          this.check4 = !this.check4
        } else if (v === 5) {
          this.check5 = !this.check5
        }

        if (this.check1 && this.check2 && this.check3) {
          this.checkAll = true
        } else {
          this.checkAll = false
        }
      },
      async enroll() {
        if (
          !this.check1 || !this.check2 || this.$route.query.type === '노무사'
            ? !this.check4
            : !this.check5
        ) {
          console.log(this.$route.query)
          this.showAlert('필수항목에 동의해주세요.')
        } else {
          try {
            let location = ''
            this.selected.map((value, idx) => {
              if (idx === 0) {
                location = value
                console.log(location, 'location')
              } else {
                location = location + ':' + value
              }
            })
            let service = ''
            this.service.map((value, idx) => {
              if (idx === 0) {
                service = value
                console.log(service)
              } else {
                service = service + ' :' + value
              }
            })
            // const body = {
            //   phone: this.phone,
            //   location,
            //   check: this.check3 ? 'Y' : 'N',
            //   fcm_token:
            //     this.fcm_token === undefined || this.fcm_token === null
            //       ? ''
            //       : this.fcm_token,
            //   user_type: service
            // }
            let formData = new FormData()
            formData.append('phone', this.phone)
            formData.append('location', location)
            formData.append('check', this.check3 ? 'Y' : 'N')
            formData.append(
              'token',
              this.fcm_token === undefined || this.fcm_token === null
                ? ''
                : this.fcm_token
            )
            formData.append('user_type', this.service)
            formData.append('owner_name', this.handleEquipName)
            formData.append('owner_phone', this.handleEquipPhone)
            formData.append('equipname', this.handleEquipOwnerName)
            formData.append('owner_category', this.handleEquipCategory)
            formData.append('owner_address', this.handleEquipAddress)
            // 정훈님 코드
            // if (this.photo.size !== undefined) {
            //   formData.append('owner_image', this.photo)
            // }
            if (this.handleEquipPhoto.size !== undefined) {
              formData.append('owner_image', this.handleEquipPhoto)
            }
            formData.append('owner_career', this.handleEquipYear)
            formData.append('equip_spec', this.handleEquipYear)

            // console.log(body, 'body')
            for (let key of formData.keys()) {
              console.log(key, ':', formData.get(key))
            }
            const config = {
              headers: {
                'Content-Type': 'multipart/form-data',
                'Access-Control-Allow-Origin': '*',
                authorization: `Bearer ${this.$store.getters.token}`
              }
            }

            // console.log(formData, 'body')

            await api
              .post(`auth/open/enrollUser`, formData, config)
              .then(result => {
                console.log(result, '123')
                if (result.data.success) {
                  console.log('success')
                  if (
                    result.data.data !== undefined &&
                    result.data.data !== null
                  ) {
                    this.setUser(result.data.data.token)
                    this.setWallet(result.data.data.blockchain)

                    this.$router.push({
                      name: this.info[this.step.value - 1].link.name
                    })
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
      },
      async newEnroll() {
        try {
          let location = ''
          // const body = {
          //   phone: this.phone,
          //   location,
          //   check: this.check3 ? 'Y' : 'N',
          //   fcm_token:
          //     this.fcm_token === undefined || this.fcm_token === null
          //       ? ''
          //       : this.fcm_token,
          //   user_type: service
          // }
          let formData = new FormData()
          formData.append('phone', this.phone)
          formData.append('location', location)
          formData.append('check', this.check3 ? 'Y' : 'N')
          formData.append('name', this.name)
          formData.append('nickname', this.nickname)
          formData.append(
            'token',
            this.fcm_token === undefined || this.fcm_token === null
              ? ''
              : this.fcm_token
          )
          formData.append('user_type', this.service)

          const config = {
            headers: {
              'Content-Type': 'multipart/form-data',
              'Access-Control-Allow-Origin': '*',
              authorization: `Bearer ${this.$store.getters.token}`
            }
          }

          console.log(formData, 'body')

          await api
            .post(`auth/open/enrollUser`, formData, config)
            .then(result => {
              console.log(result, '123')
              if (result.data.success) {
                console.log('success')
                if (
                  result.data.data !== undefined &&
                  result.data.data !== null
                ) {
                  this.setUser(result.data.data.token)
                  this.setWallet(result.data.data.blockchain)

                  this.$router.push({
                    name: this.info[this.step.value - 1].link.name,
                    query: {
                      name: this.name,
                      nickname: this.nickname,
                      phone: this.phone
                    }
                  })
                }
              } else {
                this.showAlert(result.data.message)
              }
            })
            .catch(err => console.log(err))
        } catch (err) {
          console.log(err)
        }
      },
      onTokenChange(v) {
        this.fcm_token = v
      },
      moveNext(number) {
        let result = Number(this.$options.filters.numberValidation(number))
        if (typeof result === 'number' && this.step.max >= result) {
          this.step.value = result
        }
      },
      showAlert(msg) {
        this.$dialog.show({
          type: 'alert', //confirm or alert
          title: msg
        })
      },
      getTokenFcm(token) {
        alert(token)
      },
      async getSocialInfo(type) {
        if (type === 'kakao') {
          const config = {
            headers: {
              'Content-Type': 'application/json; charset = utf-8',
              'Access-Control-Allow-Origin': '*'
              // 'Access-Control-Allow-Headers': '*',
              // 'Access-Control-Allow-Methods': '*',
            }
          }
          const body = {
            code: this.$route.query.code
          }
          const url = 'auth/open/kakao/callback'
          try {
            await api
              .post(url, body, config)
              .then(result => {
                console.log(result, '123')
                if (result.data.success) {
                  console.log('success')
                  if (
                    result.data.data !== undefined &&
                    result.data.data !== null
                  ) {
                    // this.setUser(result.data.data.token)
                    // this.setWallet(result.data.data.blockchain)
                    // this.$router.push({
                    //   name: this.info[this.step.value - 1].link.name
                    // })
                    if (result.data.data.exist === 'false') {
                      let phone = result.data.data.phone
                      let name = result.data.data.name
                      if (phone !== undefined && phone !== null) {
                        phone = phone.replace('+82 ', '0')
                      } else {
                        phone = ''
                      }
                      if (name !== undefined && name !== null) {
                        this.name = name
                      } else {
                        this.name = ''
                      }
                      this.$dialog
                        .show({
                          type: 'alert',
                          message: '회원정보가 없어 가입을 진행합니다.'
                        })
                        .$on('dialog:confirm', () => {
                          this.phone = phone
                          this.nickname = result.data.data.nickname
                          if (this.phone !== '') {
                            this.$router.push({ name: this.info[2].link.name })
                          }
                        })
                    } else {
                      this.setUser(result.data.data)
                      this.$router.push({ name: 'Main' })
                    }
                  }
                } else {
                  this.showAlert(result.data.message)
                }
              })
              .catch(err => console.log(err))
          } catch (err) {
            console.log(err)
          }
        } else if (type === 'naver') {
          const url = `/oauth2.0/token?grant_type=authorization_code&client_id=${process.env.VUE_APP_NAVER_CLIENT_ID}&client_secret=${process.env.VUE_APP_NAVER_CLIENT_SECRET}&code=${this.state.code}&state=${this.state.states}`
          const header = {
            'X-Naver-Client-Id': process.env.VUE_APP_NAVER_CLIENT_ID,
            'X-Naver-Client-Secret': process.env.VUE_APP_NAVER_CLIENT_SECRET
          }
          const { data } = await axios.get(url, { header })
          console.log(data)
          this.naverInfo(data)
        }
      },
      async naverInfo(token) {
        const url = `/v1/nid/me`
        let header = 'Bearer ' + token.access_token
        const headers = { Authorization: header }
        console.log('headers => ', headers)
        const { data } = await axios.get(url, { headers })
        console.log('*****naverUserInfo data***** => ', data)
        if (data !== null && data !== undefined && data.message === 'success') {
          const res = data.response
          this.phone = res.mobile
          this.name = res.name
          this.nickname = res.nickname
          try {
            const config = {
              headers: {
                'Content-Type': 'application/json; charset = utf-8',
                'Access-Control-Allow-Origin': '*'
              }
            }
            const body = {
              phone_number: res.mobile
            }
            await api
              .post(`auth/open/naver/callback`, body, config)
              .then(result => {
                console.log(result)
                if (result.data.success) {
                  console.log('success')
                  if (
                    result.data.data !== undefined &&
                    result.data.data !== null
                  ) {
                    if (result.data.data.exist === 'false') {
                      this.$dialog
                        .show({
                          type: 'alert',
                          message: '회원정보가 없어 가입을 진행합니다.'
                        })
                        .$on('dialog:confirm', () => {
                          this.$router.push({
                            name: this.info[3].link.name
                          })
                        })
                    } else {
                      this.setUser(result.data.data)
                      this.$router.push({ name: 'Main' })
                    }
                  }
                } else {
                  this.showAlert('문자 발송 실패')
                }
              })
              .catch(err => console.log(err))
          } catch (err) {
            console.log(err)
          }
        }
      }
      // receiveMessage(data) {
      //   alert(JSON.stringify(data))
      //   console.log(JSON.stringify(data))
      //   this.fcm_token = data.data
      // }
    }
  }
</script>

<style scoped></style>
