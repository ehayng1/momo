<template>
  <section id="evaluation">
    <div v-if="step.value === 0">
      <close />
    </div>
    <div v-else>
      <back />
    </div>
    <progress-bar
      :max="step.max"
      :value="step.value"
      :style="{ visibility: step.value !== 0 ? 'visible' : 'hidden' }"
    />

    <div class="info">
      <h1 class="title">{{ info[step.value].title }}</h1>
      <p>{{ info[step.value].desc }}</p>
    </div>

    <router-view />
  </section>
</template>

<script>
  import progressBar from '@/components/common/progressBar'
  import back from '@/components/layout/back'
  import close from '@/components/layout/close'

  export default {
    name: 'Evaluation',
    components: {
      progressBar,
      back,
      close
    },
    data() {
      return {
        step: {
          max: 4,
          value: 0
        },
        info: [
          {
            id: 1,
            title: '현장 평가하기',
            desc: ''
            // link : {
            //   title : '완료',
            //   name : 'AccountStepThree'
            // }
          },
          {
            id: 2,
            title: '업무 유형',
            desc: '현장에서 어떤 업무를 하셨나요?'
          },
          {
            id: 3,
            title: '세부 업무 유형',
            desc: '조금더 자세한 업무 분류를 나누어주세요.'
          },
          {
            id: 4,
            title: '업무 경력',
            desc: '해당 현장 근무 당시 경력이 어떻게 되시나요?'
          },
          {
            id: 5,
            title: '현장 평가',
            desc: '각각의 항목 별로 5점 만점에 몇점에 해당하는지 선택해주세요.'
          }
        ]
      }
    },
    watch: {
      $route(v) {
        this.moveNext(v.path)
      }
    },
    mounted() {
      this.moveNext(this.$route.path)
    },
    methods: {
      moveNext(number) {
        let result = Number(this.$options.filters.numberValidation(number))
        if (typeof result === 'number' && this.step.max >= result) {
          this.step.value = result
        } else {
          this.step.value = 0
        }
      }
    }
  }
</script>

<style scoped></style>
