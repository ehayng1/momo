export default {
  path: '/account',
  component: () =>
    import(/* webpackChunkName: "about" */ '../views/account/Index'),
  children: [
    {
      path: '1',
      name: 'AccountStepOne',
      component: () =>
        import(/* webpackChunkName: "about" */ '../views/account/StepOne')
    },
    {
      path: '2',
      name: 'AccountStepTwo',
      component: () =>
        import(/* webpackChunkName: "about" */ '../views/account/StepTwo')
    },
    // {
    //   path: '3',
    //   name: 'AccountStepThree',
    //   component: () =>
    //     import(/* webpackChunkName: "about" */ '../views/account/StepThree')
    // },
    // {
    //   path: '4',
    //   name: 'AccoutService',
    //   component: () =>
    //     import(/* webpackChunkName: "about" */ '../views/account/StepService')
    // },
    // {
    //   path: '5',
    //   name: 'EquipAndNoMuInfo',
    //   component: () =>
    //     import(/* webpackChunkName: "about" */ '../views/account/EquiAndNuMu')
    // },
    // {
    //   path: '6',
    //   name: 'NoMuSa',
    //   component: () =>
    //     import(/* webpackChunkName: "about" */ '../views/account/NoMuSa')
    // },
    {
      path: '3',
      name: 'AccountStepFour',
      component: () =>
        import(/* webpackChunkName: "about" */ '../views/account/StepFour')
    },
    {
      path: '4',
      name: 'AccountNameSetting',
      component: () =>
        import(/* webpackChunkName: "about" */ '../views/account/NameSetting')
    },
    {
      path: '5',
      name: 'AccountNicknameSetting',
      component: () =>
        import(
          /* webpackChunkName: "about" */ '../views/account/NicknameSetting'
        )
    },
    {
      path: '6',
      name: 'AccountStepFive',
      component: () =>
        import(/* webpackChunkName: "about" */ '../views/account/StepFive')
    }
  ]
}
