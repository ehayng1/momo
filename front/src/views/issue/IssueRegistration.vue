<template>
  <section class="issue">
    <back />
    <div class="info">
      <h1 class="title">게시글 작성</h1>
    </div>

    <form class="form" @submit.prevent="onSubmit">
      <dl class="col">
        <dt>제목</dt>
        <dd>
          <text-input
            type="text"
            placeholder="제목을 입력해주세요."
            v-model="title"
          />
        </dd>
      </dl>
      <dl class="col" style="margin-bottom: 24px">
        <dt>내용</dt>
        <dd class="textarea_wrap">
          <textarea
            style="height: 40vh"
            placeholder="내용을 입력해주세요."
            v-model="content"
          />
        </dd>
      </dl>
      <dl class="col">
        <dt>사진 및 동영상 ({{ this.files.length }} / 5)</dt>
        <div class="preview_container">
          <div
            v-for="(item, key) in this.files"
            style="margin-bottom: 10px"
            :key="key"
            class="preview_wrapper"
          >
            <div
              class="close_btn"
              @click.stop="onDeleteImages"
              :name="item.number"
            >
              x
            </div>
            <div style="position: relative">
              <img
                style=""
                ref="preview"
                :src="item.preview"
                alt=""
                @click="setLead(item.number)"
                @mouseover="setMouseover(item)"
                @mouseleave="setMouseover(item)"
              />
              <span v-if="item.lead === 0">대표 사진</span>
              <span v-if="item.lead === 1 && item.mouse === 1"
                >대표 사진 설정</span
              >
            </div>
          </div>
        </div>
        <dd
          v-if="!this.files.length"
          style="display: flex; flex-direction: column"
        >
          <div class="form-control-wrap file">
            <div style="margin: 0; padding: 0" class="upload_wrap">
              <input
                id="customFile"
                type="file"
                @change="onChangeImages"
                ref="images"
                accept="image/*, video/*"
                hidden
                multiple
                max="5"
              />
              <!-- <label class="custom-file-label" for="customFile">{{
                files.name
              }}</label> -->
              <button
                type="button"
                class="btn-login"
                @click="onClickImageUpload"
              >
                파일 선택
              </button>
            </div>
          </div>
        </dd>
        <dd v-else style="display: flex; flex-direction: column">
          <div class="form-control-wrap file">
            <div style="margin: 0; padding: 0" class="upload_wrap">
              <input
                id="customFile2"
                type="file"
                @change="onAddImages"
                ref="addImages"
                accept="image/*, video/*"
                hidden
                multiple
                max="5"
              />
              <!-- <label class="custom-file-label" for="customFile">{{
                files.name
              }}</label> -->
              <button
                type="button"
                class="btn-login"
                @click="onClickAddImageUpload"
              >
                추가 파일 선택
              </button>
            </div>
          </div>
        </dd>
      </dl>
      <dl class="col">
        <dt>첨부링크</dt>
        <dd>
          <text-input
            type="text"
            placeholder="링크를 입력해주세요."
            v-model="additional"
          />
        </dd>
      </dl>
      <button
        type="submit"
        style="margin-top: 25px"
        class="big_btn submit_write"
      >
        등록
      </button>
    </form>
  </section>
</template>

<script>
  import textInput from '@/components/common/input'
  import api from '@/api/api'
  import back from '@/components/layout/back'

  export default {
    name: 'IssueRegistration',
    components: {
      textInput,
      back
    },
    data() {
      return {
        title: '',
        content: '',
        files: [],
        filesPreview: [],
        uploadImageIndex: 0,
        additional: '',
        mouse: 1
      }
    },
    watch: {},
    methods: {
      async onSubmit() {
        console.log(this.files)
        // if (this.title.trim() === '' || this.content.trim() === '') {
        if (this.content.trim() === '') {
          return false
        }

        if (this.additional.trim() !== '') {
          const extension = [
            '.bmp',
            '.rle',
            '.dib',
            '.jpg',
            '.jpeg',
            '.gif',
            '.png',
            '.tif',
            '.tiff',
            '.raw',
            '.svg',
            '.webp',
            '.mp4',
            '.m4v',
            '.avi',
            '.wmv',
            '.mpg',
            '.mpeg',
            '.mkv',
            '.mov',
            '.3gp',
            '.3g2',
            '.webm'
          ]
          let flag = false
          extension.map(inner => {
            if (this.additional.includes(inner)) {
              flag = true
            }
          })
          if (!flag) {
            return false
          }
        }
        let sortList = this.files
        sortList = sortList.sort((a, b) => {
          if (a.lead > b.lead) return 1
          if (a.lead < b.lead) return -1
          return 0
        })

        console.log(sortList)
        try {
          const config = {
            headers: {
              'Content-Type': 'multipart/form-data',
              'Access-Control-Allow-Origin': '*',
              authorization: `Bearer ${this.$store.getters.token}`
            }
          }

          let images = [
            '.bmp',
            '.rle',
            '.dib',
            '.jpg',
            '.jpeg',
            '.gif',
            '.png',
            '.tif',
            '.tiff',
            '.raw',
            '.svg',
            '.webp'
          ]
          let videos = [
            '.mp4',
            '.m4v',
            '.avi',
            '.wmv',
            '.mpg',
            '.mpeg',
            '.mkv',
            '.mov',
            '.3gp',
            '.3g2',
            '.webm'
          ]
          let fileExt = []

          let formData = new FormData()
          formData.append('issueTitle', this.title)
          formData.append('issueContent', this.content)
          if (sortList.length > 0) {
            for (let i = 0; i < sortList.length; i++) {
              formData.append('issueImage', sortList[i].file)
              let filename = sortList[i].file.name
                .substring(sortList[i].file.name.lastIndexOf('.'))
                .toLowerCase()
              images.map(val => {
                if (filename === val) fileExt.push('0')
              })
              if (fileExt.length === i + 1) continue
              videos.map(val => {
                if (filename === val) fileExt.push('1')
              })
            }
            if (fileExt.length !== sortList.length) {
              this.$dialog.show({
                type: 'alert',
                title: '파일 확장자명을 확인해주세요'
              })
              return false
            }
            formData.append('fileExt', fileExt)
          }
          formData.append('additional', this.additional)

          // const body = {
          //   issueTitle: this.title,
          //   issueContent: this.content,
          //   issueImage: this.files === {} ? '' : this.files
          // }
          await api
            .post(`board/write`, formData, config)
            .then(result => {
              console.log(result)
              if (result.data.success) {
                this.$dialog
                  .show({
                    type: 'alert', //confirm or alert
                    title: '게시글 작성 성공'
                  })
                  .$on('dialog:confirm', () => {
                    this.$router.push('/issue')
                  })
              } else {
                this.$dialog.show({
                  type: 'alert', //confirm or alert
                  title: '게시글 작성 실패'
                })
              }
            })
            .catch(err => console.log(err))
        } catch (err) {
          console.log(err)
        }
      },
      onClickImageUpload() {
        this.$refs.images.click()
      },
      onClickAddImageUpload() {
        this.$refs.addImages.click()
      },
      onChangeImages(e) {
        if (e.target.files.length > 5) {
          this.$dialog.show({
            type: 'alert',
            title: '최대 5개까지 첨부가 가능합니다.'
          })
          return false
        }

        let num = -1
        for (let i = 0; i < e.target.files.length; i++) {
          this.files = [
            ...this.files,
            {
              file: e.target.files[i],
              preview: URL.createObjectURL(e.target.files[i]),
              number: i,
              lead: 1,
              mouse: 0
            }
          ]
          num = i
        }

        this.uploadImageIndex = num + 1
        // if (e.target.files && e.target.files[0]) {
        //   const reader = new FileReader()

        //   reader.onload = e => {
        //     console.log(e)
        //     this.$refs.preview.src = e.target.result
        //   }
        //   reader.readAsDataURL(e.target.files[0])

        //   this.files = e.target.files
        // }
        // this.files.file_name = e.target.files[0].name;

        // let files = e.target.files || e.dataTransfer.files;
        // console.log('files', files)
        // if (!files.length)
        //   return;
        // this.uploadImage(files[0]);

        // const formData = new FormData();

        // if(!this.files.length){
        //   return setfilesErr('대표이미지를 등록해주세요')
        // }
        // [].forEach.call(this.files, (f) => {
        //   formData.append('image', f);
        // });

        // formData.append('review', JSON.stringify({
        //   "productId" : productId,
        //   "visitYear": parseInt(visitYear),
        //   "visitMonth": parseInt(visitMonth),
        //   "productGrade" : grade,
        //   "title": title,
        //   "contents": contents,
        //   "openYn": openYn
        // }))
      },
      removeImage: function (e) {
        console.log(e)
        this.image = {}
      },
      onDeleteImages(e) {
        const name = e.target.getAttribute('name')
        this.files = this.files.filter(val => val.number !== Number(name))
      },
      onAddImages(e) {
        if (this.files.length + e.target.files.length > 5) {
          this.$dialog.show({
            type: 'alert',
            title: '최대 5개까지 첨부가 가능합니다.'
          })
          return false
        }

        let num = -1
        for (let i = 0; i < e.target.files.length; i++) {
          this.files = [
            ...this.files,
            {
              file: e.target.files[i],
              preview: URL.createObjectURL(e.target.files[i]),
              number: i + this.uploadImageIndex,
              lead: 1,
              mouse: 0
            }
          ]
          num = i
        }
        this.uploadImageIndex = this.uploadImageIndex + num + 1
      },
      setLead(v) {
        let list = this.files.map(val => {
          val.lead = 1
          return val
        })
        list.map(val => {
          if (val.number === v) {
            val.lead = 0
          }
          return val
        })
        this.files = list
      },
      setMouseover(v) {
        v.mouse = v.mouse === 0 ? 1 : 0
      }
    }
  }
</script>

<style scoped></style>
