<template>
  <section class="page work_history">
    <back />
    <div class="info" ref="container">
      <h1 class="title">업무이력</h1>
    </div>
    <evaluation v-bind:list="ratingList" />
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
