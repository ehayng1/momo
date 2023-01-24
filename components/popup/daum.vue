<template>
  <modal
    name="post"
    :scrollable="true"
    :resizable="false"
    :height="'auto'"
    :width="'90%'"
    :minWidth="this.width * 0.9"
    :minHeight="this.height * 0.9"
  >
    <DaumPostcode :on-complete="handleAddress" />
  </modal>
</template>

<script>
  import DaumPostcode from 'vuejs-daum-postcode'

  // var handleAddress = data => {
  //   let fullAddress = data.address
  //   let extraAddress = ''
  //   if (data.addressType === 'R') {
  //     if (data.bname !== '') {
  //       extraAddress += data.bname
  //     }
  //     if (data.buildingName !== '') {
  //       extraAddress +=
  //         extraAddress !== '' ? `, ${data.buildingName}` : data.buildingName
  //     }
  //     fullAddress += extraAddress !== '' ? ` (${extraAddress})` : ''
  //   }

  //   console.log(fullAddress) // e.g. '서울 성동구 왕십리로2길 20 (성수동1가)'
  //   this.$emit('handleAddr', fullAddress)
  // }

  export default {
    name: 'App',
    data() {
      return {
        width: screen.width,
        height: screen.height
      }
    },
    components: {
      DaumPostcode
    },
    methods: {
      handleAddress(data) {
        let fullAddress = data.address
        let extraAddress = ''
        if (data.addressType === 'R') {
          if (data.bname !== '') {
            extraAddress += data.bname
          }
          if (data.buildingName !== '') {
            extraAddress +=
              extraAddress !== '' ? `, ${data.buildingName}` : data.buildingName
          }
          fullAddress += extraAddress !== '' ? ` (${extraAddress})` : ''
        }

        console.log(fullAddress) // e.g. '서울 성동구 왕십리로2길 20 (성수동1가)'
        this.$emit('handleAddr', fullAddress)
      },
      show() {
        this.$modal.show('post')
      },
      hide() {
        this.$modal.hide('post')
      }
    },
    mount() {
      this.show()
    }
  }
</script>
