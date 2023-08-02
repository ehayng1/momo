<template>
  <div class="main" ref="container">
    <h2 hidden>평가</h2>

    <div class="button_area">
      <a :class="[{ unactive: select !== '0' }]" @click="click('Main')">
        <button type="button" class="btn-login">실시간 HOT</button>
      </a>
      <div class="space" />
      <a :class="[{ unactive: select !== '1' }]" @click="click('FreeBoard')">
        <button type="button" class="btn-login">자유게시판</button>
      </a>
      <a :class="[{ unactive: select !== '2' }]">
        <button type="button" class="btn-login">일자리게시판</button>
      </a>
    </div>

    <section class="main_banner">
      <div ref="visualSwiper" class="swiper">
        <div class="swiper-wrapper">
          <div
            v-for="(item, key) in bannerList"
            :key="key"
            class="swiper-slide visual"
          >
            <router-link :to="{ path: item.link }">
              <img :src="item.src" alt="" />
            </router-link>
          </div>
          <div v-if="bannerList.length <= 0" class="empty">empty</div>
        </div>
        <div ref="pagination" class="swiper-pagination" />
      </div>
    </section>

    <div style="background-color: #f6f8fb">
      <div class="list" v-if="select === '1'">
        <ul v-if="issueList.length" class="">
          <li
            class="card"
            v-for="(item, key) in issueList"
            :key="key"
            @click="goIssuePage(item.issue_idx)"
          >
            <dl>
              <div
                style="
                  display: flex;
                  flex-direction: row;
                  align-items: center;
                  margin-bottom: 2rem;
                "
              >
                <dd style="margin-right: 1rem">
                  <profile />
                </dd>
                <div
                  style="
                    display: flex;
                    flex-direction: column;
                    justify-content: center;
                  "
                >
                  <dd class="id">
                    {{ item.nickname ? item.nickname : item.code_blockchain }}
                  </dd>
                  <dd class="date" style="">
                    <!-- <span>{{ item.timestamp_create }}</span> -->
                    {{ item.timestamp_create }}
                  </dd>
                </div>
              </div>
              <dd class="title">{{ item.issue_title }}</dd>
              <dd class="content">
                {{
                  item.issue_content.length > 95
                    ? item.issue_content.substring(0, 95) + '...'
                    : item.issue_content
                }}
              </dd>

              <div class="space" />

              <dd class="date">
                <div class="bottom_left">
                  <!-- image preview deactivated -->
                  <!-- <fa
                    v-if="item.check_image === 0"
                    :icon="['fa', 'image']"
                    style="margin-right: 5px"
                  />
                  <fa
                    v-if="item.check_video === 0"
                    :icon="['fa', 'camera']"
                    style="margin-right: 5px"
                  /> -->
                  <like />
                  <!-- <div></div> -->
                  좋아요 {{ (Math.random() * 20).toFixed(0) }}
                  <look style="margin-left: 1rem" /> 조회
                  {{ item.view_count }}
                </div>
                <div class="bottom_right">
                  <comment />
                  댓글

                  {{
                    item.reply_count === undefined || item.reply_count === null
                      ? 0
                      : item.reply_count
                  }}
                </div>
              </dd>
            </dl>
          </li>
        </ul>
        <ul v-else class="">
          <li class="empty">
            <dl>
              <dd class="icon">
                <subtitlesOff />
              </dd>
              <dd class="content">게시글이 존재하지 않습니다.</dd>
            </dl>
          </li>
        </ul>
      </div>

      <div class="jobList" v-if="select === '2'">
        <ul class="" v-if="jobList.length">
          <!--hunting(구인) or opening(구직)-->
          <li
            class="card"
            :class="{
              hunting: item.work_type === 0,
              opening: item.work_type === 1
            }"
            v-for="(item, key) in jobList"
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
              <div
                style="display: flex; flex-direction: row; align-items: center"
              >
                <dd style="margin-right: 1rem">
                  <profile />
                </dd>
                <div
                  style="
                    display: flex;
                    flex-direction: column;
                    justify-content: center;
                  "
                >
                  <dd class="id">
                    {{ item.work_user_name }}
                  </dd>
                  <dd class="date" style="">
                    <!-- <span>{{ item.timestamp_create }}</span> -->
                    {{ item.timestamp_create }}
                  </dd>
                </div>
              </div>
              <dt class="title" style="display: flex; flex-direction: row">
                <!-- <div style="flex: 1">
                  <span
                    style="margin-right: 5px"
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
                <div class="look">
                  <look style="margin-right: 5px" />조회 {{ item.view_count }}
                </div> -->
              </dt>
              <div style="display: flex; flex-direction: row">
                <dt
                  style="flex: 1; padding: 0px"
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
                    style="font-size: 13px; font-weight: 100; margin-right: 5px font-color: #DF2E2E; background-color: #FFE9E9; padding:5px; border-radius:11px; color:DF2E2E;"
                    :style="[
                      { color: item.work_status === 2 ? '#dddddd' : '#CD0000' }
                    ]"
                  >
                    인기
                  </div>
                  <!-- <div class="look">
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
                  </div> -->
                  <!-- <div class="look">
                    <look style="margin-right: 5px" />조회 {{ item.view_count }}
                  </div> -->
                </div>
              </div>
              <dd class="address" :class="{ type2: item.work_status === 2 }">
                <img
                  :src="require('@/static/images/common/payment.png')"
                  alt=""
                  style="margin-right: 5px; display: inline-block"
                />{{ item.work_payment }} 원/일
              </dd>
              <dd class="address" :class="{ type2: item.work_status === 2 }">
                <img
                  :src="require('@/static/images/common/person.png')"
                  alt=""
                  style="margin-right: 5px; display: inline-block"
                />보통인부 {{ Math.floor(Math.random() * 4) }}명
              </dd>
              <dd class="address" :class="{ type2: item.work_status === 2 }">
                <img
                  :src="require('@/static/images/common/worklocation.png')"
                  alt=""
                  style="margin-right: 5px; display: inline-block"
                />{{ item.work_location }}
              </dd>
              <dd class="address" :class="{ type2: item.work_status === 2 }">
                {{ item.dist }}
              </dd>
              <dd
                style="padding-bottom: 15px"
                class="address"
                :class="{ type2: item.work_status === 2 }"
              >
                <img
                  :src="require('@/static/images/common/workperiod.png')"
                  alt=""
                  style="margin-right: 5px; display: inline-block"
                />{{ item.work_period_start }} ~ {{ item.work_period_end }}
              </dd>
              <dd
                v-if="item.work_etc"
                class="address"
                style="padding-bottom: 15px"
              >
                {{ item.work_etc }}
              </dd>

              <div class="footer">
                <!-- <dd class="address" :class="{ type2: item.work_status === 2 }">
                  <img
                    :src="require('@/static/images/common/workperiod.png')"
                    alt=""
                    style="margin-right: 5px"
                  />{{ item.work_period_start }} ~ {{ item.work_period_end }}
                </dd>
                <dd
                  class="pay"
                  v-if="item.work_type === 0"
                  :class="{ type2: item.work_status === 2 }"
                ></dd> -->
              </div>
              <!-- <div
                class="address"
                style="padding-bottom: 15px; padding-top: 15px"
              >
                {{ item.work_etc }}
              </div> -->
              <div class="space" />
              <dd class="date">
                <div class="bottom_left">
                  <!-- image preview deactivated -->
                  <!-- <fa
                    v-if="item.check_image === 0"
                    :icon="['fa', 'image']"
                    style="margin-right: 5px"
                  />
                  <fa
                    v-if="item.check_video === 0"
                    :icon="['fa', 'camera']"
                    style="margin-right: 5px"
                  /> -->
                  <like />
                  <!-- <div></div> -->
                  좋아요 {{ (Math.random() * 20).toFixed(0) }}
                  <look style="margin-left: 0.5rem" /> 조회
                  {{ item.view_count }}
                </div>
                <div class="bottom_right">
                  <comment style="margin-left: 0.5rem" />
                  댓글

                  {{
                    item.reply_count === undefined || item.reply_count === null
                      ? 0
                      : item.reply_count
                  }}
                </div>
              </dd>
            </dl>
          </li>
        </ul>
        <ul v-else class="">
          <li class="empty">
            <dl>
              <dd class="icon">
                <subtitlesOff />
              </dd>
              <dd class="content">인력항목이 존재하지 않습니다.</dd>
            </dl>
          </li>
        </ul>
      </div>

      <!-- <section style="padding-right: 5px" class="best_board">
        <div style="display: flex; align-items: center">
          <div style="flex: 12 1 0; font-size: 22px; font-weight: bold">
            오픈채팅
          </div>
          <div style="font-size: 1.5rem; display: flex; align-items: flex-end">
            더보기
          </div>
          <div @click="click('HumanResources')">
            <img :src="require('@/static/images/common/arrow.png')" alt="" />
          </div>
        </div>
        <div class="list" style="">
          <div style="justify-content: space-between; padding-top: 15px">
            <div
              v-for="(item, key) in chatRoomList.slice(0, 3)"
              :key="key"
              class="chat"
            >
              <div
                style="display: flex; align-items: center"
                class="text_radius"
                @click="goPage(item.chatroom_idx, item.chatroom_description)"
              >
                <emptyProfile />
                <div style="flex: 3 1 0; text-align: left; margin-left: 1rem">
                  <router-link
                    :to="{
                      name: 'OpenChat',
                      query: {
                        idx: item.chatroom_idx,
                        name: item.chatroom_description
                      }
                    }"
                    style="color: black"
                  >
                    {{ item.chatroom_description }} <br />
                  </router-link>
                  <div style="color: grey; font-size: 1.4rem">
                    {{ (Math.random() * 20).toFixed(0) }} 명 · 방금전
                  </div>
                </div>
                <div
                  style="
                    flex: 1 1 0;
                    background-color: #e8f5ff;
                    border-radius: 0.5rem;
                    color: #008ff6;
                    padding: 0.5rem 0px;
                    font-size: 1.2rem;
                    margin-right: 0.5rem;
                  "
                >
                  참여하기
                </div>
              </div>
            </div>
          </div>
        </div>
      </section> -->
    </div>

    <!-- 05/27 평가통계 삭제요청으로 인한 주석처리 -->
    <!-- <section class="statistics">
      <h3><span>의미있고 건설적인 통계</span>실시간 평가 통계</h3>

      <nav class="sub_tab">
        <tab :list="tabList" :current="tabCurrent" @input="onChangeTab" />
      </nav>

      <div class="list">
        <table>
          <tbody>
            <tr v-for="(item, key) in companyList" :key="key">
              <td class="logo">
                <img
                  :src="
                    'https://momobuild.s3.ap-northeast-2.amazonaws.com/' +
                    item.company_logo
                  "
                  alt=""
                />
              </td>
              <td class="number">{{ key + 1 }}</td>
              <td class="average">
                {{
                  item.total_point === undefined || item.total_point === null
                    ? 0
                    : Number(item.total_point).toFixed(1)
                }}
              </td>
              <td class="like">
                <div>
                  <like class="icon" />
                  {{
                    item.company_up === undefined || item.company_up === null
                      ? 0
                      : item.company_up
                  }}
                </div>
              </td>
              <td class="unlike">
                <div>
                  <unlike class="icon" />
                  {{
                    item.company_down === undefined ||
                    item.company_down === null
                      ? 0
                      : item.company_down
                  }}
                </div>
              </td>
              <td class="search">
                <router-link
                  :to="{
                    name: 'Construction',
                    query: { companyIdx: item.company_idx }
                  }"
                >
                  <search />
                </router-link>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </section> -->
  </div>
</template>

<script>
  import Swiper, { Navigation, Pagination, Autoplay } from 'swiper'
  Swiper.use([Navigation, Pagination, Autoplay])
  import 'swiper/swiper.scss'
  // import look from '@/static/images/common/view.svg'
  // import comment from '@/static/images/common/comment.svg'
  import like_red from '@/static/images/common/like_red.svg'
  import like from '@/static/images/common/like.svg'
  import comment from '@/static/images/common/comment.svg'
  import subtitlesOff from '@/static/images/common/subtitles_off.svg'
  import profile from '@/static/images/common/profile.svg'
  import look from '@/static/images/common/view.svg'
  import unlike from '@/static/images/common/unlike_small.svg'
  import tab from '@/components/common/tab'
  import api from '@/api/api'
  import moment from 'moment'
  import KakaoMapVue from '../../../components/kakaoMap/KakaoMap.vue'

  export default {
    name: 'Evaluation',
    components: {
      // eslint-disable-next-line vue/no-unused-components
      search: () => import('@/static/images/main/search.svg'),
      // look,
      // comment,
      // eslint-disable-next-line vue/no-unused-components
      like_red,
      like,
      comment,
      subtitlesOff,
      profile,
      look,
      // eslint-disable-next-line vue/no-unused-components
      unlike,

      // eslint-disable-next-line vue/no-unused-components
      tab
      // person,
      // emptyProfile
      // profile
      // chattingBox
      // chat,
    },

    data() {
      return {
        select: '2',
        border_style:
          'width: 84vw; height: 0.1rem; background-color: #eef1f5; margin-bottom: 1rem',
        visualSlider: null,
        bannerList: [],
        people: 0,
        tabCurrent: 1,
        category: 'total',
        tabList: [
          { id: 1, label: '종합', category: 'total' },
          { id: 2, label: '안전', category: 'safe' },
          { id: 3, label: '복지', category: 'welfare' },
          { id: 4, label: '강도', category: 'hard' },
          { id: 5, label: '급여', category: 'payment' },
          { id: 6, label: '식사', category: 'meal' },
          { id: 7, label: '휴식', category: 'rest' }
        ],
        companyList: [],
        issueList: [],
        chatRoomList: [],
        pageNo: 1,
        scrollPosition: 0,
        isLoading: true,
        jobList: []
      }
    },
    beforeDestroy() {
      if (this.visualSlider) {
        this.visualSlider.destroy()
      }
    },
    mounted() {
      this.getBannerList()
      this.getChatRoomList()
      this.getIssueList()
      this.getList('timeStamp')

      // this.getCompanyList()
    },
    created() {
      if (this.$route.params.type !== undefined) {
        this.select = this.$route.params.type
      }
      //   this.showMyFindLocation(this.myUserInfo)
      window.addEventListener('scroll', this.handleScroll)
    },
    destroyed() {
      window.removeEventListener('scroll', this.handleScroll)
    },
    methods: {
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
            // title: '신청시 채팅 연결합니다',
            message: '신청시 채팅 연결합니다.',

            data: {
              KakaoMapVue: KakaoMapVue,
              currentLocation: currentLocation,
              allItem: allItem,
              confirmText: '신청하기',
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
                        // 신청시 비활성화 기능 삭제
                        // this.list.map(val => {
                        //   if (val.work_idx === workIdx) {
                        //     // val.work_status = 2
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
            console.log('763')
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
      goIssuePage(v) {
        this.$router.push({ name: 'IssueDetail', query: { issueIdx: v } })
      },
      //   handleSelect(v) {
      //     this.select = v
      //   },
      goPage(idx, description) {
        this.$router.push({
          name: 'OpenChat',
          query: {
            idx: idx,
            name: description
          }
        })
      },
      click(name) {
        this.$router.push({ name: name })
      },
      userCheck(v) {
        if (this.$store.getters.token === '') {
          this.$dialog.show({
            type: 'alert',
            title: '로그인 후 이용해주세요'
          })
        }
        this.$router.push({
          name: 'OpenChat',
          query: { idx: v.chatroom_idx, name: v.chatroom_description }
        })
      },
      async getList(v) {
        // console.log(v, this.val1, this.val2)
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
              // `job/open/${this.val1}/${this.val2}/${this.select}/${this.pageNo}`,
              `job/open/all/all/0/${this.pageNo}`,
              config
            )
            .then(result => {
              console.log(result, 'result')
              if (result.data.success) {
                if (
                  result.data.data.result !== undefined &&
                  result.data.data.result !== null
                ) {
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
                      ).format('YYYY.MM.DD')}(${moment(value.work_period_start)
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
                    value.age = false
                    cntList.push({
                      idx: value.work_idx,
                      cnt: value.view_count
                    })

                    // sort by timeStamp
                    if (v === 'timeStamp') {
                      list = list.sort((a, b) => {
                        // console.log(a.dist)
                        // console.log(b.dist)
                        if (
                          new Date(a.timestamp_create) >
                          new Date(b.timestamp_create)
                        )
                          return -1
                        if (
                          new Date(a.timestamp_create) ==
                          new Date(b.timestamp_create)
                        )
                          return 0
                        if (
                          new Date(a.timestamp_create) <
                          new Date(b.timestamp_create)
                        )
                          return 1
                      })
                    }

                    let timestamp = moment(value.timestamp_create).format()
                    let now = moment()
                    let diff = now.diff(timestamp, 'minutes')
                    if (diff < 60 * 24 * 7 * 4 * 12) {
                      if (diff < 3) {
                        value.timestamp_create = '얼마전'
                      } else if (diff < 60) {
                        value.timestamp_create = diff + '분전'
                      } else if (diff < 60 * 24) {
                        value.timestamp_create = parseInt(diff / 60) + '시간전'
                      } else if (diff < 60 * 24 * 7) {
                        value.timestamp_create = parseInt(diff / 1440) + '일전'
                      } else if (diff < 60 * 24 * 7 * 4) {
                        value.timestamp_create =
                          parseInt(diff / 10080) + '주일전'
                      } else if (diff < 60 * 24 * 7 * 4 * 12) {
                        value.timestamp_create = parseInt(diff / 40320) + '달전'
                      }
                    } else {
                      value.timestamp_create = moment(
                        value.timestamp_create
                      ).format('YYYY.MM.DD')
                    }
                    // 거리계산 로직 시작 ... JY
                    // this.geocoder.addressSearch(
                    //   value.work_location,
                    //   (result, status) => {
                    //     if (status === kakao.maps.services.Status.OK) {
                    //       // let coords = new kakao.maps.LatLng(
                    //       //   result[0].y,
                    //       //   result[0].x
                    //       // )
                    //       let newLat = result[0].y
                    //       let newLng = result[0].x
                    //       let radLat1 = (Math.PI * this.latitude) / 180
                    //       let radLat2 = (Math.PI * newLat) / 180
                    //       let theta = this.longitude - newLng
                    //       console.log(this.longitude, 'currentLng')
                    //       let radTheta = (Math.PI * theta) / 180
                    //       let dist =
                    //         Math.sin(radLat1) * Math.sin(radLat2) +
                    //         Math.cos(radLat1) *
                    //           Math.cos(radLat2) *
                    //           Math.cos(radTheta)
                    //       if (dist > 1) dist = 1

                    //       dist = Math.acos(dist)
                    //       dist = (dist * 180) / Math.PI
                    //       dist = dist * 60 * 1.1515 * 1.609344 * 1000
                    //       if (dist < 100) dist = Math.round(dist / 10) * 10
                    //       else dist = Math.round(dist / 100) * 100
                    //       console.log(dist / 1000, '반환값')

                    //       value.dist = dist
                    //       // var marker = new kakao.maps.Marker({
                    //       //   position: coords
                    //       // fn(result[0].y, result[0].x, coords)
                    //       // this.$emit('distance', result2 / 1000 + 'km')
                    //     }
                    //   }
                    // )
                    // 거리계산 로직 종료 ... JY
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
                  // 소팅 로직 시작 ... JY

                  // list = list.sort((a, b) => {
                  //   console.log(a.dist)
                  //   console.log(b.dist)
                  //   if (a.dist > b.dist) return 1
                  //   if (a.dist === b.dist) return 0
                  //   if (a.dist < b.dist) return -1
                  // })
                  // 소팅 로직 시작 ... JY
                  console.log(list)
                  // sort by payment
                  if (v === 'payment') {
                    list = list.sort((a, b) => {
                      let temp_a = a.work_payment.replace(/,/g, '')
                      temp_a = parseInt(temp_a, 10)
                      let temp_b = b.work_payment.replace(/,/g, '')
                      temp_b = parseInt(temp_b, 10)
                      // console.log(a.dist)
                      // console.log(b.dist)
                      if (temp_a < temp_b) return 1
                      if (temp_a == temp_b) return 0
                      if (temp_a > temp_b) return -1
                    })
                  }

                  // sort by startDate
                  if (v === 'startDate') {
                    list = list.sort((a, b) => {
                      // console.log(a.dist)
                      // console.log(b.dist)
                      if (
                        new Date(a.work_period_start) >
                        new Date(b.work_period_start)
                      )
                        return 1
                      if (
                        new Date(a.work_period_start) ==
                        new Date(b.work_period_start)
                      )
                        return 0
                      if (
                        new Date(a.work_period_start) <
                        new Date(b.work_period_start)
                      )
                        return -1
                    })
                  }

                  // sort by recent
                  if (v === 'dateUploaded') {
                    list = list.sort((a, b) => {
                      // console.log(a.dist)
                      // console.log(b.dist)
                      if (
                        new Date(a.timestamp_create) <
                        new Date(b.timestamp_create)
                      )
                        return 1
                      if (
                        new Date(a.timestamp_create) ==
                        new Date(b.timestamp_create)
                      )
                        return 0
                      if (
                        new Date(a.timestamp_create) >
                        new Date(b.timestamp_create)
                      )
                        return -1
                    })
                  }
                  this.jobList = this.jobList.concat(list)
                  this.pageNo += 1
                  this.isLoading = false
                } else {
                  if (result.data.code === 2121) {
                    this.jobList = []
                  }
                }
              } else {
                // this.$dialog.show({
                //   type: 'alert',
                //   title: result.data.message
                // })
              }
            })
            .catch(err => console.log(err))
        } catch (err) {
          console.log(err)
        }
      },
      async getBannerList() {
        try {
          const config = {
            headers: {
              'Content-Type': 'application/json; charset = utf-8',
              'Access-Control-Allow-Origin': '*',
              authorization: `Bearer ${this.$store.getters.token}`
            }
          }
          await api
            .get(`banner/open/list`, config)
            .then(result => {
              console.log(result)
              if (result.data.success) {
                if (
                  result.data.data !== undefined &&
                  result.data.data !== null
                ) {
                  let rawList = result.data.data.result
                  let refinedList = rawList.map(val => {
                    const obj = {
                      id: val.banner_idx,
                      name: val.banner_name,
                      src:
                        'https://momobuild.s3.ap-northeast-2.amazonaws.com/' +
                        val.banner_image,
                      link: val.banner_to
                    }
                    return obj
                  })
                  console.log(refinedList)
                  this.bannerList = refinedList
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

        if (this.bannerList.length > 0) {
          this.$nextTick(this.setVisualSwiper)
        }
      },
      async getChatRoomList() {
        try {
          const config = {
            headers: {
              'Content-Type': 'application/json; charset = utf-8',
              'Access-Control-Allow-Origin': '*',
              authorization: `Bearer ${this.$store.getters.token}`
            }
          }
          await api
            .get(`chat/open/rooms`, config)
            .then(result => {
              console.log(result)
              if (result.data.success) {
                if (
                  result.data.data !== undefined &&
                  result.data.data !== null
                ) {
                  this.chatRoomList = result.data.data.result
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

        if (this.bannerList.length > 0) {
          this.$nextTick(this.setVisualSwiper)
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
      onChangeTab(id, category) {
        this.tabCurrent = id
        // eslint-disable-next-line no-undef
        this.category = category
        this.getCompanyList()
      },

      async getIssueList() {
        try {
          const config = {
            headers: {
              'Content-Type': 'application/json; charset = utf-8',
              'Access-Control-Allow-Origin': '*',
              authorization: `Bearer ${this.$store.getters.token}`
            }
          }
          await api
            .get(`board/open/board/new/1`, config)
            .then(result => {
              console.log(result)
              if (result.data.success) {
                if (
                  result.data.data !== undefined &&
                  result.data.data !== null
                ) {
                  let list = result.data.data.result.sort(
                    // (a, b) => b.timestamp_create - a.timestamp_create
                    (a, b) => b.view_count - a.view_count
                  )

                  if (list.length > 10) {
                    list = list.filter((val, idx) => {
                      return idx < 10
                    })
                  }
                  console.log('list: ', list)

                  // list = result.data.data.result.sort((a, b) => a.timestamp_create - b.timestamp_create);
                  list = list.map(value => {
                    if (
                      value.view_count !== undefined &&
                      value.view_count !== null
                    ) {
                      value.view_count = this.$options.filters.autoCommaNumber(
                        value.view_count
                      )
                    }
                    if (
                      value.reply_count !== undefined &&
                      value.reply_count !== null
                    ) {
                      value.reply_count = this.$options.filters.autoCommaNumber(
                        value.reply_count
                      )
                    }
                    if (String(value.issue_title).length > 15) {
                      value.issue_title =
                        String(value.issue_title).substring(0, 15) + '...'
                    }
                    //newfunction
                    let timestamp = moment(value.timestamp_create).format()
                    let now = moment()
                    let diff = now.diff(timestamp, 'minutes')
                    if (diff < 60 * 24 * 7 * 4 * 12) {
                      if (diff < 3) {
                        value.timestamp_create = '얼마전'
                      } else if (diff < 60) {
                        value.timestamp_create = diff + '분전'
                      } else if (diff < 60 * 24) {
                        value.timestamp_create = parseInt(diff / 60) + '시간전'
                      } else if (diff < 60 * 24 * 7) {
                        value.timestamp_create = parseInt(diff / 1440) + '일전'
                      } else if (diff < 60 * 24 * 7 * 4) {
                        value.timestamp_create =
                          parseInt(diff / 10080) + '주일전'
                      } else if (diff < 60 * 24 * 7 * 4 * 12) {
                        value.timestamp_create = parseInt(diff / 40320) + '달전'
                      }
                    } else {
                      value.timestamp_create = moment(
                        value.timestamp_create
                      ).format('YYYY.MM.DD')
                    }
                    return value
                  })
                  this.issueList = list
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
      async getCompanyList() {
        try {
          const config = {
            headers: {
              'Content-Type': 'application/json; charset = utf-8',
              'Access-Control-Allow-Origin': '*',
              authorization: `Bearer ${this.$store.getters.token}`
            }
          }
          await api
            .get(`company/open/statistics/${this.category}`, config)
            .then(result => {
              console.log(result)
              if (result.data.success) {
                if (
                  result.data.data.siteInfo !== undefined &&
                  result.data.data.siteInfo !== null
                ) {
                  this.companyList = result.data.data.siteInfo
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
      handleScroll() {
        // console.log(this.$refs.container.scrollHeight)
        // console.log(window.scrollY + window.innerHeight)
        if (
          window.scrollY + window.innerHeight >
          this.$refs.container.scrollHeight
        ) {
          if (this.jobList.length === (this.pageNo - 1) * 10) {
            if (this.isLoading === false) {
              this.isLoading = true
              this.getList()
            }
          }
        }
      },
      checkRegist(v) {
        if (this.$store.getters.token === '') {
          this.$dialog.show({
            type: 'alert',
            title: '로그인 후 이용해주세요.'
          })
        } else {
          this.$router.push({ name: v })
        }
      }
    }
  }
</script>

<style scoped></style>
