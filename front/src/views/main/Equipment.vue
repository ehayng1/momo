<template>
  <div class="main" ref="container">
    <h2 hidden>장비</h2>

    <div class="equipment">
      <!-- <a @click="checkRegist('Rental')">
        <button type="button" class="btn-login">장비임대</button>
      </a>
      <div class="space" />
      <a @click="checkRegist('Lease')">
        <button type="button" class="btn-login">장비임차</button>
      </a> -->
      <!-- <a @click="checkRegist('EquipRegist')">
        <button type="button" class="btn-equipment">내 장비 올리기</button>
      </a> -->
      <div class="sort_area" style="text-align: center; margin-left: 10px">
        <div style="" class="sort_choice">
          <div class="select" style="cursor: pointer" @click="showCheckList(0)">
            <div class="selector">
              <div class="label" style="font-size: 1.4rem; color: #6c747c">
                <!-- <span>{{ selected.text }}</span> -->
                장비 종류
              </div>
              <div class="arrow"></div>
            </div>
          </div>
        </div>
        <div style="" class="sort_choice">
          <div class="select" style="cursor: pointer" @click="showCheckList(1)">
            <div class="selector">
              <div class="label" style="font-size: 1.4rem; color: #6c747c">
                <span>지역</span>
              </div>
              <div class="arrow"></div>
            </div>
          </div>
        </div>
        <!-- <div style="width: 50%" class="sort_choice">
          <select-box
            style="background-color: #d7d8dc; width: 100%"
            text="종류"
            :list="categories"
            @change="changeCategory"
            @click.stop="showCheckList(0)"
          />
        </div> -->

        <!-- <div style="width: 50%" class="sort_choice">
          <div style="width: 100%">
            <select-box
              style="background-color: #d7d8dc; width: 100%"
              text="지역"
              :list="subCategories"
              @change="changeSubCategory"
              @click.stop="showCheckList(1)"
            />
          </div>
        </div> -->

        <!-- <div class="sort_box">
          <button type="button" class="type1">임대</button>
          <button type="button" class="type2">임차</button>
        </div> -->
      </div>

      <div class="list">
        <ul class="" v-if="list.length">
          <li
            class="card"
            :class="{
              rental: item.construction_type === 0,
              lease: item.construction_type === 1
            }"
            v-for="(item, key) in list"
            :key="key"
            @click="
              goDetail(
                item.construction_user_phone,
                item.construction_status,
                item
              )
            "
          >
            <div class="image">
              <div
                class="image_inner"
                v-bind:style="{
                  backgroundColor:
                    item.construction_img === undefined ||
                    item.construction_img === ''
                      ? '#e3e5e5'
                      : '#FFF'
                }"
              >
                <!-- <img
                  :src="
                    require('@/static/images/main/equipment_card_sample.jpeg')
                  "
                  alt=""
                /> -->
                <!-- <img :src="item.construction_img" alt="" /> -->
              </div>
            </div>
            <dl>
              <dt
                class="title"
                :class="{ type2: item.construction_status === 2 }"
                style="justify-content: space-between"
              >
                {{ item.construction_title }}
                <!-- <div class="look">
                  <fa
                    @click.stop="
                      toggleLike(item.like_status, item.construction_idx, item)
                    "
                    :icon="
                      item.like_status === 0
                        ? 'fa-solid fa-heart'
                        : 'fa-regular fa-heart'
                    "
                    :style="{
                      color: item.like_status === 0 ? 'red' : '#cdcdcd',
                      marginRight: 5,
                      cursor: 'pointer'
                    }"
                    class="heart"
                  />
                </div> -->
              </dt>
              <dd
                class="address"
                :class="{ type2: item.construction_status === 2 }"
              >
                <img
                  :src="require('@/static/images/common/worklocation.png')"
                  alt=""
                  style="margin-right: 0px; display: inline-block"
                />
                {{ item.construction_location }}
                {{ item.construction_location_detail }}
              </dd>
              <!-- <div class="footer">
                <dd class="date">
                  {{ item.construction_period_start }} ~
                  {{ item.construction_period_end }}
                </dd>
              </div> -->
            </dl>
            <!-- <dl>
              <div class="header">
                <dt class="title">{{ item.construction_title }}</dt>
                <dd class="year">
                  연식 : {{ item.construction_divide_second }}년
                </dd>
              </div>
              <dd class="phone">{{ item.construction_user_phone }}</dd>
              <dd class="date">
                {{ item.construction_period_start }} ~
                {{ item.construction_period_end }}
                <span class="address">
                  {{ item.construction_location }}
                  {{ item.construction_location_detail }}
                </span>
              </dd>
            </dl> -->
          </li>
        </ul>
        <ul v-else class="">
          <li class="empty">
            <dl>
              <dd class="icon">
                <subtitlesOff />
              </dd>
              <dd class="content">장비목록이 존재하지 않습니다.</dd>
            </dl>
          </li>
        </ul>
      </div>
      <div class="addButton" @click="checkRegist('EquipRegist')">
        <fa :icon="['fa', 'plus']" style="width: 100%; height: 45px" />
      </div>
    </div>
  </div>
</template>

<script>
  import subtitlesOff from '@/static/images/common/subtitles_off.svg'
  import api from '@/api/api'
  import moment from 'moment'
  import equip from '@/components/common/equip'
  import shortArea from '@/components/common/shortArea'
  import equipCheckList from '@/components/popup/equipCheckList'
  // import conditionCheckListVue from '../../components/popup/conditionCheckList.vue'
  // import conditionCheckList from '@/components/popup/conditionCheckList'

  export default {
    name: 'Equipment',
    components: {
      // eslint-disable-next-line vue/no-unused-components
      selectBox: () => import('@/components/common/selectbox'),
      subtitlesOff
    },
    data() {
      return {
        category: 'all',
        categories: equip,
        selected: equip[0],
        subCategories: shortArea,
        subSelected: shortArea[0],
        subCategory: 'all',
        list: [],
        pageNo: 1,
        scrollPosition: 0,
        isLoading: true
      }
    },
    mounted() {
      this.getList()
    },
    created() {
      window.addEventListener('scroll', this.handleScroll)
    },
    destroyed() {
      window.removeEventListener('scroll', this.handleScroll)
    },
    methods: {
      changeCategory(v) {
        this.selected = v
        this.category = v.value
        this.list = []
        this.pageNo = 1
        this.isLoading = true
        this.getList()
      },
      changeSubCategory(v) {
        this.subSelected = v
        this.subCategory = v.value
        this.list = []
        this.pageNo = 1
        this.isLoading = true
        this.getList()
      },
      async showCheckList(v) {
        console.log(v)
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
          await api
            .get(`equip/open/count/${v}`, config)
            .then(result => {
              console.log(result)
              if (result.data.success) {
                if (result.data.message === 'success') {
                  let list = v === 0 ? this.categories : this.subCategories
                  list = list.map(outer => {
                    result.data.data.result.map(inner => {
                      if (outer.text === inner.text) {
                        outer.cnt = inner.cnt
                        return inner
                      }
                    })
                    return outer
                  })
                  console.log(list)
                  this.$dialog
                    .show({
                      template: equipCheckList,
                      // template: conditionCheckList,
                      data: {
                        list,
                        type: v
                      }
                    })
                    .$on('dialog:confirm', v2 => {
                      console.log(v2)
                      if (v === 0) {
                        this.categories.map(val => {
                          if (val.value === v2) {
                            this.selected = val
                            this.category = v2
                          }
                        })
                      } else {
                        this.subCategories.map(val => {
                          if (val.value === v2) {
                            this.subSelected = val
                            this.subCategory = v2
                          }
                        })
                      }
                      this.list = []
                      this.pageNo = 1
                      this.isLoading = true
                      this.getList()
                    })
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
          alert('에러발생')
          return
        }
      },
      checkRegist(page) {
        if (this.$store.getters.token === '') {
          this.$dialog.show({
            type: 'alert',
            title: '로그인 후 이용해주세요.'
          })
          return false
        } else {
          this.$router.push({ name: page })
        }
      },
      goPage(type) {
        if (type === 'rental') {
          this.$router.push({ name: 'Rental' })
        } else {
          this.$router.push({ name: 'Lease' })
        }
      },
      async goDetail(phone, status, item) {
        if (this.$store.getters.token === '') {
          this.$dialog.show({
            type: 'alert',
            title: '로그인 후 이용해주세요'
          })
        } else {
          // 카드 클릭시 통화하기 팝업출력
          if (status === 2) {
            return
          }
          await this.$dialog
            .show({
              type: 'confirm', //confirm or alert
              title: '빌리시겠습니까?',
              message:
                '신청을 하시면 등록된 전화번호로 바로 전화 연결이 됩니다.',
              data: {
                confirmText: '신청',
                equip: item,
                token: this.$store.getters.token
              }
            })
            .$on('dialog:confirm', () => {
              console.log('카드에 등록된 전화번호로 바로 전화연결')
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
                  equipIdx: item.construction_idx,
                  userIdx: item.construction_user_idx
                }
                api
                  .post('equip/inert', body, config)
                  .then(result => {
                    console.log(result)
                    if (result.data.success) {
                      if (result.data.message === 'success') {
                        this.list.map(val => {
                          if (val.construction_idx === item.construction_idx) {
                            val.construction_status = 2
                          }
                          return val
                        })
                        window.location.href = 'tel://' + phone
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
                alert('에러발생')
                return
              }
            })
            // eslint-disable-next-line no-unused-vars
            .$on('toggleLikeList', (idx, type) => {
              console.log('442')
              this.list = this.list.map(val => {
                if (val.construction_idx === idx) {
                  console.log(val.like_status)
                  // if (
                  //   val.like_status === undefined ||
                  //   val.like_status === null ||
                  //   val.like_status === 1
                  // ) {
                  //   val.like_status = 0
                  // } else {
                  //   val.like_status = 1
                  // }
                }
                return val
              })
            })
        }
      },
      async getList() {
        if (this.isLoading === false) {
          return false
        }
        try {
          const config = {
            headers: {
              'Content-Type': 'application/json; charset = utf-8',
              'Access-Control-Allow-Origin': '*',
              authorization: `Bearer ${this.$store.getters.token}`
            }
          }
          await api
            .get(
              `equip/open/${this.category}/${this.subCategory}/${this.pageNo}`,
              config
            )
            .then(result => {
              console.log(result)
              if (result.data.success) {
                if (
                  result.data.data.result !== undefined &&
                  result.data.data.result !== null
                ) {
                  let list = result.data.data.result
                  list = list.map(value => {
                    if (
                      value.construction_period_start !== undefined &&
                      value.construction_period_start !== null
                    ) {
                      value.construction_period_start = `${moment(
                        value.construction_period_start
                      ).format('YYYY.MM.DD')}(${moment(
                        value.construction_period_start
                      )
                        .locale('ko')
                        .format('dd')})`
                    }
                    if (
                      value.construction_period_end !== undefined &&
                      value.construction_period_end !== null
                    ) {
                      value.construction_period_end = `${moment(
                        value.construction_period_end
                      ).format('YYYY.MM.DD')}(${moment(
                        value.construction_period_end
                      )
                        .locale('ko')
                        .format('dd')})`
                    }
                    if (
                      value.construction_img !== undefined &&
                      value.construction_img !== null &&
                      value.construction_img !== ''
                    ) {
                      value.construction_img =
                        'https://momobuild.s3.ap-northeast-2.amazonaws.com/' +
                        value.construction_img
                    }
                    return value
                  })
                  console.log(list)
                  this.list = this.list.concat(list)
                  this.pageNo += 1
                  this.isLoading = false
                } else {
                  if (result.data.code === 2121) {
                    this.list = []
                  }
                  this.$dialog.show({
                    type: 'alert',
                    title: result.data.message
                  })
                }
              }
            })
            .catch(err => console.log(err))
        } catch (err) {
          console.log(err)
        }
      },
      handleScroll() {
        // console.log(this.$refs.container.scrollHeight)
        // console.log(window.scrollY + window.innerHeight)

        if (
          window.scrollY + window.innerHeight >
          this.$refs.container.scrollHeight
        ) {
          if (this.list.length === (this.pageNo - 1) * 10) {
            if (this.isLoading === false) {
              this.isLoading = true
              this.getList()
            }
          }
        }
      },
      async toggleLike(status, boardNum, item) {
        if (!this.$store.getters.token || this.$store.getters.token === '') {
          return this.$dialog.show({
            type: 'alert',
            title: '로그인 후 이용 해주세요 '
            // return false
          })
        }
        let url = ''
        if (status === undefined || status === null) {
          url = 'like/insert'
        } else {
          url = 'like/update'
        }

        try {
          const config = {
            headers: {
              'Content-Type': 'application/json; charset = utf-8',
              'Access-Control-Allow-Origin': '*',
              authorization: `Bearer ${this.$store.getters.token}`
            }
          }
          const body = {
            type: 1,
            boardNum
          }
          await api
            .post(url, body, config)
            .then(result => {
              console.log(result)
              let arr = this.list.map(val => {
                if (val.construction_idx === item.construction_idx) {
                  if (status === undefined || status === null || status === 1) {
                    val.like_status = 0
                  } else {
                    val.like_status = 1
                  }
                }
                return val
              })

              this.list = arr
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
