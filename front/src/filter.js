import Vue from 'vue'

//숫자만.
Vue.filter('numberValidation', value => {
  const regex = /[^0-9]/g
  return value.replace(regex, '')
})

Vue.filter('makeComma', val => {
  console.log(val)
  return val.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')
  // if (isNaN(parseInt(val))) {
  //   return val
  // }
  // return val.toLocaleString('ko-KR')
})

//phone validation
Vue.filter('phone', value => {
  const regex = /01[016789]-[^0][0-9]{2,3}-[0-9]{3,4}/
  return regex.test(value)
})

//핸드폰번호 숫자만 000-0000-0000 , 000-000-0000 형태로으로 리턴
Vue.filter('phoneValidation', value => {
  const regex = /[^0-9]/g
  const number = value.replace(regex, '')
  return number.replace(
    /(^02.{0}|^01.{1}|[0-9]{3})([0-9]+)([0-9]{4})/,
    '$1-$2-$3'
  )
})

//바이트만큼 문자열 자르기
Vue.filter('stringByteLength', (string, limit) => {
  let len = limit
  let str = string
  let l = 0
  for (let i = 0; i < str.length; i++) {
    l += str.charCodeAt(i) > 128 ? 2 : 1
    if (l > len) return str.subString(0, i) + '...'
  }
  return str
})

// 숫자 3자리식 자르기
Vue.filter('autoCommaNumber', value => {
  const str = value.toString()
  return str.replace(/\B(?=(\d{3})+(?!\d))/g, ',')
})

//new Date() 데이터 YYYY-MM-DD 로 변경.
Vue.filter('dateFormat', date => {
  const year = date.getFullYear()
  const month = ('0' + (1 + date.getMonth())).slice(-2)
  const day = ('0' + date.getDate()).slice(-2)
  return `${year}-${month}-${day}`
})
