<template>
  <div class="main" ref="container">
    <h2 hidden>일자리</h2>

    <div class="human_resources">
      <!-- 05/27 버튼 기능 변경으로 인한 원래기능 주석처리 -->
      <!-- <div class="button_area">
        <a @click="checkRegist('OpeningRegistration')">
          <button type="button" class="btn-login">인력 구인</button>
        </a>
        <div class="space" />
        <a @click="checkRegist('JobRegistration')">
          <button type="button" class="btn-login">인력 구직</button>
        </a>
      </div> -->
      <div class="button_area">
        <a :class="[{ unactive: select === '1' }]" @click="handleSelect('0')">
          <button type="button" class="btn-login">사람 구해요</button>
        </a>
        <div class="space" />
        <a :class="[{ unactive: select === '0' }]" @click="handleSelect('1')">
          <button type="button" class="btn-login">일자리 구해요</button>
        </a>
      </div>

      <!-- 05/27 수정요청으로 인한 주석처리 -->
      <!-- <div class="sort_area">
        <div class="sort_choice">
          <select-box
            text="사람/일감"
            :list="categories[0].list"
            @change="(...args) => this.changeCategory([0, ...args])"
          />
        </div>

        <div class="sort_choice">
          <select-box
            text="소분류"
            :list="categories[0].selected.subList"
            @change="(...args) => this.changeCategory([1, ...args])"
          />
        </div>

        <div class="sort_box">
          <button type="button" class="type1">구직</button>
          <button type="button" class="type2">구인</button>
        </div>
      </div> -->

      <div class="sort_area">
        <div class="sort_choice" style="margin-left: 10px">
          <!-- <select-box
            text="사람/일감"
            :list="categories[0].list"
            @change="(...args) => this.changeCategory([0, ...args])"
          /> -->
          <div
            style="
              display: flex;
              flex-direction: row;
              align-items: center;
              width: 100%;
            "
          >
            <div style="" class="sort_area">
              <div class="sort_choice">
                <select-box
                  text="최신순"
                  :list="sortCategories"
                  @change="handlesort"
                />
              </div>
            </div>
            <div style="flex-direction: row; display: flex">
              <div
                style="display: flex; flex-direction: row"
                class="button_deactive"
              >
                <button @click="showCheckList(select, 0)">지역</button>

                <!-- <button @click="showModal">지역</button> -->

                <arrowDown />
              </div>
              <div
                style="display: flex; flex-direction: row"
                class="button_deactive"
              >
                <!-- <button>업종</button> -->
                <button @click="showCheckList(select, 1)">업종</button>
                <arrowDown />
              </div>

              <div
                v-if="this.select === '1'"
                style="display: flex; flex-direction: row"
                class="button_deactive"
              >
                <!-- <button>업종</button> -->
                <button @click="showCheckList(1, 0)">개인/팀</button>
                <arrowDown />
              </div>

              <div
                v-if="this.select === '1'"
                style="display: flex; flex-direction: row"
                class="button_deactive"
              >
                <!-- <button>업종</button> -->
                <button @click="showCheckList(1, 1)">안전교육</button>
                <arrowDown />
              </div>

              <!-- <a @click="myFindLocation">
                <button type="button" class="button_deactive">
                  내 조건 적용하기
                </button>
              </a> -->
            </div>
            <!-- <div>
              <selectJobBox
                @getList="getList"
                @showCheckList="showCheckList"
                @findLocationAndService="findLocationAndService"
                :listArray="myUserInfo"
              ></selectJobBox>
            </div> -->
          </div>
          <!-- <selectBox>asdfas</selectBox> -->
        </div>
      </div>

      <div class="list">
        <ul class="" v-if="list.length">
          <!--hunting(구인) or opening(구직)-->
          <li
            class="card"
            :class="{
              hunting: item.work_type === 0,
              opening: item.work_type === 1
            }"
            v-for="(item, key) in list"
            :key="key"
            @click="
              goDetail(
                item.work_user_phone,
                item.work_idx,
                item.work_location,
                item
              )
            "
          >
            <dl>
              <dt class="title" style="display: flex; flex-direction: row">
                <div style="flex: 1">
                  <span
                    style="margin-right: 5px"
                    v-for="(innerItem, key) in item.subList"
                    :key="key"
                    class="occupation"
                    :class="{ type2: item.work_status === 2 }"
                  >
                    {{ innerItem }}
                  </span>
                  <span
                    v-if="item.work_divide_second"
                    class="occupation"
                    :class="{ type2: item.work_status === 2 }"
                  >
                    {{ item.work_divide_second }}
                  </span>
                </div>
                <div class="look">
                  <look style="margin-right: 5px" />조회 {{ item.view_count }}
                </div>
              </dt>
              <div style="display: flex; flex-direction: row">
                <dt
                  style="flex: 1; padding: 0px"
                  class="title"
                  :class="{ type2: item.work_status === 2 }"
                >
                  <div>
                    {{
                      item.work_title.length >= 10
                        ? `${item.work_title}...`
                        : item.work_title
                    }}
                  </div>
                </dt>
                <div style="display: flex; align-items: center">
                  <div
                    v-if="item.best"
                    style="font-size: 13px; font-weight: 100; margin-right: 5px font-color: #DF2E2E; background-color: #FFE9E9; padding:5px; border-radius:11px; color:DF2E2E;"
                    :style="[
                      { color: item.work_status === 2 ? '#dddddd' : '#CD0000' }
                    ]"
                  >
                    인기
                  </div>
                  <!-- <div class="look">
                    <fa
                      @click.stop="
                        toggleLike(item.like_status, item.work_idx, item)
                      "
                      :icon="
                        item.like_status === 0
                          ? 'fa-solid fa-heart'
                          : 'fa-regular fa-heart'
                      "
                      :style="{
                        color: item.like_status === 0 ? 'red' : '#cdcdcd',
                        marginRight: 5,
                        cursor: 'pointer'
                      }"
                      class="heart"
                    />
                  </div> -->
                  <!-- <div class="look">
                    <look style="margin-right: 5px" />조회 {{ item.view_count }}
                  </div> -->
                </div>
              </div>
              <dd class="address" :class="{ type2: item.work_status === 2 }">
                <img
                  :src="require('@/static/images/common/worklocation.png')"
                  alt=""
                  style="margin-right: 5px; display: inline-block"
                />{{ item.work_location }}
              </dd>
              <dd class="address" :class="{ type2: item.work_status === 2 }">
                {{ item.dist }}
              </dd>
              <div class="footer">
                <dd class="date" :class="{ type2: item.work_status === 2 }">
                  <img
                    :src="require('@/static/images/common/workperiod.png')"
                    alt=""
                    style="margin-right: 5px"
                  />{{ item.work_period_start }} ~ {{ item.work_period_end }}
                </dd>
                <dd
                  class="pay"
                  v-if="item.work_type === 0"
                  :class="{ type2: item.work_status === 2 }"
                ></dd>
              </div>
              <div
                class="hi"
                style="padding-bottom: 15px; padding-top: 15px; color=#2D3238; font-size: 20px; font-weight: bold;"
              >
                {{ item.work_payment }}원/일
              </div>
            </dl>
          </li>
        </ul>
        <ul v-else class="">
          <li class="empty">
            <dl>
              <dd class="icon">
                <subtitlesOff />
              </dd>
              <dd class="content">인력항목이 존재하지 않습니다.</dd>
            </dl>
          </li>
        </ul>
      </div>
      <div class="addButton" @click="checkRegist">
        <fa :icon="['fa', 'plus']" style="width: 100%; height: 45px" />
      </div>
      <!-- <div id="map" style="display: none" /> -->
    </div>
  </div>
</template>

<script>
  // import PlusImages from '@/static/images/common/Plus_symbol.svg'
  import subtitlesOff from '@/static/images/common/subtitles_off.svg'
  import arrowDown from '@/static/images/common/Toggle.svg'
  import api from '@/api/api'
  import look from '@/static/images/common/view.svg'

  import moment from 'moment'
  import job from '@/components/common/job'
  import conditionCheckList from '@/components/popup/conditionCheckList'
  import KakaoMapVue from '../../components/kakaoMap/KakaoMap.vue'
  import jobCheckList from '../../components/popup/jobCheckList.vue'
  export default {
    props: {},
    components: {
      look,
      // PlusImages,
      // plus: () => import('@/static/images/common/Plus_symbol.svg'),

      // KakaoMap: () => import('../..//components/kakaoMap/KakaoMap'),
      // textInput: () => import('@/components/common/input')
      // eslint-disable-next-line vue/no-unused-components
      selectBox: () => import('@/components/common/selectbox'),
      // zxcvzxcv: () => import('@/components/common/selectJobBox'),
      // selectJobBox: () => import('@/components/common/selectResourcesBox'),
      subtitlesOff,
      arrowDown
    },
    data() {
      return {
        select: '0',
        val1: 'all',
        val2: 'all',
        val3: 'all',
        val4: 'all',
        categories: [
          {
            selected: {},
            list: job
          },
          {
            selected: {},
            list: []
          }
        ],
        category: 'dateUploaded',
        sortCategories: [
          {
            value: 0,
            text: '거리순',
            category: 'distance'
          },
          {
            value: 1,
            text: '출근일 빠른순',
            category: 'startDate'
          },
          {
            value: 2,
            text: '일당 높은순',
            category: 'payment'
          },
          {
            value: 3,
            text: '최신순',
            category: 'dateUploaded'
          }
        ],
        list: [],
        pageNo: 1,
        scrollPosition: 0,
        isLoading: true,
        latitude: null,
        longitude: null,
        geocoder: null,

        myUserInfo: [{ userLocation: '', userService: '' }]
      }
    },

    mounted() {
      this.showMyFindLocation(this.myUserInfo)

      // this.showMyFindLocation(this.myUserInfo)

      // 거리계산 로직 시작 ... JY
      // new Promise(resolve => {
      //   // this.currentLocation(this.latitude, this.longitude)
      //   this.$store.commit('startSpinner')
      //   navigator.geolocation.getCurrentPosition(
      //     pos => {
      //       this.latitude = pos.coords.latitude
      //       this.longitude = pos.coords.longitude
      //       console.log('asdas')

      //       const script = document.createElement('script')

      //       // this.initMap()

      //       /* global kakao */
      //       script.onload = () => kakao.maps.load(this.initMap)
      //       script.src =
      //         // 서버용 ... JY
      //         // '//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=94614ad1d473ba8d7602a52573c2e378&libraries=services'
      //         // 로컬용 ... JY
      //         '//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=45e7d88af801e0e575d54e8a0cd2ac8a&libraries=services'
      //       document.head.appendChild(script)
      //       resolve()
      //     },
      //     err => {
      //       alert(err.message)
      //       resolve()
      //     }
      //   )
      // }).then(() => {
      //   setTimeout(this.getList(), 1300)
      // })
      // 거리계산 로직 끝 ... JY
      // 아래 코드는 거리계산로직 주석 풀면 실행되면 안됩니다
      this.getList()
      this.openAlert()
    },
    created() {
      if (this.$route.params.type !== undefined) {
        this.select = this.$route.params.type
      }
      this.showMyFindLocation(this.myUserInfo)

      window.addEventListener('scroll', this.handleScroll)
    },
    destroyed() {
      window.removeEventListener('scroll', this.handleScroll)
    },
    methods: {
      openAlert() {
        this.$dialog
          .show({
            type: 'alert',
            title: '오픈채팅 기능 준비중',
            message:
              '오픈채팅 기능은 현재 준비중입니다. 기능이 완성되면 알림을 보내드릴게요.'
            // return false
          })
          .$on('dialog:confirm', () => {
            this.$router.push({ name: 'Main' })
          })
      },
      // 위치잡기
      // eslint-disable-next-line no-unused-vars
      currentLocation(latitude, longitude) {
        // eslint-disable-next-line no-unused-vars
        function success({ coords, timestamp }) {
          latitude = coords.latitude
          longitude = coords.longitude
          // console.log(
          //   `위도: ${latitude}, 경도: ${longitude}, 위치 반환 시간: ${timestamp}`
          // )
        }
        function getUserLocation() {
          if (!navigator.geolocation) {
            throw '위치 정보가 지원되지 않습니다.'
          }
          navigator.geolocation.getCurrentPosition(success)
        }
        getUserLocation()

        // const latitude = coords.latitude
        // const longitude = coords.longitude
      },
      handlesort(v) {
        // console.log(v.category)
        this.category = v.category
        this.list = []
        this.pageNo = 1
        this.isLoading = true
        this.getList(v.category)
      },
      handleSelect(v) {
        this.select = v
        console.log('select: ', v)
        this.val1 = 'all'
        this.val2 = 'all'
        this.list = []
        this.pageNo = 1
        this.isLoading = true
        this.getList()
      },
      goPage(v) {
        this.$router.push({ name: 'detailChat', query: { idx: v } })
      },
      changeCategory(args) {
        this.categories[args[0]].selected = args[1]
        if (args[0] === 0) {
          this.val1 = args[1].value
          if (args[1].value !== 'all') {
            this.val2 = 'all'
            this.categories[1].selected = this.categories[0].selected.subList[0]
          }
        } else if (args[0] === 1) {
          this.val2 = args[1].value
        }
        // console.log(this.val1)
        this.list = []
        this.pageNo = 1
        this.isLoading = true
        this.getList()
      },
      changeSubCategory(v) {
        this.subSelected = v
      },
      async goDetail(phone, workIdx, currentLocation, allItem) {
        // console.log(allItem, '!23')
        // console.log([event.target.classList])
        // if ([...event.target.classList].includes('heart')) {
        //   console.log('1231231231231312314123523463473456')
        //   return false
        // }
        console.log('424')
        if (allItem.work_status === 2) {
          return
        }
        // 조회수 증가
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
            workIdx
          }
          api
            .post('job/open/increase', body, config)
            .then(result => {
              // console.log(result)
              if (result.data.success) {
                // console.log('조회수 증가 성공')
              } else {
                // console.log('조회수 증가 실패')
              }
            })
            .catch(err => console.log(err))
        } catch (err) {
          console.log(err)
          alert('에러발생')
          return
        }
        //구직카드 클릭시 통화하기 팝업출력
        await this.$dialog

          .show({
            // work_location: work_location,
            // currentLocation: currentLocation,
            type: 'confirm', //confirm or alert
            // title: '신청시 채팅 연결합니다',
            message: '신청시 채팅 연결합니다.',

            data: {
              KakaoMapVue: KakaoMapVue,
              currentLocation: currentLocation,
              allItem: allItem,
              confirmText: '신청하기',
              token: this.$store.getters.token
            }
          })

          .$on('dialog:confirm', () => {
            if (this.$store.getters.token === '') {
              this.$dialog.show({
                type: 'alert',
                title: '로그인 후 이용해주세요'
              })
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
                  workIdx,
                  userIdx: allItem.work_user_idx
                }
                api
                  .post('job/request', body, config)
                  .then(result => {
                    if (result.data.success) {
                      if (result.data.message === 'success') {
                        // 신청시 비활성화 기능 삭제
                        // this.list.map(val => {
                        //   if (val.work_idx === workIdx) {
                        //     // val.work_status = 2
                        //   }
                        //   return val
                        // })
                        // alert('신청완료')
                        this.$dialog
                          .show({
                            type: 'alert',
                            title: '신청완료'
                          })
                          .$on('dialog:confirm', () => {
                            this.$router.push({
                              name: 'detailChat',
                              query: {
                                idx: result.data.data,
                                name: allItem.work_user_name
                              }
                            })
                          })
                        // window.location.href = 'tel://' + phone
                        // this.goPage(workIdx)
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
          })
          // eslint-disable-next-line no-unused-vars
          .$on('toggleLikeList', (idx, type) => {
            console.log('763')
            this.list = this.list.map(val => {
              if (val.work_idx === idx) {
                console.log(val.like_status)
                // if (
                //   val.like_status === undefined ||
                //   val.like_status === null ||
                //   val.like_status === 1
                // ) {
                //   val.like_status = 0
                // } else {
                //   val.like_status = 1
                // }
              }
              return val
            })
          })
      },
      async showMyCondition() {
        // console.log('실행')
        // console.log(this.myUserInfo[0])
        // this.$emit('showMyCondition', this.myUserInfo[0].length)
        // console.log(Object.keys(this.myUserInfo[0]).length, '12345')
        if (!this.$store.getters.token || this.$store.getters.token === '') {
          this.$dialog.show({
            type: 'alert',
            title: '로그인 후 이용 해주세요 '
            // return false
          })
        } else if (Object.keys(this.myUserInfo[0]).length === 1) {
          // this.$emit('')
          this.$dialog.show({
            type: 'alert',
            title: '건설노동자만 사용가능합니다'
            // return false
          })
        }
      },
      // eslint-disable-next-line no-unused-vars
      async getList(v) {
        // console.log(v, this.val1, this.val2)
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
            .get(
              `job/open/${this.val1}/${this.val2}/${this.select}/${this.pageNo}`,
              config
            )
            .then(result => {
              console.log(result, 'result')
              if (result.data.success) {
                if (
                  result.data.data.result !== undefined &&
                  result.data.data.result !== null
                ) {
                  let list = result.data.data.result
                  let subList = []
                  let cntList = []
                  list = list.map(value => {
                    if (String(value.work_divide_first).length > 0) {
                      subList = value.work_divide_first.split(':')
                    }
                    value.subList = subList
                    if (
                      value.work_payment !== null &&
                      value.work_payment !== null &&
                      value.work_type !== undefined &&
                      value.work_type === 0
                    ) {
                      value.work_payment =
                        this.$options.filters.autoCommaNumber(
                          value.work_payment
                        )
                    }
                    if (
                      value.work_period_start !== undefined &&
                      value.work_period_start !== null
                    ) {
                      value.work_period_start = `${moment(
                        value.work_period_start
                      ).format('YYYY.MM.DD')}(${moment(value.work_period_start)
                        .locale('ko')
                        .format('dd')})`
                    }
                    if (
                      value.work_period_end !== undefined &&
                      value.work_period_end !== null
                    ) {
                      value.work_period_end = `${moment(
                        value.work_period_end
                      ).format('YYYY.MM.DD')}(${moment(value.work_period_end)
                        .locale('ko')
                        .format('dd')})`
                    }
                    value.best = false
                    value.age = false
                    cntList.push({
                      idx: value.work_idx,
                      cnt: value.view_count
                    })
                    // 거리계산 로직 시작 ... JY
                    // this.geocoder.addressSearch(
                    //   value.work_location,
                    //   (result, status) => {
                    //     if (status === kakao.maps.services.Status.OK) {
                    //       // let coords = new kakao.maps.LatLng(
                    //       //   result[0].y,
                    //       //   result[0].x
                    //       // )
                    //       let newLat = result[0].y
                    //       let newLng = result[0].x
                    //       let radLat1 = (Math.PI * this.latitude) / 180
                    //       let radLat2 = (Math.PI * newLat) / 180
                    //       let theta = this.longitude - newLng
                    //       console.log(this.longitude, 'currentLng')
                    //       let radTheta = (Math.PI * theta) / 180
                    //       let dist =
                    //         Math.sin(radLat1) * Math.sin(radLat2) +
                    //         Math.cos(radLat1) *
                    //           Math.cos(radLat2) *
                    //           Math.cos(radTheta)
                    //       if (dist > 1) dist = 1

                    //       dist = Math.acos(dist)
                    //       dist = (dist * 180) / Math.PI
                    //       dist = dist * 60 * 1.1515 * 1.609344 * 1000
                    //       if (dist < 100) dist = Math.round(dist / 10) * 10
                    //       else dist = Math.round(dist / 100) * 100
                    //       console.log(dist / 1000, '반환값')

                    //       value.dist = dist
                    //       // var marker = new kakao.maps.Marker({
                    //       //   position: coords
                    //       // fn(result[0].y, result[0].x, coords)
                    //       // this.$emit('distance', result2 / 1000 + 'km')
                    //     }
                    //   }
                    // )
                    // 거리계산 로직 종료 ... JY
                    return value
                  })
                  cntList = cntList.sort((a, b) => {
                    if (a.cnt < b.cnt) return 1
                    if (a.cnt === b.cnt) return 0
                    if (a.cnt > b.cnt) return -1
                  })
                  cntList.map((val, idx) => {
                    if (idx < 3) {
                      list.map(inner => {
                        if (inner.work_idx === val.idx) {
                          inner.best = true
                        }
                        return list
                      })
                    }
                  })
                  cntList = []
                  // 소팅 로직 시작 ... JY

                  // list = list.sort((a, b) => {
                  //   console.log(a.dist)
                  //   console.log(b.dist)
                  //   if (a.dist > b.dist) return 1
                  //   if (a.dist === b.dist) return 0
                  //   if (a.dist < b.dist) return -1
                  // })
                  // 소팅 로직 시작 ... JY
                  console.log(list)
                  // sort by payment
                  if (v === 'payment') {
                    list = list.sort((a, b) => {
                      let temp_a = a.work_payment.replace(/,/g, '')
                      temp_a = parseInt(temp_a, 10)
                      let temp_b = b.work_payment.replace(/,/g, '')
                      temp_b = parseInt(temp_b, 10)
                      // console.log(a.dist)
                      // console.log(b.dist)
                      if (temp_a < temp_b) return 1
                      if (temp_a == temp_b) return 0
                      if (temp_a > temp_b) return -1
                    })
                  }

                  // sort by timestamp
                  if (v === 'startDate') {
                    list = list.sort((a, b) => {
                      // console.log(a.dist)
                      // console.log(b.dist)
                      if (
                        new Date(a.work_period_start) >
                        new Date(b.work_period_start)
                      )
                        return 1
                      if (
                        new Date(a.work_period_start) ==
                        new Date(b.work_period_start)
                      )
                        return 0
                      if (
                        new Date(a.work_period_start) <
                        new Date(b.work_period_start)
                      )
                        return -1
                    })
                  }

                  // sort by recent
                  if (v === 'dateUploaded') {
                    list = list.sort((a, b) => {
                      // console.log(a.dist)
                      // console.log(b.dist)
                      if (
                        new Date(a.timestamp_create) <
                        new Date(b.timestamp_create)
                      )
                        return 1
                      if (
                        new Date(a.timestamp_create) ==
                        new Date(b.timestamp_create)
                      )
                        return 0
                      if (
                        new Date(a.timestamp_create) >
                        new Date(b.timestamp_create)
                      )
                        return -1
                    })
                  }
                  this.list = this.list.concat(list)
                  this.pageNo += 1
                  this.isLoading = false
                } else {
                  if (result.data.code === 2121) {
                    this.list = []
                  }
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
      initialize() {},
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
      //     if (val.name === 'all') {
      //     this.val1 = val.value
      //   } else {
      //     this.val2 = val.value
      //   }
      // }
      checkRegist() {
        if (this.$store.getters.token === '') {
          this.$dialog.show({
            type: 'alert',
            title: '로그인 후 이용해주세요.'
          })
        } else {
          if (this.select === '0') {
            this.$router.push({ name: 'OpeningRegistration' })
          } else {
            this.$router.push({ name: 'JobRegistration' })
          }
        }
      },
      async showMyFindLocation(array) {
        // console.log(array)
        if (!this.$store.getters.token || this.$store.getters.token === '') {
          return
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
            .get('mypage/info', config)
            .then(result => {
              // console.log(result, '123')
              if (
                result.data.data.result.user_type === '장비주' ||
                result.data.data.result.user_type === '노무사'
              ) {
                // console.log(this.myUserInfo.[0].this)
                delete this.myUserInfo[0].userService
                this.myUserInfo[0].userLocation =
                  result.data.data.result.user_location
                // console.log('123')
                // this.userService = result.data.data.result.type
              } else {
                console.log(array[0].userService, '!@312312')
                array[0].userService = result.data.data.result.user_type
                array[0].userLocation = result.data.data.result.user_location
                // this.$dialog.show({
                //   type: 'alert',
                //   title: result.data.message
                // })
              }
              // } else {
              //   this.$dialog.show({
              //     type: 'alert',
              //     title: result.data.message
              //   })
              // }
            })
            .catch(err => console.log(err))
        } catch (error) {
          console.log(error)
        }
      },
      myFindLocation() {
        if (Object.keys(this.myUserInfo[0]).length === 1) {
          return this.$dialog.show({
            type: 'alert',
            title: '건설노동자만 사용가능합니다'
            // return false
          })
        } else if (
          !this.$store.getters.token ||
          this.$store.getters.token === ''
        ) {
          return this.$dialog.show({
            type: 'alert',
            title: '로그인 후 이용 해주세요 '
            // return false
          })
        }
        this.val1 = this.myUserInfo[0].userLocation
        this.val2 = this.myUserInfo[0].userService
        this.isLoading = true
        this.list = []
        this.pageNo = 1
        this.getList()
      },
      findLocationAndService(location, service, type) {
        // console.log(location, service, type, '!23123')
        if (service === 'userLocation' || type === '단일선택') {
          this.val1 = location
          this.val2 = 'all'
        } else if (service === 'userService') {
          this.val1 = 'all'
          this.val2 = location
        } else if (type === '서비스선택') {
          // console.log('123여기')
          this.val1 = 'all'
          this.val2 = service
        } else if (type === '초기화') {
          this.val1 = 'all'
          this.val2 = 'all'
          // this.val2
        } else if (type === '다중선택') {
          this.val1 = service
          this.val2 = location
          // this.val2
        }
        this.isLoading = true
        this.list = []
        this.pageNo = 1
        this.getList()
      },
      // showModal() {
      //   this.$dialog.show({
      //     template: conditionCheckList
      //   })
      // },

      // eslint-disable-next-line no-unused-vars
      showCheckList(select, filters) {
        // console.log(v, v1)
        // console.log(this.myUserInfo[0])
        // this.val1 = this.myUserInfo[0].userLocation
        // this.val2 = this.myUserInfo[0].userService
        // console.log(this.myUserInfo[0]).length, '여기야')
        if (Object.keys(this.myUserInfo[0]).length === 1) {
          return this.$dialog.show({
            type: 'alert',
            title: '건설노동자만 사용가능합니다'
            // return false
          })
        }
        if (!this.$store.getters.token || this.$store.getters.token === '') {
          return this.$dialog.show({
            type: 'alert',
            title: '로그인 후 이용 해주세요 '
            // return false
          })
        }

        // if (type === '초기화') {
        //   this.val1 = location
        //   this.val2 = service
        // } else if (type === 'selectAll') {
        //   this.val1 = location
        //   this.val2 = service
        // } else if (!location || !location === 'undefinded') {
        //   // console.log(this.myUserInfo[0].us)
        //   this.val1 = this.myUserInfo[0].userLocation
        //   this.val2 = this.myUserInfo[0].userService
        // }

        // if v is 0...
        this.$dialog
          .show({
            // template: conditionCheckList
            template: select === '0' ? conditionCheckList : jobCheckList
          })

          .$on('dialog:confirm', v => {
            console.log(this.val2)
            console.log(v, 'v')

            // if (v !== undefined && v !== null && v.length > 0) {
            //   v.map(val => {
            //     // console.log(v[1])
            //     // console.log(val.name, 'val')
            //     if (val.name === 'location') {
            //       this.val2 = 'all'
            //       this.val1 = val.value
            //       // this.val2 = 'all'
            //     } else if (val.type === '초기화') {
            //       this.val1 = val.value
            //       this.val2 = val.value
            //     } else if (val.name === 'skill') {
            //       this.val1 = 'all'
            //       this.val2 = val.value
            //     }
            //     if (v.length > 1) {
            //       // console.log(val.name === 'location' && val.name === 'skill')
            //       this.val1 = v[0].value
            //       this.val2 = v[1].value
            //     }
            //     // this.console // this.val2 = val.value
            //     //   .log(this.val1, 'val2')
            //   })
            // }

            this.isLoading = true
            this.list = []
            this.pageNo = 1
            // make a loop, assign val1 and val2 inside the list and call getlist() repeatedly
            // to filter multiple selects.

            // either of location or skill was not select -> get all
            if (this.val2 === 'all') {
              for (let outer = 0; outer < v.length; outer++) {
                if (v[outer].name === 'location') {
                  this.val1 = v[outer].value
                  this.val2 = 'all'
                } else {
                  this.val1 = 'all'
                  this.val2 = v[outer].value
                }
                console.log('location: ', this.val1, 'skill:', this.val2)
                this.getList()
              }
            } else {
              for (let outer = 0; outer < v.length; outer++) {
                for (let inner = outer; inner < v.length; inner++) {
                  this.val2 = 'all'
                  this.val1 = 'all'
                  if (v[inner].name !== v[outer].name) {
                    if (v[inner].name === 'location') {
                      this.val1 = v[inner].value
                      this.val2 = v[outer].value
                    } else {
                      this.val1 = v[outer].value
                      this.val2 = v[inner].value
                    }
                    console.log('location: ', this.val1, 'skill:', this.val2)
                    this.getList()
                  }
                }
              }
            }
            // this.getList()
          })
      },
      async toggleLike(status, boardNum, item) {
        if (!this.$store.getters.token || this.$store.getters.token === '') {
          return this.$dialog.show({
            type: 'alert',
            title: '로그인 후 이용 해주세요 '
            // return false
          })
        }
        let url = ''
        if (status === undefined || status === null) {
          url = 'like/insert'
        } else {
          url = 'like/update'
        }

        try {
          const config = {
            headers: {
              'Content-Type': 'application/json; charset = utf-8',
              'Access-Control-Allow-Origin': '*',
              authorization: `Bearer ${this.$store.getters.token}`
            }
          }
          const body = {
            type: 0,
            boardNum
          }
          await api
            .post(url, body, config)
            .then(result => {
              console.log(result)
              let arr = this.list.map(val => {
                if (val.work_idx === item.work_idx) {
                  if (status === undefined || status === null || status === 1) {
                    val.like_status = 0
                  } else {
                    val.like_status = 1
                  }
                }
                return val
              })
              this.list = arr
            })
            .catch(err => console.log(err))
        } catch (err) {
          console.log(err)
        }
      }
    }
  }
</script>
