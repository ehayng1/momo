<template>
  <div>
    <div class="container experience">
      <text-input
        type="number"
        pattern="\d*"
        placeholder="숫자만 입력"
        v-model="number"
        @input="(...args) => this.onInputNumber([2, ...args])"
      />
      <div class="consent_form">
        <input
          type="checkbox"
          :value="checked"
          id="c"
          @change="toggleChecked"
        />
        <label for="c">신입입니다</label>
      </div>
    </div>
    <a class="big_btn" @click="goNext">다음</a>
  </div>
</template>

<script>
  import textInput from '@/components/common/input'

  export default {
    name: 'StepThree',
    components: {
      textInput
    },
    data() {
      return {
        number: '',
        checked: false
      }
    },
    methods: {
      onInputNumber(args) {
        if (args[1].length > args[0]) this.number = args[1].slice(0, args[0])
        else if (this.checked) this.number = 0
        else this.number = args[1]
      },
      toggleChecked() {
        if (this.checked) {
          this.number = ''
        } else {
          this.number = '0'
        }
        this.checked = !this.checked
      },
      goNext() {
        if (this.number.length <= 0) return false
        this.$router.push({
          name: 'EvaluationFour',
          query: {
            siteIdx: this.$route.query.siteIdx,
            type1: this.$route.query.type1,
            type2: this.$route.query.type2,
            year: this.number
          }
        })
      }
    },
    mounted() {
      console.log(this.$route.query)
    }
  }
</script>

<style scoped></style>
