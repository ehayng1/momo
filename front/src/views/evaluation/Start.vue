<template>
  <div class="applicaion">
    <div class="container">
      <div class="list">
        <ul>
          <li class="card">
            <div class="image">
              <img
                :src="
                  $route.query.sitePicture === undefined ||
                  $route.query.sitePicture === null
                    ? require('@/static/images/main/site_sample.png')
                    : $route.query.sitePicture
                "
                alt=""
                style="height: 100%"
              />
            </div>
            <dl>
              <dt class="title">
                <img :src="$route.query.companyLogo" alt="" />
                <p>{{ $route.query.siteName }}</p>
              </dt>
              <dd class="address">{{ $route.query.siteAddress }}</dd>
            </dl>
          </li>
        </ul>
      </div>

      <div class="consent_form">
        <input
          type="checkbox"
          :value="checked"
          id="form"
          @change="toggleChecked"
        />
        <label for="form"
          >사실만으로 평가를 할 것에 동의하며, 일방적 비방은 삭제될 수 있음에
          동의합니다.</label
        >
      </div>
    </div>

    <a class="big_btn" @click="goNext">평가시작하기</a>
  </div>
</template>

<script>
  export default {
    name: 'Start',
    mounted() {},
    data() {
      return {
        checked: false
      }
    },
    methods: {
      goNext() {
        if (!this.checked) {
          return false
        } else {
          this.$router.push({
            name: 'EvaluationOne',
            query: { siteIdx: this.$route.query.siteIdx }
          })
        }
      },
      toggleChecked() {
        this.checked = !this.checked
      }
    }
  }
</script>

<style scoped></style>
