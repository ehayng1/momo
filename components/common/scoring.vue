<template>
  <dl class="scoring">
    <dt :class="icon">{{ title }}</dt>
    <dd>
      <span v-for="(index, key) in 5" :key="key">
        <star
          class="star"
          v-bind="$attrs"
          :class="[{ active: index < score }]"
          @click="getActiveStar(index)"
        />
      </span>
    </dd>
  </dl>
</template>

<script>
  import star from '@/static/images/common/big_star.svg'

  export default {
    name: 'scoring',
    components: {
      // star: () => import('@/static/images/common/big_star.svg'),
      star
    },
    props: {
      title: {
        type: String,
        default: ''
      },
      icon: {
        type: String,
        default: ''
      },
      starName: {
        type: String
      }
    },
    data() {
      return {
        score: 0,
        propStarName: this.starName
      }
    },
    methods: {
      getActiveStar(index) {
        this.score = index + 1
        console.log('0101')
        this.updateStar(index)
      },
      updateStar(v) {
        this.$emit('input', v)
        console.log(v)
      }
    },
    mounted() {
      console.log(this.starName)
    }
  }
</script>
