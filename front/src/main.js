import Vue from 'vue'
import VueCookies from 'vue-cookies'
import App from './App.vue'
import router from './router'
import Dialog from './plugins/dialog'
import store from './store'
import './plugins/lodash'
import './plugins/moment'
import '@/sanitize'
import '@/filter'
import { TOKEN_FCM, getTokenFcm } from './components/common/token'
import HtmlView from './components/base/htmlView'
import VueClipboard from 'vue-clipboard2'
import VModal from 'vue-js-modal'
import VueGeolocationApi from 'vue-geolocation-api'
import VueVideoPlayer from 'vue-video-player'
import './static/scss/common/video-js.css'
import VueDaumPostcode from 'vue-daum-postcode'
import { library } from '@fortawesome/fontawesome-svg-core'
import vuescroll from 'vuescroll'

import {
  faHouse,
  faBuilding,
  faTools,
  faAddressCard,
  faComment,
  faImage,
  faPlus,
  faCamera,
  faXmark,
  faStar,
  faHeart
} from '@fortawesome/free-solid-svg-icons'
import { faHeart as faAsd } from '@fortawesome/free-regular-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'

VueClipboard.config.autoSetContainer = true // add this line
Vue.use(VueClipboard)
Vue.use(VModal)
Vue.use(Dialog)
Vue.use(VueGeolocationApi)
Vue.use(VueCookies)
Vue.use(VueVideoPlayer)
Vue.use(VueDaumPostcode)
Vue.use(vuescroll, {
  ops: {
    vuescroll: {},
    scrollPanel: {},
    rail: {},
    bar: {
      background: '#00778B',
      opacity: 1,
      keepShow: true
    }
  }
})
Vue.component('html-view', HtmlView)
Vue.config.productionTip = false
Vue.prototype.$token = { TOKEN_FCM, getTokenFcm }
library.add(faHouse)
library.add(faBuilding)
library.add(faTools)
library.add(faAddressCard)
library.add(faComment)
library.add(faImage)
library.add(faPlus)
library.add(faStar)
library.add(faXmark)
library.add(faCamera)
library.add(faHeart)
library.add(faAsd)

Vue.component('fa', FontAwesomeIcon)

var vm = new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')

window['getTokenFcm'] = vm.getTokenFcm
