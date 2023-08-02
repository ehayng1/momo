<template>
  <section class="issue">
    <back />
    <div class="detail">
      <div class="header">
        <!-- <h1 class="title">{{ name }}</h1> -->
        <div class="info">
          <div
            class="img_back"
            v-if="image"
            v-bind:style="{
              backgroundColor:
                image === undefined || image === '' ? '#e3e5e5' : '#FFF'
            }"
          >
            <img
              :src="
                'https://momobuild.s3.ap-northeast-2.amazonaws.com/' + image
              "
              alt=""
              style="
                border-radius: 50%;
                width: auto;
                height: 100%;
                border: none;
                margin: auto;
              "
            />
          </div>
          <div class="img_back" v-else>
            <user class="img" />
          </div>
          <div class="info_content">
            <div class="top">
              <span class="id">
                {{ name }}
              </span>
            </div>
            <div class="bottom">
              <span class="reply_count"> {{ phone }} </span> |
              <span class="date"> {{ career }}년차 </span>
            </div>
          </div>
        </div>
      </div>
      <div class="content">
        {{ content }}
      </div>
      <footer class="footer" ref="footer" style="min-height: 0px">
        <div class="bottom">
          <button
            style="width: 100%"
            type="button"
            @click="callLabor"
            class="btn-login"
          >
            전화걸기
          </button>
        </div>
      </footer>
    </div>
  </section>
</template>
<script>
  import user from '@/static/images/common/user_small.svg'
  import textInput from '@/components/common/inputArea'
  import api from '@/api/api'
  // eslint-disable-next-line no-unused-vars
  import moment from 'moment'
  import back from '@/components/layout/back'

  export default {
    components: {
      // eslint-disable-next-line vue/no-unused-components
      user,
      // eslint-disable-next-line vue/no-unused-components
      textInput,
      // eslint-disable-next-line vue/no-unused-components
      back
    },
    data() {
      return {
        idx: this.$route.query.laborIdx,
        name: '',
        phone: '',
        content: ``,
        career: '',
        image: ''
      }
    },
    methods: {
      async getDetail() {
        try {
          const config = {
            headers: {
              'Content-Type': 'application/json; charset = utf-8',
              'Access-Control-Allow-Origin': '*',
              authorization: `Bearer ${this.$store.getters.token}`
            }
          }
          await api
            .get(`labor/open/detail/${this.$route.query.laborIdx}`, config)
            .then(result => {
              // console.log(result)
              if (result.data.success) {
                if (
                  result.data.data.result !== undefined &&
                  result.data.data.result !== null
                ) {
                  const detail = result.data.data.result
                  this.name = detail.labor_name
                  this.phone = detail.labor_phone
                  this.content = detail.labor_description
                  this.career = detail.labor_career
                  if (
                    detail.labor_image !== undefined &&
                    detail.labor_image !== null
                  ) {
                    this.image = detail.labor_image
                  }
                }
              } else {
                this.$dialog.show({
                  type: 'alert',
                  title: result.data.message
                })
              }
            })
            .catch(err => console.log(err))
        } catch (err) {
          console.log(err)
        }
      },
      callLabor() {
        window.location.href = 'tel://' + self.phone
      }
    },
    mounted() {
      this.getDetail()
    }
  }
</script>
