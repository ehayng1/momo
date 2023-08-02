<template>
  <section class="search">
    <h1 hidden>검색</h1>

    <back />
    <div class="search_box">
      <text-input
        type="text"
        placeholder="건설사 또는 현장 검색"
        v-model="word"
      >
        <template v-slot:addon>
          <search class="icon" @click="onSearch"></search>
        </template>
      </text-input>
    </div>

    <tab :list="tabList" :current="tabCurrent" @input="onChangeTab" />

    <div class="no_data" v-if="!list">
      <dl v-if="tabCurrent === 1 || tabCurrent === 2">
        <dt class="title">검색 TIP</dt>
        <dd class="desc">건설사 및 현장명의 일부만 입력해보세요</dd>
        <dd class="ex">SK 건설 → ‘SK’</dd>
        <dd class="ex">호반베르디움 → ‘호반’ 또는 ‘베르디움’</dd>
      </dl>

      <dl v-if="tabCurrent === 3">
        <dt class="title">검색 TIP</dt>
        <dd class="desc">일자리 검색의 일부만 입력해보세요</dd>
        <dd class="ex">수원체육문화센터 → '수원'</dd>
      </dl>
      <dl v-if="tabCurrent === 4">
        <dt class="title">검색 TIP</dt>
        <dd class="desc">장비 검색을 해주세요</dd>
      </dl>
    </div>

    <!-- <div class="list" v-if="tabCurrent === 1">
      <ul v-for="(item, key) in list" :key="key" @click="goInfo(item.site_idx)">
        <li class="SCard">
          <div class="image site">
            <img
              :src="
                item.site_picture === undefined || item.site_picture === null
                  ? require('@/static/images/main/site_sample.png')
                  : 'https://momobuild.s3.ap-northeast-2.amazonaws.com/' +
                    item.site_picture
              "
              alt=""
            />
          </div>
          <dl>
            <div class="left">
              <dt class="title">{{ item.site_name }}</dt>
              <dd class="desc">{{ item.site_address }}</dd>
            </div>
            <dd class="logo">
              <img
                :src="
                  item.company_logo === undefined || item.company_logo === null
                    ? ''
                    : 'https://momobuild.s3.ap-northeast-2.amazonaws.com/' +
                      item.company_logo
                "
                alt=""
              />
            </dd>
          </dl>
        </li>
      </ul>
    </div>

    <div class="list" v-if="tabCurrent === 2">
      <ul>
        <li
          class="card"
          v-for="(item, key) in list"
          :key="key"
          @click="goCompany(item.company_idx)"
        >
          <div class="image logo">
            <img
              :src="
                item.company_logo === undefined || item.company_logo === null
                  ? ''
                  : 'https://momobuild.s3.ap-northeast-2.amazonaws.com/' +
                    item.company_logo
              "
              alt=""
            />
          </div>
          <dl>
            <dt class="title">{{ item.company_name }}</dt>
            <dd class="synthesis">
              총점
              {{
                item.company_total === undefined || item.company_total === null
                  ? '평가 전'
                  : item.company_total
              }}
            </dd>
          </dl>
        </li>
      </ul>
    </div> -->

    <div class="list" v-if="tabCurrent !== 4">
      <ul class="">
        <!--hunting(구직) or opening(구인)-->
        <!-- HumanResourcesCard ==> HRCard -->
        <div v-for="(item, key) in list" :key="key">
          <li
            class="HRCard"
            v-bind:class="item.work_type === 0 ? 'hunting' : 'opening'"
            @click="
              workDetail(
                item.work_user_phone,
                item.work_idx,
                item.work_location,
                item
              )
            "
          >
            <dl>
              <dt class="title" style="display: flex">
                {{ item.work_title }}
                <div v-if="item.work_divide_first">
                  <span
                    class="occupation"
                    v-for="(inner, key) in item.subList"
                    :key="key"
                  >
                    {{ inner }}
                  </span>
                  <span v-if="item.work_divide_second" class="occupation">
                    {{ item.work_divide_second }}
                  </span>
                </div>
              </dt>
              <dd class="address">{{ item.work_location }}</dd>
              <div class="footer">
                <dd class="date">
                  {{ item.work_period_start }} ~
                  {{ item.work_period_end }}
                </dd>
                <dd class="pay" v-if="item.work_type === 0">
                  <em>일</em> {{ item.work_payment }}원
                </dd>
              </div>
            </dl>
          </li>
        </div>
      </ul>
    </div>

    <div class="list" v-if="tabCurrent === 4">
      <ul class="">
        <!-- EquipmentCard ==> ECard -->
        <div v-for="(item, key) in list" :key="key">
          <li
            class="ECard"
            :class="{
              rental: item.construction_type === 0,
              lease: item.construction_type === 1
            }"
            @click="
              equipDetail(
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
                <img :src="item.construction_img" alt="" />
              </div>
            </div>
            <dl>
              <div class="header">
                <dt class="title">{{ item.construction_title }}</dt>
                <dd class="year">
                  연식 : {{ item.construction_divide_second }}년
                </dd>
              </div>
              <dd class="phone">{{ item.construction_user_phone }}</dd>
              <!--<a href='tel:010-1234-5678'>-->
              <dd class="date">
                <!-- {{ item.construction_period_start }} ~
                {{ item.construction_period_end }} -->
                <!-- <span class="address"> -->
                {{ item.construction_location }}
                {{ item.construction_location_detail }}
                <!-- </span> -->
              </dd>
            </dl>
          </li>
        </div>
      </ul>
    </div>
  </section>
</template>

<script>
  import textInput from '@/components/common/input'
  import tab from '@/components/common/tab'
  import search from '@/static/images/common/search.svg'
  import api from '@/api/api'
  import back from '@/components/layout/back'
  import moment from 'moment'
  export default {
    name: 'Search',
    components: {
      textInput,
      tab,
      search,
      back,

      // KakaoMap,
      // eslint-disable-next-line vue/no-unused-components
      moment
    },
    data() {
      return {
        word: '',
        list: null,
        tabCurrent: 1,
        category: 'site',
        tabList: [
          { id: 1, label: '현장', category: 'site' },
          { id: 2, label: '건설사', category: 'company' },
          { id: 3, label: '일자리', category: 'work' },
          { id: 4, label: '장비', category: 'equip' }
        ]
      }
    },
    watch: {
      tabCurrent() {
        this.onSearch()
      }
    },
    methods: {
      async onSearch(e) {
        if (this.word.trim().length === 0) {
          return false
        }
        e.preventDefault()
        try {
          const config = {
            headers: {
              'Content-Type': 'application/json; charset = utf-8',
              'Access-Control-Allow-Origin': '*',
              authorization: `Bearer ${this.$store.getters.token}`
            }
          }
          await api
            .get(`search/open/${this.category}/${this.word}`, config)
            .then(result => {
              console.log(result)
              if (result.data.success) {
                if (
                  result.data.data !== undefined &&
                  result.data.data !== null
                ) {
                  // if (this.category === 'work') {
                  if (this.category !== 'equip') {
                    let list = result.data.data.result
                    let subList = []
                    list = list.map(value => {
                      if (String(value.work_divide_first).length > 0) {
                        subList = value.work_divide_first.split(':')
                      }
                      value.subList = subList
                      if (
                        value.work_payment !== null &&
                        value.work_payment !== null &&
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
                      return value
                    })
                    this.list = list
                  } else if (this.category === 'equip') {
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
                    this.list = list
                  } else {
                    this.list = result.data.data.result
                  }
                }
              } else {
                this.list = []
                // alert 같은거 띄우기
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
      onChangeTab(id, category) {
        console.log(
          '탭 바뀌면 api 다시 호출. 값이 없으면 값이 없는 페이지 필요. '
        )
        this.tabCurrent = id
        // eslint-disable-next-line no-undef
        this.category = category
        this.list = null
      },
      goInfo(idx) {
        this.$router.push({ name: 'ConstructionInfo', query: { siteIdx: idx } })
      },
      goCompany(idx) {
        this.$router.push({ name: 'Construction', query: { companyIdx: idx } })
      },
      async HRCheck(phone, workIdx) {
        //구인카드 클릭시
        // this.$router.push({name: 'ConstructionInfo'})
        if (this.$store.getters.token === '') {
          this.$dialog.show({
            type: 'alert',
            title: '로그인 후 이용해주세요'
          })
        } else {
          //구직카드 클릭시 통화하기 팝업출력
          await this.$dialog
            .show({
              type: 'confirm', //confirm or alert
              title: '신청하시겠습니까?',
              message:
                '신청을 하시면 등록된 전화번호로 바로 전화 연결이 됩니다.',
              data: {
                confirmText: '신청'
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
                  workIdx
                }
                api
                  .post('job/request', body, config)
                  .then(result => {
                    // console.log(result)
                    if (result.data.success) {
                      if (result.data.message === 'success') {
                        // window.location.href = 'tel://' + phone
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
        }
      },
      async equipCheck(phone) {
        if (this.$store.getters.token === '') {
          this.$dialog.show({
            type: 'alert',
            title: '로그인 후 이용해주세요'
          })
        } else {
          // 카드 클릭시 통화하기 팝업출력
          await this.$dialog
            .show({
              type: 'confirm', //confirm or alert
              title: '신청하시겠습니까?',
              message:
                '신청을 하시면 등록된 전화번호로 바로 전화 연결이 됩니다.',
              data: {
                confirmText: '신청'
              }
            })
            .$on('dialog:confirm', () => {
              console.log('카드에 등록된 전화번호로 바로 전화연결')
              // try {
              //   const config = {
              //     headers: {
              //       'Content-Type': 'application/json; charset = utf-8',
              //       'Access-Control-Allow-Origin': '*',
              //       // 'Access-Control-Allow-Headers': '*',
              //       // 'Access-Control-Allow-Methods': '*',
              //       authorization: `Bearer ${this.$store.getters.token}`
              //     }
              //   }
              //   const body = {
              //     workIdx
              //   }
              //   api
              //     .post('job/request', body, config)
              //     .then(result => {
              //       console.log(result)
              //       if (result.data.success) {
              //         if (result.data.message === 'success') {
              window.location.href = 'tel://' + phone
              //         }
              //       }
              //     })
              //     .catch(err => console.log(err))
              // } catch (err) {
              //   console.log(err)
              //   alert('에러발생')
              //   return
              // }
            })
        }
      },
      async workDetail(phone, workIdx, currentLocation, allItem) {
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
                        // this.list.map(val => {
                        //   if (val.work_idx === workIdx) {
                        //     val.work_status = 2
                        //   }
                        //   return val
                        // })
                        // alert('신청완료')
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
                                name: allItem.work_user_name
                              }
                            })
                          })
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
          // eslint-disable-next-line no-unused-vars
          .$on('toggleLikeList', (idx, type) => {
            this.list = this.list.map(val => {
              if (val.work_idx === idx) {
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
                        // this.list.map(val => {
                        //   if (val.construction_idx === item.construction_idx) {
                        //     val.construction_status = 2
                        //   }
                        //   return val
                        // })
                        // window.location.href = 'tel://' + phone
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
                                name: item.user_name
                              }
                            })
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
      }
    }
  }
</script>
