<template>
  <date-range-picker
    v-model="dateRange"
    opens="inline"
    single-date-picker="range"
    :locale-data="localeData"
    :ranges="false"
    :minDate="new Date()"
    @start-selection="changStartDate"
    @finish-selection="changEndDate"
  >
    <!--    header slot-->
    <!--
    <div slot="header" class="slot">
      <h3>Calendar header</h3>
    </div>
-->

    <!--    input slot (new slot syntax)-->

    <template #input="picker" style="min-width: 400px">
      {{ picker.startDate | date }} - {{ picker.endDate | date }}
    </template>

    <!--    date slot-->
    <template #date="data">
      <span class="small">{{ data.date | dateCell }}</span>
    </template>
    <!--    ranges (new slot syntax) -->
    <template #ranges="ranges">
      <div class="ranges">
        <ul>
          <li
            v-for="(range, name) in ranges.ranges"
            :key="name"
            @click="ranges.clickRange(range)"
          >
            <b>{{ name }}</b>
          </li>
        </ul>
      </div>
    </template>

    <!--    footer slot-->
    <div slot="footer" slot-scope="data" class="slot">
      <div><b class="text-black" /> {{ data.rangeText }}</div>
      <!--              <div style="margin-left: auto">
        <a
          @click="data.clickApply"
          v-if="!data.in_selection"
          class="btn btn-primary btn-sm"
          >Choose current</a
        >
      </div>-->
    </div>
  </date-range-picker>
</template>

<script>
  import DateRangePicker from 'vue2-daterange-picker'
  //you need to import the CSS manually
  import 'vue2-daterange-picker/dist/vue2-daterange-picker.css'

  export default {
    components: {
      DateRangePicker
    },
    data() {
      // endDate.setDate(endDate.getDate() + 2)
      return {
        localeData: {
          direction: 'ltr',
          format: 'yyyy/mm/dd',
          separator: ' - ',
          applyLabel: '적용',
          cancelLabel: '취소',
          weekLabel: '주',
          customRangeLabel: 'Custom Range',
          daysOfWeek: ['일', '월', '화', '수', '목', '금', '토'],
          monthNames: [
            '1월',
            '2월',
            '3월',
            '4월',
            '5월',
            '6월',
            '7월',
            '8월',
            '9월',
            '10월',
            '11월',
            '12월'
          ],
          firstDay: 0
        },
        dateRange: {
          startDate: this.start,
          endDate: this.end
        }
      }
    },
    mounted() {},
    computed: {},
    props: ['start', 'end', 'changStartDate', 'changEndDate'],
    filters: {
      dateCell(value) {
        let dt = new Date(value)

        return dt.getDate()
      },
      date(val) {
        return val ? val.toLocaleString() : ''
      }
    },
    methods: {
      onInput(event) {
        this.active = event.target.value.length > 0
        this.input = event.target.value
        this.$emit('input', event.target.value)
      },
      onfocus() {
        this.$refs[this.$attrs.id].focus()
      }
    }
  }
</script>
<style lang="scss">
  @import '~@/static/scss/components/datePicker';
</style>
