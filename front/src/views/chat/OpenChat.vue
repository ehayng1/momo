<template>
  <div class="chatRoomList">
    <div class="chatToptitle">
      <back />

      <p class="title">{{ title }} ({{ this.people }})</p>
    </div>
    <div class="contentWrapper" id="contentWrapper">
      <ul id="chat__body" class="content">
        <li v-for="(item, idx) in reciveList" :key="idx" class="chat_item">
          <span
            :style="{ color: item.writer === userName ? '#00778B' : '#000' }"
            @click="report(item.writer)"
          >
            {{ item.writer }}
          </span>

          <span> {{ item.message }}</span>
          <div class="date">
            <p>{{ item.date }}</p>
          </div>
        </li>
      </ul>
    </div>
    <div class="footer" ref="footer">
      <div class="top">
        <input
          @keyup.enter="sendMessage"
          v-model="message"
          type="text"
          placeholder="메세지를 입력하세요."
        />
      </div>
      <div class="bottom">
        <button @click="sendMessage" type="button" class="btn-login">
          보내기
        </button>
      </div>
    </div>
  </div>
</template>

<script>
  import back from '@/components/layout/back'
  import textInput from '@/components/common/inputArea'
  import Stomp from 'webstomp-client'
  import SockJS from 'sockjs-client'
  import api from '@/api/api'
  import moment from 'moment'
  export default {
    name: 'OpenChat',
    // eslint-disable-next-line vue/no-unused-components
    components: { back, textInput },
    data() {
      return {
        userName: '',
        message: '',
        reciveList: [],
        title: '',
        people: 0,
        roomIdx: ''
        // sendDate: new Date()
      }
    },
    mounted() {
      this.title = this.$route.query.name
      this.getInfo()
      this.connect()
      console.log(Stomp, SockJS)
    },
    beforeDestroy() {
      this.exit()
    },
    methods: {
      async getInfo() {
        if (this.$store.getters.token === '') {
          this.$dialog
            .show({
              type: 'alert',
              title: '로그인정보가 없습니다'
            })
            .$on('dialog:confirm', () => {
              this.$router.push({ name: 'Main' })
            })
        } else {
          try {
            const config = {
              headers: {
                'Content-Type': 'application/json; charset = utf-8',
                'Access-Control-Allow-Origin': '*',
                authorization: `Bearer ${this.$store.getters.token}`
              }
            }
            await api
              .get('mypage/info', config)
              .then(result => {
                console.log(result)
                if (result.data.success) {
                  console.log('success')
                  if (
                    result.data.data !== undefined &&
                    result.data.data !== null
                  ) {
                    this.userName = result.data.data.result.nickname
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
      },
      sendMessage() {
        if (this.message !== '') {
          this.send()
          this.message = ''
        }
      },
      handleMessage(e) {
        console.log(e)
        console.log(e.keyCode)
        if (e.keyCode === 13 && this.message !== '') {
          this.send()
          this.message = ''
        }
      },
      send() {
        console.log('Send message:' + this.stompClient.send)
        if (this.stompClient && this.stompClient.connected) {
          let date = moment(new Date()).format('HH:mm')
          const msg = {
            writer: this.userName,
            message: this.message,
            // token: this.$store.getters.token,
            roomId: this.$route.query.idx,
            date: date
          }
          console.log(msg)
          this.stompClient.send('/pub/api/v1/chat/message', JSON.stringify(msg))
        }
      },
      connect() {
        this.roomIdx = this.$route.query.idx
        // const serverURL = 'http://localhost:8080/ws'
        let socket = new SockJS('/api/v1/stomp/chat')
        this.stompClient = Stomp.over(socket)
        console.log(`소켓 연결을 시도합니다. 서버 주소: ${socket}`)
        console.log(socket)
        this.stompClient.connect(
          {
            'Content-Type': 'application/json; charset = utf-8',
            'Access-Control-Allow-Origin': '*',
            authorization: `Bearer ${this.$store.getters.token}`
          },
          frame => {
            // 소켓 연결 성공
            this.connected = true
            console.log('소켓 연결 성공', frame)
            // 서버의 메시지 전송 endpoint를 구독합니다.
            // 이런형태를 pub sub 구조라고 합니다.

            this.stompClient.subscribe(
              `/sub/api/v1/chat/room/${this.$route.query.idx}`,
              res => {
                console.log(
                  '구독으로 받은 메시지 입니다.',
                  JSON.parse(res.body)
                )
                // 받은 데이터를 json으로 파싱하고 리스트에 넣어줍니다.
                // this.reciveList.push(JSON.parse(res.body))
                // let date = moment(new Date()).format('HH:mm')
                // res.body.date = date
                new Promise(resolve => {
                  this.renewalMessage(res)
                  resolve()
                })
                  .then(() => {
                    const body = this.$el.querySelector('#contentWrapper')
                    const eh = body.scrollHeight + body.scrollTop
                    const isScroll = body.scrollHeight < eh
                    if (isScroll) {
                      body.scrollTop = body.scrollHeight
                    }
                  })
                  .catch(err => console.log(err))
              }
            )
            this.enter()
          },
          error => {
            // 소켓 연결 실패
            console.log('소켓 연결 실패', error)
            this.connected = false
          }
        )
      },
      async renewalMessage(obj) {
        console.log(obj)
        const body = JSON.parse(obj.body)
        this.people = body.peoples
        if (body.message === undefined || body.message === null) {
          return
        }
        await this.reciveList.push(JSON.parse(obj.body))
      },
      async enter() {
        // enter 처리
        const msg = {
          writer: this.userName,
          roomId: this.$route.query.idx
        }
        await this.stompClient.send(
          '/pub/api/v1/chat/enter',
          JSON.stringify(msg)
        )
      },
      async exit() {
        // exit 처리
        const msg = {
          writer: this.userName,
          roomId: this.roomIdx
        }
        await this.stompClient.send(
          '/pub/api/v1/chat/exit',
          JSON.stringify(msg)
        )
      },
      async report(name) {
        if (name === this.userName) {
          return false
        }
        if (this.$store.getters.token === '') {
          this.$dialog.show({
            type: 'alert',
            title: '로그인을 해주세요'
          })
          return false
        }
        await this.$dialog
          .show({
            type: 'confirm',
            title: '사용자를 신고하시겠습니까?',
            data: {
              confirmText: '신고'
            }
          })
          .$on('dialog:confirm', () => {
            try {
              const config = {
                headers: {
                  'Content-Type': 'application/json; charset = utf-8',
                  'Access-Control-Allow-Origin': '*',
                  authorization: `Bearer ${this.$store.getters.token}`
                }
              }
              const body = {
                name: name,
                type: 'nickname'
              }
              api
                .post(`report/insert/chat`, body, config)
                .then(result => {
                  console.log(result)
                  if (result.data.success) {
                    this.$dialog.show({
                      type: 'alert',
                      title: '신고되었습니다.'
                    })
                  } else {
                    this.$dialog.show({
                      type: 'alert',
                      title: '에러 발생'
                    })
                  }
                })
                .catch(err => console.log(err))
            } catch (err) {
              console.log(err)
            }
          })
      }
    }
  }
</script>
