const state = () => ({
  LoadingStatus: false
})
// getters
const getters = {
  LoadingStatus: prev => prev.LoadingStatus
}

// actions
const actions = {
  // setUser(context, data) {
  //   context.commit('setUser', data)
  // },
  // setWallet(context, data) {
  //   context.commit('setWallet', data)
  // },
  // clearUser(context) {
  //   context.commit('clearUser')
  // }
}

// mutations
const mutations = {
  startSpinner(state) {
    state.LoadingStatus = true
  },
  endSpinner(state) {
    state.LoadingStatus = false
  }
}

export default {
  // namespaced: true,
  state,
  getters,
  actions,
  mutations
}
