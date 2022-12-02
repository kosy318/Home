<template>
  <div @click="move">
      <strong>
        <strong>{{ news.title }}</strong>
      </strong>
      <p class="text-left">{{ news.text }}</p>
  </div>
</template>

<script>
export default {
  name: "CrawlingNewsItem",
  props: {
    news: {},
  },
  methods: {
    move: function () {
      window.open(this.news.titleHref, "_blank");
    },
  },
};
</script>

<style></style>
