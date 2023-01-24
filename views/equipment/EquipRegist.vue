<template>
  <section id="job_search">
    <back />
    <div class="info">
      <h1 class="title">내 장비 올리기</h1>
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
            :text="
              equipment[0].selected.text === undefined
                ? '장비종류'
                : equipment[0].selected.text
            "
            :list="equipment[0].list"
            @change="(...args) => this.changeEquipment([0, ...args])"
          />
          <select-box
            :text="
              equipment[1].selected.text === undefined
                ? '연식'
                : equipment[1].selected.text
            "
            :list="equipment[1].list"
            @change="(...args) => this.changeEquipment([1, ...args])"
          />
        </dd>
      </dl>

      <dl class="col">
        <dt>지역<sup>*</sup></dt>
        <dd>
          <select-box
            :text="
              area[0].selected.text === undefined
                ? '시/도'
                : area[0].selected.text
            "
            :list="area[0].list"
            @change="(...args) => this.changeArea([0, ...args])"
          />
          <select-box
            :text="
              area[1].selected.text === undefined
                ? '구/군'
                : area[1].selected.text
            "
            :list="area[0].selected.subList"
            @change="(...args) => this.changeArea([1, ...args])"
          />
        </dd>
      </dl>

      <!-- <dl class="col">
        <dt>장비사진 첨부<sup>*</sup></dt>
        <dd>
          <div class="form-control-wrap file">
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
                files.name
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
        <div style="margin-top: 10px" class="image_wrap">
          <img ref="preview" :src="this.files.length === 0" alt="" />
        </div>
      </dl> -->

      <dl class="col">
        <dt>장비사진 첨부 ({{ this.files.length }} / 5)</dt>
        <div class="preview_container">
          <div
            v-for="(item, key) in this.filesPreview"
            style="margin-bottom: 10px"
            :key="key"
            class="preview_wrapper"
          >
            <img
              style=""
              ref="preview"
              :src="
                'https://momobuild.s3.ap-northeast-2.amazonaws.com/' +
                item.file_path
              "
              alt=""
            />
          </div>
          <div
            v-for="(item, key) in this.files"
            style="margin-bottom: 10px"
            :key="key"
            class="preview_wrapper"
          >
            <div class="close_btn" @click="onDeleteImages" :name="item.number">
              x
            </div>
            <img style="" ref="preview" :src="item.preview" alt="" />
          </div>
        </div>
        <dd
          v-if="!this.files.length"
          style="display: flex; flex-direction: column"
        >
          <div class="form-control-wrap file">
            <div style="margin: 0; padding: 0" class="upload_wrap">
              <input
                id="customFile"
                type="file"
                @change="onChangeImages"
                ref="images"
                accept="image/*"
                hidden
                multiple
                max="5"
              />
              <!-- <label class="custom-file-label" for="customFile">{{
                files.name
              }}</label> -->
              <button
                type="button"
                class="btn-login"
                @click="onClickImageUpload"
              >
                사진 선택
              </button>
            </div>
          </div>
        </dd>
        <dd v-else style="display: flex; flex-direction: column">
          <div class="form-control-wrap file">
            <div style="margin: 0; padding: 0" class="upload_wrap">
              <input
                id="customFile2"
                type="file"
                @change="onAddImages"
                ref="addImages"
                accept="image/*"
                hidden
                multiple
                max="5"
              />
              <!-- <label class="custom-file-label" for="customFile">{{
                files.name
              }}</label> -->
              <button
                type="button"
                class="btn-login"
                @click="onClickAddImageUpload"
              >
                추가 사진 선택
              </button>
            </div>
          </div>
        </dd>
      </dl>

      <!-- <dl class="col">
        <dt>작업 가능 날짜<sup>*</sup></dt>
        <dd>
          <date-picker
            :start="date.start"
            :end="date.end"
            :changStartDate="changStartDate"
            :changEndDate="changEndDate"
          />
        </dd>
      </dl> -->

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

      <button type="submit" class="big_btn">게시하기</button>
      <modal :termsContent="termsContent" />
    </form>
  </section>
</template>

<script>
  import textInput from '@/components/common/input'
  import selectBox from '@/components/common/selectbox'
  import area from '@/components/common/area'
  import api from '@/api/api'
  // eslint-disable-next-line no-unused-vars
  import moment from 'moment'
  import datePicker from '@/components/common/datePicker'
  import back from '@/components/layout/back'
  import modal from '@/components/popup/modal'
  import equip from '@/components/common/equip'

  export default {
    name: 'Lease',
    components: {
      textInput,
      selectBox,
      // eslint-disable-next-line vue/no-unused-components
      datePicker,
      back,
      modal
    },
    data() {
      return {
        termsContent: '',
        equipTitle: '',
        capacity: [
          {
            selected: {},
            list: [{}]
          }
        ],
        equipment: [
          {
            selected: {},
            list: equip
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
            list: []
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
        etc: '',
        files: [],
        filesPreview: [],
        uploadImageIndex: 0,
        modify: 0, // 수정 시 사용
        idx: 0 // 수정 시 사용
      }
    },
    async created() {
      if (this.$route.params.data !== undefined) {
        const data = this.$route.params.data
        try {
          const config = {
            headers: {
              'Content-Type': 'application/json; charset = utf-8',
              'Access-Control-Allow-Origin': '*',
              authorization: `Bearer ${this.$store.getters.token}`
            }
          }
          await api
            .get(`mypage/getFiles/${data.construction_idx}`, config)
            .then(result => {
              console.log(result)
              if (result.data.success) {
                console.log('success')
                if (
                  result.data.data !== undefined &&
                  result.data.data !== null
                ) {
                  this.modify = 1
                  this.idx = data.construction_idx
                  this.filesPreview = result.data.data.result
                }
              } else {
                this.$dialog.show({
                  type: 'alert',
                  title: '사진 불러오기 실패'
                })
              }
            })
            .catch(err => console.log(err))
        } catch (err) {
          console.log(err)
        }
        console.log(data)
        const periodStart = data.construction_period_start.split('.')
        const periodEnd = data.construction_period_end.split('.')
        this.equipTitle = data.construction_title
        this.date.start = new Date(
          `${periodStart[0]}-${periodStart[1]}-${periodStart[2]}`
        )
        this.date.end = new Date(
          `${periodEnd[0]}-${periodEnd[1]}-${periodEnd[2]}`
        )
        this.phone = data.construction_user_phone
        this.etc = data.construction_etc
        this.equipment[0].selected = {
          text: data.construction_divide_first,
          value: data.construction_divide_first
        }
        this.equipment[1].selected = {
          text: data.construction_divide_second + '년',
          value: data.construction_divide_second
        }
        this.area[0].selected = {
          text: data.construction_location,
          value: data.construction_location
        }
        this.area[1].selected = {
          text: data.construction_location_detail,
          value: data.construction_location_detail
        }
      }
    },
    watch: {},
    mounted() {
      this.getInfo()
    },
    methods: {
      changeEquipment(args) {
        this.equipment[args[0]].selected = args[1]
      },
      onClickImageUpload() {
        if (this.modify === 1) {
          this.$dialog
            .show({
              type: 'confirm',
              title: '사진을 다시 선택하시겠습니까?',
              data: {
                confirmText: '선택'
              }
            })
            .$on('dialog:confirm', () => {
              this.$refs.images.click()
            })
        } else {
          this.$refs.images.click()
        }
      },
      onClickAddImageUpload() {
        this.$refs.addImages.click()
      },
      onChangeImages(e) {
        if (e.target.files.length > 5) {
          this.$dialog.show({
            type: 'alert',
            title: '최대 5개까지 첨부가 가능합니다.'
          })
          return false
        }

        this.filesPreview = []
        this.modify = 0

        let num = -1
        for (let i = 0; i < e.target.files.length; i++) {
          this.files = [
            ...this.files,
            {
              file: e.target.files[i],
              preview: URL.createObjectURL(e.target.files[i]),
              number: i
            }
          ]
          num = i
        }

        this.uploadImageIndex = num + 1
      },
      removeImage: function (e) {
        console.log(e)
        this.files = {}
      },
      changeArea(args) {
        this.area[args[0]].selected = args[1]
        if (args[0] === 0) {
          this.area[1].selected = {}
        }
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
          // !moment(this.date.start).isValid() ||
          // !moment(this.date.end).isValid() ||
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
      async onInsert() {
        try {
          const config = {
            headers: {
              'Content-Type': 'application/json; charset = utf-8',
              'Access-Control-Allow-Origin': '*',
              authorization: `Bearer ${this.$store.getters.token}`
            }
          }

          let images = [
            '.bmp',
            '.rle',
            '.dib',
            '.jpg',
            '.jpeg',
            '.gif',
            '.png',
            '.tif',
            '.tiff',
            '.raw',
            '.svg',
            '.webp'
          ]
          let videos = [
            '.mp4',
            '.m4v',
            '.avi',
            '.wmv',
            '.mpg',
            '.mpeg',
            '.mkv',
            '.mov',
            '.3gp',
            '.3g2',
            '.webm'
          ]
          let fileExt = []

          const formData = new FormData()
          formData.append('title', this.equipTitle)
          formData.append('divideFirst', this.equipment[0].selected.value)
          formData.append('divideSecond', this.equipment[1].selected.value)
          formData.append('location', this.area[0].selected.value)
          formData.append('locationDetail', this.area[1].selected.value)
          formData.append('periodStart', this.date.start)
          formData.append('periodEnd', this.date.end)
          formData.append('name', this.name)
          formData.append('phone', this.phone)
          formData.append('etc', this.etc)
          if (this.modify === 1) {
            formData.append('modify', 1)
            formData.append('idx', this.idx)
          }
          if (this.files.length > 0) {
            for (let i = 0; i < this.files.length; i++) {
              formData.append('constructionImage', this.files[i].file)
              let filename = this.files[i].file.name
                .substring(this.files[i].file.name.lastIndexOf('.'))
                .toLowerCase()
              images.map(val => {
                if (filename === val) fileExt.push('0')
              })
              if (fileExt.length === i + 1) continue
              videos.map(val => {
                if (filename === val) fileExt.push('1')
              })
            }

            if (fileExt.length !== this.files.length) {
              this.$dialog.show({
                type: 'alert',
                title: '파일 확장자명을 확인해주세요'
              })
              return false
            }
            formData.append('fileExt', fileExt)
          }

          await api
            .post(`equip/add/rental`, formData, config)
            .then(result => {
              console.log(result)
              if (result.data.success) {
                this.$dialog
                  .show({
                    type: 'alert', //confirm or alert
                    title: '장비등록 성공'
                  })
                  .$on('dialog:confirm', () => {
                    this.goHome()
                  })
              } else {
                this.$dialog
                  .show({
                    type: 'confirm', //confirm or alert
                    title: '장비등록 실패',
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
        this.$router.push({ name: 'Equipment' })
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
      onDeleteImages(e) {
        const name = e.target.getAttribute('name')
        this.files = this.files.filter(val => val.number !== Number(name))
      },
      onAddImages(e) {
        if (this.files.length + e.target.files.length > 5) {
          this.$dialog.show({
            type: 'alert',
            title: '최대 5개까지 첨부가 가능합니다.'
          })
          return false
        }

        let num = -1
        for (let i = 0; i < e.target.files.length; i++) {
          this.files = [
            ...this.files,
            {
              file: e.target.files[i],
              preview: URL.createObjectURL(e.target.files[i]),
              number: i + this.uploadImageIndex
            }
          ]
          num = i
        }
        this.uploadImageIndex = this.uploadImageIndex + num + 1
      }
    }
  }
</script>

<style scoped></style>
