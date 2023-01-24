<template>
  <div class="main" ref="container">
    <h2 hidden>노무</h2>
    <div class="labor">
      <div style="justify-content: right" class="sort_area">
        <!-- <div class="sort_choice">
          <select-box text="선택" :list="categories" @change="changeCategory" />
        </div> -->
        <a @click="showCheckList">
          <button type="button" class="btn-login">체크리스트 등록</button>
        </a>
      </div>
      <div class="list">
        <ul class="">
          <li
            class="card"
            v-for="(item, key) in laborList"
            :key="key"
            @click="goDetail(item.labor_idx)"
          >
            <div class="image">
              <div
                class="image_inner"
                v-bind:style="{
                  backgroundColor:
                    item.labor_image === undefined || item.labor_image === ''
                      ? '#e3e5e5'
                      : '#FFF'
                }"
              >
                <img
                  :src="
                    item.labor_image === undefined || item.labor_image === null
                      ? null
                      : 'https://momobuild.s3.ap-northeast-2.amazonaws.com/' +
                        item.labor_image
                  "
                  alt=""
                />
              </div>
            </div>
            <dl>
              <dt
                class="title"
                stlye="font-size: 16pt; color: #2D3238; font-weight: bold"
              >
                {{ item.labor_name }}
              </dt>
              <div class="career">{{ item.labor_career }}년차</div>
              <dd class="intro">
                {{ item.labor_description }}
              </dd>
              <!-- <dd class="date">{{ item.timestamp_create }}</dd> -->
            </dl>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
  // eslint-disable-next-line no-unused-vars
  import laborCheckList from '@/components/popup/laborCheckList'
  import api from '@/api/api'
  import moment from 'moment'

  export default {
    components: {
      // selectBox: () => import('@/components/common/selectbox')
    },
    data() {
      return {
        category: 'all',
        categories: [
          {
            value: 'all',
            text: '전체'
          },
          {
            value: 1,
            text: '직장인갑질'
          },
          {
            value: 2,
            text: '산재확인'
          },
          {
            value: 3,
            text: '임금체불'
          }
        ],
        selected: {},
        laborList: [],
        typeList: [],
        pageNo: 1,
        scrollPosition: 0,
        isLoading: true
      }
    },
    created() {
      window.addEventListener('scroll', this.handleScroll)
    },
    destroyed() {
      this.dialog.onClose()
      window.removeEventListener('scroll', this.handleScroll)
    },
    mounted() {
      // this.dialog.$on('$dialog:alert')
      this.getTypeList()
      this.getLaborList('all')
    },
    beforeDestroy() {
      this.$dialog.close()
      window.removeEventListener('scroll', this.handleScroll)
    },
    methods: {
      changeCategory(v) {
        this.selected = v
        this.category = v.value
        this.laborList = []
        this.pageNo = 1
        this.isLoading = true
        this.getLaborList(v.value)
      },
      async getTypeList() {
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
            .get(`labor/open/typeList`, config)
            .then(result => {
              console.log(result)
              if (result.data.success) {
                if (
                  result.data.data.result !== undefined &&
                  result.data.data.result !== null
                ) {
                  let list = result.data.data.result
                  list = list.map(value => {
                    value.value = value.type_idx
                    value.title = value.type_name
                    return value
                  })
                  this.typeList = list
                  // this.$dialog
                  //   .show({
                  //     template: laborCheckList,
                  //     data: list
                  //   })
                  //   .$on('dialog:confirm', v => {
                  //     this.isLoading = true
                  //     this.getLaborList(String(v).toString())
                  //   })
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
      async getLaborList(type) {
        console.log(type)
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
            .get(`labor/open/list/${type}/${this.pageNo}`, config) // category에 따른 검색처리도 구현해야함 !! ... JY
            .then(result => {
              console.log(result)
              if (result.data.success) {
                if (
                  result.data.data.laborList !== undefined &&
                  result.data.data.laborList !== null
                ) {
                  let list = result.data.data.laborList
                  list = list.map(value => {
                    value.timestamp_create = moment(value.timestamp_create)
                      .utc()
                      .format('YYYY.MM.DD')
                    return value
                  })
                  this.laborList = this.laborList.concat(list)
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
      goDetail(v) {
        //구인카드 클릭시
        this.$router.push({ name: 'LaborDetail', query: { laborIdx: v } })

        //구직카드 클릭시 통화하기 팝업출력
        // this.$dialog
        //   .show({
        //     type: 'confirm', //confirm or alert
        //     title: '신청하시겠습니까?',
        //     message: '신청을 하시면 등록된 전화번호로 바로 전화 연결이 됩니다.',
        //     data: {
        //       confirmText: '신청'
        //     }
        //   })
        //   .$on('dialog:confirm', () => {
        //     console.log('카드에 등록된 전화번호로 바로 전화연결')
        //     window.location.href = 'tel://' + v
        //   })
      },
      handleScroll() {
        // console.log(this.$refs.container.scrollHeight)
        // console.log(window.scrollY + window.innerHeight)
        if (
          window.scrollY + window.innerHeight >
          this.$refs.container.scrollHeight
        ) {
          if (this.laborList.length === (this.pageNo - 1) * 10) {
            if (this.isLoading === false) {
              this.isLoading = true
              this.getLaborList(this.category)
            }
          }
        }
      },
      showCheckList() {
        this.$dialog
          .show({
            template: laborCheckList,
            data: this.typeList
          })
          .$on('dialog:confirm', v => {
            console.log(v)
            this.isLoading = true
            this.laborList = []
            this.pageNo = 1
            this.category = String(v).toString()
            this.getLaborList(String(v).toString())
          })
      }
    }
  }
</script>
