<template>
  <div class="alarm">
    <back />
    <div class="info">
      <h1 class="title">채팅</h1>
    </div>
    <div class="list">
      <ul v-if="list" class="">
        <li
          v-for="(item, key) in list"
          class="card"
          :key="key"
          @click="
            goPage(
              item.chatroom_idx,
              item.user_idx_1,
              item.chat_title,
              item.name_2,
              item
            )
          "
        >
          <dl class="contentsite">
            <dd class="chatContent">{{ item.content }}</dd>
            <button
              @click="outChatingRoom(item.chatroom_idx)"
              @click.stop=""
              class="btn-out"
            >
              나가기
            </button>
          </dl>
        </li>
      </ul>
      <!-- 메세지없을때  -->

      <ul v-else class="">
        <li class="empty">
          <dl>
            <dd class="icon">
              <subtitlesOff />
            </dd>

            <dd class="content">메세지 항목이 존재하지 않습니다.</dd>
          </dl>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
  import back from '@/components/layout/back'
  import api from '@/api/api'
  // import moment from 'moment'

  export default {
    components: { back },
    mounted() {
      this.getList()
      // this.outChatingRoom()
    },
    data() {
      return {
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

      goPage(v, idx, title, name, item) {
        this.$router.push({
          name: 'detailChat',
          query: { idx: v },
          params: { data: idx, title: title, name: name, item }
        })
        // this.$dialog.show({})
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
