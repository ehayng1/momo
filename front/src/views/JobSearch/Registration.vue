<template>
  <section id="job_search">
    <back />
    <div class="info">
      <h1 class="title">일자리 구해요</h1>
    </div>
    <form id="registrationForm" class="form" @submit.prevent="onSubmit">
      <dl class="col">
        <dt>기본 정보<sup>*</sup></dt>
        <!--        <dd><text-input placeholder="숫자만 입력" v-model="textInput"  @input="onInputNumber" /></dd>-->
        <dd>
          <text-input type="text" placeholder="이름" v-model="name" required />
        </dd>
      </dl>
      <div :style="border_style"></div>

      <dl class="col">
        <dd>
          <text-input
            type="tel"
            placeholder="연락처"
            v-model="phone"
            @input="onInputPhoneNumber"
            maxlength="13"
            minlength="12"
            required
          />
        </dd>
      </dl>
      <div :style="border_style"></div>

      <dl class="col">
        <dt>업종</dt>
        <dd>
          <select-box
            :text="
              occupation[0].selected.text === undefined
                ? '대분류'
                : occupation[0].selected.text
            "
            :list="occupation[0].list"
            @change="(...args) => this.changeOccupation([0, ...args])"
          />
          <select-box
            :text="
              occupation[1].selected.text === undefined
                ? '소분류'
                : occupation[1].selected.text
            "
            :list="occupation[0].selected.subList"
            @change="(...args) => this.changeOccupation([1, ...args])"
          />
        </dd>
      </dl>

      <dl style="" class="col">
        <div style="display: flex">
          <dd>
            <text-input type="text" placeholder="업종" required maxlength="0" />
          </dd>
          <div
            style="justify-content: flex-end"
            @click="click('HumanResources')"
          >
            <img :src="require('@/static/images/common/arrow.png')" alt="" />
          </div>
        </div>
      </dl>
      <div :style="border_style"></div>

      <dl style="" class="col">
        <div style="display: flex">
          <dd>
            <text-input
              type="text"
              placeholder="희망지역"
              required
              maxlength="0"
            />
          </dd>
          <div
            style="justify-content: flex-end"
            @click="click('HumanResources')"
          >
            <img :src="require('@/static/images/common/arrow.png')" alt="" />
          </div>
        </div>
      </dl>
      <div :style="border_style"></div>

      <dl class="row">
        <!-- <dt style="flex: 1.5 1 0">개인/팀</dt> -->
        <dd>
          <div style="font-size: 16px; font-weight: 700; flex: 1.5">
            개인/팀
          </div>
          <div class="radio_wrap" style="flex: 1">
            <input
              type="radio"
              id="individual"
              name="personnel"
              value="0"
              hidden
              @change="changeTeam"
              v-model="personnel"
            />
            <label for="individual">개인</label>
          </div>
          <div class="radio_wrap" style="flex: 1">
            <input
              type="radio"
              id="team"
              name="personnel"
              value="1"
              hidden
              v-model="personnel"
            />
            <label for="team">팀</label>
          </div>
          <div>
            <div v-if="personnel == 1" class="number_wrap">
              <dl class="row">
                <dt>팀원 수</dt>
                <dd>
                  <div class="form-control-container">
                    <span class="form-control-wrap number">
                      <button
                        class="left"
                        type="button"
                        @click="member -= 1"
                      ></button>
                      <input
                        class="quantity"
                        min="0"
                        name="quantity"
                        value="1"
                        type="number"
                        style="color: #fff"
                        v-model="member"
                      />
                      <button
                        class="plus right"
                        type="button"
                        @click="member += 1"
                      ></button>
                    </span>
                  </div>
                </dd>
              </dl>
            </div>
          </div>
        </dd>
      </dl>

      <dl class="row">
        <dd style="flex: 1">
          <div style="font-size: 16px; font-weight: 700; flex: 1.5">
            안전교육 미수여부
          </div>
          <div class="radio_wrap" style="flex: 1">
            <input
              type="radio"
              id="complete"
              name="bb"
              value="0"
              hidden
              @change="changeSafeany"
              v-model="safetyEducation"
            />
            <label for="complete">이수</label>
          </div>
          <div class="radio_wrap" style="flex: 1">
            <input
              type="radio"
              id="notComplete"
              name="bb"
              value="1"
              hidden
              @change="changeSafeany"
              v-model="safetyEducation"
            />
            <label for="notComplete">미이수</label>
          </div>
          <!-- <div style="width: 182px; height: 35px"></div> -->
        </dd>
      </dl>

      <dl style="margin: 35px 0 35px 0" class="col">
        <dt>업무 가능 기간<sup>*</sup></dt>
        <dd>
          <date-picker
            :start="date.start"
            :end="date.end"
            :changStartDate="changStartDate"
            :changEndDate="changEndDate"
          />
        </dd>
      </dl>

      <dl style="margin-bottom: 2rem" class="col">
        <dt>기타사항</dt>
        <dd class="textarea_wrap">
          <textarea
            placeholder="기타 사항이 있다면 작성해주세요."
            v-model="etc"
          />
        </dd>
      </dl>

      <!-- <div class="box">
        민간 유료 직업 소개소의 <span class="emphasis">외국인</span> 고용알선은
        <span class="emphasis">불법</span>으로 외국인의 구직등록이
        <span class="emphasis">불가</span>합니다.
      </div> -->

      <!-- <div style="display: flex; align-items: center" class="consent_form">
        <input
          type="checkbox"
          id="form"
          :value="agree"
          @change="onChangeAgree"
        />
        <label for="form" style="width: 100%">
          (필수) 개인정보 수집 및 이용에 동의합니다
        </label>
        <div style="justify-content: flex-end" @click="getTerms(2)">
          <img :src="require('@/static/images/common/arrow.png')" alt="" />
        </div>
      </div> -->

      <div style="display: flex; align-items: center" class="consent_form">
        <input
          type="checkbox"
          id="form"
          :value="agree"
          @change="onChangeArea"
        />
        <label
          for="form"
          style="width: 100%; margin: 1.5rem 0; margin-top: 1.5rem"
        >
          <!-- <span class="">[필수]</span> -->
          (필수) 개인정보 수집 및 이용에 동의합니다
          <!-- <button type="button" @click="getTerms(2)">내용보기</button> -->
        </label>
        <div style="justify-content: flex-end" @click="getTerms(2)">
          <img :src="require('@/static/images/common/arrow.png')" alt="" />
        </div>
      </div>
      <div :style="border_style"></div>

      <div style="margin-top: 3rem" class="dialog_foot_buttons">
        <button
          form="registrationForm"
          type="submit"
          class="btn btn_primary"
          style="width: 100%; font-size: 1.5em; height: 5rem"
        >
          작성완료
        </button>
      </div>

      <!-- <div class="dialog_foot_buttons">
        <button form="registrationForm" type="submit" class="btn btn_primary">
          게시
        </button>
      </div> -->
      <modal :termsContent="termsContent" />
    </form>
  </section>
</template>

<script>
  import textInput from '@/components/common/input'
  import selectBox from '@/components/common/selectbox'
  import datePicker from '@/components/common/datePicker'
  import api from '@/api/api'
  import back from '@/components/layout/back'
  import moment from 'moment'
  import modal from '@/components/popup/modal'
  import job from '@/components/common/job'

  export default {
    name: 'Registration',
    components: {
      textInput,
      selectBox,
      datePicker,
      back,
      modal
    },
    data() {
      return {
        border_style: 'width: 90vw; height: 2px; background-color: #eef1f5;',
        termsContent: '',
        name: '',
        phone: '',
        date: {
          start: new Date(),
          end: new Date()
        },
        occupation: [
          {
            selected: {},
            list: job
          },
          {
            selected: {},
            list: []
          }
        ],
        personnel: 0,
        member: 1,
        safetyEducation: 0,
        etc: '',
        agree: false,
        visibal: false
      }
    },
    created() {
      if (this.$route.params.data !== undefined) {
        const data = this.$route.params.data
        const periodStart = data.work_period_start.split('.')
        const periodEnd = data.work_period_end.split('.')
        this.name = data.work_title
        this.phone = data.work_user_phone
        this.date.start = new Date(
          `${periodStart[0]}-${periodStart[1]}-${periodStart[2]}`
        )
        this.date.end = new Date(
          `${periodEnd[0]}-${periodEnd[1]}-${periodEnd[2]}`
        )
        this.occupation[0].selected = {
          category: data.work_divide_first.split(':')[0],
          text: data.work_divide_first.split(':')[0],
          value: data.work_divide_first.split(':')[0]
        }
        this.occupation[1].selected = {
          category: data.work_divide_second,
          text: data.work_divide_second,
          value: data.work_divide_second
        }
        this.etc = data.work_etc
        this.personnel = data.work_divide_first.includes('개인') ? 0 : 1
        if (data.work_divide_first.includes('팀')) {
          let str = data.work_divide_first.split(':')[1]
          this.member = str.substring(2, str.length - 1) * 1
        }
      }
    },
    mounted() {
      this.getInfo()
    },
    methods: {
      changeSafeany(e) {
        const a = e.target.value

        console.log(a)
      },
      changeTeam(e) {
        const a = e.target.value
        console.log(a)
      },
      onInputPhoneNumber(value) {
        this.phone = this.$options.filters.phoneValidation(value)
      },
      onInputNumber(args) {
        if (args[2].length > args[1])
          this.date[args[0]] = args[2].slice(0, args[1])
        else this.date[args[0]] = args[2]
      },
      changeOccupation(args) {
        this.occupation[args[0]].selected = args[1]
        if (args[0] === 0) {
          this.occupation[1].selected = {}
        }
      },
      onChangeArea() {
        this.agree = !this.agree
      },
      onSubmit() {
        if (!this.agree) {
          this.$dialog.show({
            type: 'alert', //confirm or alert
            title: '동의항목을 확인해주세요'
          })
          return
        } else {
          this.$dialog
            .show({
              type: 'confirm', //confirm or alert
              title: '게시하시겠습니까?',
              data: {
                confirmText: '게시'
              }
            })
            .$on('dialog:confirm', () => {
              this.onInsert()
            })
        }
      },
      cancel() {
        history.go(-1)
      },
      async onInsert() {
        try {
          const config = {
            headers: {
              'Content-Type': 'application/json; charset = utf-8',
              'Access-Control-Allow-Origin': '*',
              authorization: `Bearer ${this.$store.getters.token}`
            }
          }
          if (Number(this.safetyEducation) === 1) {
            return this.$dialog.show({
              type: 'alert', //confirm or alert
              title: '기초안전교육 미이수 시 신청이 불가능합니다'
            })
          } else if (
            this.occupation[0].selected.value === '' ||
            this.occupation[0].selected.value === undefined ||
            this.occupation[1].selected.value === '' ||
            this.occupation[1].selected.value === undefined
          ) {
            return this.$dialog.show({
              type: 'alert',
              title: '직종을 선택해주세요'
            })
          }
          let divideFirst =
            this.occupation[0].selected.value === undefined
              ? ''
              : this.occupation[0].selected.value
          if (divideFirst.length !== 0) {
            divideFirst = divideFirst + ':'
          }
          divideFirst =
            this.personnel === 0
              ? divideFirst + '개인'
              : divideFirst + `팀(${this.member})`
          if (this.safetyEducation === 0)
            divideFirst = divideFirst + ':안전교육이수'

          if (this.name.length > 2) {
            let originName = this.name.split('')
            originName.forEach((name, i) => {
              if (i === 0 || i === originName.length - 1) return
              originName[i] = '*'
            })
            let joinName = originName.join()
            this.name = joinName.replace(/,/g, '')
          } else {
            let pattern = /.$/ // 정규식
            return this.name.replace(pattern, '*')
          }
          const body = {
            name: this.name,
            divideFirst,
            divideSecond:
              this.occupation[1].selected.value === undefined
                ? ''
                : this.occupation[1].selected.value,
            periodStart: moment(this.date.start).format('YYYY-MM-DD'),
            periodEnd: moment(this.date.end).format('YYYY-MM-DD'),
            phone: this.phone,
            etc: this.etc,
            siteIdx:
              this.$route.params.siteIdx === undefined
                ? null
                : this.$route.params.siteIdx
          }
          await api
            .post(`job/add/search`, body, config)
            .then(result => {
              console.log(result)
              if (result.data.success) {
                this.$dialog
                  .show({
                    type: 'alert', //confirm or alert
                    title: '구직 등록 성공'
                  })
                  .$on('dialog:confirm', () => {
                    this.goHome()
                  })
              } else {
                this.$dialog
                  .show({
                    type: 'confirm', //confirm or alert
                    title: '구직 등록 실패',
                    data: {
                      confirmText: '다시 등록'
                    }
                  })
                  .$on('dialog:confirm', () => {
                    this.onInsert()
                    return
                  })
              }
            })
            .catch(err => console.log(err))
        } catch (err) {
          console.log(err)
        }
      },
      goHome() {
        this.$router.push({ name: 'HumanResources', params: { type: '1' } })
      },
      changStartDate(date) {
        this.date.start = date
      },
      changEndDate(date) {
        this.date.end = date
      },
      async getTerms(v) {
        try {
          const config = {
            headers: {
              'Content-Type': 'application/json; charset = utf-8',
              'Access-Control-Allow-Origin': '*',
              authorization: `Bearer ${this.token}`
            }
          }
          await api
            .get(`terms/open/detail/${v}`, config)
            .then(result => {
              console.log(result)
              if (result.data.success) {
                console.log('success')
                if (
                  result.data.data !== undefined &&
                  result.data.data !== null
                ) {
                  this.termsContent = result.data.data.result.term_content
                  console.log(this.termsContent)
                  this.$modal.show('terms')
                }
              } else {
                this.showAlert(result.data.message)
              }
            })
            .catch(err => console.log(err))
        } catch (err) {
          console.log(err)
        }
      },
      async getInfo() {
        if (this.$store.getters.token === '') {
          this.$dialog
            .show({
              type: 'alert',
              title: '로그인정보가 없습니다'
            })
            .$on('dialog:confirm', () => {
              this.$router.push({ name: 'Main' })
            })
        } else {
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
                console.log(result)
                if (result.data.success) {
                  console.log('success')
                  if (
                    result.data.data !== undefined &&
                    result.data.data !== null
                  ) {
                    this.name = result.data.data.result.user_name
                    this.phone = result.data.data.result.user_phone
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
        }
      }
    }
  }
</script>
