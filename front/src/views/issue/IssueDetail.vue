<template>
  <div class="issue">
    <back style="padding: 20px" />
    <div class="detail">
      <div class="header">
        <div class="info">
          <div class="img_back">
            <img
              v-if="profileImg !== undefined"
              :src="
                'https://momobuild.s3.ap-northeast-2.amazonaws.com/' +
                profileImg
              "
            />
            <user v-else class="img" />
          </div>
          <div class="info_content">
            <div class="top">
              <span class="id">
                {{ id }}
              </span>
            </div>

            <div class="bottom">
              <span class="date">
                {{ date }}
              </span>

              <button
                v-if="modify === 'true'"
                @click="deleteIssue"
                type="button"
                class="btn_small"
              >
                삭제
              </button>
            </div>
          </div>
          <div>
            <more />
          </div>
          <!-- <div class="upload_btn">
            <button
              type="button"
              class="btn_small"
              @click="report(idx, 'board')"
            >
              신고
            </button>
            &nbsp;
            <button type="button" class="btn_small" @click="share">공유</button>
          </div> -->
        </div>
        <h1 class="title">{{ title }}</h1>
      </div>
      <div class="content">
        <div v-if="this.additional">
          <div v-if="this.additionalDivision === 'image'">
            <img
              :src="
                'https://momobuild.s3.ap-northeast-2.amazonaws.com/' +
                additional
              "
              alt=""
            />
          </div>
          <div v-else-if="this.additionalDivision === 'video'">
            <video-player
              class="video-player-box"
              ref="videoPlayer"
              :options="additionalPlayerOptions"
              :playsinline="true"
              customEventName="customstatechangedeventname"
              @play="onPlayerPlay($event)"
              @pause="onPlayerPause($event)"
              @ended="onPlayerEnded($event)"
              @waiting="onPlayerWaiting($event)"
              @statechanged="playerStateChanged($event)"
              @ready="playerReadied"
            >
              <!-- <div class="video-js" style="background-color: blue"></div> -->
            </video-player>
          </div>
        </div>
        <div v-for="(item, key) in this.files" :key="key">
          <div v-if="item.file_type === 0">
            <img
              :src="
                'https://momobuild.s3.ap-northeast-2.amazonaws.com/' +
                item.file_path
              "
              alt=""
            />
          </div>
          <div v-else-if="item.file_type === 1">
            <video-player
              class="video-js"
              ref="videoPlayer"
              :options="{
                // videojs options
                controlBar: {
                  timeDivider: true, //
                  durationDisplay: true, //
                  remainingTimeDisplay: true, //
                  fullscreenToggle: true //
                },
                muted: true,
                language: 'en',
                playbackRates: [0.7, 1.0, 1.5, 2.0],
                sources: [
                  {
                    type: 'video/mp4',
                    src:
                      'https://momobuild.s3.ap-northeast-2.amazonaws.com/' +
                      item.file_path
                  }
                ]
                // poster: '/static/images/author.jpg'
              }"
              :playsinline="true"
              customEventName="customstatechangedeventname"
              @play="onPlayerPlay($event)"
              @pause="onPlayerPause($event)"
              @ended="onPlayerEnded($event)"
              @waiting="onPlayerWaiting($event)"
              @statechanged="playerStateChanged($event)"
              @ready="playerReadied"
            >
            </video-player>
          </div>
        </div>
        <div v-if="this.image">
          <img
            :src="'https://momobuild.s3.ap-northeast-2.amazonaws.com/' + image"
            alt=""
          />
        </div>
        <div v-else-if="this.video">
          <video-player
            class="video-js"
            ref="videoPlayer"
            :options="playerOptions"
            :playsinline="true"
            customEventName="customstatechangedeventname"
            @play="onPlayerPlay($event)"
            @pause="onPlayerPause($event)"
            @ended="onPlayerEnded($event)"
            @waiting="onPlayerWaiting($event)"
            @statechanged="playerStateChanged($event)"
            @ready="playerReadied"
          >
          </video-player>
        </div>
        {{ content }}
      </div>
      <div class="date">
        <div class="bottom_left" style="color: #868e97; margin-bottom: 2rem">
          <!-- image preview deactivated -->
          <!-- <fa
                    v-if="item.check_image === 0"
                    :icon="['fa', 'image']"
                    style="margin-right: 5px"
                  />
                  <fa
                    v-if="item.check_video === 0"
                    :icon="['fa', 'camera']"
                    style="margin-right: 5px"
                  /> -->
          <like style="margin-left: 1.5rem" />
          <!-- <div></div> -->
          좋아요 {{ (Math.random() * 20).toFixed(0) }}
          <look style="margin-left: 1rem" /> 조회
          {{ count }}
          <comment style="margin-left: 1rem" />
          댓글
          {{ replyCount === undefined || replyCount === null ? 0 : replyCount }}
        </div>
        <!-- <div class="bottom_right"> -->

        <!-- </div> -->
      </div>
      <!-- <span class="count"> 조회수 {{ count }} </span>
      <span class="reply_count"> 댓글 {{ replyCount }} </span> -->

      <div style="height: auto">
        <div v-if="comment.length" class="comment_wrap">
          <div class="comment_div">댓글</div>

          <div class="comment" v-for="(item, key) in comment" :key="key">
            <div class="id">
              <span>
                {{ item.nickname }}
              </span>
              <div>
                <div v-if="item.modify">
                  <button
                    v-if="item.modifyReply === false"
                    @click="item.modifyReply = true"
                    type="button"
                    class="btn_small"
                  >
                    수정
                  </button>
                  <button
                    v-else
                    @click="updateReply(item)"
                    type="button"
                    class="btn_small"
                  >
                    적용
                  </button>
                  &nbsp;
                  <button
                    @click="deleteReply(item.reply_idx)"
                    type="button"
                    class="btn_small"
                  >
                    삭제
                  </button>
                </div>
                <button
                  v-if="!item.modify"
                  type="button"
                  class="btn_small"
                  @click="report(item.reply_idx, 'comment')"
                >
                  신고
                </button>
              </div>
            </div>
            <div v-if="item.modifyReply === false" class="reply_content">
              {{ item.reply_content }}
            </div>
            <div v-else>
              <text-input
                type="text"
                placeholder="댓글을 입력해주세요."
                v-model="item.reply_content"
                required
              />
            </div>
            <div class="date">{{ item.timestamp_create }}</div>
          </div>
        </div>
      </div>
    </div>
    <footer class="footer" ref="footer">
      <form id="replyForm" class="form">
        <div class="top">
          <text-input
            type="text"
            placeholder="    댓글을 입력해주세요."
            v-model="reply"
            required
          />
        </div>
        <div class="bottom">
          <button
            style="width: 100%; margin-right: 1rem; margin-bottom: 1rem"
            type="button"
            @click="onSubmit"
            class="btn-login"
          >
            등록
          </button>
        </div>
      </form>
    </footer>
  </div>
</template>
<script>
  import user from '@/static/images/common/user_small.svg'
  import textInput from '@/components/common/inputArea'
  import api from '@/api/api'
  import moment from 'moment'
  import back from '@/components/layout/back'
  import look from '@/static/images/common/view.svg'
  import like from '@/static/images/common/like.svg'
  import comment from '@/static/images/common/comment.svg'
  import more from '@/static/images/common/more.svg'
  export default {
    components: {
      // eslint-disable-next-line vue/no-unused-components
      user,
      // eslint-disable-next-line vue/no-unused-components
      textInput,
      // eslint-disable-next-line vue/no-unused-components
      back,
      like,
      look,
      comment,
      more
    },
    data() {
      return {
        idx: this.$route.query.issueIdx,
        userIdx: '',
        title: '',
        id: '',
        content: ``,
        date: '',
        count: '',
        replyCount: '',
        comment: [],
        image: '',
        video: '',
        reply: '',
        modifyReply: '',
        modify: 'false',
        playerOptions: {
          // videojs options
          muted: true,
          language: 'en',
          playbackRates: [0.7, 1.0, 1.5, 2.0],
          sources: [
            {
              type: 'video/mp4',
              src: 'https://cdn.theguardian.tv/webM/2015/07/20/150716YesMen_synd_768k_vp8.webm'
            }
          ]
          // poster: '/static/images/author.jpg'
        },
        additional: '',

        additionalDivision: '',
        additionalPlayerOptions: {
          // videojs options
          controlBar: {
            timeDivider: true, //
            durationDisplay: true, //
            remainingTimeDisplay: true, //
            fullscreenToggle: true //
          },
          muted: true,
          language: 'en',
          playbackRates: [0.7, 1.0, 1.5, 2.0],
          sources: [
            {
              type: 'video/mp4',
              src: 'https://cdn.theguardian.tv/webM/2015/07/20/150716YesMen_synd_768k_vp8.webm'
            }
          ],
          profileImg: undefined // 프로필 이미지
          // poster: '/static/images/author.jpg'
        },

        duration: 0,
        currentTime: 0,
        files: []
      }
    },
    computed: {
      player() {
        return this.$refs.videoPlayer.player
      },
      webShareApiSupported() {
        return navigator.share
      }
    },
    methods: {
      // Player
      // listen event
      onPlayerPlay(player) {
        console.log('!!', player.controlBar)
      },
      onPlayerPause(player) {
        console.log('player pause!', player)
      },
      // ...player event
      // or listen state event
      playerStateChanged(playerCurrentState) {
        console.log('player current update state', playerCurrentState)
      },
      // player is ready
      playerReadied(player) {
        console.log('the player is readied', player)
        // you can use it to do something...
        // player.[methods]
      },
      // getHeight() {
      //   let val = this.$refs.footer.scrollHeight
      // }
      onSubmit() {
        if (this.$store.getters.token === '') {
          this.$dialog.show({
            type: 'alert',
            title: '로그인을 해주세요'
          })
          return false
        } else if (this.reply.trim().length === 0) {
          this.$dialog.show({
            type: 'alert',
            title: '댓글을 입력해주세요'
          })
          return false
        } else {
          this.$dialog
            .show({
              type: 'confirm',
              title: '댓글을 작성하시겠습니까?',
              data: {
                confirmText: '작성'
              }
            })
            .$on('dialog:confirm', this.addReply)
        }
      },
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
              console.log('files: ', result.data.data.files)
              if (result.data.success) {
                if (
                  result.data.data.detail !== undefined &&
                  result.data.data.detail !== null
                ) {
                  const detail = result.data.data.detail
                  console.log('issue detail: ', detail)
                  this.title = detail.issue_title
                  this.userIdx = detail.issue_user_idx
                  this.id = detail.nickname
                    ? detail.nickname
                    : detail.code_blockchain
                  this.count = this.$options.filters.autoCommaNumber(
                    detail.view_count
                  )
                  this.replyCount = this.$options.filters.autoCommaNumber(
                    result.data.data.reply.length
                  )
                  this.profileImg = detail.user_image
                  let timestamp = moment(detail.timestamp_create).format()
                  let now = moment()
                  let diff = now.diff(timestamp, 'minutes')
                  if (diff < 60 * 24 * 7 * 4 * 12) {
                    if (diff < 3) {
                      detail.timestamp_create = '얼마전'
                    } else if (diff < 60) {
                      detail.timestamp_create = diff + '분전'
                    } else if (diff < 60 * 24) {
                      detail.timestamp_create = parseInt(diff / 60) + '시간전'
                    } else if (diff < 60 * 24 * 7) {
                      detail.timestamp_create = parseInt(diff / 1440) + '일전'
                    } else if (diff < 60 * 24 * 7 * 4) {
                      detail.timestamp_create =
                        parseInt(diff / 10080) + '주일전'
                    } else if (diff < 60 * 24 * 7 * 4 * 12) {
                      detail.timestamp_create = parseInt(diff / 40320) + '달전'
                    }
                  } else {
                    detail.timestamp_create = moment(
                      detail.timestamp_create
                    ).format('YYYY.MM.DD')
                  }
                  this.date = detail.timestamp_create
                  // this.date = moment(detail.timestamp_create)
                  //   .utc(9)
                  //   .format('MM.DD HH:mm')

                  this.content = detail.issue_content
                  let replyList = result.data.data.reply
                  replyList = replyList.map(value => {
                    value.modifyReply = false
                    value.timestamp_create = moment(value.timestamp_create)
                      .utc(9)
                      .format('MM.DD HH:mm')
                    return value
                  })
                  this.comment = replyList
                  // 이미지 / 비디오 구분용
                  let flag = false
                  const imageExtension = [
                    '.bmp',
                    '.rle',
                    '.dib',
                    '.jpg',
                    '.jpeg',
                    '.gif',
                    '.png',
                    '.tif',
                    '.tiff',
                    '.raw',
                    '.svg',
                    '.webp'
                  ]
                  if (
                    String(detail.issue_image) !== undefined &&
                    String(detail.issue_image) !== null &&
                    String(detail.issue_image) !== ''
                  ) {
                    const loc = String(detail.issue_image).toLowerCase()
                    imageExtension.map(inner => {
                      if (String(loc).includes(inner)) {
                        flag = true
                      }
                    })
                  }
                  if (
                    detail.issue_additional !== undefined &&
                    detail.issue_additional !== null &&
                    String(detail.issue_additional) !== ''
                  ) {
                    const loc = String(detail.issue_additional).toLowerCase()
                    imageExtension.map(inner => {
                      if (String(loc).includes(inner)) {
                        this.additional = detail.issue_additional
                        this.additionalDivision = 'image'
                      }
                    })
                    if (this.additionalDivision === '') {
                      this.additional = 'true'
                      this.additionalDivision = 'video'
                      this.additionalPlayerOptions.sources[0].src =
                        detail.issue_additional
                    }
                  }
                  if (flag) {
                    this.image = detail.issue_image
                  } else {
                    this.video = detail.issue_image
                    this.playerOptions.sources[0].src =
                      'https://momobuild.s3.ap-northeast-2.amazonaws.com/' +
                      detail.issue_image
                  }
                  this.modify = detail.modify
                  this.files = result.data.data.files
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
      async addReply() {
        try {
          const config = {
            headers: {
              'Content-Type': 'application/json; charset = utf-8',
              'Access-Control-Allow-Origin': '*',
              authorization: `Bearer ${this.$store.getters.token}`
            }
          }
          const body = {
            issueIdx: this.idx,
            replyContent: this.reply
          }
          await api
            .post(`board/reply/write`, body, config)
            .then(result => {
              console.log(result)
              if (result.data.success) {
                // this.$dialog.show({
                //   type: 'alert',
                //   title: '댓글 작성 성공'
                // })
                this.getReplyList()
                this.reply = ''
              } else {
                // 실패
                this.$dialog.show({
                  type: 'alert',
                  title: '댓글 작성 실패'
                })
              }
            })
            .catch(err => console.log(err))
        } catch (err) {
          console.log(err)
        }
      },
      async deleteIssue() {
        await this.$dialog
          .show({
            type: 'confirm',
            title: '삭제하시겠습니까?',
            data: {
              confirmText: '삭제'
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
                issueIdx: this.idx
              }
              api
                .put(`board/board/${this.idx}`, body, config)
                .then(result => {
                  console.log(result)
                  if (result.data.success) {
                    this.$dialog
                      .show({
                        type: 'alert',
                        title: '삭제했습니다.'
                      })
                      .$on('dialog:confirm', () => {
                        this.$router.push('/issue')
                      })
                  } else {
                    this.$dialog.show({
                      type: 'alert',
                      title: '삭제 실패'
                    })
                  }
                })
                .catch(err => console.log(err))
            } catch (err) {
              console.log(err)
            }
          })
      },
      async getReplyList() {
        try {
          const config = {
            headers: {
              'Content-Type': 'application/json; charset = utf-8',
              'Access-Control-Allow-Origin': '*',
              authorization: `Bearer ${this.$store.getters.token}`
            }
          }
          await api
            .get(`board/open/board/detail/reply/${this.idx}`, config)
            .then(result => {
              console.log(result)
              if (result.data.data !== undefined && result.data.data !== null) {
                let replyList = result.data.data.result
                replyList = replyList.map(value => {
                  value.modifyReply = false
                  value.timestamp_create = moment(value.timestamp_create)
                    .utc(9)
                    .format('MM.DD HH:mm')
                  return value
                })
                this.comment = replyList
              }
            })
            .catch(err => console.log(err))
        } catch (err) {
          console.log(err)
        }
      },
      async updateReply(item) {
        await this.$dialog
          .show({
            type: 'confirm',
            title: '댓글을 수정하시겠습니까?',
            data: {
              confirmText: '수정'
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
                idx: item.reply_idx,
                content: item.reply_content
              }
              api
                .post(`board/reply/update`, body, config)
                .then(result => {
                  console.log(result)
                  if (result.data.success) {
                    item.modifyReply = false
                    this.$dialog.show({
                      type: 'alert',
                      title: '수정했습니다.'
                    })
                  } else {
                    this.$dialog.show({
                      type: 'alert',
                      title: '수정 실패'
                    })
                  }
                })
                .catch(err => console.log(err))
            } catch (err) {
              console.log(err)
            }
          })
      },
      async deleteReply(replyIdx) {
        await this.$dialog
          .show({
            type: 'confirm',
            title: '댓글을 삭제하시겠습니까?',
            data: {
              confirmText: '삭제'
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
                issue_idx: this.idx,
                reply_idx: replyIdx
              }
              api
                .post(`board/reply/delete`, body, config)
                .then(result => {
                  console.log(result)
                  if (result.data.success) {
                    this.$dialog
                      .show({
                        type: 'alert',
                        title: '삭제했습니다.'
                      })
                      .$on('dialog:confirm', () => {
                        this.comment = this.comment.filter(
                          val => val.reply_idx !== replyIdx
                        )
                        this.replyCount = this.comment.length
                      })
                  } else {
                    this.$dialog.show({
                      type: 'alert',
                      title: '삭제 실패'
                    })
                  }
                })
                .catch(err => console.log(err))
            } catch (err) {
              console.log(err)
            }
          })
      },
      share() {
        if (navigator.canShare) {
          console.log('yesyesyes')
          navigator.share({
            // title: this.content,
            title:
              this.content.length > 10
                ? this.content.substring(0, 10) + '...'
                : this.content,
            url: location.href
          })
        } else {
          console.log('no')
          this.$copyText(location.href).then(
            () => {
              this.$dialog.show({
                type: 'alert',
                title: '클립보드로 복사되었습니다.'
              })
            },
            () => {
              this.$dialog.show({
                type: 'alert',
                title: '복사하지 못했습니다.'
              })
            }
          )
        }
      },
      async report(idx, category) {
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
            title: '게시글을 신고하시겠습니까?',
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
                reportedIdx: idx,
                category: category
              }
              api
                .post(`report/insert`, body, config)
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
    },
    mounted() {
      this.getIssueDetail()
    }
  }
</script>
