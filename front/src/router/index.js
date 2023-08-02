/* eslint-disable no-unused-vars */
import Vue from 'vue'
import VueRouter from 'vue-router'

import Main from '@/router/main'
import Evaluation from '@/router/evaluation'
import Account from '@/router/account'
import KakaoAccess from '@/router/kakaoAccess'
import NaverAccess from '@/router/naverAccess'

import store from '@/store'

Vue.use(VueRouter)

const routes = [
  Main,
  Evaluation,
  Account,
  KakaoAccess,
  NaverAccess,
  {
    path: '/search',
    name: 'Search',
    component: () => import(/* webpackChunkName: "about" */ '../views/Search')
  },

  {
    path: '/chat',
    name: 'Chat',
    component: () => import(/* webpackChunkName: "about" */ '../views/chat')
  },
  {
    path: '/OpenChat',
    name: 'OpenChat',
    component: () =>
      import(/* webpackChunkName: "about" */ '../views/chat/OpenChat')
  },
  {
    path: '/rating',
    name: 'rating',
    component: () =>
      import(/* webpackChunkName: "about" */ '../views/rating/index')
  },
  {
    path: '/construction',
    name: 'Construction',
    component: () =>
      import(/* webpackChunkName: "about" */ '../views/construction/Index')
  },
  {
    path: '/info',
    name: 'ConstructionInfo',
    component: () =>
      import(/* webpackChunkName: "about" */ '../views/construction/Info')
  },
  {
    path: '/sites',
    name: 'ConstructionSites',
    component: () =>
      import(/* webpackChunkName: "about" */ '../views/construction/Sites')
  },
  {
    path: '/share/:idx',
    name: 'Share',
    component: () =>
      import(/* webpackChunkName: "about" */ '../views/share/Share')
  },
  {
    path: '/share/equip/:idx',
    name: 'EquipShare',
    component: () =>
      import(/* webpackChunkName: "about" */ '../views/share/EquipShare')
  },
  {
    path: '/job/registration',
    name: 'JobRegistration',
    component: () =>
      import(/* webpackChunkName: "about" */ '../views/JobSearch/Registration')
  },
  {
    path: '/job/openingRegistration',
    name: 'OpeningRegistration',
    component: () =>
      import(
        /* webpackChunkName: "about" */ '../views/JobSearch/OpeningRegistration'
      )
  },
  {
    path: '/equipment/rental',
    name: 'Rental',
    component: () =>
      import(/* webpackChunkName: "about" */ '../views/equipment/Rental')
  },
  {
    path: '/equipment/lease',
    name: 'Lease',
    component: () =>
      import(/* webpackChunkName: "about" */ '../views/equipment/Lease')
  },
  {
    path: '/equipment/regist',
    name: 'EquipRegist',
    component: () =>
      import(/* webpackChunkName: "about" */ '../views/equipment/EquipRegist')
  },
  {
    path: '/mypage',
    name: 'Mypage',
    component: () =>
      import(/* webpackChunkName: "about" */ '../views/user/MyPage')
  },
  {
    path: '/mypage/application',
    name: 'Application',
    component: () =>
      import(/* webpackChunkName: "about" */ '../views/user/Application')
  },
  {
    path: '/mypage/work',
    name: 'WorkHistory',
    component: () =>
      import(/* webpackChunkName: "about" */ '../views/user/WorkHistory')
  },
  {
    path: '/mypage/issue',
    name: 'IssueHistory',
    component: () =>
      import(/* webpackChunkName: "about" */ '../views/user/IssueHistory')
  },
  {
    path: '/mypage/like',
    name: 'LikeHistory',
    component: () =>
      import(/* webpackChunkName: "about" */ '../views/user/LikeHistory')
  },
  // {
  //   path: '/mypage/acceptance',
  //   name: 'Acceptance',
  //   component: () =>
  //     import(/* webpackChunkName: "about" */ '../views/user/AcceptanceHistory')
  // },
  {
    path: '/chat/detail',
    name: 'detailChat',
    props: true,
    component: () =>
      import(/* webpackChunkName: "about" */ '../views/chat/detailChat')
  },
  {
    path: '/issue/detail',
    name: 'IssueDetail',
    component: () =>
      import(/* webpackChunkName: "about" */ '../views/issue/IssueDetail')
  },
  // {
  //   path: '/account/41',
  //   name: 'EquipAndNoMuInfo',
  //   component: () =>
  //     import(/* webpackChunkName: "about" */ '../views/account/EquiAndNuMu')
  // },
  // {
  //   path: '/account/42',
  //   name: 'NoMuSa',
  //   component: () =>
  //     import(/* webpackChunkName: "about" */ '../views/account/NoMuSa')
  // },
  {
    path: '/issue/registration',
    name: 'IssueRegistration',
    component: () =>
      import(/* webpackChunkName: "about" */ '../views/issue/IssueRegistration')
  },
  {
    path: '/alarm',
    name: 'Alarm',
    component: () =>
      import(/* webpackChunkName: "about" */ '../views/alarm/Index')
  },
  {
    path: '/labor/detail',
    name: 'LaborDetail',
    component: () =>
      import(/* webpackChunkName: "about" */ '../views/labor/Detail')
  },
  {
    path: '*',
    name: 'Error',
    component: () => import('../views/Error')
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

router.beforeEach((to, from, next) => {
  store.commit('startSpinner')
  setTimeout(() => {
    next()
  }, 1)
})

router.afterEach((to, from) => {
  store.commit('endSpinner')
})

export default router
