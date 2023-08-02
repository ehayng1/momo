<template>
  <div class="alarm">
    <back />
    <div class="info">
      <h1 class="title">메세지</h1>
    </div>
    <div>
      <div class="list">
        <ul v-if="list.length" class="">
          <li
            v-for="(item, key) in list"
            class="card"
            :key="key"
            @click="goPage(item.chatroom_idx, item.chat_title)"
            style="border-bottom: 0px; box-shadow: 0px"
          >
            <div style="flex-direction: column">
              <dl class="contentsite">
                <div style="flex-direction: row">
                  <dd style="margin-bottom: 1em" class="username">
                    {{ item.chat_title }}
                  </dd>
                  <div
                    style="color: #6c747c; font-weight: normal; font-size: 14px"
                  >
                    {{ item.timestamp_create }}
                  </div>
                  <dd class="chatContent">{{ item.content }}</dd>
                </div>
                <!-- <button
              @click="outChatingRoom(item.chatroom_idx)"
              @click.stop=""
              class="btn-out"
            >
              나가기
            </button> -->
              </dl>
              <div :style="border_style"></div>
            </div>
          </li>
        </ul>
        <!-- 메세지없을때  -->

        <ul v-else class="">
          <li class="empty">
            <dl>
              <dd class="icon">
                <subtitlesOff />
              </dd>

              <dd class="content">아직 채팅 내역이 없어요.</dd>
            </dl>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
  import subtitlesOff from '@/static/images/common/subtitles_off.svg'
  import back from '@/components/layout/back'
  import api from '@/api/api'
  import moment from 'moment'

  export default {
    components: { subtitlesOff, back },
    mounted() {
      this.getList()
      // this.outChatingRoom()
    },
    data() {
      return {
        border_style:
          'width: 95vw; height: 0.1rem; background-color: #eef1f5; margin-bottom: 1rem; margin-top: 1rem',
        list: []
        // work_user_idx: ''
      }
    },
    methods: {
      outChatingRoom(idx) {
        this.$dialog.show({
          type: 'confirm',
          title: '채팅방을 나가시겠습니까 ?',
          data: {
            chatroomIdx: idx
          }
        })
        console.log(idx)
      },

      goPage(idx, title) {
        this.$router.push({
          name: 'detailChat',
          query: { idx: idx, name: title }
        })
      },
      async getList() {
        if (this.$store.getters.token === '') {
          this.$dialog.show({
            type: 'alert',
            title: '로그인 후 이용해주세요'
          })
          return false
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
            api
              .get('chat/chatroom', config)
              .then(result => {
                console.log(result)
                if (result.data.success) {
                  if (result.data.message === 'success') {
                    this.list = result.data.data.result
                    // this.list.user_idx_1
                    console.log(this.list, '12312321')
                    let list = result.data.data.result
                    list.map(value => {
                      if (String(value.issue_content).length > 15) {
                        value.issue_content =
                          String(value.issue_content).substring(0, 15) + '...'
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
                          value.timestamp_create =
                            parseInt(diff / 60) + '시간전'
                        } else if (diff < 60 * 24 * 7) {
                          value.timestamp_create =
                            parseInt(diff / 1440) + '일전'
                        } else if (diff < 60 * 24 * 7 * 4) {
                          value.timestamp_create =
                            parseInt(diff / 10080) + '주일전'
                        } else if (diff < 60 * 24 * 7 * 4 * 12) {
                          value.timestamp_create =
                            parseInt(diff / 40320) + '달전'
                        }
                      } else {
                        value.timestamp_create = moment(
                          value.timestamp_create
                        ).format('YYYY.MM.DD')
                      }
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
            alert('에러발생')
            return
          }
        }
      }
    }
  }
</script>
