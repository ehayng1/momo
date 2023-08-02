<template>
  <div class="rating">
    <ul class="list">
      <li
        :key="star"
        v-for="star in maxStars"
        :class="{ abc: star <= stars }"
        @click="rate(star)"
        class="star"
      >
        <fa class="fa-2x" :icon="['fa', 'star']" style="margin-bottom: 5px" />
      </li>
    </ul>
  </div>
</template>

<script>
  export default {
    props: {
      init: {
        type: Number,
        required: true
      },
      maxStars: {
        type: Number,
        default: 5
      }
    },
    data() {
      return {
        stars: this.init
      }
    },
    methods: {
      rate(star) {
        if (typeof star === 'number' && star <= this.maxStars && star >= 0)
          this.stars = this.stars === star ? star - 1 : star
        console.log(star)
      }
    }
  }
</script>
