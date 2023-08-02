export default {
  path: '/',
  component: () => import('../views/main/Index'),
  // redirect : ''
  meta: {
    keepAlive: false
  },
  children: [
    {
      path: '/',
      name: 'Main',
      component: () => import('@/views/main/Evaluation')
    },
    {
      path: 'equipment',
      name: 'Equipment',
      component: () => import('@/views/main/Equipment'),
      meta: {
        scroll: false
      }
    },

    {
      path: 'resources',
      name: 'HumanResources',
      component: () => import('@/views/main/HumanResources'),
      meta: {
        scroll: false
      }
    },
    {
      path: 'message',
      name: 'Message',
      component: () => import('@/views/main/Message'),
      meta: {
        scroll: false
      }
    },
    {
      path: 'labor',
      name: 'Labor',
      component: () => import('@/views/main/Labor'),
      meta: {
        scroll: false
      }
    },
    {
      path: 'issue',
      name: 'Issue',
      component: () => import('@/views/main/Issue'),
      meta: {
        scroll: false
      }
    },
    {
      path: 'mypage',
      name: 'Mypage',
      component: () => import('@/views/user/MyPage'),
      meta: {
        scroll: false
      }
    },
    {
      path: 'freeboard',
      name: 'FreeBoard',
      component: () => import('@/views/main/Home/FreeBoard'),
      meta: {
        scroll: false
      }
    },
    {
      path: 'wordboard',
      name: 'WorkBoard',
      component: () => import('@/views/main/Home/WorkBoard'),
      meta: {
        scroll: false
      }
    }

    // 필요시
  ]
}
