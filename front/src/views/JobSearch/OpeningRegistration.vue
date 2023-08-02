<template>
  <section id="job_search">
    <back />
    <div class="info">
      <h1 class="title">사람 구해요</h1>
    </div>
    <form id="registrationForm" class="form" @submit.prevent="onSubmit">
      <dl class="col">
        <dt>현장 정보<sup>*</sup></dt>
        <dd style="">
          <text-input
            type="text"
            placeholder="현장 이름을 입력해주세요"
            v-model="siteName"
            required
          />
          <!-- <select-box text="현장명" :list="siteList" @change="handleSiteList" /> -->
        </dd>
      </dl>

      <div :style="border_style"></div>

      <dl class="col">
        <!-- <dt>현장 주소<sup>*</sup></dt> -->
        <!--        <dd><text-input placeholder="숫자만 입력" v-model="textInput"  @input="onInputNumber" /></dd>-->
        <!-- <dd>
          <select-box
            text="시/도"
            :list="area[0].list"
            @change="(...args) => this.changeArea([0, ...args])"
          />
          <select-box
            text="구/군"
            :list="area[1].list"
            @change="(...args) => this.changeArea([1, ...args])"
          />
        </dd> -->
        <dt>건설사명<sup>*</sup></dt>
        <dl style="" class="col">
          <dd>
            <text-input
              type="text"
              placeholder="건설사 이름을 입력해주세요"
              v-model="constructorName"
              required
            />
          </dd>
          <div :style="border_style"></div>
        </dl>
        <dt>현장 주소<sup>*</sup></dt>
        <dd style="">
          <div style="flex-grow: 1">
            <text-input
              type="text"
              placeholder="현장 주소 검색하기"
              v-model="address"
              required
            />
            <!-- <select-box text="건설사" :list="companyList" @change="getSiteList" /> -->
          </div>
          <button
            type="button"
            class="btn button_active"
            style="
              justify-content: flex-end;
              padding: 0px;
              width: 15%;
              margin-top: 0;
            "
            @click="$modal.show('post')"
          >
            주소찾기
          </button>
        </dd>
        <div :style="border_style"></div>
      </dl>
      <dl style="" class="col">
        <dd>
          <text-input
            type="text"
            placeholder="상세주소를 입력해주세요"
            v-model="detailAddress"
            required
          />
          <!-- <select-box text="건설사" :list="companyList" @change="getSiteList" /> -->
        </dd>
        <div :style="border_style"></div>
      </dl>

      <dl style="" class="col">
        <dt>모집 업종/인원<sup>*</sup></dt>
        <dd style="flex-direction: row">
          <text-input
            type="text"
            placeholder="업종 선택"
            v-model="detailAddress"
            required
            style="border-bottom: solid rgb(238, 241, 245)"
          />
          <div style="font-size: 3rem; margin-right: 1.5rem">></div>

          <text-input
            type="text"
            placeholder="인원 수(명)"
            v-model="detailAddress"
            required
            style="border-bottom: solid rgb(238, 241, 245)"
          />
        </dd>
        <!-- <div :style="border_style"></div> -->
      </dl>

      <dl class="col">
        <dd>
          <select-box
            :text="
              occupation[0].selected.text === undefined
                ? '대분류'
                : occupation[0].selected.text
            "
            :list="occupation[0].list"
            @change="(...args) => this.changeOccupation([0, ...args])"
          />
          <select-box
            :text="
              occupation[1].selected.text === undefined
                ? '소분류'
                : occupation[1].selected.text
            "
            :list="occupation[0].selected.subList"
            @change="(...args) => this.changeOccupation([1, ...args])"
          />
        </dd>
      </dl>
      <dl class="col">
        <dt>모집 성별<sup>*</sup></dt>
        <dd>
          <input type="radio" id="noSex" value="성별 무관" v-model="sex" />
          <label for="noSex">성별 무관</label>

          <input type="radio" id="male" value="남성" v-model="sex" />
          <label for="male">남성</label>

          <input type="radio" id="female" value="여성" v-model="sex" />
          <label for="female">여성</label>
        </dd>
      </dl>
      <dl class="col">
        <dt>모집 언령<sup>*</sup></dt>
        <dd style="flex-direction: column">
          <input type="radio" id="noAge" value="연령 무관" v-model="age" />
          <label for="noAge">연령 무관</label>

          <input type="radio" id="ageLimit" value="연령 제한" v-model="age" />
          <label for="ageLimit">연령 제한</label>
        </dd>
      </dl>

      <dl style="" class="col">
        <dt>일당<sup>*</sup></dt>
        <dd>
          <text-input
            type="text"
            placeholder="일당(1.0공수 기준, ₩)"
            v-model="pay"
            required
          />
        </dd>
        <div :style="border_style"></div>
      </dl>

      <!-- 사진첨부는 어드민으로 이동됨 ... JY -->
      <!-- <dl class="col">
        <dt>현장사진 첨부</dt>
        <dd>
          <div class="form-control-wrap file">
            <div class="image_wrap">
              <img ref="preview" alt="" />
            </div>
            <div class="upload_wrap">
              <input
                id="customFile"
                type="file"
                @change="onChangeImages"
                ref="images"
                accept="image/*"
                hidden
              />
              <label class="custom-file-label" for="customFile">{{
                imagePaths.name
              }}</label>

              <button
                type="button"
                class="btn-login"
                @click="onClickImageUpload"
              >
                파일 선택
              </button>
            </div>
          </div>
        </dd>
      </dl> -->

      <dl style="margin-top: 35px" class="col">
        <dt>출근 일정<sup>*</sup></dt>
        <dd>
          <date-picker
            :start="date.start"
            :end="date.end"
            :changStartDate="changStartDate"
            :changEndDate="changEndDate"
          />
        </dd>
      </dl>

      <dl class="col">
        <dt>출근 시간<sup>*</sup></dt>
        <dd>
          <text-input
            type="text"
            placeholder="오전 00:00"
            v-model="time"
            required
          />
        </dd>
      </dl>
      <!-- <dl style="" class="col">
        <dt>휴무일<sup>*</sup></dt>
        <div style="display: flex">
          <dd>
            <text-input type="text" placeholder="휴무일(선택)" />
          </dd>
          <div
            style="justify-content: flex-end"
            @click="click('HumanResources')"
          >
            <img :src="require('@/static/images/common/arrow.png')" alt="" />
          </div>
        </div>
      </dl> -->
      <div :style="border_style"></div>
      <dl class="col">
        <dt>휴무일<sup>*</sup></dt>
        <dd style="flex-direction: row">
          <input
            type="radio"
            id="satSun"
            value="토요일, 일요일"
            v-model="holiday"
          />
          <label style="flex: 1" for="satSun">토요일, 일요일</label>

          <input type="radio" id="sun" value="일요일" v-model="holiday" />
          <label style="flex: 1" for="sun">일요일</label>
        </dd>
      </dl>
      <!-- <dl class="row">
        <dt>휴무일<sup>*</sup></dt>
        <dd>
          <div class="consent_form center">
            <input
              type="checkbox"
              id="saturday"
              :value="saturday"
              @change="onChangeSaturday"
            />
            <label for="saturday" class="no_vertical">토요일, 일요일</label>
          </div>
        </dd>
        <dd>
          <div class="consent_form center">
            <input
              type="checkbox"
              id="sunday"
              :value="sunday"
              @change="onChangeSunday"
            />
            <label for="sunday" class="no_vertical">일요일</label>
          </div>
        </dd>
      </dl> -->

      <dl class="col">
        <dt>근무 조건<sup>*</sup></dt>
        <!-- <dd style="flex-direction: row">
          <input
            type="radio"
            id="satSun"
            value="토요일, 일요일"
            v-model="holiday"
          />
          <label style="flex: 1" for="satSun">토요일, 일요일</label>

          <input type="radio" id="sun" value="일요일" v-model="holiday" />
          <label style="flex: 1" for="sun">일요일</label>
        </dd> -->
      </dl>

      <!--        <dd><text-input placeholder="숫자만 입력" v-model="textInput"  @input="onInputNumber" /></dd>-->
      <!-- <dl class="col">
        <dt style="margin-top: 25px">작성자 정보<sup>*</sup></dt>
        <dd>
          <text-input type="text" placeholder="이름" v-model="name" required />
        </dd>
      </dl> -->
      <!-- <div :style="border_style"></div> -->
      <!-- <dl>
        <dd>
          <text-input
            type="tel"
            placeholder="연락처"
            v-model="phone"
            @input="onInputPhoneNumber"
            maxlength="13"
            minlength="12"
            required
          />
        </dd>
      </dl> -->
      <!-- <div :style="border_style"></div> -->

      <dl style="margin-top: 35px" class="col">
        <dt>기타사항</dt>
        <dd class="textarea_wrap">
          <textarea
            placeholder="기타 사항이 있다면 작성해주세요."
            v-model="etc"
          />
        </dd>
      </dl>

      <!-- <div class="box">
        민간 유료 직업 소개소의 <span class="emphasis">외국인</span> 고용알선은
        <span class="emphasis">불법</span>으로 외국인의 구직등록이
        <span class="emphasis">불가</span>합니다.
      </div> -->

      <div class="consent_form">
        <div style="display: flex; align-items: center">
          <input
            type="checkbox"
            id="form"
            :value="agree"
            @change="onChangeAgree"
          />
          <label
            for="form"
            style="
              width: 100%;
              margin: 1.5rem 0;
              margin-top: 1.5rem;
              font-weight: 400;
              font-size: 1.5rem;
            "
          >
            <!-- <span class="">(필수)</span> -->
            (필수) 개인정보 수집 및 이용에 동의합니다
            <!-- <button type="button" @click="getTerms(2)">내용보기</button> -->
          </label>
          <div style="justify-content: flex-end" @click="getTerms(2)">
            <img :src="require('@/static/images/common/arrow.png')" alt="" />
          </div>
        </div>
      </div>
      <div :style="border_style"></div>

      <div style="margin-top: 3rem" class="dialog_foot_buttons">
        <button
          form="registrationForm"
          type="submit"
          class="btn btn_primary"
          style="width: 100%; font-size: 1.5em; height: 5rem"
        >
          작성완료
        </button>
      </div>
      <modal :termsContent="termsContent" />
      <daum @handleAddr="handleAddr" />
    </form>
  </section>
</template>

<script>
  import textInput from '@/components/common/input'
  import selectBox from '@/components/common/selectbox'
  import datePicker from '@/components/common/datePicker'
  import api from '@/api/api'
  import moment, { relativeTimeRounding } from 'moment'
  import back from '@/components/layout/back'
  import modal from '@/components/popup/modal'
  import daum from '@/components/popup/daum'
  import Vue from 'vue'
  import job from '@/components/common/job'

  export default {
    name: 'HuntingRegistration',
    components: {
      textInput,
      selectBox,
      datePicker,
      back,
      modal,
      daum
    },
    data() {
      return {
        sex: '성별 무관',
        age: '연령 무관',
        holiday: '',
        border_style:
          'width: 93vw; height: 2px; background-color: #eef1f5; margin-top: 0.5rem; margin-bottom: 0.5rem;',
        chagePay: '',
        termsContent: '',
        address: '',
        constructorName: '',
        site: '',
        detailAddress: '',
        name: '',
        phone: '',
        date: {
          start: new Date(),
          end: new Date()
        },
        occupation: [
          {
            selected: {},
            list: job
          },
          {
            selected: {},
            list: []
          }
        ],
        personnel: 0,
        safetyEducation: 0,
        etc: '',
        agree: false,
        imagePaths: {},
        nextDay: false,
        saturday: false,
        sunday: false,
        time: '',
        pay: null,
        companyList: [],
        companySelected: {},
        siteList: [],
        siteSelected: {},
        siteIdx: null,
        siteName: ''
      }
    },
    created() {
      if (this.$route.params.data !== undefined) {
        const data = this.$route.params.data
        console.log('created: ', data)
        const periodStart = data.work_period_start.split('.')
        const periodEnd = data.work_period_end.split('.')
        this.siteName = data.work_title
        this.address = data.work_location
        this.detailAddress = data.work_detailaddress
        this.constructorName = data.work_constructor
        this.date.start = new Date(
          `${periodStart[0]}-${periodStart[1]}-${periodStart[2]}`
        )
        this.date.end = new Date(
          `${periodEnd[0]}-${periodEnd[1]}-${periodEnd[2]}`
        )
        this.saturday = data.work_rest.includes('토') ? true : false
        this.sunday = data.work_rest.includes('일') ? true : false
        this.phone = data.work_user_phone
        this.pay = data.work_payment
        this.etc = data.work_etc
        this.age = data.work_age
        this.occupation[0].selected = {
          category: data.work_divide_first,
          text: data.work_divide_first,
          value: data.work_divide_first
        }
        this.occupation[1].selected = {
          category: data.work_divide_second,
          text: data.work_divide_second,
          value: data.work_divide_second
        }
      }
    },
    watch: {
      pay: function (newValue) {
        const result = newValue
          .replace(/\D/g, '')
          .replace(/\B(?=(\d{3})+(?!\d))/g, ',')
        Vue.nextTick(() => (this.pay = result))
      }
    },
    mounted() {
      // this.getCompanyList() // 기업정보 가져오기 -> 지금은 직접입력으로 변경됨
      this.getInfo()
    },

    methods: {
      onInputPhoneNumber(value) {
        this.phone = this.$options.filters.phoneValidation(value)
      },
      chage(event) {
        const e = event.target.value
        console.log(e)
      },

      onInputPay(value) {
        this.pay = this.$options.filters.numberValidation(value)
      },

      onInputNumber(args) {
        if (args[2].length > args[1])
          this.date[args[0]] = args[2].slice(0, args[1])
        else this.date[args[0]] = args[2]
      },
      changeArea(args) {
        this.area[args[0]].selected = args[1]
      },
      changeOccupation(args) {
        this.occupation[args[0]].selected = args[1]
        if (args[0] === 0) {
          this.occupation[1].selected = {}
        }
      },
      onChangeAgree() {
        this.agree = !this.agree
      },
      onChangeNextDay() {
        this.nextDay = !this.nextDay
      },
      onChangeSaturday() {
        this.saturday = !this.saturday
      },
      onChangeAge() {
        this.age = !this.age
      },
      onChangeNoAge() {
        this.noAge = !this.noAge
      },
      onChangeSunday() {
        this.sunday = !this.sunday
      },
      onSubmit() {
        if (!this.agree) {
          this.$dialog.show({
            type: 'alert', //confirm or alert
            title: '동의항목을 확인해주세요'
          })
          return
        } else if (
          this.address.trim().length === 0 ||
          this.detailAddress.trim().length === 0 ||
          this.constructorName.trim().length === 0 ||
          !this.siteName.length ||
          // this.site.trim().length === 0 ||
          // this.companySelected.company_idx === undefined ||
          // this.siteIdx === null ||
          !moment(this.date.start).isValid() ||
          !moment(this.date.end).isValid() ||
          this.name.trim().length === 0 ||
          this.phone.trim().length === 0 ||
          this.pay.toString === '0'
        ) {
          this.$dialog.show({
            type: 'alert',
            title: '필수 항목을 입력해주세요'
          })
          relativeTimeRounding
        } else {
          this.$dialog
            .show({
              type: 'confirm', //confirm or alert
              title: '게시하시겠습니까?',
              data: {
                confirmText: '게시'
              }
            })
            .$on('dialog:confirm', () => {
              this.onInsert()
            })
        }
      },
      cancel() {
        history.go(-1)
      },
      onClickImageUpload() {
        this.$refs.images.click()
      },
      onChangeImages(e) {
        if (e.target.files && e.target.files[0]) {
          const reader = new FileReader()

          reader.onload = e => {
            this.$refs.preview.src = e.target.result
          }
          reader.readAsDataURL(e.target.files[0])

          this.imagePaths = e.target.files[0]
        }
        // this.imagePaths.file_name = e.target.files[0].name;

        // let files = e.target.files || e.dataTransfer.files;
        // console.log('files', files)
        // if (!files.length)
        //   return;
        // this.uploadImage(files[0]);

        // const formData = new FormData();

        // if(!this.imagePaths.length){
        //   return setImagePathsErr('대표이미지를 등록해주세요')
        // }
        // [].forEach.call(this.imagePaths, (f) => {
        //   formData.append('image', f);
        // });

        // formData.append('review', JSON.stringify({
        //   "productId" : productId,
        //   "visitYear": parseInt(visitYear),
        //   "visitMonth": parseInt(visitMonth),
        //   "productGrade" : grade,
        //   "title": title,
        //   "contents": contents,
        //   "openYn": openYn
        // }))
      },
      removeImage: function (e) {
        console.log(e)
        this.imagePaths = {}
      },
      async getInfo() {
        if (this.$store.getters.token === '') {
          this.$dialog
            .show({
              type: 'alert',
              title: '로그인정보가 없습니다'
            })
            .$on('dialog:confirm', () => {
              this.$router.push({ name: 'Main' })
            })
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
                console.log(result)
                if (result.data.success) {
                  console.log('success')
                  if (
                    result.data.data !== undefined &&
                    result.data.data !== null
                  ) {
                    this.name = result.data.data.result.user_name
                    this.phone = result.data.data.result.user_phone
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
      async getCompanyList() {
        try {
          const config = {
            headers: {
              'Content-Type': 'multipart/form-data',
              'Access-Control-Allow-Origin': '*',
              authorization: `Bearer ${this.$store.getters.token}`
            }
          }
          await api
            .get(`company/open/selectAllCompany`, config)
            .then(result => {
              console.log(result)
              if (result.data.success) {
                let list = result.data.data.company
                list = list.map(value => {
                  value.text = value.company_name
                  return value
                })
                this.companyList = list
              } else {
                this.$dialog.show({
                  type: 'alert', //confirm or alert
                  title: '건설사 목록 불러오기 실패'
                })
              }
            })
            .catch(err => console.log(err))
        } catch (err) {
          console.log(err)
        }
      },
      async getSiteList(v) {
        console.log(v)
        this.companySelected = v
        let companyIdx = v.company_idx
        try {
          const config = {
            headers: {
              'Content-Type': 'multipart/form-data',
              'Access-Control-Allow-Origin': '*',
              authorization: `Bearer ${this.$store.getters.token}`
            }
          }
          await api
            .get(`company/open/getSiteList/${companyIdx}`, config)
            .then(result => {
              console.log(result)
              if (result.data.success) {
                let list = result.data.data.siteList
                list = list.map(value => {
                  value.text = value.site_name
                  return value
                })
                this.siteList = list
              } else {
                if (result.data.code === 2102) {
                  this.$dialog.show({
                    type: 'alert', //confirm or alert
                    title: '등록된 현장이 없습니다.'
                  })
                } else {
                  this.$dialog.show({
                    type: 'alert', //confirm or alert
                    title: '현장 목록 불러오기 실패'
                  })
                }
              }
            })
            .catch(err => console.log(err))
        } catch (err) {
          console.log(err)
        }
      },
      handleSiteList(v) {
        this.siteSelected = v
        this.address = v.site_address
        this.siteIdx = v.site_idx
      },
      async onInsert() {
        try {
          if (
            this.occupation[0].selected.value === '' ||
            this.occupation[0].selected.value === undefined ||
            this.occupation[1].selected.value === '' ||
            this.occupation[1].selected.value === undefined
          ) {
            return this.$dialog.show({
              type: 'alert',
              title: '직종을 선택해주세요'
            })
          }
          const config = {
            headers: {
              'Content-Type': 'multipart/form-data',
              'Access-Control-Allow-Origin': '*',
              authorization: `Bearer ${this.$store.getters.token}`
            }
          }
          let formData = new FormData()
          /*
              {
                "contructor": "string",
                "siteName": "string",
                "jobImage": "string",
                "divideFirst": "string",
                "divideSecond": "string",
                "siteAddress": "string",
                "periodStart": "string",
                "periodEnd": "string",
                "phone": "string",
                "etc": "string"
              }
            */
          let rest = ''
          if (this.saturday && this.sunday) {
            rest = '토, 일'
          } else if (this.saturday && !this.sunday) {
            rest = '토'
          } else if (!this.saturday && this.sunday) {
            rest = '일'
          }
          let age = ''
          if (this.noAge) {
            // age = '연령무관'
            age = '0'
          } else {
            // age = '연령제한'
            age = '1'
          }
          // formData.append('constructor', this.companySelected.company_name)
          // formData.append('siteName', this.siteSelected.site_name)
          formData.append('constructor', this.constructorName)
          formData.append('detailAddress', this.detailAddress)
          formData.append('siteName', this.siteName)
          if (this.imagePaths.size !== undefined) {
            formData.append('jobImage', this.imagePaths)
          }
          formData.append(
            'divideFirst',
            this.occupation[0].selected.value === undefined
              ? ''
              : this.occupation[0].selected.value
          )
          formData.append(
            'divideSecond',
            this.occupation[1].selected.value === undefined
              ? ''
              : this.occupation[1].selected.value
          )
          formData.append('siteAddress', this.address)
          formData.append(
            'periodStart',
            moment(this.date.start).format('YYYY-MM-DD')
          )
          formData.append(
            'periodEnd',
            moment(this.date.end).format('YYYY-MM-DD')
          )
          formData.append('phone', this.phone + age)
          formData.append('payment', this.pay)
          formData.append('etc', this.etc)
          formData.append('rest', rest)
          formData.append('age', age)

          await api
            .post(`job/add/offer`, formData, config)
            .then(result => {
              console.log(result)
              if (result.data.success) {
                this.$dialog
                  .show({
                    type: 'alert', //confirm or alert
                    title: '구인 등록 성공'
                  })
                  .$on('dialog:confirm', () => {
                    this.goHome()
                  })
              } else {
                this.$dialog
                  .show({
                    type: 'confirm', //confirm or alert
                    title: '구인 등록 실패',
                    data: {
                      confirmText: '다시 등록'
                    }
                  })
                  .$on('dialog:confirm', () => {
                    this.onInsert()
                    return
                  })
              }
            })
            .catch(err => console.log(err))
        } catch (err) {
          console.log(err)
        }
      },
      goHome() {
        this.$router.push({ name: 'HumanResources', params: { type: '0' } })
      },
      changStartDate(date) {
        this.date.start = date
      },
      changEndDate(date) {
        this.date.end = date
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
      },
      handleAddr(v) {
        this.address = v
        this.$modal.hide('post')
      }
    }
  }
</script>
