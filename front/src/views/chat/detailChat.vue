<template>
  <div class="chatRoomList">
    <div class="chatToptitle">
      <back />

      <p class="title">{{ name }}</p>
    </div>
    <!-- <div id="chat__body" class="chat__body">
      <div>
        <div v-for="(item, idx) in reciveList" :key="idx">
          <div v-if="myMessage(item.recvname)" class="myMessage">
            {{ item.recvname }}
            {{ item.content }}
          </div>
          <div>
            <div v-if="!myMessage(item.recvname)" class="recv">
              <div class="recvMessage">
                <p v-if="!myMessage(item.recvname)">{{ item.content }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div> -->

    <div class="contentWrapper" id="contentWrapper">
      <ul id="chat__body" class="content">
        <li
          v-for="(item, idx) in reciveList"
          :key="idx"
          class="chat_item"
          style="margin-bottom: 0.5rem"
        >
          <!-- NOT ME speaking -->
          <!-- <div v-if="true"> -->
          <div v-if="item.writer !== userName">
            <div v-if="true">
              <!-- <div v-if="this.prevSpeaker === userName"> -->
              <div style="display: flex">
                <!-- <div class="img_back">
                  <img
                    v-if="profileImg !== undefined"
                    :src="
                      'https://momobuild.s3.ap-northeast-2.amazonaws.com/' +
                      profileImg
                    "
                  />
                  <user v-else class="img" />
                </div> -->
                <div style="display: flex; flex-direction: column"></div>
                <span
                  :style="{
                    color: item.writer === userName ? '#00778B' : '#000'
                  }"
                  @click="report(item.writer)"
                >
                  {{ item.writer }}
                </span>
                <span
                  style="
                    flex: 1;
                    border-radius: 2rem;
                    background-color: #eef1f5;
                    padding: 1rem;
                    color: #2d3238;
                  "
                >
                  {{ item.message }}</span
                >
                <div
                  class="date"
                  style="color: #6C747C;
              font-weight: 400;
              font-size: 1.2rem;
              align-self: end;
              }"
                >
                  <p>{{ item.date.slice(-5) }}</p>
                </div>
              </div>
            </div>
            <div v-else>
              <span
                style="
                  flex: 1;
                  border-radius: 2rem;
                  background-color: #eef1f5;
                  padding: 1rem;
                  color: #2d3238;
                "
              >
                {{ item.message }}</span
              >
              <div
                class="date"
                style="color: #6C747C;
              font-weight: 400;
              font-size: 1.2rem;
              align-self: end;
              }"
              >
                <p>{{ item.date.slice(-5) }}</p>
              </div>
            </div>
          </div>
          <!-- I AM speaking -->
          <div v-else>
            <div style="display: flex">
              <!-- <div class="img_back">
                <img
                  v-if="profileImg !== undefined"
                  :src="
                    'https://momobuild.s3.ap-northeast-2.amazonaws.com/' +
                    profileImg
                  "
                />
                <user v-else class="img" />
              </div> -->
              <div style="display: flex">
                <div
                  class="date"
                  style="color: #6C747C;
              font-weight: 400;
              font-size: 1.2rem;
              align-self: end;
              }"
                >
                  <p>{{ item.date.slice(-5) }}</p>
                </div>
                <span
                  style="
                    margin-left: 0px;
                    border-radius: 2rem;
                    background-color: #008ff6;
                    padding: 1rem;
                    color: white;
                  "
                >
                  {{ item.message }}</span
                >
              </div>
            </div>
          </div>
        </li>
      </ul>
    </div>

    <footer class="footer" ref="footer">
      <div class="top">
        <button @click="showJobs" type="button" class="btn-login">제안</button>
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
    </footer>
  </div>
</template>

<script>
  import back from '@/components/layout/back'
  import textInput from '@/components/common/inputArea'
  import Stomp from 'webstomp-client'
  import SockJS from 'sockjs-client'
  import api from '@/api/api'
  import moment from 'moment'
  import jobDialog from '@/plugins/dialog/jobDialog'

  export default {
    name: 'detailChat',
    // eslint-disable-next-line vue/no-unused-components
    components: { back, textInput },
    data() {
      return {
        toeknName: this.$store.getters.token,
        userName: '',
        message: '',
        reciveList: [],
        stompClient: null,
        name: this.$route.query.name,
        isSame: false,
        list: this.$route.params.item,
        recvIdx: '',
        jobList: [],
        selectedItem: '',
        profileImg: undefined, // 프로필 이미지
        prevSpeaker: '', // currently speaking
        prevTimeStamp: ''
      }
    },

    computed() {
      // this.getMessage()
    },
    mounted() {
      console.log(this.$route.params)
      // console.log(this.test, 'thist,test')
      this.getInfo()
      this.getIssueDetail()
      this.getJobList()
      this.getMessage()
      this.connect()
      setTimeout(() => {
        const element = document.getElementById('chat__body')
        element.scrollTop = element.scrollHeight
      }, 0)
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
                    this.userName = result.data.data.result.user_name
                  }
                } else {
                  this.$dialog.show({
                    type: 'alert',
                    title: result.data.message
                  })
                }
              })
              .catch(err => console.log(err))

            await api
              .get(`chat/info/${this.$route.query.idx}`, config)
              .then(result => {
                console.log(result)
                if (result.data.success) {
                  if (
                    result.data.data !== undefined &&
                    result.data.data !== null
                  ) {
                    this.recvIdx = result.data.data.recv
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
      // get profileImg
      async getIssueDetail() {
        try {
          const config = {
            headers: {
              'Content-Type': 'application/json; charset = utf-8',
              'Access-Control-Allow-Origin': '*',
              authorization: `Bearer ${this.$store.getters.token}`
            }
          }
          await api
            .get(
              `board/open/board/detail/${this.$route.query.issueIdx}`,
              config
            )
            .then(result => {
              if (result.data.success) {
                if (
                  result.data.data.detail !== undefined &&
                  result.data.data.detail !== null
                ) {
                  const detail = result.data.data.detail
                  // this.title = detail.issue_title
                  // this.userIdx = detail.issue_user_idx
                  // this.id = detail.nickname
                  //   ? detail.nickname
                  //   : detail.code_blockchain
                  // this.count = this.$options.filters.autoCommaNumber(
                  //   detail.view_count
                  // )
                  // this.replyCount = this.$options.filters.autoCommaNumber(
                  //   result.data.data.reply.length
                  // )
                  console.log(detail)
                  this.profileImg = detail.user_image

                  // let timestamp = moment(detail.timestamp_create).format()
                  // let now = moment()
                  // let diff = now.diff(timestamp, 'minutes')
                  // if (diff < 60 * 24 * 7 * 4 * 12) {
                  //   if (diff < 3) {
                  //     detail.timestamp_create = '얼마전'
                  //   } else if (diff < 60) {
                  //     detail.timestamp_create = diff + '분전'
                  //   } else if (diff < 60 * 24) {
                  //     detail.timestamp_create = parseInt(diff / 60) + '시간전'
                  //   } else if (diff < 60 * 24 * 7) {
                  //     detail.timestamp_create = parseInt(diff / 1440) + '일전'
                  //   } else if (diff < 60 * 24 * 7 * 4) {
                  //     detail.timestamp_create =
                  //       parseInt(diff / 10080) + '주일전'
                  //   } else if (diff < 60 * 24 * 7 * 4 * 12) {
                  //     detail.timestamp_create = parseInt(diff / 40320) + '달전'
                  //   }
                  // } else {
                  //   detail.timestamp_create = moment(
                  //     detail.timestamp_create
                  //   ).format('YYYY.MM.DD')
                  // }
                  // this.date = detail.timestamp_create
                  // this.date = moment(detail.timestamp_create)
                  //   .utc(9)
                  //   .format('MM.DD HH:mm')

                  // this.content = detail.issue_content
                  // let replyList = result.data.data.reply
                  // replyList = replyList.map(value => {
                  //   value.modifyReply = false
                  //   value.timestamp_create = moment(value.timestamp_create)
                  //     .utc(9)
                  //     .format('MM.DD HH:mm')
                  //   return value
                  // })
                  // this.comment = replyList
                  // // 이미지 / 비디오 구분용
                  // let flag = false
                  // const imageExtension = [
                  //   '.bmp',
                  //   '.rle',
                  //   '.dib',
                  //   '.jpg',
                  //   '.jpeg',
                  //   '.gif',
                  //   '.png',
                  //   '.tif',
                  //   '.tiff',
                  //   '.raw',
                  //   '.svg',
                  //   '.webp'
                  // ]
                  // if (
                  //   String(detail.issue_image) !== undefined &&
                  //   String(detail.issue_image) !== null &&
                  //   String(detail.issue_image) !== ''
                  // ) {
                  //   const loc = String(detail.issue_image).toLowerCase()
                  //   imageExtension.map(inner => {
                  //     if (String(loc).includes(inner)) {
                  //       flag = true
                  //     }
                  //   })
                  // }
                  // if (
                  //   detail.issue_additional !== undefined &&
                  //   detail.issue_additional !== null &&
                  //   String(detail.issue_additional) !== ''
                  // ) {
                  //   const loc = String(detail.issue_additional).toLowerCase()
                  //   imageExtension.map(inner => {
                  //     if (String(loc).includes(inner)) {
                  //       this.additional = detail.issue_additional
                  //       this.additionalDivision = 'image'
                  //     }
                  //   })
                  //   if (this.additionalDivision === '') {
                  //     this.additional = 'true'
                  //     this.additionalDivision = 'video'
                  //     this.additionalPlayerOptions.sources[0].src =
                  //       detail.issue_additional
                  //   }
                  // }
                  // if (flag) {
                  //   this.image = detail.issue_image
                  // } else {
                  //   this.video = detail.issue_image
                  //   this.playerOptions.sources[0].src =
                  //     'https://momobuild.s3.ap-northeast-2.amazonaws.com/' +
                  //     detail.issue_image
                  // }
                  // this.modify = detail.modify
                  // this.files = result.data.data.files
                }
              } else {
                // this.$dialog.show({
                //   type: 'alert',
                //   title: result.data.message
                // })
              }
            })
            .catch(err => console.log(err))
        } catch (err) {
          console.log(err)
        }
      },
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
      sendMessage() {
        if (this.message !== '') {
          this.send()
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
                  let list = result.data.data.result
                  list = list.map(val => {
                    val.date = moment(val.date)
                      .utc(9)
                      .format('YYYY.MM.DD HH:mm')

                    return val
                  })
                  this.reciveList = list
                  // console.log(this.reciveList)
                }
              } else {
                this.$dialog.show({
                  type: 'alert',
                  title: result.data.message
                })
              }
            })
            .catch(err => console.log(err))
            .finally(() => {
              const body = this.$el.querySelector('#contentWrapper')
              const eh = body.scrollHeight + 100 // 처음 불러올때만 맨 아래로 이동, 갱신될때는 최상단이 아닐 경우 갱신됨
              const isScroll = body.scrollHeight < eh
              console.log(body)
              console.log(eh)
              console.log(body.scrollHeight)
              if (isScroll) {
                body.scrollTop = body.scrollHeight
              }
            })
        } catch (err) {
          console.log(err)
          alert('에러발생')
          return
        }
      },
      async send() {
        console.log('Send message:' + this.stompClient.send)
        if (this.stompClient && this.stompClient.connected) {
          let date = moment(new Date()).format('YYYY.MM.DD HH:mm')
          const msg = {
            writer: this.userName,
            message: this.message,
            token: this.$store.getters.token,
            roomId: this.$route.query.idx,
            date: date,
            recvUserIdx: this.recvIdx
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
                type: 'name'
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
      },
      async getJobList() {
        if (this.$store.getters.token === '') {
          this.$dialog.show({
            type: 'alert',
            title: '로그인을 해주세요'
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
          await api
            .get(`job/activeList`, config)
            .then(result => {
              console.log(result)
              if (result.data.success) {
                let res = result.data.data
                res = res.map(v => {
                  if (
                    v.work_period_start !== undefined &&
                    v.work_period_start !== null
                  ) {
                    v.work_period_start = `${moment(v.work_period_start).format(
                      'YYYY.MM.DD'
                    )}(${moment(v.work_period_start)
                      .locale('ko')
                      .format('dd')})`
                  }
                  if (
                    v.work_period_end !== undefined &&
                    v.work_period_end !== null
                  ) {
                    v.work_period_end = `${moment(v.work_period_end).format(
                      'YYYY.MM.DD'
                    )}(${moment(v.work_period_end).locale('ko').format('dd')})`
                  }
                  return v
                })
                this.jobList = res
              } else {
                this.$dialog.show({
                  type: 'alert',
                  title: '에러 발생'
                })
                return
              }
            })
            .catch(err => console.log(err))
        } catch (err) {
          console.log(err)
        }
      },
      async showJobs() {
        try {
          this.$dialog
            .show({
              template: jobDialog,
              type: 'confirm',
              data: {
                confirmText: '제안',
                list: this.jobList,
                selectedItem: this.selectedItem
              }
            })
            .$on('dialog:confirm', v => {
              this.suggest(v)
              // this.selectedItem = ''
            })
        } catch (e) {
          console.log(e)
        }
        // try {
        //   const config = {
        //     headers: {
        //       'Content-Type': 'application/json; charset = utf-8',
        //       'Access-Control-Allow-Origin': '*',
        //       authorization: `Bearer ${this.$store.getters.token}`
        //     }
        //   }
        //   await api
        //     .get(`job/activeList`, config)
        //     .then(result => {
        //       console.log(result)
        //       if (result.data.success) {
        //         let res = result.data.data
        //         res = res.map(v => {
        //           if (
        //             v.work_period_start !== undefined &&
        //             v.work_period_start !== null
        //           ) {
        //             v.work_period_start = `${moment(v.work_period_start).format(
        //               'YYYY.MM.DD'
        //             )}(${moment(v.work_period_start)
        //               .locale('ko')
        //               .format('dd')})`
        //           }
        //           if (
        //             v.work_period_end !== undefined &&
        //             v.work_period_end !== null
        //           ) {
        //             v.work_period_end = `${moment(v.work_period_end).format(
        //               'YYYY.MM.DD'
        //             )}(${moment(v.work_period_end).locale('ko').format('dd')})`
        //           }
        //           return v
        //         })
        //         this.jobList = res

        //         this.$dialog
        //           .show({
        //             template: jobDialog,
        //             type: 'confirm',
        //             data: {
        //               confirmText: '제안',
        //               list: this.jobList,
        //               selectedItem: this.selectedItem
        //             }
        //           })
        //           // .$on('dialog:confirm', () => {
        //           //   this.suggest()
        //           //   this.selectedItem = ''
        //           //   resolve()
        //           // })
        //           .$on('dialog:confirm', () => {
        //             console.log('asd')
        //           })
        //       } else {
        //         this.$dialog.show({
        //           type: 'alert',
        //           title: '에러 발생'
        //         })
        //         return
        //       }
        //     })
        //     .catch(err => console.log(err))
        // } catch (err) {
        //   console.log(err)
        // }
      },
      async suggest(suggestIdx) {
        try {
          const config = {
            headers: {
              'Content-Type': 'application/json; charset = utf-8',
              'Access-Control-Allow-Origin': '*',
              authorization: `Bearer ${this.$store.getters.token}`
            }
          }
          const body = {
            workIdx: suggestIdx,
            userIdx: this.recvIdx
          }
          api
            .post(`job/suggest`, body, config)
            .then(result => {
              if (result.data.success) {
                this.$dialog.show({
                  type: 'alert',
                  title: '제안 성공'
                })
              } else {
                this.$dialog.show({
                  type: 'alert',
                  title: '에러 발생'
                })
                return
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
