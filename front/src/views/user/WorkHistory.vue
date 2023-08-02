<template>
  <section class="page work_history">
    <back />
    <div class="info" ref="container"></div>
    <evaluation v-bind:list="ratingList" />

    <div class="work_history">
      <div class="button_area">
        <a :class="[{ unactive: select === '1' }]" @click="handleSelect('0')">
          <button type="button" class="btn-login">근로 이력</button>
        </a>
        <div class="space" />
        <a :class="[{ unactive: select === '0' }]" @click="handleSelect('1')">
          <button type="button" class="btn-login">급여 달력</button>
        </a>
      </div>
    </div>
    <div style="font-size: 16px; font-weight: bold; margin-bottom: 5%">
      이번 주
    </div>
    <div>
      <dl>
        <dt class="title" style="display: flex; flex-direction: row">
          <div style="flex: 1">수원문화체육센터</div>
        </dt>
        <div class="footer">
          <dd class="date">2022.03.14(월) ~ 2022.03.17(목)</dd>
        </div>
        <div style="display: flex; flex-direction: row">
          <div
            style="padding-bottom: 15px; padding-top: 15px; color=#2D3238; font-size: 20px; font-weight: bold; margin-right: 10px"
          >
            150,000원/일(1.0)
          </div>
          <div class="date">공수/급여 수정 ></div>
        </div>
        <div class="date" style="margin-top: 2%">급여가 지급 되었나요?</div>
      </dl>
    </div>
    <div :style="border_style"></div>
    <div>
      <dl>
        <dt class="title" style="display: flex; flex-direction: row">
          <div style="flex: 1">수원문화체육센터</div>
          <!-- 3 dots go  here -->
          <!-- <div style="justify-content: flex-end">hi</div> -->
        </dt>
        <div class="footer">
          <dd class="date">2022.03.14(월) ~ 2022.03.17(목)</dd>
        </div>
        <div style="display: flex; flex-direction: row">
          <div
            style="padding-bottom: 15px; padding-top: 15px; color=#2D3238; font-size: 20px; font-weight: bold; margin-right: 10px"
          >
            150,000원/일(1.0)
          </div>
          <div class="date">공수/급여 수정 ></div>
        </div>
        <div class="date" style="margin-top: 2%">급여가 지급 되었나요?</div>
      </dl>
    </div>
    <div :style="border_style"></div>
    <div style="font-size: 16px; font-weight: bold; margin-bottom: 5%">
      이번 달
    </div>
    <div>
      <dl>
        <dt class="title" style="display: flex; flex-direction: row">
          <div style="flex: 1">수원문화체육센터</div>
        </dt>
        <div class="footer">
          <dd class="date">2022.03.14(월) ~ 2022.03.17(목)</dd>
        </div>
        <div style="display: flex; flex-direction: row">
          <div
            style="padding-bottom: 15px; padding-top: 15px; color=#2D3238; font-size: 20px; font-weight: bold; margin-right: 10px"
          >
            150,000원/일(1.0)
          </div>
          <div class="date">공수/급여 수정 ></div>
        </div>
        <div class="date" style="margin-top: 2%">급여가 지급 되었나요?</div>
      </dl>
    </div>
    <div :style="border_style"></div>
  </section>
</template>

<script>
  import evaluation from '@/components/common/evaluation'
  import api from '@/api/api'
  import moment from 'moment'
  import back from '@/components/layout/back'

  export default {
    components: {
      evaluation,
      back
    },
    data() {
      return {
        border_style:
          'width: 100vw; height: 0.3rem; background-color: #eef1f5; margin-bottom: 2rem; margin-top: 2rem',
        select: '0',
        ratingList: [],
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
      handleSelect(v) {
        this.select = v
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
            .get(`mypage/workHistory/${this.pageNo}`, config)
            .then(result => {
              console.log(result)
              if (result.data.success) {
                if (
                  result.data.data !== undefined &&
                  result.data.data !== null
                ) {
                  this.ratingList = result.data.data.result.map(value => {
                    value.timestamp_create = moment(value.timestamp_create)
                      .utc()
                      .format('YYYY.MM.DD')
                    return value
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
      }
    }
  }
</script>
