const state = () => ({
  token: '',
  login: false,
  wallet: '',
  recvcode: ''
})
// getters
const getters = {
  token: prev => prev.token,
  login: prev => prev.login,
  wallet: prev => prev.wallet,
  recvcode: prev => prev.recvcode
}

// actions
const actions = {
  setUser(context, data) {
    context.commit('setUser', data)
  },
  setWallet(context, data) {
    context.commit('setWallet', data)
  },
  clearUser(context) {
    context.commit('clearUser')
  },
  setRecvcode(context) {
    context.commit('setRecvcode')
  }
}

// mutations
const mutations = {
  setUser(state, data) {
    state.token = data
    state.login = true
  },
  setWallet(state, data) {
    state.wallet = data
  },
  setUserCode(state, data) {
    state.wallet = data
  },
  clearUser(state) {
    state.token = ''
    state.login = false
    state.wallet = ''
  },
  recvcode(state) {
    state.recvcode = ''
  }
}

export default {
  // namespaced: true,
  state,
  getters,
  actions,
  mutations
}
