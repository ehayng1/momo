<template>
  <div>
    <div class="dialog dialog_main" v-if="isShow">
      <div style="overflowy: scroll; height: 70vh; bottom: -35%" class="modal">
        <vueScroll :ops="ops">
          <div
            style="
              width: 33vw;
              margin-left: 33vw;
              height: 0.5vh;
              border-radius: 10px;
              margin-bottom: 4rem;
              background-color: #ecf0f5;
            "
          ></div>
          <div class="button_area" style="display: flex; flex-direction: row">
            <a
              :class="[{ unactive: select === 1 }]"
              @click="handleSelect(0)"
              style="margin-right: 3rem"
            >
              <button
                type="button"
                class=""
                style="font-weight: 700; font-size: 2rem"
              >
                지역
              </button>
            </a>
            <div class="space" />
            <a :class="[{ unactive: select === 0 }]" @click="handleSelect(1)">
              <button
                type="button"
                class=""
                style="font-weight: 700; font-size: 2rem"
              >
                업종
              </button>
            </a>
            <button
              type="button"
              style="font-size: 2rem; position: absolute; right: 0px"
              @click="close"
            >
              X
            </button>
          </div>
          <div class="dialog_body">
            <div class="dialog_title">
              <!-- <p>해당 항목에 체크 해주세요</p> -->
            </div>
            <div class="dialog_check_list">
              <div
                v-for="(item, key) in checkList[select].subList"
                :key="key"
                class="dialog_check_wrap_2"
              >
                <!-- <div class="dialog_label">
                  <span class="dialog_divide_line">{{ item.title }}</span>
                </div> -->
                <div
                  style="display: flex; flex-direction: row"
                  class="filter"
                  :class="{
                    selected: list.findIndex(el => el.value === item.title) > -1
                  }"
                >
                  <button @click="addFilter">
                    {{ item.title }}
                  </button>
                  <div style="margin-left: 0.5rem">
                    <!-- {{ Math.floor(Math.random() * 19) }} -->
                    2
                  </div>
                </div>

                <!-- <button class="filter">{{ item.title }} {{item.cnt}</button> -->
              </div>
              <!-- old code -->
              <!-- <div v-for="(item, key) in checkList" :key="key">
                <div class="dialog_label">
                  <span class="dialog_divide_line">{{ item.title }}</span>
                </div>
                <div
                  style="
                    width: 100%;
                    display: grid;
                    grid-template-columns: 1fr 1fr;
                  "
                >
                  <div
                    v-for="(inner, innerKey) in item.subList"
                    :key="innerKey"
                    class="dialog_check_wrap_2"
                  >
                    <div class="list_bundle" style="margin-top: 10px">
                      <input
                        class="radio_wrap"
                        type="radio"
                        :value="inner.value"
                        :id="`${inner.title}${inner.id}`"
                        :name="item.value"
                        @change="checkItem"
                      />
                      <label :for="`${inner.title}${inner.id}`">
                        {{ inner.title }}
                      </label>
                    </div>
                  </div>
                </div>
              </div> -->
            </div>
            <div
              style="
                display: flex;
                flex-direction: row;
                gap: 1rem;
                margin-bottom: 2rem;
                position: absolute;
                bottom: 5rem;
              "
            >
              <div v-for="(item, key) in list" :key="key">
                <button
                  style="padding-top: 0.5rem; padding-bottom: 0.5rem"
                  class="filter btn_primary"
                >
                  {{ item.value }} X
                </button>
              </div>
            </div>
            <!-- {{ list.filter(el => el.name === this.selected) }} -->
            <div
              class="dialog_confirm_bundle"
              style="position: absolute; bottom: 0"
            >
              <button
                type="button"
                class=""
                @click="initialize('초기화')"
                style="
                  opacity: 0.2;
                  border: 0.5px solid #d3d9e0;
                  border-radius: 8px;
                  flex: 0.33;
                  margin-right: 1rem;
                "
              >
                초기화
              </button>
              <button
                type="button"
                style="
                  margin-right: 15px;
                  flex: 0.67;
                  padding: 0.5rem rem;
                  font-size: 2rem;
                  padding-top: 0.25rem;
                  padding-bottom: 0.25rem;
                "
                class="btn_primary"
                @click="check"
              >
                확인
              </button>
            </div>
          </div>
        </vueScroll>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'laborCheckList',
    data() {
      return {
        selected: 'location',
        select: 0,
        eventStatus: null,
        ops: {
          vuescroll: {},
          scrollPanel: {},
          rail: {},
          bar: {
            background: '#00778B',
            opacity: 1,
            keepShow: true
          }
        },
        checkList: [
          {
            id: 0,
            title: '지역',
            value: 'location',
            subList: [
              {
                id: 0,
                title: '서울',
                value: '서울'
              },
              {
                id: 1,
                title: '경기',
                value: '경기'
              },
              {
                id: 2,
                title: '인천',
                value: '인천'
              },
              {
                id: 3,
                title: '평택',
                value: '평택'
              },
              {
                id: 4,
                title: '이천',
                value: '이천'
              },
              {
                id: 5,
                title: '청주',
                value: '청주'
              },
              {
                id: 6,
                title: '기흥/화성',
                value: '기흥/화성'
              },
              {
                id: 7,
                title: '탕정',
                value: '탕정'
              },
              {
                id: 8,
                title: '파주',
                value: '파주'
              },
              {
                id: 9,
                title: '기타',
                value: '기타'
              }
            ]
          },
          {
            id: 1,
            title: '기술직',
            value: 'skill',
            subList: [
              {
                id: 10,
                title: '조공',
                value: '조공'
              },
              {
                id: 11,
                title: '목수(목공)',
                value: '목수(목공)'
              },
              {
                id: 12,
                title: '용접공',
                value: '용접공'
              },
              {
                id: 13,
                title: '설비',
                value: '설비'
              },
              {
                id: 14,
                title: '조경',
                value: '조경'
              },
              {
                id: 15,
                title: '미장공',
                value: '미장공'
              },
              {
                id: 16,
                title: '타일공',
                value: '타일공'
              },
              {
                id: 17,
                title: '석제',
                value: '석제'
              },
              {
                id: 18,
                title: '조적공(쓰미)',
                value: '조적공(쓰미)'
              },
              {
                id: 19,
                title: '콘크리트공(타설)',
                value: '콘크리트공(타설)'
              },
              {
                id: 20,
                title: '방수',
                value: '방수'
              },
              {
                id: 21,
                title: '철근공',
                value: '철근공'
              },
              {
                id: 22,
                title: '전기',
                value: '전기'
              },
              {
                id: 23,
                title: '로프공',
                value: '로프공'
              },
              {
                id: 24,
                title: '도장공(페인트공)',
                value: '도장공(페인트공)'
              },
              {
                id: 25,
                title: '기타(기공)',
                value: '기타(기공)'
              },
              {
                id: 26,
                title: '안전관리자',
                value: '안전관리자'
              },
              {
                id: 27,
                title: '신호수',
                value: '신호수'
              },
              {
                id: 28,
                title: '왓치맨(순찰)',
                value: '왓치맨(순찰)'
              },
              {
                id: 29,
                title: '화기감시자',
                value: '화기감시자'
              },
              {
                id: 30,
                title: '기타',
                value: '기타'
              }
            ]
          }
        ],
        list: []
      }
    },
    beforeMount() {
      this.$on('dialog:close', () => {
        this.setClose()
      })
    },
    mounted() {
      console.log(this.list, 'mounted,this.list checkItem')

      this.init()
    },
    methods: {
      handleSelect(v) {
        console.log(v)
        this.select = v
        this.selected = this.select === 0 ? 'location' : 'skill'
        // this.checkList[this.select].subList
        // this.val1 = 'all'
        // this.val2 = 'all'
        // this.list = []
        // this.pageNo = 1
        // this.isLoading = true
        // this.getList()
      },
      initialize(type) {
        if (type === '초기화') {
          this.$dialog.close()

          this.setClose(type)
        }
      },
      init() {
        this.$on('dialog:open', () => {
          console.log('오픈하고 실행')
        })
      },

      close() {
        this.$dialog.close()
      },
      setClose(type) {
        if (type === '초기화') {
          this.$dialog.close()

          return this.check(type)
        } else {
          this.$dialog.close()

          return this.$emit(this.eventStatus, this.list)
        }
      },
      check(type) {
        this.eventStatus = 'dialog:confirm'

        if (type === '초기화') {
          // this.setClose()

          this.$emit(
            this.eventStatus,
            this.list.push({ name: 'all', value: 'all', type })
          )
        }

        this.setClose()
      },
      addFilter(e) {
        console.log(e)
        const v = e.target.innerText
        // const n = this.select === 0 ? 'location' : 'skill'
        const n = this.selected

        // first filter added
        if (this.list.length === 0) {
          this.list.push({ name: n, value: v })
        }
        // } else {
        //   this.list.map(el => {
        //     // filter does not exist
        //     if (el.name !== n || el.value !== v) {
        //       // push
        //       this.list.append({ name: n, value: v })
        //     }
        //   })
        // }
        else {
          const index = this.list.findIndex(
            object => object.name === n && object.value === v
          )
          // if filter does not exist
          if (index === -1) {
            // push
            if (n === 'location') {
              this.list.unshift({ name: n, value: v })
            } else {
              this.list.push({ name: n, value: v })
            }
          }
        }
        console.log(this.list)
      },
      checkItem(e) {
        console.log(e)
        const v = e.target.value // 서울
        const n = e.target.name // location

        if (this.list.length === 0) {
          this.list.push({ name: n, value: v })
        } else if (this.list.length > 1) {
          this.list.map(val => {
            if (val.name === n) {
              val.value = v
            }

            return this.list.push({ val })
          })
        } else {
          if (this.list[0].name === n) {
            // console.log(this.list[0].name, 'else')
            this.list[0].value = v
          } else {
            this.list.push({ name: n, value: v })
          }
        }
        console.log(this.list)
      }
    }
  }
</script>

<style scoped></style>
