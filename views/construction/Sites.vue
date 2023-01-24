<template>
  <section class="page applicaion" id="construction">
    <div class="info">
      <back />
      <h1 class="title">{{ this.$route.query.companyName }}</h1>
      <p>전체 현장</p>
    </div>

    <div class="list">
      <ul v-if="siteList.length">
        <li
          class="card"
          v-for="(item, key) in siteList"
          :key="key"
          @click="goInfo(item.site_idx)"
        >
          <div class="image">
            <img
              :src="
                item.site_picture === undefined || item.site_picture === null
                  ? require('@/static/images/main/site_sample.png')
                  : 'https://momobuild.s3.ap-northeast-2.amazonaws.com/' +
                    item.site_picture
              "
              alt=""
              style="height: 100%"
            />
          </div>
          <dl>
            <dt class="title">
              <img :src="item.company_logo" alt="" />
              <p>{{ item.site_name }}</p>
            </dt>
            <dd class="address">{{ item.site_address }}</dd>
            <dd class="address">
              {{ item.site_period_start }} ~ {{ item.site_period_end }}
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
            <dd class="content">등록된 현장이 없습니다.</dd>
          </dl>
        </li>
      </ul>
    </div>
  </section>
</template>

<script>
  import subtitlesOff from '@/static/images/common/subtitles_off.svg'
  import api from '@/api/api'
  import moment from 'moment'
  import back from '@/components/layout/back'

  export default {
    name: 'Sites',
    components: {
      subtitlesOff,
      back
    },
    data() {
      return {
        companyIdx: '',
        siteList: []
      }
    },
    mounted() {
      this.companyIdx = this.$route.query.companyIdx
      this.getList()
    },
    methods: {
      goInfo(idx) {
        this.$router.push({ name: 'ConstructionInfo', query: { siteIdx: idx } })
      },
      async getList() {
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
              if (result.data.success) {
                if (
                  result.data.data !== undefined &&
                  result.data.data !== null
                ) {
                  let list = result.data.data.siteList
                  list.map(value => {
                    value.site_period_start = moment(
                      value.site_period_start
                    ).format('YYYY.MM')
                    value.site_period_end = moment(
                      value.site_period_end
                    ).format('YYYY.MM')
                    if (
                      value.company_logo !== undefined &&
                      value.company_logo !== null
                    ) {
                      value.company_logo =
                        'https://momobuild.s3.ap-northeast-2.amazonaws.com/' +
                        value.company_logo
                    }
                    return value
                  })
                  this.siteList = result.data.data.siteList
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
