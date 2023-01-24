<template>
  <section style="height: 100vh; display: flex; flex-flow: column">
    <div>
      <fa
        @click="goPage"
        class="fa-3x"
        :icon="['fa', 'xmark']"
        style="margin-bottom: 20px; color: #a7a8a8; cursor: pointer"
      />
    </div>
    <div class="rating_rayout">
      <div class="rating_star">
        <rating :init="4" :maxStars="5" style="margin-bottom: 20px"></rating>
        <h1>일자리는 만족하셨나요?</h1>
      </div>
    </div>
    <div class="rating_footer">
      <div class="rating_box">
        <textarea placeholder="만족스러웠던 점을 편하게 적어주세요." />
      </div>
      <button>신청하기</button>
    </div>
  </section>
</template>

<script>
  import rating from './Rating.vue'
  export default {
    components: { rating },

    methods: {
      goPage() {
        this.$router.push({
          name: 'Main'
        })
      }
    }
  }
</script>
