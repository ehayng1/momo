<template>
  <div class="list">
    <ul>
      <li class="card" v-for="(item, key) in list" :key="key">
        <dl>
          <div class="wrap">
            <dt class="title">{{ item.site_name }}</dt>
            <dd class="star_wrap">
              <!-- <star class="star active" />
              <star class="star active" />
              <star class="star" />
              <star class="star" />
              <star class="star" /> -->
              <star
                v-for="(innerItem, key) in [0, 1, 2, 3, 4]"
                class="star"
                v-bind:class="{ active: item.average >= innerItem + 1 }"
                :key="key"
              />
            </dd>
          </div>
          <dd class="desc">
            {{ item.site_rating_comment }}
          </dd>
          <dd class="date">
            {{ item.timestamp_create }}
            <span class="average">
              종합
              {{ item.average.toFixed(1) }}
            </span>
          </dd>
        </dl>
      </li>
    </ul>
  </div>
</template>

<script>
  import star from '@/static/images/common/star.svg'
  export default {
    name: 'evaluation',
    components: {
      star
    },
    props: {
      list: {
        type: Array
      }
    }
  }
</script>

<style scoped></style>
