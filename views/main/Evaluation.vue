<template>
  <div class="main">
    <h2 hidden>평가</h2>

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

    <!-- <section class="main_text_banner_wrap">
      <router-link :to="{ name: 'HumanResources' }" class="text_banner"
        >일자리 찾기
      </router-link>
      <a @click="checkRegist('JobRegistration')" class="text_banner"
        >일자리 찾기</a
      >
    </section> -->
    <div style="background-color: #f6f8fb">
      <section
        style="padding: 10px 5px 10px 5px; margin: 10px 20px 10px 20px"
        class=""
      >
        <div
          style="
            display: grid;
            grid-template-columns: 50% 50%;
            justify-content: space-evenly;
            gap: 10px;
          "
        >
          <div @click="click('HumanResources')" class="button">
            <div style="margin-left: 1.5rem; flex: 1">
              <h3>일자리</h3>
              <div style="color: #6c747c; font-size: 17px">구인,구직</div>
            </div>
            <div style="flex: 1">
              <img
                :src="require('@/static/images/common/character.png')"
                alt=""
              />
            </div>
            <div style="align-items: flex-end; margin-right: 2%">
              <img :src="require('@/static/images/common/arrow.png')" alt="" />
            </div>
          </div>
          <div @click="click('Equipment')" class="button">
            <div style="margin-left: 1.5rem; flex: 1">
              <h3>장비</h3>
              <div style="color: #6c747c; font-size: 17px">임대,임차</div>
            </div>
            <div style="flex: 1">
              <img :src="require('@/static/images/common/tool1.png')" alt="" />
            </div>
            <div style="margin-right: 2%; align-items: flex-end">
              <img :src="require('@/static/images/common/arrow.png')" alt="" />
            </div>
          </div>
        </div>
      </section>

      <section style="padding-right: 5px" class="best_board">
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
            <div v-for="(item, key) in chatRoomList" :key="key" class="chat">
              <div
                style="display: flex; align-items: center"
                class="text_radius"
                @click="goPage(item.chatroom_idx, item.chatroom_description)"
              >
                <div style="flex: 1 1 0">이미지</div>
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
                  <div style="color: grey; font-size: 1.4rem">명 ·</div>
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
          <!-- <div class="bottom_border" style="margin-top: 10px; width: 100vw"></div> -->
        </div>
      </section>

      <section class="best_board" style="margin-top: 2rem; padding-right: 5px">
        <div style="display: flex; align-items: center">
          <div style="flex: 12 1 0; font-size: 22px; font-weight: bold">
            실시간 인기글
          </div>
          <div style="font-size: 1.5rem; display: flex; align-items: flex-end">
            더보기
          </div>
          <div @click="click('Issue')">
            <img :src="require('@/static/images/common/arrow.png')" alt="" />
          </div>
        </div>

        <div class="list" style="margin-top: 3rem">
          <ul>
            <li v-for="(item, key) in issueList" :key="key">
              <div style="display: flex">
                <div style="flex: 1; margin-right: 1rem">이미지</div>
                <div
                  style="
                    flex: 12;
                    text-align: left;
                    font-size: 1.1rem;
                    color: #6c747c;
                  "
                >
                  {{ item.nickname }}
                  <div style="font-size: 0.9rem; font-weight: lighter">
                    {{ item.timestamp_create }}
                  </div>
                </div>
              </div>
              <br />
              <router-link
                :to="{
                  name: 'IssueDetail',
                  query: { issueIdx: item.issue_idx }
                }"
                style="font-size: 1.3rem"
              >
                {{
                  item.issue_content.length > 10
                    ? item.issue_content.substring(0, 10) + '...'
                    : item.issue_content
                }}
              </router-link>
              <div>{{ item.issue_title }}</div>
              <div style="margin-bottom: 1rem"></div>
              <div :style="border_style"></div>
              <!-- {{ item.nickname }}
              {{ item.view_count }}
              {{ item.reply_count }}
              {{ item.timestamp_create }} -->
            </li>
          </ul>
        </div>
      </section>
    </div>

    <div class="main_footer">
      <div class="main_footer_content" style="">
        <div>
          <div style="margin: 23px 0px 30px 0px">(주) 피앤에이치홀딩스</div>
          <p>주소 : 서울특별시 강남구 선릉로92길 28, 4층 4104호</p>
          <p>사업자번호 : 299-86-02219</p>
          <p>업태 : 정보통신업 종목 : 포털 및 기타 인터넷 정보 매개 서비스업</p>
          <p>TEL : 010-2111-3921</p>
          <p>Mail : gunseolmm@gmail.com</p>
        </div>
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
  import like from '@/static/images/common/like_small.svg'
  import unlike from '@/static/images/common/unlike_small.svg'
  import tab from '@/components/common/tab'
  import api from '@/api/api'
  import moment from 'moment'

  export default {
    name: 'Evaluation',
    components: {
      // eslint-disable-next-line vue/no-unused-components
      search: () => import('@/static/images/main/search.svg'),
      // look,
      // comment,
      // eslint-disable-next-line vue/no-unused-components
      like,
      // eslint-disable-next-line vue/no-unused-components
      unlike,
      // eslint-disable-next-line vue/no-unused-components
      tab
    },
    data() {
      return {
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
        chatRoomList: []
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
      // this.getCompanyList()
    },
    methods: {
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
            .get(`board/open/board/best/1`, config)
            .then(result => {
              console.log(result)
              if (result.data.success) {
                if (
                  result.data.data !== undefined &&
                  result.data.data !== null
                ) {
                  let list = result.data.data.result
                  if (list.length > 3) {
                    list = list.filter((val, idx) => {
                      return idx < 5
                    })
                  }
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
                    if (diff < 180) {
                      if (diff < 3) {
                        value.timestamp_create = '얼마전'
                      } else if (diff < 60) {
                        value.timestamp_create = diff + '분전'
                      } else {
                        value.timestamp_create = parseInt(diff / 60) + '시간전'
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
