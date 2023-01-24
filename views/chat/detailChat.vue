<template>
  <div class="chatRoomList">
    <div class="chatToptitle">
      <back />

      <!-- <p class="title">{{ this.list.chat_title  }} 님과의 채팅</p> -->
    </div>
    <div id="chat__body" class="chat__body">
      <div>
        <div v-for="(item, idx) in reciveList" :key="idx">
          <div v-if="myMessage(item.recvname)" class="myMessage">
            <!-- {{ item.content }} -->
            {{ item.recvname }}

            <!-- {{ item.recvname }} -->
            {{ item.content }}
          </div>

          <!-- {{ item.recvname.slice(0, 6) }}
          {{ item.content }} -->
          <!-- <div v-if="myMessage(item.recvname)" class="recvMessage">
            {{ item.content }}
          </div> -->
          <div>
            <div v-if="!myMessage(item.recvname)" class="recv">
              <div class="recvMessage">
                <p v-if="!myMessage(item.recvname)">{{ item.content }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <footer class="footer" ref="footer">
      <form class="form">
        <div class="top">
          <text-input
            @click="sendMessage"
            v-model="message"
            type="text"
            placeholder="메세지를 입력하세요."
          />
        </div>
        <div class="bottom">
          <button
            @click="sendMessage"
            v-bind:disabled="message == ''"
            type="button"
            class="btn-login"
          >
            보내기
          </button>
        </div>
      </form>
    </footer>
  </div>
</template>

<script>
  import back from '@/components/layout/back'
  import textInput from '@/components/common/inputArea'
  import Stomp from 'webstomp-client'
  import SockJS from 'sockjs-client'
  import api from '@/api/api'

  // import moment from 'moment'/
  export default {
    name: 'detailChat',
    components: { back, textInput },
    data() {
      return {
        toeknName: this.$store.getters.token,
        userName: '',
        message: '',
        reciveList: [],
        paramsList: [],
        stompClient: null,
        name: this.$route.params.name,
        isSame: false,
        list: this.$route.params.item
      }
    },

    computed() {
      // this.getMessage()
    },
    mounted() {
      console.log(this.$route.params)
      // console.log(this.test, 'thist,test')
      this.getMessage()

      setTimeout(() => {
        const element = document.getElementById('chat__body')
        element.scrollTop = element.scrollHeight
      }, 0)
    },
    created() {
      this.connect()
    },
    methods: {
      myMessage(id) {
        // console.log(this.$route.params.item.name_2, id, 'id')
        // return (
        if (id) {
          id === this.$route.params.name
          // console.log(this., 'ud')
          // console.log('여기타니 ?')
          console.log(id === this.$route.params.name)
          return id === this.reciveList.recvname
        } else {
          // return id == this.$route.params?.chatData?.test?.code_blockchain
          //  return id === this.$route.params.item.name_1
          // return id !== this.$route.params.item.name_2
        }
        // this.$route.params.item.name_1
        // )
        // return this.$route.params.data.test.code_blockchain == id
        // console.log(dfs, '!231231')
        // return id == this.reciveList.sendname
      },
      sendMessage(e) {
        this.sendTO()

        this.send()
        this.message = ''

        if (e.keyCode === 13 && this.message !== '') {
          this.send()
          this.sendTO()

          this.message = ''
        }
      },
      async getMessage() {
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

          await api
            .get(`chat/chatroom/${this.$route.query.idx}`, config)
            .then(result => {
              // console.log(result.data.data.result, 'resulte')
              if (result.data.success) {
                if (result.data.message === 'success') {
                  this.reciveList = result.data.data.result
                  console.log(this.reciveList, 'result')
                  setTimeout(() => {
                    const element = document.getElementById('chat__body')
                    element.scrollTop = element.scrollHeight
                  }, 0)
                  // window.location.href = 'tel://' + phone
                }
              } else {
                this.$dialog.show({
                  type: 'alert',
                  title: result.data.message
                })
              }
            })
            .catch(err => console.log(err))
          // this.stompClient.send('/api/v1/chat/send', JSON.stringify(msg), {})
        } catch (err) {
          console.log(err)
          alert('에러발생')
          return
        }
      },
      async send() {
        // console.log('Send message:' + this.stompClient.send)
        if (this.stompClient && this.stompClient.connected) {
          const msg = {
            chatroom_idx: this.$route.params.id || this.$route.query.idx,
            sendName: this.$store.getters.token,
            recvName:
              this.$route.params?.item?.user_idx_2 ||
              this.$route.params?.item?.user_idx_1 ||
              this.$route.params?.data?.userIdx,

            // this.$route.params.item.user_idx_2 ||
            // this.$route.params.item.user_idx_1,
            // this.$route.params.data.work_user_idx ||
            // this.$route.params.data.user_idx_2,
            content: this.message
            // token: this.$store.getters.token
          }

          const config = {
            headers: {
              'Content-Type': 'application/json; charset = utf-8',
              'Access-Control-Allow-Origin': '*',
              // 'Access-Control-Allow-Headers': '*',
              // 'Access-Control-Allow-Methods': '*',
              authorization: `Bearer ${this.$store.getters.token}`
            }
          }
          console.log(msg, 'msg')
          await api
            .post('chat/send', msg, config)
            .then(result => {
              if (result.data.success) {
                if (result.data.message === 'success') {
                  console.log(result, '!231231231')
                  // console.log(this.$store.getters.token === '김정훈')
                  this.getMessage()
                  // console.log(data, 'get')
                  // console.log(result.data, 'userIddx')
                  // this.stompClient.send(
                  //   '/api/v1/chat/send',
                  //   JSON.stringify(msg),
                  //   {}
                  // )
                  // window.location.href = 'tel://' + phone
                }
              } else {
                this.$dialog.show({
                  type: 'alert',
                  title: result.data.message
                })
              }
            })
            .catch(err => console.log(err))
        }
      },
      sendTO() {
        // e.preventDefault()

        var msg = {
          chatroom_idx: this.$route.params.id || this.$route.query.idx,
          // userName: this.list.chat_title,
          userName: this.$store.getters.token,
          recvname:
            this.$route.params?.item?.user_idx_2 ||
            this.$route.params?.item?.user_idx_1 ||
            this.$route.params?.data?.userIdx,
          // this.$route.params.data.work_user_idx ||
          // this.list.chatroom_user_idx_2 || this.list.chatroom_user_idx_1,
          // recvName:
          //   this.$route.params.data.work_user_idx || this.$route.params.data,
          content: this.message
        }
        console.log(msg, 'sendTo message')
        this.stompClient.send('/send', JSON.stringify(msg), {})
        console.log('Send message:' + this.message)
      },
      connect() {
        // const serverURL = 'api/v1/chat/detail'
        let socket = new SockJS('/api/v1/chat/detail')
        // this.stompClient = over(socket)

        this.stompClient = Stomp.over(socket)
        // console.log(
        //   `소켓 연결을 시도합니다. 서버 주소: ${'api/v1/chat/detail'}`
        // )
        this.stompClient.connect(
          {
            'Content-Type': 'application/json; charset = utf-8',
            'Access-Control-Allow-Origin': '*',
            // 'Access-Control-Allow-Headers': '*',
            // 'Access-Control-Allow-Methods': '*',
            authorization: `Bearer ${this.$store.getters.token}`
          },
          // onConnected,

          frame => {
            // 소켓 연결 성공
            this.connected = true
            console.log('소켓 연결 성공', frame)

            this.stompClient.subscribe('/send', res => {
              // res.body.userName = this.test
              // console.log(res.body.userName, 'subscribe')
              console.log(this.reciveList, 'data')
              this.reciveList.push(JSON.parse(res.body))
            })
          },

          error => {
            // 소켓 연결 실패
            console.log('소켓 연결 실패', error)
            this.connected = false
          }
        )
      }
    }
  }
</script>
