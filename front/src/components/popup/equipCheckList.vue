<!-- eslint-disable prettier/prettier -->
<template>
  <div>
    <div class="dialog dialog_main" v-if="isShow">
      <!-- <div style="overflowy: scroll; height: 50vh" class="dialog_contents"> -->
      <div style="overflowy: scroll;" class="modal">
        <vueScroll :ops="ops">
          <div class="dialog_body" style="padding: 0 10px">
            <div class="dialog_title">
              <p>장비 종류</p>
              <button
                type="button"
                class="btn btn_primary"
                @click="initialize('all')"
              >
                초기화
              </button>
            </div>
            <div class="dialog_check_list">
              <div
                style="
                  width: 100%;
                  display: flex;
                  flex-wrap: wrap;
                  gap: 2%
                "
              >
                <div
                  v-for="(item, key) in data.list"
                  :key="key"
                  class="dialog_check_wrap_2"
                >
                  <!-- <div class="list_bundle" style="margin-top: 10px">
                    <input
                      class="radio_wrap"
                      type="radio"
                      :value="item.value"
                      :id="`${item.value}${key}`"
                      name="sort"
                      @change="checkItem"
                    />
                    <label :for="`${item.value}${key}`">
                      {{ item.text }} {{ item.cnt === undefined ? 0 : item.cnt }}
                    </label>
                  </div> -->
                  <div class="list_bundle" style="margin-top: 10px">
                    <button style="background-color: #EEF1F5; border-radius: 30%; font-size: 12px; 
                    padding: 10px">
                      {{ item.text }} {{ item.cnt === undefined ? 0 : item.cnt }}
                    </button>
                  </div>
                </div>
              </div>
              <div class="dialog_confirm_bundle">
                <button
                  type="button"
                  style="margin-right: 15px"
                  class="btn btn_primary"
                  @click="check"
                >
                  확인
                </button>
                <button type="button" class="btn btn_gray" @click="close">
                  취소
                </button>
              </div>
            </div>
          </div>
        </vueScroll>
      </div>
    </div>
  </div>
</template>

<script>
  // import vuescroll from 'vuescroll'

  export default {
    name: 'laborCheckList',
    // components: { vuescroll },
    data() {
      return {
        eventStatus: null,
        list: [],
        ops: {
          vuescroll: {},
          scrollPanel: {},
          rail: {},
          bar: {
            background: '#00778B',
            opacity: 1,
            keepShow: true
          }
        }
      }
    },
    beforeMount() {
      this.$on('dialog:close', () => {
        this.setClose()
      })
    },
    mounted() {
      this.init()
    },
    methods: {
      initialize(type) {
        if (type === 'all') {
          this.list[0] = 'all'

          this.check()
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
      setClose() {
        this.close()
        return this.$emit(this.eventStatus, this.list[0])
      },
      check() {
        this.eventStatus = 'dialog:confirm'
        this.setClose()
      },
      checkItem(e) {
        const v = e.target.value
        if (this.list.length === 0) {
          this.list.push(v)
        } else {
          this.list[0] = v
        }
      }
    }
  }
</script>

<style scoped></style>
