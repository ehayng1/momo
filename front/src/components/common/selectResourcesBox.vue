<template>
  <div class="selectResource" :data-value="selected">
    <div class="selector" @click="toggle()">
      <div class="label">
        <span>{{ selected }}</span>
      </div>
      <div class="arrow" :class="{ expanded: visible }"></div>
      <div :class="{ hidden: !visible, visible }">
        <ul>
          <li
            :class="{ current: item === selected }"
            v-for="(item, key) in selectList[0]"
            :key="key"
            @click="select(item, key)"
          >
            <!-- @click="select(item)" -->

            {{ item }}
          </li>

          <div @click="showModal" class="plus">
            <fa
              :icon="['fa', 'plus']"
              style="height: 15px; text-align: right"
            />
          </div>
        </ul>
      </div>
    </div>
    <div class="overlay" v-if="visible" @click="toggle()"></div>
  </div>
</template>

<script>
  import conditionCheckList from '@/components/popup/conditionCheckList'
  import api from '@/api/api'

  export default {
    name: 'selectJobBox',
    data() {
      return {
        visible: false,
        selected: '',
        selectList: [{ userLocation: '', userService: '' }],
        type: '',
        filterList: []
      }
    },
    props: {
      //   text: {
      //     type: String,
      //     default: 'Select a FruitSelect a Fruit'
      //   },
      listArray: {
        type: Array,
        default: () => {
          return [
            // {
            //   value: 0,
            //   text: '사과'
            // }
            // {
            //   value: 1,
            //   text: '오렌지'
            // },
            // {
            //   value: 2,
            //   text: '귤'
            // },
            // {
            //   value: 3,
            //   text: '망고'
            // }
          ]
        }
      }
    },

    mounted() {
      console.log(this.props)
      // this.init()
      this.showMyFindLocation()
      // console.log(this.listArray)
      //   this.selected = this.text
    },
    methods: {
      async showMyFindLocation(array) {
        console.log(array)
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
              console.log(this.selectList[0], 'selectbox')
              this.selectList[0].userService = result.data.data.result.user_type
              this.selectList[0].userLocation =
                result.data.data.result.user_location
              console.log(this.selectList)
            })
            .catch(err => console.log(err))
        } catch (error) {
          console.log(error)
        }
      },
      // init() {
      //   this.$on('dialog:open', () => {
      //     console.log('오픈하고 실행')
      //   })
      // },

      showModal() {
        this.$dialog
          .show({
            template: conditionCheckList
          })

          .$on('dialog:confirm', v => {
            if (v.length === 2) {
              this.val1 = v[0].value
              this.val2 = v[1].value
              return this.$emit(
                'findLocationAndService',
                this.val2,
                this.val1,
                '다중선택'
              )
            }
            if (v !== undefined && v !== null && v.length > 0) {
              v.map(val => {
                if (val.type === '초기화') {
                  this.val1 = 'all'
                  this.val2 = 'all'
                  this.type = val.type
                  this.$emit(
                    'findLocationAndService',
                    this.val2,
                    this.val1,
                    this.type
                  )
                } else if (val.name === 'location') {
                  console.log(val, 'oca')
                  this.val1 = val.value
                  this.val2 = 'all'
                  this.$emit(
                    'findLocationAndService',
                    this.val1,
                    this.val2,
                    '단일선택'
                  )
                } else if (val.name === 'skill') {
                  console.log(val, 'oca')
                  this.val1 = 'all'
                  this.val2 = val.value
                  this.$emit(
                    'findLocationAndService',
                    this.val1,
                    this.val2,
                    '서비스선택'
                  )
                }
                // console.log(v[1])
                // console.log(val.name, 'val')
                // if (val.name === 'location') {
                //   this.val2 = 'all'
                //   this.val1 = val.value
                //   this.type = val.name
                //   this.$emit('showCheckList', this.val2, this.val1, this.type)
                //   // this.val2 = 'all'
                // } else if (val.type === '초기화') {
                //   this.val1 = val.value
                //   this.val2 = val.value
                //   this.type = val.type
                //   this.$emit('showCheckList', this.val2, this.val1, this.type)
                // } else if (val.name === 'skill') {
                //   this.val1 = 'all'
                //   this.val2 = val.value
                //   this.type = val.name

                //   this.$emit('showCheckList', this.val2, this.val1, this.type)
                // }
                // if (v.length > 1) {
                //   // console.log(val.name === 'location' && val.name === 'skill')
                //   this.val1 = v[0].value
                //   this.val2 = v[1].value
                //   this.$emit('showCheckList', this.val2, this.val1, 'selectAll')
                // }
                // this.console // this.val2 = val.value
                //   .log(this.val1, 'val2')
              })
              this.isLoading = true
              this.list = []
              this.pageNo = 1
            }
          })
        // this.eventStatus = 'dialog:confirm'
        // this.$emit(this.eventStatus, this.eventStatus)
      },
      toggle() {
        if (!this.$store.getters.token || this.$store.token === '') {
          return this.$dialog.show({
            type: 'alert',
            title: '로그인 후 이용해주세요'
          })
        } else if (Object.keys(this.listArray[0]).length === 1) {
          // this.$emit('')
          return this.$dialog.show({
            type: 'alert',
            title: '건설노동자만 사용가능합니다'
            // return false
          })
        } else this.visible = !this.visible
      },

      select(option, key) {
        this.selected = option.text

        // this.$emit('showMyCondition', option)
        this.$emit('findLocationAndService', option, key)
      }
    }
  }
</script>
