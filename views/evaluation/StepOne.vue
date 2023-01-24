<template>
  <div class="container">
    <router-link
      :to="{
        name: 'EvaluationTwo',
        query: { siteIdx: this.$route.query.siteIdx, type1: '기술공' }
      }"
    >
      <a class="btn_type tool">기술공</a>
    </router-link>
    <router-link
      :to="{
        name: 'EvaluationTwo',
        query: { siteIdx: this.$route.query.siteIdx, type1: '조공' }
      }"
    >
      <a class="btn_type settings">조공</a>
    </router-link>
    <router-link
      :to="{
        name: 'EvaluationTwo',
        query: { siteIdx: this.$route.query.siteIdx, type1: '중장비' }
      }"
    >
      <a class="btn_type truck">중장비</a>
    </router-link>
  </div>
</template>

<script>
  export default {
    name: 'StepOne'
  }
</script>

<style scoped></style>
