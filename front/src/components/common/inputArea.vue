<template>
  <div class="form-control-container">
    <span
      class="form-control-wrap"
      :class="{
        'form-control-active': active
      }"
    >
      <textarea
        class="form-control"
        v-bind="$attrs"
        :ref="$attrs.id"
        :value="value"
        @input="onInput"
        autocomplete="off"
        rows="1"
        style="
          border-radius: 2rem;
          margin-right: 1rem;
          padding-top: 1rem;
          padding-bottom: 1rem;
        "
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
        active: false
      }
    },
    computed: {
      isMessage() {
        return this.$slots['message']
      }
    },
    props: {
      value: {
        type: [String, Number],
        default: ''
      }
    },
    methods: {
      onInput(event) {
        this.active = event.target.value.length > 0
        this.input = event.target.value
        this.$emit('input', event.target.value)
      },
      onfocus() {
        this.$refs[this.$attrs.id].focus()
      }
    }
  }
</script>
