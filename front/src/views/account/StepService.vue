<template>
  <div class="container">
    <h3>조공</h3>
    <hr />
    <div class="area">
      <div
        class="card"
        v-for="(item, key) in zogong"
        :key="key"
        :class="[item.selected ? 'active' : null]"
        v-on:click="checkFun(item.name, zogong, key, item.selected)"
      >
        {{ item.name }}
      </div>
    </div>
    <h3>기공</h3>
    <hr />
    <div class="gigonArea">
      <div
        class="card"
        :class="[item.selected ? 'active' : null]"
        v-for="(item, key) in gigong"
        :key="key"
        v-on:click="checkFun(item.name, gigong, key)"
      >
        {{ item.name }}
      </div>
    </div>

    <hr />
    <div class="area">
      <div
        class="card"
        v-for="(item, key) in middleService"
        :key="key"
        :class="[item.selected ? 'active' : null]"
        v-on:click="checkFun(item.name, middleService, key)"
      >
        {{ item.name }}
      </div>
    </div>
    <hr />
    <div class="area">
      <div
        class="card"
        v-for="(item, key) in equimentAndNoMusa"
        :key="key"
        v-on:click="checkFun(item.name, equimentAndNoMusa, key, item.id)"
        :class="[item.selected ? 'active' : null]"
      >
        {{ item.name }}
      </div>
    </div>
  </div>
</template>
<script>
  import { service } from './constanst'
  export default {
    components: {},
    data() {
      return {
        serviceArr: [],
        isOpen: false,
        zogong: service.zogong,
        gigong: service.gigong,
        middleService: service.middleService,
        equimentAndNoMusa: service.equimentAndNoMusa
      }
    },
    mounted() {
      // this.gigong = service.gigong
      this.initialize()
    },

    methods: {
      initialize() {
        Object.entries(service).map(index =>
          index[1].map(index => (index.selected = false))
        )
        // this.zogong = service.zogong
        // this.gigong = service.gigong
        // this.middleService = service.middleService
        // this.equimentAndNoMusa = service.equimentAndNoMusa
        this.serviceArr = []

        // this.zogong = service.zogong
        this.$emit('handleService', this.serviceArr)
      },
      checkFun(name, object, key, selected) {
        console.log(this.serviceArr, selected, 'checkFun')

        if (this.serviceArr.includes(name)) {
          // console.log(this.serviceArr.includes(name), '123')

          // console.log('여기탐')
          this.serviceArr = this.serviceArr.filter(inner => inner !== name)
          object[key].selected = !object[key].selected
          this.$emit('handleService', this.serviceArr)
        } else {
          if (this.serviceArr.length >= 1) {
            this.$dialog.show({
              type: 'alert',
              title: '선택 항목이 최대치입니다.'
            })

            return
          }
          this.initialize(key, selected)
          // console.log(object[key].selected, '1234')
          this.serviceArr = [...this.serviceArr, name]
          object[key].selected = !object[key].selected
          this.$emit('handleService', this.serviceArr)
        }
      }
    }
  }
</script>
