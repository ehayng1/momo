<template>
  <div
    class="select"
    :data-value="selected"
    :class="{ active: visible }"
    style="padding-left: 0.5rem; padding-right: 0.5rem"
  >
    <div class="selector" @click="toggle()">
      <div style="display: flex; flex-direction: row">
        <div class="label">
          <span>{{ selected }}</span>
        </div>
        <!-- <div class="arrow" :class="{ expanded: visible }"></div> -->
        <arrowDown />
      </div>

      <div :class="{ hidden: !visible, visible }">
        <ul>
          <li
            :class="{ current: item === selected }"
            v-for="(item, key) in list"
            @click="select(item)"
            :key="key"
          >
            {{ item.text }}
          </li>
        </ul>
      </div>
    </div>
    <div class="overlay" v-if="visible" @click="toggle()"></div>
  </div>
</template>

<script>
  import arrowDown from '@/static/images/common/Toggle.svg'
  export default {
    components: {
      arrowDown
    },
    name: 'selectbox',
    data() {
      return {
        visible: false,
        selected: ''
      }
    },
    props: {
      text: {
        type: String,
        default: 'Select a FruitSelect a Fruit'
      },
      list: {
        type: Array,
        default: () => {
          return [
            // {
            //   value: 0,
            //   text: '사과'
            // },
            // {
            //   value: 1,
            //   text: '오렌지'
            // },
            // {
            //   value: 2,
            //   text: '귤'
            // },
            // {
            //   value: 3,
            //   text: '망고'
            // }
          ]
        }
      }
    },
    watch: {
      text() {
        this.selected = this.text
      }
    },
    mounted() {
      this.selected = this.text
    },
    methods: {
      toggle() {
        this.visible = !this.visible
      },
      select(option) {
        this.selected = option.text
        this.$emit('change', option)
      }
    }
  }
</script>
