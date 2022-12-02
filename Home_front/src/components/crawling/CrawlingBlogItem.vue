<template>
  <div @click="move">
    <div class="col-md-4" style="display: flex; justify-content: center; align-items: center">
      <div class="card-image position-relative border-radius-lg">
        <div class="blur-shadow-image">
          <img class="img border-radius-lg" style="width: 70%" :src="blog.imgSrc" alt="architecture" loading="lazy" />
        </div>
      </div>
    </div>
    <div class="col-md-8 my-sm-auto mt-3 ms-sm-3">
      <strong>
        <strong>{{ blog.title }}</strong>
      </strong>
      <p class="text-left">{{ blog.text }}</p>
    </div>
  </div>
</template>

<script>
export default {
  name: "CrawlingBlogItem",
  props: {
    blog: {},
  },
  methods: {
    move: function () {
      window.open(this.blog.titleHref, "_blank");
    },
  },
};
</script>

<style></style>
