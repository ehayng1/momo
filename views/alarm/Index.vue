<template>
  <section class="page alarm">
    <back />
    <div class="info">
      <h1 class="title">알림</h1>
    </div>
    <div class="list">
      <div v-if="list.length" class="">
        <div v-for="(item, key) in list" :key="key" class="card">
          <div class="content">{{ item.push_content }}</div>
          <div class="date">{{ item.timestamp_create }}</div>
        </div>
      </div>
      <div v-else class="empty">
        <div class="icon">
          <off />
        </div>
        <span class="content">알림 내역이 존재하지 않습니다.</span>
      </div>
    </div>
  </section>
</template>

<script>
  import off from '@/static/images/common/notifications_off.svg'
  import api from '@/api/api'
  import moment from 'moment'
  import back from '@/components/layout/back'

  export default {
    components: { off, back },
    data() {
      return {
        list: [
          // {
          //   content: `알림 리스트 입니다.알림 리스트 입니다.알림 리스트 입니다.알림 리스트 입니다.알림 리스트 입니다.알림 리스트 입니다.알림 리스트 입니다.알림 리스트 입니다.알림 리스트 입니다.알림 리스트 입니다.`,
          //   date: '2021.08.11'
          // },
          // {
          //   content: '미확인 구인구직 알림이 존재합니다.',
          //   date: '2021.08.11'
          // },
          // {
          //   content: '어플이 정상적으로 업데이트 되었습니다.',
          //   date: '2021.08.11'
          // }
        ]
      }
    },
    methods: {
      async getAlarmList() {
        try {
          const config = {
            headers: {
              'Content-Type': 'application/json; charset = utf-8',
              'Access-Control-Allow-Origin': '*',
              authorization: `Bearer ${this.$store.getters.token}`
            }
          }
          await api
            .get(`alarm/list`, config)
            .then(result => {
              console.log(result)
              if (result.data.success) {
                if (
                  result.data.data.result !== undefined &&
                  result.data.data.result !== null
                ) {
                  let list = result.data.data.result
                  list = list.map(value => {
                    value.timestamp_create = moment(
                      value.timestamp_create.split('.')[0]
                    )
                      .utc()
                      .format('YYYY.MM.DD')
                    return value
                  })
                  this.list = list
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
      }
    },
    mounted() {
      this.getAlarmList()
    }
  }
</script>
