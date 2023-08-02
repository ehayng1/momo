<template>
  <div id="construction">
    <h1 hidden>현장보기</h1>
    <section class="top info">
      <back />
      <img :src="siteInfo.company_logo" alt="로고" />
      <h2 class="name">{{ siteInfo.site_name }}</h2>
      <p class="desc">{{ siteInfo.site_address }}</p>
      <p class="desc">
        {{ siteInfo.site_period_start }} ~ {{ siteInfo.site_period_end }}
      </p>
    </section>

    <!-- <section
      class="visual"
      :style="{
        backgroundImage:
          'url(https://newsroom-prd-data.s3.ap-northeast-2.amazonaws.com/wp-content/uploads/2017/08/%EC%82%AC%EB%AC%BC%EC%9D%B8%ED%84%B0%EB%84%B7_%EC%8A%A4%EB%A7%88%ED%8A%B8%ED%83%9C%EA%B7%B8_1.png)'
      }"
    ></section> -->

    <section class="page work_history detail_eval">
      <div class="info">
        <h3 class="title">
          현장 평가<span
            ><star class="star active" />{{ siteInfo.site_total }}({{
              siteInfo.count
            }})</span
          >
        </h3>
        <div class="opinion">
          <span id="like">
            <h4 hidden>좋아요</h4>
            <like class="like" />{{ siteInfo.site_up }}
          </span>
          <span id="hate">
            <h4 hidden>싫어요</h4>
            <hate class="rot" />{{ siteInfo.site_down }}
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

      <div class="btn_wrap">
        <!--유저가 해당현장에 근무한 이력이있으면 현장보기에서 '요청하기' 버튼이 '평가하기' 버튼으로 변경해주시면 됩니다-->
        <button type="button" class="big_btn">
          <a @click="checkRegist">구직 요청하기</a>
        </button>
        <button type="button" class="big_btn">
          <a @click="checkRegist2"> 평가 작성하기 </a>
        </button>
      </div>

      <evaluation v-bind:list="ratingList" />
    </section>
  </div>
</template>

<script>
  import api from '@/api/api'
  import moment from 'moment'
  import back from '@/components/layout/back'

  export default {
    name: 'Info',
    components: {
      evaluation: () => import('@/components/common/evaluation'),
      star: () => import('@/static/images/common/star.svg'),
      like: () => import('@/static/images/common/like_red.svg'),
      hate: () => import('@/static/images/common/unlike_small.svg'),
      back
    },
    data() {
      return {
        ratingList: [],
        evaluate: [
          { id: 1, title: '안전', rating: 0 },
          { id: 2, title: '복지', rating: 0 },
          { id: 3, title: '강도', rating: 0 },
          { id: 4, title: '급여', rating: 0 },
          { id: 5, title: '식사', rating: 0 },
          { id: 6, title: '휴식', rating: 0 }
        ],
        siteIdx: this.$route.query.siteIdx,
        siteInfo: {}
      }
    },
    mounted() {
      this.getDetail()
    },
    methods: {
      checkRegist() {
        if (this.$store.getters.token === '') {
          this.$dialog.show({
            type: 'alert',
            title: '로그인 후 이용해주세요'
          })
          return false
        } else {
          this.$router.push({
            name: 'JobRegistration',
            params: { siteIdx: this.siteIdx }
          })
        }
      },
      checkRegist2() {
        if (this.$store.getters.token === '') {
          this.$dialog.show({
            type: 'alert',
            title: '로그인 후 이용해주세요'
          })
          return false
        } else {
          this.$router.push({
            name: 'EvaluationStart',
            query: {
              siteIdx: this.siteIdx,
              siteName: this.siteInfo.site_name,
              siteAddress: this.siteInfo.site_address,
              companyLogo: this.siteInfo.company_logo,
              sitePicture: this.siteInfo.site_picture
            }
          })
        }
      },
      async getDetail() {
        // const { data } = await mainCardList()
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
            .get(`company/open/selectSiteDetail/${this.siteIdx}`, config)
            .then(result => {
              console.log(result)
              if (result.data.success) {
                if (
                  result.data.data !== undefined &&
                  result.data.data !== null
                ) {
                  let info = result.data.data.siteInfo
                  info.site_period_start = moment(
                    info.site_period_start
                  ).format('YYYY.MM')
                  info.site_period_end = moment(info.site_period_end).format(
                    'YYYY.MM'
                  )
                  info.site_up =
                    info.site_up === undefined || info.site_up === null
                      ? 0
                      : info.site_up
                  info.site_down =
                    info.site_down === undefined || info.site_down === null
                      ? 0
                      : info.site_down
                  if (
                    info.company_logo !== undefined &&
                    info.company_logo !== null
                  ) {
                    info.company_logo =
                      'https://momobuild.s3.ap-northeast-2.amazonaws.com/' +
                      info.company_logo
                  }
                  if (
                    info.site_picture !== undefined &&
                    info.site_picture !== null
                  ) {
                    info.site_picture =
                      'https://momobuild.s3.ap-northeast-2.amazonaws.com/' +
                      info.site_picture
                  }
                  this.siteInfo = info
                  this.ratingList = result.data.data.ratingList.map(value => {
                    value.timestamp_create = moment(value.timestamp_create)
                      .utc()
                      .format('YYYY.MM.DD')
                    return value
                  })
                  this.evaluate[0].rating =
                    result.data.data.siteInfo.site_safe === undefined ||
                    result.data.data.siteInfo.site_safe === null
                      ? 0
                      : result.data.data.siteInfo.site_safe
                  this.evaluate[1].rating =
                    result.data.data.siteInfo.site_welfare === undefined ||
                    result.data.data.siteInfo.site_welfare === null
                      ? 0
                      : result.data.data.siteInfo.site_welfare
                  this.evaluate[2].rating =
                    result.data.data.siteInfo.site_hard === undefined ||
                    result.data.data.siteInfo.site_hard === null
                      ? 0
                      : result.data.data.siteInfo.site_hard
                  this.evaluate[3].rating =
                    result.data.data.siteInfo.site_payment === undefined ||
                    result.data.data.siteInfo.site_payment === null
                      ? 0
                      : result.data.data.siteInfo.site_payment
                  this.evaluate[4].rating =
                    result.data.data.siteInfo.site_meal === undefined ||
                    result.data.data.siteInfo.site_meal === null
                      ? 0
                      : result.data.data.siteInfo.site_meal
                  this.evaluate[5].rating =
                    result.data.data.siteInfo.site_rest === undefined ||
                    result.data.data.siteInfo.site_rest === null
                      ? 0
                      : result.data.data.siteInfo.site_rest
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
      }
    }
  }
</script>

<style scoped></style>
