<template>
  <div class="container">
    <div class="blockchain">
      <div class="card">
        <div class="card_nickname">
          <div class="preview" style="min-width: 76px">
            <input
              @change="onChangeImages"
              type="file"
              ref="images"
              accept="image/*"
              hidden
            />
            <!-- <button type="button" @click="onClickImageUpload">
              이미지 등록
            </button> -->
            <img
              ref="preview"
              alt=""
              style="width: 75px; height: 75px"
              @click="onClickImageUpload"
            />
          </div>
          <div>
            <input
              type="button"
              @click="randomProfileImage"
              value="기본이미지 변경"
            />
          </div>
        </div>
        <div class="card_nickname" style="margin-left: 17px; width: 100%">
          <p style="margin-bottom: 11px">
            BLOCKCHAIN <br />
            {{ wallet }}
          </p>
          <div style="width: 100%; display: flex; align-items: center">
            <div class="signUpChange">
              <p style="">이름</p>
              <input
                @input="handleName(name)"
                v-model="name"
                placeholder="이름을 설정해주세요"
              />
            </div>
          </div>
          <div style="width: 100%; display: flex">
            <div style="" class="signUpChange">
              <p style="">닉네임</p>
              <input
                @input="handleNickName(NickName)"
                v-model="NickName"
                placeholder="닉네임을 설정해주세요"
                maxlength="15"
              />
            </div>
          </div>
        </div>
      </div>

      <div class="text_container">
        <chain />
        <p>귀하의 개인정보는 블록체인으로부터 보호받고 있습니다.</p>
      </div>
    </div>
  </div>
</template>
<script>
  import user from '@/static/images/common/chainUser.svg'
  import chain from '@/static/images/common/chain.svg'
  import user2 from '@/static/images/common/chainUser2.png'
  // import test from '@/static/images/common/'

  export default {
    // eslint-disable-next-line vue/no-unused-components
    components: { user, chain, user2 },
    data() {
      return {
        wallet: '',
        NickName: '',
        photo: '',
        profile: '',
        name: '',
        // name: ''
        test: null,
        randNum: ''
      }
    },

    methods: {
      onChangeImages(e) {
        console.log(this.test)
        if (e.target.files && e.target.files[0]) {
          const reader = new FileReader()

          reader.onload = e => {
            this.$refs.preview.src = e.target.result
          }
          reader.readAsDataURL(e.target.files[0])

          this.photo = e.target.files[0]
          // console.log(this.photo && null)
          if (this.photo.length < 1) {
            this.handleInitialPhoto(this.test)
          }

          // //   // this.test = e.target.files[0]
          // //   console.log(this.test)
          // //   this.handletest(this.test)
          // }
          this.handleInitialPhoto(this.photo)
        }
      },
      onClickImageUpload() {
        this.$refs.images.click()
      },
      getWallet() {
        this.wallet = this.$store.getters.wallet
      },
      handleNickName(value) {
        if (value.length < 9) {
          this.$emit('handleNickName', value)
        } else {
          this.$dialog.show({
            type: 'alert',
            title: '8자 이하로 설정해주세요'
          })
          return false
        }
      },
      handleInitialPhoto(value) {
        console.log(value, '사진')
        this.$emit('handleInitialPhoto', value)
      },
      handleName(value) {
        console.log(value, '이름')
        this.$emit('handleName', value)
      },
      randomProfileImage() {
        let num = Math.floor(Math.random() * 16) + 1
        let num2 = this.randNum
        while (num === num2) {
          num = Math.floor(Math.random() * 16) + 1
          if (num !== num2) {
            break
          }
        }
        this.$refs.preview.src = `https://momobuild.s3.ap-northeast-2.amazonaws.com/file/userImage/${num}.png`
        this.randNum = num
        this.$emit('handleRandNum', this.randNum)
      }
    },
    mounted() {
      // console.log((this.test = e.target))
      console.log(this.photo, '포토')
      this.randNum = Math.floor(Math.random() * 16) + 1
      this.$emit('handleRandNum', this.randNum)
      this.$refs.preview.src = `https://momobuild.s3.ap-northeast-2.amazonaws.com/file/userImage/${this.randNum}.png`
      // this.randomProfileImage()
      this.getWallet()
    }
  }
</script>
