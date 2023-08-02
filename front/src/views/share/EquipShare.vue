<template>
  <div class="share">
    <back v-if="this.referer === true" />
    <div>
      <div ref="visualSwiper" class="swiper">
        <div class="swiper-wrapper">
          <div
            v-for="(item, key) in this.images"
            :key="key"
            class="swiper-slide visual"
          >
            <img
              :src="
                'https://momobuild.s3.ap-northeast-2.amazonaws.com/' +
                item.file_path
              "
              alt="장비사진"
            />
          </div>
        </div>
        <div ref="pagination" class="swiper-pagination" />
      </div>
    </div>
    <div v-if="this.equip.construction_img">
      <img
        :src="
          'https://momobuild.s3.ap-northeast-2.amazonaws.com/' +
          this.equip.construction_img
        "
        alt="장비사진"
      />
    </div>
    <div class="share_info">
      <span>장비이름: </span>
      <p>
        {{ this.equip.construction_title }}
      </p>
    </div>
    <div class="share_info_address">
      <span>지역: </span>
      <p>
        {{ this.equip.construction_location }}
        {{ this.equip.construction_location_detail }}
      </p>
    </div>
    <div class="share_info">
      <span>종류: </span>
      <p>
        {{ this.equip.construction_divide_first }}
      </p>
    </div>
    <div class="share_info">
      <span>연식: </span>
      <p>
        {{ this.equip.construction_divide_second }}
      </p>
    </div>
    <div v-if="this.$store.getters.token" class="share_info">
      <span>전화번호: </span>
      <p>
        {{ this.equip.construction_user_phone }}
      </p>
    </div>
    <div class="share_info">
      <span>기타: </span>
      <p>
        {{ this.equip.construction_etc }}
      </p>
    </div>
    <button @click="equipRegist">신청하기</button>
  </div>
</template>

<script>
  import Swiper, { Navigation, Pagination, Autoplay } from 'swiper'
  Swiper.use([Navigation, Pagination, Autoplay])
  import 'swiper/swiper.scss'
  import api from '@/api/api'
  import back from '@/components/layout/back'

  export default {
    name: 'EquipShare',
    components: {
      back
    },
    data() {
      return {
        images: [],
        equip: [],
        visualSlider: null,
        referer: false
      }
    },
    mounted() {
      console.log(this.$route)
      this.getDetail()
      this.referrerCheck()
    },
    beforeDestroy() {
      if (this.visualSlider) {
        this.visualSlider.destroy()
      }
    },
    methods: {
      async equipRegist() {
        if (!this.$store.getters.token || this.$store.getters.token === '') {
          this.$dialog
            .show({
              type: 'alert',
              title: '로그인 정보가 없습니다.'
            })
            .$on('dialog:confirm', () => {
              this.$router.push({ name: 'Main' })
            })
        }
        try {
          const config = {
            headers: {
              'Content-Type': 'application/json; charset = utf-8',
              'Access-Control-Allow-Origin': '*',
              // 'Access-Control-Allow-Headers': '*',
              // 'Access-Control-Allow-Methods': '*',
              authorization: `Bearer ${this.$store.getters.token}`
            }
          }
          const body = {
            equipIdx: this.equip.construction_idx,
            userIdx: this.equip.construction_user_idx
          }
          api
            .post('equip/inert', body, config)
            .then(result => {
              if (result.data.success) {
                if (result.data.message === 'success') {
                  this.$dialog
                    .show({
                      type: 'alert',
                      title: '신청완료'
                    })
                    .$on('dialog:confirm', () => {
                      this.$router.push({
                        name: 'detailChat',
                        query: {
                          idx: result.data.data,
                          name: this.equip.user_name
                        }
                      })
                    })
                  // this.list.map(val => {
                  //   if (val.work_idx === workIdx) {
                  //     val.work_status = 2
                  //   }
                  //   return val
                  // })
                  // window.location.href = 'tel://' + phone
                  // // this.goPage(workIdx)
                  this.$router.push({ name: 'Main' })
                }
              } else {
                this.$dialog
                  .show({
                    type: 'alert',
                    title: result.data.message
                  })
                  .$on('dialog:confirm', () => {
                    this.$router.push({ name: 'Main' })
                  })
              }
            })
            .catch(err => console.log(err))
        } catch (err) {
          console.log(err)
          alert('에러발생')
          return
        }
      },
      phoneSlice(phone) {
        // const phone = '010-4797-8158'
        const phoneStr = phone?.match(/\d{2,3}-\d{3,4}-\d{4}/gi)
        // console.log(phoneStr)
        const maskingStr = phone
          .toString()
          .replace(
            phoneStr,
            phoneStr?.toString().replace(/-[0-9]{4}-/g, '-****-')
          )
        return (this.phoneNumber = maskingStr)
      },
      lastNameSlice(name) {
        console.log('name')
        const sliceName = name?.split('')

        if (name?.length === 4 || name?.length === 3) {
          sliceName?.forEach((name, index) => {
            console.log(index, sliceName?.length)
            if (index === 0 || index === sliceName?.length - 1) return
            sliceName[index] = '*'
            console.log((sliceName[index] = '*'), name)
          })
          const returnBackName = sliceName?.join()
          console.log(returnBackName?.replace(/,/g, ''))
          return (this.username = returnBackName?.replace(/,/g, ''))
        } else if (name?.length === 2) {
          sliceName[1] = '*'
          const backName = sliceName?.join()?.replace(/,/g, '')
          return (this.username = backName)
        }
      },
      async getDetail() {
        try {
          const config = {
            headers: {
              'Content-Type': 'application/json; charset = utf-8',
              'Access-Control-Allow-Origin': '*',

              authorization: `Bearer ${this.$store.getters.token}`
            }
          }
          await api
            .get(`equip/open/equipImages/${this.$route.params.idx}`, config)
            .then(result => {
              console.log(result)
              if (
                result.data.data.result !== undefined &&
                result.data.data.result !== null
              ) {
                this.images = result.data.data.result
                this.equip = result.data.data.equip
                this.$nextTick(this.setVisualSwiper)
              } else {
                this.$dialog.show({
                  type: 'alert',
                  title: '통신 실패'
                })
              }
            })
            .catch(err => console.log(err))
        } catch (err) {
          console.log(err)
          alert('에러발생')
          return
        }
      },
      setVisualSwiper() {
        const options = {
          // autoplay: {
          //   delay: 2000
          // },
          loop: true,
          centeredSlides: true,
          slidesPerView: 1,
          pagination: {
            el: this.$refs.pagination,
            type: 'bullets',
            clickable: true,
            renderBullet: function (index, className) {
              return `<button type="button" class="${className}"><span hidden>${
                index + 1
              }</span></button>`
            },
            bulletActiveClass: 'active',
            bulletClass: 'swiper-pagination-bullet',
            bulletElement: 'button',
            modifierClass: ''
          },
          autoplay: {
            delay: 5000
          }
        }

        this.visualSlider = new Swiper(this.$refs.visualSwiper, options)
      },
      onEnded() {
        setTimeout(() => {
          this.visualSlider.slideNext()
        }, 300)
      },
      referrerCheck() {
        if (history.length > 3) {
          this.referer = true
        }
      }
    }
  }
</script>
<style scoped>
  #map {
    max-width: 100%;
    height: 70vh;
  }
</style>
