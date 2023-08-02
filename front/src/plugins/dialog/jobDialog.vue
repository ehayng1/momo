<!-- <template>
  <div v-if="isShow">
    <transition name="dialog" @after-leave="afterTransitionLeave">
      <div class="dialog" v-if="visible">
        <div class="dialog_contents">
          <div v-if="data && data.list.length">
            <div v-for="(item, key) in data.list" :key="key">
              
            </div>
          </div>
          <html-view
            v-if="title"
            class="dialog_title"
            v-model="title"
          ></html-view>
          <html-view
            v-if="message"
            class="dialog_message"
            v-model="message"
          ></html-view>

          <div class="dialog_foot">
            <div class="dialog_foot_buttons">
              <button
                type="button"
                class="btn btn_gray"
                v-if="type === 'confirm'"
                @click="onCancel"
              >
                취소
              </button>
              <button type="button" class="btn btn_primary" @click="onConfirm">
                {{ (data && data.confirmText) || '확인' }}
              </button>
            </div>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template> -->

<!-- eslint-disable prettier/prettier -->
<template>
  <div>
    <div class="dialog dialog_main" v-if="isShow">
      <div style="overflowy: scroll; height: 50vh" class="dialog_contents">
        <vueScroll :ops="ops">
          <div class="dialog_body" style="padding: 0 10px">
            <div class="dialog_title">
              <!-- <p>장비 종류</p>  -->
            </div>
            <div 
              class="dialog_check_list" 
              style="display: flex; justify-content: space-between; flex-direction: column;"
            >
              <div
                style="width: 100%; height: 100%"
              >
                <div
                  v-for="(item, key) in data.list"
                  :key="key"
                  class="dialog_check_wrap_2"
                >
                  <div 
                    class="list_bundle" 
                    style="margin-top: 10px; display: flex; justify-content: flex-start; border-bottom: 1px solid #e3e5e5; padding-bottom: 5px"
                    @click="checkItem(item.work_idx)"
                  >
                    <input
                      class="radio_wrap"
                      type="radio"
                      :value="item.work_idx"
                      :id="`${item.work_idx}`"
                      name="selectedItem"
                      v-model="selectedItem"
                      @change="checkItem(item.work_idx)"
                      style="display: block"
                    />
                    <div style="display: flex; flex-direction: column; justify-content:flex-start; align-items: flex-start; font-size: 14px;">
                      <label :for="`${item.work_idx}`">
                        {{ item.work_title }}
                      </label>
                      <br />
                      <label :for="`${item.work_idx}`">
                        {{ item.work_location  }}
                      </label>
                      <br />
                      <label :for="`${item.work_idx}`">
                        {{ item.work_period_start  }} ~ {{ item.work_period_end }}
                      </label>
                    </div>
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
                <button type="button" class="btn btn_gray" @click="onCancel">
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
  export default {
    name: 'jobDialog',
    data() {
      return {
        visible: false,
        eventStatus: null,
        selectedItem: ''
      }
    },
    beforeMount() {
      this.$on('dialog:open', () => {
        this.visible = true
      })
      this.$on('dialog:close', () => {
        this.visible = false
      })
    },
    methods: {
      init() {
        this.$on('dialog:open', () => {
          console.log('오픈하고 실행')
        })
      },
      onConfirm() {
        this.$emit('dialog:close')
        this.eventStatus = 'dialog:confirm'
      },
      onCancel() {
        this.data.selectedItem = ''
        this.selectedItem = ''
        this.$emit('dialog:close')
        this.eventStatus = 'dialog:cancel'
        this.setClose()
      },
      close() {
        this.$dialog.close()
      },
      setClose() {
        this.$dialog.close()
        return this.$emit(this.eventStatus, this.selectedItem)
      },
      check() {
        if (this.selectedItem === '') {
          console.log('선택값 없음')
          return
        }
        this.eventStatus = 'dialog:confirm'
        this.setClose()
      },
      checkItem(v) {
        this.selectedItem = v
        this.data.selectedItem = v
      }
    }
  }
</script>
