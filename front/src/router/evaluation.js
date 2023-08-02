export default {
  path: '/evaluation',
  component: () =>
    import(/* webpackChunkName: "about" */ '../views/evaluation/Index'),
  children: [
    {
      path: '/',
      name: 'EvaluationStart',
      component: () =>
        import(/* webpackChunkName: "about" */ '../views/evaluation/Start')
    },
    {
      path: '1',
      name: 'EvaluationOne',
      component: () =>
        import(/* webpackChunkName: "about" */ '../views/evaluation/StepOne')
    },
    {
      path: '2',
      name: 'EvaluationTwo',
      component: () =>
        import(/* webpackChunkName: "about" */ '../views/evaluation/StepTwo')
    },
    {
      path: '3',
      name: 'EvaluationThree',
      component: () =>
        import(/* webpackChunkName: "about" */ '../views/evaluation/StepThree')
    },
    {
      path: '4',
      name: 'EvaluationFour',
      component: () =>
        import(/* webpackChunkName: "about" */ '../views/evaluation/StepFour')
    }
  ]
}
