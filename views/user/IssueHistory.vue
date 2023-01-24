<template>
  <section class="page work_history">
    <back />
    <div class="info" ref="container">
      <h1 class="title">나의 작성글</h1>
      <div class="issue">
        <div class="list" id="main">
          <ul v-if="list.length" class="">
            <li
              class="card"
              v-for="(item, key) in list"
              :key="key"
              @click="goPage(item.issue_idx)"
            >
              <dl>
                <dd class="title">{{ item.issue_title }}</dd>
                <dd class="content">
                  {{ item.issue_content }}
                </dd>
                <dd class="id">
                  {{ item.nickname ? item.nickname : item.code_blockchain }}
                </dd>
                <div class="space" />
                <div class="date" style="left: 22px">
                  <span>{{ item.timestamp_create }}</span>
                </div>
                <dd class="date">
                  <div class="bottom_left">
                    <fa
                      v-if="item.check_image === 0"
                      :icon="['fa', 'image']"
                      style="margin-right: 5px"
                    />
                    <fa
                      v-if="item.check_video === 0"
                      :icon="['fa', 'camera']"
                      style="margin-right: 5px"
                    />
                    <look />
                    {{ item.view_count }}
                  </div>
                  <div class="bottom_right">
                    <comment />
                    {{
                      item.reply_count === undefined ||
                      item.reply_count === null
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
        </div>
      </div>
    </div>
  </section>
</template>

<script>
  import api from '@/api/api'
  import moment from 'moment'
  import back from '@/components/layout/back'

  export default {
    components: {
      back
    },
    data() {
      return {
        list: [],
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
        try {
          const config = {
            headers: {
              'Content-Type': 'application/json; charset = utf-8',
              'Access-Control-Allow-Origin': '*',
              authorization: `Bearer ${this.$store.getters.token}`
            }
          }
          await api
            .get(`mypage/issueHistory/${this.pageNo}`, config)
            .then(result => {
              console.log(result)
              if (result.data.success) {
                if (
                  result.data.data.result !== undefined &&
                  result.data.data.result !== null
                ) {
                  let list = result.data.data.result
                  list = list.map(value => {
                    if (String(value.issue_content).length > 15) {
                      value.issue_content =
                        String(value.issue_content).substring(0, 15) + '...'
                    }
                    let timestamp = moment(value.timestamp_create).format()
                    let now = moment()
                    let diff = now.diff(timestamp, 'minutes')
                    if (diff < 180) {
                      if (diff < 3) {
                        value.timestamp_create = '얼마전'
                      } else if (diff < 60) {
                        value.timestamp_create = diff + '분전'
                      } else {
                        value.timestamp_create = parseInt(diff / 60) + '시간전'
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
      },
      goPage(v) {
        this.$router.push({ name: 'IssueDetail', query: { issueIdx: v } })
      }
    }
  }
</script>
