<template>
  <div id="construction">
    <h1 hidden>건설사</h1>
    <div style="padding: 30px 24px">
      <back />
    </div>

    <section class="top company">
      <img
        :src="companyInfo.company_logo"
        alt=""
        style="max-height: 90px; max-width: 175px"
      />
      <h2 class="name">{{ companyInfo.company_name }}</h2>
      <p class="desc">
        <span>{{ siteList.length }} 현장</span>
        <span>건설모모 종합 순위 {{ companyInfo.rownum }}위</span>
      </p>
    </section>

    <section class="card_slide">
      <div class="info">
        <h3 class="title">현장</h3>
        <div class="btn_wrap">
          <button type="button">
            <router-link
              :to="{
                name: 'ConstructionSites',
                query: {
                  companyIdx: companyIdx,
                  companyName: companyInfo.company_name
                }
              }"
            >
              현장 찾아보기 >
            </router-link>
          </button>
        </div>
      </div>

      <div ref="cardSwiper" class="swiper_wrap">
        <div class="card-list">
          <div
            v-for="(item, key) in siteList"
            :key="key"
            class="card"
            @click="goInfo(item.site_idx)"
          >
            <dl>
              <dt class="logo">
                <img :src="companyInfo.company_logo" alt="" />
              </dt>
              <dd class="name">{{ item.site_name }}</dd>
              <dd class="address">{{ item.site_address }}</dd>
            </dl>
          </div>
          <div v-if="siteList.length <= 0" class="empty">
            등록된 현장이 없습니다.
          </div>
        </div>
        <div ref="cardPagination" class="swiper-pagination" />
      </div>
    </section>

    <section class="page work_history detail_eval">
      <div class="info">
        <h3 class="title">
          세부가 평가
          <span>
            <star class="star active" />
            {{ companyInfo.company_total }}
            (
            {{ ratingList.length }}
            )
          </span>
        </h3>
        <div class="opinion">
          <span class="like">
            <h4 hidden>좋아요</h4>
            <like />{{ companyInfo.company_up }}
          </span>
          <span class="hate">
            <h4 hidden>싫어요</h4>
            <hate />{{ companyInfo.company_down }}
          </span>
        </div>
      </div>

      <div class="graph_wrap">
        <div class="chart" v-for="(item, key) in evaluate" :key="key">
          <div class="bar">
            <div
              class="value"
              :style="{ height: `calc(84px / 5 * ${item.rating})` }"
            ></div>
          </div>
          <div class="title">
            <p>{{ item.title }}</p>
            <p>{{ item.rating }}</p>
          </div>
        </div>
      </div>

      <div v-if="ratingList.length">
        <evaluation v-bind:list="ratingList" />
      </div>
      <div v-else class="empty">등록된 현장이 없습니다.</div>
    </section>
  </div>
</template>

<script>
  import Swiper, { Navigation, Pagination, Autoplay } from 'swiper'
  Swiper.use([Navigation, Pagination, Autoplay])
  // import 'swiper/swiper.scss';
  import api from '@/api/api'
  import moment from 'moment'
  import back from '@/components/layout/back'

  export default {
    name: 'Construction',
    components: {
      evaluation: () => import('@/components/common/evaluation'),
      star: () => import('@/static/images/common/star.svg'),
      like: () => import('@/static/images/common/like_red.svg'),
      hate: () => import('@/static/images/common/unlike_small.svg'),
      back
    },
    data() {
      return {
        cardSlider: null,
        evaluate: [
          { id: 1, title: '안전', rating: 0 },
          { id: 2, title: '복지', rating: 0 },
          { id: 3, title: '강도', rating: 0 },
          { id: 4, title: '급여', rating: 0 },
          { id: 5, title: '식사', rating: 0 },
          { id: 6, title: '휴식', rating: 0 }
        ],
        companyIdx: this.$route.query.companyIdx,
        companyInfo: {},
        siteList: [],
        ratingList: []
      }
    },
    beforeDestroy() {
      if (this.cardSlider) {
        this.cardSlider.destroy()
      }
    },
    mounted() {
      this.companyIdx = this.$route.query.companyIdx
      this.getCompanyInfo()
    },
    methods: {
      async getList() {
        // const { data } = await mainCardList()

        // this.siteList = data

        if (this.siteList.length > 0) {
          this.$nextTick(this.setCardSwiper)
        }
      },
      setCardSwiper() {
        const options = {
          // autoplay: {
          //   delay: 3000
          // },
          loop: true,
          spaceBetween: 0,
          // centeredSlides: true,
          centerInsufficientSlides: true,
          slidesPerGroup: 1,
          slidesPerView: 'auto',
          slideClass: 'card',
          wrapperClass: 'card-list',
          // pagination: {
          //   el: this.$refs.cardPagination,
          //   type: 'bullets',
          //   clickable: true,
          //   renderBullet: function (index, className) {
          //     return `<button type="button" class="${className}">${index + 1}</button>`
          //   },
          //   bulletActiveClass: 'active',
          //   bulletClass: 'swiper-pagination-bullet',
          //   bulletElement: 'button',
          //   modifierClass: ''
          // },
          navigation: false,
          width: 240
        }

        this.cardSlider = new Swiper(this.$refs.cardSwiper, options)
      },
      goInfo(idx) {
        this.$router.push({ name: 'ConstructionInfo', query: { siteIdx: idx } })
      },
      async getCompanyInfo() {
        try {
          const config = {
            headers: {
              'Content-Type': 'application/json; charset = utf-8',
              'Access-Control-Allow-Origin': '*',
              authorization: `Bearer ${this.$store.getters.token}`
            }
          }
          await api
            .get(`company/open/selectCompanyInfo/${this.companyIdx}`, config)
            .then(result => {
              console.log(result)
              if (result.data.success) {
                if (
                  result.data.data !== undefined &&
                  result.data.data !== null
                ) {
                  let companyInfo = result.data.data.companyInfo
                  if (
                    companyInfo.company_logo !== undefined &&
                    companyInfo.company_logo !== null
                  ) {
                    companyInfo.company_logo =
                      'https://momobuild.s3.ap-northeast-2.amazonaws.com/' +
                      companyInfo.company_logo
                  }
                  companyInfo.company_up =
                    companyInfo.company_up === undefined ||
                    companyInfo.company_up === null
                      ? 0
                      : companyInfo.company_up
                  companyInfo.company_down =
                    companyInfo.company_down === undefined ||
                    companyInfo.company_down === null
                      ? 0
                      : companyInfo.company_down
                  this.companyInfo = companyInfo
                  this.siteList = result.data.data.siteList
                  this.ratingList = result.data.data.ratingList.map(value => {
                    value.timestamp_create = moment(value.timestamp_create)
                      .utc()
                      .format('YYYY.MM.DD')
                    return value
                  })
                  // 안전 복지 강도 급여 식사 휴식
                  this.evaluate[0].rating =
                    result.data.data.evaluate.company_safe
                  this.evaluate[1].rating =
                    result.data.data.evaluate.company_welfare
                  this.evaluate[2].rating =
                    result.data.data.evaluate.company_hard
                  this.evaluate[3].rating =
                    result.data.data.evaluate.company_payment
                  this.evaluate[4].rating =
                    result.data.data.evaluate.company_meal
                  this.evaluate[5].rating =
                    result.data.data.evaluate.company_rest
                }
              } else {
                this.$dialog.show({
                  type: 'alert',
                  title: result.data.message
                })
              }
            })
            .catch(err => console.log(err))
            .finally(() => {
              if (this.ratingList.length > 0) this.getList()
            })
        } catch (err) {
          console.log(err)
        }
      },
      async getSiteList() {
        try {
          const config = {
            headers: {
              'Content-Type': 'application/json; charset = utf-8',
              'Access-Control-Allow-Origin': '*',
              authorization: `Bearer ${this.$store.getters.token}`
            }
          }
          await api
            .get(`company/open/getSiteList/${this.companyIdx}`, config)
            .then(result => {
              console.log(result)
              if (result.data.data !== undefined && result.data.data !== null) {
                this.list = result.data.data
              }
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
