import dialogTemplate from './dialog.vue'

export default {
  install(Vue) {
    let DialogConstructor = null
    let dialogInstance = null

    Vue.prototype.$dialog = function () {}

    Vue.prototype.$dialog.show = function (options) {
      DialogConstructor = options.template
        ? Vue.extend(options.template)
        : Vue.extend(dialogTemplate)

      if (dialogInstance) {
        dialogInstance.isShow = true
        dialogInstance.type = options.type
        dialogInstance.title = options.title
        dialogInstance.message = options.message
        dialogInstance.data = options.data
        // dialogInstance.$nextTick(() => {
        //   dialogInstance.$emit('dialog:open')
        // })

        dialogInstance.$emit('dialog:open')
        return dialogInstance
      }

      dialogInstance = new DialogConstructor({
        el: document.createElement('div'),
        data() {
          return {
            isShow: true,
            type: options.type,
            title: options.title,
            message: options.message,
            data: options.data
          }
        },
        mounted() {
          this.$nextTick(() => {
            this.$emit('dialog:open')
          })
        }
      })

      document.body.appendChild(dialogInstance.$el)

      return dialogInstance
    }

    Vue.prototype.$dialog.close = function () {
      if (dialogInstance) {
        // dialogInstance.$emit('dialog:close')
        dialogInstance.isShow = false
      }

      DialogConstructor = null
      dialogInstance = null
    }
  }
}
