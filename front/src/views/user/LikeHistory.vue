<template>
  <section class="page applicaion" ref="container" style="padding: 0">
    <back />
    <div class="info">
      <h1 style="text-align: center" class="title">관심 목록</h1>
    </div>

    <tab :list="tabList" :current="tabCurrent" @input="onChangeTab" />
    <div class="human_resources">
      <div class="list" style="margin: 0">
        <ul v-if="tabCurrent !== 3 && list.length">
          <!-- <li
            :class="[{ back: item.work_history_status === 1 }]"
            class="card"
            v-for="(item, key) in list"
            :key="key"
            @click="goPage(item.work_idx, tabCurrent)"
          >
            <dl>
              <dt class="title">
                <img
                  v-if="item.company_logo !== undefined"
                  :src="item.company_logo"
                  alt=""
                />
                <p>{{ item.work_title }}</p>
              </dt>
              <dd class="phone">{{ item.work_user_phone }}</dd>
              <dd class="date">
                {{ item.work_period_start }} ~ {{ item.work_period_end }}
                <span v-if="item.company_total" class="career">
                  종합 {{ item.company_total }}
                </span>
              </dd>
            </dl>
          </li> -->
          <div style="border: 0 solid">
            <li
              class="card"
              :class="{
                hunting: item.work_type === 0,
                opening: item.work_type === 1
              }"
              v-for="(item, key) in list"
              :key="key"
              @click="
                goDetail(
                  item.work_user_phone,
                  item.work_idx,
                  item.work_location,
                  item
                )
              "
            >
              <dl>
                <div style="display: flex; flex-direction: row">
                  <dt
                    style="flex: 1"
                    class="title"
                    :class="{ type2: item.work_status === 2 }"
                  >
                    <div>
                      {{
                        item.work_title.length >= 10
                          ? `${item.work_title}...`
                          : item.work_title
                      }}
                    </div>
                  </dt>
                  <div style="display: flex; align-items: center">
                    <div
                      v-if="item.best"
                      style="
                        font-size: 20px;
                        font-weight: 600;
                        margin-right: 5px;
                      "
                      :style="[
                        {
                          color: item.work_status === 2 ? '#dddddd' : '#CD0000'
                        }
                      ]"
                    >
                      인기
                    </div>
                    <div class="look">
                      <fa
                        @click.stop="
                          toggleLike(item.like_status, item.work_idx, item)
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
                    </div>
                    <div class="look">
                      <look style="margin-right: 5px" />{{ item.view_count }}
                    </div>
                  </div>
                </div>
                <div class="title">
                  <span
                    style="margin: 0"
                    v-for="(innerItem, key) in item.subList"
                    :key="key"
                    class="occupation"
                    :class="{ type2: item.work_status === 2 }"
                  >
                    {{ innerItem }}
                  </span>
                  <span
                    v-if="item.work_divide_second"
                    class="occupation"
                    :class="{ type2: item.work_status === 2 }"
                  >
                    {{ item.work_divide_second }}
                  </span>
                </div>
                <dd class="address" :class="{ type2: item.work_status === 2 }">
                  {{ item.work_location }}
                </dd>
                <dd class="address" :class="{ type2: item.work_status === 2 }">
                  {{ item.dist }}
                </dd>
                <div class="footer">
                  <dd class="date" :class="{ type2: item.work_status === 2 }">
                    {{ item.work_period_start }} ~ {{ item.work_period_end }}
                  </dd>
                </div>
                <dd
                  style="
                    padding-bottom: 15px;
                    padding-top: 15px;
                    color: #2d3238;
                    font-weight: bold;
                  "
                  class="pay"
                  v-if="item.work_type === 0"
                  :class="{ type2: item.work_status === 2 }"
                >
                  {{ item.work_payment }}원/일
                </dd>
              </dl>
            </li>
          </div>
        </ul>
        <ul v-else-if="tabCurrent === 3 && list.length">
          <!-- <li
            :class="[{ back: item.construction_status === 1 }]"
            class="card"
            v-for="(item, key) in list"
            :key="key"
          >
            <dl>
              <dt class="title2">
                <span class="name">{{ item.construction_title }}</span>
                <span class="year">
                  연식 {{ item.construction_divide_second }}년
                </span>
              </dt>
              <dd class="phone">{{ item.construction_user_phone }}</dd>
              <dd class="date">
                {{ item.construction_period_start }} ~
                {{ item.construction_period_end }}
                <span class="amount">
                  {{ item.construction_location }}
                  {{ item.construction_location_detail }}
                </span>
              </dd>
            </dl>
          </li> -->
          <li
            class="card"
            :class="{
              rental: item.construction_type === 0,
              lease: item.construction_type === 1
            }"
            v-for="(item, key) in list"
            :key="key"
            @click="
              equipDetail(
                item.construction_user_phone,
                item.construction_status,
                item
              )
            "
          >
            <dl>
              <dt
                class="title"
                :class="{ type2: item.construction_status === 2 }"
                style="justify-content: space-between"
              >
                {{ item.construction_title }}
                <div class="look">
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
                </div>
              </dt>
              <dd
                class="address"
                :class="{ type2: item.construction_status === 2 }"
              >
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
        <ul v-else>
          <li class="empty">
            <dl>
              <dd class="icon">
                <subtitlesOff />
              </dd>
              <dd class="content">목록이 존재하지 않습니다.</dd>
            </dl>
          </li>
        </ul>
      </div>
    </div>
  </section>
</template>

<script>
  import subtitlesOff from '@/static/images/common/subtitles_off.svg'
  import tab from '@/components/common/tab'
  import api from '@/api/api'
  import moment from 'moment'
  import back from '@/components/layout/back'
  import look from '@/static/images/common/view.svg'

  export default {
    components: {
      tab,
      subtitlesOff,
      // eslint-disable-next-line vue/no-unused-components
      back,
      look
    },
    data() {
      return {
        select: 0,
        tabCurrent: 1,
        tabList: [
          {
            id: 1,
            label: '사람 구해요'
          },
          {
            id: 2,
            label: '일자리 구해요'
          },
          {
            id: 3,
            label: '노무'
          }
        ],
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
      this.dialog.onClose()
      window.removeEventListener('scroll', this.handleScroll)
    },
    methods: {
      goPage(idx, current) {
        if (Number(current) === 1) {
          this.$router.push({ name: 'Share', params: { idx: idx } })
        }
      },
      async getList() {
        // const { data } = await mainApi.getCardList()
        // this.ratingList = data
        try {
          const config = {
            headers: {
              'Content-Type': 'application/json; charset = utf-8',
              'Access-Control-Allow-Origin': '*',
              authorization: `Bearer ${this.$store.getters.token}`
            }
          }
          await api
            .get(`mypage/like/${this.tabCurrent - 1}/${this.pageNo}`, config)
            .then(result => {
              console.log(result)
              if (result.data.success) {
                if (
                  result.data.data !== undefined &&
                  result.data.data !== null
                ) {
                  let list = []
                  list = result.data.data.result
                  if (this.tabCurrent !== 3) {
                    let list = result.data.data.result
                    let subList = []
                    let cntList = []
                    list = list.map(value => {
                      if (String(value.work_divide_first).length > 0) {
                        subList = value.work_divide_first.split(':')
                      }
                      value.subList = subList
                      if (
                        value.work_payment !== null &&
                        value.work_payment !== null &&
                        value.work_type !== undefined &&
                        value.work_type === 0
                      ) {
                        value.work_payment =
                          this.$options.filters.autoCommaNumber(
                            value.work_payment
                          )
                      }
                      if (
                        value.work_period_start !== undefined &&
                        value.work_period_start !== null
                      ) {
                        value.work_period_start = `${moment(
                          value.work_period_start
                        ).format('YYYY.MM.DD')}(${moment(
                          value.work_period_start
                        )
                          .locale('ko')
                          .format('dd')})`
                      }
                      if (
                        value.work_period_end !== undefined &&
                        value.work_period_end !== null
                      ) {
                        value.work_period_end = `${moment(
                          value.work_period_end
                        ).format('YYYY.MM.DD')}(${moment(value.work_period_end)
                          .locale('ko')
                          .format('dd')})`
                      }
                      value.best = false
                      cntList.push({
                        idx: value.work_idx,
                        cnt: value.view_count
                      })
                      return value
                    })
                    cntList = cntList.sort((a, b) => {
                      if (a.cnt < b.cnt) return 1
                      if (a.cnt === b.cnt) return 0
                      if (a.cnt > b.cnt) return -1
                    })
                    cntList.map((val, idx) => {
                      if (idx < 3) {
                        list.map(inner => {
                          if (inner.work_idx === val.idx) {
                            inner.best = true
                          }
                          return list
                        })
                      }
                    })
                    cntList = []
                    this.list = this.list.concat(list)
                    this.pageNo += 1
                    this.isLoading = false
                  } else {
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
                    this.list = this.list.concat(list)
                    this.pageNo += 1
                    this.isLoading = false
                  }
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
      },
      async goDetail(phone, workIdx, currentLocation, allItem) {
        // console.log(allItem, '!23')
        // console.log([event.target.classList])
        // if ([...event.target.classList].includes('heart')) {
        //   console.log('1231231231231312314123523463473456')
        //   return false
        // }
        console.log('424')
        if (allItem.work_status === 2) {
          return
        }
        // 조회수 증가
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
            workIdx
          }
          api
            .post('job/open/increase', body, config)
            .then(result => {
              // console.log(result)
              if (result.data.success) {
                // console.log('조회수 증가 성공')
              } else {
                // console.log('조회수 증가 실패')
              }
            })
            .catch(err => console.log(err))
        } catch (err) {
          console.log(err)
          alert('에러발생')
          return
        }
        //구직카드 클릭시 통화하기 팝업출력
        await this.$dialog

          .show({
            // work_location: work_location,
            // currentLocation: currentLocation,
            type: 'confirm', //confirm or alert
            title: '신청하시겠습니까?',
            message: '신청을 하시면 등록된 전화번호로 바로 전화 연결이 됩니다.',

            data: {
              // KakaoMapVue: KakaoMapVue,
              currentLocation: currentLocation,
              allItem: allItem,
              confirmText: '신청',
              token: this.$store.getters.token
            }
          })

          .$on('dialog:confirm', () => {
            if (this.$store.getters.token === '') {
              this.$dialog.show({
                type: 'alert',
                title: '로그인 후 이용해주세요'
              })
            } else {
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
                  workIdx,
                  userIdx: allItem.work_user_idx
                }
                api
                  .post('job/request', body, config)
                  .then(result => {
                    if (result.data.success) {
                      if (result.data.message === 'success') {
                        this.list.map(val => {
                          if (val.work_idx === workIdx) {
                            val.work_status = 2
                          }
                          return val
                        })
                        alert('신청완료')
                        // window.location.href = 'tel://' + phone
                        // this.goPage(workIdx)
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
            }
          })
      },
      async equipDetail(phone, status, item) {
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
          console.log(item)
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
          // .$on('toggleLikeList', (boardNum, type) => {
          //   console.log(boardNum)
          //   console.log(type)
          // })
        }
      },
      // eslint-disable-next-line no-unused-vars
      currentLocation(latitude, longitude) {
        // eslint-disable-next-line no-unused-vars
        function success({ coords, timestamp }) {
          latitude = coords.latitude
          longitude = coords.longitude
          // console.log(
          //   `위도: ${latitude}, 경도: ${longitude}, 위치 반환 시간: ${timestamp}`
          // )
        }
        function getUserLocation() {
          if (!navigator.geolocation) {
            throw '위치 정보가 지원되지 않습니다.'
          }
          navigator.geolocation.getCurrentPosition(success)
        }
        getUserLocation()

        // const latitude = coords.latitude
        // const longitude = coords.longitude
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
      onChangeTab(id) {
        console.log(
          '탭 바뀌면 api 다시 호출. 값이 없으면 값이 없는 페이지 필요. '
        )
        this.tabCurrent = id
        this.btnList = this.tabList[id - 1].btns
        this.select = 0
        this.pageNo = 1
        this.list = []
        this.getList()
      },
      async onButton(val) {
        console.log(val)
        if (this.select === 0) {
          this.$dialog.show({
            type: 'alert',
            title: '카드를 선택해주세요.'
          })
          return false
        } else {
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
              type: this.tabCurrent - 1,
              action: val,
              idx: this.select
            }
            await api
              .put('mypage/myRequest', body, config)
              .then(result => {
                console.log(result)
                if (result.data.success) {
                  this.$dialog
                    .show({
                      type: 'alert', //confirm or alert
                      title: '수정 성공'
                    })
                    .$on('dialog:confirm', () => {
                      this.select = 0
                      this.pageNo = 1
                      this.list = []
                      this.getList()
                    })
                } else {
                  this.$dialog.show({
                    type: 'alert', //confirm or alert
                    title: '수정 실패'
                  })
                }
              })
              .catch(err => console.log(err))
          } catch (err) {
            console.log(err)
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
            type: this.tabCurrent === 3 ? 1 : 0,
            boardNum
          }
          await api
            .post(url, body, config)
            .then(result => {
              console.log(result)
              let arr = this.list.map(val => {
                if (val.work_idx === item.work_idx) {
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
