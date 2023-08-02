<template>
  <section class="page applicaion" ref="container" style="padding: 0">
    <!-- <div> -->
    <back />
    <div class="info">
      <h1 style="text-align: center" class="title">작성내역</h1>
    </div>

    <tab :list="tabList" :current="tabCurrent" @input="onChangeTab">
      <!-- <template v-slot:btn>
        <div class="btn_wrap">
          <div v-for="(item, key) in btnList" :key="key">
            <button type="button" @click="item.onClick">
              {{ item.name }}
            </button>
          </div>
        </div>
      </template> -->
    </tab>

    <div class="list">
      <ul v-if="tabCurrent !== 3 && list.length">
        <li
          :class="[{ back: item.work_status === 1 }]"
          class="card"
          v-for="(item, key) in list"
          :key="key"
        >
          <!-- <div v-if="item.own === 0" class="input_box">
            <input
              type="radio"
              :id="item.work_idx"
              name="aa"
              :value="item.work_idx"
              hidden
              v-model="select"
              :ref="item.work_idx"
            />
            <label :for="item.work_idx"></label>
          </div> -->
          <dl
            @click="goPage(item.work_idx, tabCurrent)"
            style="padding-left: 16px; padding-right: 16px; padding-top: 16px"
          >
            <dt class="title">
              <img
                v-if="item.company_logo !== undefined"
                :src="item.company_logo"
                alt=""
              />
              <p style="font-size: 18px">{{ item.work_title }}</p>
            </dt>
            <!-- <dd class="phone">{{ item.work_user_phone }}</dd> -->
            <dd style="color= #868E97" class="date">
              <img
                :src="require('@/static/images/common/worklocation.png')"
                alt=""
                style="margin-right: 5px; display: inline-block"
              />
              {{ item.work_location }}
            </dd>
            <dd class="date">
              <img
                :src="require('@/static/images/common/workperiod.png')"
                alt=""
                style="margin-right: 5px"
              />
              {{ item.work_period_start }} ~
              {{ item.work_period_end.substring(5) }}
              <!-- <span class="amount">개인</span> -->
              <span v-if="item.company_total" class="career">
                종합 {{ item.company_total }}
              </span>
            </dd>
            <dd
              style="display: flex; padding-bottom: 15px; padding-top: 15px; color=#2D3238; font-size: 16px; font-weight: bold;"
            >
              <div style="flex: 1">{{ item.work_payment }} 원/일</div>
              <div
                style="
                  align-self: flex-end;
                  color: #868e97;
                  font-size: 10px;
                  font-weight: 100;
                "
              >
                <div style="display: flex; align-items: center">
                  <div class="look">
                    <look style="margin-right: 5px" />{{ item.view_count }}
                  </div>
                  <div class="heart">
                    <heart style="margin-left: 5px; margin-right: 5px" />{{
                      item.view_count
                    }}
                  </div>
                </div>
              </div>
            </dd>
          </dl>
          <div :style="border_style"></div>
          <div
            style="
              display: flex;
              flex-direction: row;
              font-size: 16px;
              align-items: center;
              justify-content: space-around;
              text-align: center;
            "
          >
            <div style="flex-grow: 1">구인종료로 변경</div>
            <div
              style="flex-grow: 1; border-left: 1px solid #d3d9e0"
              v-if="item.own === 0"
              class="upload_btn"
              @click.stop="uploadBtn(item.work_idx)"
            >
              다시올리기
            </div>
          </div>
          <div :style="border_style1"></div>
        </li>
      </ul>
      <ul v-else-if="tabCurrent === 3 && list.length">
        <li
          :class="[{ back: item.construction_status === 1 }]"
          class="card"
          v-for="(item, key) in list"
          :key="key"
        >
          <div v-if="item.own === 0" class="input_box">
            <input
              type="radio"
              :id="item.construction_idx"
              name="aa"
              :value="item.construction_idx"
              hidden
              v-model="select"
              :ref="item.construction_idx"
            />
            <label :for="item.construction_idx"></label>
          </div>
          <dl @click="goPage(item.construction_idx, tabCurrent)">
            <dt class="title2">
              <span class="name">{{ item.construction_title }}</span>
              <!-- <span class="year">
                연식 {{ item.construction_divide_second }}년
              </span> -->
            </dt>
            <dd class="phone">{{ item.construction_user_phone }}</dd>
            <dd class="date">
              <!-- {{ item.construction_period_start }} ~
              {{ item.construction_period_end }} -->
              연식 {{ item.construction_divide_second }}년
              <span class="amount">
                {{ item.construction_location }}
                {{ item.construction_location_detail }}
              </span>
            </dd>
          </dl>
          <div
            v-if="item.own === 0"
            class="upload_btn"
            @click.stop="uploadBtn(item.construction_idx)"
          >
            <span>다시올리기</span>
          </div>
        </li>
      </ul>
      <ul v-else>
        <li class="empty">
          <dl>
            <dd class="icon">
              <subtitlesOff />
            </dd>
            <dd class="content">목록이 존재하지 않습니다.</dd>
          </dl>
        </li>
      </ul>
    </div>
  </section>
  <!-- </div> -->
</template>

<script>
  import subtitlesOff from '@/static/images/common/subtitles_off.svg'
  import tab from '@/components/common/tab'
  import api from '@/api/api'
  import moment from 'moment'
  import back from '@/components/layout/back'
  import look from '@/static/images/common/view.svg'
  import heart from '@/static/images/common/heart.svg'

  export default {
    components: {
      tab,
      look,
      heart,
      subtitlesOff,
      // eslint-disable-next-line vue/no-unused-components
      back
    },
    data() {
      return {
        border_style:
          'width: 100vw; height: 0.15rem; background-color: #eef1f5;',
        border_style1:
          'width: 100vw; height: 0.6rem; background-color: #eef1f5;',
        select: 0,
        tabCurrent: 1,
        tabList: [
          {
            id: 1,
            label: '구인',
            btns: [
              { id: 1, name: '활성', onClick: () => this.onButton(0) },
              { id: 2, name: '비활성', onClick: () => this.onButton(1) }
            ]
          },
          {
            id: 2,
            label: '구직',
            // btns: [{ id: 1, name: '삭제', onClick: () => this.onButton(3) }]
            btns: [
              { id: 1, name: '활성', onClick: () => this.onButton(0) },
              { id: 2, name: '비활성', onClick: () => this.onButton(1) }
            ]
          },
          {
            id: 3,
            label: '장비',
            btns: [
              { id: 1, name: '활성', onClick: () => this.onButton(0) },
              { id: 2, name: '비활성', onClick: () => this.onButton(1) }
            ]
          }
        ],
        btnList: [
          { id: 1, name: '활성', onClick: () => this.onButton(0) },
          { id: 2, name: '비활성', onClick: () => this.onButton(1) }
        ],
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
      this.dialog.onClose()
      window.removeEventListener('scroll', this.handleScroll)
    },
    beforeDestroy() {
      window.removeEventListener('scroll', this.handleScroll)
    },
    methods: {
      goPage(idx, current) {
        if (Number(current) === 1) {
          this.$router.push({
            name: 'Share',
            params: { idx: idx, type: 'work' }
          })
        } else if (current === 3) {
          this.$router.push({
            name: 'EquipShare',
            params: { idx: idx, type: 'equip' }
          })
        }
      },
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
            .get(
              `mypage/myRequest/${this.tabCurrent - 1}/${this.pageNo}`,
              config
            )
            .then(result => {
              console.log(result)
              if (result.data.success) {
                if (
                  result.data.data !== undefined &&
                  result.data.data !== null
                ) {
                  let list = []
                  list = result.data.data.result
                  if (this.tabCurrent !== 3) {
                    list = list.map(value => {
                      value.work_period_start = moment(
                        value.work_period_start
                      ).format('YYYY.MM.DD')
                      value.work_period_end = moment(
                        value.work_period_end
                      ).format('YYYY.MM.DD')
                      if (String(value.company_logo).slice(0, 4) === 'file') {
                        value.company_logo =
                          'https://momobuild.s3.ap-northeast-2.amazonaws.com/' +
                          value.company_logo
                      }
                      return value
                    })
                  } else {
                    list.map(value => {
                      value.construction_period_start = moment(
                        value.construction_period_start
                      ).format('YYYY.MM.DD')
                      value.construction_period_end = moment(
                        value.construction_period_end
                      ).format('YYYY.MM.DD')
                      return value
                    })
                  }
                  this.list = this.list.concat(list)
                  this.pageNo += 1
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
      onChangeTab(id) {
        console.log(
          '탭 바뀌면 api 다시 호출. 값이 없으면 값이 없는 페이지 필요. '
        )
        this.tabCurrent = id
        this.btnList = this.tabList[id - 1].btns
        this.select = 0
        this.pageNo = 1
        this.list = []
        this.getList()
      },
      async onButton(val) {
        console.log(val)
        if (this.select === 0) {
          this.$dialog.show({
            type: 'alert',
            title: '카드를 선택해주세요.'
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
            const body = {
              type: this.tabCurrent - 1,
              action: val,
              idx: this.select
            }
            await api
              .put('mypage/myRequest', body, config)
              .then(result => {
                console.log(result)
                if (result.data.success) {
                  this.$dialog
                    .show({
                      type: 'alert', //confirm or alert
                      title: '수정 성공'
                    })
                    .$on('dialog:confirm', () => {
                      this.select = 0
                      this.pageNo = 1
                      this.list = []
                      this.getList()
                    })
                } else {
                  this.$dialog.show({
                    type: 'alert', //confirm or alert
                    title: '수정 실패'
                  })
                }
              })
              .catch(err => console.log(err))
          } catch (err) {
            console.log(err)
          }
        }
      },
      async uploadBtn(idx) {
        let item
        let name
        if (this.tabCurrent === 3) {
          name = 'EquipRegist'
          this.list.map(val => {
            if (val.construction_idx === idx) {
              item = val
            }
          })
        } else {
          name =
            this.tabCurrent === 1 ? 'OpeningRegistration' : 'JobRegistration'
          this.list.map(val => {
            if (val.work_idx === idx) {
              item = val
            }
          })
        }

        this.$router.push({ name: name, params: { data: item } })

        // try {
        //   const config = {
        //     headers: {
        //       'Content-Type': 'application/json; charset = utf-8',
        //       'Access-Control-Allow-Origin': '*',
        //       // 'Access-Control-Allow-Headers': '*',
        //       // 'Access-Control-Allow-Methods': '*',
        //       authorization: `Bearer ${this.$store.getters.token}`
        //     }
        //   }
        //   const body = {
        //     type: this.tabCurrent - 1,
        //     idx
        //   }
        //   await api
        //     .put('mypage/myRequest/reload', body, config)
        //     .then(result => {
        //       console.log(result)
        //       if (result.data.success) {
        //         this.$dialog
        //           .show({
        //             type: 'alert', //confirm or alert
        //             title: '올리기 성공'
        //           })
        //           .$on('dialog:confirm', () => {
        //             this.select = 0
        //             this.pageNo = 1
        //             this.list = []
        //             this.getList()
        //           })
        //       } else {
        //         this.$dialog.show({
        //           type: 'alert', //confirm or alert
        //           title: '올리기 실패'
        //         })
        //       }
        //     })
        //     .catch(err => console.log(err))
        // } catch (err) {
        //   console.log(err)
        // }
      }
    }
  }
</script>
