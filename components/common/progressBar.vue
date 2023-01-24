<template>
  <div class="progressbar_wrap">
    <progress :value="value" :max="max">
      <strong>Progress : {{ percentage }}%</strong>
    </progress>
  </div>
</template>

<script>
  export default {
    name: 'progressBar',
    props: {
      value: {
        type: Number,
        default: 1
      },
      max: {
        type: Number,
        default: 5
      }
    },
    computed: {
      percentage() {
        return (this.value / this.max) * 100
      }
    }
  }
</script>

<style scoped></style>
