<template>
  <div class="form-control-container">
    <span
      class="form-control-wrap"
      :class="{
        'form-control-active': active
      }"
    >
      <input
        :ref="$attrs.id"
        v-bind="$attrs"
        v-on="inputListeners"
        v-model="input"
        class="form-control"
        @input="onInput"
        :maxlength="$attrs.maxlength"
        autocomplete="off"
      />
      <slot name="addon"></slot>
    </span>
    <p class="is-invalid-message" v-if="isMessage">
      <slot name="message"></slot>
    </p>
  </div>
</template>

<script>
  export default {
    inheritAttrs: false,
    data() {
      return {
        // input: '',
        active: false
      }
    },
    computed: {
      inputListeners() {
        const vm = this
        return Object.assign({}, this.$listeners, {
          input: function (event) {
            vm.$emit('input', event.target.value)
          }
        })
      },
      isMessage() {
        return this.$slots['message']
      },
      input: {
        get() {
          return this.value
        },
        set(newValue) {
          this.$emit('input', newValue)
          return newValue
        }
      }
    },
    props: {
      value: {
        type: String,
        default: ''
      },
      filters: {
        type: String,
        default: ''
      }
    },
    methods: {
      onInput(event) {
        this.active = event.target.value.length > 0
        this.input = event.target.value
      },
      onfocus() {
        this.$refs[this.$attrs.id].focus()
      }
    }
  }
</script>
