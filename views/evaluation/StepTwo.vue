<template>
  <div class="container">
    <div class="btn_type tool detail">
      기술공
      <div class="sub_types">
        <router-link
          :to="{
            name: 'EvaluationThree',
            query: { siteIdx: $route.query.siteIdx, type1: type1, type2: type }
          }"
          v-for="(type, key) in types"
          :key="key"
          class="btn_sub_type"
        >
          {{ type }}
        </router-link>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'StepTwo',
    data() {
      return {
        types: [
          '전체',
          '목수',
          '조경',
          '미장공',
          '타일공',
          '석제',
          '조적공',
          '콘크리트공',
          '방수',
          '철근공',
          '전기',
          '로프공',
          '도장공',
          '기타'
        ],
        type1: ''
      }
    },
    mounted() {
      console.log(this.$route.params)
      console.log(this.$route.query)
      this.type1 = this.$route.query.type1
    }
  }
</script>

<style scoped></style>
