<template>
  <div class="share">
    <back v-if="this.referer === true" />
    <div id="map"></div>
    <div class="share_info">
      <span>거리: </span>
      <p>
        {{ this.distanceData / 1000 }}km
        <!-- {{ this.allItem.work_location }} -->
      </p>
    </div>
    <div class="share_info_address">
      <span>현장장소: </span>
      <div @click="goPageAddres">
        <p>{{ this.allItem.work_title }}</p>
      </div>
    </div>
    <div class="share_info">
      <span>건설사: </span>
      <p>
        {{ this.allItem.work_constructor }}
      </p>
    </div>
    <div v-if="this.$store.getters.token" class="share_info">
      <span>이름: </span>
      <p>
        {{ this.username }}
      </p>
    </div>
    <div v-if="this.$store.getters.token" class="share_info">
      <span>전화번호: </span>
      <p>
        {{ this.phoneNumber }}
      </p>
    </div>
    <div class="share_info">
      <span>소분류: </span>
      <p>
        {{ this.allItem.work_divide_second }}
      </p>
    </div>
    <div class="share_info">
      <span>대분류: </span>
      <p>
        {{ this.allItem.work_divide_first }}
      </p>
    </div>
    <button @click="jobApply">신청하기</button>
  </div>
</template>

<script>
  import api from '@/api/api'
  import axios from 'axios'
  import back from '@/components/layout/back'

  export default {
    name: 'Share',
    components: {
      back
    },
    data() {
      return {
        number: '',
        KakaoMap: null,
        myLocationX: '',
        myLocationY: '',
        getLocationX: '',
        getLocationY: '',
        myLocation: '',
        title: '',
        map: null,
        document: '',
        allItem: [],
        exampleTitle: '',
        geocoder: null,
        distanceData: 0,
        username: '',
        phoneNumber: '',
        getLinkMapY: '',
        getLinkMapX: '',
        id: '',
        referer: false
      }
    },
    mounted() {
      console.log(this.$route)
      this.referrerCheck()
      navigator.geolocation.getCurrentPosition(position => {
        this.myLocationX = position.coords.latitude
        this.myLocationY = position.coords.longitude
        this.addressSearch(
          this.myLocationX,
          this.myLocationY,
          this.getLocationX,
          this.getLocationY
        )
      })

      if (window.kakao && window.kakao.maps) {
        this.addressSearch()
      } else {
        const script = document.createElement('script')

        /* global kakao */
        script.onload = () => kakao.maps.load(this.addressSearch)
        script.src = `//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=633234b9f7768e388b7901b539e164b8&libraries=services`
        document.head.appendChild(script)
      }
    },

    methods: {
      async goPageAddres() {
        if (this.id === null || !this.id) {
          alert('지도가 로딩중입니다')
        } else {
          window.open(`https://map.kakao.com/link/map/${this.id}`)
        }
      },
      async jobApply() {
        if (!this.$store.getters.token || this.$store.getters.token === '') {
          this.$router.push({ name: 'Main' })
        }
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
            workIdx: this.allItem.work_idx,
            userIdx: this.allItem.work_user_idx
          }
          api
            .post('job/request', body, config)
            .then(result => {
              if (result.data.success) {
                if (result.data.message === 'success') {
                  // this.$dialog
                  //   .show({
                  //     type: 'alert',
                  //     title: '신청이 완료 되었습니다'
                  //   })
                  //   .$on('dialog:confirm', () => {
                  //     this.$router.push('/')
                  //   })
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
                          name: this.allItem.work_user_name
                        }
                      })
                    })
                  // this.list.map(val => {
                  //   if (val.work_idx === workIdx) {
                  //     val.work_status = 2
                  //   }
                  //   return val
                  // })
                  // window.location.href = 'tel://' + phone
                  // // this.goPage(workIdx)
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
      },
      phoneSlice(phone) {
        // const phone = '010-4797-8158'
        const phoneStr = phone?.match(/\d{2,3}-\d{3,4}-\d{4}/gi)
        // console.log(phoneStr)
        const maskingStr = phone
          .toString()
          .replace(
            phoneStr,
            phoneStr?.toString().replace(/-[0-9]{4}-/g, '-****-')
          )
        return (this.phoneNumber = maskingStr)
      },
      lastNameSlice(name) {
        console.log('name')
        const sliceName = name?.split('')

        if (name?.length === 4 || name?.length === 3) {
          sliceName?.forEach((name, index) => {
            console.log(index, sliceName?.length)
            if (index === 0 || index === sliceName?.length - 1) return
            sliceName[index] = '*'
            console.log((sliceName[index] = '*'), name)
          })
          const returnBackName = sliceName?.join()
          console.log(returnBackName?.replace(/,/g, ''))
          return (this.username = returnBackName?.replace(/,/g, ''))
        } else if (name?.length === 2) {
          sliceName[1] = '*'
          const backName = sliceName?.join()?.replace(/,/g, '')
          return (this.username = backName)
        }
      },
      async addressSearch(a, b, c, d) {
        console.log(a, b, c, d)

        try {
          const config = {
            headers: {
              'Content-Type': 'application/json; charset = utf-8',
              'Access-Control-Allow-Origin': '*',

              authorization: `Bearer ${this.$store.getters.token}`
            }
          }
          await api
            .get(`job/open/share/${this.$route.params.idx}`, config)
            .then(result => {
              if (result.data.data) {
                this.allItem = result.data?.data.result[0]
                this.title = result.data?.data.result[0].work_title
                console.log(this.allItem)

                this.phoneSlice(result.data?.data.result[0].work_user_phone)
                this.lastNameSlice(result.data?.data.result[0].work_user_name)
                this.geocoder = new kakao.maps.services.Geocoder()

                this.geocoder.addressSearch(
                  result.data?.data.result[0]?.work_location,
                  (result, status) => {
                    try {
                      axios
                        .get(
                          `https://dapi.kakao.com/v2/local/search/keyword.json?query=${this.title}`,
                          {
                            headers: {
                              Authorization: `KakaoAK 66a6e1a7d96cd18bb50effc8aa2bcb93`
                            }
                          }
                        )
                        .then(res => {
                          if (res.statusText === 'OK') {
                            console.log(res.data.documents[0].id)
                            this.id = res.data.documents[0].id
                          }

                          // console.log(res)
                        })
                        .catch(error => {
                          console.log(error)
                        })
                    } catch (error) {
                      console.log(error)
                    }
                    console.log(result, 'result', status)
                    // this.goPageAddres(coords)

                    var coords = new kakao.maps.LatLng(result[0].y, result[0].x)

                    const container = document.getElementById('map') //지도를 담을 영역의 DOM 레퍼런스
                    const options = {
                      center: new window.kakao.maps.LatLng(
                        33.450701,
                        126.570667
                      ),
                      level: 3 // 지도의 확대 레벨
                    }

                    const map = new window.kakao.maps.Map(container, options)
                    var locPosition = new kakao.maps.LatLng(
                      result[0].y,
                      result[0].x
                    )
                    var message = `<div style="padding:5px;">${this.title}</div>`

                    this.displayMarker(locPosition, message, map)
                    // this.getLinkMapY = result[0].y
                    // this.getLinkMapY = result[0].x
                    console.log(this.getLinkMapY, this.getLinkMapX)
                    // this.goPageAddres(result[0].x, result[0].y)
                    map.setCenter(coords)
                    this.distanceFun(a, b, result[0].x, result[0].y)
                  }
                )
              } else {
                console.log('workdetail 데이터없음')
              }
            })
            .catch(err => console.log(err))
        } catch (err) {
          console.log(err)
          alert('에러발생')
          return
        }
      },
      displayMarker(locPosition, message, map) {
        // 마커를 생성합니다
        var marker = new kakao.maps.Marker({
          map: map,
          position: locPosition
        })

        var iwContent = message, // 인포윈도우에 표시할 내용
          iwRemoveable = true

        var infowindow = new kakao.maps.InfoWindow({
          content: iwContent,
          removable: iwRemoveable
        })
        function setZoomable(zoomable) {
          // 마우스 휠로 지도 확대,축소 가능여부를 설정합니다
          map.setZoomable(zoomable)
        }
        function setDraggable(draggable) {
          // 마우스 드래그로 지도 이동 가능여부를 설정합니다
          map.setDraggable(draggable)
        }
        infowindow.open(map, marker)

        // 지도 중심좌표를 접속위치로 변경합니다
        map.setCenter(locPosition)

        infowindow.open(map, marker)
        setZoomable(false)
        setDraggable(false)
        // 지도 중심좌표를 접속위치로 변경합니다
        map.setCenter(locPosition)
      },
      distanceFun(x, y, getX, getY) {
        //지도 생성 및 객체 리턴

        if (x === undefined || y === undefined || null) {
          this.distanceData = 0
        } else {
          let newLat = x
          let newLng = y
          var radLat1 = (Math.PI * getY) / 180
          var radLat2 = (Math.PI * newLat) / 180
          var theta = getX - newLng
          var radTheta = (Math.PI * theta) / 180
          var dist =
            Math.sin(radLat1) * Math.sin(radLat2) +
            Math.cos(radLat1) * Math.cos(radLat2) * Math.cos(radTheta)
          if (dist > 1) dist = 1
          dist = Math.acos(dist)
          dist = (dist * 180) / Math.PI
          dist = dist * 60 * 1.1515 * 1.609344 * 1000
          if (dist < 100) dist = Math.round(dist / 10) * 10
          else dist = Math.round(dist / 100) * 100
          console.log(dist / 1000, '반환값')

          this.distanceData = dist
          this.getLinkMapX = getX
          this.getLinkMapY = getY
          // this.goPageAddres(this.getLinkMapX, this.getLinkMapY)
        }
      },
      referrerCheck() {
        if (history.length > 3) {
          this.referer = true
        }
      }
    }
  }
</script>
<style scoped>
  #map {
    max-width: 100%;
    height: 70vh;
  }
</style>
