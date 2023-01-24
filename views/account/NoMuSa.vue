<template>
  <section id="signin">
    <div ref="embed" class="info">
      <h1 class="title">노무사</h1>

      <div class="image_wrap">
        <img ref="preview" :src="this.imagePaths.length === 0" alt="" />
      </div>
      <dl class="col">
        <div class="">
          <div style=""></div>

          <dd style="width: 100%">
            <div class="form-control-wrap file">
              <div style="display: flex; align-items: center">
                <dt
                  style="
                    font-size: 14px;
                    font-weight: 700;
                    width: 100%;
                    color: #7a7e80;
                    margin-top: 10px;
                    margin-right: 10px;
                    white-space: nowrap;
                    min-width: 100px;
                  "
                >
                  프로필사진
                </dt>
              </div>
              <!--            http://yoonbumtae.com/?p=3304-->
              <div style="margin: 0px; padding: 10px 0px" class="upload_wrap">
                <input
                  id="customFile"
                  class="upload-name"
                  type="file"
                  @change="onChangeImages"
                  ref="images"
                  accept="image/*, video/*"
                  hidden
                />
                <label class="upload-image" for="customFile">{{
                  imagePaths.name
                }}</label>

                <button type="button" class="btn" @click="onClickImageUpload">
                  파일 선택
                </button>
              </div>
            </div>
          </dd>
        </div>
        <div class="infoContent">
          <div>
            <p>노무사 이름</p>
            <input @change="handleName(name)" type="text" v-model="name" />
          </div>
          <div>
            <p>노무사 연락처</p>
            <input
              type="tel"
              @change="handleNumber(phone)"
              @keyup="getPhoneMask(phone)"
              maxlength="13"
              minlength="12"
              v-model="phone"
            />
          </div>
          <div>
            <p>노무사 주소</p>
            <input
              @change="handleAddress(address)"
              v-bind:disabled="!!address"
              type="text"
              @click="showAddredss"
              v-model="address"
            />
          </div>
          <div>
            <p>노무사 연차</p>
            <input
              @change="handleEquipYear(number)"
              type="number"
              v-model="number"
              maxLength="3"
              minlength="1"
              onkeypress="return event.keyCode === 8 || event.charCode >= 48 && event.charCode <= 57"
            />
          </div>
          <div>
            <p>노무사 한줄 소개</p>
            <input
              @change="handleOwnerName(intro)"
              type="text"
              v-model="intro"
            />
          </div>

          <div>
            <p>노무사 상세소개</p>
            <input
              @change="handleDetailText(detailIntro)"
              type="text"
              v-model="detailIntro"
            />
          </div>
        </div>
      </dl>
      <!-- <p>{{ info[step.value - 1].desc }}</p> -->
    </div>
    <!-- <button v-on:click="postNoMusa()" class="big_btn">완료</button> -->
  </section>
</template>

<script>
  // import api from '@/api/api'

  //   import Modal from '../../components/popup/addressModal.vue'/
  export default {
    components: {
      //   Modal
    },
    name: 'NoMuSa',
    mounted() {
      this.$emit('QueryTypes', this.$route.query.type)

      console.log(this.imagePaths)
    },

    data() {
      return {
        isModalViewed: false,
        photo: '',
        intro: '',
        address: '',
        phone: '',
        detailIntro: '',
        number: '',
        name: '',
        imagePaths: {}
      }
    },

    methods: {
      handleDetailText(text) {
        // alert(text)
        this.$emit('EquipCategory', text)
      },
      handleName(name) {
        this.$emit('EquipName', name)
      },
      handleNumber(number) {
        console.log(number)

        // this.$emit('EquipPhone', number)
      },
      handleOwnerName(name) {
        this.$emit('EquipOwnerName', name)
      },
      handleAddress(number) {
        this.$emit('EquipAddress', number)
      },
      // handleCategory(number) {
      //   this.$emit('EquipCategory', number)
      // },
      // handlePhoto(number) {
      //   this.$emit('EquipPhoto', number)
      // },
      handleEquipYear(number) {
        number.replace(/[^0-9.]/g, '').replace(/(\..*)\./g)

        // number.replace(/[^0-9]/g, '') //정규식 사용
        // this.number = number
        this.$emit('EquipYear', number)
      },
      getPhoneMask(value) {
        this.phone = this.$options.filters.phoneValidation(value)

        this.$emit('EquipPhone', this.$options.filters.phoneValidation(value))
      },
      onChangeImages(e) {
        console.log(this.photo)
        if (e.target.files && e.target.files[0]) {
          const reader = new FileReader()

          reader.onload = e => {
            this.$refs.preview.src = e.target.result
          }
          reader.readAsDataURL(e.target.files[0])

          // this.photo = e.target.files[0] // 정훈님 코드
          this.imagePaths = e.target.files[0]
          this.$emit('EquipPhoto', this.imagePaths)
        }
      },
      // async postNoMusa() {
      //   try {
      //     const config = {
      //       headers: {
      //         'Content-Type': 'multipart/form-data',
      //         'Access-Control-Allow-Origin': '*',
      //         authorization: `Bearer ${this.$store.getters.token}`
      //       }
      //     }
      //     let formData = new FormData()
      //     formData.append('labor_name', this.name)
      //     formData.append('labor_phone', this.phone)

      //     formData.append('labor_simple_explain', this.intro)
      //     formData.append('labor_description', this.detailIntro)
      //     formData.append('labor_address', this.address)
      //     formData.append('labor_career', this.number)
      //     // 정훈님 코드
      //     // if (this.photo.size !== undefined) {
      //     //   formData.append('labor_image', this.photo)
      //     // }
      //     if (this.imagePaths.size !== undefined) {
      //       formData.append('labor_image', this.imagePaths)
      //     }
      //     if (
      //       this.name === '' ||
      //       this.phone === '' ||
      //       this.intro === '' ||
      //       this.detailIntro === '' ||
      //       this.address === '' ||
      //       // this.photo === '' ||
      //       this.number === ''
      //     ) {
      //       return this.$dialog.show({
      //         type: 'alert', //confirm or alert
      //         title: '빈칸을 채워주세요'
      //       })
      //     }
      //     // console.log(body, 'body')

      //     await api
      //       .post(`labor/open/regi`, formData, config)
      //       .then(result => {
      //         console.log(result)
      //         if (result.data.success) {
      //           return this.$router.push({
      //             name: 'AccountStepFour',
      //             query: {
      //               user_type: this.$route.query.type
      //             }
      //           })
      //         } else {
      //           this.showAlert(result.data.message)
      //         }
      //       })
      //       .catch(err => console.log(err))
      //   } catch (err) {
      //     console.log(err)
      //   }
      // },
      movePage() {
        // this.$router.push({ name: 'AccountStepFour' })

        console.log(this.intro, this.address)
      },
      showAddredss() {
        new window.daum.Postcode({
          oncomplete: data => {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

            // 도로명 주소의 노출 규칙에 따라 주소를 조합한다.
            let fullRoadAddr = data.roadAddress
            let extraRoadAddr = ''

            // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
            if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
              extraRoadAddr += data.bname
            }
            // 건물명이 있고, 공동주택일 경우
            if (data.buildingName !== '' && data.apartment === 'Y') {
              extraRoadAddr +=
                extraRoadAddr !== ''
                  ? ', ' + data.buildingName
                  : data.buildingName
            }
            // 도로명, 지번 조합형 주소가 있을 경우,
            if (extraRoadAddr !== '') {
              extraRoadAddr = ' (' + extraRoadAddr + ')'
            }
            // 도로명, 지번 주소의 유무에 따라 해당 조합형 주소를 추가
            if (fullRoadAddr !== '') {
              fullRoadAddr += extraRoadAddr
            }

            this.address = fullRoadAddr
            this.$emit('EquipAddress', this.address)
          }
        }).open()
      },
      onClickImageUpload() {
        this.$refs.images.click()
      }
    }
  }
</script>
