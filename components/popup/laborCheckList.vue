<template>
  <div class="dialog dialog_main" v-if="isShow">
    <div class="dialog_contents">
      <div class="dialog_body">
        <div class="dialog_title">해당 항목에 체크 해주세요.</div>
        <div class="dialog_check_list">
          <div class="dialog_check_wrap">
            <div @click="togleClick(1)">
              <input
                id="1"
                type="checkbox"
                :value="1"
                @change="checkItem"
                v-model="checkAll"
              />
              <label for="1">직장인갑질</label>

              <span
                style="
                  margin-left: 10px;
                  font-size: 15px;
                  color: red;
                  font-weight: 700;
                "
                >{{ message }}</span
              >
            </div>
            <div v-if="visible" class="check_content">
              <input
                id="a"
                @change="ifFun"
                v-model="check1"
                type="checkbox"
                :value="1"
              />
              <label for="a">행위자(가해자)가 누군지 지목할 수 있다.</label>
              <input
                id="b"
                type="checkbox"
                v-model="check2"
                :value="1"
                @change="ifFun"
              />
              <label for="b"> 보통의 업무처리상 통상적인 행위가 아니다 </label>
              <input
                :value="1"
                @change="ifFun"
                v-model="check3"
                type="checkbox"
                id="c"
              />
              <label for="c">
                체적인 증거(영상, 녹취록, 사진 등)가 있거나 수집이 가능하다
              </label>
            </div>
            <div @click="togleClick(2)" style="margin-top: 10px">
              <input
                id="ab"
                type="checkbox"
                :value="2"
                @change="checkItem"
                v-model="checkAll2"
              />
              <label for="ab">산재확인</label>
              <span
                style="
                  margin-left: 10px;
                  font-size: 15px;
                  color: red;
                  font-weight: 700;
                "
                >{{ message1 }}</span
              >
            </div>

            <div v-if="visible1" class="check_content">
              <input
                id="d"
                @change="ifFun"
                v-model="check4"
                type="checkbox"
                :value="2"
              />
              <label for="d">근무시간내 상해를 (사고를) 당했다</label>
              <input
                id="e"
                type="checkbox"
                v-model="check5"
                :value="2"
                @change="ifFun"
              />
              <label for="e"> 휴업이 3일이상(진단 4일 이상)이다 </label>
              <input
                :value="2"
                @change="ifFun"
                v-model="check6"
                type="checkbox"
                id="f"
              />
              <label for="f">
                평소 가지고 있던 지병과 관계가 없는 상해이다
              </label>
            </div>
            <div @click="togleClick(3)">
              <input
                id="abc"
                type="checkbox"
                :value="3"
                @change="checkItem"
                v-model="checkAll3"
              />
              <label style="margin-top: 10px" for="abc">임금체불</label>
              <span
                style="
                  margin-left: 10px;
                  font-size: 15px;
                  color: red;
                  font-weight: 700;
                "
                >{{ message2 }}</span
              >
            </div>

            <div v-if="visible2" class="check_content">
              <input
                id="m"
                @change="ifFun"
                v-model="check7"
                type="checkbox"
                :value="3"
              />
              <label for="m">
                급여,퇴직금,수당(휴일,연장,등)이 미지급 되었다.</label
              >
              <input
                id="n"
                type="checkbox"
                v-model="check8"
                :value="3"
                @change="ifFun"
              />
              <label for="n">
                근로계약서가 존재한다(또는 일정하게 금액이 지금된 통장 증명이
                가능하다)
              </label>
              <input
                :value="3"
                @change="ifFun"
                v-model="check9"
                type="checkbox"
                id="x"
              />
              <label for="x">
                체적인 증거(영상, 녹취록, 사진 등)가 있거나 수집이 가능하다
              </label>
            </div>
          </div>
        </div>
      </div>
      <div class="dialog_confirm_bundle">
        <button
          type="button"
          style="margin-right: 10px"
          class="btn_dialog btn_primary"
          @click="check"
        >
          확인
        </button>
        <button type="button" class="btn_dialog btn_gray" @click="close">
          취소
        </button>
      </div>
    </div>
  </div>
</template>

<script>
  import { dataList } from './constant'
  export default {
    name: 'laborCheckList',
    data() {
      return {
        eventStatus: null,
        list: [],
        bottomlist: [],
        checkAll: false,
        checkAll2: false,
        check1: false,
        check2: false,
        check3: false,
        check4: false,
        check5: false,
        check6: false,
        check7: false,
        check8: false,
        checkAll3: false,
        check9: false,
        case1: [],
        select: '',
        type: '',
        test: '',
        visible: false,
        visible1: false,
        visible2: false,
        message: '',
        message1: '',
        message2: ''
      }
    },
    beforeMount() {
      this.$on('dialog:close', () => {
        this.setClose()
      })
    },
    mounted() {
      this.bottomlist = dataList

      this.init()
    },
    computed: {},
    methods: {
      ifFun(e) {
        const v = e.target.value

        console.log(v)
        this.list.push(v)
        this.list = [...new Set(this.list)]
        console.log(this.list)
        if (
          this.check1 === true &&
          this.check2 === true &&
          this.check3 === true
        ) {
          this.message = '해당 됩니다'
          this.checkAll = true
        } else {
          this.message = ''
        }
        if (
          this.check4 === true &&
          this.check5 === true &&
          this.check6 === true
        ) {
          this.message1 = '해당 됩니다'
          this.checkAll2 = true
        } else {
          this.message1 = ''
        }
        if (
          this.check7 === true &&
          this.check8 === true &&
          this.check9 === true
        ) {
          this.message2 = '해당됩니다'
          this.checkAll3 = true
        } else {
          this.message2 = ''
        }
      },

      close() {
        this.$dialog.close()
      },
      togleClick(number) {
        if (Number(number) === 1) {
          this.visible = true

          if (this.checkAll) {
            this.visible = false
            this.message = ''
          }
        } else if (Number(number) === 2) {
          this.visible1 = true
          if (this.checkAll2) {
            this.visible1 = false
            this.message1 = ''
          }
        } else if (Number(number) === 3) {
          this.visible2 = true
          if (this.checkAll3) {
            this.visible2 = false
            this.message2 = ''
          }
        }
        // this.visible = true
        // if (this.checkAll) {
        //   this.visible = false

        //   this.message = ''
        // }
      },
      // togleClick1() {
      //   this.visible1 = true
      //   if (this.checkAll2) {
      //     this.visible1 = false

      //     this.message = ''
      //   }
      // },
      init() {
        this.$on('dialog:open', () => {
          console.log('오픈하고 실행')
        })
      },
      setClose() {
        this.$dialog.close()
        this.$emit(this.eventStatus, this.list)
      },
      check() {
        //TODO 산재확인 api 연동후 성공하면 close하기.
        if (this.list.length === 0) {
          // 선택 안했을때 전체조회하게끔 하는 코드
          // this.list.push('all')
          // 코드 끝
          alert('항목을 1개 이상 선택해주세요.')
          return false
        }
        this.eventStatus = 'dialog:confirm'
        this.setClose()
      },

      checkItem(e) {
        const v = e.target.value
        const id = e.target.id
        console.log(v, id)

        if (v === '1') {
          if (!this.checkAll) {
            this.check1 = false
            this.check2 = false
            this.check3 = false
          } else {
            this.message = '해당됩니다'

            this.check1 = true
            this.check2 = true
            this.check3 = true
          }
        } else if (v === '2') {
          if (!this.checkAll2) {
            this.check4 = false
            this.check5 = false
            this.check6 = false
          } else {
            this.message1 = '해당 됩니다'
            this.check4 = true
            this.check5 = true
            this.check6 = true
          }
        } else if (v === '3') {
          if (!this.checkAll3) {
            this.check7 = false
            this.check8 = false
            this.check9 = false
          } else {
            this.message2 = '해당 됩니다'

            this.check7 = true
            this.check8 = true
            this.check9 = true
          }
        }

        if (this.list.length === 0) {
          // console.log(e, , this.data[0].value, v)

          this.list.push(v)
        } else if (this.list.includes(v)) {
          this.list = this.list.filter(val => val !== v)
        } else {
          this.list.push(v)
        }
        console.log(this.list)
      }
    }
  }
</script>

<style scoped></style>
