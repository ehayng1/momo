<template>
  <section id="signin">
    <!-- <back :path="'AccoutService'" /> -->
    <div class="info">
      <h1 class="title" style="margin-bottom: 10px">장비주</h1>
      <div style="margin-bottom: 20px" class="image_wrap">
        <img ref="preview" :src="this.imagePaths.length === 0" alt="" />
      </div>
      <dl class="col">
        <div class="infoContent">
          <div>
            <p>장비주 성함</p>
            <input
              type="text"
              @change="handleName(equipOwenrName)"
              v-model="equipOwenrName"
            />
          </div>
          <div>
            <p>장비주 연락처</p>
            <input
              @keyup="getPhoneMask(equipNumber)"
              @change="handleNumber(equipNumber)"
              type="text"
              maxlength="13"
              minlength="12"
              v-model="equipNumber"
            />
          </div>
          <div>
            <p>장비주 부류</p>

            <select name="asdf" v-model="isValue" @change="onChangeSelcet">
              <!-- <option selected>보기를 선택하세요</option> -->
              <option disabled value="">부류를 선택해주세요</option>

              <option
                v-for="(item, key) in equipment[0].list"
                :value="item.name"
                :key="key"
              >
                {{ item.name }}
              </option>
            </select>
          </div>
          <!-- <div>
            <p>장비주 주소</p>
            <input
              @change="handleAddress(equipAddress)"
              v-bind:disabled="!!equipAddress"
              type="text"
              @click="showAddredss"
              v-model="equipAddress"
            />
          </div> -->
          <div>
            <p>장비 스펙</p>

            <input @change="handleEquipYear(year)" type="text" v-model="year" />
          </div>
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
                  장비 사진 업로드
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
          <!-- <div style="display: flex">
            <p>장비사진</p>

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
          </div> -->
        </div>
      </dl>
    </div>
    <!-- <button v-on:click="queryFunction" class="big_btn">완료</button> -->
  </section>
</template>

<script>
  // import back from '@/components/layout/back'
  // import api from '@/api/api'
  import { equipmentArr } from './constanst'
  export default {
    name: 'EquipAndNoMuInfo',
    mounted() {
      console.log()
      console.log(this.$route.query.type)
      this.$emit('QueryTypes', this.$route.query.type)
    },
    data() {
      return {
        test: '',
        equipOwenrName: '',
        equipName: '',
        equipNumber: '',
        nullImage: '',
        equipAddress: '',
        photo: '',
        year: '',
        equipArr: [],
        equipment: equipmentArr,
        isValue: '',
        data: [],
        // selected:'',
        imagePaths: {}
      }
    },
    methods: {
      getPhoneMask(value) {
        this.equipNumber = this.$options.filters.phoneValidation(value)
        this.$emit('EquipPhone', this.$options.filters.phoneValidation(value))
      },
      onChangeSelcet() {
        this.isValue
        alert(this.isValue)
        console.log(this.isValue)
        this.$emit('EquipCategory', this.isValue)
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
            this.equipAddress = fullRoadAddr
            this.$emit('EquipAddress', this.equipAddress)
          }
        }).open()
      },
      handleName(name) {
        this.$emit('EquipName', name)
        // alert(name)
      },
      handleNumber(number) {
        // alert(number)
        console.log(number)

        // this.$emit('EquipPhone', number)
      },
      handleOwnerName(number) {
        // alert(number)
        console.log(number)

        this.$emit('EquipOwnerName', number)
      },
      handleAddress(number) {
        // alert(number)
        console.log(number)

        this.$emit('EquipAddress', number)
      },
      // handleCategory(number) {
      //   this.$emit('EquipCategory', number)
      // },
      // handlePhoto(number) {
      //   alert(number)
      //   this.$emit('EquipPhoto', number)
      //   console.log('123134134')
      // },
      handleEquipYear(number) {
        //장비연식= > 장비 스펙으로바뀜
        // alert(number)
        console.log(number)
        this.$emit('EquipYear', number)
      },
      onChangeImages(e) {
        console.log(this.imagePaths)

        if (e.target.files && e.target.files[0]) {
          const reader = new FileReader()
          reader.onload = e => {
            this.$refs.preview.src = e.target.result
          }
          reader.readAsDataURL(e.target.files[0])
          // this.photo = e.target.files[0] // 정훈님 코드
          this.imagePaths = e.target.files[0]

          console.log(this.imagePaths)
          // alert(this.imagePaths.size)
          this.$emit('EquipPhoto', this.imagePaths)
          // this.handleChange(this.imagePaths)
        }
      },
      // async queryFunction() {
      //   try {
      //     const config = {
      //       headers: {
      //         'Content-Type': 'multipart/form-data',
      //         'Access-Control-Allow-Origin': '*',
      //         authorization: `Bearer ${this.$store.getters.token}`
      //       }
      //     }
      //     let formData = new FormData()
      //     formData.append('owner_name', this.equipOwenrName)
      //     formData.append('owner_phone', this.equipNumber)
      //     formData.append('equipname', this.equipName)
      //     formData.append('owner_category', this.isValue)
      //     formData.append('owner_address', this.equipAddress)
      //     // 정훈님 코드
      //     // if (this.photo.size !== undefined) {
      //     //   formData.append('owner_image', this.photo)
      //     // }
      //     if (this.imagePaths.size !== undefined) {
      //       formData.append('owner_image', this.imagePaths)
      //     }
      //     formData.append('owner_career', this.year)
      //     // console.log(body, 'body')
      //     for (let key of formData.keys()) {
      //       console.log(key, ':', formData.get(key))
      //     }
      //     if (
      //       this.equipOwenrName === '' ||
      //       this.equipNumber === '' ||
      //       this.equipName === '' ||
      //       this.equipAddress === '' ||
      //       this.year === '' ||
      //       this.photo === '' ||
      //       this.isValue.length < 1
      //     ) {
      //       return this.$dialog.show({
      //         type: 'alert', //confirm or alert
      //         title: '빈칸을 다 채워주세요'
      //       })
      //     }
      //     await api
      //       .post(`equip/add/regi`, formData, config)
      //       .then(result => {
      //         console.log(result)
      //         if (result.data.success) {
      //           this.$dialog.show({
      //             type: 'alert', //confirm or alert
      //             title: '장비주 등록 성공'
      //           })
      //           return this.$router.push({
      //             name: 'AccountStepFour',
      //             query: {
      //               user_type: this.$route.query.type
      //             }
      //           })
      //         }
      //       })
      //       .catch(err => console.log(err))
      //   } catch (err) {
      //     console.log(err)
      //   }
      // },
      onClickImageUpload() {
        this.$refs.images.click()
      }
    }
  }
</script>
