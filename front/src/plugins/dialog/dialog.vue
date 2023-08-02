<template>
  <div v-if="isShow">
    <transition name="dialog" @after-leave="afterTransitionLeave">
      <div class="dialog" v-if="visible">
        <div class="dialog_contents">
          <!-- <KakaoMap> -->
          <div
            v-if="data && data.currentLocation"
            style="background-color: #eef1f5"
          >
            <KakaoMap
              @coordinateX="coordinateX"
              @coordinateY="coordinateY"
              @distance="distance"
              :location="data.currentLocation"
              :allItem="data.allItem"
            />
          </div>
          <div v-else-if="data && data.equip && data.equip.construction_img">
            <img :src="data.equip.construction_img" alt="장비사진" />
          </div>
          <!-- <div v-for="(item, key) in this.images" :key="key">
            <img
              :src="
                'https://momobuild.s3.ap-northeast-2.amazonaws.com/' +
                item.file_path
              "
              alt="장비사진"
            />
          </div> -->
          <div style="width: 80vw">
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

          <div class="dialog_info" v-if="data && data.currentLocation">
            <div class="dialog_distance">
              거리:
              <p>{{ locationDistance }}</p>
            </div>
            <!-- 공유하기 -->
            <!-- <div class="dialog_bigTitle" v-if="data && data.currentLocation">
              <button @click="onShareClick">공유하기</button>
            </div> -->
            <!-- <div
              class="dialog_infoContent"
              style="
                display: flex;
                width: 100%;
                flex-direction: row;
                justify-content: space-between;
                align-items: center;
              "
            >
              <div style="display: flex; align-items: center">
                <div class="dialog_smallTitle">현장명:</div>
                <div class="dialog_new_page" @click="goNewPage">
                  {{ this.data.allItem.work_title }}
                </div>
              </div>
              <div
                style="
                  display: flex;
                  align-items: center;
                  font-size: 18px;
                  line-height: 14px;
                  color: #7a7e80;
                "
              >
                <fa
                  @click.stop="toggleLike(data, 0)"
                  :icon="
                    like_status === 0
                      ? 'fa-solid fa-heart'
                      : 'fa-regular fa-heart'
                  "
                  :style="{
                    color: like_status === 0 ? 'red' : '#cdcdcd',
                    marginRight: 5,
                    cursor: 'pointer'
                  }"
                  class="heart"
                />
              </div>
            </div> -->

            <div
              style="display: flex; align-items: center; margin-bottom: 1.5rem"
            >
              <div class="dialog_title">
                {{ this.data.allItem.work_title }}
              </div>
            </div>
            <div class="dialog_infoContent">
              <div class="dialog_smallTitle">건설사</div>
              <html-view
                class="dialog_message"
                v-model="this.data.allItem.work_constructor"
              ></html-view>
            </div>
            <div :style="border_style"></div>
            <div class="dialog_infoContent">
              <div class="dialog_smallTitle">업종</div>
              <span class="dialog_message">
                {{ this.data.allItem.work_divide_second }} >
                {{ this.data.allItem.work_divide_first }}
              </span>
            </div>
            <div class="dialog_infoContent">
              <div class="dialog_smallTitle" style="white-space: nowrap">
                주소
              </div>
              <html-view
                class="dialog_message"
                v-model="this.data.allItem.work_location"
              ></html-view>
            </div>
            <div class="dialog_infoContent">
              <div class="dialog_smallTitle">일정</div>
              <html-view
                class="dialog_message"
                v-model="this.data.allItem.work_period_start"
              ></html-view>
              <div class="dialog_message">
                ~ {{ this.data.allItem.work_period_end }}
              </div>
            </div>
            <div class="dialog_infoContent">
              <div class="dialog_smallTitle">단가</div>
              <html-view
                class="dialog_message"
                v-model="this.data.allItem.work_payment"
              ></html-view>
              <div class="dialog_message">/일</div>
            </div>
            <div :style="border_style"></div>
            <div v-if="this.data.token" class="dialog_infoContent">
              <div class="dialog_smallTitle">작성자</div>
              <html-view
                class="dialog_message"
                v-model="ReturnName"
              ></html-view>
            </div>
            <div v-if="this.data.token" class="dialog_infoContent">
              <div class="dialog_smallTitle">연락처</div>
              <html-view
                class="dialog_message"
                v-model="phoneNumber"
              ></html-view>
            </div>
            <div :style="border_style"></div>
            <div v-if="this.data.token" class="dialog_infoContent">
              <div class="dialog_smallTitle">기타</div>
              <html-view
                class="dialog_message"
                v-model="this.data.allItem.work_etc"
              ></html-view>
            </div>
            <!-- <div class="dialog_infoContent">
              <div class="dialog_smallTitle">대분류:</div>
              <html-view
                class="dialog_message"
                v-model="this.data.allItem.work_divide_first"
              ></html-view>
            </div> -->

            <!-- <div v-if="this.data.token" class="dialog_infoContent">
              <div class="dialog_smallTitle">전화번호:</div>
              <html-view
                class="dialog_message"
                v-model="this.data.allItem.work_user_phone"
              ></html-view>
            </div> -->
          </div>
          <div v-else-if="data && data.equip">
            <div
              class="dialog_infoContent"
              style="
                display: flex;
                width: 100%;
                flex-direction: row;
                justify-content: space-between;
                align-items: center;
              "
            >
              <div style="display: flex; align-items: center">
                <div class="dialog_smallTitle">장비이름:</div>
                <html-view
                  class="dialog_message"
                  v-model="this.data.equip.construction_title"
                ></html-view>
              </div>
              <div
                style="
                  display: flex;
                  align-items: center;
                  font-size: 18px;
                  line-height: 14px;
                  color: #7a7e80;
                "
              >
                <fa
                  @click.stop="toggleLike(data, 1)"
                  :icon="
                    like_status === 0
                      ? 'fa-solid fa-heart'
                      : 'fa-regular fa-heart'
                  "
                  :style="{
                    color: like_status === 0 ? 'red' : '#cdcdcd',
                    marginRight: 5,
                    cursor: 'pointer'
                  }"
                  class="heart"
                />
                <!-- <fa
                  @click.stop="toggleLike(data, 1)"
                  :icon="
                    data.equip.like_status === 0
                      ? 'fa-solid fa-heart'
                      : 'fa-regular fa-heart'
                  "
                  :style="{
                    color: data.equip.like_status === 0 ? 'red' : '#cdcdcd',
                    marginRight: 5,
                    cursor: 'pointer'
                  }"
                  class="heart"
                /> -->
              </div>
            </div>
            <div class="dialog_infoContent">
              <div class="dialog_smallTitle">지역:</div>
              <html-view
                class="dialog_message"
                v-model="this.data.equip.construction_location"
              ></html-view>
              &nbsp;
              <html-view
                class="dialog_message"
                v-model="this.data.equip.construction_location_detail"
              ></html-view>
            </div>
            <div class="dialog_infoContent">
              <div class="dialog_smallTitle">종류:</div>
              <html-view
                class="dialog_message"
                v-model="this.data.equip.construction_divide_first"
              ></html-view>
            </div>
            <div class="dialog_infoContent">
              <div class="dialog_smallTitle">연식(년):</div>
              <html-view
                class="dialog_message"
                v-model="this.data.equip.construction_divide_second"
              ></html-view>
            </div>
            <div v-if="this.data.token" class="dialog_infoContent">
              <div class="dialog_smallTitle">전화번호:</div>
              <html-view
                class="dialog_message"
                v-model="this.data.equip.construction_user_phone"
              ></html-view>
            </div>
            <div v-if="data.equip.construction_etc" class="dialog_infoContent">
              <div class="dialog_smallTitle">기타:</div>
              <html-view
                class="dialog_message"
                v-model="this.data.equip.construction_etc"
              ></html-view>
            </div>
          </div>
          <div v-else-if="data && data.allItem && !data.currentLocation">
            <div
              class="dialog_infoContent"
              style="
                display: flex;
                width: 100%;
                flex-direction: row;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 2rem;
              "
            >
              <!-- <div
                class="dialog_infoContent"
                style="display: flex; align-items: center"
              >
                <div class="dialog_smallTitle">이름:</div>
                <html-view
                  class="dialog_message"
                  v-model="this.data.allItem.work_title"
                ></html-view>
              </div>
              <div
                style="
                  display: flex;
                  align-items: center;
                  font-size: 18px;
                  line-height: 14px;
                  color: #7a7e80;
                "
              >
                <fa
                  @click.stop="toggleLike(data, 0)"
                  :icon="
                    like_status === 0
                      ? 'fa-solid fa-heart'
                      : 'fa-regular fa-heart'
                  "
                  :style="{
                    color: like_status === 0 ? 'red' : '#cdcdcd',
                    marginRight: 5,
                    cursor: 'pointer'
                  }"
                  class="heart"
                />
              </div> -->
              <div style="display: flex; align-items: center">
                <div class="dialog_title">
                  {{ this.data.allItem.work_title }}
                </div>
              </div>
            </div>
            <div class="dialog_infoContent">
              <div class="dialog_smallTitle">업종</div>
              <html-view
                class="dialog_message"
                v-model="this.data.allItem.work_divide_first.split(':')[0]"
              ></html-view>
              <html-view
                class="dialog_message"
                :value="'(' + this.data.allItem.work_divide_second + ')'"
              ></html-view>
            </div>
            <div class="dialog_infoContent">
              <div class="dialog_smallTitle">기간</div>
              <html-view
                class="dialog_message"
                v-model="this.data.allItem.work_period_start"
              ></html-view>
              ~
              <html-view
                class="dialog_message"
                v-model="this.data.allItem.work_period_end"
              ></html-view>
            </div>
            <div class="dialog_infoContent">
              <div class="dialog_smallTitle">희망지역</div>
              <html-view
                class="dialog_message"
                v-model="this.data.allItem.work_divide_first.split(':')[1]"
              ></html-view>
            </div>
            <div :style="border_style"></div>
            <div class="dialog_infoContent">
              <div class="dialog_smallTitle">개인/팀</div>
              <html-view
                class="dialog_message"
                v-model="this.data.allItem.work_divide_first.split(':')[1]"
              ></html-view>
            </div>
            <div class="dialog_infoContent">
              <div class="dialog_smallTitle">안전교육</div>
              <html-view
                class="dialog_message"
                :value="
                  this.data.allItem.work_divide_first.split(':').length === 2
                    ? '이수'
                    : '이수 예정'
                "
              ></html-view>
            </div>
            <div v-if="this.data.token" class="dialog_infoContent">
              <div class="dialog_smallTitle">연락처</div>
              <html-view
                class="dialog_message"
                v-model="this.data.allItem.work_user_phone"
              ></html-view>
            </div>
            <div :style="border_style"></div>
            <div v-if="data.allItem.work_etc" class="dialog_infoContent">
              <div class="dialog_smallTitle">기타</div>
              <html-view
                class="dialog_message"
                v-model="this.data.allItem.work_etc"
              ></html-view>
            </div>
          </div>

          <div class="dialog_bigTitle">
            <html-view
              v-if="title"
              class="dialog_title"
              v-model="title"
              :style="titleStyle"
            ></html-view>
          </div>

          <html-view
            v-if="message"
            class="dialog_unactive"
            v-model="message"
          ></html-view>

          <div class="dialog_foot">
            <div class="dialog_foot_buttons">
              <button
                type="button"
                class="btn button_gray"
                v-if="type === 'confirm'"
                @click="onCancel"
              >
                취소
              </button>
              <button
                type="button"
                class="btn button_primary"
                @click="onConfirm"
              >
                {{ (data && data.confirmText) || '확인' }}
              </button>
            </div>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
  import Swiper, { Navigation, Pagination, Autoplay } from 'swiper'
  Swiper.use([Navigation, Pagination, Autoplay])
  import 'swiper/swiper.scss'
  import HtmlView from '../../components/base/htmlView.vue'
  import KakaoMap from '../../components/kakaoMap/KakaoMap.vue'
  import api from '@/api/api'
  export default {
    components: {
      KakaoMap,
      HtmlView
    },
    mounted() {
      this.token = this.data?.token
      // console.log(this.data.token)
      this.lastNameSlice(this?.data?.allItem?.work_user_name)
      this.phoneSlice(this?.data?.allItem?.work_user_phone)
    },
    created() {
      if (this?.data?.equip) {
        this.getEquipImages(this?.data?.equip?.construction_idx)
        this.like_status = this?.data?.equip?.like_status
        this.titleStyle.justifyContent = 'space-between'
      }
      if (this?.data?.allItem) {
        this.like_status = this?.data?.allItem?.like_status
        this.titleStyle.justifyContent = 'space-between'
      }
    },
    data() {
      return {
        border_style:
          'width: 80vw; height: 0.25rem; background-color: #eef1f5; margin-top: 0.5rem; margin-bottom: 0.5rem',
        visualSlider: null,
        // name: this?.data?.allItem?.work_user_name,
        isShow: false,
        locationDistance: 0,
        ReturnName: '',
        visible: false,
        eventStatus: null,
        token: '',
        KaKaoEmitX: '',
        KaKaoEmitY: '',
        worktitle: this?.data?.allItem?.work_title,
        phoneNumber: '',
        type: false,
        images: [],
        like_status: 1,
        titleStyle: {
          justifyContent: 'center'
        }
        // work_title: this.data.allItem.work_title
      }
    },
    beforeMount() {
      // this.lastNameSlice(this?.data?.allItem?.work_user_name)

      this.$on('dialog:open', () => {
        this.visible = true
      })
      this.$on('dialog:close', () => {
        this.visible = false
      })
    },
    beforeDestroy() {
      if (this.visualSlider) {
        this.visualSlider.destroy()
      }
    },
    methods: {
      onShareClick() {
        // // console.log(this.)
        // if (!this.token || this.token === '') {
        //   alert('로그인 후 이용해주세요')
        // } else {
        this.$copyText(
          window.location.host + `/share/${this.data.allItem.work_idx}`
        ).then(() => {
          alert('복사되었습니다')
        })
        // }
      },

      // this.onCancel()
      // console.log(this.$router)
      // this.$router.push({ name: 'Share' })

      phoneSlice(phone) {
        // const phone = '010-4797-8158'
        const phoneStr = phone?.match(/\d{2,3}-\d{3,4}-\d{4}/gi)
        // console.log(phoneStr)
        const maskingStr = phone
          ?.toString()
          .replace(
            phoneStr,
            phoneStr?.toString().replace(/-[0-9]{4}-/g, '-****-')
          )
        return (this.phoneNumber = maskingStr)
      },
      lastNameSlice(name) {
        const sliceName = name?.split('')

        if (name?.length === 4 || name?.length === 3) {
          sliceName?.forEach((name, index) => {
            // console.log(index, sliceName?.length)
            if (index === 0 || index === sliceName?.length - 1) return
            sliceName[index] = '*'
            // console.log((sliceName[index] = '*'), name)
          })
          const returnBackName = sliceName?.join()
          // console.log(returnBackName?.replace(/,/g, ''))
          return (this.ReturnName = returnBackName?.replace(/,/g, ''))
        } else if (name?.length === 2) {
          sliceName[1] = '*'
          const backName = sliceName?.join()?.replace(/,/g, '')
          return (this.ReturnName = backName)
        }
      },
      async goNewPage() {
        if (
          this.KaKaoEmitX <= 0 ||
          this.KaKaoEmitY <= 0 ||
          this.data.allItem.work_title === null
        ) {
          // console.log(
          //   '123123',
          //   `https://map.kakao.com/link/map/${this.data.allItem.work_title},${this.KaKaoEmitY},${this.KaKaoEmitX}`
          // )
          alert('지도가 로딩중입니다. 잠시 기다려주세요')
        } else {
          window.open(
            `https://map.kakao.com/link/map/${this.data.allItem.work_title},${this.KaKaoEmitY},${this.KaKaoEmitX}`
          )
        }

        // window.open = `https://map.kakao.com/link/map/${this.KaKaoEmitY},${this.KaKaoEmitX}`
      },
      distance(v) {
        if (v.length >= 9) {
          this.locationDistance = 0
        } else {
          this.locationDistance = v
          // console.log(v.length, '!@412412')
          // console.log(this.locationDistance, '!@412412')
        }
      },
      coordinateX(v) {
        this.KaKaoEmitX = v
        // console.log(v, 'X')
      },

      coordinateY(v) {
        this.KaKaoEmitY = v
        // console.log(v, 'Y')
      },
      afterTransitionLeave() {
        this.$dialog.close()
        this.$emit(this.eventStatus)
      },
      onConfirm() {
        this.$emit('dialog:close')
        this.eventStatus = 'dialog:confirm'
      },
      onCancel() {
        this.$emit('dialog:close')
        this.eventStatus = 'dialog:cancel'
      },
      async toggleLike(val, type) {
        if (!val.token || val.token === '') {
          return this.$dialog.show({
            type: 'alert',
            title: '로그인 후 이용 해주세요 '
            // return false
          })
        }
        let url = ''
        let boardNum = ''
        console.log(val)
        console.log(type)

        if (type === 1) {
          if (
            val.equip.like_status === undefined ||
            val.equip.like_status === null
          ) {
            url = 'like/insert'
          } else {
            url = 'like/update'
          }
          boardNum = val.equip.construction_idx
        } else {
          if (
            val.allItem.like_status === undefined ||
            val.allItem.like_status === null
          ) {
            url = 'like/insert'
          } else {
            url = 'like/update'
          }
          boardNum = val.allItem.work_idx
        }

        try {
          const config = {
            headers: {
              'Content-Type': 'application/json; charset = utf-8',
              'Access-Control-Allow-Origin': '*',
              authorization: `Bearer ${val.token}`
            }
          }
          const body = {
            type,
            boardNum
          }
          await api
            .post(url, body, config)
            .then(result => {
              console.log(result)
              if (type === 1) {
                if (
                  val.equip.like_status === undefined ||
                  val.equip.like_status === null ||
                  val.equip.like_status === 1
                ) {
                  val.equip.like_status = 0
                  this.like_status = 0
                } else {
                  val.equip.like_status = 1
                  this.like_status = 1
                }
              } else {
                if (
                  val.allItem.like_status === undefined ||
                  val.allItem.like_status === null ||
                  val.allItem.like_status === 1
                ) {
                  val.allItem.like_status = 0
                  this.like_status = 0
                } else {
                  val.allItem.like_status = 1
                  this.like_status = 1
                }
              }
              this.$emit('toggleLikeList', boardNum, type)
            })
            .catch(err => console.log(err))
        } catch (err) {
          console.log(err)
        }
      },
      async getEquipImages(idx) {
        try {
          const config = {
            headers: {
              'Content-Type': 'application/json; charset = utf-8',
              'Access-Control-Allow-Origin': '*',
              authorization: `Bearer ${this.token}`
            }
          }
          await api
            .get(`equip/open/equipImages/${idx}`, config)
            .then(result => {
              console.log(result.data.data.result)

              this.images = result.data.data.result

              console.log(this.images)
              this.$nextTick(this.setVisualSwiper)
            })
            .catch(err => console.log(err))
        } catch (err) {
          console.log(err)
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
      }
    }
  }
</script>
