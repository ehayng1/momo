<template>
  <section id="job_search">
    <back />
    <div class="info">
      <h1 class="title">장비임대</h1>
    </div>

    <form class="form" @submit.prevent="onSubmit">
      <dl class="row">
        <dt>장비이름<sup>*</sup></dt>
        <dd>
          <text-input
            type="text"
            placeholder="그라인더"
            v-model="equipTitle"
            required
          />
        </dd>
      </dl>

      <dl class="col">
        <dt>장비<sup>*</sup></dt>
        <dd>
          <select-box
            text="장비종류"
            :list="equipment[0].list"
            @change="(...args) => this.changeEquipment([0, ...args])"
          />
          <select-box
            text="연식"
            :list="equipment[1].list"
            @change="(...args) => this.changeEquipment([1, ...args])"
          />
        </dd>
      </dl>

      <dl class="col">
        <dt>장비사진 첨부<sup>*</sup></dt>
        <dd>
          <div class="form-control-wrap file">
            <div class="image_wrap">
              <img
                ref="preview"
                :src="require('@/static/images/common/big_tool.png')"
                alt=""
              />
            </div>
            <!--            http://yoonbumtae.com/?p=3304-->
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
      </dl>

      <dl class="col">
        <dt>지역<sup>*</sup></dt>
        <dd>
          <select-box
            text="시/도"
            :list="area[0].list"
            @change="(...args) => this.changeArea([0, ...args])"
          />
          <select-box
            text="구/군"
            :list="area[0].selected.subList"
            @change="(...args) => this.changeArea([1, ...args])"
          />
        </dd>
      </dl>

      <dl class="col">
        <dt>작업 가능 날짜<sup>*</sup></dt>
        <dd>
          <date-picker
            :start="date.start"
            :end="date.end"
            :changStartDate="changStartDate"
            :changEndDate="changEndDate"
          />
        </dd>
      </dl>

      <!-- <dl class="row">
        <dt>작업일수</dt>
        <dd><text-input type="text" placeholder="1일" v-model="workday" /></dd>
      </dl> -->

      <dl class="row">
        <dt>신청자<sup>*</sup></dt>
        <dd><text-input type="text" placeholder="이름" v-model="name" /></dd>
      </dl>

      <dl class="row">
        <dt>연락처<sup>*</sup></dt>
        <dd>
          <text-input
            type="tel"
            placeholder="핸드폰 번호"
            v-model="phone"
            @input="onInputPhoneNumber"
            maxlength="13"
            minlength="12"
          />
        </dd>
      </dl>

      <dl class="col">
        <dt>기타사항</dt>
        <dd class="textarea_wrap">
          <textarea placeholder="기타사항을 입력해주세요." v-model="etc" />
        </dd>
      </dl>

      <div class="consent_form">
        <input
          type="checkbox"
          id="form"
          :value="agree"
          @change="onChangeArea"
        />
        <label for="form">
          <span class="emphasis">[필수]</span>
          개인정보 수집 및 이용에 동의합니다
          <button type="button" @click="getTerms(2)">내용보기</button>
        </label>
      </div>

      <button type="submit" class="big_btn">장비 임대 신청</button>
      <modal :termsContent="termsContent" />
    </form>
  </section>
</template>

<script>
  import textInput from '@/components/common/input'
  import selectBox from '@/components/common/selectbox'
  import area from '@/components/common/area'
  import api from '@/api/api'
  import moment from 'moment'
  import datePicker from '@/components/common/datePicker'
  import back from '@/components/layout/back'
  import modal from '@/components/popup/modal'

  export default {
    name: 'Rental',
    components: {
      textInput,
      selectBox,
      datePicker,
      back,
      modal
    },
    data() {
      return {
        termsContent: '',
        equipTitle: '',
        equipment: [
          {
            selected: {},
            list: [
              {
                value: 'tool',
                text: '공구'
              },
              {
                value: 'gear',
                text: '장비'
              }
            ]
          },
          {
            selected: {},
            list: [
              {
                value: '0',
                text: '0년'
              },
              {
                value: '1',
                text: '1년'
              },
              {
                value: '2',
                text: '2년'
              },
              {
                value: '3',
                text: '3년'
              },
              {
                value: '4',
                text: '4년'
              },
              {
                value: '5',
                text: '5년'
              },
              {
                value: '6',
                text: '6년'
              },
              {
                value: '7',
                text: '7년'
              },
              {
                value: '8',
                text: '8년'
              },
              {
                value: '9',
                text: '9년'
              }
            ]
          }
        ],
        area: [
          {
            selected: {},
            list: area
          },
          {
            selected: {},
            list: [
              {
                value: 0,
                text: '성북구'
              },
              {
                value: 1,
                text: '강북부'
              }
            ]
          }
        ],
        date: {
          start: new Date(),
          end: new Date()
        },
        workday: '',
        name: '',
        phone: '',
        agree: false,
        imagePaths: {},
        etc: ''
      }
    },
    watch: {},
    methods: {
      changeEquipment(args) {
        this.equipment[args[0]].selected = args[1]
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
        //   formData.append('imagePaths', f);
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
      changeArea(args) {
        this.area[args[0]].selected = args[1]
      },
      onInputNumber(args) {
        if (args[2].length > args[1])
          this.date[args[0]] = args[2].slice(0, args[1])
        else this.date[args[0]] = args[2]
      },
      onInputPhoneNumber(value) {
        this.phone = this.$options.filters.phoneValidation(value)
      },
      onChangeArea() {
        this.agree = !this.agree
      },
      onSubmit() {
        if (!this.agree) {
          this.$dialog.show({
            type: 'alert', //confirm or alert
            title: '동의항목을 확인해주세요'
          })
          return
        } else if (
          this.equipTitle.trim().length === 0 ||
          JSON.stringify(this.equipment[0].selected) === '{}' ||
          JSON.stringify(this.equipment[1].selected) === '{}' ||
          JSON.stringify(this.area[0].selected) === '{}' ||
          JSON.stringify(this.area[1].selected) === '{}' ||
          !moment(this.date.start).isValid() ||
          !moment(this.date.end).isValid() ||
          this.name.trim().length === 0 ||
          this.phone.trim().length === 0
        ) {
          this.$dialog.show({
            type: 'alert',
            title: '필수 항목을 입력해주세요'
          })
          return
        } else {
          this.$dialog
            .show({
              type: 'confirm', //confirm or alert
              title: '신청하시겠습니까?',
              data: {
                confirmText: '신청'
              }
            })
            .$on('dialog:confirm', () => {
              this.onInsert()
            })
        }
      },
      async onInsert() {
        try {
          const config = {
            headers: {
              'Content-Type': 'multipart/form-data',
              'Access-Control-Allow-Origin': '*',
              authorization: `Bearer ${this.$store.getters.token}`
            }
          }
          let formData = new FormData()
          formData.append('title', this.equipTitle)
          formData.append('divideFirst', this.equipment[0].selected.value)
          formData.append('divideSecond', this.equipment[1].selected.value)
          console.log('1')
          if (this.imagePaths.size !== undefined) {
            console.log('2')
            formData.append('constructionImage', this.imagePaths)
          }
          console.log('3')
          formData.append('location', this.area[0].selected.value)
          formData.append('locationDetail', this.area[1].selected.value)
          formData.append('periodStart', this.date.start)
          formData.append('periodEnd', this.date.end)
          formData.append('name', this.name)
          formData.append('phone', this.phone)
          formData.append('etc', this.etc)

          console.log(formData.get('constructionImage'))

          await api
            .post(`equip/add/rental`, formData, config)
            .then(result => {
              console.log(result)
              if (result.data.success) {
                this.$dialog
                  .show({
                    type: 'alert', //confirm or alert
                    title: '임대 등록 성공'
                  })
                  .$on('dialog:confirm', () => {
                    this.goHome()
                  })
              } else {
                this.$dialog
                  .show({
                    type: 'confirm', //confirm or alert
                    title: '임대 등록 실패',
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
        this.$router.push({ path: '/' })
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
      }
    }
  }
</script>

<style scoped></style>
