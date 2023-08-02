<template>
  <div class="container">
    <div
      :class="['consent_form', this.checkAll ? 'active' : 'unactive']"
      @click.self="toggleCheckAll"
    >
      <input
        type="checkbox"
        v-model="checkAll"
        id="a"
        @change="handleCheck(0)"
      />
      <label for="a">전체 동의하기</label>
    </div>

    <!-- <hr /> -->
    <div class="inputWrapper">
      <div class="consent_form">
        <div>
          <input
            type="checkbox"
            v-model="check1"
            id="b"
            @change="handleCheck(1)"
          />
          <label for="b">(필수) 이용약관 동의</label>
        </div>
        <button type="button" class="more" @click="getTerms(1)">&gt;</button>
      </div>

      <div class="consent_form">
        <div>
          <input
            type="checkbox"
            v-model="check2"
            id="c"
            @change="handleCheck(2)"
          />
          <label for="c">(필수) 개인정보 처리방침 동의</label>
        </div>
        <button type="button" class="more" @click="getTerms(2)">&gt;</button>
      </div>
      <!-- <div v-if="this.$route.query.type === '노무사'" class="consent_form">
        <input type="checkbox" v-model="check4" id="f" @change="handleCheck(4)" />
        <label for="f">(필수) 노무사 개인정보 처리방침 동의</label>
        <button type="button" class="more" @click="getTerms(5)">&gt;</button>
      </div>
      <div v-if="this.$route.query.type === '장비주'" class="consent_form">
        <input type="checkbox" v-model="check5" id="d" @change="handleCheck(5)" />
        <label for="d">(필수) 장비주 개인정보 처리방침 동의</label>
        <button type="button" class="more" @click="getTerms(4)">&gt;</button>
      </div> -->

      <hr />

      <div class="consent_form">
        <div>
          <input
            type="checkbox"
            v-model="check3"
            id="e"
            @change="handleCheck(3)"
          />
          <label for="e">(선택) 마케팅 정보 수신 동의</label>
        </div>
        <button type="button" class="more" @click="getTerms(3)">&gt;</button>
      </div>
    </div>
    <modal :termsContent="termsContent" />
  </div>
</template>
<script>
  import modal from '@/components/popup/modal'
  import api from '@/api/api'

  export default {
    components: { modal },
    data() {
      return {
        number: '',
        checkAll: false,
        check1: false,
        check2: false,
        check3: false,
        check4: false,
        check5: false,
        termsContent: ''
      }
    },
    mounted() {
      console.log(this.$route.query.type, 'params')
      this.$emit('QueryTypes', this.$route.query.type)
    },
    methods: {
      onInputNumber(value) {
        this.number = this.$options.filters.numberValidation(value)
      },
      typeCheckFun() {},
      toggleCheckAll() {
        this.checkAll = !this.checkAll
        this.handleCheck(0)
      },
      handleCheck(v) {
        if (v === 0) {
          if (!this.checkAll) {
            this.check1 = false
            this.check2 = false

            this.check3 = false
            this.check4 = false
            this.check5 = false
          } else {
            this.check1 = true
            this.check2 = true
            this.check3 = true
            this.check4 = true
            this.check5 = true
          }
        }
        if (this.check1 && this.check2 && this.check3) {
          this.checkAll = true
        } else {
          this.checkAll = false
        }
        this.$emit('handleCheck', v)
      },
      async getTerms(v) {
        console.log(v, 'v')
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
      }
    }
  }
</script>
