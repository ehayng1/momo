import Vue from 'vue'
import Vuex from 'vuex'
import createPersistedState from 'vuex-persistedstate'
import user from './moduels/user'
import loading from './moduels/loading'

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    user,
    loading
  },
  plugins: [createPersistedState()]
})
