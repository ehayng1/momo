<template>
  <div class="container">
    <div class="score">
      <scoring
        :value="safe"
        @input="onChangeSafe"
        title="안전"
        icon="safety_helmet"
      />
      <scoring
        :value="welfare"
        @input="onChangeWelfare"
        title="복지"
        icon="gift"
      />
      <scoring :value="hard" @input="onChangeHard" title="강도" icon="wrench" />
      <scoring
        :value="payment"
        @input="onChangePayment"
        title="급여"
        icon="coins"
      />
      <scoring :value="meal" @input="onChangeMeal" title="식사" icon="spoon" />
      <scoring :value="rest" @input="onChangeRest" title="휴식" icon="coffee" />
    </div>

    <div class="review">
      <h2 class="title">종합 한줄평</h2>
      <text-input
        type="text"
        v-model="comment"
        placeholder="현장에 대해 한마디로 정리하자면?"
      />
      <div class="liked">
        <div class="block" v-on:click="() => this.changeLike('like')">
          <like class="like" :class="[like === 'like' ? 'active' : null]" />
          <p>추천하기</p>
        </div>
        <div class="block" v-on:click="() => this.changeLike('unlike')">
          <like
            class="like un"
            :class="[like === 'unlike' ? 'active' : null]"
          />
          <p>아쉬워요</p>
        </div>
      </div>
    </div>

    <p class="message">
      평가 완료자 대해서는 일한 이력에 대해서 모모에서 관리해드립니다.
    </p>

    <a class="big_btn mt0" @click="onSubmit">평가 완료</a>
  </div>
</template>

<script>
  import textInput from '@/components/common/input'
  import scoring from '@/components/common/scoring'
  import like from '@/static/images/common/big_like.svg'
  import api from '@/api/api'

  export default {
    name: 'StepFour',
    components: {
      textInput,
      scoring,
      like
      // textInput: () => import('@/components/common/input'),
      // scoring: () => import('@/components/common/scoring'),
      // like: () => import('@/static/images/common/big_like.svg')
      // safetyHelmet: () => import('@/static/images/common/safety_helmet.svg'),
    },
    data() {
      return {
        like: '',
        safe: '',
        welfare: '',
        hard: '',
        payment: '',
        meal: '',
        rest: '',
        comment: ''
      }
    },
    methods: {
      changeLike(v) {
        if (this.like === v) {
          this.like = ''
        } else {
          this.like = v
        }
      },
      onChangeSafe(val) {
        console.log(val)
        this.safe = val
      },
      onChangeWelfare(val) {
        console.log(val)
        this.welfare = val
      },
      onChangeHard(val) {
        console.log(val)
        this.hard = val
      },
      onChangePayment(val) {
        console.log(val)
        this.payment = val
      },
      onChangeMeal(val) {
        console.log(val)
        this.meal = val
      },
      onChangeRest(val) {
        console.log(val)
        this.rest = val
      },
      async onSubmit() {
        if (
          this.safe === '' ||
          this.welfare === '' ||
          this.hard === '' ||
          this.payment === '' ||
          this.meal === '' ||
          this.rest === ''
        ) {
          this.$dialog.show({
            type: 'alert',
            title: '별점을 매겨주세요'
          })
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
          const body = {
            site_idx: this.$route.query.siteIdx,
            user_divide_first: `${this.$route.query.type1}/${this.$route.query.type2}`,
            user_divide_second: `${this.$route.query.year}`,
            site_rating_safe: this.safe,
            site_rating_welfare: this.welfare,
            site_rating_hard: this.hard,
            site_rating_payment: this.payment,
            site_rating_meal: this.meal,
            site_rating_rest: this.rest,
            site_rating_up: this.like === 'like' ? 1 : 0,
            site_rating_down: this.like === 'unlike' ? 1 : 0,
            site_rating_comment: this.comment
          }
          await api
            .post(`company/site`, body, config)
            .then(result => {
              console.log(result)
              if (result.data.success) {
                console.log('success')
                this.$dialog
                  .show({
                    type: 'alert', //confirm or alert
                    title: '평가 성공'
                  })
                  .$on('dialog:confirm', () => {
                    this.goHome()
                  })
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
      goHome() {
        console.log(this.$router)
        this.$router.push({ path: '/' })
      }
    },
    mounted() {
      console.log(this.$route.params)
      console.log(this.$route.query)
    }
  }
</script>
