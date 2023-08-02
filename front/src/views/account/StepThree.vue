<template>
  <div class="container">
    <div class="area">
      <div
        v-for="(item, key) in area"
        :key="key"
        class="card"
        :class="[item.selected ? 'active' : null]"
        v-on:click="checkArea(item.id, item.name)"
      >
        <p>{{ item.name }}</p>
      </div>
    </div>
    <h3>반도체</h3>
    <div class="area">
      <div
        v-for="(item, key) in vandoChae"
        :key="key"
        class="card"
        :class="[item.selected ? 'active' : null]"
        v-on:click="functionVandoChae(item.id, item.name)"
      >
        <p>{{ item.name }}</p>
      </div>
    </div>
    <h3>디스플레이</h3>
    <div class="area">
      <div
        v-for="(item, key) in dislayData"
        :key="key"
        class="card"
        :class="[item.selected ? 'active' : null]"
        v-on:click="functionDisplay(item.id, item.name)"
      >
        <p>{{ item.name }}</p>
      </div>
    </div>
    <h3>기타</h3>
    <div class="area">
      <div
        v-for="(item, key) in etc"
        :key="key"
        class="card"
        :class="[item.selected ? 'active' : null]"
        v-on:click="functionEtc(item.id, item.name)"
      >
        <p>{{ item.name }}</p>
      </div>
    </div>
  </div>
</template>
<script>
  export default {
    components: {},
    data() {
      return {
        number: '',
        selected: [],
        area: [
          { id: 0, name: '서울', selected: false },
          { id: 1, name: '인천', selected: false },
          { id: 2, name: '대구', selected: false },
          { id: 3, name: '충청', selected: false }
        ],
        vandoChae: [
          { id: 0, name: '평택', selected: false },
          { id: 1, name: '이천', selected: false },
          { id: 2, name: '청주', selected: false },
          { id: 3, name: '기흥/화성', selected: false }
        ],
        dislayData: [
          { id: 0, name: '탕정', selected: false },
          { id: 1, name: '파주', selected: false }
        ],
        etc: [{ id: 0, name: '기타', selected: false }]
      }
    },
    watch: {
      selectNumber() {
        return alert('ㅁㅇㄴㄹ')
      }
    },
    methods: {
      onInputNumber(value) {
        this.number = this.$options.filters.numberValidation(value)
      },
      checkArea(id, name) {
        if (this.selected.includes(name)) {
          this.selected = this.selected.filter(inner =>
            console.log(inner !== name)
          )
          this.area[id].selected = !this.area[id].selected
          this.$emit('handleSelected', this.selected)
        } else {
          if (this.selected.length >= 3) {
            this.$dialog.show({
              type: 'alert',
              title: '선택 항목이 최대치입니다.'
            })
            return
          }
          this.selected = [...this.selected, name]
          this.area[id].selected = !this.area[id].selected
          this.$emit('handleSelected', this.selected)
        }
      },
      functionVandoChae(id, name) {
        console.log(this.selected, 'selected')

        if (this.selected.includes(name)) {
          this.selected = this.selected.filter(inner => inner !== name)
          this.vandoChae[id].selected = !this.vandoChae[id].selected
          this.$emit('handleSelected', this.selected)
        } else {
          if (this.selected.length >= 3) {
            this.$dialog.show({
              type: 'alert',
              title: '선택 항목이 최대치입니다.'
            })
            return
          }
          this.selected = [...this.selected, name]
          this.vandoChae[id].selected = !this.vandoChae[id].selected
          this.$emit('handleSelected', this.selected)

          // this.sele
          console.log(name, 'test name')
        }
      },
      functionDisplay(id, name) {
        if (this.selected.includes(name)) {
          this.selected = this.selected.filter(inner => inner !== name)
          this.dislayData[id].selected = !this.dislayData[id].selected
          this.$emit('handleSelected', this.selected)
        } else {
          if (this.selected.length >= 3) {
            this.$dialog.show({
              type: 'alert',
              title: '선택 항목이 최대치입니다.'
            })
            return
          }
          this.selected = [...this.selected, name]
          this.dislayData[id].selected = !this.dislayData[id].selected
          this.$emit('handleSelected', this.selected)

          // this.sele
          console.log(name, 'test name')
        }
      },
      functionEtc(id, name) {
        console.log(this.selected, 'selected')

        if (this.selected.includes(name)) {
          this.selected = this.selected.filter(inner => inner !== name)
          this.etc[id].selected = !this.etc[id].selected
          this.$emit('handleSelected', this.selected)
        } else {
          if (this.selected.length >= 3) {
            this.$dialog.show({
              type: 'alert',
              title: '선택 항목이 최대치입니다.'
            })
            return
          }
          this.selected = [...this.selected, name]
          this.etc[id].selected = !this.etc[id].selected
          this.$emit('handleSelected', this.selected)

          // this.sele
          console.log(name, 'test name')
        }
      }
    }
  }
</script>
