<template>
  <div class="main" ref="container">
    <h2 hidden>평가</h2>

    <div class="button_area">
      <a :class="[{ unactive: select !== '0' }]" @click="click('Main')">
        <button type="button" class="btn-login">실시간 HOT</button>
      </a>
      <div class="space" />
      <a :class="[{ unactive: select !== '1' }]">
        <button type="button" class="btn-login">자유게시판</button>
      </a>
      <a :class="[{ unactive: select !== '2' }]" @click="click('WorkBoard')">
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
              <div v-for="(file, key) in item.files" :key="key">
                <!-- <h1>111</h1> -->
                <!-- <div v-if="file">
                  <h1>asdasdasd</h1>
                </div>
                <div v-else><h1>asdasssssss</h1></div> -->
                <div v-if="file.file_type === 0">
                  <img
                    :src="
                      'https://momobuild.s3.ap-northeast-2.amazonaws.com/' +
                      file.file_path
                    "
                    alt=""
                  />
                </div>
                <div v-else-if="file.file_type === 1">
                  <video-player
                    class="video-js"
                    ref="videoPlayer"
                    :options="{
                      // videojs options
                      controlBar: {
                        timeDivider: true, //
                        durationDisplay: true, //
                        remainingTimeDisplay: true, //
                        fullscreenToggle: true //
                      },
                      muted: true,
                      language: 'en',
                      playbackRates: [0.7, 1.0, 1.5, 2.0],
                      sources: [
                        {
                          type: 'video/mp4',
                          src:
                            'https://momobuild.s3.ap-northeast-2.amazonaws.com/' +
                            file.file_path
                        }
                      ]
                      // poster: '/static/images/author.jpg'
                    }"
                    :playsinline="true"
                    customEventName="customstatechangedeventname"
                    @play="onPlayerPlay($event)"
                    @pause="onPlayerPause($event)"
                    @ended="onPlayerEnded($event)"
                    @waiting="onPlayerWaiting($event)"
                    @statechanged="playerStateChanged($event)"
                    @ready="playerReadied"
                  >
                  </video-player>
                  <h1>sss</h1>
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
        select: '1',
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
        jobList: [],
        playerOptions: {
          // videojs options
          muted: true,
          language: 'en',
          playbackRates: [0.7, 1.0, 1.5, 2.0],
          sources: [
            {
              type: 'video/mp4',
              src: 'https://cdn.theguardian.tv/webM/2015/07/20/150716YesMen_synd_768k_vp8.webm'
            }
          ]
          // poster: '/static/images/author.jpg'
        },
        additional: '',
        additionalDivision: '',
        additionalPlayerOptions: {
          // videojs options
          controlBar: {
            timeDivider: true, //
            durationDisplay: true, //
            remainingTimeDisplay: true, //
            fullscreenToggle: true //
          },
          muted: true,
          language: 'en',
          playbackRates: [0.7, 1.0, 1.5, 2.0],
          sources: [
            {
              type: 'video/mp4',
              src: 'https://cdn.theguardian.tv/webM/2015/07/20/150716YesMen_synd_768k_vp8.webm'
            }
          ],
          profileImg: undefined // 프로필 이미지
          // poster: '/static/images/author.jpg'
        },
        modify: 'false',
        image: '',
        video: '',
        files: []
      }
    },
    beforeDestroy() {
      if (this.visualSlider) {
        this.visualSlider.destroy()
      }
    },
    mounted() {
      this.getBannerList()
      this.getIssueList()
      this.$forceUpdate()
      //   this.getList()
      // this.getCompanyList()
    },
    created() {
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
      async getIssueDetail(idx) {
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
              //   `board/open/board/detail/${this.$route.query.issueIdx}`,
              `board/open/board/detail/${idx}`,
              config
            )
            .then(result => {
              if (result.data.success) {
                if (
                  result.data.data.detail !== undefined &&
                  result.data.data.detail !== null
                ) {
                  const detail = result.data.data.detail
                  console.log('issue detail: ', detail)
                  // 이미지 / 비디오 구분용
                  let flag = false
                  const imageExtension = [
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
                  if (
                    String(detail.issue_image) !== undefined &&
                    String(detail.issue_image) !== null &&
                    String(detail.issue_image) !== ''
                  ) {
                    const loc = String(detail.issue_image).toLowerCase()
                    imageExtension.map(inner => {
                      if (String(loc).includes(inner)) {
                        flag = true
                      }
                    })
                  }
                  if (
                    detail.issue_additional !== undefined &&
                    detail.issue_additional !== null &&
                    String(detail.issue_additional) !== ''
                  ) {
                    const loc = String(detail.issue_additional).toLowerCase()
                    imageExtension.map(inner => {
                      if (String(loc).includes(inner)) {
                        this.additional = detail.issue_additional
                        this.additionalDivision = 'image'
                      }
                    })
                    if (this.additionalDivision === '') {
                      this.additional = 'true'
                      this.additionalDivision = 'video'
                      this.additionalPlayerOptions.sources[0].src =
                        detail.issue_additional
                    }
                  }
                  if (flag) {
                    this.image = detail.issue_image
                  } else {
                    this.video = detail.issue_image
                    this.playerOptions.sources[0].src =
                      'https://momobuild.s3.ap-northeast-2.amazonaws.com/' +
                      detail.issue_image
                  }
                  this.modify = detail.modify
                  console.log(result.data.data.files)
                  this.files = result.data.data.files
                  console.log(this.files)
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
            .get(`board/open/board/new/${this.pageNo}`, config)
            .then(result => {
              console.log(result)
              if (result.data.success) {
                if (
                  result.data.data !== undefined &&
                  result.data.data !== null
                ) {
                  let list = result.data.data.result
                  // sort by viewCount
                  //   let list = result.data.data.result.sort(
                  //     (a, b) => b.view_count - a.view_count
                  //   )

                  //   if (list.length > 10) {
                  //     list = list.filter((val, idx) => {
                  //       return idx < 10
                  //     })
                  //   }
                  //   console.log('list: ', list)

                  // list = result.data.data.result.sort((a, b) => a.timestamp_create - b.timestamp_create);
                  list = list.map(value => {
                    if (value.check_image === 0) {
                      this.getIssueDetail(value.issue_idx).then(
                        () => (value.files = this.files)
                      )
                    }

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
                    // this.image = value.files.file_path

                    return value
                  })
                  this.issueList = this.issueList.concat(list)
                  this.pageNo = this.pageNo + 1
                  this.isLoading = false
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
        if (
          window.scrollY + window.innerHeight >
          this.$refs.container.scrollHeight
        ) {
          if (this.issueList.length === (this.pageNo - 1) * 10) {
            console.log(this.isLoading)
            if (this.isLoading === false) {
              this.isLoading = true
              this.getIssueList()
            }
          }
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
