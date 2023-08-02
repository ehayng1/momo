<template>
  <div class="main" ref="container">
    <h2 hidden>이슈</h2>
    <div class="issue">
      <!-- <div style="padding: 20px 5px" class="sort_area">
        <div class="sort_choice">
          <select-box
            text="최신순"
            :list="categories"
            @change="changeCategory"
          />
        </div>
      </div> -->
      <div class="list" id="main">
        <ul v-if="list.length" class="">
          <li
            class="card"
            v-for="(item, key) in list"
            :key="key"
            @click="goPage(item.issue_idx)"
          >
            <dl>
              <div
                style="
                  display: flex;
                  flex-direction: row;
                  align-items: center;
                  margin-bottom: 2rem;
                "
              >
                <dd style="margin-right: 1rem">
                  <profile />
                </dd>
                <div
                  style="
                    display: flex;
                    flex-direction: column;
                    justify-content: center;
                  "
                >
                  <dd class="id">
                    {{ item.nickname ? item.nickname : item.code_blockchain }}
                  </dd>
                  <dd class="date" style="">
                    <!-- <span>{{ item.timestamp_create }}</span> -->
                    {{ item.timestamp_create }}
                  </dd>
                </div>
              </div>
              <dd class="title">{{ item.issue_title }}</dd>
              <dd class="content">
                {{ item.issue_content }}
              </dd>

              <div class="space" />

              <dd class="date">
                <div class="bottom_left">
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
                  <like />
                  <!-- <div></div> -->
                  좋아요 {{ (Math.random() * 20).toFixed(0) }}
                  <look style="margin-left: 1rem" /> 조회
                  {{ item.view_count }}
                </div>
                <div class="bottom_right">
                  <comment />
                  댓글

                  {{
                    item.reply_count === undefined || item.reply_count === null
                      ? 0
                      : item.reply_count
                  }}
                </div>
              </dd>
            </dl>
          </li>
        </ul>
        <ul v-else class="">
          <li class="empty">
            <dl>
              <dd class="icon">
                <subtitlesOff />
              </dd>
              <dd class="content">게시글이 존재하지 않습니다.</dd>
            </dl>
          </li>
        </ul>
        <div class="addButton" @click="checkRegist">
          <fa :icon="['fa', 'plus']" style="width: 100%; height: 45px" />
        </div>
        <!-- <a @click="checkRegist">
          <button type="button" id="write" class="btn-login">글쓰기</button>
        </a> -->
      </div>
    </div>
  </div>
</template>

<script>
  import subtitlesOff from '@/static/images/common/subtitles_off.svg'
  import profile from '@/static/images/common/profile.svg'
  import look from '@/static/images/common/view.svg'
  import like from '@/static/images/common/like.svg'
  import comment from '@/static/images/common/comment.svg'
  import api from '@/api/api'
  import moment from 'moment'

  export default {
    components: {
      // selectBox: () => import('@/components/common/selectbox'),
      subtitlesOff,
      like,
      look,
      comment,
      profile
    },
    data() {
      return {
        category: 'new',
        categories: [
          {
            value: 0,
            text: '최신순',
            category: 'new'
          },
          {
            value: 1,
            text: '댓글순',
            category: 'reply'
          },
          {
            value: 2,
            text: '조회순',
            category: 'view'
          }
        ],
        selected: {},
        list: [],
        pageNo: 1,
        scrollPosition: 0,
        isLoading: true
      }
    },
    mounted() {
      this.getIssueList()
    },
    created() {
      window.addEventListener('scroll', this.handleScroll)
    },
    destroyed() {
      window.removeEventListener('scroll', this.handleScroll)
    },
    methods: {
      changeCategory(v) {
        console.log(v)
        this.selected = v

        this.category = v.category
        this.list = []
        this.pageNo = 1
        this.isLoading = true
        this.getIssueList()
      },
      goPage(v) {
        this.$router.push({ name: 'IssueDetail', query: { issueIdx: v } })
      },
      checkRegist() {
        if (this.$store.getters.token === '') {
          this.$dialog.show({
            type: 'alert',
            title: '로그인 후 이용해주세요.'
          })
          return false
        } else {
          this.$router.push({ name: 'IssueRegistration' })
        }
      },
      async getIssueList() {
        if (this.isLoading === false) {
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
            .get(`board/open/board/${this.category}/${this.pageNo}`, config)
            .then(result => {
              console.log(result)
              if (result.data.success) {
                if (
                  result.data.data.result !== undefined &&
                  result.data.data.result !== null
                ) {
                  let list = result.data.data.result
                  list = list.map(value => {
                    if (String(value.issue_content).length > 98) {
                      value.issue_content =
                        String(value.issue_content).substring(0, 98) + '...'
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
                        value.timestamp_create = parseInt(diff / 60) + '시간전'
                      } else if (diff < 60 * 24 * 7) {
                        value.timestamp_create = parseInt(diff / 1440) + '일전'
                      } else if (diff < 60 * 24 * 7 * 4) {
                        value.timestamp_create =
                          parseInt(diff / 10080) + '주일전'
                      } else if (diff < 60 * 24 * 7 * 4 * 12) {
                        value.timestamp_create = parseInt(diff / 40320) + '달전'
                      }
                    } else {
                      value.timestamp_create = moment(
                        value.timestamp_create
                      ).format('YYYY.MM.DD')
                    }
                    return value
                  })
                  this.list = this.list.concat(list)
                  this.pageNo = this.pageNo + 1
                  this.isLoading = false
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
        if (
          window.scrollY + window.innerHeight >
          this.$refs.container.scrollHeight
        ) {
          if (this.list.length === (this.pageNo - 1) * 10) {
            if (this.isLoading === false) {
              this.isLoading = true
              this.getIssueList()
            }
          }
        }
      }
    }
  }
</script>
