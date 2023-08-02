<template>
  <div>
    <div>
      <div id="map"></div>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'KakaoMap',
    props: ['location', 'allItem'],
    // created() {},
    data() {
      return {
        props: {
          allItem: { type: Object, default: {} }
        },
        data: {
          receiveData: this.location,
          // allItem: {}
          allItem: this.allItem
        },
        loading: true,
        markers: [],
        newArray: [],
        latitude: null,
        longitude: null,
        infowindow: null,
        currentLng: 0,

        // data: new kakao.maps.services.Geocoder(),
        cureetltd: 0,
        resultCoordinate: 0,
        // apikey: process.env.VUE_APP_KAKAO
        map: '',
        myLocationX: '',
        myLocationY: '',
        getX: '',
        geocoder: '',
        getY: ''
      }
    },
    created() {},
    mounted() {
      console.log(this.data)
      navigator.geolocation.getCurrentPosition(position => {
        this.myLocationX = position.coords.latitude
        this.myLocationY = position.coords.longitude
        this.initMap(
          this.myLocationX,
          this.myLocationY,
          this.getLocationX,
          this.getLocationY
        )
      })

      if (window.kakao && window.kakao.maps) {
        this.initMap(this.myLocationX, this.myLocationY)
      } else {
        const script = document.createElement('script')

        /* global kakao */
        script.onload = () => kakao.maps.load(this.initMap)
        script.src =
          '//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=633234b9f7768e388b7901b539e164b8&libraries=services'
        document.head.appendChild(script)
      }
      err => {
        alert(err.message)
      }

      // // console.log(Spinner)
      // this.currentLocation(this.latitude, this.longitude)
      // console.log(this.latitude, this.longitude, '12실행')
      // navigator.geolocation.getCurrentPosition(
      //   pos => {
      //     this.latitude = pos.coords.latitude
      //     this.longitude = pos.coords.longitude
      //     if (window.kakao && window.kakao.maps) {
      //       this.initMap(
      //         this.allItem,
      //         this.longitude,
      //         this.latitude,
      //         this.allItem.work_title,
      //         // this.currentLng,
      //         // this.cureetltd,
      //         this.resultCoordinated
      //       )
      //       console.log(this.allItem.work_title, 'pos2')
      //     } else {
      //       console.log('1여기탐 ')
      //       const script = document.createElement('script')
      //       /* global kakao */
      //       script.onload = () => kakao.maps.load(this.initMap())
      //       script.src =
      //         // 서버용 ... JY
      //         '//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=94614ad1d473ba8d7602a52573c2e378&libraries=services'
      //       // 로컬용 ... JY
      //       // '//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=45e7d88af801e0e575d54e8a0cd2ac8a&libraries=services'
      //       document.head.appendChild(script)
      //     }
      //   },
      //   err => {
      //     alert(err.message)
      //   }
      // )
    },

    methods: {
      getUserLocation(success) {
        if (!navigator.geolocation) {
          throw '위치 정보가 지원되지 않습니다.'
        }
        navigator.geolocation.getCurrentPosition(success)
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
        setZoomable(false)
        setDraggable(false)
      },
      async initMap() {
        this.geocoder = new kakao.maps.services.Geocoder()

        this.geocoder.addressSearch(this.data.receiveData, (result, status) => {
          this.$emit('coordinateX', result[0].x)
          this.$emit('coordinateY', result[0].y)
          console.log(result[0].x, status, this.getLocationX)
          var coords = new kakao.maps.LatLng(result[0].y, result[0].x)
          const container = document.getElementById('map') //지도를 담을 영역의 DOM 레퍼런스
          const options = {
            center: new window.kakao.maps.LatLng(33.450701, 126.570667),
            level: 3 // 지도의 확대 레벨
          }

          const map = new window.kakao.maps.Map(container, options)
          var locPosition = new kakao.maps.LatLng(result[0].y, result[0].x)

          var message = `<div style="padding:5px;">${this?.data?.allItem?.work_title}</div>`
          this.displayMarker(locPosition, message, map)

          map.setCenter(coords)
          this.distanceFun(
            this.myLocationX,
            this.myLocationY,
            result[0].x,
            result[0].y
          )
        })

        // const container = document.getElementById('map') //지도를 담을 영역의 DOM 레퍼런스
        // const options = {
        //   center: new window.kakao.maps.LatLng(33.450701, 126.570667),
        //   level: 3 // 지도의 확대 레벨
        // }
        // this.map = new window.kakao.maps.Map(container, options)
        // console.log(map)
      },
      async distanceFun(myX, myY, getX, getY) {
        console.log(myX, myY, getX, getY)
        if (myX === undefined || null || myY === undefined || null) {
          this.$emit('distance', 0 + 'km')
        } else {
          let newLat = myX
          let newLng = myY
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
          console.log(dist, '반환값')
          this.$emit('distance', dist / 1000 + 'km')

          // this.distanceData = dist
        }
      }
      //좌표
      //두좌표사이의 거리
      //현재위치 거리
      // currentLocation(latitude, longitude) {
      //   function success({ coords, timestamp }) {
      //     latitude = coords.latitude
      //     longitude = coords.longitude
      //     console.log(
      //       `위도: ${latitude}, 경도: ${longitude}, 위치 반환 시간: ${timestamp}`
      //     )
      //   }
      //   function getUserLocation() {
      //     if (!navigator.geolocation) {
      //       throw '위치 정보가 지원되지 않습니다.'
      //     }
      //     navigator.geolocation.getCurrentPosition(success)
      //   }
      //   getUserLocation()
      // },
      // initMap(item, currentLng, currentlat, worktitle, result2) {
      //   if (window.kakao && window.kakao.maps) {
      //     // this.resultCoordinate = result2
      //     var geocoder = new kakao.maps.services.Geocoder()
      //     console.log(geocoder, '!23')
      //     this.resultCoordinate = result2
      //     console.log(this.resultCoordinate === result2, 'item')
      //     worktitle = this.data.allItem.work_title
      //     currentLng = this.longitude
      //     currentlat = this.latitude
      //     var fn
      //     fn = function callBackTest(y, x, cor) {
      //       const container = document.getElementById('map')
      //       const options = {
      //         center: new kakao.maps.LatLng(y, x),
      //         level: 3
      //       }
      //       var map = new kakao.maps.Map(container, options)
      //       function setZoomable(zoomable) {
      //         // 마우스 휠로 지도 확대,축소 가능여부를 설정합니다
      //         map.setZoomable(zoomable)
      //       }
      //       function setDraggable(draggable) {
      //         // 마우스 드래그로 지도 이동 가능여부를 설정합니다
      //         map.setDraggable(draggable)
      //       }
      //       var marker = new kakao.maps.Marker({
      //         map: map,
      //         position: cor
      //       }) // 인포윈도우로 장소에 대한 설명을 표시합니다
      //       var infowindow = new kakao.maps.InfoWindow({
      //         content: `<div style="width:150px;text-align:center;padding:3px 0;"> ${worktitle}  </div>`
      //       })
      //       var newLat = y
      //       var newLng = x
      //       var radLat1 = (Math.PI * currentlat) / 180
      //       var radLat2 = (Math.PI * newLat) / 180
      //       var theta = currentLng - newLng
      //       console.log(currentLng, 'currentLng')
      //       var radTheta = (Math.PI * theta) / 180
      //       var dist =
      //         Math.sin(radLat1) * Math.sin(radLat2) +
      //         Math.cos(radLat1) * Math.cos(radLat2) * Math.cos(radTheta)
      //       if (dist > 1) dist = 1
      //       dist = Math.acos(dist)
      //       dist = (dist * 180) / Math.PI
      //       dist = dist * 60 * 1.1515 * 1.609344 * 1000
      //       if (dist < 100) dist = Math.round(dist / 10) * 10
      //       else dist = Math.round(dist / 100) * 100
      //       console.log(dist / 1000, '반환값')
      //       result2 = dist
      //       console.log(result2, '!231')
      //       infowindow.open(map, marker)
      //       map.setCenter(cor)
      //       setZoomable(false)
      //       setDraggable(false)
      //     }
      //     geocoder.addressSearch(this.location, (result, status) => {
      //       if (status === kakao.maps.services.Status.OK) {
      //         let coords = new kakao.maps.LatLng(result[0].y, result[0].x)
      //         console.log(result)
      //         this.$emit('coordinateX', result[0].x)
      //         this.$emit('coordinateY', result[0].y)
      //         fn(result[0].y, result[0].x, coords)
      //         this.$emit('distance', result2 / 1000 + 'km')
      //       }
      //     })
      //   } else {
      //     console.log('ASDFADSF')
      //   }
      // }
    }
  }
</script>

<style scoped>
  #map {
    width: 100%;
    height: 25vh;
  }
</style>
